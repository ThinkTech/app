$(document).ready(function(){
	page.details.bind = function(container,entity) {
		$(".messages form",container).submit(function(event){
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
						  page.render($("> div",list), comment, true, function() {
							  page.release();
							  alert("votre message a &edot;t&edot; bien ajout&edot;");
						  });
					  }
				  },
				  dataType: "json"
			});
			return false;
		 });
		};

		$(".window > div > form").submit(function(event){
			const form = $(this);
			const ticket = {};
			ticket.subject = form.find("input[name=subject]").val();
			ticket.service =  form.find("select[name=service]").val();
			ticket.priority =  form.find("select[name=priority]").val();
			ticket.message =  form.find("textarea[name=message]").val();
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
							  $("input[type=text],textarea",form).val("");
							  page.table.addRow(ticket,function(row){
								  row.click(function(event) {
										const div = $(".details").show();
										return false;
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