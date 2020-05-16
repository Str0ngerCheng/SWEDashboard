
var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"
$(function(){
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
		parent.setCommentAndScore($('#comment').val(), $('#score').val(), 0)
		parent.layer.closeAll()
	}

}
