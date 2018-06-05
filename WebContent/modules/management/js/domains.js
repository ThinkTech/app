$(document).ready(function(){
	page.details.bind = function(container,domain) {
		if(domain.status == "finished"){
			$(".manage,.businessEmail",container).show();  
		}else {
			$(".manage,.businessEmail",container).hide();
			
		}
		$(".submit",container).hide();  
		if(domain.status == "stand by"){
			const div = $(".submit",container).show();
			$("input[type=submit]",div).click(function(event){
				page.details.hide();
				const top = $(".chit-chat-layer1").offset().top;
				page.wait({top : top});
				head.load("modules/payment/js/wizard.js",function() {
					const bill = domain.bill;
				    payment.wizard.show(bill,top,function(){
				    	const tr = $(".table tr[id="+bill.product_id+"]");
  						$("span.label",tr).html("en cours").removeClass().addClass("label label-danger");
				   });
		        });
		       return false;
		   });
		   $("input[type=button]",div).click(function(event) {
				$(".window").hide();
		   });
		}
		if(domain.action == "Transfert"){
			$(".eppCode",container).show();
		}else{
			$(".eppCode",container).hide();
		}
	};
	
	page.initDomainSearch();
	
	$(".search .close").click(function(event){
  	   $(this).parent().parent().hide();
	});
	
	 $(".buttons .cancel").click(function(event){
	   $(".modal").hide();
	});
	 
});

page.details.addDomain = function(purchase){
	const div = $(".search-wizard");
	const top = div.offset().top+div.height()/2; 
    purchase.service = "domainhosting";
	purchase.user_id = $("input[name=user]",div).val();
	page.wait({top : top});
	$.ajax({
		  type: "POST",
		  url: "https://thinktech-platform.herokuapp.com/services/order",
		  data: JSON.stringify(purchase),
		  contentType : "application/json",
		  success: function(response) {
			  if(response.entity){
				  page.release();
				  purchase.id = response.entity.id;
				  const bill = {};
				  bill.product_id = purchase.id;
				  bill.user = {};
				  bill.user.id = purchase.user_id;
				  bill.service = purchase.service;
				  bill.fee = "h&eacute;bergement domaine : "+purchase.domain;
				  bill.amount = purchase.price;
				  const date = new Date();
				  bill.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
				  purchase.date = bill.date;
				  bill.id = response.entity.bill_id;
				  page.table.addRow(purchase,function(){
				    var h3 = $("h3.domainCount");
				    h3.html(parseInt(h3.text())+1);
				    h3 = $("h3.domainUnregistered");
				    h3.html(parseInt(h3.text())+1);
  					page.wait({top : top});
      				head.load("modules/payment/js/wizard.js",function() {
      				    payment.wizard.show(bill,top,function(){
      				    	const tr = $(".table tr[id="+purchase.id+"]");
      				    	$("span.label",tr).html("en cours").removeClass().addClass("label label-danger");
      				    });
      				});  
				 });
			  }
		  },
		  error : function(){
			  page.release();
			  alert("erreur lors de la connexion au serveur");
		  },
		  dataType: "json"
	});
};

page.initDomainSearch = function(){
	$(".search label").click(function(event){
   	    $(this).prev().prop("checked", true);
	});
    $(".search-wizard .finish").click(function(event){
    	const div = $(".search-wizard");
    	const top = div.offset().top+div.height()/2;
    	const purchase = JSON.parse(localStorage.getItem('purchase'));
    	if(purchase.action == "transfer"){
    		const input = $("input[name=eppCode]",div);
    		const code = input.val().trim();
    		if(code){
    			confirm("&ecirc;tes vous s&ucirc;r de vouloir transf&eacute;rer ce domaine?",function(){
    				purchase.eppCode = code;
    				page.details.addDomain(purchase);
    				$(".modal").hide();
    		  	 });
    		}else{
    			alert("vous devez entrer votre EPP Code",function(){
    				input.focus();
    		  	});
    		}
    	}else{
    		confirm("&ecirc;tes vous s&ucirc;r de vouloir acheter ce domaine?",function(){
				page.details.addDomain(purchase);
				$(".modal").hide();
		  	});
    	}
	});
    $(".search-wizard .domain-edit a").click(function(event){
    	$(".search-wizard").hide();
    	$(".search-results").show();
	});
    $(".tld-domain-search-wrapper input").keyup(function(event){
    	if (event.keyCode === 13) {
    		$(this).parent().next().trigger("click");
    	}else{
    		$(".tld-domain-search-wrapper input").val($(this).val());
    	}
    	return false;
    });
    $(".tld-domain-search-wrapper .tld-search-button").click(function(event){
    	$(".search").hide();
    	const button = $(this);
    	const top = button.offset().top;
    	const pricing = {};
    	pricing.com = 7000;
    	pricing.net = 7000;
    	pricing.org = 7000;
    	pricing.biz = 7000;
    	pricing.info = 8000;
    	pricing.tv = 20000;
    	pricing.press = 15000;
    	pricing.news = 15000;
    	pricing.tech = 10000;
    	const div = $(".tld-domain-search-wrapper");
    	const input = $("input",div);
    	const purchase = {};
  		purchase.extension = button.prev().find("select").val();
  		purchase.year = 1;
  		purchase.search = input.val().toLowerCase();
    	var domain = purchase.search.replace(/\s+/g, '');
    	if(domain){
    		const index = domain.indexOf(".");
    		if(domain.indexOf(".")!=-1) domain = domain.substring(0,index);
    		input.val(domain);
    		const url = "https://thinktech-platform.herokuapp.com/domains/search?domain="+domain;
    		page.wait({top : top-20});
    		$.ajax({
    	  	     type: "GET",
    	  	     url: url,
    	  	     dataType : "json",
    	  	     success : function(response){
    	  	    	page.release();
    	  	    	var result = response["1"].result;
    	  	    	if(result){
    	  	    		const search = $(".search-results").css("top",10).show();
    	  	    		search.parent().css("height",$('body').height()+"px").show();
        	  	    	const tbody = $("table tbody",search).empty();
	    	  	    	var tr;
	    	  	    	var i;
	    	  	    	var extension;
	    	  	    	const clone = {};
	    	  	    	clone[purchase.extension] = result[purchase.extension];
	    	  	    	for (extension in result) {
	    	  	    	    if(result.hasOwnProperty(extension)) {
	    	  	    	    	if(extension!=purchase.extension){
	    	  	    	    		clone[extension] = result[extension];
	    	  	    	    	}
	    	  	    	    }
	    	  	    	}
	    	  	    	result = clone;
	    	  	    	for (extension in result) {
	    	  	    	    if(result.hasOwnProperty(extension)) {
	    	  	    	    	if(!result[extension]){
	    	  	    	          tr = $("<tr/>");
	    	  	    	          if(purchase.extension == extension){
	    	  	    	        	tr.addClass("selected").append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
	    	  	    	          }else{
	    	  	    	        	tr.append("<td>"+domain+"."+extension+"</td>");
	    	  	    	          }
	    	  	    	          var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<10;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{tr : tr,td : td, price : pricing[extension]},function(event){
	    	  	    	        	  purchase.year = parseInt($(this).val());
	    	  	    	        	  purchase.price = event.data.price * purchase.year;
	    	  	    	        	  event.data.td.find("span").html(purchase.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	        	  $("tr",search).removeClass("selected");
	    	  	    	        	  event.data.tr.addClass("selected");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Acheter</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{tr : tr,td : td,extension : extension},function(event){
	    	  	    	        	 $("select",div).val(event.data.extension);
	    	  	    	        	 $("tr",search).removeClass("selected");
	    	  	    	        	 event.data.tr.addClass("selected");
	    	  	    	        	 purchase.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 purchase.price = purchase.year * pricing[event.data.extension];
	    	  	    	        	 purchase.extension = event.data.extension;
	    	  	    	        	 search.hide();
	    	  	    	        	 const wizard = $(".search-wizard");
	    	  	    	        	 wizard.css("top",10).show();
	    	  	    	        	 purchase.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("purchase",JSON.stringify(purchase));
	    	  	    	        	 $(".domain-name").html(purchase.domain).val(purchase.domain);
	    	  	    	        	 $(".domain-year").html(purchase.year).val(purchase.year);
	    	  	    	        	 $(".domain-price").html(pricing[event.data.extension].toLocaleString("fr-FR")).val(pricing[event.data.extension].toLocaleString("fr-FR"));
	    	  	    	        	 $(".domain-amount").html(purchase.price.toLocaleString("fr-FR")).val(purchase.price.toLocaleString("fr-FR"));
	    	  	    	        	 $(".epp-code").hide();
	    	  	    	          });
	    	  	    	          tbody.append(tr);
	    	  	    	    	}else {
	    	  	    	    	 tr = $("<tr/>");
		    	  	    	     if(purchase.extension == extension){
		    	  	    	        tr.addClass("selected").append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
		    	  	    	     }else{
		    	  	    	        tr.append("<td>"+domain+"."+extension+"</td>");
		    	  	    	      }
		    	  	    	      var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<10;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{tr : tr,td : td, price : pricing[extension]},function(event){
	    	  	    	        	  purchase.year = parseInt($(this).val());
	    	  	    	        	  purchase.price = event.data.price * purchase.year;
	    	  	    	        	  event.data.td.find("span").html(purchase.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	        	  $("tr",search).removeClass("selected");
	    	  	    	        	  event.data.tr.addClass("selected");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Transf&eacute;rer</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{tr : tr,td : td,extension : extension},function(event){
	    	  	    	        	 $("select",div).val(event.data.extension);
	    	  	    	        	 $("tr",search).removeClass("selected");
	    	  	    	        	 event.data.tr.addClass("selected");
	    	  	    	        	 purchase.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 purchase.price = purchase.year * pricing[event.data.extension];
	    	  	    	        	 purchase.extension = event.data.extension;
	    	  	    	        	 search.hide();
	    	  	    	        	 const wizard = $(".search-wizard");
	    	  	    	        	 wizard.css("top",10).show();
	    	  	    	        	 purchase.action = "transfer";
	    	  	    	        	 purchase.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("purchase",JSON.stringify(purchase));
	    	  	    	        	 $(".domain-name").html(purchase.domain).val(purchase.domain);
	    	  	    	        	 $(".domain-year").html(purchase.year).val(purchase.year);
	    	  	    	        	 $(".domain-price").html(pricing[event.data.extension].toLocaleString("fr-FR")).val(pricing[event.data.extension].toLocaleString("fr-FR"));
	    	  	    	        	 $(".domain-amount").html(purchase.price.toLocaleString("fr-FR")).val(purchase.price.toLocaleString("fr-FR"));
	    	  	    	        	 $(".epp-code").show();
	    	  	    	          });
	    	  	    	          tbody.append(tr);
	       	  	    	          tr.addClass("unavailable");
	       	  	    	          tbody.append(tr);
	    	  	    	    	}
	    	  	    	    }
	    	  	    	}
	    	  	    	$(".domain-name",search).html(domain+"."+purchase.extension);
	    	  	    	if(result[purchase.extension]){
	    	  	    		$(".domain-availability",search).removeClass("green").html("indisponible").addClass("red");
	    	  	    		$(".fa-check-circle-o",search).removeClass("green");
	    	  	    	}else{
	    	  	    		$(".domain-availability",search).removeClass("red").html("disponible").addClass("green");
	    	  	    		$(".fa-check-circle-o",search).addClass("green");
	    	  	    	}
    	  	    	}else {
    	  	    		alert("le nom fourni est invalide");
    	  	    	}
    	  	     },
    	  	     error : function(){
    	  	    	page.release();
    	  	    	alert("erreur lors de la connexion au serveur");
    	  	     }
    	  	});
    	}else {
    		alert("vous devez choisir votre domaine web",function(){
    			button.prev().find("input").val("").focus();
    		});
    	}
    	return false;
    });
};