$(document).ready(function(){
	page.details.bind = function(container,bill) {
		if(bill.status == "finished"){
		   $(".submit",container).hide();
		   $(".details",container).show();
		}else {
		   $(".submit",container).show();
		   $(".details",container).hide();
		}
		$("input[type=submit]",container).click(function(event){
			page.details.hide();
			const top = $(".chit-chat-layer1").offset().top;
			page.wait({top : top});
			head.load("modules/payment/js/wizard.js",function() {
			    page.wizard.show(bill,top,function(){
			    	const tr = $(".table tr[id="+bill.id+"]");
					$("span.label",tr).html("termin&edot;").removeClass().addClass("label label-success");
					$(".badge",tr).html("100%");
			    });
			});
			return false;
		});
		$("input[type=button]",container).click(function(event) {
			$(".window").hide();
		})
	};
 });