var prefix = document.getElementsByTagName('meta')['ctx'].content
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
                striped: true,
                useRowAttrFunc: true,
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize : 'outline',
                toolbar : '#toolbar',
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : true, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
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
                columns : [
                    {
                        checkbox : true
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
function getReportContent(id) {
    layer.open({
        type : 2,
        title : '周报详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/bio/report/reportContent/'+ id
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
        content : prefix + '/bio/topic/weekInfo',
    });
    //layer.full(index)
}
function  submit(){
    var allTableData = $('#topicTable').bootstrapTable('getData');
    for(var i=0;i<allTableData.length;i++){
        allTableData[i].orderNum=i
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix + "/sys/user/batchUpdateOrderNum",
        dataType: 'json',
        contentType : 'application/json',
        data : JSON.stringify(allTableData),
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(data) {
            if (data.code == 0) {
                layer.alert("已提交",{icon:1})

            } else {
                layer.alert(data.msg)
            }
        }
    });
}

