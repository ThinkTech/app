import org.metamorphosis.core.ActionSupport
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper

class User {
   def name
   def email
   def telephone
   def fonction
   def role
   def structure
}

class Structure {
   def name
   def ninea
}

class ModuleAction extends ActionSupport {

    def ModuleAction() {
       def user = new User(name : "Malorum", email : "malorum@gmail.com",role : "administrateur",fonction : "CEO",telephone : "776154520")
       user.structure = new Structure(name : "Sesame",ninea : 1454554)
       request.setAttribute("user",user)
   }

	def login() {
	    def user = new JsonSlurper().parse(request.inputStream) 
		def url = request.contextPath+"/dashboard"
		response.writer.write(json([url: url]))
	}
	
	def changePassword() {
	   def user = new JsonSlurper().parse(request.inputStream) 
	   response.writer.write(json([status: 1]))
	}
	
	def updateProfil() {
	   response.writer.write(json([status: 1]))
	}
	
	def logout() {
	    session.invalidate()
		def url = request.contextPath+"/"
		response.sendRedirect(url)
	}
	
}

new ModuleAction()