jQuery(document).ready(function( $ ) {
	$(".password-form").submit(function(event){
		const form = $(this);
		const user = {};
		user.password = form.find("input[name=password]").val();
		user.confirm =  form.find("input[name=confirm]").val();
		if(user.password != user.confirm) {
			alert("les deux mots de passe ne sont pas identiques",function(){
				form.find("input[name=password]").focus();
			});
			return false;
		}
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(user),
			  contentType : "application/json",
			  success: function(response) {
				  if(response.status){
					  form.find("input[type=password]").val("");
					  alert("votre mot de passe a &edot;t&edot; bien modifi&edot;");
				  }
				  page.release();
			  },
			  dataType: "json"
		});
		return false;
	});
	
	$(".user a").click(function(event){
		$(".profile-details").hide();
		$(".profile-edition").show();
		$(this).hide();
		return false;
	});
	
	$(".profile-edition input[type=button]").click(function(event){
		$(".profile-details").show();
		$(".profile-edition").hide();
		$(".user a").show();
	});
	
	$(".profile-edition form").submit(function(event){
		$(".profile-details").show();
		$(".profile-edition").hide();
		$(".user a").show();
		const form = $(this);
		const profile = {};
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(profile),
			  contentType : "application/json",
			  success: function(response) {
				  if(response.status){
					  form.find("input[type=password]").val("");
					  alert("votre profil a &edot;t&edot; bien modifi&edot;");
				  }
				  page.release();
			  },
			  dataType: "json"
		});
		return false;
	});
});
