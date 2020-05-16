var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/query"
var prefix_report = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"
$(function() {
    load();
});

function load() {
    var picker1 =$('#datetimepicker1').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),

    });
    var picker2 =$('#datetimepicker2').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
    });
    //动态设置最小值
    picker1.on('dp.change', function (e) {
        picker2.data('DateTimePicker').minDate(e.date);
        picker2.data('DateTimePicker').maxDate(new Date());
    });
    //动态设置最大值
    picker2.on('dp.change', function (e) {
        picker1.data('DateTimePicker').maxDate(e.date);
    });
    var picker_t1=$('#datetimepicker_t1').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
    });
    var picker_t2=$('#datetimepicker_t2').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
    });
    picker_t1.on('dp.change', function (e) {
        picker_t2.data('DateTimePicker').minDate(e.date);
        picker_t2.data('DateTimePicker').maxDate(new Date());
    });
    //动态设置最大值
    picker_t2.on('dp.change', function (e) {
        picker_t1.data('DateTimePicker').maxDate(e.date);
    });
    $('#queryTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix_report + "/list", // 服务器数据的加载地址
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
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                    };
                },
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
                        checkbox : true
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'content',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'status',
                        title : '完成状态',
                        align : 'center',
                        formatter:function(value,row,index){
                            var value="";
                            if(row.status=="0"){
                                value = ' <a class="btn btn-danger btn-sm"  title="未完成"><i class="fa fa-clock-o"></i></a>';
                            }else if(row.status=="1"){
                                value = ' <a class="btn btn-success btn-sm"  title="已完成"><i class="fa fa-check-circle"></i></a> ';
                            }else{
                                value = row.status ;
                            }
                            return value;
                        }
                    },
                    {
                        field : 'rcreateDate',
                        title : '创建时间',
                        align : 'center',
                    },
                    {
                        field : 'rchgDate',
                        title : '修改时间',
                        align : 'center',
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-success btn-sm '+'" href="#" mce_href="#" title="详细" onclick="getDetail(\''
                                + row.id
                                + '\')"><i class="fa fa-info-circle"></i></a> ';
                            var d = '<a class="btn btn-primary btn-sm '+'" href="#" title="导出"  mce_href="#" onclick="export(\''
                                + row.id
                                + '\')"><i class="fa fa-download"></i></a> ';
                            return e + d ;
                        }
                    } ]
            });
    $('#queryTable_t')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix_report + "/list", // 服务器数据的加载地址
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
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                    };
                },
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
                        checkbox : true
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'content',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'status',
                        title : '完成状态',
                        align : 'center',
                        formatter:function(value,row,index){
                            var value="";
                            if(row.status=="0"){
                                value = ' <a class="btn btn-danger btn-sm"  title="未完成"><i class="fa fa-clock-o"></i></a>';
                            }else if(row.status=="1"){
                                value = ' <a class="btn btn-success btn-sm"  title="已完成"><i class="fa fa-check-circle"></i></a> ';
                            }else{
                                value = row.status ;
                            }
                            return value;
                        }
                    },
                    {
                        field : 'rcreateDate',
                        title : '创建时间',
                        align : 'center',
                    },
                    {
                        field : 'rchgDate',
                        title : '修改时间',
                        align : 'center',
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-primary btn-sm '+'" href="#" mce_href="#" title="详细" onclick="getDetail(\''
                                + row.id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm '+'" href="#" title="导出"  mce_href="#" onclick="export(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d ;
                        }
                    } ]
            });
}
function reLoad() {
    $('#queryTable').bootstrapTable('refresh');
    $('#queryTable_t').bootstrapTable('refresh');
}
function selectUser() {
    layer.open({
        type : 2,
        title : '选择小伙伴',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/userSelect' // iframe的url
    });
}
function setUser(name){
    $('#userName').val(name)
}