import app.FileManager
import groovy.sql.Sql
import groovy.text.markup.MarkupTemplateEngine
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize as byteCount

class ModuleAction extends ActionSupport {

   def showProjects(){
       def connection = getConnection()
       def projects = []
       connection.eachRow("select p.id,p.subject,p.date,p.status,p.progression,u.name from projects p, users u where p.user_id = u.id and p.structure_id = ? order by p.date DESC", [user.structure.id], { row -> 
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
       def active = connection.firstRow("select count(*) AS num from projects where status = 'in progress' and structure_id = $user.structure.id").num
       def unactive = connection.firstRow("select count(*) AS num from projects where status = 'stand by' and structure_id = $user.structure.id").num
       request.setAttribute("projects",projects)  
       request.setAttribute("total",projects.size())
       request.setAttribute("active",active)
       request.setAttribute("unactive",unactive)
       def domains = []
       connection.eachRow("select d.id, d.name from domains d where d.status = 'finished' and not exists (select p.domain_id from projects p where d.id = p.domain_id) order by d.date DESC", [], { row -> 
          def domain = new Expando()
          domain.with {
           id = row.id
           name =  row.name
          }
		  domains << domain
       })
       request.setAttribute("domains",domains)
       connection.close() 
       SUCCESS
   }
	
   def getProjectInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def project = connection.firstRow("select p.*,u.name,d.name as domain from projects p,users u, domains d where p.id = ? and p.user_id = u.id and p.domain_id = d.id", [id])
	   if(project.status == 'finished'){
	      project.end = project.closedOn
	      project.duration = connection.firstRow("select TIMESTAMPDIFF(MONTH,startedOn,closedOn) as duration from projects where id = ?", [project.id]).duration
	   }
	   else if(project.status == 'in progress'){ 
	   	project.end = connection.firstRow("select date_add(startedOn,interval duration month) as end from projects where id = ?", [project.id]).end
	   }
	   else{ 
	   	project.end = connection.firstRow("select date_add(date,interval duration month) as end from projects where id = ?", [project.id]).end
	   }
	   project.date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.date)
	   project.end = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(project.end)
	   project.comments = []
	   connection.eachRow("select c.id, c.message, c.date, u.name from projects_comments c, users u where c.createdBy = u.id and c.project_id = ?", [project.id],{ row -> 
          def comment = new Expando()
          comment.id = row.id
          comment.with{
           author = row.name
           date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
           message = row.message  
          }
          project.comments << comment
       })
       project.documents = []
	   connection.eachRow("select d.project_id, d.name, d.size, d.date, u.name as author from documents d, users u where d.createdBy = u.id and d.project_id = ?", [project.id],{ row -> 
          def document = new Expando()
          document.with{
          	project_id = row.project_id
            author = row.author
            date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(row.date)
            name = row.name
            size = byteCount(row.size as long) 
          }
          project.documents << document
       })
       project.tasks = []
	   connection.eachRow("select name,description,info,status,progression from projects_tasks where project_id = ?", [project.id],{ row -> 
          def task = new Expando()
          task.with{
            name = row.name
            description = row.description
            status = row.status
            progression = row.progression
            info = row.info ? row.info : "aucune information" 
          }
          project.tasks << task
       })
	   connection.close() 
	   json([entity : project])
	}
	
	def addComment() {
	   def comment = parse(request) 
	   def connection = getConnection()
	   def params = [comment.message,comment.project,user.id]
       connection.executeInsert 'insert into projects_comments(message,project_id,createdBy) values (?,?,?)', params
       def subject = connection.firstRow("select subject from projects  where id = ?", [comment.project]).subject
       sendMail("ThinkTech Support","support@thinktech.sn","Projet : ${subject}",getCommentTemplate(user,comment))
	   connection.close()
	   json([status: 1])
	}
	
	def saveDocuments() {
	   def upload = parse(request) 
	   def connection = getConnection()
	   def query = 'insert into documents(name,size,project_id,createdBy) values (?,?,?,?)'
       connection.withBatch(query){ ps ->
          for(def document : upload.documents) ps.addBatch(document.name,document.size,upload.id,user.id)
       }
	   connection.close()
	   json([status: 1])
	}
	
	def downloadDocument(){
	   def dir = "structure_"+user.structure.id+"/"+"project_"+getParameter("project_id")
	   def name = getParameter("name")
	   response.contentType = context.getMimeType(name)
	   response.addHeader("Content-disposition","attachment; filename=$name")
	   def fileManager = new FileManager()
	   fileManager.download(dir+"/"+name,response.outputStream)
	}
	
	def updateProjectDescription() {
	   def project = parse(request)
	   def connection = getConnection()
	   connection.executeUpdate "update projects set description = ? where id = ?", [project.description,project.id] 
	   connection.close()
	   json([status: 1])
	}
	
	def getCommentTemplate(user,comment) {
		MarkupTemplateEngine engine = new MarkupTemplateEngine()
		def text = '''\
		 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-bottom:2%;padding-top:2%;text-align:center;background:#05d2ff") {
		      h4(style : "font-size: 120%;color: #fff;margin: 3px") {
		        span("Nouveau commentaire ajout&eacute;")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		     h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-bottom: 0px") {
		         span("Auteur : $user.name")
		     }
		     p("$comment.message")

		    }
		    div(style : "text-align:center;margin-top:30px;margin-bottom:10px") {
			    a(href : "$url/dashboard/projects",style : "font-size:130%;width:140px;margin:auto;text-decoration:none;background: #05d2ff;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
			        span("R&eacute;pondre")
			    }
			}
		  }
		  
		 }
		'''
		def template = engine.createTemplate(text).make([comment:comment,user:user,url : "https://thinktech-crm.herokuapp.com"])
		template.toString()
	}
	
	def getConnection()  {
		new Sql(dataSource)
	}
	
}