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
						<h3>${total}</h3>
						<h4>domaines</h4>
					</div>
					<div class="col-md-4 info-update-right">
						<i class="fa fa-${activeItem.icon}"> </i>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 info-update-gd">
				<div class="info-update-block clr-block-6">
					<div class="col-md-8 info-update-left">
						<h3>${registered}</h3>
						<h4>domaines enregistr�s</h4>
					</div>
					<div class="col-md-4 info-update-right">
						<i class="fa fa-${activeItem.icon}"> </i>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 info-update-gd">
				<div class="info-update-block clr-block-3">
					<div class="col-md-8 info-update-left">
						<h3 class="unpayed">${unregistered}</h3>
						<h4>domaines en attente</h4>
					</div>
					<div class="col-md-4 info-update-right">
						<i class="fa fa-${activeItem.icon}"> </i>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
		   <div class="clearfix"> </div>
		</div>
<!--info updates end here-->
<!--mainpage chit-chating-->
<div class="chit-chat-layer1">
	<div class="col-md-12 chit-chat-layer1-left">
	    <div class="tld-domain-search-wrapper">
			<div class="tld-domain-search">
			<div class="input-container">
				<span>www.</span>
				<input placeholder="votre domaine web" type="text" maxlength="200">
				<select>
					<option value="com">.com</option>
					<option value="net">.net</option>
					<option value="org">.org</option>
					<option value="biz">.biz</option>
					<option value="info">.info</option>
					<option value="tv">.tv</option>
					<option value="press">.press</option>
					<option value="news">.news</option>
					<option value="tech">.tech</option>
				</select>
			</div>
			<a  rel="nofollow" class="tld-search-button">
			  <i class="fa fa-search" aria-hidden="true"></i>
			</a>
		 </div>
		</div>
               <div class="work-progres">
                            <div class="chit-chat-heading">
                                  <h3 class="tlt">Vos Domaines</h3>
                            </div>
                            <div class="table-responsive">
                                <table data-url="${url}/domains/info" class="domains table table-hover">
                                  <thead>
                                    <tr class="clr-block-6">
                                      <th></th>
                                      <th>Domaine</th>
                                      <th>Date Cr�ation</th>  
                                      <th>Dur�e</th>
                                      <th>Montant</th>
                                      <th>Action</th>                                                           
                                      <th>Enregistrement</th>
                                  </tr>
                              </thead>
                              <tbody>
                               <s:iterator value="#request.domains" var="domain" status="status">
	                                <tr id="${domain.properties.id}" class="${domain.properties.status=='finished' ? 'paid' : ''}">
	                                  <td><span class="number">${status.index+1}</span></td>
	                                  <td>${domain.properties.name}</td>
	                                  <td><s:date name="properties.date" format="dd/MM/yyyy" /></td>
	                                  <td>${domain.properties.year} an</td>
                                  	  <td><span class="digit">${domain.properties.price}</span> CFA</td>
                                  	   <td>${domain.properties.action}</td>                                        
	                                  <td><span class="label ${domain.properties.status=='in progress' ? 'label-danger' : '' } ${domain.properties.status=='finished' ? 'label-success' : '' } ${domain.properties.status=='stand by' ? 'label-info' : '' }">
	                                  ${domain.properties.status=='in progress' ? 'en cours' : '' } ${domain.properties.status=='finished' ? 'termin�' : '' } ${domain.properties.status=='stand by' ? 'en attente' : '' }
	                                  </span></td>
	                              </tr>
	                          </s:iterator>
	                          <template>
							     {#.}
							      <tr id="{id}">
							            <td><span class="number"></span></td>
							   	        <td>{name}</td>
							   	        <td>{date}</td> 
							   	        <td>{year}</td>
							   	        <td>{price}</td>
							   	        <td>{action}</td>  
							            <td><span class="label label-info">en attente</span></td>
							   	    </tr>
							     {/.}
							   </template>
                          </tbody>
                      </table>
                      <div class="empty"><span>aucun domaine</span></div>
                  </div>
             </div>
      </div>
      
     <div class="clearfix"> </div>
</div>
<div class="modal modal-top">
		<div class="search search-results">
		   <span class="close">X</span>
		   <h1><i class="fa fa-search" aria-hidden="true"></i> R�sultats</h1>
		   <div>
		   	   <h3><i class="fa fa-check-circle-o" aria-hidden="true"></i> Le nom de domaine web <span class="domain-name"></span> est <span class="domain-availability"></span></h3>
		   </div>
		   <div>
		   <table class="table-sm table-striped">
		 	<tbody>
		 	</tbody>
		 	</table>
		   </div>
		</div>
		<div class="search search-wizard">
		   <span class="close">X</span>
		   <h1><i class="fa fa-shopping-cart" aria-hidden="true"></i> Achat Domaine Web</h1>
		   <div>
		   	   <h3 class="green"><i class="fa fa-check-circle-o green" aria-hidden="true"></i> domaine web : <span class="domain-name"></span></h3>
		   	   <h4>Dur�e Choisie : <span class="domain-year"></span> an</h4> <h4 class="price">Montant : <span class="domain-amount"></span> CFA</h4> <span class="domain-edit"><a>[ modifier ]</a></span>
		   </div>
		   <div class="wizard-message">
		      <p>Selon l'action que vous avez choisie, votre domaine web sera mis en attente d'enregistrement. Celui-ci sera marqu� en cours d�s que vous aurez pay� votre facture d'h�bergement de domaine qui sera g�n�r�e automatiquement apr�s la confirmation de votre action.</p>
		   </div>
		   <div style="border:none;text-align:center">
		    <fieldset>    
		         <span class="text-right"><i class="fa fa-globe" aria-hidden="true"></i> Domaine </span> <span class="domain-name"></span>
		         <span class="text-right"><i class="fa fa-money" aria-hidden="true"></i> Prix (CFA) </span> <span class="domain-price"></span>
		         <span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Dur�e Choisie</span> <span class="domain-year"></span>
		         <span class="text-right"><i class="fa fa-money" aria-hidden="true"></i> Montant (CFA) </span> <span class="domain-amount"></span>
		         <span class="text-right epp-code"><i class="fa fa-code" aria-hidden="true"></i> EPP Code </span> <span class="epp-code"><input name="eppCode"></input></span>
		    </fieldset>
		   </div>
		   <div class="buttons">
		      <a class="finish">Confirmer</a>
		      <a class="cancel">Annuler</a>
		   </div>
		   <br>
		</div>
		</div>
<div class="window details">
     <div>
		<span title="fermer" class="close">X</span>
		<section>
		 <template>
		 <h1><i class="fa fa-${activeItem.icon}" aria-hidden="true"></i>Domaine</h1>
		<fieldset>
		    <span class="text-right"><i class="fa fa-globe" aria-hidden="true"></i> Domaine </span> <span>{name}</span>
			<span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Date Cr�ation</span> <span>{date}</span>
			<span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Dur�e </span> <span>{year} an</span>
			<span class="text-right"><i class="fa fa-money" aria-hidden="true"></i> Montant </span> <span><b class="digit">{price}</b> CFA</span>
			<span class="text-right"><i class="fa fa-tasks" aria-hidden="true"></i> Action </span> <span>{action}</span>
			<div class="eppCode">
			 <fieldset>
			  <span class="text-right"><i class="fa fa-code" aria-hidden="true"></i> EPP Code </span> <span>{eppCode|s}</span>
			 </fieldset>
			</div>
			<div class="manage">
			    <fieldset>
			     <span class="text-right"><i class="fa fa-check" aria-hidden="true"></i> Enregistrement </span> <span><span class="label label-success">termin�</span></span>
			     <span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Enregistr� le  </span> <span>{registeredOn}</span>
			 	 <span class="text-right"><i class="fa fa-building" aria-hidden="true"></i> Management </span> <span><a href="https://cloudlogin.co" target="_blank" rel="nofollow">[ manage ]</a></span>
			 	</fieldset>
			</div>
		</fieldset>
		</template>
		</section>
	</div>
</div>
</div>
<script src="${js}/domains.js" defer></script>