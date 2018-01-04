import org.metamorphosis.core.ActionSupport 
import static groovy.json.JsonOutput.toJson as json
import groovy.json.JsonSlurper
import groovy.sql.Sql

class ModuleAction extends ActionSupport {

   def pay(){
      def bill = new JsonSlurper().parse(request.inputStream) 
      def connection = getConnection()
	  connection.executeUpdate "update bills set status = 'finished', paidWith = ?, paidOn = NOW() where id = ?", [bill.paidWith,bill.id]
	  if(bill.fee == "caution"){
	  	connection.executeUpdate "update projects set status = 'in progress', progression = 5 where id = ?", [bill.project_id]
	  	connection.executeUpdate "update projects_tasks set status = 'finished', progression = 100 where task_id = ? and project_id = ?", [2,bill.project_id]
	  }
	  connection.close()
      response.writer.write(json([status: 1]))
   }
   
   def getConnection()  {
	  def db = [url:'jdbc:mysql://localhost/thinktech', user:'root', password:'thinktech', driver:'com.mysql.jdbc.Driver']
      Sql.newInstance(db.url, db.user, db.password, db.driver)
   }
}

new ModuleAction()