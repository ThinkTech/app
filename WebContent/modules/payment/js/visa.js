function onVisaCheckoutReady(){
  V.init( {
  apikey: "5CUQJ9M76DYS2QYARXZZ21PcguqrizMxsdocAavPttpscAbNU",
  paymentRequest:{
    currencyCode: "USD",
    subtotal: "11.00",
    shippingHandling: "2.00",
    tax: "0.00",
    discount: "0.00",
    giftWrap: "0.00",
    total : "13.00"
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
	payment.done = true;
  });
  V.on("payment.cancel", function(response){ 
  });
  V.on("payment.error", function(response, error){ 
  });
}