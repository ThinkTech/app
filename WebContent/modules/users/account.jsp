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
									<i class="fa fa-user" aria-hidden="true"></i>
								</div>
								<div>
								   <fieldset class="profile-details">
								        <span class="text-right">Prénom et Nom </span>
										<span id="name">${user.name}</span>
									    <span class="text-right">Email </span>
										<span id="email">${user.email}</span>
										<span class="text-right">Profession </span>
   										<span id="profession">${user.profession}</span>
										<span class="text-right">Téléphone </span>
										<span id="telephone">${user.telephone}</span>
										<span class="text-right">Role </span>
   										<span id="role">${user.role}</span>
										<span class="text-right">Structure </span>
   										<span id="structure">${user.structure.name}</span>
   										<span class="text-right">Ninea </span>
   										<span id="ninea">${user.structure.ninea}</span>
   								 </fieldset>
   								 <a class="text-center">[ modifier ]</a>
   								 <form action="${url}/profile/update">
   								 <fieldset class="profile-edition">
   								    	<span class="text-right">Prénom et Nom </span>
										<input type="text" name="name" value="${user.name}" required>
									    <span class="text-right">Email </span>
										<input type="email" name="email" value="${user.email}" required>
										<span class="text-right">Profession </span>
   										<input name="profession" type="text" value="${user.profession}" required>
										<span class="text-right">Téléphone </span>
										<input name="telephone" value="${user.telephone}" type="text">
										<span class="text-right">Role </span>
   										<span>${user.role}</span>
										<span class="text-right">Structure </span>
   										<input name="structure" value="${user.structure.name}" type="text" required>
   										<span class="text-right">Ninea </span>
   										<input name="ninea" value="${user.structure.ninea}" type="text" required>
   								    <div class="submit">
   								      <input type="submit" value="Modifier">
   								      <input type="button" value="Annuler">
   								    </div>
   								 </fieldset>
   								</form>
								</div>
							</div>
					     </div>
				      </div>
					  	<div class="clearfix"></div>
				   <!--//third-one-->
			    </div>
</div>
<script src="${js}/account.js" defer></script>