import org.metamorphosis.core.ActionSupport
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper
import app.FileManager
import groovy.sql.Sql

class ModuleAction extends ActionSupport {

   def showProjects(){
       def connection = getConnection()
       def projects = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select p.id,p.subject,p.date,p.status,p.progression,u.name from projects p, users u where p.user_id = u.id and p.structure_id = ? ", [id], { row -> 
          def project = new Expando()
          project.id = row.id
          project.author =  row.name
          project.subject = row.subject
          project.date = row.date
          project.status = row.status
          project.progression = row.progression
          projects << project
       })
       def active = connection.firstRow("select count(*) AS num from projects where status = 'in progress' and structure_id = "+id).num
       def unactive = connection.firstRow("select count(*) AS num from projects where status = 'stand by' and structure_id = "+id).num
       connection.close() 
       request.setAttribute("projects",projects)  
       request.setAttribute("total",projects.size())
       request.setAttribute("active",active)
       request.setAttribute("unactive",unactive)
       SUCCESS
   }

   def createProject() {
	   def project = new JsonSlurper().parse(request.inputStream) 
	   def template = getProjectTemplate(project)
	   def connection = getConnection()
	   def user = session.getAttribute("user")
	   def params = ["Projet : " +project.subject,template,user.id,user.structure.id]
       connection.executeInsert 'insert into messages(subject,message,user_id,structure_id) values (?, ?, ?, ?)', params
	   params = [project.subject,project.priority,project.service,project.plan, project.description,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into projects(subject,priority,service,plan,description,user_id,structure_id) values (?, ?, ?,?,?,?,?)', params
       def id = result[0][0]
       def bill = createBill(project)
       if(bill.amount){
          params = [bill.fee,bill.amount,id]
       	  connection.executeInsert 'insert into bills(fee,amount,project_id) values (?,?,?)', params
       	  def query = 'insert into projects_tasks(task_id,project_id) values (?, ?)'
      	  connection.withBatch(query){ ps ->
           9.times{
              ps.addBatch(it+1,id)
           } 
          }
       }else{
          def query = 'insert into projects_tasks(task_id,project_id) values (?, ?)'
      	  connection.withBatch(query){ ps ->
          9.times{
              if(it!=0) ps.addBatch(it+1,id)
          }
         }
       }
	   connection.close()
	   def mailConfig = new MailConfig(context.getInitParameter("smtp.email"),context.getInitParameter("smtp.password"),"smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def mail = new Mail("$user.name","$user.email","Projet : ${project.subject}",template)
	   mailSender.sendMail(mail) 
	   response.writer.write(json([id: id]))
	}
	
	def createBill(project){
	   def bill = new Expando()
	   if(project.service == "web dev") {
	      bill.fee = "caution"
	      if(project.plan == "plan business") {
	         bill.amount = 20000 * 3
	      }else if(project.plan == "plan corporate") {
	         bill.amount = 15000 * 3
	      }else if(project.plan == "plan personal") {
	         bill.amount = 10000 * 3
	      }
	   }
	   bill
	}
	
	def getProjectInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def project = connection.firstRow("select p.*,u.name from projects p,users u where p.id = ? and p.user_id = u.id", [id])
	   project.end = connection.firstRow("select date_add(date,interval duration month) as end from projects where id = ?", [id]).end
	   if(project.subject.length()>40) project.subject = project.subject.substring(0,40)+"..."
	   project.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.date)
	   project.end = new java.text.SimpleDateFormat("dd/MM/yyyy").format(project.end)
	   project.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name from projects_comments c, users u where c.createdBy = u.id and c.project_id = ?", [project.id],{ row -> 
          def comment = new Expando()
          comment.id = row.id
          comment.author = row.name
          comment.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          comment.message = row.message
          project.comments << comment
       })
       project.documents = []
	   connection.eachRow("select d.project_id, d.name, d.date, u.name as author from documents d, users u where d.createdBy = u.id and d.project_id = ?", [project.id],{ row -> 
          def document = new Expando()
          document.project_id = row.project_id
          document.author = row.author
          document.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          document.name = row.name
          project.documents << document
       })
       project.tasks = []
	   connection.eachRow("select t.name,t.description, p.status, p.progression from tasks t, projects_tasks p where t.id = p.task_id and p.project_id = ?", [project.id],{ row -> 
          def task = new Expando()
          task.name = row.name
          task.description = row.description
          task.status = row.status
          task.progression = row.progression
          project.tasks << task
       })
       if(project.status == "stand by" && project.plan != "plan social") {
         project.bill = connection.firstRow("select b.*,p.service from bills b, projects p where b.project_id = p.id and p.id = ?", [id])
	  	 project.bill.date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(project.bill.date)
       }
	   connection.close() 
	   response.writer.write(json([entity : project]))
	}
	
	def getProjectBill() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
       def bill = connection.firstRow("select b.*,p.service from bills b, projects p where b.project_id = p.id and p.id = ?", [id])
	   bill.date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(bill.date)
	   response.writer.write(json([entity : bill]))
	   connection.close()
	}
	
	def addComment() {
	   def comment = new JsonSlurper().parse(request.inputStream) 
	   def connection = getConnection()
	   def params = [comment.message,comment.project,session.getAttribute("user").id]
       connection.executeInsert 'insert into projects_comments(message,project_id,createdBy) values (?,?,?)', params
	   connection.close()
	   response.writer.write(json([status: 1]))
	}
	
	def saveDocuments() {
	   def upload = new JsonSlurper().parse(request.inputStream) 
	   def id = upload.id as int
	   def connection = getConnection()
	   def query = 'insert into documents(name,project_id,createdBy) values (?,?,?)'
       connection.withBatch(query){ ps ->
           for(def document : upload.documents){
              ps.addBatch(document.name,id,session.getAttribute("user").id)
           }
       }
	   connection.close()
	   response.writer.write(json([status: 1]))
	}
	
	def downloadDocument(){
	   def user = session.getAttribute("user")
	   def dir = "structure_"+user.structure.id+"/"+"project_"+getParameter("project_id")
	   def name = getParameter("name")
	   response.contentType = servletContext.getMimeType(name)
	   response.setHeader("Content-disposition","attachment; filename=$name")
	   def fileManager = new FileManager()
	   fileManager.download(dir+"/"+name,response.outputStream)
	}
	
	def updateProjectDescription() {
	   def project = new JsonSlurper().parse(request.inputStream)
	   def connection = getConnection()
	   connection.executeUpdate "update projects set description = ? where id = ?", [project.description,project.id] 
	   connection.close()
	   response.writer.write(json([status: 1]))
	}
		
	def getProjectTemplate(project) {
	    TemplateConfiguration config = new TemplateConfiguration()
		MarkupTemplateEngine engine = new MarkupTemplateEngine(config)
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding:10px;height:90px;text-align:center;background:#eee") {
		      h4(style : "font-size: 200%;color: rgb(0, 0, 0);margin: 3px") {
		        span("Souscription reussie")
		      }
		      p(style : "font-size:150%;color:rgb(100,100,100)"){
		         span("votre projet a &edot;t&edot; bien cr&edot;&edot;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      if(project.structure) {
		        h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Structure : $project.structure")
		        }
		      }
		      p("Merci pour votre souscription au ${project.plan}")
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Description du projet")
		      }
		      p("$project.description")
		      br()
		      p("Votre projet est en attente de traitement.")
		    }
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que vous (ou quelqu'un utilisant cet email)")
		      p("a cr&edot;&edot; un projet en utilisant cette adresse")
		  }
		  
		   
		 }
		'''
		def template = engine.createTemplate(text).make([project:project,url : baseUrl])
		template.toString()
	}
	
	def getConnection()  {
		def ds =  new javax.naming.InitialContext().lookup("java:comp/env/jdbc/db");
		new Sql(ds)
	}
	
}

new ModuleAction()