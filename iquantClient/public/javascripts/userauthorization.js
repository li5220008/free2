
var isReloadIfSubmit = true;//全局变量 点击“确定”按钮后 控制是否reload页面
var resetAll = true;//全局变量 重置方式 true：全部重置 false：只重置input
var isReloadIfCancel = false;//全局变量 点击“取消”按钮后 控制是否reload页面
 function showUserAuthorizationDialog(url, parameters, title, renderContainer,reloadWindow,isResetAll,reloadWindowIfCancel) {
     if(typeof(reloadWindow)=="boolean"){
         isReloadIfSubmit = reloadWindow;
     }
     if(typeof(reloadWindowIfCancel)=="boolean"){
         isReloadIfCancel = reloadWindowIfCancel;
     }
     if(typeof(isResetAll)=="boolean"){
         resetAll = isResetAll;
     }
     //加载数据之前 显示loading。。。
         $.qicLoading({
             target: 'body',
             text: "努力加载中...",
             modal: true,
             width: 220,
             top: '280px',
             left: '450px',
             postion: "absolute",
             zIndex: 2000
         });

    $.ajax({
        url: url,
        data: {"idArray":parameters.idArray.toString()},
        type:"post",
        dataType:"html",
        success:function (html) {
            $(renderContainer).attr("title", title);
            $(renderContainer).html(html);
                $(renderContainer).dialog({
                    autoOpen: true,
                    width: 590,
                    height: 720,
                    modal: true,
                    resizable: false,
                    draggable: true
                });
        }
    });
     $.qicLoading({remove: true});//移除loading。。。
}

function removeUserFromList(idArray) {
    for(var i in idArray) {
        $(".tbl_checked tr[userid='" + idArray[i] + "']").remove();
    }
    if($(".tbl_checked tbody tr").size() <= 1) {
        //关闭对话框
        $(".privilege_manage").dialog("close");
        if(isReloadIfSubmit){
            setTimeout(function(){window.location.reload();},400);
    }
    }
}

$(function(){
    //点击关闭对话框
    $("#authorize_cancel").live("click", function(){
        //关闭对话框
        $(".privilege_manage").dialog("close");
        if(isReloadIfCancel){
            window.location.reload();
        }
    });

        //点击重置
        $("#authorize_reset").live('click',function(){
            if(resetAll){
                var idArray = [];
                $(":checkbox[id^='userid']").each(function(){
                    if($(this).attr("checked") == "checked"){
                        idArray.push(parseInt($(this).val()));
                    }
                });
                showUserAuthorizationDialog(authorizationRoute.url(), {idArray: idArray}, "更改用户权限", ".privilege_manage");
            }else{
                $("#start_applydate").attr("value","");
                $(":checkbox[id^='authorizationrole_']").each(function(){
                    $(this).attr("checked",false);
                })

            }
        });

});



$(function(){
    $(".tbl_checked tr").live('click', function(){
        var target = $(this);
        if(target.attr("class") == "tr_checked") {
            target.removeClass("tr_checked");
        } else {
            target.addClass("tr_checked");
        }
    })
    $("#authorization").live("click", function(){
        var form = $("#authorizeForm");
        var selectedUserIdArray = [];
        var selectedRoleIdArray = [];
        var currentTime = new Date();
        var setTime = $("#start_applydate").attr("realValue");
        var compareTime = Date.parse(setTime.replace(/-/g, "/"));
        $(".tbl_checked tr[class='tr_checked']").each(function(){
            selectedUserIdArray.push(parseInt($(this).attr("userid")));
        });
        $(":checkbox[id^='authorizationrole_'][checked]").each(function(){
            selectedRoleIdArray.push(parseInt($(this).val()));
        });
        if(selectedUserIdArray.length==0){
            $.qicTips({message:'请选择用户', level:2, target:'#authorization', mleft:0, mtop:-60});
            return;
        }
        if(selectedRoleIdArray.length==0){
            $.qicTips({message:'请选择所属角色', level:2, target:'#authorization', mleft:0, mtop:-60});
            return;
        }
      /*  if(compareTime<currentTime){
            $.qicTips({message:'设置日期必须大于当前日期', level:2, target:'#authorization', mleft:0, mtop:-60});
            return;
        }*/
        //加载数据之前 显示loading。。。
        $.qicLoading({
            target: 'body',
            text: "努力加载中...",
            modal: true,
            width: 220,
            top: '340px',
            left: '550px',
            postion: "absolute",
            zIndex: 2000
        });
        $.ajax({
            url:getRidRoute.url(),
            type:'post',
            data:form.serialize()+"&uids="+selectedUserIdArray+"&rid="+selectedRoleIdArray+"&edate="+setTime,
            //{uid:selectedUserIdArray,rid:selectedRoleIdArray,edate:setTime},
            dataType:"json",
            success:function (data) {
                if (data.returnBoolean) {
                    $.qicTips({message:data.msg, level:1, target:'#authorization', mleft:0, mtop:-60});
                    removeUserFromList(selectedUserIdArray);
                } else {
                    $.qicTips({message:data.msg, level:2, target:'#authorization', mleft:0, mtop:-60});
                }
            }
        });
       // $.qicLoading({remove: true});//移除loading。。。
    })
});

<!--点击获得日期 begin-->
(function ($) {
    var FormatDateTime = function FormatDateTime() { };
    $.FormatDateTime = function (days) {
        //var correcttime1 = eval('( new ' + obj.replace(new RegExp("\/", "gm"), "") + ')');
        var myDate = new Date();
        myDate.setDate(myDate.getDate()+days);
        var year = myDate.getFullYear();
        var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
        var day = ("0" + myDate.getDate()).slice(-2);

        var s=year+"-"+month+"-"+day;
        return s ;

    }
})(jQuery);

$(function(){
$("#oneweek").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(7));
    $("#start_applydate").attr("realValue",$.FormatDateTime(7));
});
$("#onemonth").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(30));
    $("#start_applydate").attr("realValue",$.FormatDateTime(30));
});
$("#threemonth").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(90));
    $("#start_applydate").attr("realValue",$.FormatDateTime(90));
});
$("#halfyear").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(180));
    $("#start_applydate").attr("realValue",$.FormatDateTime(180));
});
$("#oneyear").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(365));
    $("#start_applydate").attr("realValue",$.FormatDateTime(365));
});
$("#twoyear").live('click',function(){
    $("#start_applydate").val($.FormatDateTime(730));
    $("#start_applydate").attr("realValue",$.FormatDateTime(730));
});

});
<!--点击获得日期 begin-->
