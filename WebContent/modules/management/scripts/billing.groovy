import org.metamorphosis.core.ActionSupport
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper
import groovy.sql.Sql

class User {
   def id
   def name
   def email
   def telephone
   def profession
   def role
   def structure
}

class Structure {
   def id
   def name
   def ninea
}

class ModuleAction extends ActionSupport {

   def ModuleAction() {
       def user = new User(id : 1,name : "Malorum Diaz", email : "malorum@gmail.com",role : "administrateur",profession : "CEO",telephone : "776154520")
       user.structure = new Structure(id : 1,name : "Sesame",ninea : 1454554)
       session.setAttribute("user",user)
       request.setAttribute("projects_count",6)
       request.setAttribute("bills_count",4)
       request.setAttribute("messages_count",3)
   }
     
   def showBills(){
       def connection = getConnection()
       def bills = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select b.id,b.fee,b.amount,b.date,b.status,p.service from bills b,projects p where b.project_id = p.id and p.structure_id = ?",[id], { row -> 
          def bill = new Expando()
          bill.id = row.id
          bill.fee = row.fee
          bill.amount = row.amount
          bill.date = row.date
          bill.status = row.status
          bill.service = row.service
          bills << bill
       })
       def unpayed = connection.firstRow("select count(*) AS num from bills b, projects p where b.project_id = p.id and p.structure_id = "+id).num
       connection.close() 
       request.setAttribute("bills",bills)  
       request.setAttribute("total",bills.size())
       request.setAttribute("unpayed",unpayed)
       SUCCESS
    }
    
    def getBillInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def bill = connection.firstRow("select b.*,p.subject,p.service from bills b, projects p where b.project_id = p.id and b.id = ?", [id])
	   bill.date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(bill.date)
	   connection.close()
	   response.writer.write(json([entity : bill]))
	}
	
	def getConnection()  {
		def db = [url:'jdbc:mysql://localhost/thinktech', user:'root', password:'thinktech', driver:'com.mysql.jdbc.Driver']
        Sql.newInstance(db.url, db.user, db.password, db.driver)
	}
	
}

new ModuleAction()