
var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"
$(function(){
	if(""==$('#result').val()) {
		$('#result').val(90)
		$('#score').val(90)
	}
	$('#score').bind('input propertychange', function() {
		$('#result').val($(this).val());
	});

})
function getVal(value){
	$('#score').val(value);

}
function submitContent() {
	if($('#comment').val()=='')
		layer.alert('请您添加评价哦~', {icon: 2});
	else {
		report.comment=$('#comment').val();
		report.score=$('#score').val();
		$.ajax({
			cache : true,
			type : "POST",
			url : prefix + "/update",
			dataType: 'json',

			contentType : 'application/json',
			data : JSON.stringify(report),
			async : false,
			error : function(request) {
				parent.layer.alert("Connection error");
			},
			success : function(data) {
				if (data.code == 0) {
					parent.reLoad();
					parent.layer.closeAll();
				} else {
					parent.layer.alert(data.msg)
				}
			}
		});
	}

}
