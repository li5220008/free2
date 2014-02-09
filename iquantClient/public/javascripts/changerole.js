/**
 * 角色用户操作
 * User: liuhongjiang
 * Date: 12-12-11
 * Time: 上午9:33
 */

/*var userid=[];
var userRoleid=[];
var username=[];
var userRolename=[];
var useraccount=[];
var userRoleaccount=[];


function initTable(){
    $(".tbl_checked tr").each(function(){
        userid.push($(this).attr("param_id"));
        username.push($(this).attr("param_name"));
        useraccount.push($(this).attr("param_account"));
    });
    $(".tb1_role_checked tr").each(function(){
        userRoleid.push($(this).attr("param_roleid"));
        userRolename.push($(this).attr("param_rolename"));
        userRoleaccount.push($(this).attr("param_roleaccount"));
    });
}*/

//加载用户列表，角色用户列表
function changeroleDialog(url, parameters, renderContainer) {
            //加载数据之前 显示loading。。。
                $.qicLoading({
                    target: 'body',
                    text: "努力加载中...",
                    modal: true,
                    width: 180,
                    top: '290px',
                    left: '450px',
                    postion: "absolute",
                    zIndex: 2000
                });

         $.ajax({
                 url: url,
                 data: parameters,
                 type:"GET",
                 dataType:"html",
                 success:function (html) {
                     $(renderContainer).html(html);
                     $(renderContainer).dialog({
                         autoOpen: true,
                         width: 590,
                         modal: true,
                         resizable: false,
                         draggable: true
                     });
                 }
                });
    $.qicLoading({remove: true});//移除loading。。。
}

$(function(){
    //点击 加载用户列表，角色用户列表
    $(".add_remove_user").live('click',function() {
        var rid = $(".current").attr("id").substring("ut_".length);
        changeroleDialog(changeroleRoute.url(), {id: rid}, ".set_user_list");

    });

    <!--选中高亮 begin-->
    $(".tbl_checked tr").live('click', function(){
        var target = $(this);
        if(target.attr("class") == "tr_checked") {
            target.removeClass("tr_checked");
        } else {
            target.addClass("tr_checked");
        }
    });

    $(".tb1_role_checked tr").live('click', function(){
        var target = $(this);
        if(target.attr("class") == "tr_checked_1") {
            target.removeClass("tr_checked_1");
        } else {
            target.addClass("tr_checked_1");
        }
    });
    <!--选中高亮 end-->

      <!--点击”添加/删除“按钮 左右移动列表内容 begin-->
    $("#addThisRole").live('click',function(){
        if($(".tb1_role_checked tr").first().attr("default_value")=='noResult'){
            var table = $(".tb1_role_checked");
            $(".tb1_role_checked tr").remove();
            var tr = $("<tr ></tr>")
                .append($("<th></th>").html('姓名'))
                .append($("<th></th>").html('账号'))
            table.append(tr);
        }

        $(".tr_checked").each(function(){
            $(this).remove();
            var element = $(this);
            var id = element.attr("param_id")
            var name = element.attr("param_name")
            var account = element.attr("param_account")
            var table = $(".tb1_role_checked");
            var tr = $("<tr param_roleid='"+id+"' param_rolename='"+name+"'  param_roleaccount='"+account+"' style='cursor: pointer'></tr>")
                .append($("<td></td>").html(element.attr("param_name")))
                .append($("<td></td>").html(element.attr("param_account")))
            //table.append(tr);
            tr.insertAfter($(".tb1_role_checked tr").first());
        });


    });

    $("#deleteThisRole").live('click',function(){
        if($(".tbl_checked tr").first().attr("default_value")=='noResult'){
            var table = $(".tbl_checked");
            $(".tbl_checked tr").remove();
            var tr = $("<tr ></tr>")
                .append($("<th></th>").html('姓名'))
                .append($("<th></th>").html('账号'))
            table.append(tr);
        }

        $(".tr_checked_1").each(function(){
            $(this).remove();
            var element = $(this);
            var id = element.attr("param_roleid");
            var name = element.attr("param_rolename");
            var account = element.attr("param_roleaccount");
            var table = $(".tbl_checked");
            var tr = $("<tr param_id='"+id+"' param_name='"+name+"' param_account='"+account+"' style='cursor: pointer'></tr>")
                .append($("<td></td>").html(element.attr("param_rolename")))
                .append($("<td></td>").html(element.attr("param_roleaccount")))
            //table.insertBefore(tr, $(".tbl_checked tr").first());
            tr.insertAfter($(".tbl_checked tr").first());
        });
    });
});
<!--点击”添加/按钮“ 左右移动列表内容 end-->


//--鼠标按下 取消文本框提示消息 并聚焦 begin---
$(function(){
    $(".set_user_i").live('mousedown',function(){
        if( $(".set_user_i").val()=='请输入姓名/账号'){
            $(".set_user_i").val("");
            $(".set_user_i").focus;
        }
    });

    $(".set_user_i_2").live('mousedown',function(){
        if( $(".set_user_i_2").val()=='请输入姓名/账号'){
            $(".set_user_i_2").val("");
            $(".set_user_i_2").focus;
        }
    })
})
//####### 鼠标按下 取消文本框提示消息 并聚焦 end #######

//--在用户列表输入内容 按enter 显示查询结果 begin----
$(".set_user_i").live('keypress',function(event){
    var keycode = event.which;
    var condition = $(".set_user_i").val();
    if (keycode == 13) {
        //加载数据之前 显示loading。。。
            $.qicLoading({
                target: 'body',
                text: "努力加载中...",
                modal: true,
                width: 180,
                top: '290px',
                left: '450px',
                postion: "absolute",
                zIndex: 2000
            });

        $.ajax({
            url: getUserRount.url(),
            data: {condition : condition},
            type:"GET",
            dataType:"json",
            success:function (data) {
                var table = $(".tbl_checked");
                if(data.length==0){
                    $(".tbl_checked tr").remove();
                    var tr = $("<tr style='color: #f6a828;' default_value='noResult'></tr>")
                        .append($("<th></th>").html('没有匹配的查询结果'))
                    table.append(tr);
                    $.qicLoading({remove: true});//移除loading。。。
                    return;
                }
               /* $(".tr_checked").each(function(){
                    $(this).remove();
                });*/
                $(".tbl_checked tr").remove();
                var tr = $("<tr ></tr>")
                    .append($("<th></th>").html('姓名'))
                    .append($("<th></th>").html('账号'))
                table.append(tr);
                for(var i=0;i<data.length;i++){
                   var id = data[i]._1;
                   var name = data[i]._2;
                   var account = data[i]._3;
                    var tr = $("<tr class='tr_checked' param_id='"+id+"' param_name='"+name+"' param_account='"+account+"' style='cursor: pointer'></tr>")
                        .append($("<td></td>").html(name))
                        .append($("<td></td>").html(account))
                    table.append(tr);
                }
            }
        });
        $.qicLoading({remove: true});//移除loading。。。
    }
});


$(".set_user_i_2").live('keypress',function(event){
    var keycode = event.which;
    // 文本框内容
    var condition = $(".set_user_i_2").val();
    //当前选中的角色ID
    var rid = $(".current").attr("id").substring("ut_".length);
    if (keycode == 13) {
        //加载数据之前 显示loading。。。
            $.qicLoading({
                target: 'body',
                text: "努力加载中...",
                modal: true,
                width: 180,
                top: '300px',
                left: '770px',
                postion: "absolute",
                zIndex: 2000
            });
        $.ajax({
            url: getRoleUserRount.url(),
            data: {condition : condition,roleId:rid},
            type:"GET",
            dataType:"json",
            success:function (data) {
                var table = $(".tb1_role_checked");
                if(data.length==0){
                    $(".tb1_role_checked tr").remove();
                    var tr = $("<tr style='color: #f6a828;' default_value='noResult' ></tr>")
                        .append($("<th></th>").html('没有匹配的查询结果'))
                    table.append(tr);
                    $.qicLoading({remove: true});//移除loading。。。
                    return;
                }

                /* $(".tr_checked").each(function(){
                 $(this).remove();
                 });*/
                $(".tb1_role_checked tr").remove();
                var tr = $("<tr ></tr>")
                    .append($("<th></th>").html('姓名'))
                    .append($("<th></th>").html('账号'))
                table.append(tr);
                for(var i=0;i<data.length;i++){
                    var id = data[i]._1;
                    var name = data[i]._2;
                    var account = data[i]._3;
                    var tr = $("<tr class='tr_checked_1' param_roleid='"+id+"' param_rolename='"+name+"' param_roleaccount='"+account+"' style='cursor: pointer'></tr>")
                        .append($("<td></td>").html(name))
                        .append($("<td></td>").html(account))
                    table.append(tr);
                }

            }
        });
        $.qicLoading({remove: true});//移除loading。。。
    }
});
<!--在列表输入内容 按enter 显示查询结果 end-->


<!--点击”确定“按钮提交 用户转换角色(后台) bigen-->
$(function(){
    $("#submit_change").live('click', function(){
        var flag1 =false;
        var flag2 =false;
        var form = $("#changeRoleForm");
        var urid = [];//角色用户列表中用户ID数组
        var uid = [];//用户列表中用户ID数组
        //当前选中的角色ID
        var rid = $(".current").attr("id").substring("ut_".length);
        $(".tbl_checked tr").each(function(){
            if($(this).attr("param_id")!=undefined){
                uid.push($(this).attr("param_id"));
            }
            console.log(uid);
        })
        $(".tb1_role_checked tr").each(function(){
            if($(this).attr("param_roleid")!=undefined){
                urid.push($(this).attr("param_roleid"));
            }
            console.log(urid);
        })
        //加载数据之前 显示loading。。。
            $.qicLoading({
                target: 'body',
                text: "努力加载中...",
                modal: true,
                width: 180,
                top: '50%',
                left: '50%',
                postion: "absolute",
                zIndex: 2000
            });
        $.ajax({
            url: changeUserRoleRount.url(),
            data: form.serialize()+"&urid="+urid+"&uids="+uid+"&rid="+rid,
            type:"post",
            dataType:"json",
            success:function (data) {
                if(data.flag){
                    $.qicTips({message:data.msg, level:1, target:'#submit_change', mleft:0, mtop:-60});
                }else{
                    $.qicTips({message:data.msg, level:2, target:'#submit_change', mleft:0, mtop:-60});
                }
            }
        });
        $.qicLoading({remove: true});//移除loading。。。
    });
});
<!--点击”确定“按钮提交 用户转换角色(后台) end-->


//点击”取消“按钮 关闭对话框
$(function(){
   $("#cancel_change").live('click',function(){
       $(".set_user_list").dialog("close");
   });
});



//点击”重置“按钮 返回初始状态
$(function(){

    $("#reset_change").live('click',function(){
        var rid = $(".current").attr("id").substring("ut_".length);
        changeroleDialog(changeroleRoute.url(), {id: rid}, ".set_user_list");

       /* var table = $(".tb1_checked");
        $(".tbl_checked tr").remove();
        var tr = $("<tr ></tr>")
            .append($("<th></th>").html('姓名'))
            .append($("<th></th>").html('账号'))
        table.append(tr);
        for(var i=0;i<userid.length;i++){
            var id = userid[i];
            var name = username[i];
            var account = useraccount[i];
            var tr = $("<tr class='tr_checked' param_id='"+id+"' param_name='"+name+"' param_account='"+account+"' style='cursor: pointer'></tr>")
                .append($("<td></td>").html(name))
                .append($("<td></td>").html(account))
            table.append(tr);
        }


        var rtable = $(".tb1_role_checked");
        $(".tb1_role_checked tr").remove();
        var tr = $("<tr ></tr>")
            .append($("<th></th>").html('姓名'))
            .append($("<th></th>").html('账号'))
        table.append(tr);
        for(var i=0;i<userRoleid.length;i++){
            var id = userRoleid[i];
            var name = userRolename[i];
            var account = userRoleaccount[i];
            var tr = $("<tr class='tr_checked_1' param_roleid='"+id+"' param_rolename='"+name+"' param_roleaccount='"+account+"' style='cursor: pointer'></tr>")
                .append($("<td></td>").html(name))
                .append($("<td></td>").html(account))
            rtable.append(tr);
        }*/

    });
});
