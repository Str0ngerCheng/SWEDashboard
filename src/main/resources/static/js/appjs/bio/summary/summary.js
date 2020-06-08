var prefix = document.getElementsByTagName('meta')['ctx'].content;
var overAllIds_dep=new Array();
$(function() {
	load();
});

function load() {
	$('#summaryTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/bio/summary/list", // 服务器数据的加载地址
				//	showRefresh : true,
				//	showToggle : true,
				//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : false, // 设置为true会在底部显示分页条
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				queryParamsType : "",
				// //设置为limit则会发送符合RESTFull格式的参数
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				sortable: true,
				//排序方式
				sortOrder: "asc",//排序
				sortName: 'orderNum',
				columns : [
					{
						checkbox : true,
						formatter:function (i,row) {
							if($.inArray(row.deptId, overAllIds_dep)!=-1){
								return {
									checked:true
								}
							}
						}
					},
					{
						field : 'deptName',
						title : '专题名称' ,
						align : 'center',
					},
					{
						field : 'title',
						title : '周报题目',
						align : 'center',
						formatter : function(value, row, index) {
							return  '<a class="btn btn-link btn-sm" onclick="getTopicView(\'' + row.deptId+ '\')" target="_blank">' + row.title+ '</a>';

						}
					},
					{
						field : 'count',
						title : '浏览次数' ,
						align : 'center',
					},
					{
						field : 'rCreateDate',
						title : '提交时间' ,
						align : 'center',
					}
				],
				onLoadSuccess:function(){
					$('#summaryTable').bootstrapTable('checkAll');
				}

			});
	$('#summaryTable').on('check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
		var datas=$.isArray(rows)?rows:[rows];
		examine1(e.type,datas);
	});
}
function reLoad() {
	$('#summaryTable').bootstrapTable('refresh');
}
function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/bio/summary/add' // iframe的url
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/bio/summary/edit/' + id // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/bio/summary/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function review(id) {

	alert("review");

}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
		// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/bio/summary/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}

function getTopicView(deptId){
	var index=layer.open({
		type : 2,
		title : '专题周报详情',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/bio/topic/weekInfo/'+deptId,
	});
}

function getSummaryView() {
	var index=layer.open({
		type : 2,
		title : '小组周报详情',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/bio/summary/weekInfo',
	});

}

function submit() {
	var loadIndex=layer.load(1,{
		content: "等待中…",
		shade: [0.3,'#fff'],
		success: function (layero) {
			layero.find('.layui-layer-content').css({
				'padding-top': '40px',
				'width': '60px',
				'font-weight': 'bold'
			});
		}
	});
	$.ajax({
		type : 'GET',
		url : prefix + '/bio/summary/submit',
		error : function(request) {
		    layer.close(loadIndex);
			layer.alert("Connection error");
		},
		success : function(r) {
		 layer.close(loadIndex);
			if (r.code == 0) {
				layer.alert("已提交",{icon:1});
			} else if(r.code == 1) {
				layer.alert('组内<label style="color:red">'+r.data+'</label>本周的周报汇总还未提交，请等待小组所有专题的周报汇总均提交后再尝试！', {icon: 2})
			} else if(r.code == 2){
				layer.alert("您已提交本周小组周报汇总（含附件），不要重复提交！", {icon: 2})
			}
			else layer.alert(+r.msg, {icon: 2})
		}
	});
}

function examine1(type, datas) {
	if(type.indexOf('uncheck')==-1){
		$.each(datas,function (i,v) {
			overAllIds_dep.indexOf(v.deptId) == -1 ? overAllIds_dep.push(v.deptId) : -1;
		});
	}else{
		$.each(datas,function (i,v) {
			overAllIds_dep.splice(overAllIds_dep.indexOf(v.deptId),1);
		});
	}
}


function batchExport1(exportmode) {
	//绑定选中事件、取消事件、全部选中、全部取消
	var ids = new Array();
	ids=overAllIds_dep;
	if (ids.length == 0) {
		layer.msg("请选择要导出的数据");
		return;
	}else{
		layer.confirm("确认要导出选中的'" + ids.length + "'条数据吗?", {
			btn: ['确定', '取消']
			// 按钮
		}, function (index) {
			layer.close(index);
			var loadIndex=layer.load(1,{
				shade: [0.2,'#fff'] //0.1透明度的白色背景
			});
			var rows=$('#summaryTable').bootstrapTable('getSelections');
			var titlesplit=rows[0].title.split('-');
			var downloadfilename=titlesplit[0].replace(/\//g,'-') +'-'+titlesplit[1].replace(/\//g,'-') +'-'+"SWE小组周报汇总";
			console.log("downloadfilename",titlesplit);
			if(exportmode==3) {
				//仅导出附件的时候需要判断附件是否存在
				$.ajax({
					cache: false,
					type: "GET",
					url: prefix + '/bio/summary/ifTopicFileExist?deptids='+ids,
					//async: false,
					dataType: 'json',
					contentType: 'application/json',
					error: function (request) {
						layer.close(loadIndex);
						layer.alert("Connection error");
					},
					success: function (data) {
						if (data.code == 0) {
							var url = prefix + '/bio/summary/batchExport1?ids=' + ids + '&mode='+exportmode+'&downloadfilename='+downloadfilename;
							console.log(url);
							try {
								var elemIF = document.createElement('iframe');
								elemIF.src = url;
								elemIF.style.display = 'none';
								document.body.appendChild(elemIF);
								// 防止下载两次
								// setTimeout(function() {
								//     document.body.removeChild(elemIF)
								// }, 5000);
								layer.close(loadIndex);
							} catch (e) {
								console.log(e);
							}
						} else {
							layer.close(loadIndex);
							layer.alert(data.msg + "：" + data.data);
						}
					}
				});
			}
			else {
				var url = prefix + '/bio/summary/batchExport1?ids=' + ids+'&mode='+exportmode+'&downloadfilename='+downloadfilename;
				try{
					var elemIF = document.createElement('iframe');
					elemIF.src = url;
					elemIF.style.display = 'none';
					document.body.appendChild(elemIF);
					// // 防止下载两次
					// setTimeout(function() {
					//     document.body.removeChild(elemIF)
					// }, 10000);
					layer.close(loadIndex);
					layer.close(index);
				}catch(e){
					console.log(e);
				}
			}

			// var url=prefix + '/bio/summary/batchExport1?ids=' + ids;
			// try{
			// 	var elemIF = document.createElement('iframe');
			// 	elemIF.src = url;
			// 	elemIF.style.display = 'none';
			// 	document.body.appendChild(elemIF);
			// 	//layer.close(loadIndex);
			// 	// 防止下载两次
			// 	// setTimeout(function() {
			// 	// 	document.body.removeChild(elemIF)
			// 	// }, 10000);
			// 	layer.close(index);
			// }catch(e){
			// 	console.log(e);
			// }
			// /*window.location.href = prefix + '/batchExport?ids=' + ids;*/
			// $.ajax({
			// 	url:prefix + '/bio/summary/batchExport1?ids=' + ids,
			// 	success: function (r) {
			// 		if (r.code == 0) {
			// 			layer.msg(r.msg);
			// 			reLoad();
			// 		} else {
			// 			layer.msg(r.msg);
			// 		}
			// 	}
			// });
		}, function () {
		});
	}
}
