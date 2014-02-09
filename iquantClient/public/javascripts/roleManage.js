/*角色名称操作js*/
var utMap = {};
var cndType =2;
var allowClick = true;
var showAddRoleInput = false;
$(function() {
    //条件管理,可编辑元素
    window.editElement(".role_list_ul li", cndType); //可编辑元素
            $("#ut_ul_list > li").click( function () {
                if($(this).attr("id") != ("ut_"+$("#curRoleId").val())){
                    var curTab = $("#curTab").val();
                    var curRoleId = $(this).attr("roleId");
                    top.window.location.replace("/RoleInfos/roleList?tab="+curTab+"&roleId=" + curRoleId);
                    return;
                }
                $("#ut_ul_list > li.current").removeClass("current");
                var $li = $(this);
                $li.addClass("current");
                var cndId = $li.attr("id").substring("ut_".length);
                if (cndId == -999) { //新的节点
                    return;
                }
                var cndJson = utMap[cndId];

    }).mouseover(function(){
            if($(this).attr("id") != ("ut_"+$("#curRoleId").val())){
                $(this).css("cursor","pointer");//鼠标变手型
                return;
             }

   });

    //删除搜索条件
    $("#delCndBtn").click(function () {
        var $li = $("#ut_ul_list > li.current");
        if ($li.size() == 0) {
            $.qicTips({message:'请选择角色名', level:2, target:'#delCndBtn', mleft:0, mtop:-10});
            return;
        }
        var cndId = $li.attr("id").substring("ut_".length);
        if (cndId == -999) {
            $li.remove();
            $("#ut_ul_list > li:first").trigger("click");
            return;
        }
        //var cfm = confirm("是否确认删除");
        //if (!cfm) return; //不删除则直接返回

        new QicDialog({
            message:"是否确认删除",
            title:"删除角色" ,
            confirm:function(){
                //加载数据之前 显示loading。。。效果
                $.qicLoading({
                    target: 'body',
                    text: "响应操作中...",
                    modal: true,
                    width: 220,
                    top: '280px',
                    left: '450px',
                    postion: "absolute",
                    zIndex: 2000
                });
                $.ajax({
                    url:delRoleRoute.url({id:cndId}),
                    type:delRoleRoute.method,
                    dataType:"json",
                    success:function (data) {
                        if (data.op) {
                            $li.remove();
                            $.qicTips({message:data.msg, level:1, target:'#ut_ul_list', mleft:0, mtop:-60});
                            //删除成功后, 又让第一个被选中
                            setTimeout(function() { $("#ut_ul_list > li:first").trigger("click");},400)
                        } else {
                            $.qicTips({message:data.msg, level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
                        }
                    }
                });
                //$.qicLoading({remove: true});//移除loading。。。
            },
            cancel:function(){
                return;
            }
        }).confirm()
    });

    //增加搜索条件
    $("#addCndBtn").click(function () {
     /*   $("#ut_ul_list > li").each(function(){
           // alert($(this).attr('roleid'))
            if($(this).attr('roleid')==undefined){
                $.qicTips({message:"正在保存当前角色，请稍后", level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
                input.focus();
                return;
            }
        })*/
        if(!allowClick){
            showAddRoleInput = true;
            return;
        }
        allowClick =false;
        if ($("#ut_ul_list > li").size() >= 30) {
           // alert("自定义角色上限为30个");
            new QicDialog({
                message:"自定义角色上限为30个",
                title:"提示"
            }).warn();
            return;
        }

        $("#ut_ul_list > li.current").removeClass("current");//先去掉选中的
        var newNameTmp = "角色";
        var newName = "";
        var tmpNameIntArr = []; //用于保存 newNameTmp 开头的接下来的整数值
        $("#ut_ul_list li a").each(function () {
            var itemHtml = $(this).html();
            if (itemHtml.indexOf(newNameTmp) > -1) {
                var idx = parseInt(itemHtml.substring(newNameTmp.length));//后续数字
                if (isNaN(idx)) {
                    tmpNameIntArr.push(0);
                } else {
                    tmpNameIntArr.push(idx);
                }
            }
        });
        //排序取出最大的, 作为名称
        if (tmpNameIntArr.length > 0) {
            tmpNameIntArr.sort(new Function("a","b","return a-b;"));
                 newName = newNameTmp + (tmpNameIntArr[tmpNameIntArr.length - 1] + 1);
                 //newName = newNameTmp;
        } else {
            newName = newNameTmp + "1";
            //newName = newNameTmp ;
        }
        var $li = $("<li id='ut_-999'><a title='双击修改角色名称'>" + newName + "</a></li>");
        $li.addClass("current");
        $li.appendTo($("#ut_ul_list"));
        $('#ut_ul_list').scrollTop($('#ut_ul_list')[0].scrollHeight); //滚动到底端
        $li.trigger("dblclick");
    });


    //触发第一个被选中
    if ($("#ut_ul_list > li").size() > 0) {
       // console.log("有啊");
       // $("#ut_ul_list > li:first").trigger("click");  update by liujl
    } else {
       // console.log("safasdfasd");
        //把星级也重置
       // $("#options_starDown li.star_1").trigger("click");
       // $("#options_starUp li.star_5").trigger("click");
       // console.log("d22");
    }
});



/*

//保存按钮点击事件处理. 这里也就是修改了
function saveBtn_onclick(){
    if(cndType == 2){ //股票池
        treeVal();
    }
    var $form = $("#cndForm");
    var $li = $("#ut_ul_list > li.current");
    if($li.size() == 0){
        $.qicTips({message:'请先选择查询条件', level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
        return ;
    }
    var cndId = $li.attr("id").substring("ut_".length);

    $.ajax({
      url: saveCondRoute.url(),
      //type: saveCondRoute.method,
      type: 'post',
      dataType: "json",
      //data:$form.serialize() + "&cndName=" + $("#cndName").val(),
      data:$form.serialize() + "&id=" + cndId,
      success:function(data){
        if(data.op){
            //操作成功, 要增加一个dom节点
            //var domStr = "<li id='ut_" + data.id + "'>" + cndName + "</li>";
            //$("#ut_ul_list").append(domStr);
            utMap[cndId] = data.utInfo;
            $.qicTips({message:data.msg, level:1, target:'#ut_ul_list', mleft:0, mtop:-60});
        }else{
            $.qicTips({message:data.msg, level:1, target:'#ut_ul_list', mleft:0, mtop:-60});
        }
      }
    });
}
*/

//小值是否比大值还大
function downIsLargeUp(id1, id2){
    var downVal = parseFloat($(id1).val());
    var upVal = parseFloat($(id2).val());
    if(downVal > upVal){
        return true;
    }else{
        return false;
    }
}

function utListItemdblclick2(e){
    if(li.attr("id") != ("ut_"+$("#curRoleId").val())){
        // top.window.location.replace("http://baidu.com");
        return;
    }
    utListItemdblclick();

}

//搜索条件列表项双击事件处理
function utListItemdblclick(e) {
    var typeVal = e.data.type;
    var li = $(this);
 /*   if(li.attr("id") != ("ut_"+$("#curRoleId").val())){
       // top.window.location.replace("http://baidu.com");
        return;
    }*/
    var data_id=0;
    li.removeClass("current");
    var cndId = li.attr("id").substring("ut_".length); //模板id
    var text = li.text();
    li.html("");
    var input = $("<input type='text' style='margin-left: 35px'>");
    input.attr("value", text);
    input.blur(function (event){//失去焦点
        li.addClass("current");
        var inputtext = $.trim(input.val());
        if(inputtext.length == 0){
            $.qicTips({message:"角色名不能为空", level:2, target:'.current', mleft:10, mtop:-30});
            input.focus();
            return ;
        }else if(inputtext.length > 15){
            $.qicTips({message:"超过15字符长度", level:2, target:'.current', mleft:10, mtop:-30});
            input.focus();
            return ;
        }

        var sameName = false;
        $("#ut_ul_list > li a").each(function(i){
            var $eachThis = $(this);
            if($eachThis == li){ //当前节点

            }else if(inputtext == $eachThis.html()){ //说明重名
                sameName = true;
                return false;
            }

            return true;
        });
        if(sameName){
            $.qicTips({message:"名称已存在", level:2, target:'.current', mleft:10, mtop:-30});
            li.addClass("current");
            li.html("<a>" + inputtext+ "</a>");
            li.trigger("dblclick", {"type":typeVal});
            return ;
        }

        if (cndId == -999 || (inputtext.length > 0 && text != inputtext) ) { //新加节点 或者 有修改
            //加载数据之前 显示loading。。。效果
        /*    $.qicLoading({
                target: 'body',
                text: "响应操作中...",
                modal: true,
                width: 220,
                top: '280px',
                left: '450px',
                postion: "absolute",
                zIndex: 2000
            });*/
            $.ajax({
                url:renameRoleRoute.url({name:inputtext,id:cndId}),
                type:renameRoleRoute.method,
               // data:{"type":typeVal},
                dataType:"json",
                success:function (data) {
                    var liNode = input.parent();
                    if (data.op) {
                        liNode.html("<a>" + inputtext + "</a>");
                        if (data.id) { //新加节点
                            liNode.attr("id", "ut_" + data.id);

                        }
                        $.qicTips({message:data.msg, level:1, target:'.current', mleft:10, mtop:-30});

                        setTimeout(function() {
                                if(showAddRoleInput){
                                top.location.replace("/RoleInfos/roleList?tab=0&scroll=bottom&roleId="+data.id+"&iflag="+0);
                                }else{
                                    top.location.replace("/RoleInfos/roleList?tab=0&scroll=bottom&roleId="+data.id);
                                }
                            },
                            400)

                    } else {
                        if(data.sameName){ //是否重名错误
                            liNode.html("<a>" + inputtext + "</a>") ;
                            liNode.trigger("dblclick", {"type":typeVal});
                        }else{
                            liNode.html("<a>" + text + "</a>");
                        }
                        $.qicTips({message:data.msg, level:2, target:'.current', mleft:10, mtop:-30});
                    }
                    liNode.dblclick({"type":2}, utListItemdblclick2);
                }
            });
        } else { //还原回去
            var tdNode = input.parent();
            tdNode.html("<a>" + text + "</a>");
            tdNode.dblclick({"type":typeVal}, utListItemdblclick2);
        }

        li.addClass("current");
    });

    //把文本框加到元素中去
    li.append(input);

    //让文本框中的文字被高亮选中
    //需要将jquery的对象转换为dom对象
    var inputdom = input.get(0);
    inputdom.select();
    //6.清除元素上注册的点击事件
    li.unbind("dblclick");
}



