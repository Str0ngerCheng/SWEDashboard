var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"

$().ready(function() {
	validateContent();
	setSummaryStyle();
});


$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});

function validateContent() {
	if($('#summary').val()=="")
		$('#summary').val("（以下内容为默认模板）\n" +
			"1. 针对***，做了***，取得了***；\n" +
			"2. 为了***，开展了***工作，形成了***；\n" +
			"3. 在前期**基础上，对***，获取了***。")
	if($('#nextPlan').val()=="")
		$('#nextPlan').val("（以下内容为默认模板）\n" +
			"1. 在***方面，计划开展***，拟取得***；\n" +
			"2. 因为***，所以在下周将***，计划完成***\n" +
			"3. 按照***计划，将继续完成***，形成***")
	if($('#problem').val()=="")
		$('#problem').val("（以下内容为默认模板）\n" +
			"1. 由于***，所以存在***问题，需要***")
}

function getLastReport(){

}

function clearAll(){
	$('#lastPlan').val('')
	$('#summary').val('')
	$('#nextPlan').val('')
	$('#problem').val('')
}

$('#summary').on('input propertychange', function () {
	setSummaryStyle()
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
function setSummaryStyle(){
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

function handleFiles() {
	//获取文件对象
	var input = document.getElementById('reportfile');
	console.log("file",input);
	var fileObj = input.files[0];
	if (fileObj == null) {
		parent.layer.alert("请选择zip或rar文件");
		return;
	}
	var filename = fileObj.name;
	var index = filename.lastIndexOf(".");
	var ext = filename.substr(index + 1);
	console.log(ext);
	if (ext != "zip" && ext != "rar") {
		parent.layer.alert("请上传zip或rar文件");
		input.outerHTML = input.outerHTML.replace(/(value=\").+\"/i, "$1\"");
		return;
	}
	var newfilename = document.getElementById('reportTitle').innerText.replace(/\//g,'-') + "附件.zip";
	//更改label的名字

	document.getElementById('filelabel').innerHTML = newfilename;
}

function clearFile() {
	var input = document.getElementById('reportfile');
	input.outerHTML = input.outerHTML.replace(/(value=\").+\"/i, "$1\"");
	document.getElementById('filelabel').innerHTML = "请点击此处选择文件";
}
function checkFile() {
	var input = document.getElementById('reportfile');
	var fileObj = input.files[0];
	if(fileObj==null){
		parent.layer.alert("请先选择文件！");
		return;
	}
	//var newfile = new File([fileObj], $('#reportTitle').text().replace('/','-') + ".zip");
	var newfile=new Blob([fileObj],{type:"application/zip"})
	var link = document.createElement('a');
	link.href = window.URL.createObjectURL(newfile);
	console.log($('#reportTitle').text());
	link.download = $('#reportTitle').text().replace(/\//g,'-')+ "附件.zip";
	link.click();
	window.URL.revokeObjectURL(link.href);
}

//上传文件
function uploadFiles() {

	//获取文件对象
	var input = document.getElementById('reportfile');
	var fileObj = input.files[0];
	if (fileObj == null) {
		parent.layer.alert("请选择zip或rar文件");
		return;
	}
	var filename = fileObj.name;
	var index = filename.lastIndexOf(".");
	var ext = filename.substr(index + 1);
	console.log(ext);
	if (ext != "zip" && ext != "rar") {
		parent.layer.alert("请上传zip或rar文件");
		input.outerHTML = input.outerHTML.replace(/(value=\").+\"/i, "$1\"");
		return;
	}
	var form = new FormData();
	//创建新文件对象（实现更名）

	var newfile = new File([fileObj], $('#reportTitle').text().replace(/\//g,'-') + "附件.zip");
	form.append("file", newfile);

	$.ajax({
		xhr: function () {
			var xhr = $.ajaxSettings.xhr();
			xhr.upload.addEventListener('progress', handleProgress);
			return xhr;
		},
		type: 'POST',
		url: document.getElementsByTagName('meta')['ctx'].content + "/reportfile/uploadfile",   //submit to UploadFileServlet
		data: form,
		cache: false,
		processData: false,
		contentType: false,
		success: function (ret) {
			parent.layer.alert(ret);
			input.outerHTML = input.outerHTML.replace(/(value=\").+\"/i, "$1\"");
			document.getElementById('progressBar').value = 0;
			document.getElementById('filelabel').innerHTML = "文件已上传，点击重新上传";
		},
		error: function (jqXHR, textStatus, errorThrown) {
			parent.layer.alert("上传失败,错误信息：" + textStatus);
			console.log(jqXHR);
			document.getElementById('progressBar').value = 0;
			input.outerHTML = input.outerHTML.replace(/(value=\").+\"/i, "$1\"");
			document.getElementById('filelabel').innerHTML = "请点击此处选择文件";
		}
	})
}

//文件进度条
function handleProgress(e) {
	var progressBar = document.getElementById("progressBar");
	if (e.lengthComputable) {
		progressBar.max = e.total;
		progressBar.value = e.loaded;
	}
}
