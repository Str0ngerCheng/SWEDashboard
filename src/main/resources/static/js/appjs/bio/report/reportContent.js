
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
function downloadFile() {
	// var list = $(".activecheck");
	var NameList = $('#reporttitle').text().replace(/\//g,'-')+"附件.zip";
	console.log("reporttitle:",NameList)
	var name = encodeURI(encodeURI(NameList));
	//将名称传入后台
	//window.location.href = document.getElementsByTagName('meta')['ctx'].content+"/reportfile/test";
	//window.location.href = document.getElementsByTagName('meta')['ctx'].content+"/reportfile/downloadreportfile?filename="+ name;
	var url= document.getElementsByTagName('meta')['ctx'].content+"/reportfile/downloadreportfile?filename="+ name;
	try{
		var elemIF = document.createElement('iframe');
		elemIF.src = url;
		elemIF.style.display = 'none';
		document.body.appendChild(elemIF);
		// 防止下载两次
		setTimeout(function() {
			document.body.removeChild(elemIF)
		}, 1000);

	}catch(e){
		console.log(e);
	}
}