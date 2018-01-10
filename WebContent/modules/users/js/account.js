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
	
	$(".profile form").submit(function(event){
		const form = $(this);
		const user = {};
		user.structure = {};
		user.name = form.find("input[name=name]").val().trim();
		user.email = form.find("input[name=email]").val().trim();
		user.telephone = form.find("input[name=telephone]").val().trim();
		user.profession = form.find("input[name=profession]").val().trim();
		user.structure.name = form.find("input[name=structure]").val().trim();
		user.structure.business = form.find("input[name=business]").val().trim();
		user.structure.ninea = form.find("input[name=ninea]").val().trim();
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(user),
			  contentType : "application/json",
			  success: function(response) {
				  if(response.status){
					  form.find("input[type=password]").val("");
					  alert("votre profil a &edot;t&edot; bien modifi&edot;");
					  $(".profile-details #name").html("&nbsp;"+user.name);
					  $(".profile-details #email").html("&nbsp;"+user.email);
					  $(".profile-details #telephone").html("&nbsp;"+user.telephone);
					  $(".profile-details #profession").html("&nbsp;"+user.profession);
					  $(".profile-details #structure").html("&nbsp;"+user.structure.name);
					  $(".profile-details #business").html("&nbsp;"+user.structure.business);
					  $(".profile-details #ninea").html("&nbsp;"+user.structure.ninea);
					  $(".profile-details").show();
					  $(".profile-edition").hide();
					  $(".user a").show();
					  if(page.updateUserName) page.updateUserName(user.name);
				  }else{
					  alert("cet email est d&edot;ja utilis&edot; par un autre utilisateur",function(){
						  form.find("input[name=email]").select().focus();
					  });
				  }
				  page.release();
			  },
			  error : function(){
				  page.release();
				  alert("erreur lors de la connexion au serveur");
			  },
			  dataType: "json"
		});
		return false;
	});
	
	$("legend a").click(function(event){
		const window = $(".window").show();
		$("form input[type=email]",window).val("");
	});
	
	$(".window form").submit(function(event){
		const window = $(".window").hide();
		const form = $(this);
		const user = {};
		user.email = form.find("input[name=email]").val().trim();
		page.wait({top : form.offset().top});
		$.ajax({
			  type: "POST",
			  url: form.attr("action"),
			  data: JSON.stringify(user),
			  contentType : "application/json",
			  success: function(response) {
				  if(response.id){
					  form.find("input[type=password]").val("");
					  user.id = response.id;
					  page.table.addRow(user,function(){
						  page.release();
						  alert("votre collaborateur a &edot;t&edot; bien ajout&edot;");
					  });
				  }else{
					  alert("cet email est d&edot;ja utilis&edot; par un autre utilisateur",function(){
						  window.show();
						  form.find("input[name=email]").select().focus();
					  });
				  }
				  page.release();
			  },
			  error : function(){
				  page.release();
				  alert("erreur lors de la connexion au serveur");
			  },
			  dataType: "json"
		});
		return false;
	});
	
});
