var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/query"
var prefix_report = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"
var overAllIds_dep=new Array();
var overAllIds_per=new Array();
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
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                method : 'get',// 服务器数据的请求方式 get or post
                url : prefix + "/list",// 服务器数据的加载地址
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
                maintainSelected:true,
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
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_per)!=-1){
                                return {
                                    checked:true
                                }
                            }else{
                                return {
                                    checked:false
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'summary',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'plan',
                        title : '下周计划',
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
                url : prefix + "/list", // 服务器数据的加载地址
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
                pageSize :10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                maintainSelected:true,
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize,
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
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_dep)!=-1){
                                return {
                                    checked:true
                                }
                            }else{
                                return {
                                    checked:false
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'summary',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'plan',
                        title : '下周计划',
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

function query() {
     $('#queryTable').bootstrapTable('destroy');
       $('#queryTable').bootstrapTable(
            {
                type: "get",
                url: prefix + "/queryReport",
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
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                maintainSelected:true,
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize,
                        userName: $('#userName').val(),
                        WeekMonth: $('#WeekMonth option:selected').val(),
                        datetimepicker1: $('#datetimepicker1').find("input").val(),
                        datetimepicker2: $('#datetimepicker2').find("input").val()
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
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_per)!=-1){
                                return {
                                    checked:true
                                }
                            }else{
                                return {
                                    checked:false
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'summary',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'plan',
                        title : '下周计划',
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

}

function query1() {
    $('#queryTable_t').bootstrapTable('destroy');
   $('#queryTable_t')
        .bootstrapTable(
            {
                type: "get",
                url: prefix + "/queryDepReport",
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
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                maintainSelected:true,
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize,
                        topicName: $('#topicName').val(),
                        WeekMonth1: $('#WeekMonth1 option:selected').val(),
                        datetimepicker_t1: $('#datetimepicker_t1').find("input").val(),
                        datetimepicker_t2: $('#datetimepicker_t2').find("input").val()
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
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_dep)!=-1){
                                return {
                                    checked:true
                                }
                            }else{
                                return {
                                    checked:false
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'summary',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'plan',
                        title : '下周计划',
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
}

function Keyquery() {
    $('#queryTable').bootstrapTable('destroy');
    $('#queryTable')
        .bootstrapTable(
            {
                type: "get",
                url: prefix + "/searchKey",
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
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                maintainSelected:true,
                // //设置为limit则会发送符合RESTFull格式的参数
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageNumber : params.pageNumber,
                        pageSize : params.pageSize,
                        searchKey: $('#searchKey').find("input").val()
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
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_per)!=-1){
                                return {
                                    checked:true
                                }
                            }else{
                                return {
                                    checked:false
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                    },
                    {
                        field : 'summary',
                        title : '周报内容',
                        align : 'center',
                    },
                    {
                        field : 'plan',
                        title : '下周计划',
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
}
function examine(type, datas) {
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function (i,v) {
            overAllIds_per.indexOf(v.id) == -1 ? overAllIds_per.push(v.id) : -1;
        });
    }else{
        $.each(datas,function (i,v) {
            overAllIds_per.splice(overAllIds_per.indexOf(v.id),1);
        });
    }
}
function batchExport() {
    //绑定选中事件、取消事件、全部选中、全部取消
    this.$('#queryTable').on('check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine(e.type,datas);
    });
    var ids = new Array();
    // 遍历所有选择的行数据，取每条数据对应的ID
    ids=overAllIds_per;
  /*  var rows = $('#queryTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组*/
    if (ids.length == 0) {
        layer.msg("请选择要导出的数据");
        return;
    }
    layer.confirm("确认要导出选中的'" + ids.length + "'条数据吗?", {
        btn : [ '确定', '取消' ]
        // 按钮
    }, function() {
        window.location.href=prefix+'/batchExport?ids='+ids;
      $.ajax({
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

function examine1(type, datas) {
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function (i,v) {
            overAllIds_dep.indexOf(v.id) == -1 ? overAllIds_dep.push(v.id) : -1;
        });
    }else{
        $.each(datas,function (i,v) {
            overAllIds_dep.splice(overAllIds_dep.indexOf(v.id),1);
        });
    }
}


function batchExport1() {
    //绑定选中事件、取消事件、全部选中、全部取消
   this.$('#queryTable_t').on('check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine1(e.type,datas);
   });
    var ids = new Array();
    // 遍历所有选择的行数据，取每条数据对应的ID
    ids=overAllIds_dep;
        if (ids.length == 0) {
            layer.msg("请选择要导出的数据");
            return;
        }
        layer.confirm("确认要导出选中的'" + ids.length + "'条数据吗?", {
            btn: ['确定', '取消']
            // 按钮
        }, function () {

            /*$.each(rows, function (i, row) {
                ids[i] = row['id'];
            });*/
            window.location.href = prefix + '/batchExport?ids=' + ids;
            $.ajax({
                success: function (r) {
                    if (r.code == 0) {
                        layer.msg(r.msg);
                        reLoad();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        }, function () {
        });

  /*  var rows = $('#queryTable_t').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组*/

}

