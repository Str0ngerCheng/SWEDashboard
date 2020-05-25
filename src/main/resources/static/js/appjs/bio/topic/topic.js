var prefix = document.getElementsByTagName('meta')['ctx'].content
$(function() {
    load();
});

function load() {
    $('#topicTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/bio/topic/weekList", // 服务器数据的加载地址
                reorderableRows: true,
                striped: true,
                useRowAttrFunc: true,
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
                        field : 'name',
                        title : '姓名',
                        align : 'center',
                    },
                    {
                        field : 'title',
                        title : '周报题目',
                        align : 'center',
                        formatter : function(value, row, index) {
                            return  '<a class="btn btn-link btn-sm" onclick="getReportContent(\'' + index + '\')" target="_blank">' + row.title+ '</a>';
                        }
                    },
                    {
                        field : 'submitTime',
                        title : '提交时间',
                        align : 'center',
                    },
                    {
                        field : 'status',
                        title : '状态',
                        align : 'center',
                        formatter:function(value,row,index){
                            var value="";
                            if(row.status=="0"){
                                value = ' <a class="btn btn-danger btn-sm"  title="未审阅"><i class="fa fa-clock-o"></i></a>';
                            }else if(row.status=="1"){
                                value = ' <a class="btn btn-success btn-sm"  title="已审阅"><i class="fa fa-check-circle"></i></a> ';
                            }else{
                                value = row.status ;
                            }
                            return value;
                        }
                    },
                    {
                        field : 'comment',
                        title : '评价',
                        width:250,
                        align : 'center',
                    },
                    {
                        field : 'score',
                        title : '评分',
                        align : 'center',
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-success btn-sm '+'" href="#" mce_href="#" title="详细信息" onclick="getReportContent(\''
                                + row.id
                                + '\')"><i class="fa fa-info-circle"></i></a> ';
                            var d = '<a class="btn btn-primary btn-sm '+'" href="#" title="下载附件"  mce_href="#" onclick="export(\''
                                + row.id
                                + '\')"><i class="fa fa-download"></i></a> ';
                            return e + d ;
                        }
                    } ]
            });
}
function reLoad() {
    $('#topicTable').bootstrapTable('refresh');
}
function getReportContent(row) {
    console.log($('#topicTable').bootstrapTable('getData'))
    layer.open({
        type : 2,
        title : '周报详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/bio/report/reportContent'
    });
}

function setCommentAndScore(comment,score,index){
    console.log([comment,score,index])
    $('#topicTable').bootstrapTable('updateRow', {
        index:index,
        replace:false,
        row: {
            comment: comment,
            score:score,
            status:1
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

