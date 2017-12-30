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
   def fonction
   def role
   def structure
}

class Structure {
   def name
   def ninea
}

class ModuleAction extends ActionSupport {

   def ModuleAction() {
       def user = new User(id : 1,name : "Malorum", email : "malorum@gmail.com",role : "administrateur",fonction : "CEO",telephone : "776154520")
       user.structure = new Structure(name : "Sesame",ninea : 1454554)
       session.setAttribute("user",user)
       request.setAttribute("projects_count",6)
       request.setAttribute("bills_count",4)
       def messages = []
       def message = new Expando(id : "1",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "2",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "3",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       request.setAttribute("inbox",messages)
       request.setAttribute("messages_count",messages.size())
   }
   
   def showProjects(){
       request.setAttribute("total",6)
       request.setAttribute("unactive",3)
       request.setAttribute("active",1)
       def projects = session.getAttribute("projects")
       if(!projects) {
           projects = []    
	       def project = new Expando(id : 1,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '3',status : "in progress",progression : 50)
	       project.tasks = []
	       def task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 2", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 3", progression : 100)
	       project.tasks << task
	       projects << project
	       project = new Expando(id : 2,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '4',status : "finished",progression : 100)
	       project.tasks = []
	       task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       projects << project
	       project = new Expando(id : 3,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '3',status : "stand by",progression : 0)
	       project.tasks = []
	       task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       projects << project
	       project = new Expando(id : 4,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '3',status : "stand by",progression : 0)
	       project.tasks = []
	       task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       projects << project
	       project = new Expando(id : 5,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '3',status : "stand by",progression : 0)
	       project.tasks = []
	       task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       projects << project
	       project = new Expando(id : 6,subject: 'cr&edot;ation site web',description : '<p>cr&edot;ation site web e-commerce</p>',plan : 'plan business',date : "17/09/2017",duration : '3',status : "stand by",progression : 0)
	       project.tasks = []
	       task = new Expando(name : "contrat et caution", progression : 100)
	       project.tasks << task
	       task = new Expando(name : "item 1", progression : 100)
	       project.tasks << task
	       projects << project
	       session.setAttribute("projects",projects)
       }
       SUCCESS
   }

   def createProject() {
	   def project = new JsonSlurper().parse(request.inputStream) 
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def template = getProjectTemplate(project)
	   def mail = new Mail("Mamadou Lamine Ba","lamine.ba@thinktech.sn","Projet : ${project.subject}",template)
	   //mailSender.sendMail(mail) 
	   createBill()
	   def connection = getConnection()
	   def params = [project.subject,template,session.getAttribute("user").id]
       connection.executeInsert 'insert into messages(subject,message,user_id) values (?, ?, ?)', params
	   connection.close()
	   response.writer.write(json([id: 1]))
	}
	
	def createBill(){
	   showBills()
	   def bills = session.getAttribute("bills")
	   def bill = new Expando(id : bills.size()+1,fee: 'caution',service : 'site web',amount : '60 000',date : "17/09/2017",status : "stand by")
	   bills << bill
	}
	
	def getProjectInfo() {
	   def projects = session.getAttribute("projects")
	   if(!projects) showProjects()
	   projects = session.getAttribute("projects")
	   def id = getParameter("id") as int
	   def project = projects[id-1] 
	   response.writer.write(json([entity : project]))
	}
	
	def addComment() {
	   def comment = new JsonSlurper().parse(request.inputStream) 
	   def id = comment.project as int
	   def projects = session.getAttribute("projects")
	   def project = projects[id-1]
	   if(!project.comments) project.comments = []
	   project.comments << comment
	   response.writer.write(json([status: 1]))
	}
	
	def saveDocuments() {
	   def upload = new JsonSlurper().parse(request.inputStream) 
	   def id = upload.id as int
	   def projects = session.getAttribute("projects")
	   def project = projects[id-1]
	   if(!project.documents) project.documents = []
	   project.documents = project.documents + upload.documents
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
	   def projects = session.getAttribute("projects")
	   def id = project.id as int
	   projects[id-1].description = project.description
	   response.writer.write(json([status: 1]))
	}
	
	def showTickets(){
       def connection = getConnection()
       def tickets = []
       connection.eachRow("select t.id,t.subject,t.message,t.date,t.service,t.status,t.progression, u.firstName, u.lastName from tickets t, users u where t.createdBy = u.id", { row -> 
          def ticket = new Expando()
          ticket.id = row.id
          ticket.author =  row.firstName + " " + row.lastName
          ticket.subject = row.subject
          ticket.message = row.message
          ticket.date = row.date
          ticket.service = row.service
          ticket.status = row.status
          ticket.progression = row.progression
          tickets << ticket
       })
       def unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished'").num
       connection.close() 
       request.setAttribute("tickets",tickets)  
       request.setAttribute("total",tickets.size())
       request.setAttribute("unsolved",unsolved)
       SUCCESS
    }
	
	def createTicket() {
	   def ticket = new JsonSlurper().parse(request.inputStream) 
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def template = getTicketTemplate(ticket)
	   def mail = new Mail("Mamadou Lamine Ba","lamine.ba@thinktech.sn","Ticket : ${ticket.subject}",template)
	   // mailSender.sendMail(mail)
	   def connection = getConnection()
	   def params = [ticket.subject,template,session.getAttribute("user").id]
       connection.executeInsert 'insert into messages(subject,message,user_id) values (?, ?, ?)', params
	   params = [ticket.subject,ticket.service,ticket.message,ticket.priority,session.getAttribute("user").id]
       def result = connection.executeInsert 'insert into tickets(subject,service,message,priority,createdBy) values (?, ?, ?, ?,?)', params
	   connection.close()
	   response.writer.write(json([id: result[0][0]]))
	}
	
	def getTicketInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def ticket = connection.firstRow("select t.*, u.firstName, u.lastName from tickets t,users u where t.id = ? and t.createdBy = u.id", [id])
	   if(ticket.subject.length()>40) ticket.subject = ticket.subject.substring(0,40)+"..."
	   ticket.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.date)
	   ticket.comments = []
	   connection.eachRow("select id, message from tickets_comments where ticket_id = ?", [ticket.id],{ row -> 
          def comment = new Expando()
          comment.id = row.id
          comment.message = row.message
          ticket.comments << comment
       })
	   connection.close()
	   response.writer.write(json([entity : ticket]))
	}
	
	def addTicketMessage() {
	   def comment = new JsonSlurper().parse(request.inputStream) 
	   def connection = getConnection()
	   def params = [comment.message,comment.ticket,session.getAttribute("user").id]
       connection.executeInsert 'insert into tickets_comments(message,ticket_id,createdBy) values (?,?,?)', params
	   connection.close()
	   response.writer.write(json([status: 1]))
	}
	
	def showMessages(){
	   def connection = getConnection()
       def messages = []
       def id = session.getAttribute("user").id
       connection.eachRow("select m.id,m.subject,m.message,m.date,m.unread from messages m where m.user_id = ?",[id], { row -> 
          def message = new Expando()
          message.id = row.id
          message.subject = row.subject
          message.date = row.date
          message.unread = row.unread
          messages << message
       })
       def unread = connection.firstRow("select count(*) AS num from messages where unread = true").num
       connection.close() 
       request.setAttribute("messages",messages)  
       request.setAttribute("total",messages.size())
       request.setAttribute("unread",unread)
       SUCCESS
    }
    
    def getMessageInfo() {
	   def id = getParameter("id") as int
	   def connection = getConnection()
	   def message = connection.firstRow("select * from messages where id = ?", [id])
	   if(message.subject.length()>40) message.subject = message.subject.substring(0,40)+"..."
	   message.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(message.date)
	   connection.executeUpdate 'update messages set unread = false where id = ?', [id] 
	   connection.close()
	   response.writer.write(json([entity : message]))
	}
    
    def showBills(){
       request.setAttribute("total",6)
       request.setAttribute("unpayed",4)
       def bills = session.getAttribute("bills")
       if(!bills) {
	       bills = []
	       def bill = new Expando(id : 1,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "finished")
	       bills << bill
	       bill = new Expando(id : 2,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "finished")
	       bills << bill
	       bill = new Expando(id : 3,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "stand by")
	       bills << bill
	       bill = new Expando(id : 4,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "stand by")
	       bills << bill
	       bill = new Expando(id : 5,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "stand by")
	       bills << bill
	       bill = new Expando(id : 6,fee: 'h&edot;bergement',service : 'site web',amount : '20 000',date : "17/09/2017",status : "stand by")
	       bills << bill
	       session.setAttribute("bills",bills)
       }
       SUCCESS
    }
    
    def getBillInfo() {
	   def bills = session.getAttribute("bills")
	   def id = getParameter("id") as int
	   def bill = bills[id-1] 
	   response.writer.write(json([entity : bill]))
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
	
	
	def getTicketTemplate(ticket) {
	    TemplateConfiguration config = new TemplateConfiguration()
		MarkupTemplateEngine engine = new MarkupTemplateEngine(config)
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding:10px;height:90px;text-align:center;background:#eee") {
		      h4(style : "font-size: 200%;color: rgb(0, 0, 0);margin: 3px") {
		        span("Nouveau Ticket")
		      }
		      p(style : "font-size:150%;color:rgb(100,100,100)"){
		         span("votre ticket a &edot;t&edot; bien cr&edot;&edot;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Service : $ticket.service")
		      }
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Message")
		      }
		      p("$ticket.message")
		      br()
		      p("Votre ticket est en attente de traitement.")
		    }
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que vous (ou quelqu'un utilisant cet email)")
		      p("a cr&edot;&edot; un ticket en utilisant cette adresse")
		  }
		  
		   
		 }
		'''
		def template = engine.createTemplate(text).make([ticket:ticket,url : baseUrl])
		template.toString()
	}
	
	def getConnection()  {
		def db = [url:'jdbc:mysql://localhost/thinktech', user:'root', password:'thinktech', driver:'com.mysql.jdbc.Driver']
        Sql.newInstance(db.url, db.user, db.password, db.driver)
	}
	
}

new ModuleAction()