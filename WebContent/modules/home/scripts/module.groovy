import org.metamorphosis.core.ActionSupport 
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper

class ModuleAction extends ActionSupport {
 
    def subscribe() {
       response.addHeader("Access-Control-Allow-Origin", "*");
       response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
       if(request.method == "POST") {
         def subscription = new JsonSlurper().parse(request.inputStream)
         def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
		 def mailSender = new MailSender(mailConfig)
		 def mail = new Mail(subscription.contact,subscription.email,"${subscription.contact}, confirmer votre souscription au ${subscription.plan}",getSubscriptionTemplate(subscription))
		 mailSender.sendMail(mail)
		 response.writer.write(json([status: 1]))
       }
    }
    
    def getSubscriptionTemplate(subscription) {
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
		         span("cliquer sur le bouton en bas pour confirmation")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      if(subscription.structure) {
		        h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Structure : $subscription.structure")
		        }
		      }
		      p("Merci pour votre souscription au ${subscription.plan}")
		      p("Veuillez confirmer votre projet pour son traitement.")
		    }
		    div(style : "text-align:center;margin-bottom:10px") {
		       a(href : "$url/registration/confirm",style : "font-size:150%;width:180px;margin:auto;text-decoration:none;background: #05d2ff;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
		         span("Confirmer")
		       }
		    }
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que vous (ou quelqu'un utilisant cet email)")
		      p("a cr&edot;&edot; un projet en utilisant cette adresse")
		  }
		  
		   
		 }
		'''
		def template = engine.createTemplate(text).make([subscription:subscription,url : baseUrl])
		template.toString()
	}
       
}

new ModuleAction()