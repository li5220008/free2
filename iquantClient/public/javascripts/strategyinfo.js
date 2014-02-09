/**
 * User: liuhongjiang
 * Date: 12-12-12
 * Time: 下午4:16
 */

var selectedStraArray=[];//存放选中的策略ID  这里设置为全局变量

$(".sale_out").live('click',function(){
    selectedStraArray =[];
    $(":checkbox[id^='stra_'][checked]").each(function(){
        selectedStraArray.push(parseInt($(this).val()));
    });
    if(selectedStraArray.length==0){
        $.qicTips({message:'请选择策略', level:2, target:'.sale_out', mleft:-30, mtop:-40});
        return;
    }
    $.ajax({
        url: getStrDownTemplateRoute.url(),
        //data: parameters,
        type:"GET",
        dataType:"html",
        success:function (html) {
            $(".sold_out_pop").html(html);
            $(".sold_out_pop").dialog({
                autoOpen: true,
                width: 450,
                height: 500,
                modal: true,
                resizable: false,
                draggable: true
            });
        }
    });



});
//$(".sale_out").click();

(function(){
    downBox('select_info_year1_4', '#options_year1_4', 'reportDate_4', 'reportName_4');
    downBox('select_info_year1_5', '#options_year1_5', 'reportDate_5', 'reportName_5');
    downBox('select_info_year1_6', '#options_year1_6', 'reportDate_6', 'reportName_6');
    downBox('select_info_year1_7', '#options_year1_7', 'reportDate_7', 'reportName_7');
})();

//###########点击确定按钮 进行下架操作####################
$(function(){
   $("#submint_stra_down").live('click',function(){
       var setTime = $("#start_applydate").attr("realValue");
       var strdownMessage = $("#strdownMessage").val();
       var form = $("#strdownForm");
       if(strdownMessage.trim()==""){
           $.qicTips({message:'请输入下架内容', level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
           return;
       }
           if($("#reportDate_4").val() == '12'){//选择“立即下架”
               $("#start_applydate").val("");
               $.qicLoading({
                   target: 'body',
                   text: "loading...",
                   modal: true,
                   width: 220,
                   top: '280px',
                   left: '450px',
                   postion: "absolute",
                   zIndex: 2000
               });
               $.ajax({
                   url: strategyDownRoute.url(),
                   data: form.serialize()+"&stId="+selectedStraArray+"&mark=1",
                   type:"post",
                   dataType:"json",
                   success:function (data) {
                       if(data.flag){
                          $.qicTips({message:data.msg, level:1, target:'#submint_stra_down', mleft:0, mtop:-60});
                          $(".sold_out_pop").dialog("close");
                           setTimeout(function(){ window.location.reload()},400)//为了让提示信息正确显示，延迟刷新页面
                       }
                       else{
                           $.qicTips({message:data.msg, level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
                       }
                   }
               });
               $.qicLoading({remove: true});//移除loading。。。
               return;
           }
           if($("#reportDate_4").val() == '11'){//选择“延时下架”
               var currentTime = new Date();
               var compareTime = Date.parse(setTime.replace(/-/g, "/"))
               if(compareTime<currentTime){
                  $.qicTips({message:'设置日期必须大于当前日期', level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
                  return;
              }
               //加载数据之前 显示loading。。。效果
               $.qicLoading({
                   target: 'body',
                   text: "loading...",
                   modal: true,
                   width: 220,
                   top: '280px',
                   left: '450px',
                   postion: "absolute",
                   zIndex: 2000
               });

               $.ajax({
                   dataType:"json",
                   type:"post",
                   url: strategyDownRoute.url(),
                   data:form.serialize()+"&stId="+selectedStraArray+"&mark=2",
                   success:function (data) {
                       if(data.flag){
                           $.qicTips({message:data.msg, level:1, target:'#submint_stra_down', mleft:0, mtop:-60});
                           $(".sold_out_pop").dialog("close");
                           setTimeout(function(){ window.location.reload()},400)//为了让提示信息正确显示，延迟刷新页面
                       }
                       else{
                           $.qicTips({message:data.msg, level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
                       }
                   }
               });
               $.qicLoading({remove: true});//移除loading。。。
             return;
           }
       $.qicTips({message:'请选择下架方式', level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
   })

   //点击”取消“按钮关闭对话框
    $("#concel_stra_down").live('click',function(){
        $(".sold_out_pop").dialog("close");
    })

    /**
     * 选择"立即下架" 1.隐藏时间设置 2.替换“下架通知”的策略名和下架时间
     * 选择"下架设置" 替换“下架通知”的策略名和下架时间
     */
    $("#options_year1_4 li").live('click', function(){
        var index = $(this).index();
        if(index == 0){
           // replaceNameAndDate(0);//选择"立即下架"替换 “下架通知”的策略名和下架时间
            $("#hidDate").hide();
        }else{
         //   replaceNameAndDate("2012,12,12");//选择"下架设置"替换 “下架通知”的策略名和下架时间
            $("#hidDate").show();
        }

    });

});

/*
//拿到下架通知模板 替换策略名和下架时间
function replaceNameAndDate(d){
    $(".soldout_notice").html("尊敬的用户您好！“#”将于*下架，请悉知。");
        var text=$(".soldout_notice").html();
        if(d!=0){
            var myDate = new Date(d);
        }else{
            var myDate = new Date();
        }
    if(text.indexOf("#")!=-1 && text.indexOf("*")!=-1){//重新加载-》替换
         var selectedStraName =[];
        $(":checkbox[id^='stra_'][checked]").each(function(){
            selectedStraName.push($(this).attr("param_name"));
        });
        var year = myDate.getFullYear();
        var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
        var day = ("0" + myDate.getDate()).slice(-2);
        var startStr = text.substring(0,text.indexOf("#"));
        var middStr = text.substring(text.indexOf("#")+1,text.indexOf("*"));
        var endStr = text.substring(text.indexOf("*")+1);
        var dateStr =(year+"年"+month+"月"+day+"日");
        $(".soldout_notice").replaceWith("<div class='soldout_notice'> "+startStr+selectedStraName+middStr+dateStr+endStr+"</div>");
    }


  var reg=/\[\$[a-z]+\.[A-Z]{1}[\W\w]+/gi;
 //var reg=/^(\+|-)?\d+$/;
 if(!reg.test(strdownMessage)){
 $.qicTips({message:'请按提示填写下架信息', level:2, target:'#submint_stra_down', mleft:0, mtop:-60});
 return;
 }
}*/


