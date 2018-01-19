import org.metamorphosis.core.ActionSupport

class ModuleAction extends ActionSupport {

    def String execute(){
       def user = session.getAttribute("user")
       if(user){
         response.sendRedirect(request.contextPath+"/dashboard")
       }else {
         SUCCESS
       }
    }
    
}

new ModuleAction()