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
     	
   def showMessages(){
	   def connection = getConnection()
       def messages = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select m.id,m.subject,m.message,m.date,m.unread,u.name from messages m, users u where m.structure_id = ? and m.user_id = u.id",[id], { row -> 
          def message = new Expando()
          message.id = row.id
          message.subject = row.subject
          message.date = row.date
          message.user = row.name
          message.unread = row.unread
          messages << message
       })
       def unread = connection.firstRow("select count(*) AS num from messages where unread = true and structure_id = "+id).num
       connection.close() 
       request.setAttribute("messages",messages)  
       request.setAttribute("total",messages.size())
       request.setAttribute("unread",unread)
       SUCCESS
    }
    
    def getMessageInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def message = connection.firstRow("select m.*, u.name from messages m, users u where m.user_id=u.id and m.id = ?", [id])
	   if(message.subject.length()>40) message.subject = message.subject.substring(0,40)+"..."
	   message.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(message.date)
	   connection.executeUpdate 'update messages set unread = false where id = ?', [id] 
	   connection.close()
	   response.writer.write(json([entity : message]))
	}
    
	def showServices(){
	   request.setAttribute("total",1)
       request.setAttribute("subscribed",1)
       def services = []
       def service = new Expando(name : 'site web',icon : 'siteweb-service.png')
       services << service
       request.setAttribute("services",services)
       SUCCESS
    }
			
	def getConnection()  {
		def db = [url:'jdbc:mysql://localhost/thinktech', user:'root', password:'thinktech', driver:'com.mysql.jdbc.Driver']
        Sql.newInstance(db.url, db.user, db.password, db.driver)
	}
	
}

new ModuleAction()