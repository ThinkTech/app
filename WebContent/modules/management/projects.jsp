<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="inner-block">
	<div class="logo-name">
		<h1><i class="fa fa-briefcase" aria-hidden="true"></i>Vos Projets</h1>
	</div>
	<!--info updates updates-->
	<div class="info-updates">
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-1">
				<div class="col-md-8 info-update-left">
					<h3>${total}</h3>
					<h4>projets</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-briefcase"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-3">
				<div class="col-md-8 info-update-left">
					<h3>${unactive}</h3>
					<h4>nouveaux projets</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-briefcase"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-3">
				<div class="col-md-8 info-update-left">
					<h3>${active}</h3>
					<h4>projets en cours</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-briefcase"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="col-md-12 buttons">
        <a><i class="fa fa-plus" aria-hidden="true"></i></a>
    </div>
	<!--info updates end here-->
	<!--mainpage chit-chating-->
	<div class="chit-chat-layer1">
		<div class="col-md-12 chit-chat-layer1-left">
			<div class="work-progres">
				<div class="chit-chat-heading">
					<h3 class="tlt">
						<i class="fa fa-briefcase" aria-hidden="true"></i> Vos Projets
					</h3>
				</div>
				<div class="table-responsive">
					      <table class="projects table table-hover">
                                  <thead>
                                    <tr>
                                      <th></th>
                                      <th>Projet</th>
                                      <th>Plan</th> 
                                      <th>Date</th>                                                             
                                      <th>Traitement</th>
                                      <th>Progression</th>
                                  </tr>
                              </thead>
                              <tbody>
                              <s:iterator value="#request.projects" var="project" status="status">
	                                <tr>
	                                  <td><span class="number">${status.index+1}</span></td>
	                                  <td><i class="fa fa-briefcase" aria-hidden="true"></i> ${project.properties['subject']}</td>
	                                  <td><i class="fa fa-code" aria-hidden="true"></i> ${project.properties['plan']}</td>
	                                  <td>17/09/2017</td>                                        
	                                  <td><span class="label ${project.properties['status']=='in progress' ? 'label-danger' : '' } ${project.properties['status']=='finished' ? 'label-success' : '' } ${project.properties['status']=='stand by' ? 'label-info' : '' }">
	                                  ${project.properties['status']=='in progress' ? 'en cours' : '' } ${project.properties['status']=='finished' ? 'terminé' : '' } ${project.properties['status']=='stand by' ? 'en attente' : '' }
	                                  </span></td>
	                                  <td><span class="badge badge-info">${project.properties['progression']}%</span></td>
	                              </tr>
	                          </s:iterator>
                             
                              <template type="text/x-dust-template">
							     {#.}
							      <tr id="{id}">
							            <td><span class="number"></span></td>
							   	        <td><i class="fa fa-briefcase" aria-hidden="true"></i> {subject}</td>
							            <td><i class="fa fa-code" aria-hidden="true"></i> {plan}</td>
		                                <td>{date}</td>           
		                                <td><span class="label label-info">en attente</span></td>
		                                <td><span class="badge badge-info">0%</span></td>
							   	    </tr>
							     {/.}
							   </template>
                          </tbody>
                      </table>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="window details">
	<span title="fermer" class="close">X</span>
	<h1><i class="fa fa-briefcase" aria-hidden="true"></i>Projet #1</h1>
	<fieldset>
	    <span class="text-right">Objet </span> <span>création site web</span>
		<span class="text-right">Plan </span> <span>plan business</span>
		<span class="text-right">Structure </span> <span>Sesame</span>
		<span class="text-right">Date </span> <span>17/09/2017</span>
		<span class="text-right">Traitement </span> <span class="label label-info">en attente</span><br>
		<span class="text-right">Progression </span> <span class="badge badge-info">0%</span>
	</fieldset>
	<div class="col-md-12">
		  <div class="content-process">
			<div class="content3">
				<div class="shipment">
					<div class="confirm">
						<div class="imgcircle">
							<img src="${images}/confirm.png" alt="confirm order">
						</div>
						<span class="line"></span>
						<p>Contrat et Caution</p>
					</div>
					<div class="process">
						<div class="imgcircle">
							<img src="${images}/process.png" alt="process order">
						</div>
						<span class="line"></span>
						<p>Développement</p>
					</div>
					<div class="quality">
						<div class="imgcircle">
							<img src="${images}/quality.png" alt="quality check">
						</div>
						<span class="line"></span>
						<p>Tests et Validation</p>
					</div>
					<div class="delivery">
						<div class="imgcircle">
							<img src="${images}/delivery.png" alt="delivery">
						</div>
						<p>Livraison Produit</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		   </div>	
	   </div>
	   <div class="clearfix"></div>
	<fieldset>
	   <legend>
	     <i class="fa fa-file-text-o"></i> Description <a class="message-add"><i class="fa fa-edit" aria-hidden="true"></i></a>
	   </legend>
	   <div class="description messages">
	        <div class="message-list">
   		 		<h6>pas de description</h6>
   		 		<div></div>
   		 	</div>
   		 	<div class="message-edition">
   		 	    <form action="${url}/projects/description/update">
   		 		<textarea name="description" placeholder="entrer votre description" required></textarea>
   		 		<div class="submit">
			      <input type="submit" value="Modifier">
			      <input type="button" value="Annuler">
			    </div>
			    </form>
   		 	</div>
   		 </div>
	</fieldset>
	<fieldset>
	   <legend>
	   <i class="fa fa-file"></i> Documents <a class="document-add"><i class="fa fa-plus" aria-hidden="true"></i></a>
	   </legend>
	   <div class="documents">
	        <div class="document-list">
   		 		<h6>pas de documents</h6>
   		 	</div>
   		 	<div class="document-upload">
   		 	  <form method="POST" enctype="multipart/form-data" action="documents/upload.html">
   		 	   <fieldset>
   		 	    <span class="text-right">Document 1 </span> <input name="file1" type="file" required>
				<span class="text-right">Document 2 </span> <input name="file2" type="file">
				<span class="text-right">Document 3 </span> <input name="file3" type="file">
				</fieldset>
   		 		<div class="submit">
			      <input type="submit" value="Envoyer">
			      <input type="button" value="Annuler">
			    </div>
			  </form>  
   		 	</div>
   	   </div>
	</fieldset>
	<fieldset>
        <legend>
    	<i class="fa fa-comments"></i> Commentaires <a class="message-add"><i class="fa fa-plus" aria-hidden="true"></i></a>
   		</legend>
   		 <div class="comments messages">
   		    <div class="message-list">
   		 		<h6>pas de commentaires</h6>
   		 		<div></div>
   		 	</div>
   		 	<div class="message-edition">
   		 	   <form action="${url}/projects/comments/create">
   		 		<textarea name="message" placeholder="entrer votre commentaire" required></textarea>
   		 		<div class="submit">
			      <input type="submit" value="Ajouter">
			      <input type="button" value="Annuler">
			    </div>
			   </form>
   		 	</div>
   		 </div>
  </fieldset>
</div>
<div class="window form">
   <span title="fermer" class="close">X</span>
	<h1><i class="fa fa-briefcase" aria-hidden="true"></i>Nouveau Projet</h1>
   <form action="${url}/projects/create">
	<fieldset>
	    <span class="text-right">Objet </span>
	    <select name="subject">
		  <option value="création site web">création site web</option>
		</select>
		<span class="text-right">Plan </span>
		<select name="plan">
		  <option value="plan business">plan business</option>
		  <option value="plan corporate">plan corporate</option>
		  <option value="plan personal">plan personal</option>
		  <option value="plan social">plan social</option>
		</select>
		<span class="text-right">Structure </span>
	    <select name="structure">
		  <option value="Sesame">Sesame</option>
		</select>
		<span class="text-right full">Description du projet</span>
		<textarea name="description" placeholder="entrer votre description" required></textarea>
	</fieldset>
	<div class="submit">
		 <input type="submit" value="Créér">
		 <input type="button" value="Annuler">
	</div>
	</form>
</div>
</div>
<script src="${js}/projects.js" defer></script>