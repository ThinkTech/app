class ModuleAction extends ActionSupport {

   def showDomains(){
       def connection = getConnection()
       def domains = connection.rows("select d.id,d.name,d.year,d.date,d.price,d.status,d.emailOn,d.emailActivatedOn,u.name as author from domains d, users u where d.structure_id = ? and d.user_id = u.id order by date DESC",[user.structure.id])
       def registered = connection.firstRow("select count(*) AS num from domains where status = 'finished' and structure_id = $user.structure.id").num
       def unregistered = connection.firstRow("select count(*) AS num from domains where status != 'finished' and structure_id = $user.structure.id").num
       connection.close() 
       request.setAttribute("domains",domains)  
       request.setAttribute("total",domains.size())
       request.setAttribute("registered",registered)
       request.setAttribute("unregistered",unregistered)
       request.setAttribute("email",user.email.substring(0,user.email.indexOf("@")))
       SUCCESS
    }
    
    def getDomainInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def domain = connection.firstRow("select d.*,u.name as author from domains d, users u where d.id = ? and d.user_id = u.id", [id])
	   domain.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(domain.date)
	   domain.action = domain.action ? "Transfert" : "Achat"
	   domain.eppCode = domain.eppCode ? domain.eppCode : "&nbsp;"
	   if(domain.registeredOn) domain.registeredOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(domain.registeredOn)
	   connection.close()
	   json([entity : domain])
	}
	
}