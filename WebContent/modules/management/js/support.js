$(document).ready(function(){
	page.details.bind = function(container,ticket) {
		if(ticket.status == "finished") {
			$("legend a",container).hide();
		}
		if(ticket.comments){
			 const list = $(".message-list",container);
			 list.find("h6").hide();
			 page.details.render($("> div",list),ticket.comments);
		}
		$(".messages form",container).submit(function(event){
			const form = $(this);
			const comment = {};
			comment.message =  tinyMCE.activeEditor.getContent();
			if(tinyMCE.activeEditor.getContent({format: 'text'}).trim() == ""){
				alert("vous devez entrer votre message",function(){
					tinyMCE.activeEditor.focus();
				});
				return false;
			}
			comment.ticket =   form.find("input[name=id]").val();
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(comment),
				  contentType : "application/json",
				  success: function(response) {
					  page.release();
					  if(response.status){
						  tinyMCE.activeEditor.setContent("");
						  form.find("input[type=button]").click();
						  const div = form.parent().parent();
						  const list = $(".message-list",div);
						  list.find("h6").hide();
						  page.render($("> div",list), [comment], true, function() {
							  page.release();
							  alert("votre message a &edot;t&edot; bien ajout&edot;");
						  });
					  }
				  },
				  dataType: "json"
			});
			return false;
		 });
		tinymce.init({ selector:'textarea',language: 'fr_FR',menubar:false,statusbar: false});
		};
		$(".window > div > form").submit(function(event){
			const form = $(this);
			const ticket = {};
			ticket.subject = form.find("input[name=subject]").val();
			ticket.service =  form.find("select[name=service]").val();
			ticket.priority =  form.find("select[name=priority]").val();
			ticket.message =  tinyMCE.activeEditor.getContent();
			if(tinyMCE.activeEditor.getContent({format: 'text'}).trim() == ""){
				alert("vous devez entrer une description",function(){
					tinyMCE.activeEditor.focus();
				});
				return false;
			}
			const date = new Date();
			ticket.date = date.getDay()+"/"+date.getMonth()+"/"+date.getFullYear();
			confirm("&ecirc;tes vous s&ucirc;r de vouloir cr&edot;&edot;r ce ticket?",function(){
				$(".window").hide();
				page.wait({top : form.offset().top});
				$.ajax({
					  type: "POST",
					  url: form.attr("action"),
					  data: JSON.stringify(ticket),
					  contentType : "application/json",
					  success: function(response) {
						  if(response.status){
							  $("input[type=text]",form).val("");
							  tinyMCE.activeEditor.setContent("");
							  ticket.id = response.status;
							  page.table.addRow(ticket,function(row){
								  row.click(function() {
									    page.details.show(ticket);
								  });
								  page.release();
								  alert("votre ticket a &edot;t&edot; bien cr&edot;&edot;");
							  });
							 
						  }
					  },
					  dataType: "json"
				});
			});
			return false;
		});
});