
var prefix = document.getElementsByTagName('meta')['ctx'].content;

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
				pagination : true, // 设置为true会在底部显示分页条
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者 "server"
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
						checkbox : true
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
				]
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
	$.ajax({
		type : 'GET',
		url : prefix + '/bio/summary/submit',
		success : function(r) {
			if (r.code == 0) {
				layer.alert("已提交",{icon:1})
			} else {
				layer.msg(r.msg);
			}
		}
	});
}