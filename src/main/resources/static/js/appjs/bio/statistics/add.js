var prefix = document.getElementsByTagName('meta')['ctx'].content+"/bio/statistics"
$(function() {
    load();
});

function load() {
    $('#statisticsTable')
        .bootstrapTable(
            {
                method:'get',
                url:prefix+'/list',
                useRowAttrFunc: true,
                iconSize : 'outline',
                toolbar : '#toolbar',
                striped : true, // 设置为true会有隔行变色效果
                pagination : true, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
                // //发送到服务器的数据编码类型
                pageSize : 100, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                // //设置为limit则会发送符合RESTFull格式的参数
                columns : [
                    [
                        {
                            "title": "考勤统计表",
                            "align":"center",
                            "colspan": 2
                        }
                    ],
                    [{
                        field : 'userName',
                        title : '学生姓名',
                        align : 'center',
                    },
                        {
                        field : 'absenceRate',
                        title : '缺卡次数',
                        align : 'center'
                    }
                    ]
                ],
                onDblClickCell: function (field,value,row,$element) {
                    $element.attr('contenteditable',true);
                    $element.blur(function () {
                        var index=$element.parent().data('index');
                        var tdValue=$element.html();
                        saveData(index,field,tdValue);
                    })
                }
            });
}


function saveData(index,field,value) {
    $('#statisticsTable').bootstrapTable('updateCell', {
            index:index,
            field:field,
            value:value
})
}

function getData() {
    var getTableData=JSON.stringify($('#statisticsTable').bootstrapTable('getData'));
    $.ajax({
        type: "get",
        url: prefix + "/edit",
        data: {
            datetimepicker1: $('#datetimepicker1').find("input").val(),
            datetimepicker2: $('#datetimepicker2').find("input").val(),
            sum:$('#sum').find("input").val(),
            getTableData:getTableData
        },
        success : function(data) {
            if (data.code == 0) {
                layer.msg("操作成功");
            } else {
                layer.alert(data.msg)
            }
        }
    });

}

