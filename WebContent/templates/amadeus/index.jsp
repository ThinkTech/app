
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>ThinkTech - Portail</title>
<!-- Meta tag Keywords -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="theme-color" content="#05d2ff"> 
<meta property="og:type" content="website">
<meta name="description" content="Bienvenue sur le portail de ThinkTech"> 
<meta name="twitter:card" content="summary">
 <meta name="twitter:site" content="@thinktech">
 <meta name="twitter:domain" property="og:site_name" content="app.thinktech.sn">
 <meta name="twitter:url" property="og:url" content="${baseUrl}">
 <meta name="twitter:title" property="og:title" content="ThinkTech - Portail"> 
 <meta name="twitter:description" property="og:description" content="Bienvenue sur le portail de ThinkTech"> 
 <meta name="twitter:image" property="og:image" content="${baseUrl}/images/banner.jpeg">
<!-- Meta tag Keywords -->
<!-- css files -->
<link rel="stylesheet" href="css/metamorphosis.css" type="text/css" media="all" /> <!-- Style-CSS --> 
<link rel="stylesheet" href="templates/amadeus/css/template.css" type="text/css" media="all" /> <!-- Style-CSS --> 
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet" type="text/css" media="all">
<!-- //css files -->
<!-- online-fonts -->
<link href="//fonts.googleapis.com/css?family=Work+Sans:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">
<!-- //online-fonts -->
<link rel="shortcut icon" href="templates/amadeus/images/arrow.png" sizes="32x32">
</head>
<body>
<div class="w3-agile-banner">
	<div class="center-container">
		<!--header-->
		<div class="header-w3l">
			<h1><span>T</span>hinkTech</h1>
		</div>
		<!--//header-->
		<!--main-->
		<div class="main-content-agile">
			<div class="wthree-pro">
				<h2><i class="fa fa-user" aria-hidden="true"></i> Entrez vos identifiants</h2>
			</div>
			<div class="sub-main-w3 login">	
				<form action="users/login" method="post">
					<input placeholder="email" name="email" type="email" required>
					<input  placeholder="mot de passe" name="password" type="password" required>
					<input type="submit" value="Connexion">
					<div class="rem-w3">
						<a class="w3-pass"><i class="fa fa-key" aria-hidden="true"></i> mot de passe oublié?</a>
						<div class="clear"></div>
					</div>
				</form>
			</div>
			<div class="sub-main-w3 recover">
			  <form action="users/password/recover" method="post">
			   <input placeholder="email" name="email" type="email" required>
			   <input type="submit" value="Récupérer">
			   <div class="rem-w3">
					<a class="w3-pass"><i class="fa fa-sign-out" aria-hidden="true"></i> retour</a>
					<div class="clear"></div>
			  </div>
			  </form>
			</div>
		</div>
		<!--//main-->
		<!--footer-->
		<div class="footer">
			<p>© 2017 Tous droits réservés | Concu par <a href="https://w3layouts.com/">W3layouts</a> et <a href="https://www.thinktech.sn/">ThinkTech</a></p>
		</div>
		<!--//footer-->
	</div>
<script type="text/javascript" src="templates/amadeus/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/metamorphosis.js"></script>
<script>
		jQuery(document).ready(function( $ ) {
			$('input[name=email]').focus();
			$(".login form").submit(function(event){
				const form = $(this);
				const user = {};
				user.email = form.find("input[name=email]").val();
				user.password = form.find("input[name=password]").val();
				page.wait({top : form.offset().top});
				$.ajax({
					  type: "POST",
					  url: form.attr("action"),
					  data: JSON.stringify(user),
					  contentType : "application/json",
					  success: function(response) {
						  location.href = response.url;
					  },
					  dataType: "json"
				});
				return false;
			});
			$(".recover form").submit(function(event){
				const form = $(this);
				const user = {};
				user.email = form.find("input[name=email]").val();
				page.wait({top : form.offset().top});
				$.ajax({
					  type: "POST",
					  url: form.attr("action"),
					  data: JSON.stringify(user),
					  contentType : "application/json",
					  success: function(response) {
						  alert("un message vous a été envoyé à l'adresse fournie");
						  page.release();
					  },
					  dataType: "json"
				});
				return false;
			});
			$(".login a").click(function(){
				$(".login").hide();
				$(".recover").show();
			});
			$(".recover a").click(function(){
				$(".login").show();
				$(".recover").hide();
			});
		});
</script>
</div>
</body>
</html>