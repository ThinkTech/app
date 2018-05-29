$(document).ready(function(){
	page.details.bind = function(container,domain) {
		if(domain.status == "finished"){
			$(".manage",container).show();
		}else {
			$(".manage",container).hide();
		}
		if(domain.action == "Transfert"){
			$(".eppCode",container).show();
		}else{
			$(".eppCode",container).hide();
		}
	};
 });