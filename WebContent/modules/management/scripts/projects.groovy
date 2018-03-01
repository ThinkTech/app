import app.FileManager
import groovy.sql.Sql
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize as byteCount

class ModuleAction extends ActionSupport {

   def showProjects(){
       def connection = getConnection()
       def projects = []
       def id = user.structure.id
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
         project.bill.user = user
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
	   bill.user = user
	   bill.date = new SimpleDateFormat("dd/MM/yyyy").format(bill.date)
	   json([entity : bill])
	   connection.close()
	}
	
	def addComment() {
	   def comment = parse(request) 
	   def user_id = user.id
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
	   def user_id = user.id
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