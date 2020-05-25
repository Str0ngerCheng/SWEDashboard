function update() {
	if($('#id').val() == "") {
		console.log(1)
		$('#id').val(null)
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : document.getElementsByTagName('meta')['ctx'].content +"/sys/user/updateUserPlan",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg(data.msg);
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(data.msg);
			}

		}
	});


}
