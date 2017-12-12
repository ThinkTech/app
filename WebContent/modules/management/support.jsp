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
						<h4>tickets non r�solus</h4>
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
                               <table class="support table table-hover">
                                  <thead>
                                    <tr>
                                      <th></th>
                                      <th>Objet</th>
                                      <th>Service</th>
                                      <th>Auteur</th> 
                                      <th>Date</th>                                                             
                                      <th>Traitement</th>
                                      <th>Progression</th>
                                  </tr>
                              </thead>
                              <tbody>
                               <s:iterator value="#request.tickets" var="ticket" status="status">
	                                <tr>
	                                  <td><span class="number">${status.index+1}</span></td>
	                                  <td><i class="fa fa-question-circle-o" aria-hidden="true"></i> ${ticket.properties['subject']}</td>
	                                  <td><i class="fa fa-ticket" aria-hidden="true"></i> ${ticket.properties['service']}</td>
                                      <td><i class="fa fa-user" aria-hidden="true"></i> ${ticket.properties['author']}</td>
                                      <td>17/09/2017</td>                                       
	                                  <td><span class="label ${ticket.properties['status']=='in progress' ? 'label-danger' : '' } ${ticket.properties['status']=='finished' ? 'label-success' : '' } ${ticket.properties['status']=='stand by' ? 'label-info' : '' }">
	                                  ${ticket.properties['status']=='in progress' ? 'en cours' : '' } ${ticket.properties['status']=='finished' ? 'termin�' : '' } ${ticket.properties['status']=='stand by' ? 'en attente' : '' }
	                                  </span></td>
	                                  <td><span class="badge badge-info">${ticket.properties['progression']}%</span></td>
	                              </tr>
	                          </s:iterator>
                              <template type="text/x-dust-template">
							     {#.}
							      <tr id="{id}">
							            <td><span class="number"></span></td>
							   	        <td><i class="fa fa-question-circle-o" aria-hidden="true"></i> {subject}</td>
                                  		<td><i class="fa fa-ticket" aria-hidden="true"></i> {service}</td>
                                  		<td><i class="fa fa-user" aria-hidden="true"></i> malorum</td>
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
   <span title="fermer" class="close">X</span>
   <h1><i class="fa fa-question-circle-o" aria-hidden="true"></i>Nouveau Ticket</h1>
   <form action="${url}/support/tickets/create">
	<fieldset>
	    <span class="text-right">Objet </span>
	     <input name="subject" placeholder="votre objet" type="text" required>
	     <span class="text-right">Service </span>
		<select name="service">
		  <option value="site web">site web</option>
		</select>
		<span class="text-right">Priorit� </span>
		<select name="priority">
		  <option value="low">normal</option>
		  <option value="medium">moyen</option>
		  <option value="high">�l�v�</option>
		</select>
		<span class="text-right full">Description du probl�me</span>
		<textarea name="message" placeholder="entrer votre description" required></textarea>
	</fieldset>
	<div class="submit">
		 <input type="submit" value="Cr��r">
		 <input type="button" value="Annuler">
	</div>
	</form>
</div>
<div class="window details">
		<span title="fermer" class="close">X</span>
		<h1><i class="fa fa-question-circle-o" aria-hidden="true"></i>Ticket #1</h1>
		<fieldset>
		    <span class="text-right">Objet </span> <span>site web down</span>
		    <span class="text-right">Service </span> <span>site web</span>
		    <span class="text-right">Auteur </span> <span>malorum</span>
			<span class="text-right">Date </span> <span>17/09/2017</span>
			<span class="text-right">Traitement </span> <span class="label label-info">en attente</span><br>
			<span class="text-right">Progression </span> <span class="badge badge-info">0%</span>
		</fieldset>
		<fieldset>
	    <fieldset>
	        <legend>
	    		Messages <a class="message-add"><i class="fa fa-plus" aria-hidden="true"></i></a>
	   		</legend>
	   		 <div class="messages">
	   		    <div class="message-list">
   		 		 <h6>pas de messages</h6>
   		 		 <div></div>
   		 		</div>
	   		 	<div class="message-edition">
	   		 	   <form action="${url}/support/messages/create">
	   		 		<textarea name="message" placeholder="entrer votre message" required></textarea>
	   		 		<div class="submit">
				      <input type="submit" value="Ajouter">
				      <input type="button" value="Annuler">
				    </div>
				    </form>
	   		 	</div>
	   		 </div>
	    </fieldset>
	</fieldset>
</div>
</div>
<script src="${js}/support.js" defer></script>