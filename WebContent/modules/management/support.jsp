<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="inner-block">
 <div class="logo-name">
	<h1><i class="fa fa-question-circle-o" aria-hidden="true"></i>Assistance</h1> 								
 </div>
<!--info updates updates-->
	 <div class="info-updates">
	        <div class="col-md-4 info-update-gd">
				<div class="info-update-block clr-block-1">
					<div class="col-md-8 info-update-left">
						<h3>${total}</h3>
						<h4>tickets</h4>
					</div>
					<div class="col-md-4 info-update-right">
						<i class="fa fa-question-circle-o"> </i>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
			<div class="col-md-4 info-update-gd">
				<div class="info-update-block clr-block-3">
					<div class="col-md-8 info-update-left">
						<h3>${unsolved}</h3>
						<h4>tickets non résolus</h4>
					</div>
					<div class="col-md-4 info-update-right">
						<i class="fa fa-question-circle-o"> </i>
					</div>
				  <div class="clearfix"> </div>
				</div>
			</div>
		   <div class="clearfix"> </div>
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
                                  <h3 class="tlt"><i class="fa fa-question-circle-o" aria-hidden="true"></i> Vos Tickets</h3>
                            </div>
                            <div class="table-responsive">
                               <table data-url="${url}/support/tickets/info" class="support table table-hover">
                                  <thead>
                                    <tr>
                                      <th></th>
                                      <th>Objet</th>
                                      <th>Service</th>
                                      <th>Auteur</th> 
                                      <th>Date Création</th>                                                             
                                      <th>Traitement</th>
                                      <th>Progression</th>
                                  </tr>
                              </thead>
                              <tbody>
                               <s:iterator value="#session.tickets" var="ticket" status="status">
	                                <tr id="${ticket.properties['id']}">
	                                  <td><span class="number">${status.index+1}</span></td>
	                                  <td><i class="fa fa-question-circle-o" aria-hidden="true"></i> ${ticket.properties['subject']}</td>
	                                  <td><i class="fa fa-ticket" aria-hidden="true"></i> ${ticket.properties['service']}</td>
                                      <td><i class="fa fa-user" aria-hidden="true"></i> ${ticket.properties['author']}</td>
                                      <td>${ticket.properties['date']}</td>                                       
	                                  <td><span class="label ${ticket.properties['status']=='in progress' ? 'label-danger' : '' } ${ticket.properties['status']=='finished' ? 'label-success' : '' } ${ticket.properties['status']=='stand by' ? 'label-info' : '' }">
	                                  ${ticket.properties['status']=='in progress' ? 'en cours' : '' } ${ticket.properties['status']=='finished' ? 'terminé' : '' } ${ticket.properties['status']=='stand by' ? 'en attente' : '' }
	                                  </span></td>
	                                  <td><span class="badge badge-info">${ticket.properties['progression']}%</span></td>
	                              </tr>
	                          </s:iterator>
                              <template>
							     {#.}
							      <tr id="{id}">
							            <td><span class="number"></span></td>
							   	        <td><i class="fa fa-question-circle-o" aria-hidden="true"></i> {subject}</td>
                                  		<td><i class="fa fa-ticket" aria-hidden="true"></i> {service}</td>
                                  		<td><i class="fa fa-user" aria-hidden="true"></i> Malorum</td>
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
     <div class="clearfix"> </div>
</div>
<div class="window form">
  <div>
   <span title="fermer" class="close">X</span>
   <h1><i class="fa fa-question-circle-o" aria-hidden="true"></i>Nouveau Ticket</h1>
   <form action="${url}/support/tickets/create">
	<fieldset>
	    <span class="text-right"><i class="fa fa-ticket" aria-hidden="true"></i> Service </span>
		<select name="service">
		  <option value="site web">site web</option>
		</select>
	    <span class="text-right"><i class="fa fa-file-code-o" aria-hidden="true"></i> Objet </span>
	     <input name="subject" placeholder="votre objet" type="text" required>
		<span class="text-right"><i class="fa fa-product-hunt" aria-hidden="true"></i> Priorité </span>
		<select name="priority">
		  <option value="low">normal</option>
		  <option value="medium">moyen</option>
		  <option value="high">élévé</option>
		</select>
		<span class="text-right"><i class="fa fa-building" aria-hidden="true"></i> Structure </span>
	    <input name="structure" type="text" value="Sesame" readonly>
	    <span class="text-right"><i class="fa fa-user" aria-hidden="true"></i> Auteur </span>
	    <input name="author" type="text" value="Malorum" readonly>
		<span class="text-right full"><i class="fa fa-file-text-o" aria-hidden="true"></i> Description du probléme</span>
		<textarea name="message" placeholder="entrer votre description" required></textarea>
	</fieldset>
	<div class="submit">
		 <input type="submit" value="Créér">
		 <input type="button" value="Annuler">
	</div>
	</form>
	</div>
</div>
<div class="window details">
    <div>
		<span title="fermer" class="close">X</span>
		<section>
		 <template>
		 <h1><i class="fa fa-question-circle-o" aria-hidden="true"></i>Ticket : {id}</h1>
		<fieldset>
		    <span class="text-right"><i class="fa fa-ticket" aria-hidden="true"></i> Service </span> <span>{service}</span>
		    <span class="text-right"><i class="fa fa-file-code-o" aria-hidden="true"></i> Objet </span> <span>{subject}</span>
		    <span class="text-right"><i class="fa fa-user" aria-hidden="true"></i> Auteur </span> <span>{author}</span>
			<span class="text-right"><i class="fa fa-calendar" aria-hidden="true"></i> Date Création </span> <span>{date} - 17:35:25</span>
			<span class="text-right"><i class="fa fa-tasks" aria-hidden="true"></i> Traitement </span> 
			<span data-status="stand by" style="display:none"><span class="label label-info">en attente</span> <span class="label label-info">staff technique</span> </span>
			<span data-status="in progress" style="display:none"><span class="label label-danger">en cours</span></span>  
			<span data-status="finished" style="display:none"><span class="label label-success">terminé</span></span>
			<span class="text-right"><i class="fa fa-tasks" aria-hidden="true"></i> Progression </span> <span class="badge badge-info">{progression}%</span>
		</fieldset>
		<fieldset>
	    <fieldset>
	        <legend>
	    		Messages <a class="message-add"><i class="fa fa-plus" aria-hidden="true"></i></a>
	   		</legend>
	   		 <div class="messages">
	   		    <div class="message-list">
   		 		 <h6>pas de messages</h6>
   		 		 <div data-template="messages">
   		 		</div>
   		 		</div>
	   		 	<div class="message-edition">
	   		 	   <form action="${url}/support/messages/create">
	   		 		<textarea name="message" placeholder="entrer votre message" required></textarea>
	   		 		<input name="id" type="hidden" value={id}>
	   		 		<div class="submit">
				      <input type="submit" value="Ajouter">
				      <input type="button" value="Annuler">
				    </div>
				    </form>
	   		 	</div>
	   		 </div>
	    </fieldset>
	</fieldset>
	</template>
	</section>
	<template id="template-messages">
   	  {#.}
	      <div>
	        <img width= "32px" height="32" src="${images}/user_64.png"/> 
	   	  	<p class="message">{message}</p>
	   	  	<span>14/12/2017 - 17:35:25</span>
	   	  </div>
	   	   <hr/>
   	  {/.}
   	</template>
	</div>
</div>
</div>
<script src="${js}/support.js" defer></script>