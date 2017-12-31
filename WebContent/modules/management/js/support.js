$(document).ready(function(){
	page.details.bind = function(container,ticket) {
		$("[data-status='"+ticket.priority+"']",container).show();
		if(ticket.status == "finished") $("legend a",container).hide();
		if(ticket.comments.length) page.details.showComments(ticket.comments);
		$(".messages form",container).submit(function(event){
			page.details.addComment($(this));
			return false;
		});
	};
	$(".window > div > form").submit(function(event){
		    page.details.addTicket($(this));
		    return false;
	});
	page.details.addTicket = function(form){
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
		ticket.date = date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
		confirm("&ecirc;tes vous s&ucirc;r de vouloir cr&edot;&edot;r ce ticket?",function(){
			page.form.hide();
			page.wait({top : form.offset().top+200});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(ticket),
				  contentType : "application/json",
				  success: function(response) {
					  if(response.id){
						  $("input[type=text]",form).val("");
						  tinyMCE.activeEditor.setContent("");
						  ticket.id = response.id;
						  page.table.addRow(ticket,function(){
							  page.release();
							  alert("votre ticket a &edot;t&edot; bien cr&edot;&edot;");
							  $.each($(".info-updates h3"),function(i,node){
								  const h3 = $(node);
								  h3.html(parseInt(h3.text())+1);
							  });
						  });
						 
					  }
				  },
				  dataType: "json"
			});
		});
	  };
	  page.details.addComment = function(form) {
		const comment = {};
		comment.message =  tinyMCE.activeEditor.getContent();
		if(tinyMCE.activeEditor.getContent({format: 'text'}).trim() == ""){
			alert("vous devez entrer votre message",function(){
				tinyMCE.activeEditor.focus();
			});
			return false;
		}
		comment.ticket =   form.find("input[name=id]").val();
		comment.author =  form.find("input[name=author]").val();
		const date = new Date();
		comment.date = date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
		comment.date+=" "+(date.getHours()<10 ? "0"+date.getHours() : date.getHours())+":"+(date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes())+":"+(date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds());
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
					  alert("votre message a &edot;t&edot; bien ajout&edot;");
					  page.details.showComments([comment]);
				  }
			  },
			  dataType: "json"
		});
	};
	page.details.showComments = function(comments){
		const list = $(".message-list");
		list.find("h6").hide();
		page.render($("> div",list), comments, true, function(div) {
			page.release();
			$("a",div).click(function(event){
				 $(this).parent().prev().css({top : event.pageY-20,left : event.pageX-400}).toggle();
			});
	   });
	};
});