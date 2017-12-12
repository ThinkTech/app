 paypal.Button.render({
    env: 'sandbox',
    client : {
    	sandbox : "ARW3EYvptYs6ObbH4FHOqd6Fs0gapxI8_Eme3SzIlPxJbUtu-1FVNsperN-VKssBzbZ91g_jkFJpvOKb"
    },
    commit: true,
    locale: 'fr_FR',
    style : {
     label : 'pay'
    },
    payment: function(data,actions) {
    	return actions.payment.create({
            payment: {
                transactions: [
                    {
                        amount: { total: '100.00', currency: 'USD' }
                    }
                ]
            }
        });
    },
    onAuthorize: function(data, actions) {
    	return actions.payment.execute().then(function(payment) {
    		app.saveOrder();
        });
   }
}, '#paypal-button');