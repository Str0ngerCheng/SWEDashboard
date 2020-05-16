var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"

$().ready(function() {
	//获取输入内容
	var len = $('#summary').val().length;
	//显示字数
	$('#textarea-tip').html('已输入'+len+'个字');
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
	//获取输入内容
	var len = $(this).val().length;
	console.log($(this).css("border-color"))
	if(len<50)
		$(this).css("border-color","red")
	//显示字数
	$('#textarea-tip').html('已输入'+len+'个字');
});
function saveReport() {
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
			other: $('#other').val()
		},
		async: false,
		error: function (request) {
			parent.layer.alert("Connection error");
		},
		success: function (data) {
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
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/saveContent",
		data : {reportId:$('#reportId').val(),
			statusMSub:1,//表示已提交
			lastPlan:$('#lastPlan').val(),
			summary:$('#summary').val(),
			nextPlan:$('#nextPlan').val(),
			problem:$('#problem').val(),
			other:$('#other').val()},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("提交成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}
		}
	});

}