var prefix = document.getElementsByTagName('meta')['ctx'].content + "/bio/query"
var prefix_report = document.getElementsByTagName('meta')['ctx'].content + "/bio/report"
var overAllIds_dep=new Array();
var overAllIds_per=new Array();
var current_Date="";
$(function() {
     load();
});

function load() {
    current_Date=new Date().toLocaleDateString();
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
                pageList:[10,20,50,100],
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                maintainSelected:true,
                clickToSelect:true,
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
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center', formatter : function(value, row, index) {
                            return  '<a class="btn btn-link btn-sm" onclick="getReportContent(\'' + row.id + '\')" target="_blank">' + row.title+ '</a>';
                        }
                    },
                    {
                        field : 'comment',
                        title : '组长评价' ,
                        align : 'center',
                    },
                    {
                        field : 'suggest',
                        title : '老师意见' ,
                        align : 'center',
                    },
                    {
                        field : 'rchgDate',
                        title : '提交时间' ,
                        align : 'center',
                        width: 150
                    }]
            });
    $('#queryTable').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine(e.type,datas);
    });
    $('#queryTable_t')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/list1", // 服务器数据的加载地址
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
                pageList:[10,20,50,100],
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
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
                ]
            });
    $('#queryTable_t').on('check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine1(e.type,datas);
    });

    $('#keyWordTable')
        .bootstrapTable(

            {
                iconSize : 'outline',
                toolbar : '#exampleToolbar',
                striped : true, // 设置为true会有隔行变色效果
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
                clickToSelect:true,
                columns : [
                    {
                        checkbox : true,
                        formatter:function (i,row) {
                            if($.inArray(row.id, overAllIds_per)!=-1){
                                return {
                                    checked:true
                                }
                            }
                        }
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center', formatter : function(value, row, index) {
                            return  '<a class="btn btn-link btn-sm" onclick="getReportContent(\'' + row.id + '\')" target="_blank">' + row.title+ '</a>';
                        }
                    },
                    {
                        field : 'comment',
                        title : '组长评价' ,
                        align : 'center',
                    },
                    {
                        field : 'suggest',
                        title : '老师意见' ,
                        align : 'center',
                    },
                    {
                        field : 'rchgDate',
                        title : '提交时间' ,
                        align : 'center',
                        width: 150
                    }]
            });

    $('#keyWordTable').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine(e.type,datas);
    });

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
function reLoad1() {
    $('#userName').val("")
    $('#WeekMonth').val(1)
    $('#queryTable').bootstrapTable('refresh');

}
function reLoad2() {
    $('#topicName').val("智慧地球")
    $('#WeekMonth1').val(1)
    $('#queryTable_t').bootstrapTable('refresh');

}
function reLoad3() {
    $('#searchName').val("")
    $('#keyWordTable').bootstrapTable('refresh');
}
function query() {
    $.ajax({
        type: "get",
        url: prefix + "/queryReport",
        data: {
            pageNumber :  $("#queryTable").bootstrapTable("getOptions").pageNumber,
            pageSize : $("#queryTable").bootstrapTable("getOptions").pageSize,
            userName: $('#userName').val(),
            WeekMonth: $('#WeekMonth option:selected').val(),
            datetimepicker1: $('#datetimepicker1').find("input").val(),
            datetimepicker2: $('#datetimepicker2').find("input").val()},
        success : function(res) {
            $("#queryTable").bootstrapTable('load',{
                "total": res.data.total,//总数
                "rows": res.data.records   //数据
            });
        }
    });
}

function query1() {
    $.ajax({
        type: "get",
        url: prefix + "/queryDepReport",
        data: {
            pageNumber : $("#queryTable_t").bootstrapTable("getOptions").pageNumber,
            pageSize : $("#queryTable_t").bootstrapTable("getOptions").pageSize,
            topicName: $('#topicName').val(),
            WeekMonth1: $('#WeekMonth1 option:selected').val(),
            datetimepicker_t1: $('#datetimepicker_t1').find("input").val(),
            datetimepicker_t2: $('#datetimepicker_t2').find("input").val()
        },
        success : function(res) {
            $("#queryTable_t").bootstrapTable('load',{
                "total": res.data.total,//总数
                "rows": res.data.records   //数据
            });
        }
    });
}

function Keyquery() {
    $.ajax({
        type: "get",
        url: prefix + "/searchKey",
        data: {
            pageNumber :  $("#keyWordTable").bootstrapTable("getOptions").pageNumber,
            pageSize : $("#keyWordTable").bootstrapTable("getOptions").pageSize,
            searchKey: $('#searchName').val()},
        success : function(res) {
            $("#keyWordTable").bootstrapTable('load',{
                "total": res.data.total,//总数
                "rows": res.data.records   //数据
            });
        }
    });
}


function getReportContent(id) {
    layer.open({
        type: 2,
        title: '周报详情',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix_report + '/reportContent/' + id
    });
}

function getTopicView(deptId){
    var index=layer.open({
        type : 2,
        title : '专题周报详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/weekInfo/'+deptId,
    });
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix_report + '/add' // iframe的url
    });
}

function edit(id) {
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix_report + '/edit/' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix_report+"/remove",
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

function batchExport(exportmode) {
    //绑定选中事件、取消事件、全部选中、全部取消
    if (overAllIds_per.length == 0) {
        layer.msg("请选择要导出的数据");
        return;
    }else{
        layer.confirm("确认要导出选中的'" + overAllIds_per.length + "'条数据吗?", {
            btn : [ '确定', '取消' ]
            // 按钮
        }, function(index) {
         /*   window.location.href = prefix + '/batchExport?ids=' + overAllIds_per;*/
            // var rows=$('#queryTable').bootstrapTable('getSelections');
            // var titlesplit=rows[0].title.split('-');
            // var downloadfilename=titlesplit[0].replace(/\//g,'-') +'-'+titlesplit[1].replace(/\//g,'-')  +'-'+titlesplit[2] +'-'+"周报汇总";
            layer.close(index);
            var loadIndex=layer.load(1,{
                shade: [0.2,'#fff'] //0.1透明度的白色背景
            });
            var time = current_Date.replace(/\//g,'-');
            var downloadfilename=time+"-周报汇总";
            console.log("downloadfilename",downloadfilename);
            if(exportmode==3) {
                //仅导出附件的时候需要判断附件是否存在
                $.ajax({
                    cache: false,
                    type: "GET",
                    url: prefix + "/ifMutilFileExist?reportids="+overAllIds_per,
                    //async: false,
                    dataType: 'json',
                    contentType: 'application/json',
                    error: function (request) {
                        layer.close(loadIndex);
                        layer.alert("Connection error");
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            var url = prefix + '/batchExport?ids=' + overAllIds_per+'&mode='+exportmode+'&downloadfilename='+downloadfilename;
                            try{
                                var elemIF = document.createElement('iframe');
                                elemIF.src = url;
                                elemIF.style.display = 'none';
                                document.body.appendChild(elemIF);
                                // 防止下载两次
                                // setTimeout(function() {
                                //     document.body.removeChild(elemIF)
                                // }, 10000);
                                layer.close(loadIndex);
                            }catch(e){
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
                var url = prefix + '/batchExport?ids=' + overAllIds_per+'&mode='+exportmode+'&downloadfilename='+downloadfilename;
                try{
                    var elemIF = document.createElement('iframe');
                    elemIF.src = url;
                    elemIF.style.display = 'none';
                    document.body.appendChild(elemIF);
                    // 防止下载两次
                    // setTimeout(function() {
                    //     document.body.removeChild(elemIF)
                    // }, 10000);
                    layer.close(loadIndex);
                }catch(e){
                    console.log(e);
                }
            }
        }, function() {
        })
    }

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
    ids = overAllIds_dep;
    if (ids.length == 0) {
        layer.msg("请选择要导出的数据");
        return;
    } else {
        layer.confirm("确认要导出选中的'" + ids.length + "'条数据吗?", {
            btn: ['确定', '取消']
            // 按钮
        }, function (index) {
            layer.close(index);
            var loadIndex = layer.load(1, {
                shade: [0.2, '#fff'] //0.1透明度的白色背景
            });
            //var time = new Date().Format("yyyy-MM-dd");
            var time = current_Date.replace(/\//g,'-');
            var downloadfilename = time + "-周报汇总";
            console.log("downloadfilename", downloadfilename);
            if (exportmode == 3) {
                //仅导出附件的时候需要判断附件是否存在
                $.ajax({
                    cache: false,
                    type: "GET",
                    url: prefix + "/ifTopicFileExist?deptids=" + ids,
                    //async : false,
                    dataType: 'json',
                    contentType: 'application/json',
                    error: function (request) {
                        layer.close(loadIndex);
                        layer.alert("Connection error");
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            var url = prefix + '/batchExport1?ids=' + ids + '&mode=' + exportmode + '&downloadfilename=' + downloadfilename;
                            try {
                                var elemIF = document.createElement('iframe');
                                elemIF.src = url;
                                elemIF.style.display = 'none';
                                document.body.appendChild(elemIF);
                                // // 防止下载两次
                                // setTimeout(function() {
                                //     document.body.removeChild(elemIF)
                                // }, 10000);
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
            } else {
                var url = prefix + '/batchExport1?ids=' + ids + '&mode=' + exportmode + '&downloadfilename=' + downloadfilename;
                try {
                    var elemIF = document.createElement('iframe');
                    elemIF.src = url;
                    elemIF.style.display = 'none';
                    document.body.appendChild(elemIF);
                    // // 防止下载两次
                    // setTimeout(function() {
                    //     document.body.removeChild(elemIF)
                    // }, 10000);
                    layer.close(loadIndex);
                } catch (e) {
                    console.log(e);
                }
            }
        }, function () {
        });
    }
}

//Date.prototype.Format = function (fmt) { // 事件处理函数
//     var o = {
//         "M+": this.getMonth() + 1, // 月份
//         "d+": this.getDate(), // 日
//         "h+": this.getHours(), // 小时
//         "m+": this.getMinutes(), // 分
//         "s+": this.getSeconds(), // 秒
//         "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
//         "S": this.getMilliseconds() // 毫秒
//     };
//     if (/(y+)/.test(fmt))
//         fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
//     for (var k in o)
//         if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
//     return fmt;
// };