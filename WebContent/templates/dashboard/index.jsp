<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML>
<html>
<head>
<base href="${path}"/>
<title>ThinkTech - Portail</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
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
<link href="https://fonts.googleapis.com/css?family=Work+Sans|Open+Sans" rel="stylesheet" type="text/css" media="all" />
<style type="text/css">
 <%@include file="/templates/dashboard/css/bootstrap.css"%>
</style>
<style type="text/css">
 <%@include file="/css/metamorphosis.css"%>
</style>
<style type="text/css">
 <%@include file="/templates/dashboard/css/template.css"%>
</style>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet" type="text/css" media="all">
<link href="${css}/module.css" rel="stylesheet" type="text/css" media="all" />
<link rel="shortcut icon" href="templates/dashboard/images/arrow.png" sizes="32x32">
<script>
<%@include file="/js/jquery-3.1.1.min.js"%>
</script>
</head>
<body>	
<div class="page-container">
   <div class="sidebar-menu">		
     <h1><a><span>T</span>hinkTech</a></h1>  
		    <div class="menu">
		      <ul id="menu" >
		        <li><a href="dashboard"><i class="fa fa-television"></i><span>Dashboard</span></a></li>
		        <li><a href="dashboard/projects"><i class="fa fa-briefcase" aria-hidden="true"></i><span>Vos Projets</span></a></li>
		        <li><a href="users/account"><i class="fa fa-user" aria-hidden="true"></i><span>Votre Compte</span></a></li>
		        <li><a href="dashboard/messages"><i class="fa fa-envelope-o"></i><span>Messagerie</span></a></li>
		        <li><a href="dashboard/services"><i class="fa fa-ticket"></i><span>Nos Services</span></a></li>
		        <li><a href="dashboard/support"><i class="fa fa-question-circle-o"></i><span>Assistance</span></a></li>
		        <li><a href="dashboard/billing"><i class="fa fa-money"></i><span>Facturation</span></a></li>
		      </ul>
		    </div>
	 </div>
	<div class="clearfix"> </div>	
   <div class="left-content">
	   <div class="mother-grid-inner">
            <!--header start here-->
				<div class="header-main">
						 <div class="header-right">
							<div class="profile_details_left"><!--notifications of menu start -->
								<ul class="nofitications-dropdown">
									<li class="dropdown head-dpdn">
										<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-envelope"></i><span class="badge">3</span></a>
										<ul class="dropdown-menu">
											<li>
												<div class="notification_header">
													<h3>Vous avez 3 nouveaux messages</h3>
												</div>
											</li>
											<li><a href="dashboard/messages">
											   <div class="notification_desc">
												<p>Lorem ipsum dolor</p>
												<p><span>1 hour ago</span></p>
												</div>
											   <div class="clearfix"></div>	
											</a></li>
											<li class="odd"><a href="dashboard/messages">
											   <div class="notification_desc">
												<p>Lorem ipsum dolor </p>
												<p><span>1 hour ago</span></p>
												</div>
											  <div class="clearfix"></div>	
											</a></li>
											<li><a href="dashboard/messages">
											   <div class="notification_desc">
												<p>Lorem ipsum dolor</p>
												<p><span>1 hour ago</span></p>
												</div>
											   <div class="clearfix"></div>	
											</a></li>
											<li>
												<div class="notification_bottom">
													<a href="dashboard/messages">voir messages</a>
												</div> 
											</li>
										</ul>
									</li>	
								</ul>
								<div class="clearfix"> </div>
							</div>
							<!--notification menu end -->
							<div class="profile_details">		
								<ul>
									<li class="dropdown profile_details_drop">
									    <div class="user-name">
													<p>Malorum</p>
													<span>administrateur</span>
												</div>
										<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
											<div class="profile_img">	
												<i class="fa fa-angle-down lnr"></i>
												<i class="fa fa-angle-up lnr"></i>
												<div class="clearfix"></div>	
											</div>	
										</a>
										<ul class="dropdown-menu drp-mnu">
											<li><a  href="users/account"><i class="fa fa-user"></i>Votre Compte</a> </li> 
											<li> <a href="users/logout"><i class="fa fa-sign-out"></i>Déconnexion</a> </li>
										</ul>
									</li>
								</ul>
							</div>
							<div class="clearfix"> </div>				
						</div>
				     <div class="clearfix"> </div>	
				</div>
<!--heder end here-->  
<tiles:insertAttribute name="content"/>	
<div class="clearfix"> </div>
</div>
</div>
</div>
<script src="js/metamorphosis.js" defer></script> 
<script src="templates/dashboard/js/template.js" defer></script>
<script src="templates/dashboard/js/bootstrap.js" defer> </script>
</body>
</html>                     