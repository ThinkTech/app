
page.table = {};

page.table.paginate = function() {
	
	$(".table").unbind("repaginate").each(function() {
		const $table = $(this);
		$(".pager").remove();
	    var currentPage = 0;
	    var numPerPage = 5;
	    $table.bind('repaginate', function() {
	        $table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
	    });
	    $table.trigger('repaginate');
	    var numRows = $table.find('tbody tr').length;
	    if(numRows > numPerPage) {
		    var numPages = Math.ceil(numRows / numPerPage);
		    var $pager = $('<div class="pager"></div>').attr("id","pager");
		    for (var page = 0; page < numPages; page++) {
		        $('<span class="page-number"></span>').text(page + 1).bind('click', {
		            newPage: page
		        }, function(event) {
		            currentPage = event.data['newPage'];
		            $table.trigger('repaginate');
		            $(this).addClass('active').siblings().removeClass('active');
		            $table.parent().focus();
		            return false;
		        }).appendTo($pager);
		    }
		    $pager.insertAfter($table.parent()).find('span.page-number:first').addClass('active');
		}
	});
};

page.table.addRow = function(entity,callback) {
	const tbody = $(".table tbody");
	page.render(tbody, [entity], true, function(row) {
		$("td:first-child span.number",row).html($("tr",tbody).removeClass("active").length);
		page.table.paginate();
		row.attr("id","1455555").click(function(event) {
			$(".table tbody tr").removeClass("active");
			$(this).addClass("active");
			return false;
		}).addClass("active");
		$("span.page-number:last").click();
		if(callback) callback(row);
	});
};

$(document).ready(function(){
	 $.each($(".menu a"),function(i,element){
		 const link = $(element);
		 const href = link.attr("href");
		 if(location.href.endsWith(href)){
			 link.addClass("active");
			 return false;
	     };
	 });
	 $(".table tbody tr").click(function(event) {
		$(".table tbody tr").removeClass("active");
		$(this).addClass("active");
		return false;
	});
	$(".window .close").click(function(event) {
		const div = $(this).parent().parent().hide();
		div.find(".document-list,.message-list").show();
		div.find(".message-edition,.document-upload").hide();
	});
	$(".window.form input[type=button]").click(function(event) {
		$(".window").hide();
	});
	
	$(".window a.message-add").click(function(event) {
		const div = $(this).parent().next();
		const list = div.find(".message-list");
		if(list.is(":hidden")) {
			list.show(1);
			div.find(".message-edition").hide(1);
		}else {
			list.hide(1);
			div.find(".message-edition").show(1);
		}
		return false;
	});
	
	$(".window a.document-add").click(function(event) {
		const div = $(this).parent().next();
		const list = div.find(".document-list");
		if(list.is(":hidden")) {
			list.show(1);
			div.find(".document-upload").hide(1);
		}else {
			list.hide(1);
			div.find(".document-upload").show(1);
		}
		return false;
	});
	
	$(".window.details input[type=button]").click(function(event) {
		const div = $(this).parent().parent().parent().parent();
		div.find(".document-list,.message-list").show();
		div.find(".message-edition,.document-upload").hide();
		return false;
	});
	
	page.table.paginate();
	
	setTimeout(function(){
		$("#confirm-dialog-ok").html("Oui");
		$("#confirm-dialog-cancel").html("Annuler");
	},3000);
 });