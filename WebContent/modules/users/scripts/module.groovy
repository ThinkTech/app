import org.metamorphosis.core.ActionSupport
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper

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
       request.setAttribute("user",user)
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

	def login() {
	    def user = new JsonSlurper().parse(request.inputStream) 
		def url = request.contextPath+"/dashboard"
		response.writer.write(json([url: url]))
	}
	
	def changePassword() {
	   def user = new JsonSlurper().parse(request.inputStream) 
	   response.writer.write(json([status: 1]))
	}
	
	def recoverPassword() {
	   def user = new JsonSlurper().parse(request.inputStream)
	   def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def mail = new Mail("Mamadou Lamine Ba","$user.email","Réinitialisation de votre mot de passe",getPasswordTemplate(user))
	   mailSender.sendMail(mail) 
	   response.writer.write(json([status: 1]))
	}
	
	def updateProfil() {
	   response.writer.write(json([status: 1]))
	}
	
	def logout() {
	    session.invalidate()
		def url = request.contextPath+"/"
		response.sendRedirect(url)
	}
	
	def getPasswordTemplate(user) {
	    TemplateConfiguration config = new TemplateConfiguration()
		MarkupTemplateEngine engine = new MarkupTemplateEngine(config)
		def text = '''\
		 div(style : "background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding:10px;height:90px;text-align:center;background:#eee") {
		      h4(style : "font-size: 200%;color: rgb(0, 0, 0);margin: 3px") {
		        span("R&edot;initialisation de votre mot de passe")
		      }
		      p(style : "font-size:150%;color:rgb(100,100,100)"){
		         span("r&edot;initialisation reussie")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      p("Votre mot de passe a &edot;t&edot; r&edot;initialis&edot;")
		      br()
		      p("Mot de passe : <b>12455444444</b>")
		      br()
		      p("Vous pouvez le modifier ensuite en vous connectant &aacute; <a href='$url'>votre compte</a>")
		    }
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que vous (ou quelqu'un utilisant cet email)")
		      p("a envoy&edot; une demande en utilisant cette adresse")
		  }
		  
		   
		 }
		'''
		def template = engine.createTemplate(text).make([user:user,url : baseUrl])
		template.toString()
	}
	
}

new ModuleAction()