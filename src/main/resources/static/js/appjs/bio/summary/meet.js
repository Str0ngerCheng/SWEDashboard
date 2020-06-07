var prefix = document.getElementsByTagName('meta')['ctx'].content
$(document).ready(function () {
    load();
    // //监听浏览器异常关闭
    // var beginTime =0;//执行onbeforeunload的开始时间
    // var differTime = 0;//时间差
    // window.onunload = function (){
    //     differTime = new Date().getTime() - beginTime;
    //     if(differTime <= 5) {
    //         console.log("浏览器关闭")
    //     }else{
    //         console.log("浏览器刷新")
    //         var xmlhttp = getXMLHttpRequest();
    //         var allTableData = $('#reportsTable').bootstrapTable('getData');
    //         for(var i=0;i<allTableData.length;i++){
    //             var id ='#'+"suggest"+i;
    //             allTableData[i].suggest=$(id).val();
    //         }
    //         var msg="submitSuggestion";
    //         console.log("allTableData:",allTableData);
    //         var url=prefix + "/bio/summary/submitSuggestion";
    //         var data=JSON.stringify(allTableData);
    //         xmlhttp.open('post', url, true);
    //         xmlhttp.setRequestHeader("Content-Type", "application/json");
    //         //xmlhttp.send(data);
    //         var headers = { type: 'multipart/form-data' };
    //         var blob = new Blob([data], headers);
    //         navigator.sendBeacon(url, blob);
    //         console.log("保存成功",xmlhttp);
    //     }
    //     return "";
    // }
    // window.onbeforeunload = function (){
    //     beginTime = new Date().getTime();
    //     submitSuggestion();
    // };
});

function load() {
    $('#reportsTable')
        .bootstrapTable(
            {
                /* showRefresh : true,
                 showToggle : true,
                 showColumns : true,*/
                uniqueId:'reportsTable',
                iconSize : 'outline',
                toolbar : '#toolbar',
                striped: false,
                hover:false,
                dataType : "json", // 服务器返回的数据类型
                pagination : false, // 设置为true会在底部显示分页条
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                //search : true, // 是否显示搜索框
                queryParamsType : "",
                height: $(window).height()-95,
                // //设置为limit则会发送符合RESTFull格式的参数
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns : [


                    {
                        field : 'deptName',
                        title : '专题名称',
                        width: 85,
                        align : 'center',
                        formatter:function(value,row,index){
                            return '<label>'+value+'</label>';
                        }

                    },
                    {
                        field : 'authorName',
                        title : '姓名',
                        width: 70,
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
                    {
                        field : 'suggest',
                        title : '老师意见',
                        width:200,
                        align : 'center',
                        formatter:function(value,row,index){
                            if(value==null)
                                value=""
                            return '<textarea id="suggest' +index+ '" class="col-sm-5 form-control" rows="9">'+value+'</textarea>';
                        }
                    }

                ],
                onPostBody:function(){
                    mergeCells(topicReportDetailsList, "deptName",1, $("#reportsTable"))
                    $('#reportsTable').bootstrapTable('resetView',{height:$(window).height()-95});
                },
                // onClickCell: function(field, value, row, $element) {
                //     $element.attr('contenteditable', true);
                //     //元素失去焦点事件
                //     $element.blur(function() {
                //         //单元格修改后的的值
                //         var tdValue = $element.html();
                //         console.log(field);
                //         console.log(tdValue);
                //         console.log(row);
                //     })
                // }
            });
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
            num = 0;
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
function submitSuggestion() {
    var allTableData = $('#reportsTable').bootstrapTable('getData');
    for(var i=0;i<allTableData.length;i++){
        var id ='#'+"suggest"+i;
        allTableData[i].suggest=$(id).val();
    }
    var msg="submitSuggestion";
    console.log("allTableData:",allTableData);
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix + "/bio/summary/submitSuggestion",
        dataType: 'json',
        contentType : 'application/json',
        data : JSON.stringify(allTableData),
        async : true,
        success:function (e) {
            msg=e.data;
        },
        error:function (e) {
            msg=e.data;
        }
    });
};
window.onunload=submitSuggestion;
//
// function getXMLHttpRequest() {
//     var xmlRequestObj = null;
//     try {
//         if (window.ActiveXObject) {
//             // IE浏览器下的兼容
//             try {
//                 xmlRequestObj = new ActiveXObject("Msxml2.XMLHTTP");
//             } catch (e) {
//                 try {
//                     xmlRequestObj = new ActiveXObject("Microsoft.XMLHTTP");
//                 } catch (e) {
//                     throw e;
//                 }
//             }
//         } else if (window.XMLHttpRequest) {
//             // Firefox, Opera 8.0+, Safari 其他浏览器
//             xmlRequestObj = new window.XMLHttpRequest();
//         }
//     } catch (e) {
//     }
//     return xmlRequestObj;
// }
