jQuery(document).ready(function($) {
	$("tbody tr").click(function(event) {
		$(".window").show();
		return false;
	});
});