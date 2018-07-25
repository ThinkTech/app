document.addEventListener("DOMContentLoaded", function(event) {
	page.details.bind = function(container,project) {
		page.details.updateProjectStatus(project);
		$("[data-status='"+project.priority+"']",container).show();
		if(project.plan == "social") $(".confirm p",container).html("Contrat").addClass("adjust");
		if(project.status == "in progress") {
			$(".confirm .imgcircle,.confirm .line,.process .imgcircle",container).addClass("active");
			if(project.progression >= 70) {
				$(".process .line,.quality .imgcircle",container).addClass("active");
		    }
		    if(project.progression >= 90) {
				$(".quality .imgcircle,.quality .line,.delivery .imgcircle",container).addClass("active");
		    }
		}else if(project.status == "stand by") {
			$(".plan-edit,.priority-edit",container).show();
			$(".startedOn",container).hide().next().hide();
		}else{
			$("a.refresh",container).hide();
			$("legend a",container).hide();
			$(".imgcircle,.line",container).addClass("active");
		}
		$("a.document-list-ol",container).click(function(){
			$(".document-list ol",container).show();
			$(".tree,.icons",container).hide();
			container.find(".document-list").show();
			container.find(".document-upload").hide();
		}).hide();
		$("a.document-list-tree",container).click(function(){
			container.find(".document-list").show();
			container.find(".document-upload").hide();
			page.details.showDocumentsTree(project.documents);
		}).hide();
		$("a.document-list-icons",container).click(function(){
			container.find(".document-list").show();
			container.find(".document-upload").hide();
			page.details.showDocumentsIcons(project.documents);
		}).hide();
		$("a.refresh",container).click(function(){
			const div = $(".info-tasks",container);
			const visible = div.is(":visible");
			if(visible){
				page.details.refresh(function(project){
					$(".info-tasks",container).show();
					page.details.updateProjectStatus(project);
				});
			}else{
				page.details.refresh(function(project){
					page.details.updateProjectStatus(project);
				});
			}
		});
		if(project.description){
			const list = $(".description .message-list",container);
			list.find("h6").hide();
			$("> div",list).html(project.description);
		}
		if(project.comments && project.comments.length) page.details.showComments(project.comments);
		if(project.documents && project.documents.length) page.details.showDocuments(project.documents);
		if(project.tasks) {
			const showMessage = function(link,event){
				 $(".info-message").hide();
				 const info = link.parent().prev();
				 var left = event.pageX-info.width()-50;
				 left = left < 0 ? 5 : left;
				 info.css({top : event.pageY-20,left : left}).show();
			};
			const ol = $(".info-tasks ol",container);
			page.render(ol,project.tasks,true,function(){
				for(var i = 0; i<project.tasks.length;i++){
					if(project.tasks[i]){
						const li = $('li[data-name="'+project.tasks[i].name+'"]',ol);
						$("span[data-status='"+project.tasks[i].status+"']",li).show();
						$("a",li).click(function(event){
							 showMessage($(this),event);
							 return false;
						}).on("mouseover",function(event){
							 showMessage($(this),event);
						}).on("mouseout",function(event){
							$(".info-message").hide();
						});
					}
				}
			});
		}
		$("a.tasks",container).click(function(event) {
			$(".info-tasks",container).toggle();
		});
		$("a.duration",container).click(function(event) {
			$(".info-message").hide();
			const div = $(this).next(".info-message");
			$("p",div).hide();
			$("p[data-status='"+project.status+"']",div).show();
			div.css({top : event.pageY-20,left : event.pageX-div.width()-40}).toggle();
			return false;
		});
		$(".description form",container).submit(function(event){
			page.details.updateDescription($(this));
			return false;
		});
		$(".document-upload > form",container).submit(function(event){
			page.details.uploadDocuments($(this));
			return false;
		});
		$(".comments form",container).submit(function(event){
		    page.details.addComment($(this));
			return false;
		});
		$(".documents .document-upload input[type=file]",container).on("change",function(){
			const input = $(this);
			const val = input.val();
			var found;
			$.each($(".documents .document-list li > a",container),function(i,node){
				const name = $(node).text().trim();
				if(val.toLowerCase().indexOf(name.toLowerCase())!=-1){
					found = true;
					input.val("");
				}
			});
			if(found) alert("un fichier portant ce nom existe d&edot;ja");
		});
		$(".info-message").click(function(event){
			return false;
		});
	};
	$(".window a.read-terms").click(function(event) {
			$(".window .terms").show();
	});
	$(".window > div > form").submit(function(event){
			page.details.createProject($(this));
			return false;
	});
	page.details.updateDescription = function(form){
		const project = page.details.entity;
		project.description =  tinyMCE.get("textarea-description").getContent();
		if(tinyMCE.get("textarea-description").getContent({format: 'text'}).trim() == ""){
			alert("vous devez entrer une description",function(){
				tinyMCE.get("textarea-description").focus();
			});
			return false;
		}
		page.wait({top : form.offset().top});
		const url = form.attr("action");
		app.post(url,project,function(response){
			if(response.status){
				  form.find("input[type=button]").click();
				  const div = form.parent().parent();
				  const list = $(".message-list",div);
				  list.find("h6").hide();
				  $("> div",list).html(project.description);
				  alert("votre description a &edot;t&edot; bien modifi&edot;e");
			  }
		});
	};
	page.details.uploadDocuments = function(form){
		page.wait({top : form.offset().top});
		const project = page.details.entity;
		const project_id = project.id;
		const structure_id = project.structure_id;
		const files = new Array();
		const date = new Date();
		const author = form.find("input[name=author]").val();
		var count = 0;
		project.documents ? project.documents : new Array(); 
		$.each($("input[type=file]",form),function(i,node){
		  const input = $(node);
		  const file = {};
		  file.name = input.val();
		  if(file.name){
			file.name = file.name.split(/(\\|\/)/g).pop();
			file.project_id = project_id;
			file.size = node.files[0].size;
		  	files.push(file);
		  	project.documents.push(file);
		  	count++;
		  }
		  file.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
		  file.date+=" "+(date.getHours()<10 ? "0"+date.getHours() : date.getHours())+":"+(date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes())+":"+(date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds());
		  file.author = author;
		});  
		$.ajax({
			  xhr: function() {
			    const xhr = new window.XMLHttpRequest();
			    xhr.addEventListener("progress", function(evt) {
				  if(evt.lengthComputable) {
			        percentComplete = evt.loaded / evt.total;
			        percentComplete = parseInt(percentComplete * 100);
			        if(percentComplete == 100){
			        	form.find("input[type=button]").click(); 
						const div = form.parent().parent();
						const list = $(".document-list",div);
						list.find("h6").hide();
					    page.details.showDocuments(files,function(){
						  page.release();
						  if(count>1){
							  alert("vos documents ont &edot;t&edot; bien envoy&edot;s");
						  }else {
							  alert("votre document a &edot;t&edot; bien envoy&edot;");
						  }
						  $("ol",list).show();
						  $(".tree,.icons",list).hide();
					   });
			        }
			      }
			    }, false);
			    return xhr;
			  },
			  type: "POST",
			  enctype: 'multipart/form-data',
			  url: form.attr("action")+"?project_id="+project_id+"&structure_id="+structure_id,
			  data: new FormData(form[0]),
			  contentType : false,
			  cache: false,
			  processData:false,
			  success: function(response) {
				  $("input[type=file]",form).val(""); 
				  const url  = form.find("input[name=url]").val();
				  const upload = {};
				  upload.id = project_id;
				  upload.documents = files;
				  app.post(url,upload);
			  },
			  error : function(){
				  page.release();
				  alert("erreur lors de la connexion au serveur");
			  },
			  dataType : "json"
		});
	};
	page.details.showDocuments = function(documents,callback){
		 const list = $(".documents .document-list");
		 $(".document-list-ol,.document-list-tree,.document-list-icons").show();
		 list.find("h6").hide();
		 const showMessage = function(link,event){
			 $(".info-message").hide();
			 const info = link.parent().prev();
			 info.css({top : event.pageY-20,left : event.pageX-info.width()-50}).toggle(); 
		 };
		 page.render($("ol",list).addClass("not-empty"),documents,true,function(li){
		    $("> span > a",li).click(function(event){
		    	showMessage($(this),event);
		    	return false;
			});
		    $("> span > a",li).on("mouseover",function(event){
		    	 showMessage($(this),event);
			});
		    $("> span > a",li).on("mouseout",function(event){
		    	$(".info-message").hide();
			});
		    $("> a",li).click(function(event){
				 const href = $(this).attr("href");
				 confirm("&ecirc;tes vous s&ucirc;r de vouloir t&edot;l&edot;charger ce document?",function(){
					 location.href = href;
				 });
				 return false;
			 });
		 });
		 if(callback) callback();
	};
	page.details.showDocumentsTree = function(documents,callback){
		 const div = $(".documents");
		 $(".document-list ol,.document-list .icons",div).hide();
		 const tree = $(".tree",div);
		 const docs = $(".tree-docs",tree).empty();
		 const images = $(".tree-images",tree).empty();
		 const id = page.details.entity.id;
		 for(var i = 0; i <documents.length;i++){
			 var name = documents[i].name.toLowerCase();
			 const li = $("<li><a class='tree_label'/></li>");
			 const link = $("a",li).html('<i class="fa fa-file" aria-hidden="true"></i> '+name).attr("href",page.details.url+"/projects/documents/download?name="+name+"&project_id="+id);
			 link.click(function(event){
				 const href = $(this).attr("href");
				 confirm("&ecirc;tes vous s&ucirc;r de vouloir t&edot;l&edot;charger ce document?",function(){
					 location.href = href;
				 });
				 return false;
			 });
			 if(name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".jpeg") || name.endsWith(".jpg")){
				 images.append(li);
			 }else {
				 docs.append(li);
			 }
		 }
		 tree.show();
		 if(callback) callback();
	};
	page.details.showDocumentsIcons = function(documents,callback){
		 const root = $(".documents");
		 $(".document-list ol,.tree",root).hide();
		 const icons = $(".icons",root).empty();
		 const url = icons.data("path");
		 const id = page.details.entity.id;
		 for(var i = 0; i <documents.length;i++){
			 var name = documents[i].name.toLowerCase();
			 const div = $("<div/>");
			 const img = $("<img/>");
			 div.append(img);
			 div.append($("<a/>").html(name).attr("href",page.details.url+"/projects/documents/download?name="+name+"&project_id="+id));
			 $("a",div).click(function(event){
				 const href = $(this).attr("href");
				 confirm("&ecirc;tes vous s&ucirc;r de vouloir t&edot;l&edot;charger ce document?",function(){
					 location.href = href;
				 });
				 return false;
			 });
			 icons.append(div);
			 if(name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".jpeg") || name.endsWith(".jpg")){
				 img.attr("src",page.details.url+"/projects/documents/download?name="+name+"&project_id="+id);
				 img.css("cursor","pointer");
				 img.click(function(event){
					 $(".modal",root).remove();
					 const modal = $("<img class='modal'/>").attr("src",$(this).attr("src")).appendTo(root);
					 modal.css("top",event.pageY-100);
					 return false;
				 });
				 
			 }else {
				img.attr("src",url+"images/document.png");
			 }
		 }
		 icons.show();
		 if(callback) callback();
	};
	page.details.createProject = function(form){
		const project = {};
		project.service = "webdev";
		project.user_id = form.find("input[name=user]").val();
		project.subject = form.find("select[name=subject]").val();
		project.plan =  form.find("select[name=plan]").val();
		project.domain_id =  form.find("select[name=domain]").val();
		const select = form.find("select[name=domain]");
		if(select.is(":visible")){
			project.domain =  select.find("option:selected").text();
			if(!project.domain_id){
				alert("vous devez choisir un domaine web",function(){
					form.find("select[name=domain]").focus();
				});
				return false;
			}
			project.domainCreated = true;
		}else {
			const order = JSON.parse(localStorage.getItem('order'));
			if(!order){
				alert("vous devez rechercher un domaine web",function(){
					form.find("input[name=domain]").focus();
				});
				return false;
			}
			project.domain = order.domain;
			project.price = order.price;
			project.year = order.year;
			project.extension = order.extension;
			project.action = order.action;
			if(project.action == 'transfer'){
				project.eppCode = form.find("input[name=eppCode]").val();
				if(!project.eppCode.trim()){
					alert("vous devez entrer votre EPP code",function(){
						form.find("input[name=eppCode]").focus();
					});
					return false;
				}
			}
		}
		project.priority =  form.find("select[name=priority]").val();
		project.description =  tinyMCE.activeEditor.getContent();
		if(!tinyMCE.activeEditor.getContent({format: 'text'}).trim()){
			alert("vous devez entrer une description",function(){
				tinyMCE.activeEditor.focus();
			});
			return false;
		}
		const date = new Date();
		project.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
		confirm("&ecirc;tes vous s&ucirc;r de vouloir cr&edot;&edot;r ce projet?",function(){
			const url = "https://thinktech-platform.herokuapp.com/services/order";
			page.form.hide();
			const top = form.offset().top+200;
			page.wait({top : top});
			app.post(url,project,function(response){
				if(response.entity){
					  tinyMCE.activeEditor.setContent("");
					  project.id = response.entity.id;
					  project.subject = response.entity.subject;
					  page.table.addRow(project,function(){
						  page.release();
						  var h3 = $("h3.total");
						  h3.html(parseInt(h3.text())+1);
						  h3 = $("h3.unactive");
						  h3.html(parseInt(h3.text())+1);
						  alert("votre projet est en attente de traitement");
						  if(project.domainCreated) {
							  select.find("option:selected").remove();
						  }else {
							  form.find("input[name=domain]").val("");
						  }
					  });
				}
			});
		});
	};
	page.details.updateProjectStatus = function(project) {
		const tr = $(".table tr[id="+project.id+"]");
		if(project.status == "finished"){
			$("span.label",tr).html("termin&edot;").removeClass().addClass("label label-success");
		}else if(project.status == "in progress"){
			$("span.label",tr).html("en cours").removeClass().addClass("label label-danger");
		}
		$(".badge",tr).html(+project.progression+"%");
		var h3 = $("h3.active");
		h3.html($(".table").find("span.label-danger").length);
		h3 = $("h3.unactive");
		h3.html($(".table").find("span.label-info").length);
	};
	page.details.addComment = function(form){
		const comment = {};
		comment.message =  tinyMCE.get("textarea-message").getContent();
		if(tinyMCE.get("textarea-message").getContent({format: 'text'}).trim() == ""){
			alert("vous devez entrer votre commentaire",function(){
				tinyMCE.get("textarea-message").focus();
			});
			return false;
		}
		comment.project =  page.details.entity.id;
		comment.author =  form.find("input[name=author]").val();
		const date = new Date();
		comment.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
		comment.date+=" "+(date.getHours()<10 ? "0"+date.getHours() : date.getHours())+":"+(date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes())+":"+(date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds());
		comment.icon = "user";
		page.wait({top : form.offset().top});
		const url = form.attr("action");
		app.post(url,comment,function(response){
			if(response.status){
				  page.release();
				  tinyMCE.get("textarea-message").setContent("");
				  form.find("input[type=button]").click();
				  page.details.showComments([comment]);
			}
		});
	};
	page.details.showComments = function(comments){
		const list = $(".comments .message-list");
		list.find("h6").hide();
		const showMessage = function(link,event){
			$(".info-message").hide();
			 const info = link.parent().prev();
			 info.css({top : event.pageY-20,left : event.pageX-info.width()-50}).toggle();
		};
		page.render($("> div",list), comments, true, function(div) {
			$("a",div).click(function(event){
				 showMessage($(this),event);
				 return false;
			});
			$("a",div).on("mouseover",function(event){
				 showMessage($(this),event);
				 return false;
			});
			$("a",div).on("mouseout",function(event){
				$(".info-message").hide();
				 return false;
			});
	   });
	};
	
	$(".window.details").click(function(){
		$(".modal").remove();
	});
	$(".window.form select[name=subject]").on("change",function(){
		const val = $(this).val();
		const select = $(".window.form select[name=plan]");
		if(val.indexOf("application")!=-1){
			$("option",select).hide().attr('disabled','disabled');
			$("option:last",select).show().removeAttr('disabled').prop('selected', true);
		}else{
			$("option",select).show().removeAttr('disabled');
			$("option:last",select).hide().attr('disabled','disabled');
			$("option:first",select).prop('selected', true);
		}
	});
	
	$(".domain-search").hide().prev().hide().prev().hide();
	$(".cancel-domain-search").hide();
	$(".show-domain-search").click(function(){
		$(this).hide().prev().hide().prev().hide();
		$(this).next().show().next().show().next().show();
		$(".cancel-domain-search").show();
	});
	$(".cancel-domain-search").click(function(){
		$(this).hide().prev().hide().prev().hide().prev().hide();
		$(".show-domain-search").show().prev().show().prev().show();
	});
	
	 $(".domain-search").click(function(event){
		    const div = $(".window.form");
	    	$(".search").hide();
	    	const button = $(this);
	    	const top = button.offset().top;
	    	const pricing = {};
	    	pricing.com = 10000;
	    	pricing.net = 10000;
	    	pricing.org = 10000;
	    	pricing.biz = 10000;
	    	pricing.info = 10000;
	    	pricing.tv = 20000;
	    	pricing.press = 20000;
	    	pricing.news = 20000;
	    	pricing.tech = 25000;
	    	const input = $(".window.form input[name=domain]");
	    	const order = {};
	  		order.year = 1;
	  		order.search = input.val().toLowerCase();
	    	var domain = order.search.replace(/\s+/g, '');
	    	order.extension = domain.indexOf(".")!=-1 ? domain.substring(domain.indexOf(".")+1,domain.length) : "com";
	    	order.extension = pricing[order.extension] ? order.extension : "com";
	    	if(domain){
	    		const index = domain.indexOf(".");
	    		if(domain.indexOf(".")!=-1) domain = domain.substring(0,index);
	    		input.val(domain);
	    		const url = "https://thinktech-platform.herokuapp.com/domains/search?domain="+domain;
	    		page.wait({top : top-20});
	    		app.get(url,function(response){
	    		 	var result = response["1"].result;
		  	    	if(result){
		  	    		const search = $(".search-results").css("top",10).show();
		  	    		search.parent().css("height",$('body').height()+"px").show();
	    	  	    	const tbody = $("table tbody",search).empty();
	    	  	    	var tr;
	    	  	    	var i;
	    	  	    	var extension;
	    	  	    	const clone = {};
	    	  	    	clone[order.extension] = result[order.extension];
	    	  	    	for (extension in result) {
	    	  	    	    if(result.hasOwnProperty(extension)) {
	    	  	    	    	if(extension!=order.extension){
	    	  	    	    		clone[extension] = result[extension];
	    	  	    	    	}
	    	  	    	    }
	    	  	    	}
	    	  	    	result = clone;
	    	  	    	for (extension in result) {
	    	  	    	    if(result.hasOwnProperty(extension)) {
	    	  	    	    	if(!result[extension]){
	    	  	    	          tr = $("<tr/>");
	    	  	    	          if(order.extension == extension){
	    	  	    	        	tr.addClass("selected").append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
	    	  	    	          }else{
	    	  	    	        	tr.append("<td>"+domain+"."+extension+"</td>");
	    	  	    	          }
	    	  	    	          var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<1;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{tr : tr,td : td, price : pricing[extension]},function(event){
	    	  	    	        	  order.year = parseInt($(this).val());
	    	  	    	        	  order.price = event.data.price * order.year;
	    	  	    	        	  event.data.td.find("span").html(order.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	        	  $("tr",search).removeClass("selected");
	    	  	    	        	  event.data.tr.addClass("selected");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Acheter</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{tr : tr,td : td,extension : extension},function(event){
	    	  	    	        	 $("tr",search).removeClass("selected");
	    	  	    	        	 event.data.tr.addClass("selected");
	    	  	    	        	 order.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 order.price = order.year * pricing[event.data.extension];
	    	  	    	        	 order.extension = event.data.extension;
	    	  	    	        	 search.parent().hide();
	    	  	    	        	 order.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("order",JSON.stringify(order));
	    	  	    	        	 $(".domain-name").html(order.domain).val(order.domain);
	    	  	    	        	 $(".epp-code").hide();
	    	  	    	          });
	    	  	    	          tbody.append(tr);
	    	  	    	    	}else {
	    	  	    	    	 tr = $("<tr/>");
		    	  	    	     if(order.extension == extension){
		    	  	    	        tr.addClass("selected").append("<td><i class='fa fa-check-circle-o' aria-hidden='true'></i> "+domain+"."+extension+"</td>");
		    	  	    	     }else{
		    	  	    	        tr.append("<td>"+domain+"."+extension+"</td>");
		    	  	    	      }
		    	  	    	      var td = $("<td><span>"+pricing[extension].toLocaleString("fr-FR") +" CFA</span></td>");
	    	  	    	          var select = $("<select></select>");
	    	  	    	          for(i=0;i<1;i++){
	    	  	    	        	select.append("<option value='"+(i+1)+"'"+">"+(i+1)+" an</option>");
	    	  	    	          }
	    	  	    	          select.on("change",{tr : tr,td : td, price : pricing[extension]},function(event){
	    	  	    	        	  order.year = parseInt($(this).val());
	    	  	    	        	  order.price = event.data.price * order.year;
	    	  	    	        	  event.data.td.find("span").html(order.price.toLocaleString("fr-FR")+" CFA");
	    	  	    	        	  $("tr",search).removeClass("selected");
	    	  	    	        	  event.data.tr.addClass("selected");
	    	  	    	          });
	    	  	    	          td.append(select);
	    	  	    	          td.append("<a class='buy'>Transf&eacute;rer</a>");
	    	  	    	          tr.append(td);
	    	  	    	          $("a",tr).on("click",{tr : tr,td : td,extension : extension},function(event){
	    	  	    	        	 $("tr",search).removeClass("selected");
	    	  	    	        	 event.data.tr.addClass("selected");
	    	  	    	        	 order.year = parseInt(event.data.td.find("select").val());
	    	  	    	        	 order.price = order.year * pricing[event.data.extension];
	    	  	    	        	 order.extension = event.data.extension;
	    	  	    	        	 search.parent().hide()
	    	  	    	        	 order.action = "transfer";
	    	  	    	        	 order.domain = domain+"."+event.data.extension;
	    	         	  	    	 localStorage.setItem("order",JSON.stringify(order));
	    	  	    	        	 $(".domain-name").html(order.domain).val(order.domain);
	    	  	    	        	 $(".epp-code").show();
	    	  	    	          });
	    	  	    	          tbody.append(tr);
	       	  	    	          tr.addClass("unavailable");
	       	  	    	          tbody.append(tr);
	    	  	    	    	}
	    	  	    	    }
	    	  	    	}
	    	  	    	$(".domain-name",search).html(domain+"."+order.extension);
	    	  	    	if(result[order.extension]){
	    	  	    		$(".domain-availability",search).removeClass("green").html("indisponible").addClass("red");
	    	  	    		$(".fa-check-circle-o",search).removeClass("green");
	    	  	    	}else{
	    	  	    		$(".domain-availability",search).removeClass("red").html("disponible").addClass("green");
	    	  	    		$(".fa-check-circle-o",search).addClass("green");
	    	  	    	}
		  	    	}else {
		  	    		alert("le nom fourni est invalide");
		  	    	}	
	    		});
	    	}else {
	    		alert("vous devez choisir votre domaine web",function(){
	    			button.prev().val("").focus();
	    		});
	    	}
	    	return false;
	    });
	    
	    $(".search .close").click(function(event){
	  	   $(this).parent().parent().hide();
		});
	    
	    $(".epp-code").hide();
	    
	    $(".buttons a").click(function(event){
		  	  const div = $(".window.form");
		  	  if(!div.find("select[name=domain] option").length){
		  		$(".show-domain-search").trigger("click");
		  		$(".cancel-domain-search").hide();
		  	  }
	    });
	    
	    localStorage.removeItem("order");
	    head.load("js/tinymce/tinymce.min.js");
});