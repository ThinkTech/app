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
	
	page.initDomainSearch();
	
	$(".search .close").click(function(event){
  	   $(this).parent().parent().hide();
	});
	
	 $(".buttons .cancel").click(function(event){
	   $(".modal").hide();
	});
	 
 });

page.initDomainSearch = function(){
	$(".search label").click(function(event){
   	    $(this).prev().prop("checked", true);
	});
    $(".search-wizard .finish").click(function(event){
    	$(".modal").hide();
    	const val = $(".search-wizard input:checked").val();
    	var div = $("."+val);
    	if(div.hasClass("modal")){
    		div.css("height",$('body').height()+"px").show();
    		div.show();
    		div = $(".subscribe-form",div);
    		div.css("top",$(this).offset().top+200).show();
    	}
    	const purchase = JSON.parse(localStorage.getItem('purchase'));
    	$("input[name=structure]").val(purchase.search);
  	    $('html,body').animate({scrollTop:div.offset().top-20},300);
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
    	  	    		const search = $(".search-results").css("top",20).show();
    	  	    		search.parent().css("height",$('body').height()+"px").show();
        	  	    	$('html,body').animate({scrollTop:20},300);
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
	    	  	    	        	tr.append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
	    	  	    	          }else{
	    	  	    	        	tr.append("<td>"+domain+"."+extension+"</td>");
	    	  	    	          }
	    	  	    	          var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<10;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{td : td, price : pricing[extension]},function(event){
	    	  	    	        	  purchase.year = parseInt($(this).val());
	    	  	    	        	  purchase.price = event.data.price * purchase.year;
	    	  	    	        	  event.data.td.find("span").html(purchase.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Acheter</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{td : td,extension : extension},function(event){
	    	  	    	        	 $("select",div).val(event.data.extension);
	    	  	    	        	 purchase.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 purchase.price = purchase.year * pricing[event.data.extension];
	    	  	    	        	 purchase.extension = event.data.extension;
	    	  	    	        	 search.hide();
	    	  	    	        	 const wizard = $(".search-wizard");
	    	  	    	        	 if(button.data("wizard")!="hide"){
	    	  	    	        		wizard.css("top",20).show(); 
	    	  	    	        		$('html,body').animate({scrollTop:20},300);
	    	  	    	        	 }else{
	    	  	    	        		search.parent().hide();
	    	  	    	        		$('html,body').animate({scrollTop:top-150},300);
	    	  	    	        	 }
	    	  	    	        	 purchase.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("purchase",JSON.stringify(purchase));
	    	  	    	        	 $(".domain-name").html(purchase.domain).val(purchase.domain);
	    	  	    	        	 $(".domain-year").html(purchase.year).val(purchase.year);
	    	  	    	        	 $(".domain-price").html(pricing[event.data.extension]).val(pricing[event.data.extension]);
	    	  	    	        	 $(".domain-amount").html(purchase.price.toLocaleString("fr-FR")).val(purchase.price.toLocaleString("fr-FR"));
	    	  	    	        	 $(".epp-code").removeAttr("required").hide();
	    	  	    	          });
	    	  	    	          tbody.append(tr);
	    	  	    	    	}else {
	    	  	    	    	 tr = $("<tr/>");
		    	  	    	     if(purchase.extension == extension){
		    	  	    	        tr.append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
		    	  	    	     }else{
		    	  	    	        tr.append("<td>"+domain+"."+extension+"</td>");
		    	  	    	      }
		    	  	    	      var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<10;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{td : td, price : pricing[extension]},function(event){
	    	  	    	        	  purchase.year = parseInt($(this).val());
	    	  	    	        	  purchase.price = event.data.price * purchase.year;
	    	  	    	        	  event.data.td.find("span").html(purchase.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Transf&eacute;rer</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{td : td,extension : extension},function(event){
	    	  	    	        	 $("select",div).val(event.data.extension);
	    	  	    	        	 purchase.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 purchase.price = purchase.year * pricing[event.data.extension];
	    	  	    	        	 purchase.extension = event.data.extension;
	    	  	    	        	 search.hide();
	    	  	    	        	 const wizard = $(".search-wizard");
	    	  	    	        	 if(button.data("wizard")!="hide"){
	    	  	    	        		wizard.css("top",top).show(); 
	    	  	    	        		$('html,body').animate({scrollTop:top-30},300);
	    	  	    	        	 }else{
	    	  	    	        		search.parent().hide();
	    	  	    	        		$('html,body').animate({scrollTop:top-150},300);
	    	  	    	        	 }
	    	  	    	        	 purchase.action = "transfer";
	    	  	    	        	 purchase.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("purchase",JSON.stringify(purchase));
	    	  	    	        	 $(".domain-name").html(purchase.domain).val(purchase.domain);
	    	  	    	        	 $(".domain-year").html(purchase.year).val(purchase.year);
	    	  	    	        	 $(".domain-price").html(pricing[event.data.extension].toLocaleString("fr-FR")).val(pricing[event.data.extension].toLocaleString("fr-FR"));
	    	  	    	        	 $(".domain-amount").html(purchase.price.toLocaleString("fr-FR")).val(purchase.price.toLocaleString("fr-FR"));
	    	  	    	        	 $(".epp-code").attr("required","true").show();
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