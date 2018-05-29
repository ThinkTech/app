import groovy.sql.Sql

class ModuleAction extends ActionSupport {

   def showDomains(){
       def connection = getConnection()
       def domains = []
       connection.eachRow("select id,name,year,date,price,status,action from domains where structure_id = ? order by date DESC",[user.structure.id], { row -> 
          def domain = new Expando()
          domain.with {
            id = row.id
            name = row.name
            year = row.year
            price = row.price
            status = row.status
            action = row.action
            date = row.date 
          }
          domains << domain
       })
       def registered = connection.firstRow("select count(*) AS num from domains where status = 'finished' and structure_id = "+user.structure.id).num
       def unregistered = connection.firstRow("select count(*) AS num from domains where status != 'finished' and structure_id = "+user.structure.id).num
       connection.close() 
       request.setAttribute("domains",domains)  
       request.setAttribute("total",domains.size())
       request.setAttribute("registered",registered)
       request.setAttribute("unregistered",unregistered)
       SUCCESS
    }
    
    def getDomainInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def domain = connection.firstRow("select * from domains where id = ?", [id])
	   domain.date = new SimpleDateFormat("dd/MM/yyyy").format(domain.date)
	   if(domain.paidOn) {
	     domain.paidOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(domain.paidOn)
	     def user = connection.firstRow("select u.name from users u, domains b where u.id = b.paidBy and b.id = ?", [id])
	     domain.paidBy = user.name 
	   }else{
	   	 domain.user = user
	   }
	   connection.close()
	   json([entity : domain])
	}
	
	def getConnection()  {
		new Sql(dataSource)
	}
	
}