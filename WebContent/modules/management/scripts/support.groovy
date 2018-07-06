class ModuleAction extends ActionSupport {
	
	def showTickets(){
       def connection = getConnection()
       def tickets = connection.rows("select t.id,t.subject,t.message,t.date,t.service,t.status,t.progression, u.name as author from tickets t, users u where t.user_id = u.id and t.structure_id = ? order by t.date DESC", [user.structure.id])
       def unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = $user.structure.id").num
       def solved = connection.firstRow("select count(*) AS num from tickets where status = 'finished' and structure_id = $user.structure.id").num
       connection.close() 
       request.setAttribute("tickets",tickets)  
       request.setAttribute("total",tickets.size())
       request.setAttribute("solved",solved)
       request.setAttribute("unsolved",unsolved)
       SUCCESS
    }
	
	def createTicket() {
	   def ticket = parse(request) 
	   def connection = getConnection()
	   def params = [ticket.subject,ticket.service,ticket.message,ticket.priority,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into tickets(subject,service,message,priority,user_id,structure_id) values (?, ?, ?, ?,?,?)', params
	   sendMail("ThinkTech Support","support@thinktech.sn","Nouveau Ticket : ${ticket.subject}",getTicketTemplate(user,ticket))
	   json([id: result[0][0]])
	   connection.close()
	}
	
	def getTicketInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def ticket = connection.firstRow("select t.*, u.name from tickets t,users u where t.id = ? and t.user_id = u.id", [id])
	   ticket.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.date)
	   if(ticket.status == "in progress" || ticket.status == "finished"){
	     	ticket.startedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.startedOn)                                                      
	   }
	   if(ticket.closedOn) {
	   	ticket.closedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.closedOn)
	   	def user = connection.firstRow("select u.name from users u, tickets t where u.id = t.closedBy and t.id = ?", [id])
	    ticket.closedBy = user.name
	   }
	   ticket.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name as author, u.type from tickets_comments c, users u where c.createdBy = u.id and c.ticket_id = ?", [ticket.id],{ row -> 
          def comment = row.toRowResult()
          comment.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(comment.date)
          comment.icon = comment.type == 'customer' ? 'user' : 'address-book'
          ticket.comments << comment
       })
	   connection.close()
	   json([entity : ticket])
	}
	
	def addTicketComment() {
	   def comment = parse(request)
	   def connection = getConnection()
	   def params = [comment.message,comment.ticket,user.id]
       connection.executeInsert 'insert into tickets_comments(message,ticket_id,createdBy) values (?,?,?)', params
       def subject = connection.firstRow("select subject from tickets  where id = ?", [comment.ticket]).subject
       sendMail("ThinkTech Support","support@thinktech.sn","Ticket : ${subject}",getCommentTemplate(user,comment))
	   connection.close()
	   json([status: 1])
	}
	
	def getTicketTemplate(user,ticket) {
		MarkupTemplateEngine engine = new MarkupTemplateEngine()
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-bottom:2%;padding-top:2%;text-align:center;background:#05d2ff") {
		      h4(style : "font-size: 120%;color: #fff;margin: 3px") {
		        span("Nouveau ticket pour assistance cr&eacute;&eacute;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		     h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-bottom: 0px") {
		         span("Auteur : $user.name")
		     }
		     p("$ticket.message")

		    }
		    div(style : "text-align:center;margin-top:30px;margin-bottom:10px") {
			    a(href : "$url/dashboard/support",style : "font-size:130%;width:140px;margin:auto;text-decoration:none;background: #05d2ff;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
			        span("Voir")
			    }
			}
		  }
		  
		 }
		'''
		def template = engine.createTemplate(text).make([ticket:ticket,user:user,url : "https://thinktech-crm.herokuapp.com"])
		template.toString()
	}
	
	def getCommentTemplate(user,comment) {
		MarkupTemplateEngine engine = new MarkupTemplateEngine()
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-bottom:2%;padding-top:2%;text-align:center;background:#05d2ff") {
		      h4(style : "font-size: 120%;color: #fff;margin: 3px") {
		        span("Nouveau commentaire ajout&eacute;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		     h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-bottom: 0px") {
		         span("Auteur : $user.name")
		     }
		     p("$comment.message")

		    }
		    div(style : "text-align:center;margin-top:30px;margin-bottom:10px") {
			    a(href : "$url/dashboard/support",style : "font-size:130%;width:140px;margin:auto;text-decoration:none;background: #05d2ff;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
			        span("R&eacute;pondre")
			    }
			}
		  }
		  
		 }
		'''
		def template = engine.createTemplate(text).make([comment:comment,user:user,url : "https://thinktech-crm.herokuapp.com"])
		template.toString()
	}
	
}