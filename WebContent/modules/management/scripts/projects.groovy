import static org.apache.commons.io.FileUtils.byteCountToDisplaySize as byteCount

@Controller
class ModuleAction extends ActionSupport {

   @GET(url="projects",page="projects")
   def showProjects() {
       request.projects = connection.rows("select p.id,p.subject,p.plan,date_format(p.date,'%d/%m/%Y') as date,p.status,p.progression,u.name as author from projects p, users u where p.user_id = u.id and p.structure_id = ? order by p.date DESC", [user.structure.id])
       request.total = request.projects.size()
       request.active = connection.firstRow("select count(*) AS num from projects where status = 'in progress' and structure_id = $user.structure.id").num
       request.unactive = connection.firstRow("select count(*) AS num from projects where status = 'stand by' and structure_id = $user.structure.id").num
       request.domains = connection.rows("select d.id, d.name from domains d where d.status = 'finished' and d.structure_id = $user.structure.id and not exists (select p.domain_id from projects p where d.id = p.domain_id) order by d.date DESC", [])
       SUCCESS
   }

   @GET("projects/info")
   def getProjectInfo() {
	   def project = connection.firstRow("select p.*,u.name,d.name as domain from projects p,users u, domains d where p.id = ? and p.user_id = u.id and p.domain_id = d.id", [request.id])
	   if(project.status == 'finished'){
	      project.startedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.startedOn)
	      project.end = project.closedOn
	      project.duration = connection.firstRow("select TIMESTAMPDIFF(MONTH,startedOn,closedOn) as duration from projects where id = ?", [project.id]).duration
	      project.duration = project.duration > 0 ? project.duration : 1;
	   }
	   else if(project.status == 'in progress'){ 
	    project.startedOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.startedOn)
	   	project.end = connection.firstRow("select date_add(startedOn,interval duration month) as end from projects where id = ?", [project.id]).end
	   }
	   else{ 
	   	project.end = connection.firstRow("select date_add(date,interval duration month) as end from projects where id = ?", [project.id]).end
	   }
	   project.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.date)
	   project.end = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.end)
	   project.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name as author, u.type from projects_comments c, users u where c.createdBy = u.id and c.project_id = ?", [project.id],{ row -> 
          def comment = row.toRowResult()
          comment.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
          comment.icon = comment.type == 'customer' ? 'user' : 'address-book'
          project.comments << comment
       })
       project.documents = []
	   connection.eachRow("select d.project_id, d.name, d.size, d.date, u.name as author from documents d, users u where d.createdBy = u.id and d.project_id = ?", [project.id],{ row -> 
          def document = row.toRowResult()
          document.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(document.date)
          document.size = byteCount(document.size as long) 
          project.documents << document
       })
       project.tasks = []
	   connection.eachRow("select name,description,info,status,progression from projects_tasks where project_id = ?", [project.id],{ row -> 
          def task = row.toRowResult()
          task.info = task.info ? task.info : "aucune information" 
          project.tasks << task
       })
	   json(project)
	}
	
	def addComment() {
	   def comment = request.body 
	   def params = [comment.message,comment.project,user.id]
       connection.executeInsert 'insert into projects_comments(message,project_id,createdBy) values (?,?,?)', params
       def subject = connection.firstRow("select subject from projects  where id = ?", [comment.project]).subject
       sendSupportMail("Projet : ${subject}",parseTemplate("project_comment",[comment:comment,user:user,url:crmURL]))
	   json([status: 1])
	}
	
	def saveDocuments() {
	   def upload = request.body 
	   def query = 'insert into documents(name,size,project_id,createdBy) values (?,?,?,?)'
       connection.withBatch(query){ ps ->
          for(def document : upload.documents) ps.addBatch(document.name,document.size,upload.id,user.id)
       }
	   json([status: 1])
	}
	
	def downloadDocument() {
	   def name = request.name
	   response.contentType = context.getMimeType(name)
	   response.addHeader("Content-disposition","attachment; filename=$name")
	   def fileManager = new FileManager("structure_"+user.structure.id+"/"+"project_"+request.project_id)
	   fileManager.download(name,response.outputStream)
	}
	
	def updateProjectDescription() {
	   def project = request.body
	   connection.executeUpdate "update projects set description = ? where id = ?", [project.description,project.id] 
	   json([status: 1])
	}
	
}