const payment = {};
payment.wizard = {};
payment.wizard.init = function(){
	const wizard = $("#checkout-wizard").css("opacity","0").show();	
	const form = $(".checkout-wizard-steps > form",wizard);
	$.each($(".digit",form),function(i,node){
		 node = $(node);
		 const val = parseInt(node.text());
		 node.html(val.toLocaleString("fr-FR"));
	});
	form.easyWizard({
		    prevButton: "Pr\u0117c\u0117dent",
		    nextButton: "Suivant",
		    submitButtonText: "Terminer",
		    after: function(wizardObj,prevStep,currentStep) {
		    	const div = $(".shopping-payment",currentStep);
		    	if(div.length) {
		    		$(".payment",currentStep).hide();
		    		const input = prevStep.find("select[name='method']");
	    			const val = input.val();
                    if(val == "visa" || val == "mastercard") {
                      page.wait({top : form.offset().top+80});
                      head.load("https://sandbox-assets.secure.checkout.visa.com/checkout-widget/resources/js/integration/v1/sdk.js",function(){
                    	  page.release();
                    	  V.init( {
                    		  apikey: "5CUQJ9M76DYS2QYARXZZ21PcguqrizMxsdocAavPttpscAbNU",
                    		  paymentRequest:{
                    		    currencyCode: "USD",
                    		    total : payment.wizard.bill.amount
                    		  },
                    		  settings: {
                    			  locale: "fr_FR",
                    			  displayName: "ThinkTech - Portail",
                    			  websiteUrl: "https://app.thinktech.sn",
                    			  shipping: {
                    				  collectShipping : "false"
                    			  },
                    			  review: {
                    				  message: "Effectuer le paiement de votre facture.",
                    				  buttonAction : "Pay"
                    			  }	
                    		  } 
                    		  });
                    		  V.on("payment.success", function(response){
                    			  payment.wizard.bill.paidWith = val == 'visa' ? "Visa" : "MasterCard";
                    			  payment.wizard.submit();
                    		  });
                    		  V.on("payment.cancel", function(response){ 
                    		  });
                    		  V.on("payment.error", function(response, error){ 
                    		  });
                      });
      	    		}else if (val == "wari"){
      	    			const button = currentStep.find("input[type='button']");
      	    			button.unbind("click").click(function(){
      	    				payment.wizard.bill.code = currentStep.find("input[type='text']").val();
      	    				if(payment.wizard.bill.code){
      	    					 payment.wizard.bill.paidWith = "Wari";
      	    					 confirm("&ecirc;tes vous s&ucirc;r de vouloir effectuer ce paiement?",function(){
          	    					payment.wizard.submit();
          	    				 });	
      	    				}else {
      	    					alert("veuillez saisir votre code Wari");
      	    				}
      	    			});
      	    		}	
		    		$("."+val+"-payment",div).show();
		    	}
		    },
		    beforeSubmit: function() {
		    	const select = form.find("select[name='method']")
	    		const val = select.val();
	    		if(val == "visa") {
	    		   alert("vous devez effectuer le paiement",function(){
	    			 $("."+val+"-payment .v-button",form).trigger("click");  
	    		   });
	    		}else{
	    		 alert("vous devez effectuer le paiement");
	    		} 
		    	return false;
		    }
	});
	$(".close",wizard).click(function(){
		wizard.fadeOut(100);
	});
	wizard.hide().css("opacity","1");
};
payment.wizard.show = function(bill,top,callback){
	payment.wizard.bill = bill;
	payment.wizard.callback = callback;
	payment.wizard.top = top ? top : "15%";
	page.wait({top : top});
	head.load("modules/payment/js/jquery.easyWizard.js","modules/payment/css/wizard.css",
	  function() {
		if(!payment.wizard.loaded){
			const container = $("<div id='wizard-container'/>").appendTo($("body"));
			container.load("modules/payment/wizard.html", function() {
				const wizard = $("#checkout-wizard");
				page.render(wizard, bill, false, function() {
					payment.wizard.init();
					payment.wizard.loaded = true;
					page.release();
					wizard.show();
				});
			});
		}
		if(payment.wizard.loaded){
			const wizard = $("#checkout-wizard");
			page.render(wizard, bill, false, function() {
				payment.wizard.init();
				page.release();
				wizard.show();
			});
		}
		
    });
};
payment.wizard.submit = function(){
	const wizard = $("#checkout-wizard");
	const form = $("form",wizard);
	page.wait({top : payment.wizard.top});
	$.ajax({
		  type: "POST",
		  url: form.attr("action"),
		  data: JSON.stringify(payment.wizard.bill),
		  contentType : "application/json",
		  success: function(response) {
			  if(response.status){
				  page.release();
				  wizard.fadeOut();
				  alert("le paiement de votre facture a &edot;t&edot; bien effectu&edot;");
				  if(payment.wizard.callback) payment.wizard.callback()
			  }
		  },
		  error : function(){
			  page.release();
			  alert("erreur lors de la connexion au serveur");
		  },
		  dataType: "json"
	});
};