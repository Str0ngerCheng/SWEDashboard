
var prefix = document.getElementsByTagName('meta')['ctx'].content
$(function() {
    load();
});

function load() {
    $('#statisticsTable')
        .bootstrapTable(
            {
                useRowAttrFunc: true,
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize : 'outline',
                toolbar : '#toolbar',
                striped : true, // 设置为true会有隔行变色效果
                pagination : true, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 25, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParamsType : "",
                columns : [
                    [
                        {
                            "title": "周报统计表",
                            "align":"center",
                            "colspan": 5
                        }
                    ],
                    [{
                        field : 'deptName',
                        title : '专题名称',
                        align : 'center',
                    },
                        {
                            field : 'isLSubmit',
                            title : '状态',
                            align : 'center',
                            formatter:function(value,row,index){
                                var value="";
                                if(row.isLSubmit==0){
                                    value =  '<label style="color: red">未提交</label>';
                                }else if(row.isLSubmit==1){
                                    value = '<label style="color: green">已提交</label>';
                                }else{
                                    value = row.statusLSub ;
                                }
                                return value;
                            }
                        },
                        {
                            field : 'totalCount',
                            title : '总人数',
                            align : 'center',
                        },
                        {
                            field : 'unMSubmitCount',
                            title : '未交人数',
                            align : 'center',
                        },
                        {
                            field : 'unMSubmitUsers',
                            title : '未交名单',
                            align : 'center',
                            formatter:function(value,row,index){
                                return value==""?"-":value;
                            }
                        },
                    ]
                ]
            });
    $('#reportsTable')
        .bootstrapTable(
            {
                useRowAttrFunc: true,
                /* showRefresh : true,
                 showToggle : true,
                 showColumns : true,*/
                iconSize : 'outline',
                toolbar : '#toolbar',
                dataType : "json", // 服务器返回的数据类型
                pagination : false, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                //search : true, // 是否显示搜索框
                queryParamsType : "",
                // //设置为limit则会发送符合RESTFull格式的参数
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns : [
                    [
                        {
                            "title": "周报详情表",
                            "align":"center",
                            "colspan": 5
                        }
                    ],
                    [{
                        field : 'deptName',
                        title : '专题名称',
                        width: 80,
                        align : 'center',
                        formatter:function(value,row,index){
                            return '<label>'+value+'</label>';
                        }

                    },
                        {
                            field : 'authorName',
                            title : '姓名',
                            width: 80,
                            align : 'center',
                        },
                        {
                            field : 'monthPlan',
                            title : '本月目标',
                            width: 150,
                            align : 'center',
                            formatter:function(value,row,index){
                                return '<textarea style="background:white" class="col-sm-7 form-control" rows="9"  readonly="readonly">'+value+'</textarea>';
                            }
                        },
                        {
                            field : 'summaryAndPlan',
                            title : '本周总结 & 下周计划',
                            align : 'center',
                            formatter:function(value,row,index){
                                return '<textarea style="background:white" class="col-sm-7 form-control" rows="9"  readonly="readonly">'
                                    +'【本周总结】'+"&#10;"
                                    +row.summary+"&#10;"
                                    +"&#10;"
                                    +'【下周计划】'+"&#10;"
                                    +row.nextPlan+'</textarea>';
                            }
                        },
                        {
                            field : 'problemAndComment',
                            title : '问题反馈 & 组长评价',
                            align : 'center',
                            formatter:function(value,row,index){
                                return '<textarea style="background:white" class="col-sm-7 form-control" rows="9"  readonly="readonly">'
                                    +'【问题反馈】'+"&#10;"
                                    +row.problem+"&#10;"
                                    +"&#10;"
                                    +'【组长评价】'+"&#10;"
                                    +row.comment+'</textarea>';
                            }
                        },

                    ]

                ],
                onPostBody:function(){
                    mergeCells(topicReportDetailsList, "deptName",1, $("#reportsTable"))
                }
            });

    $('#statisticsTable').bootstrapTable('load',topicReportStatisticList);
    $('#reportsTable').bootstrapTable('load',topicReportDetailsList);
}

/**
 * 合并行
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性名称数组
 * @param colspan 列数
 * @param target 目标表格对象
 */
function mergeCells(data, fieldName, colspan, target) {
    if (data.length == 0) {
        return;
    }
    var numArr = [];
    var value = data[0][fieldName];
    var num = 0;
    for (var i = 0; i < data.length; i++) {
        if (value != data[i][fieldName]) {
            numArr.push(num);
            value = data[i][fieldName];
            num = 1;
            continue;
        }
        num++;
    }
    numArr.push(num)
    var merIndex = 0;
    for (var i = 0; i < numArr.length; i++) {
        $(target).bootstrapTable('mergeCells', { index: merIndex, field: fieldName, colspan: colspan, rowspan: numArr[i] })
        merIndex += numArr[i];
    }
}

