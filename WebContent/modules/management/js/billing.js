$(document).ready(function(){
	$(".pay").click(function(event){
		$("tr").removeClass("active");
		$(this).parent().parent().addClass("active");
		const top = $(".chit-chat-layer1").offset().top;
		page.wait({top : top});
		head.load("modules/payment/js/wizard.js",function() {
		    page.wizard.show(null,top);
		});
		return false;
	});
	$(".window input[type=submit]").click(function(event){
		$(".window").hide();
		const top = $(".chit-chat-layer1").offset().top;
		page.wait({top : top});
		head.load("modules/payment/js/wizard.js",function() {
		    page.wizard.show(null,top);
		});
		return false;
	});
	$("tbody tr").click(function(event) {
		$(".window").show();
		$(".window .submit").show();
		$(".window .details").hide();
		return false;
	});
	$("tbody tr.paid").click(function(event) {
		$(".window .submit").hide();
		$(".window .details").show();
		return false;
	});
 });