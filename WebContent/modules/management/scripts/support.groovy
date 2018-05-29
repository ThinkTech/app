import groovy.text.markup.MarkupTemplateEngine
import groovy.sql.Sql

class ModuleAction extends ActionSupport {
	
	def showTickets(){
       def connection = getConnection()
       def tickets = []
       connection.eachRow("select t.id,t.subject,t.message,t.date,t.service,t.status,t.progression, u.name from tickets t, users u where t.user_id = u.id and t.structure_id = ? order by t.date DESC", [user.structure.id], { row -> 
          def ticket = new Expando()
          ticket.with{
            id = row.id
            author =  row.name
            subject = row.subject
            message = row.message
            date = row.date
            service = row.service
            status = row.status
            progression = row.progression 
          }
          tickets << ticket
       })
       def unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = "+user.structure.id).num
       def solved = connection.firstRow("select count(*) AS num from tickets where status = 'finished' and structure_id = "+user.structure.id).num
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
	   json([id: result[0][0]])
	   connection.close()
	}
	
	def getTicketInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def ticket = connection.firstRow("select t.*, u.name from tickets t,users u where t.id = ? and t.user_id = u.id", [id])
	   if(ticket.subject.length()>40) ticket.subject = ticket.subject.substring(0,40)+"..."
	   ticket.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.date)
	   if(ticket.closedOn) {
	   	ticket.closedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(ticket.closedOn)
	   	def user = connection.firstRow("select u.name from users u, tickets t where u.id = t.closedBy and t.id = ?", [id])
	    ticket.closedBy = user.name
	    response.addHeader("Cache-control", "private, max-age=78840000")
	   }
	   ticket.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name from tickets_comments c, users u where c.createdBy = u.id and c.ticket_id = ?", [ticket.id],{ row -> 
          def comment = new Expando()
          comment.with{
            id = row.id
            author = row.name
            date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
            message = row.message  
          }
          ticket.comments << comment
       })
	   connection.close()
	   json([entity : ticket])
	}
	
	def addTicketComment() {
	   def comment = parse(request)
	   def user_id = user.id
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
	   def user_id = user.id 
	   Thread.start {
	      def connection = getConnection()
	      connection.executeUpdate "update tickets set progression = 100, status = 'finished', closedOn = NOW(), closedBy = ? where id = ?", [user_id,ticket.id] 
	      connection.close()
	   }
	   json([status : 1])
	}
	
	def getConnection()  {
	   new Sql(dataSource)	
	}
	
}