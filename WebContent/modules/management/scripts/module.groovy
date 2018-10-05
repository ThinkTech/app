class ModuleAction extends ActionSupport {

   def String execute() {
       request.projects = connection.rows("select p.id,p.subject,p.plan,date_format(p.date,'%d/%m/%Y') as date,p.status,p.progression, u.name as author from projects p, users u where p.user_id = u.id and p.structure_id = ? order by p.date DESC", [user.structure.id])  
       request.projects_count = connection.firstRow("select count(*) AS num from projects where status = 'in progress' and structure_id = $user.structure.id").num
       request.tickets_unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = $user.structure.id").num
       request.bills_count = connection.firstRow("select count(*) AS num from bills where status = 'stand by' and structure_id = $user.structure.id").num
   	   SUCCESS
   }
    
	def showServices() {
	   def services = []
       services << new Expando(name : 'domainhosting',icon : 'domain-service.png')
       services << new Expando(name : 'mailhosting',icon : 'mail-service.png')
       services << new Expando(name : 'webdev',icon : 'webdev-service.png')
       request.services = services
       request.total = 3
       request.subscribed = 3
       request.unsubscribed = 0
       SUCCESS
    }
	
}