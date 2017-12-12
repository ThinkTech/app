<div class="inner-block">
 <div class="logo-name">
	<h1><i class="fa fa-user" aria-hidden="true"></i>Votre Compte</h1> 								
 </div>
<!--web-forms-->
			    <div class="web-forms">
				 <!--first-one-->
				 <div class="col-md-4 first-one">
				  <div class="first-one-inner">
				     <h3 class="tittle">Mot de Passe</h3>
					<form class="password-form" action="users/password/change">
						<input type="password" name="password" class="text" required>
						<input type="password" name="confirm" required>
						<div class="submit"><input type="submit" value="Changer" ></div>
					</form>
				   </div>
			      </div>
				 
				   <!--/third-one-->
				   <div class="col-md-6 first-one">
					    <div class="first-one-inner lost">
						    <div class="user profile">
								<div class="profile-bottom">
									<img src="${images}/user_64.png" alt="" width="128px" height="128px"/>
									<h4>Profil</h4>
								</div>
								<div>
								   <fieldset class="profile-details">
								        <span class="text-right">Prénom et Nom </span>
										<span>Malorum</span>
									    <span class="text-right">Email </span>
										<span>email</span>
										<span class="text-right">Téléphone </span>
										<span>téléphone</span>
										<span class="text-right">Role </span>
   										<span>administrateur</span>
										<span class="text-right">Structure </span>
   										<span>Sesame</span>
   										<span class="text-right">Ninea </span>
   										<span>ninea</span>
   								 </fieldset>
   								 <a class="text-center">[ modifier ]</a>
   								 <fieldset class="profile-edition">
   								    <span class="text-right">Prénom et Nom </span>
										<input type="text">
									    <span class="text-right">Email </span>
										<input type="email">
										<span class="text-right">Téléphone </span>
										<input type="text">
										<span class="text-right">Role </span>
   										<span>administrateur</span>
										<span class="text-right">Structure </span>
   										<input type="text">
   										<span class="text-right">Ninea </span>
   										<input type="text">
   								    <div class="submit">
   								      <input type="submit" value="Modifier">
   								      <input type="submit" value="Annuler">
   								    </div>
   								 </fieldset>
								</div>
							</div>
					     </div>
				      </div>
					  	<div class="clearfix"></div>
				   <!--//third-one-->
			    </div>
</div>
<script>
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
					  alert("votre mot de passe a été bien modifié");
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
	$(".profile-edition input[type=submit]").click(function(event){
		$(".profile-details").show();
		$(".profile-edition").hide();
		$(".user a").show();
		return false;
	});
});
</script>