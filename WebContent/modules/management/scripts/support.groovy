class ModuleAction extends ActionSupport {
	
	def showTickets() {
       request.tickets = connection.rows("select t.id,t.subject,t.message,t.date,t.service,t.status,t.progression, u.name as author from tickets t, users u where t.user_id = u.id and t.structure_id = ? order by t.date DESC", [user.structure.id])
       request.total = request.tickets.size()
       request.solved = connection.firstRow("select count(*) AS num from tickets where status = 'finished' and structure_id = $user.structure.id").num
       request.unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = $user.structure.id").num
       SUCCESS
    }
	
	def createTicket() {
	   def ticket = parse(request) 
	   def params = [ticket.subject,ticket.service,ticket.message,ticket.priority,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into tickets(subject,service,message,priority,user_id,structure_id) values (?, ?, ?, ?,?,?)', params
	   sendSupportMail("Nouveau Ticket : ${ticket.subject}",parseTemplate("ticket",[ticket:ticket,user:user,url:crmURL]))
	   json([id: result[0][0]])
	}
	
	def getTicketInfo() {
	   def id = request.id
	   def ticket = connection.firstRow("select t.*, u.name from tickets t,users u where t.id = ? and t.user_id = u.id", [id])
	   ticket.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.date)
	   if(ticket.status == "in progress" || ticket.status == "finished"){
	     	ticket.startedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.startedOn)                                                      
	   }
	   if(ticket.closedOn){
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
	   json(ticket)
	}
	
	def addTicketComment() {
	   def comment = parse(request)
	   def params = [comment.message,comment.ticket,user.id]
       connection.executeInsert 'insert into tickets_comments(message,ticket_id,createdBy) values (?,?,?)', params
       def subject = connection.firstRow("select subject from tickets  where id = ?", [comment.ticket]).subject
       sendSupportMail("Ticket : ${subject}",parseTemplate("ticket_comment",[comment:comment,user:user,url:crmURL]))
	   json([status: 1])
	}
	
}