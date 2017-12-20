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
					<form class="password-form" action="${url}/password/change">
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
									<img src="${images}/user_128.png" alt="" width="128px" height="128px"/>
								</div>
								<div>
								   <fieldset class="profile-details">
								        <span class="text-right">Prénom et Nom </span>
										<span>${user.name}</span>
									    <span class="text-right">Email </span>
										<span>${user.email}</span>
										<span class="text-right">Téléphone </span>
										<span>${user.telephone}</span>
										<span class="text-right">Role </span>
   										<span>${user.role}</span>
   										<span class="text-right">Fonction </span>
   										<span>${user.fonction}</span>
										<span class="text-right">Structure </span>
   										<span>${user.structure.name}</span>
   										<span class="text-right">Ninea </span>
   										<span>${user.structure.ninea}</span>
   								 </fieldset>
   								 <a class="text-center">[ modifier ]</a>
   								 <fieldset class="profile-edition">
   								   <form action="${url}/profile/update">
   								    <span class="text-right">Prénom et Nom </span>
										<input type="text" value="${user.name}" required>
									    <span class="text-right">Email </span>
										<input type="email" value="${user.email}" required>
										<span class="text-right">Téléphone </span>
										<input value="${user.telephone}" type="text">
										<span class="text-right">Role </span>
   										<span>${user.role}</span>
   										<span class="text-right">Fonction </span>
   										<input type="text" value="${user.fonction}" required>
										<span class="text-right">Structure </span>
   										<span>${user.structure.name}</span>
   										<span class="text-right">Ninea </span>
   										<input value="${user.structure.ninea}" type="text" required>
   								    <div class="submit">
   								      <input type="submit" value="Modifier">
   								      <input type="button" value="Annuler">
   								    </div>
   								 </form>
   								 </fieldset>
								</div>
							</div>
					     </div>
				      </div>
					  	<div class="clearfix"></div>
				   <!--//third-one-->
			    </div>
</div>
<script src="${js}/account.js" defer></script>