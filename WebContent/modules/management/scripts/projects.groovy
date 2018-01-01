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
   
   def showProjects(){
       def connection = getConnection()
       def projects = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select p.id,p.subject,p.date,p.status,p.progression, u.name from projects p, users u where p.user_id = u.id and p.structure_id = ? ", [id], { row -> 
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
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def template = getProjectTemplate(project)
	   def mail = new Mail("Mamadou Lamine Ba","lamine.ba@thinktech.sn","Projet : ${project.subject}",template)
	   //mailSender.sendMail(mail) 
	   println project
	   def connection = getConnection()
	   def user = session.getAttribute("user")
	   def params = ["Projet : " +project.subject,template,user.id,user.structure.id]
       connection.executeInsert 'insert into messages(subject,message,user_id,structure_id) values (?, ?, ?, ?)', params
	   params = [project.subject,project.service,project.plan, project.description,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into projects(subject,service,plan,description,user_id,structure_id) values (?, ?, ?,?,?,?)', params
	   connection.close()
	   response.writer.write(json([id: result[0][0]]))
	}
	
	def getProjectInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def project = connection.firstRow("select p.*, u.name from projects p,users u where p.id = ? and p.user_id = u.id", [id])
	   if(project.subject.length()>40) project.subject = project.subject.substring(0,40)+"..."
	   project.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.date)
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
	   connection.eachRow("select d.id, d.name, d.date, u.name as author from documents d, users u where d.createdBy = u.id and d.project_id = ?", [project.id],{ row -> 
          def document = new Expando()
          document.id = row.id
          document.author = row.author
          document.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          document.name = row.name
          project.documents << document
       })
	   connection.close() 
	   response.writer.write(json([entity : project]))
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
	   def name = getParameter("name");
	   response.contentType = servletContext.getMimeType(name)
	   response.setHeader("Content-disposition","attachment; filename=$name");
	   def fileManager = new FileManager()
	   fileManager.download(name,response.outputStream)
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
		def db = [url:'jdbc:mysql://localhost/thinktech', user:'root', password:'thinktech', driver:'com.mysql.jdbc.Driver']
        Sql.newInstance(db.url, db.user, db.password, db.driver)
	}
	
}

new ModuleAction()