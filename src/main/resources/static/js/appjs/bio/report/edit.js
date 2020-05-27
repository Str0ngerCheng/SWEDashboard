var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"

$().ready(function() {
	setSummaryContent()
	validateRule();
});


$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});


	
	
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/update",
		data : $('#reportForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}
		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}

function getLastReport(){

}

function clearAll(){
	$('#lastPlan').val('')
	$('#summary').val('')
	$('#nextPlan').val('')
	$('#problem').val('')
	$('#other').val('')
}

$('#summary').on('input propertychange', function () {
	setSummaryContent()
});
function saveReport() {
	var loadIndex=layer.load(1,{
		shade: [0.2,'#fff'] //0.1透明度的白色背景
	});
	$.ajax({
		cache: true,
		type: "POST",
		url: prefix + "/saveContent",
		data: {
			reportId: $('#reportId').val(),
			statusMSub:0,//表示未提交
			lastPlan: $('#lastPlan').val(),
			summary: $('#summary').val(),
			nextPlan: $('#nextPlan').val(),
			problem: $('#problem').val(),
		},
		async: true,
		error: function (request) {
			layer.close(loadIndex);
			parent.layer.alert("Connection error");
		},
		success: function (data) {
			layer.close(loadIndex);
			if (data.code == 0) {
				parent.layer.msg("已保存");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}
		}
	})
}
function submitReport() {
	if(getSummaryLength()<50)
		layer.alert('总结字数不能少于50字哦', {
			icon:0,
			});
	else
		layer.alert('周报提交后无法修改，确定提交吗？', {
			icon:3,
			btn: ['确定', '取消']
			,yes: function(){
				$.ajax({
					cache : true,
					type : "POST",
					url : prefix + "/saveContent",
					data : {reportId:$('#reportId').val(),
						statusMSub:1,//表示已提交
						lastPlan:$('#lastPlan').val(),
						summary:$('#summary').val(),
						nextPlan:$('#nextPlan').val(),
						problem:$('#problem').val()},
					async : false,
					error : function(request) {
						parent.layer.alert("Connection error");
					},
					success : function(data) {
						if (data.code == 0) {
							parent.layer.msg("提交成功",{icon: 1});
							parent.reLoad();
							var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
							parent.layer.close(index);

						} else {
							parent.layer.alert(data.msg)
						}
					}
				});

			}
		});


}
function setSummaryContent(){
    var len=getSummaryLength();
	if(len<50)
		$('#summary').css("border-color","red")
	else $('#summary').css("border-color","#e5e6e7")
	//显示字数
	$('#textarea-tip').html('已输入'+len+'个字');

}

function getSummaryLength() {
	//获取输入内容
	var len = 0; //初始定义长度为0
	var txtval = $.trim($("#summary").val());
	for(var i =0;i<txtval.length;i++)
		if(txtval.charAt(i)!=" ")
			len = len + 1;

	return len;
}
