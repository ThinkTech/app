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
                                      <th>Date</th>  
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
                          </tbody>
                      </table>
                      <div class="empty"><span>aucun domaine</span></div>
                  </div>
             </div>
      </div>
      
     <div class="clearfix"> </div>
</div>
<div class="window details">
     <div>
		<span title="fermer" class="close">X</span>
		<section>
		 <template>
		 <h1><i class="fa fa-${activeItem.icon}" aria-hidden="true"></i>Domaine</h1>
		<fieldset>
		    <span class="text-right"><i class="fa fa-globe" aria-hidden="true"></i> Domaine </span> <span>{name}</span>
			<span class="text-right"><i class="fa fa-ticket" aria-hidden="true"></i> Service </span> <span>domainhosting</span>
			<span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Date </span> <span>{date}</span>
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