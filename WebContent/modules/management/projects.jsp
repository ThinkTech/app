<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="inner-block">
	<div class="logo-name">
		<h1><i class="fa fa-${activeItem.icon}" aria-hidden="true"></i>${activeItem.label}</h1>
	</div>
	<!--info updates updates-->
	<div class="info-updates">
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-1">
				<div class="col-md-8 info-update-left">
					<h3 class="total">${total}</h3>
					<h4>projets</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-${activeItem.icon}"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-3">
				<div class="col-md-8 info-update-left">
					<h3 class="unactive">${unactive}</h3>
					<h4>projets en attente</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-${activeItem.icon}"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-1">
				<div class="col-md-8 info-update-left">
					<h3 class="active">${active}</h3>
					<h4>projets en cours</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-${activeItem.icon}"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="col-md-12 buttons" style="display:${user.role == 'administrateur' ? 'block' : 'none'}">
        <a><i class="fa fa-plus" aria-hidden="true"></i></a>
    </div>
	<!--info updates end here-->
	<!--mainpage chit-chating-->
	<div class="chit-chat-layer1">
		<div class="col-md-12 chit-chat-layer1-left">
			<div class="work-progres">
				<div class="chit-chat-heading">
					<h3 class="tlt">Vos Projets</h3>
				</div>
				<div class="table-responsive">
					      <table data-url="${url}/projects/info" class="projects table table-hover">
                                  <thead>
                                    <tr>
                                      <th></th>
                                      <th>Projet</th>
                                      <th>Auteur</th>
                                      <th>Date Cr�ation</th>                                                             
                                      <th>Traitement</th>
                                      <th>Progression</th>
                                  </tr>
                              </thead>
                              <tbody>
                              <s:iterator value="#request.projects" var="project" status="status">
	                                <tr id="${project.properties.id}">
	                                  <td><span class="number">${status.index+1}</span></td>
	                                  <td>${project.properties.subject}</td>
	                                  <td>${project.properties.author}</td>
	                                  <td><s:date name="properties.date" format="dd/MM/yyyy" /></td>                                        
	                                  <td><span class="label ${project.properties.status=='in progress' ? 'label-danger' : '' } ${project.properties.status=='finished' ? 'label-success' : '' } ${project.properties.status=='stand by' ? 'label-info' : '' }">
	                                  ${project.properties.status=='in progress' ? 'en cours' : '' } ${project.properties.status=='finished' ? 'termin�' : '' } ${project.properties.status=='stand by' ? 'en attente' : '' }
	                                  </span></td>
	                                  <td><span class="badge badge-info">${project.properties.progression}%</span></td>
	                              </tr>
	                          </s:iterator>               
                              <template>
							     {#.}
							      <tr id="{id}">
							            <td><span class="number"></span></td>
							   	        <td>{subject}</td>
							            <td>${user.name}</td>
		                                <td>{date}</td>           
		                                <td><span class="label label-info">en attente</span></td>
		                                <td><span class="badge badge-info">0%</span></td>
							   	    </tr>
							     {/.}
							   </template>
                          </tbody>
                      </table>
                      <div class="empty"><span>aucun projet</span></div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
<div class="window details" data-url="${path}/${url}">
    <div>
	<span title="fermer" class="close">X</span>
	<section>
	 <template>
	 <h1><i class="fa fa-${activeItem.icon}" aria-hidden="true"></i>Projet {subject|s}</h1>
	<fieldset>
	    <span class="text-right"><i class="fa fa-user" aria-hidden="true"></i> Auteur </span> <span>{name}</span>
	    <span class="text-right"><i class="fa fa-globe" aria-hidden="true"></i> Domaine </span> <span>{domain}</span>
		<span class="text-right"><i class="fa fa-code" aria-hidden="true"></i> Plan </span> <span>{plan}</span>
		<span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Date Cr�ation </span> <span>{date}</span>
		<span class="text-right"><i class="fa fa-product-hunt" aria-hidden="true"></i> Priorit� </span> 
		<span data-status="normal" class="status" style="display:none">normale</span>
		<span data-status="medium" class="status" style="display:none">moyenne</span>
		<span data-status="high" class="status" style="display:none">�lev�e</span> 
		<span class="text-right"><i class="fa fa-calendar-check-o" aria-hidden="true"></i> Dur�e </span> <span>{duration} mois</span>
		<span class="text-right"><i class="fa fa-tasks" aria-hidden="true"></i> Traitement </span> 
		<span data-status="stand by" style="display:none"><span class="label label-info">en attente</span> <span class="label label-info">paiement</span></span>
		<span data-status="in progress" style="display:none"><span class="label label-danger">en cours</span></span>  
		<span data-status="finished" style="display:none"><span class="label label-success">termin�</span></span>
		<span class="text-right"><i class="fa fa-tasks" aria-hidden="true"></i> Progression </span> <span class="badge badge-info">{progression}%</span> <a class="tasks"><i class="fa fa-info" aria-hidden="true"></i></a> <a class="refresh"><i class="fa fa-refresh" aria-hidden="true"></i></a>
		<div class="info-tasks">
		   <h1><i class="fa fa-tasks" aria-hidden="true"></i> T�ches&nbsp;&nbsp;
			  <a class="task-list-ol"><i class="fa fa-list-ol" aria-hidden="true"></i></a>
		  </h1>
		  <ol data-template="tasks">
		  </ol>
		  <div class="col-md-12">
		  <div class="content-process">
			<div class="content3">
				<div class="shipment">
					<div class="confirm">
						<div class="imgcircle">
							<img src="${images}/confirm.png">
						</div>
						<span class="line"></span>
						<p>Contrat et Caution</p>
					</div>
					<div class="process">
						<div class="imgcircle">
							<img src="${images}/process.png">
						</div>
						<span class="line"></span>
						<p>D�veloppement</p>
					</div>
					<div class="quality">
						<div class="imgcircle">
							<img src="${images}/quality.png">
						</div>
						<span class="line"></span>
						<p>Tests et Validation</p>
					</div>
					<div class="delivery">
						<div class="imgcircle">
							<img src="${images}/delivery.png">
						</div>
						<p>Livraison Produit</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		   </div>	
	   </div>
		</div>
	</fieldset>
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
   		 	<div class="message-edition description">
   		 	    <form action="${url}/projects/description/update">
   		 		<textarea id="textarea-description" name="description">{description}</textarea>
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
	   <a class="document-list-ol"><i class="fa fa-list-ol" aria-hidden="true"></i></a>
	   <a class="document-list-tree"><i class="fa fa-list-alt" aria-hidden="true"></i></a>
	   <a class="document-list-icons"><i class="fa fa-th-list" aria-hidden="true"></i></a>
	   </legend>
	   <div class="documents">
	        <div class="document-list">
   		 		<h6>pas de documents</h6>
   		 		<ol data-template="documents">
   		 		</ol>
			   <ul class="tree">
				  <li>
				    <input type="checkbox" checked="checked" id="c1" />
				    <label class="tree_label" for="c1">Documents</label>
				    <ul class="tree-docs">
				    </ul>
				  </li>
				  <li>
				    <input type="checkbox" checked="checked" id="c2" />
				    <label class="tree_label" for="c2">Images</label>
				    <ul class="tree-images">
				    </ul>
				  </li>
			 </ul>
			 <div class="icons" data-path="${path}"></div>
   		 	</div>
   		 	<div class="document-upload">
   		 	  <form method="POST" enctype="multipart/form-data" action="documents/upload.html">
   		 	   <fieldset>
   		 	    <span class="text-right"><i class="fa fa-file"></i> Document 1 </span> <input name="file1" type="file" required>
				<span class="text-right"><i class="fa fa-file"></i> Document 2 </span> <input name="file2" type="file">
				<span class="text-right"><i class="fa fa-file"></i> Document 3 </span> <input name="file3" type="file">
				<input name="url" type="hidden" value="${url}/projects/documents/save"/>
				<input name="author" type="hidden" value="${user.name}">
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
				<div data-template="comments"></div>
				<br>
			</div>
   		 	<div class="message-edition">
   		 	   <form action="${url}/projects/comments/create">
   		 		<textarea id="textarea-message" name="message"></textarea>
   		 		<input name="author" type="hidden" value="${user.name}">
   		 		<div class="submit">
			      <input type="submit" value="Ajouter">
			      <input type="button" value="Annuler">
			    </div>
			   </form>
   		 	</div>
   		 </div>
  </fieldset>
  </template>
  </section>
  <template id="template-documents">
		{#.}
			<li>
				<a href="${url}/projects/documents/download?name={name}&project_id={project_id}"><i class="fa fa-file" aria-hidden="true"></i> {name}</a>
				<div class="info-message">
	   	  	    	<b>Auteur :</b> {author}<br>
	   	  	    	<b>Date :</b> {date}<br>
	   	  	    	<b>Taille :</b> {size}
	   	  		</div>
	   	  		<span><a><i class="fa fa-info" aria-hidden="true"></i></a></span>
			</li>
		 {/.}
  </template>
  <template id="template-comments">
      {#.}
	      <div>
	        <i class="fa fa-user" aria-hidden="true"></i> 
	   	  	<div class="message">{message|s}</div>
	   	  	<div class="info-message">
	   	  	    <b>Auteur :</b> {author}<br>
	   	  	    <b>Date :</b> {date}
	   	  	</div>
	   	  	<span><a><i class="fa fa-info" aria-hidden="true"></i></a></span>
	   	  </div>
   	  {/.}
  </template>
  <template id="template-tasks">
    {#.}
    <li data-name="{name}">
      <span><i class="fa fa-tasks" aria-hidden="true"></i> {name|s}</span> 
      <span data-status="stand by" style="display:none"><span class="label label-info">en attente</span></span>  
      <span data-status="in progress" style="display:none"><span class="label label-danger">en cours</span></span>
	  <span data-status="finished" style="display:none"><span class="label label-success">termin�</span></span>
      <span class="badge badge-info">{progression}%</span>
      <div class="info-message">
	   	  {description|s}
	  </div>
      <span class="question"><a><i class="fa fa-question" aria-hidden="true"></i></a></span>
      <div class="info-message">
	   	  {info|s}
	  </div>
      <span><a><i class="fa fa-info" aria-hidden="true"></i></a></span>
    </li>
	{/.}  
  </template>	
  </div>
</div>
<div class="window form">
 <div>
   <span title="fermer" class="close">X</span>
	<h1><i class="fa fa-${activeItem.icon}" aria-hidden="true"></i>Nouveau Projet</h1>
   <form action="https://thinktech-platform.herokuapp.com/services/projects/create">
	<fieldset>
	    <span class="text-right"><i class="fa fa-ticket" aria-hidden="true"></i> Service </span>
		<select name="service">
		  <option value="webdev">webdev</option>
		</select>
	  	<span class="text-right"><i class="fa fa-file-code-o" aria-hidden="true"></i> Projet </span>
	    <select name="subject">
		  <option value="cr�ation site web">cr�ation site web</option>
		  <option value="refonte site web">refonte site web</option>
		  <option value="cr�ation application web">cr�ation application web</option>
		  <option value="refonte application web">refonte application web</option>
		</select>
		<span class="text-right"><i class="fa fa-code" aria-hidden="true"></i> Plan </span>
		<select name="plan">
		  <option value="plan business">plan business</option>
		  <option value="plan corporate">plan corporate</option>
		  <option value="plan personal">plan personal</option>
		  <option value="plan social">plan social</option>
		  <option value="plan custom">plan custom</option>
		</select>
		<span class="text-right"><i class="fa fa-product-hunt" aria-hidden="true"></i> Priorit� </span>
		<select name="priority">
		  <option value="normal">normale</option>
		  <option value="medium">moyenne</option>
		  <option value="high">�lev�e</option>
		</select>
		<span class="text-right full"><i class="fa fa-file-text-o" aria-hidden="true"></i> Description du projet</span>
		<textarea style="height:150px" name="description" placeholder="entrer votre description"></textarea>
	    <input type="hidden" name="user" value="${user.id}">
	    <input type="hidden" name="structure" value="${user.structure.id}">
	</fieldset>
	<div class="submit">
		 <input type="submit" value="Cr��r">
		 <input type="button" value="Annuler">
	</div>
	</form>
	</div>
</div>
<div class="window project-wizard" data-url="${url}/projects/bill">
  <template>
  <div> 
  <section>
    <span>Merci pour votre souscription au {plan}</span>
	<h2><span class="number">1</span> Etape 1 : Contrat et Caution</h2>
	<div class="col-md-12">
		  <div class="content-process">
			<div class="content3">
				<div class="shipment">
					<div class="confirm">
						<div class="imgcircle">
							<img src="${images}/confirm.png">
						</div>
						<span class="line"></span>
						<p>Contrat et Caution</p>
					</div>
					<div class="process">
						<div class="imgcircle">
							<img src="${images}/process.png">
						</div>
						<span class="line"></span>
						<p>D�veloppement</p>
					</div>
					<div class="quality">
						<div class="imgcircle">
							<img src="${images}/quality.png">
						</div>
						<span class="line"></span>
						<p>Tests et Validation</p>
					</div>
					<div class="delivery">
						<div class="imgcircle">
							<img src="${images}/delivery.png">
						</div>
						<p>Livraison Produit</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		   </div>	
	   </div>
	   <p>
	    Vos informations fournies seront utilis�es pour g�n�rer le contrat vous liant � ThinkTech et ce dernier sera ajout� aux documents du projet. La facture pour le paiement de la caution a �t� cr��e. Vous pouvez choisir d'effectuer le paiement maintenant en cliquant sur le bouton Terminer afin que votre projet soit trait� au plus vite par notre �quipe de d�veloppement. 
	    <span class="terms-agreement">
	      <input type="checkbox" checked> Payer la caution
	    </span>
	   </p>
	   <div class="submit">
		 <input type="button" value="Terminer" style="float:right">
	</div>
  </section>
  <section>
   <span>Merci pour votre souscription au {plan}</span>
	<h2><span class="number">2</span> Etape 2 : D�veloppement</h2>
	<div class="col-md-12">
		  <div class="content-process">
			<div class="content3">
				<div class="shipment">
					<div class="confirm">
						<div class="imgcircle active">
							<img src="${images}/confirm.png">
						</div>
						<span class="line active"></span>
						<p>Contrat et Caution</p>
					</div>
					<div class="process">
						<div class="imgcircle active">
							<img src="${images}/process.png">
						</div>
						<span class="line"></span>
						<p>D�veloppement</p>
					</div>
					<div class="quality">
						<div class="imgcircle">
							<img src="${images}/quality.png">
						</div>
						<span class="line"></span>
						<p>Tests et Validation</p>
					</div>
					<div class="delivery">
						<div class="imgcircle">
							<img src="${images}/delivery.png">
						</div>
						<p>Livraison Produit</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		   </div>	
	   </div>
	   <p>Le contrat vous liant � ThinkTech a �t� g�n�r� et votre projet a �t� transmis � notre �quipe technique pour traitement. La dur�e du projet est estim�e � 3 mois et vous pouvez bien entendu suivre son �volution. Nous vous contacterons sous peu pour de 
	      plus amples informations ou pour fournir des documents que vous pouvez attacher au projet.
	   </p>
	   <div class="submit">
		 <input type="button" value="Terminer" style="float:right">
	</div>
  </section>
  </div>
  </template>
</div>
</div>
<script src="${js}/projects.js" defer></script>
<script src="js/tinymce/tinymce.min.js" defer></script> 