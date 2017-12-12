const payment = {};
page.wizard = {};
page.wizard.init = function(){
	const wizard = $("#checkout-wizard").css("opacity","0").show();	
	const form = $(".checkout-wizard-steps > form",wizard);
	form.easyWizard({
		    prevButton: "Pr\u0117c\u0117dent",
		    nextButton: "Suivant",
		    submitButtonText: "Terminer",
		    before: function(wizardObj,currentStep,nextStep) {
		    	//if(!user) {
		    		//alert("vous devez vous connecter");
		    		//return false;
		    	//}
		    },
		    after: function(wizardObj,prevStep,currentStep) {
		    	const div = $(".shopping-payment",currentStep);
		    	if(div.length) {
		    		$(".payment",currentStep).hide();
		    		const input = prevStep.find("select[name='method']");
	    			const val = input.val();
	    			payment.done = false;
                    if(val == "visa" || val == "mastercard" || val == "express" || val == "discover") {
                      page.wait({top : form.offset().top+80});
                      head.load("modules/payment/js/visa.js","https://sandbox-assets.secure.checkout.visa.com/checkout-widget/resources/js/integration/v1/sdk.js",function(){
                    	  page.release();
                      });
      	    		}else if(val == "paypal") {
                      page.wait({top : form.offset().top+80});
                      head.load("https://www.paypalobjects.com/api/checkout.js","modules/payment/js/paypal.js",function(){
                      	  page.release(); 
                      });
        	    	} 	
		    		$("."+val+"-payment",div).show();
		    	}
		    },
		    beforeSubmit: function() {
		    	const select = form.find("select[name='method']")
	    		const val = select.val();
	    		if(val == "visa" || val == "mastercard" || val == "express" || val == "discover") {
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
page.wizard.show = function(bill,top){
	payment.done = false;
	page.wizard.top = top;
	page.wait({top : top});
	head.load("modules/payment/js/jquery.easyWizard.js","modules/payment/css/wizard.css",
	  function() {
		if(!page.wizard.loaded){
			const container = $("<div id='wizard-container'/>").appendTo($("body"));
			container.load("modules/payment/wizard.html", function() {
				page.wizard.init();
				page.wizard.loaded = true;
				page.release();
				const wizard = $("#checkout-wizard");
				wizard.show();
			});
		}
		if(page.wizard.loaded){
			page.release();
			const wizard = $("#checkout-wizard");
			wizard.show();
		}
		
    });
};
page.wizard.submit = function(){
	const wizard = $("#checkout-wizard");
	const form = $("form",wizard);
	$.ajax({
		  type: "POST",
		  url: form.attr("action"),
		  data :form.serialize(),
		  success: function(response) {
			  page.release();
		  },
		  dataType: "json"
	});
	page.wait({top : page.wizard.top});
	wizard.fadeOut(100,function(){
		$("form",wizard).easyWizard('goToStep', 1);
	});
};
app.savePayment = function() {
	page.wizard.submit();
};