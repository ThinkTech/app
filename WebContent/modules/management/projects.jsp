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
	                                  ${project.properties['status']=='in progress' ? 'en cours' : '' } ${project.properties['status']=='finished' ? 'termin�' : '' } ${project.properties['status']=='stand by' ? 'en attente' : '' }
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
	    <span class="text-right">Objet </span> <span>cr�ation site web</span>
		<span class="text-right">Plan </span> <span>plan business</span>
		<span class="text-right">Structure </span> <span>Sesame</span>
		<span class="text-right">Date </span> <span>17/09/2017</span>
		<span class="text-right">Traitement </span> <span class="label label-info">en attente</span> <span class="label label-info">paiement caution</span> <a class="pay"><i class="fa fa-money"></i></a><br>
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
						<p>D�veloppement</p>
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
   		 	<div class="message-edition description">
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
   		 		<ol>
   		 		   <template type="text/x-dust-template">
					  {#.}
					    <li><a>{name}</a></li>
					  {/.}
				   </template>
   		 		</ol>
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
   		 		<div>
   		 		  <template type="text/x-dust-template">
   		 		     <p class="message">{message}</p>
   		 		     <hr/>
   		 		  </template>
   		 		</div>
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
		  <option value="cr�ation site web">cr�ation site web</option>
		</select>
		<span class="text-right">Plan </span>
		<select name="plan">
		  <option value="plan business">plan business</option>
		  <option value="plan corporate">plan corporate</option>
		  <option value="plan personal">plan personal</option>
		  <option value="plan social">plan social</option>
		</select>
		<span class="text-right">Structure </span>
	    <input name="structure" type="text" value="Sesame" readonly>
	    <span class="text-right">Auteur </span>
	    <input name="author" type="text" value="malorum" readonly>
		<span class="text-right full">Description du projet</span>
		<textarea name="description" placeholder="entrer votre description" required></textarea>
	</fieldset>
	<div class="terms-agreement">
		<input type="checkbox" checked disabled> J'accepte les <a class="read-terms">Termes</a>
	</div>
	<div class="submit">
		 <input type="submit" value="Cr��r">
		 <input type="button" value="Annuler">
	</div>
	
	   <div class="terms">
		     <span class="close">X</span>
			 <h1 class="text-center"><i class="fa fa-sticky-note-o"></i><span>Termes</span></h1>
			 <p>
			   Merci d'utiliser nos produits et services (Services). En utilisant nos Services, vous acceptez ces termes. Lisez-les attentivement. En utilisant nos Services, vous devez suivre les politiques mises � votre disposition dans les Services. N'abusez pas de nos Services. Par exemple, n'interf�rez pas avec nos Services ou essayez d'y acc�der en utilisant une autre m�thode que l'interface et les instructions que nous fournissons. Vous pouvez utiliser nos Services uniquement conform�ment � la loi, y compris les lois et r�glements applicables en mati�re d'exportation et de r�exportation. Nous pouvons suspendre ou arr�ter de vous fournir nos Services si vous ne respectez pas nos termes ou nos politiques ou si nous enqu�tons sur une faute suspect�e. L'utilisation de nos Services ne vous conf�re aucun droit de propri�t� intellectuelle sur nos Services ou le contenu auquel vous acc�dez. Vous ne pouvez utiliser le contenu de nos Services que si vous obtenez l'autorisation de son propri�taire ou si la loi l'autorise autrement. Ces termes ne vous accordent pas le droit d'utiliser des marques ou des logos utilis�s dans nos Services. Ne supprimez pas, obscurcissez ou modifiez les avis l�gaux affich�s dans ou avec nos Services. En ce qui concerne votre utilisation des Services, nous pouvons vous envoyer des annonces de services, des messages administratifs et d'autres informations. Vous pouvez opter pour certaines de ces communications.
			 </p> 
			 <h2 class="text-center"><i class="fa fa-commenting"></i><span>Nos garanties et d�charges</span></h2>
			 <p>
			   Nous fournissons nos Services avec un niveau de comp�tences et de soins commercialement raisonnable et nous esp�rons que vous appr�cierez l'utilisation. Mais il y'a certaines choses que nous ne promettons pas � propos de nos Services. AUTRE QUE CE SOIT EXPRESS�MENT CONFORM� EN CES CONDITIONS OU TERMES SUPPL�MENTAIRES, NI THINKTECH, NI SES FOURNISSEURS OU DISTRIBUTEURS, NE FONT DES PROMESSES SP�CIFIQUES SUR LES SERVICES. PAR EXEMPLE, NOUS NE FAISONS PAS D'ENGAGEMENTS SUR LE CONTENU DANS LES SERVICES, LES FONCTIONS SP�CIFIQUES DES SERVICES OU LEUR FIABILIT�, LA DISPONIBILIT� OU LA CAPACIT� DE R�PONDRE A VOS BESOINS. Nous fournissons les Services tels quels. CERTAINES JURIDICTIONS PR�SENTENT CERTAINES GARANTIES, COMME LA GARANTIE IMPLICITE DE QUALIT� MARCHANDE, L'ADAPTATION � UN USAGE PARTICULIER ET LA NON-CONTREFA�ON. DANS LA MESURE PERMISE PAR LA LOI, NOUS EXCLUONT TOUTES LES GARANTIES.
			 </p>
			 <h2 class="text-center"><i class="fa fa-commenting"></i><span>Nos Responsabilit�s</span></h2>
			 <p>
			  QUAND AUTORIS� PAR LA LOI, LES FOURNISSEURS ET DISTRIBUTEURS DE THINKTECH ET THINKTECH, NE SERONT PAS TENUS RESPONSABLES DES PERTES DE B�N�FICES, DE REVENUS OU DE DONN�ES, DE PERTES FINANCI�RES OU DE DOMMAGES INDIRECTS, SP�CIAUX, CONS�CUTIFS, EXEMPLAIRES OU PUNITIFS. DANS LA MESURE PERMISE PAR LA LOI, LA RESPONSABILIT� TOTALE DE THINKTECH, ET SES FOURNISSEURS ET DISTRIBUTEURS, POUR TOUTES R�CLAMATIONS EN VERTU DE CES MODALIT�S, Y COMPRIS TOUTE GARANTIE IMPLICITE, EST LIMIT�E AU MONTANT QUE VOUS AVEZ PAY� POUR UTILISER LES SERVICES. DANS TOUS LES CAS, THINKTECH, ET SES FOURNISSEURS ET DISTRIBUTEURS, NE SERONT PAS TENUS RESPONSABLES DE TOUTE PERTE OU DOMMAGE QUI NE SERA PAS RAISONNABLEMENT PREVISIBLE.
			 </p>
			 <h2 class="text-center"><i class="fa fa-commenting"></i><span>Usage commercial de nos Services</span></h2>
			 <p>
			     Si vous utilisez nos Services pour le compte d'une structure, celle-ci accepte ces termes. Elle indemnisera ThinkTech et ses soci�t�s affili�es, dirigeants, agents et employ�s de toute r�clamation, poursuite ou action d�coulant ou li�e � l'utilisation des Services ou violation de ces termes, y compris toute responsabilit� ou frais d�coulant de r�clamations, de pertes , les dommages, les poursuites, les jugements, les frais de litige et les honoraires d'avocat.
			 </p>
			 <h2 class="text-center"><i class="fa fa-commenting"></i><span>� propos de ces termes</span></h2>
			 <p>
			   Nous pouvons modifier ces termes ou tous les termes suppl�mentaires qui s'appliquent � un Service, par exemple, refl�ter les modifications apport�es � la loi ou les modifications apport�es � nos Services. Vous devriez consulter les termes r�guli�rement. Nous signalerons les modifications apport�es � ces termes sur cette page. Nous ferons parvenir un avis de conditions suppl�mentaires modifi�es dans le service applicable. Les changements ne s'appliqueront pas r�troactivement et entreront en vigueur au plus tard quatorze jours apr�s leur publication. Cependant, les modifications apport�es aux nouvelles fonctions pour un service ou les modifications apport�es pour des raisons l�gales entreront en vigueur imm�diatement. Si vous n'acceptez pas les termes modifi�s pour un service, vous devez interrompre votre utilisation de ce service. S'il y a un conflit entre ces termes et les termes suppl�mentaires, les termes suppl�mentaires contr�leront ce conflit. Ces termes contr�lent la relation entre ThinkTech et vous. Ils ne cr�ent aucun droit de b�n�ficiaire tiers. Si vous ne respectez pas ces termes et que nous n'agissons pas imm�diatement, cela ne signifie pas que nous renon�ons � des droits que nous pourrions avoir (par exemple, prendre des mesures � l'avenir).
			 </p>
		</div>
	
	</form>
</div>
<div class="window project-wizard">
  <template type="text/x-dust-template">
   <h1><i class="fa fa-briefcase" aria-hidden="true"></i>Cr�ation Projet Reussie</h1>
  <section>
    <div class="structure-info">
		<h5>
			<span><b>Structure</b> : {structure}</span>
		</h5>
		<h5>
			<span><b>Ninea</b> : {ninea}</span>
		</h5>
	</div>
	<hr/>
	<span>Merci pour votre souscription au {plan}</span>
	<h2><span class="number">1</span> Etape 1 : Contrat et Caution</h2>
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
						<p>D�veloppement</p>
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
	   <p>
	    Les informations fournies seront utilis�es pour g�n�rer le contrat vous liant � ThinkTech et ce dernier sera ajout� aux documents du projet. La facture pour le paiement de la caution a �t� cr��e. Vous pouvez choisir d'effectuer le paiement maintenant en cliquant sur le bouton Terminer afin que votre projet soit trait� au plus vite par notre �quipe de d�veloppement. 
	    <span class="terms-agreement">
	      <input type="checkbox" checked> Payer la caution
	    </span>
	   </p>
	   <div class="submit">
		 <input type="button" value="Terminer">
	</div>
  </section>
  </template>
</div>
</div>
<script src="${js}/projects.js" defer></script>