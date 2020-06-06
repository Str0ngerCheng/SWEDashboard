var prefix = document.getElementsByTagName('meta')['ctx'].content +"/sys/dept"
var deptName= '';
$(function() {
    getTreeData();
});


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

function selectOneTopic(name){
    parent.setTopic(name)
    parent.layer.closeAll()
}

$('#jstree').on("changed.jstree", function(e, data) {
    var opt = {
        query : {
            deptName : data.node.text,
        }
    };
    console.log(opt.query.deptName);
    selectOneTopic(opt.query.deptName);
});