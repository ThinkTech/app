import groovy.text.markup.MarkupTemplateEngine
import groovy.sql.Sql

class ModuleAction extends ActionSupport {
	
	def showTickets(){
       def connection = getConnection()
       def tickets = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select t.id,t.subject,t.message,t.date,t.service,t.status,t.progression, u.name from tickets t, users u where t.user_id = u.id and t.structure_id = ? ", [id], { row -> 
          def ticket = new Expando()
          ticket.id = row.id
          ticket.author =  row.name
          ticket.subject = row.subject
          ticket.message = row.message
          ticket.date = row.date
          ticket.service = row.service
          ticket.status = row.status
          ticket.progression = row.progression
          tickets << ticket
       })
       def unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = "+id).num
       connection.close() 
       request.setAttribute("tickets",tickets)  
       request.setAttribute("total",tickets.size())
       request.setAttribute("unsolved",unsolved)
       SUCCESS
    }
	
	def createTicket() {
	   def ticket = parse(request) 
	   def template = getUserTicketTemplate(ticket)
	   def connection = getConnection()
	   def user = session.getAttribute("user")
	   def params = [ticket.subject,ticket.service,ticket.message,ticket.priority,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into tickets(subject,service,message,priority,user_id,structure_id) values (?, ?, ?, ?,?,?)', params
	   connection.close()
	   ticket.user = user
	   def mailConfig = new MailConfig(context.getInitParameter("smtp.email"),context.getInitParameter("smtp.password"),"smtp.thinktech.sn")
	   def mailSender = new MailSender(mailConfig)
	   def mail = new Mail("support@thinktech.sn","support@thinktech.sn","Ticket : ${ticket.subject}",getSupportTicketTemplate(ticket))
	   mailSender.sendMail(mail)
	   json([id: result[0][0]])
	}
	
	def getTicketInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def ticket = connection.firstRow("select t.*, u.name from tickets t,users u where t.id = ? and t.user_id = u.id", [id])
	   if(ticket.subject.length()>40) ticket.subject = ticket.subject.substring(0,40)+"..."
	   ticket.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.date)
	   if(ticket.closedOn) {
	   	ticket.closedOn = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.closedOn)
	   	def user = connection.firstRow("select u.name from users u, tickets t where u.id = t.closedBy and t.id = ?", [id])
	    ticket.closedBy = user.name
	    response.setHeader("Cache-control", "private, max-age=78840000")
	   }
	   ticket.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name from tickets_comments c, users u where c.createdBy = u.id and c.ticket_id = ?", [ticket.id],{ row -> 
          def comment = new Expando()
          comment.id = row.id
          comment.author = row.name
          comment.date = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          comment.message = row.message
          ticket.comments << comment
       })
	   connection.close()
	   json([entity : ticket])
	}
	
	def addTicketComment() {
	   def comment = parse(request)
	   def user_id = session.getAttribute("user").id
	   Thread.start {
	     def connection = getConnection()
	     def params = [comment.message,comment.ticket,user_id]
         connection.executeInsert 'insert into tickets_comments(message,ticket_id,createdBy) values (?,?,?)', params
	     connection.close()
	   } 
	   json([status: 1])
	}
	
	def updateTicketPriority(){
	    def ticket = parse(request) 
	    Thread.start {
	   	   def connection = getConnection()
	       connection.executeUpdate "update tickets set priority = ? where id = ?", [ticket.priority,ticket.id] 
	       connection.close()
	    }
		json([status: 1])
	}
	
	def closeTicket() {
	   def ticket = parse(request)
	   def user_id = session.getAttribute("user").id 
	   Thread.start {
	      def connection = getConnection()
	      connection.executeUpdate "update tickets set progression = 100, status = 'finished', closedOn = NOW(), closedBy = ? where id = ?", [user_id,ticket.id] 
	      connection.close()
	   }
	   json([status : 1])
	}
	
	def getUserTicketTemplate(ticket) {
	    MarkupTemplateEngine engine = new MarkupTemplateEngine()
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-top:2%;height:100px;text-align:center;background:#05d2ff") {
		      h4(style : "font-size: 200%;color: #fff;margin: 3px") {
		        span("Nouveau Ticket")
		      }
		      p(style : "font-size:150%;color:#fff"){
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
	
	def getSupportTicketTemplate(ticket) {
	    MarkupTemplateEngine engine = new MarkupTemplateEngine()
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-top:2%;height:100px;text-align:center;background:#05d2ff") {
		      h4(style : "font-size: 200%;color: #fff;margin: 3px") {
		        span("Nouveau Ticket")
		      }
		       p(style : "font-size:150%;color:#fff"){
		         span("ce ticket a &edot;t&edot; bien cr&edot;&edot;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Service : $ticket.service")
		      }
		      if(ticket.user.structure.name){
		      	h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		        	 span("Structure : $ticket.user.structure.name")
		      	}
		      }
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Client : $ticket.user.name")
		      }
		      h5(style : "font-size: 120%;color: rgb(0, 0, 0);margin-bottom: 15px") {
		         span("Message")
		      }
		      p("$ticket.message")
		      br()
		    }
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que $ticket.user.name")
		      p("a cr&edot;&edot; un ticket en utilisant cette adresse")
		  }
		  
		   
		 }
		'''
		def template = engine.createTemplate(text).make([ticket:ticket,url : baseUrl])
		template.toString()
	}
	
	def getConnection()  {
		new Sql(dataSource)
		
	}
	
}

new ModuleAction()