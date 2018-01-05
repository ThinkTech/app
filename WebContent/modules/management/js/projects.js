$(document).ready(function(){
	page.details.bind = function(container,project) {
		$("[data-status='"+project.priority+"']",container).show();
		if(project.status == "in progress") {
			$(".confirm .imgcircle,.confirm .line,.process .imgcircle",container).addClass("active");
			if(project.progression >= 95) {
				$(".quality .imgcircle,.quality .line",container).addClass("active");
			}
		}
		if(project.status == "finished") {
			$("legend a",container).hide();
			$(".imgcircle,.line",container).addClass("active");
		}
		if(project.description){
			const list = $(".description .message-list",container);
			list.find("h6").hide();
			$("> div",list).html(project.description);
		}
		if(project.comments.length) page.details.showComments(project.comments);
		if(project.documents.length) page.details.showDocuments(project.documents);
		if(project.tasks) {
			const ol = $(".info-tasks ol",container);
			page.render(ol,project.tasks,true,function(){
				for(var i = 0; i<project.tasks.length;i++){
					if(project.tasks[i]){
						const li = $('li[data-name="'+project.tasks[i].name+'"]',ol);
						$("span[data-status='"+project.tasks[i].status+"']",li).show();
						$("a",li).click(function(event){
							 $(".info-message").hide();
							 const info = $(this).parent().prev();
							 info.css({top : event.pageY-20,left : event.pageX-info.width()-50}).toggle();
							 return false;
						});
					}
				}
			});
		}
		$("a.pay",container).click(function(event) {
			page.details.hide();
			const top = $(this).offset().top;
			page.wait({top : top});
			head.load("modules/payment/js/wizard.js",function() {
			    page.wizard.show(project.bill,top,function(){
			    	const tr = $(".table tr[id="+project.id+"]");
					$("span.label",tr).html("en cours").removeClass().addClass("label label-danger");
					$(".badge",tr).html("5%");
					var h3 = $("h3.unactive");
					h3.html(parseInt(h3.text())-1);
					h3 = $("h3.active");
					h3.html(parseInt(h3.text())+1);
					h3 = $("h3.unpayed");
					h3.html(parseInt(h3.text())-1);
			    });
			});
		});
	    if(project.plan == "plan social") {
	    	$("a.pay",container).hide().prev().hide().prev().hide();
	    }
		$("a.plan",container).click(function(event) {
			const plan = $(this).data("plan");
			const plans = $(".plans");
			$(".pricing",plans).hide();
		    const top = event.pageY;
		    plans.css("top",top-30);
			$("div[data-plan='"+plan+"']",plans).show();
			plans.toggle();
			return false;
		});
		$(".plans").hide();
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
	};
	$(".window a.read-terms").click(function(event) {
			$(".window .terms").show();
	});
	$(".window > div > form").submit(function(event){
			page.details.createProject($(this));
			return false;
	});
	page.details.updateDescription = function(form){
		const project = {};
		project.description =  tinyMCE.get("textarea-description").getContent();
		if(tinyMCE.get("textarea-description").getContent({format: 'text'}).trim() == ""){
			alert("vous devez entrer une description",function(){
				tinyMCE.get("textarea-description").focus();
			});
			return false;
		}
		project.id =  form.find("input[name=id]").val();
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(project),
			  contentType : "application/json",
			  success: function(response) {
				  page.release();
				  if(response.status){
					  form.find("input[type=button]").click();
					  const div = form.parent().parent();
					  const list = $(".message-list",div);
					  list.find("h6").hide();
					  $("> div",list).html(project.description);
					  alert("votre description a &edot;t&edot; bien modifi&edot;e");
				  }
			  },
			  dataType: "json"
		});
	};
	page.details.uploadDocuments = function(form){
		page.wait({top : form.offset().top});
		 const project_id  =  form.find("input[name=project_id]").val();
		 const structure_id  =  form.find("input[name=structure_id]").val();
		$.ajax({
			  xhr: function() {
			    const xhr = new window.XMLHttpRequest();
			    const span = $("<span class='progression'/>").appendTo($("#wait"));
			    xhr.upload.addEventListener("progress", function(evt) {
			      span.html("0%");
			      if(evt.lengthComputable) {
			        var percentComplete = evt.loaded / evt.total;
			        percentComplete = parseInt(percentComplete * 100);
			        span.html(percentComplete+"%");
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
				  form.find("input[type=button]").click();
				  const author = form.find("input[name=author]").val();
				  const div = form.parent().parent();
				  const list = $(".document-list",div);
				  list.find("h6").hide();
				  var count = 0;
				  const files = new Array();
				  const date = new Date();
				  $.each($("input[type=file]",form),function(i,node){
					  const input = $(node);
					  const file = {};
					  file.name = input.val();
					  if(file.name) {
						file.name = file.name.split(/(\\|\/)/g).pop();
						file.project_id = project_id;
					  	files.push(file);
					  	input.val(""); 
					  	count++;
					  }
					  file.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
					  file.date+=" "+(date.getHours()<10 ? "0"+date.getHours() : date.getHours())+":"+(date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes())+":"+(date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds());
					  file.author = author;
				  });
				  page.details.showDocuments(files,function(){
					  page.release();
					  $("#wait .progression").remove();
					  if(count>1){
						  alert("vos documents ont &edot;t&edot; bien envoy&edot;s");
					  }else {
						  alert("votre document a &edot;t&edot; bien envoy&edot;");
					  }
				  });
				  const url  = form.find("input[name=url]").val();
				  const upload = {};
				  upload.id = project_id;
				  upload.documents = files;
				  $.ajax({
						  type: "POST",
						  url: url,
						  data: JSON.stringify(upload),
						  contentType : "application/json",
						  success: function(response) {
						  },
						  dataType: "json"
					});
			  },
			  dataType : "json"
		});
	};
	page.details.showDocuments = function(documents,callback){
		 const list = $(".documents .document-list");
		 list.find("h6").hide();
		 page.render($("ol",list).addClass("not-empty"),documents,true,function(div){
		    $("span a",div).click(function(event){
		    	$(".info-message").hide();
				 const info = $(this).parent().prev();
				 info.css({top : event.pageY-20,left : event.pageX-info.width()-50}).toggle();
				 return false;
			});
		 });
		 if(callback) callback();
	};
	page.details.createProject = function(form){
		const project = {};
		project.service = form.find("select[name=service]").val();
		project.subject = form.find("select[name=subject]").val();
		project.plan =  form.find("select[name=plan]").val();
		project.priority =  form.find("select[name=priority]").val();
		project.description =  tinyMCE.activeEditor.getContent();
		if(tinyMCE.activeEditor.getContent({format: 'text'}).trim() == ""){
			alert("vous devez entrer une description",function(){
				tinyMCE.activeEditor.focus();
			});
			return false;
		}
		const date = new Date();
		project.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
		confirm("&ecirc;tes vous s&ucirc;r de vouloir cr&edot;&edot;r ce projet?",function(){
			page.form.hide();
			const top = form.offset().top+200;
			page.wait({top : top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(project),
				  contentType : "application/json",
				  success: function(response) {
					  if(response.id){
						  tinyMCE.activeEditor.setContent("");
						  project.id = response.id;
						  page.table.addRow(project,function(){
							  page.release();
							  var h3 = $("h3.total");
							  h3.html(parseInt(h3.text())+1);
							  h3 = $("h3.unactive");
							  h3.html(parseInt(h3.text())+1);
							  alert("votre projet a &edot;t&edot; bien cr&edot;&edot;",function(){
					    	      page.details.showProjectWizard(project);  
							  });
						  });
					  }
				  },
				  dataType: "json"
			});
		});
	};
	page.details.showProjectWizard = function(project){
		 if(project.plan != "plan social"){
			  const wizard = $(".project-wizard");
			  const url = wizard.data("url");
			  page.render(wizard, project, false, function() {
				  $("> div section:nth-child(1)",wizard).show();
				  wizard.fadeIn(100);
				  $("> div section:nth-child(1) input[type=button]",wizard).click(function(event) {
						const input = $("input[type=checkbox]",wizard);
						if(input.is(":checked")){
							page.wait({top : top});
							$.ajax({
								  type: "GET",
								  url: url+"?id="+project.id,
								  success: function(response) {
									  const bill = response.entity;
									  head.load("modules/payment/js/wizard.js",function() {
										    page.wizard.show(bill,top,function(){
										    	const tr = $(".table tr[id="+project.id+"]");
												$("span.label",tr).html("en cours").removeClass().addClass("label label-danger");
												$(".badge",tr).html("5%");
												var h3 = $("h3.unactive");
												h3.html(parseInt(h3.text())-1);
												h3 = $("h3.active");
												h3.html(parseInt(h3.text())+1);
												$("> div section:nth-child(1)",wizard).hide();
												$("> div section:nth-child(2)",wizard).show();
												wizard.fadeIn(100);
										    });
										    page.release();
									 });
								  },
								  dataType: "json"
							});
						}
						wizard.hide();
				});
				  
				$("> div section:nth-child(2) input[type=button]",wizard).click(function(event) {
					  wizard.hide();
				});  
				  
			 });
	     }
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
		comment.project =  form.find("input[name=id]").val();
		comment.author =  form.find("input[name=author]").val();
		const date = new Date();
		comment.date = (date.getDate()>=10?date.getDate():("0"+date.getDate()))+"/"+(date.getMonth()>=10?(date.getMonth()+1):("0"+(date.getMonth()+1)))+"/"+date.getFullYear();
		comment.date+=" "+(date.getHours()<10 ? "0"+date.getHours() : date.getHours())+":"+(date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes())+":"+(date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds());
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(comment),
			  contentType : "application/json",
			  success: function(response) {
				  if(response.status){
					  page.release();
					  tinyMCE.get("textarea-message").setContent("");
					  form.find("input[type=button]").click();
					  page.details.showComments([comment]);
				  }
			  },
			  dataType: "json"
		});
	};
	page.details.showComments = function(comments){
		const list = $(".comments .message-list");
		list.find("h6").hide();
		page.render($("> div",list), comments, true, function(div) {
			$("a",div).click(function(event){
				$(".info-message").hide();
				 const info = $(this).parent().prev();
				 info.css({top : event.pageY-20,left : event.pageX-info.width()-50}).toggle();
				 return false;
			});
	   });
	};
	
	$(".window.details").click(function(){
		$(".plans").hide();
	});
});