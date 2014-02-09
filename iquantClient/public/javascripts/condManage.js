/*高级搜索条件管理js*/
$(function() {
    //条件管理,可编辑元素
    //window.editElement(".search_ul li", cndType); //可编辑元素
    $(".search_ul li").live("dblclick", {"type":cndType}, utListItemdblclick)

    //用户评级
    window.starChoose("select_starUp", "#options_starUp", "#starUp", "#starUp_dn");
    window.starChoose("select_starDown", "#options_starDown", "#starDown", "#starDown_dn");

   // $("#options_starDown li.star_0").trigger("click");
  //  $("#options_starUp li.star_5").trigger("click");
    //交易品种
    $("#cndTradeVariety li.trade_-999").trigger("click");
    //交易类型
    $("#options_tradeType li.tradeType_-999").trigger("click");

    $("#ut_ul_list > li").live("click", function () {
        $("#ut_ul_list > li.cndSelected").removeClass("cndSelected");
        var $li = $(this);
        $li.addClass("cndSelected");
        var cndId = $li.attr("id").substring("ut_".length);
        if (cndId == -999) { //新的节点
            return;
        }
        var cndJson = utMap[cndId];
       // console.log(cndJson);
        resetBtn_onclick();
        if (cndType == 2) { //研究机构树节点设置
            //先清空, 后选中
            clearSelTreeNode();
            var selArr = cndJson.recommendOrgs;
            if ($.isArray(selArr)) { //是数组
                $.each(selArr, function (i, n) {
                    if (("" + n ).length > 0) { //n要有值
                        var $dom = $("#reportOrgTree li[data-orgId='" + n + "']");
                        window.tree.selectNode($dom[0]);
                    }
                });
            }
        }
        //给form设置值
        for (var k in cndJson) {
           // alert(k);
            $("#" + k).val(cndJson[k]);
        }
        $("#options_starUp li.star_" + cndJson.starUp).trigger("click");
        $("#options_starDown li.star_" + cndJson.starDown).trigger("click");


        //交易类型
        $("#options_tradeType li.tradeType_" + cndJson.tradeType).trigger("click");
        var curSelectTradeType = $("#options_tradeType li.tradeType_" + cndJson.tradeType);
        $("#tradeTypeId").val(curSelectTradeType.attr("data-value"));
        $("#select_tradeType").text(curSelectTradeType.text());

        //交易品种
        $("#cndTradeVariety li.trade_" + cndJson.tradeVariety).trigger("click");
        var curSelectTradeVariety = $("#cndTradeVariety li.trade_" + cndJson.tradeVariety);
        $("#tradeVarietyId").val(curSelectTradeVariety.attr("data-value"));
        $("#select_tradeVariety").text(curSelectTradeVariety.text());

        //更新时间
        $("#options_update li.update_" + cndJson.reportUpdatePeriod).trigger("click");
        var curSelectupdate = $("#options_update li.update_" + cndJson.reportUpdatePeriod);
        $("#updateDateId").val(curSelectupdate.attr("data-value"));
        $("#select_update").text(curSelectupdate.text());

    });

    //删除搜索条件
    $("#delCndBtn").click(function () {
        var $li = $("#ut_ul_list > li.cndSelected");
        if ($li.size() == 0) {
            $.qicTips({message:'请先选择查询条件', level:2, target:this, mleft:0, mtop:-10});
            return;
        }
        var cndId = $li.attr("id").substring("ut_".length);
        if (cndId == -999) {
            $li.remove();
            resetBtn_onclick();
            $("#ut_ul_list > li:first").trigger("click");
            return;
        }
        new QicDialog({
            message:"是否确认删除",
            title:"提示",
            confirm :function(){
                $.ajax({
                    url:delCondRoute.url({id:cndId}),
                    type:delCondRoute.method,
                    dataType:"json",
                    success:function (data) {
                        if (data.op) {
                            $li.remove();
                            resetBtn_onclick();
                            $.qicTips({message:data.msg, level:1, target:'#ut_ul_list', mleft:0, mtop:-60});
                            //删除成功后, 又让第一个被选中
                            $("#ut_ul_list > li:first").trigger("click");
                        } else {
                            $.qicTips({message:data.msg, level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
                        }
                    }
                });
                if($("#ut_ul_list li").size()==1){
                    var element = $(".ser_keep")
                    //element.removeClass("ser_keep");
                    element.addClass("dark_keep");
                }
            }
        }).confirm();

    });

    //增加搜索条件
    $("#addCndBtn").click(function () {
        //说明有重名的,要处理重名
        var $input2 = $("#ut_ul_list > li > input");
        if($input2.size() > 0){
            //console.log("还有input啊...");
            new QicDialog({
                message:"请先处理重名问题",
                title:"提示"
            }).warn();
            $('#ut_ul_list').scrollTop($input2.get(0).offsetHeight); //滚动到当前可见的位置
            $input2.get(0).focus();
            return ;
        }
        if ($("#ut_ul_list > li").size() >= 20) {
            new QicDialog({
                message:"自定义搜索上限为20个",
                title:"提示"
            }).warn();
            return;
        }

        $("#ut_ul_list > li.cndSelected").removeClass("cndSelected");//先去掉选中的

        var newNameTmp = "新搜索条件";
        var newName = "";
        var tmpNameIntArr = []; //用于保存 newNameTmp 开头的接下来的整数值
        $("#ut_ul_list > li").each(function () {
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
            tmpNameIntArr.sort(function(o1, o2){
                return o1 - o2;
            });
            newName = newNameTmp + (tmpNameIntArr[tmpNameIntArr.length - 1] + 1);
        } else {
            newName = newNameTmp + "1";
        }

        var $li = $("<li id='ut_-999'>" + newName + "</li>");
        $li.addClass("cndSelected");
        $li.appendTo($("#ut_ul_list"));
        $('#ut_ul_list').scrollTop($('#ut_ul_list')[0].scrollHeight); //滚动到底端
        $li.trigger("dblclick");

        //初始化交易类型和品种
        resfun();
    });


    //触发第一个被选中
    if ($("#ut_ul_list > li").size() > 0) {
        $("#ut_ul_list > li:first").trigger("click");
    } else {
        //把星级也重置
        $("#options_starDown li.star_0").trigger("click");
        $("#options_starUp li.star_5").trigger("click");
        $("#cndTradeVariety li.trade_-999").trigger("click");
        $("#options_tradeType li.tradeType_-999").trigger("click");
    }


});

//初始化查询条件
function resfun(){
    $("#tradeTypeId").val("-999");
    $("#select_tradeType").text("全部");

    $("#tradeVarietyId").val("-999");
    $("#select_tradeVariety").text("全部");

    $("#updateDateId").val("-999");
    $("#select_update").text("全部");
}

//重置按钮 点击事件处理
function resetBtn_onclick(){
    if(cndType == 2){
        resfun();
        clearSelTreeNode();
    }
    $("#cndForm")[0].reset();
    //把星级也重置
    $("#options_starDown li.star_0").trigger("click");
    $("#options_starUp li.star_5").trigger("click");

    //初始化条件
    resfun();
}

//树的操作
function treeVal(){
    var selArr = window.tree.getChecked();
    var selOrgIds = [];
    $.each(selArr, function(i, n){ //这里操作
        var itemData = n.data["data-orgId"];
        selOrgIds.push(itemData);
    });
    $("#recommendOrgs").val(selOrgIds.join(','));
}

//把所选的树的节点清除掉
function clearSelTreeNode(){
    var parm = function (data){
        return false;
    };
    window.tree.selectNode(parm);

    //清空掉那些半选中的节点
    $("div.l-checkbox-incomplete").removeClass("l-checkbox-incomplete");
}

//保存按钮点击事件处理. 这里也就是修改了
function saveBtn_onclick(){
    if(cndType == 2){ //股票池
        treeVal();
    }
    var $form = $("#cndForm");
    var $li = $("#ut_ul_list > li.cndSelected");
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

//搜索按钮点击
function searchBtn_onclick(){
    if(cndType == 2){ //股票池
        treeVal();
    }

    if(downIsLargeUp("#yieldDown", "#yieldUp")){
        new QicDialog({
            message:"收益率 区间选择有问题",
            title:"提示"
        }).warn();
        $("#yieldDown").focus();
        return false;
    }

    if(downIsLargeUp("#profitRatioDown", "#profitRatioUp")){
        new QicDialog({
            message:"获胜率 区间选择有问题",
            title:"提示"
        }).warn();
        $("#profitRatioDown").focus();
        return false;
    }

    if(downIsLargeUp("#starDown", "#starUp")){
        new QicDialog({
            message:"用户评级 区间选择有问题",
            title:"提示"
        }).warn();
        return false;
    }

    //处理股票池
    if (downIsLargeUp("#yearYieldDown", "#yearYieldUp")) {
        new QicDialog({
            message:"年化收益率 区间选择有问题",
            title:"提示"
        }).warn();
        $("#yearYieldDown").focus();
        return false;
    }

    if (downIsLargeUp("#sharpRateDown", "#sharpRateUp")) {
        new QicDialog({
            message:"Sharp比率 区间选择有问题",
            title:"提示"
        }).warn();
        $("#sharpRateDown").focus();
        return false;
    }

    $("#cndForm")[0].submit();
    return true;
}

//搜索条件列表项双击事件处理
function utListItemdblclick(e) {
    var li = $(this);
    if(! li.hasClass("cndSelected")){ //如果当前不选中的话, 直接跳过
        return;
    }

    var typeVal = e.data.type;
    var li = $(this);
    li.removeClass("cndSelected");
    var cndId = li.attr("id").substring("ut_".length); //模板id
    var text = li.text();
    li.html("");
    var input = $("<input type='text'>");

    input.attr("value", text);
    input.blur(function (event) {
        li.addClass("cndSelected");
        var inputtext = $.trim(input.val());
        if(inputtext.length == 0){
            $.qicTips({message:"名称不能为空", level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
            input.focus();
            return ;
        }else if(inputtext.length > 15){
            $.qicTips({message:"超过15字符长度", level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
            input.focus();
            return ;
        }

        var sameName = false;
        $("#ut_ul_list > li").each(function(i){
            var $eachThis = $(this);
            if($eachThis == li){ //当前节点

            }else if(inputtext == $eachThis.html()){ //说明重名
                sameName = true;
                return false;
            }

            return true;
        });
        if(sameName){
            $.qicTips({message:"名称已存在", level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
            li.addClass("cndSelected");
            li.html(inputtext);
            li.trigger("dblclick", {"type":typeVal});
            return ;
        }

        if (cndId == -999 || (inputtext.length > 0 && text != inputtext) ) { //新加节点 或者 有修改
            $.ajax({
                url:renameCondRoute.url({id:cndId, name:inputtext}),
                type:renameCondRoute.method,
                data:{"type":typeVal},
                dataType:"json",
                success:function (data) {
                    var liNode = input.parent();
                    if (data.op) {
                        liNode.html(inputtext);
                        if (data.id) { //新加节点
                            liNode.attr("id", "ut_" + data.id);
                            utMap[data.id] = data.utInfo;
                            //新加的,则要清空右边的选项
                            resetBtn_onclick();
                        }
                        $.qicTips({message:data.msg, level:1, target:'#ut_ul_list', mleft:0, mtop:-60});
                        //$(".ser_keep").toggleClass("ser_keep");

                        var event = $(".ser_keep");
                        if($("#ut_ul_list li").size()==1){
                            event .removeClass("dark_keep");
                            event.addClass("ser_keep");
                        }
                    } else {
                        if(data.sameName){ //是否重名错误
                            liNode.html(inputtext);
                            liNode.trigger("dblclick", {"type":typeVal});
                        }else{
                            liNode.html(text);
                        }
                        $.qicTips({message:data.msg, level:2, target:'#ut_ul_list', mleft:0, mtop:-60});
                    }
                    liNode.dblclick({"type":typeVal}, utListItemdblclick);
                }
            });
        } else { //还原回去
            var tdNode = input.parent();
            tdNode.html(text);
            tdNode.dblclick({"type":typeVal}, utListItemdblclick);
        }

        li.addClass("cndSelected");
    });

    //把文本框加到元素中去
    li.append(input);

    //让文本狂中的文字被高亮选中
    //需要将jquery的对象转换为dom对象
    var inputdom = input.get(0);
    inputdom.select();
    //inputdom.focus();
    //6.清除元素上注册的点击事件
    //li.unbind("dblclick");
}
