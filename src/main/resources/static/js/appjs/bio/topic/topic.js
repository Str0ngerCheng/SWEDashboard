var prefix = document.getElementsByTagName('meta')['ctx'].content
var prefix_query = document.getElementsByTagName('meta')['ctx'].content + "/bio/query"
var overAllIds_per=new Array();
$(function() {
    load();
});

function load() {
    $('#topicTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/bio/topic/weekList/-1",
                reorderableRows: true,
                useRowAttrFunc: true,
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize : 'outline',
                toolbar : '#toolbar',
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : false, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                queryParamsType : "",
                maintainSelected:true,
                clickToSelect:true,
                // //设置为limit则会发送符合RESTFull格式的参数
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
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
                        field : 'authorName',
                        title : '姓名',
                        align : 'center',
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                        formatter : function(value, row, index) {
                            return  '<a class="btn btn-link btn-sm" onclick="getReportContent(\'' + row.id + '\')" target="_blank">' + row.title+ '</a>';
                        }
                    },
                    {
                        field : 'comment',
                        title : '评价',
                        width:   250,
                        align : 'center',
                    },
                    {
                        field : 'score',
                        title : '评分',
                        align : 'center',
                    },
                    {
                        field : 'rchgDate',
                        title : '提交时间',
                        align : 'center',
                    },
                    {
                        field : 'statusLSub',
                        title : '审阅状态',
                        align : 'center',
                        formatter:function(value,row,index){
                            var value="";
                            if(row.statusLSub=="0"){
                                value = ' <a class="btn btn-danger btn-sm"  title="未审阅"><i class="fa fa-clock-o"></i></a>';
                            }else if(row.statusLSub=="1"){
                                value = ' <a class="btn btn-primary btn-sm"  title="已审阅"><i class="fa fa-check-circle"></i></a> ';
                            }else{
                                value = row.statusLSub ;
                            }
                            return value;
                        }
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-success btn-sm '+'" href="#" mce_href="#" title="详细信息" onclick="getReportContent(\''
                                + row.id
                                + '\')"><i class="fa fa-info-circle"></i></a> ';
                            var d = '<a class="btn btn-primary btn-sm '+'" href="#" title="下载附件"  mce_href="#" onclick="Myexport(\''
                                + row.id
                                + '\')"><i class="fa fa-download"></i></a> ';
                            return e + d ;
                        }
                    } ],
                onLoadSuccess:function(){
                    $('#topicTable').bootstrapTable('checkAll');
                }
            });
    $('#topicTable').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table', function (e, rows) {
        var datas=$.isArray(rows)?rows:[rows];
        examine(e.type,datas);
    });
}
function reLoad() {
    $('#topicTable').bootstrapTable('refresh');
}
function getReportContent(id) {
    layer.open({
        type : 2,
        title : '周报详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/bio/report/markReportContent/'+ id
    });
}

function selectStatus(status){
    var icon='<i class="fa fa-caret-down" aria-hidden="true"></i>';
    if(status==-1)
    $('#status_read').html("全部 "+icon)
    else if(status==1) {
        $('#status_read').html('已阅 '+icon)
    }
    else {
        $('#status_read').html('未阅 '+icon)
    }
    $.ajax({
        type: "get",
        url: prefix + "/bio/topic/weekList/"+status,
        success : function(res) {
            $("#topicTable").bootstrapTable('load', res.data);
            $('#topicTable').bootstrapTable('checkAll');
        }
    });
}
function getTopicView(){
    var index=layer.open({
        type : 2,
        title : '专题周报详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/bio/topic/weekInfo/-1',
    });
    //layer.full(index)
}
function  submit(){

    var loadIndex=layer.load(1,{
        shade: [0.2,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        cache : true,
        type : "GET",
        url : prefix + "/bio/topic/submitInfo",
        dataType: 'json',
        async : true,
        error : function(request) {
            layer.close(loadIndex)
            parent.layer.alert("Connection error");
        },
        success : function(res) {
            if (res.code == 0) {
                var data=res.data;
                var unMSubList=[];
                var unLSubList=[];
                var unMSubUsers='';
                var unLSubUsers='';
                for(var i=0;i<data.length;i++){
                    if(data[i].statusMSub==0) {
                        unMSubList.push(data[i])
                        unMSubUsers+=data[i].authorName+'，'
                    }
                    else  if(data[i].statusLSub==0) {
                        unLSubList.push(data[i])
                        unLSubUsers+=data[i].authorName+'，'
                    }
                }
                if(unLSubList.length>0) {
                    layer.close(loadIndex)
                    layer.alert('专题内<label style="color:red">'+unLSubUsers+'</label>本周的周报还未审阅，请完成审阅再提交！', {icon: 2})
                }
                else if(unMSubList.length>0){
                    layer.alert('专题内<label style="color:red">'+unMSubUsers+'</label>本周的周报还未完成，确定要提交吗？', {
                        icon: 0,
                        btn: ['确定', '取消'],
                        yes: function(index){
                            layer.close(index);
                            resetUserOrder(loadIndex);
                        },
                        btn2: function(index){
                            layer.close(loadIndex);
                            layer.close(index);
                        },
                        cancel: function(index){
                            layer.close(loadIndex);
                            layer.close(index);
                        }
                    })
                }
                else  resetUserOrder(loadIndex);

            } else {
                layer.alert(res.msg)
            }
        }
    });
}

function  resetUserOrder(loadIndex) {
    var allTableData = $('#topicTable').bootstrapTable('getData');
    for(var i=0;i<allTableData.length;i++){
        allTableData[i].orderNum=i
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix + "/bio/topic/submitTopicReports",
        dataType: 'json',
        contentType : 'application/json',
        data : JSON.stringify(allTableData),
        async : false,
        error : function(request) {
            layer.close(loadIndex)
            parent.layer.alert("Connection error");
        },
        success : function(data) {
            layer.close(loadIndex)
            if (data.code == 0) {
                layer.alert("已提交",{icon:1})

            } else {
                layer.alert("您已提交本周专题周报汇总，不要重复提交！",{icon:2})
            }
        }
    });

}

function btnExcelSubject() {
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
            url : prefix + '/batchRemove',
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
    if (overAllIds_per.length == 0) {
        layer.msg("请选择要导出的数据");
        return;
    }else{
        layer.confirm("确认要导出选中的'" + overAllIds_per.length + "'条数据吗?", {
            btn : [ '确定', '取消' ]
            // 按钮
        }, function() {
            $.ajax({
                url:prefix_query + '/batchExport?ids=' + overAllIds_per,
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
        })
    }

}

function Myexport(id) {
    layer.confirm("确认要导出选中的1 条数据吗?", {
        btn : [ '确定', '取消' ]
        // 按钮
    }, function() {
        $.ajax({
            url:prefix_query + '/batchExport?ids=' + overAllIds_per,
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
    })
}

