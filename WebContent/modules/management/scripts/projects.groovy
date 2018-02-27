import app.FileManager
import groovy.sql.Sql
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize as byteCount

class ModuleAction extends ActionSupport {

   def showProjects(){
       def connection = getConnection()
       def projects = []
       def id = session.getAttribute("user").structure.id
       connection.eachRow("select p.id,p.subject,p.date,p.status,p.progression,u.name from projects p, users u where p.user_id = u.id and p.structure_id = ? order by p.date DESC", [id], { row -> 
          def project = new Expando()
          project.id = row.id
          project.author =  row.name
          project.subject = row.subject
          project.date = row.date
          project.status = row.status
          project.progression = row.progression
          projects << project
       })
       def active = connection.firstRow("select count(*) AS num from projects where status = 'in progress' and structure_id = "+id).num
       def unactive = connection.firstRow("select count(*) AS num from projects where status = 'stand by' and structure_id = "+id).num
       connection.close() 
       request.setAttribute("projects",projects)  
       request.setAttribute("total",projects.size())
       request.setAttribute("active",active)
       request.setAttribute("unactive",unactive)
       SUCCESS
   }

   def createProject() {
	   def project = parse(request) 
	   def connection = getConnection()
	   def user = session.getAttribute("user")
	   def params = [project.subject,project.priority,project.service,project.plan, project.description,user.id,user.structure.id]
       def result = connection.executeInsert 'insert into projects(subject,priority,service,plan,description,user_id,structure_id) values (?, ?, ?,?,?,?,?)', params
       def id = result[0][0]
       def tasks = getTasks()
       def bill = createBill(project)
       if(bill.amount){
	       params = [bill.fee,bill.amount,id]
	       connection.executeInsert 'insert into bills(fee,amount,project_id) values (?,?,?)', params
       	   def query = 'insert into projects_tasks(name,description,info,project_id) values (?, ?, ?, ?)'
      	   connection.withBatch(query){ ps ->
             tasks.each{
               ps.addBatch(it.name,it.description,"aucune information",id)
            } 
           }
	    }else{
           def query = 'insert into projects_tasks(name,description,info,project_id) values (?, ?, ?, ?)'
      	   connection.withBatch(query){ ps ->
             tasks.eachWithIndex { it, i ->
              if(i!=0) ps.addBatch(it.name,it.description,"aucune information",id)
            }
          }
	   }
	   connection.close()
	   json([id: id])
	}
	
	def createBill(project){
	   def bill = new Expando()
	   if(project.service == "web dev") {
	      bill.fee = "caution"
	      if(project.plan == "plan business") {
	         bill.amount = 25000 * 3
	      }else if(project.plan == "plan corporate") {
	         bill.amount = 20000 * 3
	      }else if(project.plan == "plan personal") {
	         bill.amount = 15000 * 3
	      }
	   }
	   bill
	}
	
	def getTasks(){
	   def tasks = []
	   def task = new Expando(name :"Contrat et Caution",description :"cette phase intiale &edot;tablit la relation l&edot;gale qui vous lie &ugrave; ThinkTech")
	   tasks << task
	   task = new Expando(name :"Traitement",description : "cette phase d'approbation est celle o&ugrave; notre &edot;quipe technique prend en charge votre projet")
       tasks << task
       task = new Expando(name :"Analyse du projet",description : "cette phase est celle de l'analyse de votre projet pour une meilleure compr&edot;hension des objectifs")
	   tasks << task
	   task = new Expando(name :"D&edot;finition des fonctionnalit&edot;s",description : "cette phase est celle de la d&edot;finition des fonctionnalit&edot;s du produit")
	   tasks << task
	   task = new Expando(name :"Conception de l'interface",description : "cette phase est celle de la conception de l'interface utilisateur")
       tasks << task
       task = new Expando(name :"D&edot;veloppement des fonctionnalit&edot;s",description : "cette phase est celle du d&edot;veloppement des fonctionnalit&edot;s du produit")
       tasks << task
       task = new Expando(name :"Tests",description : "cette phase permet de tester les fonctionnalit&edot;s du produit")
       tasks << task
       task = new Expando(name :"Validation",description : "cette phase est celle de la validation des fonctionnalit&edot;s du produit")
       tasks << task
       task = new Expando(name :"Livraison du produit",description : "cette phase est celle du deploiement du produit final")
       tasks << task
       task = new Expando(name :"Formation",description : "cette phase finale est celle de la formation pour une prise en main du produit")
	   tasks << task
	   tasks
	}
	
	def getProjectInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def project = connection.firstRow("select p.*,u.name from projects p,users u where p.id = ? and p.user_id = u.id", [id])
	   if(project.status=='finished'){
	      project.end = project.closedOn
	      project.duration = connection.firstRow("select TIMESTAMPDIFF(MONTH,date,closedOn) as duration from projects where id = ?", [id]).duration
	   }
	   else{ 
	   	project.end = connection.firstRow("select date_add(date,interval duration month) as end from projects where id = ?", [id]).end
	   }
	   if(project.subject.length()>40) project.subject = project.subject.substring(0,40)+"..."
	   project.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.date)
	   project.end = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.end)
	   project.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name from projects_comments c, users u where c.createdBy = u.id and c.project_id = ?", [project.id],{ row -> 
          def comment = new Expando()
          comment.id = row.id
          comment.author = row.name
          comment.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          comment.message = row.message
          project.comments << comment
       })
       project.documents = []
	   connection.eachRow("select d.project_id, d.name, d.size, d.date, u.name as author from documents d, users u where d.createdBy = u.id and d.project_id = ?", [project.id],{ row -> 
          def document = new Expando()
          document.project_id = row.project_id
          document.author = row.author
          document.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          document.name = row.name
          document.size = byteCount(row.size as long)
          project.documents << document
       })
       project.tasks = []
	   connection.eachRow("select name,description,info,status,progression from projects_tasks where project_id = ?", [project.id],{ row -> 
          def task = new Expando()
          task.name = row.name
          task.description = row.description
          task.status = row.status
          task.progression = row.progression
          task.info = row.info
          project.tasks << task
       })
       if(project.status == "stand by" && project.plan != "plan social") {
         project.bill = connection.firstRow("select b.*,p.service from bills b, projects p where b.project_id = p.id and p.id = ?", [id])
	  	 project.bill.date = new SimpleDateFormat("dd/MM/yyyy").format(project.bill.date)
       }
       if(project.status == "finished"){
         response.setHeader("Cache-control", "private, max-age=78840000")
       }
	   connection.close() 
	   json([entity : project])
	}
	
	def getProjectBill() {
	   def id = getParameter("id")
	   def connection = getConnection()
       def bill = connection.firstRow("select b.*,p.service from bills b, projects p where b.project_id = p.id and p.id = ?", [id])
	   bill.date = new SimpleDateFormat("dd/MM/yyyy").format(bill.date)
	   json([entity : bill])
	   connection.close()
	}
	
	def addComment() {
	   def comment = parse(request) 
	   def user_id = session.getAttribute("user").id
	   Thread.start { 
	   	 def connection = getConnection()
	     def params = [comment.message,comment.project,user_id]
         connection.executeInsert 'insert into projects_comments(message,project_id,createdBy) values (?,?,?)', params
	     connection.close()
	   }
	   json([status: 1])
	}
	
	def updateProjectPriority(){
	    def project = parse(request) 
	    Thread.start {
	   	   def connection = getConnection()
	       connection.executeUpdate "update projects set priority = ? where id = ?", [project.priority,project.id] 
	       connection.close()
	    }
		json([status: 1])
	}
	
	def saveDocuments() {
	   def upload = parse(request) 
	   def id = upload.id
	   def user_id = session.getAttribute("user").id
	   Thread.start {
	     def connection = getConnection()
	     def query = 'insert into documents(name,size,project_id,createdBy) values (?,?,?,?)'
         connection.withBatch(query){ ps ->
           for(def document : upload.documents) ps.addBatch(document.name,document.size,id,user_id)
         }
	     connection.close()
	   }
	   json([status: 1])
	}
	
	def downloadDocument(){
	   def user = session.getAttribute("user")
	   def dir = "structure_"+user.structure.id+"/"+"project_"+getParameter("project_id")
	   def name = getParameter("name")
	   response.contentType = context.getMimeType(name)
	   response.setHeader("Content-disposition","attachment; filename=$name")
	   def fileManager = new FileManager()
	   fileManager.download(dir+"/"+name,response.outputStream)
	}
	
	def updateProjectDescription() {
	   def project = parse(request)
	   Thread.start {
	   	 def connection = getConnection()
	     connection.executeUpdate "update projects set description = ? where id = ?", [project.description,project.id] 
	     connection.close()
	   }
	   json([status: 1])
	}
	
	def getConnection()  {
		new Sql(dataSource)
	}
	
}

new ModuleAction()