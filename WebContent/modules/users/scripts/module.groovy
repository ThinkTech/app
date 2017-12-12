import org.metamorphosis.core.ActionSupport
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper


class ModuleAction extends ActionSupport {

	def login() {
	    def user = new JsonSlurper().parse(request.inputStream) 
		def url = request.contextPath+"/dashboard"
		response.writer.write(json([url: url]))
	}
	
	def changePassword() {
	   def user = new JsonSlurper().parse(request.inputStream) 
	   response.writer.write(json([status: 1]))
	}
	
	def logout() {
	    session.invalidate()
		def url = request.contextPath+"/"
		response.sendRedirect(url)
	}
	
}

new ModuleAction()