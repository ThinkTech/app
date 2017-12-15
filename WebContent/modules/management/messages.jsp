<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="inner-block">
	<div class="logo-name">
		<h1><i class="fa fa-envelope-o" aria-hidden="true"></i>Vos Messages</h1>
	</div>
	<!--info updates updates-->
	<div class="info-updates">
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-3">
				<div class="col-md-8 info-update-left">
					<h3>${total}</h3>
					<h4>messages</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-envelope-o"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="col-md-4 info-update-gd">
			<div class="info-update-block clr-block-3">
				<div class="col-md-8 info-update-left">
					<h3>${read}</h3>
					<h4>messages non lus</h4>
				</div>
				<div class="col-md-4 info-update-right">
					<i class="fa fa-envelope-o"> </i>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	<!--info updates end here-->
	<!--mainpage chit-chating-->
	<div class="chit-chat-layer1">
		<div class="col-md-12 chit-chat-layer1-left">
			<div class="work-progres">
				<div class="chit-chat-heading">
					<h3 class="tlt">
						<i class="fa fa-envelope-o" aria-hidden="true"></i> Vos Messages
					</h3>
				</div>
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th></th>
								<th>Objet</th>
								<th>Auteur</th>
								<th>Date</th>
							</tr>
						</thead>
						<tbody>
						  <s:iterator value="#request.messages" var="message" status="status">
	                            <tr>
	                                <td><span class="number">${status.index+1}</span></td>
	                                <td><i class="fa fa-envelope-o" aria-hidden="true"></i> ${message.properties['subject']}</td>
								    <td><i class="fa fa-user" aria-hidden="true"></i> ${message.properties['author']}</td>
								    <td>17/09/2017</td>
	                            </tr>
	                      </s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="clearfix"></div>
	</div>
	<div class="window">
	   <div>
		<span title="fermer" class="close">X</span>
		<h1><i class="fa fa-envelope-o" aria-hidden="true"></i>Message #1</h1>
		<fieldset>
		    <span class="text-right">Objet </span> <span>création site web</span>
			<span class="text-right">Auteur </span> <span>ThinkTech</span> 
			<span class="text-right">Date </span> <span>17/09/2017</span>
		</fieldset>
		<div class="message">
		<div style="background: #fafafa; padding-bottom: 16px; padding-top: 25px;">
			<div
				style="padding-bottom: 12px; margin-left: auto; margin-right: auto; width: 80%; background: #fff;">
				<img src="https://www.thinktech.sn/images/logo.png"
					style="display: block; margin: 0 auto;">
				<div
					style="margin-top: 10px; padding: 10px; height: 90px; text-align: center; background: #eee;">
					<h4 style="font-size: 200%; color: rgb(0, 0, 0); margin: 3px;">
						<span>Souscription reussie</span>
					</h4>
					<p style="font-size: 150%; color: rgb(100, 100, 100);">
						<span>cliquer sur le bouton en bas pour confirmation</span>
					</p>
				</div>
				<div
					style="width: 90%; margin: auto; margin-top: 30px; margin-bottom: 30px;">
					<h5
						style="font-size: 120%; color: rgb(0, 0, 0); margin-bottom: 15px;">
						<span>Structure : Sesame</span>
					</h5>
					<p>Merci pour votre souscription au plan business</p>
					<p>Veuillez confirmer votre projet pour son traitement.</p>
				</div>
				<div style="text-align: center; margin-bottom: 10px;">
					<a href="http://localhost:8080/app/registration/confirm"
						style="font-size: 150%; width: 180px; margin: auto; text-decoration: none; background: #05d2ff; display: block; padding: 10px; border-radius: 2px; border: 1px solid #eee; color: #fff;"
						target="_blank" rel="noopener"><span>Confirmer</span></a>
				</div>
			</div>
			<div style="margin: 10px;margin-top: 10px; font-size: 11px; text-align: center;">
				<p>Vous recevez cet email parce que vous (ou quelqu'un utilisant
					cet email)</p>
				<p>a créé un projet en utilisant cette adresse</p>
			</div>
		</div>
		</div>
		</div>
	</div>
</div>
<script src="${js}/messages.js" defer></script>