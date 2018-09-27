 div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-bottom:2%;padding-top:2%;text-align:center;background:#3abfdd") {
		      h4(style : "font-size: 120%;color: #fff;margin: 3px") {
		        span("Demande de collaboration")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 30px;margin-bottom:30px") {
		      p("$author.name souhaite vous ajouter &agrave; la liste de ses collaborateurs. cliquez sur le bouton en bas pour accepter cette demande.")
		      p("votre mot de passe est : <b>$user.password</b>")
		      p("vous pouvez le modifier en vous connectant &aacute; notre portail client")
		       div(style : "text-align:center;margin-top:10px;margin-bottom:20px") {
		       a(href : "$url/users/registration/confirm?activationCode=$user.activationCode",style : "font-size:130%;width:140px;margin:auto;margin-top:20px;text-decoration:none;background: #3abfdd;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
		         span("Accepter")
		       }
		     }
		    }
		  }
		  div(style :"margin: 10px;margin-top:10px;font-size : 11px;text-align:center") {
		      p("Vous recevez cet email parce que $author.name ")
		      p("a envoy&eacute; une demande de collaboration en utilisant cette adresse")
		  }
}