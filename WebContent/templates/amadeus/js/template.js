jQuery(document).ready(function( $ ) {
		$('input[name=email]').focus();
		$(".login form").submit(function(event){
			const form = $(this);
			const user = {};
			user.email = form.find("input[name=email]").val();
			user.password = form.find("input[name=password]").val();
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(user),
				  contentType : "application/json",
				  success: function(response) {
					  location.href = response.url;
				  },
				  dataType: "json"
			});
			return false;
		});
		$(".recover form").submit(function(event){
			const form = $(this);
			const user = {};
			user.email = form.find("input[name=email]").val();
			page.wait({top : form.offset().top});
			$.ajax({
				  type: "POST",
				  url: form.attr("action"),
				  data: JSON.stringify(user),
				  contentType : "application/json",
				  success: function(response) {
					  alert("un message vous a été envoyé à l'adresse fournie");
					  page.release();
				  },
				  dataType: "json"
			});
			return false;
		});
		$(".login a").click(function(){
			$(".login").hide();
			$(".recover").show();
		});
		$(".recover a").click(function(){
			$(".login").show();
			$(".recover").hide();
		});
		if('serviceWorker' in navigator) {
			navigator.serviceWorker.register('sw.js');
		};
});