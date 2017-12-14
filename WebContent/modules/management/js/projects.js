jQuery(document).ready(function($) {
		$("tbody tr").click(function(event) {
			const div = $(".details").show();
			/*const list = $(".documents-list",div);
			if($("ul li",list).length){
				list.find("h6").hide();
			}else {
				list.find("h6").show();
			}*/
			return false;
		});
		$(".buttons a").click(function(event) {
			$(".form").show();
			return false;
		});
		$(".window a.message-add").click(function(event) {
			const div = $(this).parent().next();
			const list = div.find(".message-list");
			if(list.is(":visible")) {
				div.find(".message-edition textarea").val($("> div p",list).html());
			}
			return false;
		});
		
		$(".window a.read-terms").click(function(event) {
			$(".window .terms").show();
		});
		
		$(".window > form").submit(function(event){
			const form = $(this);
			const project = {};
			project.subject = form.find("select[name=subject]").val();
			project.plan =  form.find("select[name=plan]").val();
			project.description =  form.find("textarea[name=description]").val();
			project.structure =  form.find("input[name=structure]").val();
			project.ninea = 1454545;
			project.date ="14/12/2017";
			confirm("&ecirc;tes vous s&ucirc;r de vouloir cr&edot;&edot;r ce projet?",function(){
				$(".window").hide();
				page.wait({top : form.offset().top});
				$.ajax({
					  type: "POST",
					  url: form.attr("action"),
					  data: JSON.stringify(project),
					  contentType : "application/json",
					  success: function(response) {
						  if(response.status){
							  $("textarea",form).val("");
							  page.table.addRow(project,function(row){
								  row.click(function(event) {
										const div = $(".details").show();
										return false;
								  });
								  page.release();
								  alert("votre projet a &edot;t&edot; bien cr&edot;&edot;",function(){
									  const wizard = $(".project-wizard");
									  page.render(wizard, project, false, function() {
										  if(!project.structure) $(".structure-info",wizard).hide()
										  wizard.fadeIn(100);
										  $("input[type=button]",wizard).click(function(event) {
												const input = $("input[type=checkbox]",wizard);
												if(input.is(":checked")){
													const top = input.offset().top-300;
													page.wait({top : top});
													head.load("modules/payment/js/wizard.js",function() {
														const bill = {};
														bill.service = "site web";
														bill.amount = "60 000";
														bill.fee = "caution"
														bill.date = project.date;
													    page.wizard.show(bill,top);
													});
												}
												wizard.hide();
										});
									 });
								  });
							  });
						  }
					  },
					  dataType: "json"
				});
			});
			return false;
		});
		
		$(".window .description form").submit(function(event){
			const form = $(this);
			const project = {};
			project.description =  form.find("textarea[name=description]").val();
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(project),
				  contentType : "application/json",
				  success: function(response) {
					  page.release();
					  if(response.status){
						  $("textarea",form).val("");
						  form.find("input[type=button]").click();
						  const div = form.parent().parent();
						  const list = $(".message-list",div);
						  list.find("h6").hide();
						  $("> div",list).html($("<p/>").html(project.description));
						  alert("votre description a &edot;t&edot; bien modifi&edot;");
					  }
				  },
				  dataType: "json"
			});
			return false;
		});
		
		$(".window .document-upload > form").submit(function(event){
			const form = $(this);
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  enctype: 'multipart/form-data',
				  url: form.attr("action"),
				  data: new FormData(form[0]),
				  contentType : false,
				  cache: false,
				  processData:false,
				  success: function(response) {
					  page.release();
					  form.find("input[type=button]").click();
					  const div = form.parent().parent();
					  const list = $(".document-list",div);
					  list.find("h6").hide();
					  list.append($("<ol/>"));
					  var count = 0;
					  $.each($("input[type=file]",form),function(i,node){
						  const input = $(node);
						  const link = $("<a></a>");
						  const name = input.val();
						  if(name) {
						  	link.html(name.split(/(\\|\/)/g).pop());
						  	$("ol",list).append($("<li/>").append(link));
						  	input.val(""); 
						  	count++;
						  }
					  });
					  if(count>1){
						  alert("vos documents ont &edot;t&edot; bien envoy&edot;s");
					  }else {
						  alert("votre document a &edot;t&edot; bien envoy&edot;");
					  }
				  },
				  dataType : "json"
			});
			return false;
		});
		
		$(".window .comments form").submit(function(event){
			const form = $(this);
			const comment = {};
			comment.message =  form.find("textarea[name=message]").val();
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(comment),
				  contentType : "application/json",
				  success: function(response) {
					  page.release();
					  if(response.status){
						  $("textarea",form).val("");
						  form.find("input[type=button]").click();
						  const div = form.parent().parent();
						  const list = $(".message-list",div);
						  list.find("h6").hide();
						  $("> div",list).append($("<p/>").html(comment.message));
						  alert("votre commentaire a &edot;t&edot; bien ajout&edot;");
					  }
				  },
				  dataType: "json"
			});
			return false;
		});
});