import org.metamorphosis.core.ActionSupport
import org.metamorphosis.core.Mail
import org.metamorphosis.core.MailConfig
import org.metamorphosis.core.MailSender
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.MarkupTemplateEngine
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper
import groovy.sql.Sql


class ModuleAction extends ActionSupport {

	def login() {
	   def user = new JsonSlurper().parse(request.inputStream) 
	   def connection = getConnection()
	   user = connection.firstRow("select * from users where email = ? and password = ?", [user.email,user.password])
	   if(user) {
	    user.structure = connection.firstRow("select * from structures where id = ?", [user.structure_id])
        session.setAttribute("user",user)
	   	response.writer.write(json([url: request.contextPath+"/dashboard"]))
	   }else{
	    response.writer.write(json([status : 1]))
	   }
	   connection.close()
	}
	
	def changePassword() {
	   def user = new JsonSlurper().parse(request.inputStream)
	   def connection = getConnection()
	   connection.executeUpdate 'update users set password = ? where id = ?', [user.password,session.getAttribute("user").id] 
	   connection.close()
	   response.writer.write(json([status: 1]))
	}
	
	def recoverPassword() {
	   def user = new JsonSlurper().parse(request.inputStream)
	   def connection = getConnection()
	   user = connection.firstRow("select * from users where email = ?", [user.email])
	   if(user){
	    def alphabet = (('A'..'N')+('P'..'Z')+('a'..'k')+('m'..'z')+('2'..'9')).join()  
 		def n = 15 
 		user.password = new Random().with { (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join() }
        user.structure = connection.firstRow("select * from structures where id = ?", [user.structure_id])
	   	connection.executeUpdate 'update users set password = ? where email = ?', [user.password,user.email]
	   	def template = getPasswordTemplate(user) 
	    def params = ["Réinitialisation de votre mot de passe",template,user.id,user.structure.id]
       	connection.executeInsert 'insert into messages(subject,message,user_id,structure_id) values (?, ?, ?, ?)', params
       	connection.close()
	   	def mailConfig = new MailConfig(context.getInitParameter("smtp.email"),context.getInitParameter("smtp.password"),"smtp.thinktech.sn")
	   	def mailSender = new MailSender(mailConfig)
	   	def mail = new Mail("$user.name","$user.email","Réinitialisation de votre mot de passe",template)
	   	mailSender.sendMail(mail)
	   	response.writer.write(json([status: 1]))
	   }else {
	   	response.writer.write(json([status: 0]))
	   }
	}
	
	def updateProfil() {
	   def user = new JsonSlurper().parse(request.inputStream)
	   def connection = getConnection()
	   connection.executeUpdate 'update users set name = ?, email = ?, profession = ?, telephone = ?  where id = ?', [user.name,user.email,user.profession,user.telephone,session.getAttribute("user").id]
	   connection.executeUpdate 'update structures set name = ?, business = ?, ninea = ? where id = ?', [user.structure.name,user.structure.business,user.structure.ninea,session.getAttribute("user").structure.id]
	   user = connection.firstRow("select * from users where id = ?", [session.getAttribute("user").id])
	   user.structure = connection.firstRow("select * from structures where id = ?", [user.structure_id])
       session.setAttribute("user",user) 
	   connection.close() 
	   response.writer.write(json([status: 1]))
	}
	
	def logout() {
	    session.invalidate()
		response.sendRedirect(request.contextPath+"/")
	}
	
	def subscribe() {
       response.addHeader("Access-Control-Allow-Origin", "*");
       response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
       if(request.method == "POST") { 
          def subscription = new JsonSlurper().parse(request.inputStream)
	      def connection = getConnection()
	      def user = connection.firstRow("select * from users where email = ?", [subscription.email])
	      if(user) {
		    response.writer.write(json([status : 0]))
	      }else{
	        def params = [subscription.structure]
            def result = connection.executeInsert 'insert into structures(name) values (?)', params
            def structure_id = result[0][0]
	        params = [subscription.name,subscription.email,subscription.password,"administrateur",true,structure_id]
            result = connection.executeInsert 'insert into users(name,email,password,role,owner,structure_id) values (?, ?, ?,?,?,?)', params
            def user_id = result[0][0]
            def template = getSubscriptionTemplate(subscription)
            params = ["Projet : " +subscription.subject,template,user_id,structure_id]
       		connection.executeInsert 'insert into messages(subject,message,user_id,structure_id) values (?, ?, ?, ?)', params
	   		params = [subscription.subject,"web dev",subscription.plan,user_id,structure_id]
       		result = connection.executeInsert 'insert into projects(subject,service,plan,user_id,structure_id) values (?, ?, ?,?,?,)', params
       		def id = result[0][0]
       		def bill = createBill(subscription)
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
	        def mailConfig = new MailConfig("info@thinktech.sn","qW#^csufU8","smtp.thinktech.sn")
		    def mailSender = new MailSender(mailConfig)
		    def mail = new Mail(subscription.contact,subscription.email,"${subscription.contact}, confirmer votre souscription au ${subscription.plan}",template)
		    mailSender.sendMail(mail)
		    response.writer.write(json([status : 1]))
	      }
	     connection.close()
       }
    }
    
    def createBill(subscription){
	   def bill = new Expando()
	   bill.fee = "caution"
	   if(subscription.plan == "plan business") {
	      bill.amount = 20000 * 3
	   }else if(subscription.plan == "plan corporate") {
	      bill.amount = 15000 * 3
	   }else if(subscription.plan == "plan personal") {
	      bill.amount = 10000 * 3
	   }
	   bill
	}
    
    def getSubscriptionTemplate(subscription) {
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
	
	def getPasswordTemplate(user) {
	    TemplateConfiguration config = new TemplateConfiguration()
		MarkupTemplateEngine engine = new MarkupTemplateEngine(config)
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
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
		      p("Mot de passe : <b>$user.password</b>")
		      br()
		      p("Vous pouvez le modifier en vous connectant &aacute; <a href='$url'>votre compte</a>")
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
	
	def getConnection()  {
		def ds =  new javax.naming.InitialContext().lookup("java:comp/env/jdbc/db");
		new Sql(ds)
	}
	
}

new ModuleAction()