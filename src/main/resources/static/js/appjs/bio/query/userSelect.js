var prefix = document.getElementsByTagName('meta')['ctx'].content +"/sys/user"
$(function() {
	var deptId = '';
	getTreeData();
	load(deptId);
});

function load(deptId) {
	$('#userSelectTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				// showRefresh : true,
				// showToggle : true,
				// showColumns : true,
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
				// search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者
				queryParamsType : "",
				// //设置为limit则会发送符合RESTFull格式的参数
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				responseHandler : function(res){
					console.log(res);
					return {
		                "total": res.data.total,//总数
		                "rows": res.data.records   //数据
		             };
				},
				columns : [
					{
						field : 'id', // 列字段名
						title : '序号' // 列标题
					},
					{
						field : 'name',
						title : '姓名',
						formatter : function(value, row, index) {
							return '<a href="#" onclick="selectOneUser(\'' + value+ '\')">' + value + '</a>  ';

						}
					},
					{
						field : 'username',
						title : '用户名'
					},
					{
						field : 'email',
						title : '邮箱'
					},
					{
						field : 'status',
						title : '状态',
						align : 'center',
						formatter : function(value, row, index) {
							if (value == '0') {
								return '<span class="label label-danger">禁用</span>';
							} else if (value == '1') {
								return '<span class="label label-primary">正常</span>';
							}
						}
					},
				]
			});
}
function reLoad() {
	$('#userSelectTable').bootstrapTable('refresh');
}

function getTreeData() {
	$.ajax({
		type : "GET",
		url : document.getElementsByTagName('meta')['ctx'].content +"/sys/dept/allTree",
		success : function(tree) {
			console.log(tree)
			loadTree(tree);
		}
	});
}
function loadTree(tree) {
	$('#jstree').jstree({
		'core' : {
			'data' : tree
		},
		"plugins" : [ "search" ]
	});
	$('#jstree').jstree().open_all();
}
function selectOneUser(name){
	parent.setUser(name)
	parent.layer.closeAll()

}
$('#jstree').on("changed.jstree", function(e, data) {
	if (data.selected == -1) {
		var opt = {
			query : {
				deptId : '',
			}
		}
		$('#userSelectTable').bootstrapTable('refresh', opt);
	} else {
		var opt = {
			query : {
				deptId : data.selected[0],
			}
		}
		$('#userSelectTable').bootstrapTable('refresh',opt);
	}

});