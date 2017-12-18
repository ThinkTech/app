$(document).ready(function(){
	page.details.bind = function(container,entity) {
		if(entity.status == "finished"){
			  $(".submit",container).hide();
			  $(".details",container).show();
			}else {
			  $(".submit",container).show();
			  $(".details",container).hide();
			}
	};
	$(".window input[type=submit]").click(function(event){
		$(".window").hide();
		const top = $(".chit-chat-layer1").offset().top;
		page.wait({top : top});
		head.load("modules/payment/js/wizard.js",function() {
			const bill = {};
			bill.service = "site web";
			bill.amount = "20 000";
			bill.fee = "h&edot;bergement";
			bill.date = "17/09/2017";
		    page.wizard.show(bill,top);
		});
		return false;
	});
 });