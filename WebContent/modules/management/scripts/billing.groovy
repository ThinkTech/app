class ModuleAction extends ActionSupport {

   def showBills(){
       def connection = getConnection()
       def bills = connection.rows("select id,fee,amount,date,status,service from bills where structure_id = ? order by date DESC",[user.structure.id])
       request.setAttribute("bills",bills)  
       request.setAttribute("total",bills.size())
       request.setAttribute("payed",connection.firstRow("select count(*) AS num from bills where status = 'finished' and structure_id = $user.structure.id").num)
       request.setAttribute("unpayed",connection.firstRow("select count(*) AS num from bills where status = 'stand by' and structure_id = $user.structure.id").num)
       connection.close()
       SUCCESS
    }
    
    def getBillInfo() {
	   def id = getParameter("id")
	   def connection = getConnection()
	   def bill = connection.firstRow("select * from bills where id = ?", [id])
	   bill.date = new SimpleDateFormat("dd/MM/yyyy").format(bill.date)
	   if(bill.paidOn) {
	     bill.paidOn = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(bill.paidOn)
	     bill.paidBy = connection.firstRow("select u.name from users u, bills b where u.id = b.paidBy and b.id = ?", [id]).name
	   }else{
	   	 bill.user = user
	   }
	   connection.close()
	   json(bill)
	}
	
}