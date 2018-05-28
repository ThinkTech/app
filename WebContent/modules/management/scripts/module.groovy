import groovy.sql.Sql


class ModuleAction extends ActionSupport {

   def String execute(){
       def connection = getConnection()
       def projects = []
       connection.eachRow("select p.id,p.subject,p.date,p.status,p.progression, u.name from projects p, users u where p.user_id = u.id and p.structure_id = ? order by p.date DESC", [user.structure.id], { row -> 
          def project = new Expando()
          project.with {
         	id = row.id
            author =  row.name
            subject = row.subject
            date = row.date
            status = row.status
            progression = row.progression  
          }
          projects << project
       })
       def projects_count = connection.firstRow("select count(*) AS num from projects where status = 'stand by' and structure_id = "+ user.structure.id).num
       def tickets_unsolved = connection.firstRow("select count(*) AS num from tickets where status != 'finished' and structure_id = "+ user.structure.id).num
       def bills_count = connection.firstRow("select count(*) AS num from bills where status = 'stand by' and structure_id = "+ user.structure.id).num
       connection.close() 
       request.setAttribute("projects",projects)  
       request.setAttribute("projects_count",projects_count)
       request.setAttribute("tickets_unsolved",tickets_unsolved)
       request.setAttribute("bills_count",bills_count)
   	   SUCCESS
   }
    
	def showServices(){
	   request.setAttribute("total",1)
       request.setAttribute("subscribed",1)
       def services = []
       def service = new Expando(name : 'web dev',icon : 'siteweb-service.png')
       services << service
       request.setAttribute("services",services)
       SUCCESS
    }
			
	def getConnection()  {
		new Sql(dataSource)
	}
	
}