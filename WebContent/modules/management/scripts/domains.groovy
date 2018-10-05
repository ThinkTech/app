class ModuleAction extends ActionSupport {

   def showDomains() {
       request.domains = connection.rows("select d.id,d.name,d.year,d.date,d.price,d.status,d.emailOn,d.emailActivatedOn,u.name as author from domains d, users u where d.structure_id = ? and d.user_id = u.id order by date DESC",[user.structure.id])
       request.total = request.domains.size()
       request.registered = connection.firstRow("select count(*) AS num from domains where status = 'finished' and structure_id = $user.structure.id").num
       request.unregistered = connection.firstRow("select count(*) AS num from domains where status != 'finished' and structure_id = $user.structure.id").num
       request.email = user.email.substring(0,user.email.indexOf("@"))
       SUCCESS
    }
    
    def getDomainInfo() { 
	   def domain = connection.firstRow("select d.*,u.name as author from domains d, users u where d.id = ? and d.user_id = u.id", [request.id])
	   domain.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(domain.date)
	   domain.action = domain.action ? "Transfert" : "Achat"
	   domain.eppCode = domain.eppCode ? domain.eppCode : "&nbsp;"
	   if(domain.registeredOn) domain.registeredOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(domain.registeredOn)
	   if(domain.registeredOn && !domain.emailOn){
	     def count = connection.firstRow("select count(*) AS count from domains where plan = 'free' and structure_id = $user.structure.id").count
	     domain.disableFreePlan = count > 0 ? true : false
	     count = connection.firstRow("select count(*) AS count from domains where plan != 'free' and structure_id = $user.structure.id").count
	     if(!domain.disableFreePlan && !count){
	      domain.enableEmail = true  
	     }else{
	       count = connection.firstRow("select count(*) AS count from domains where plan != 'free' and emailActivatedOn is not null and structure_id = $user.structure.id").count
	       domain.enableEmail = count > 0 ? true : false  
	     }
	   }
	   json(domain)
	}
	
}