import org.metamorphosis.core.ActionSupport
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper
import app.FileManager

class ModuleAction extends ActionSupport {

   def String execute(){
       SUCCESS
   }
   
   def showProjects(){
       request.setAttribute("total",6)
       request.setAttribute("unactive",3)
       request.setAttribute("active",1)
       def projects = []
       def project = new Expando(id : 1,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "in progress",progression : '50')
       projects << project
       project = new Expando(id : 2,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "finished",progression : '100')
       projects << project
       project = new Expando(id : 3,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "stand by",progression : '0')
       projects << project
       project = new Expando(id : 4,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "stand by",progression : '0')
       projects << project
       project = new Expando(id : 5,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "stand by",progression : '0')
       projects << project
       project = new Expando(id : 6,subject: 'cr&edot;ation site web',plan : 'plan business',date : "17/09/2017",status : "stand by",progression : '0')
       projects << project
       session.setAttribute("projects",projects)
       SUCCESS
   }

   def createProject() {
	   def project = new JsonSlurper().parse(request.inputStream) 
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def mail = new Mail("Mamadou Lamine Ba","lamine.ba@thinktech.sn","Projet : ${project.subject}",getProjectTemplate(project))
	   mailSender.sendMail(mail)
	   response.writer.write(json([status: 1]))
	}
	
	def getProjectInfo() {
	   def projects = session.getAttribute("projects")
	   def id = getParameter("id") as int
	   def project = projects[id-1] 
	   response.writer.write(json([entity : project]))
	}
	
	def generateContract(project) {
	   def folder = module.folder.absolutePath
	   Thread.start{
	      def manager = new FileManager()
	      def file = new File(folder + "/billing.jsp");
	      def stream = new FileInputStream(file)
	      manager.upload(file.name,stream)
	      stream.close()
	   }
	}
	
	def addComment() {
	   def comment = new JsonSlurper().parse(request.inputStream) 
	   response.writer.write(json([status: 1]))
	}
	
	def updateProjectDescription() {
	   def project = new JsonSlurper().parse(request.inputStream)
	   response.writer.write(json([status: 1]))
	}
	
	def showTickets(){
	   request.setAttribute("total",6)
       request.setAttribute("unsolved",5)
       def tickets = []
       def ticket = new Expando(id : "1",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "in progress",progression : '50')
       tickets << ticket
       ticket = new Expando(id : "2",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "finished",progression : '100')
       tickets << ticket
       ticket = new Expando(id : "3",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "stand by",progression : '0')
       tickets << ticket
       ticket = new Expando(id : "4",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "stand by",progression : '0')
       tickets << ticket
       ticket = new Expando(id : "5",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "stand by",progression : '0')
       tickets << ticket
       ticket = new Expando(id : "6",subject: 'site web down',service : 'site web',author : 'Malorum',date : "17/09/2017",status : "stand by",progression : '0')
       tickets << ticket
       session.setAttribute("tickets",tickets)
       SUCCESS
    }
	
	def createTicket() {
	   def ticket = new JsonSlurper().parse(request.inputStream) 
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def mail = new Mail("Mamadou Lamine Ba","lamine.ba@thinktech.sn","Ticket : ${ticket.subject}",getTicketTemplate(ticket))
	   mailSender.sendMail(mail)
	   response.writer.write(json([status: 1]))
	}
	
	def getTicketInfo() {
	   def tickets = session.getAttribute("tickets")
	   def id = getParameter("id") as int
	   def ticket = tickets[id-1] 
	   response.writer.write(json([entity : ticket]))
	}
	
	def addTicketMessage() {
	   def ticket = new JsonSlurper().parse(request.inputStream) 
	   response.writer.write(json([status: 1]))
	}
	
	def showMessages(){
	   request.setAttribute("total",6)
       request.setAttribute("read",0)
       def messages = []
       def message = new Expando(id : "1",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "2",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "3",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "4",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "5",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       message = new Expando(id : "6",subject: 'cr&edot;ation site web',author : 'ThinkTech',date : "17/09/2017")
       messages << message
       session.setAttribute("messages",messages)
       SUCCESS
    }
    
    def getMessageInfo() {
	   def messages = session.getAttribute("messages")
	   def id = getParameter("id") as int
	   def message = messages[id-1] 
	   response.writer.write(json([entity : message]))
	}
    
    def showBills(){
       request.setAttribute("total",6)
       request.setAttribute("unpayed",4)
       def bills = []
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
		 div(style : "background:#fafafa;padding-bottom:16px;padding-top: 25px"){
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
		 div(style : "background:#fafafa;padding-bottom:16px;padding-top: 25px"){
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
	
}

new ModuleAction()