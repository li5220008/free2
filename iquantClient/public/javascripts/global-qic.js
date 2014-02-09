/*
 *func: QIC
 *author:jinwei
 *email: awayInner@gmail.com
 *date: 2012-11-05
 */
//屏蔽右键
//document.oncontextmenu=function(e){return false;}
//document.onselectstart=function(){return false;}
//屏蔽F5 F12 F11键
document.onkeydown =function(e){
    if(e.keyCode == 27){
        //传递ESC键事件给终端
        qicScriptContext.onBrowerKeyDown(e.keyCode);
        return false;
    }
    if(e.keyCode == 116 || e.keyCode == 122 || e.keyCode == 123){
       // return false;
    }
}
//禁止策略大图拖动，而带来的造中状态
$(document).ready(function(){
    $(".tbl_wrap").bind("dragstart", function(){
        return false;
    });
});

;(function ($) {
    jQuery.extend({
        /**
         * obj的属性有的: message 信息(文字).
         * level 级别(1. 正确的 2.错误的, 3. 信息)
         * target  jquery的选择器(字符).
         * time 时间. 默认2000.
         * mleft  提示框离你的target对象左边多少距离
         * mtop   提示框离你的target对象top多少距离
         */
        qicTips:function (options) {//操作提示方法
            var defaults = {
                message:"操作成功",
                level:1,
                target:'body',
                time:2000,
                mleft:-60,
                mtop: -30
            };

            var options = $.extend(defaults, options); //扩展属性

            var imgSrc1 = _gQic.ctx + "/public/images/operate_sprites_1.png";
            var imgSrc2 = _gQic.ctx + "/public/images/operate_sprites_2.png";
            var imgSrc3 = _gQic.ctx + "/public/images/operate_sprites_3.png";
            if ($(".operate_tips").length == 0) {
                var html = '<div class="operate_tips" style="position:absolute;left:500px; z-index:9000;top:100px;  color:#65a5df; height:27px;"><div class="operate_tips_1" style="height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url('
                    + imgSrc1
                    + ') no-repeat 0px -0px; width:71px;">&nbsp;</div><div class="operate_tips_2" style="padding-left:1px;height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url('
                    + imgSrc2
                    + ') repeat-x 0px -0px; z-index:49;position:relative; left:-46px;text-align:center; min-width:42px;max-width:300px;overflow:hidden;white-space:nowrap;">操作成功</div><div class="operate_tips_3" style="height:29px;line-height:29px; float:left; position:relative; z-index:50; padding-bottom:1px;background:url('
                    + imgSrc3
                    + ') no-repeat 0px -0px;position:relative; left:-46px;width:6px;">&nbsp;</div></div>';

                $("body").append(html);
            }

            var operTips = $(".operate_tips");
            //取得包含框的子元素(注：children()只考虑子元素而不考虑所有后代元素
            var operTipsChildren = operTips.children("div");
            var operTipsMidTxt = $(".operate_tips_2");
            var time = options.time; //停留时间
            var clientX = 0;
            var clientY = 0;
            var $body = $("body");
            var $target = $(options.target);
            if (options.target == 'body') {
                clientX = $body.clientWidth / 2;
            } else {
                clientX = $target.offset().left + (options.mleft);
            }
            if (options.target == 'body') {
                clientY = $body[0].clientHeight / 2;
            } else {
                clientY = $target.offset().top + (options.mtop);
            }

            switch (options.level) {
                case 1:
                    operTipsChildren.css({"background-position":'0px -0px'});
                    operTipsMidTxt.html(options.message);
                    break;
                case 2:
                    operTipsChildren.css({"background-position":'0px -31px', "color":'#db1f10'});
                    operTipsMidTxt.html(options.message);
                    break;
                case 3:
                    operTipsChildren.css({"background-position":'0px -61px'});
                    operTipsMidTxt.html(options.message);
                    break;
                default:
                    break
            }

            operTips.css({
                "position":"absolute",
                "top":clientY,
                "left":clientX,
                "z-index":90000,
                'display':'none',
                "background-position":'0px 0px',
                'color':'#65a5df'
            });
            operTips.stop(false, true).show('fast');
            window.setTimeout(function () {
                operTips.stop(false, true).hide('fast');
            }, time);
        }
    });
})(jQuery);



var EventUtil = {
    addHandler: function(){ //绑定事件

    },
    getElement: function(){

    }
}


/*1.0
 *原生js tab切换
 *func: tabMenu("menuTab", "subCont", "display");
 *参数tabId：鼠标点击要切换的ul的id, subCont：要切换的内容ul的id, display:焦点class
 *一个页面多次调用
 *author: jinwei
 *email: awayInner@gmail.com
 *document.write("星期" + ['一','二','三','四','五','六','日'][new Date().getDay()]);
 */

function _id(id){
    return document.getElementById(id);
}

function tabMenu(tabId, contId, display){
    var subCont = _gid(contId); //内容父id
    var subContLi = subCont.children;
    //var subContLi = subCont.getElementsByTagName("li");
    var mentTab = _gid(tabId); //切换菜单父id
    var tabLi = mentTab.children;
    //var tabLi = mentTab.getElementsByTagName("li");
    var arr=[];



    //获取id的引用
    function _gid(id){
        return document.getElementById(id);
    }

    //遍历tab的 li
    for(var i=0; i<tabLi.length; i++){
        arr.push(tabLi[i])
        tabLi[i].onclick = function(){
            clearClass(tabLi); //清除菜单tab下的li class
            clearClass(subContLi); //清除内容下的li class
            this.className = display;

            //alert($("li").index(this)); //1.0 jQ 获取当前索引值
            //var curIndex = arr.index(this); //2.0 原生js获取当前索引值

            for(var j=0,len=arr.length; j<len; j++){
                if(arr[j] == this){
                    curIndex = j;
                    break;
                }
            }
            subContLi[curIndex].className = display;

        };

    }

    //清除所有同胞节点的class
    function clearClass(curenLi){
        for(var j=0; j<curenLi.length; j++){
            curenLi[j].className = '';
        }
    }

}


/*2.0
 *func: 高级搜索，策略对比
 */
function moreSearch(){
    // 2.1搜索设置

    $( "#dialog-link" ).click(function( event ) {
        $(".search_set_wrap").css("visibility", "visible");
        $(".search_set_wrap" ).dialog({
            autoOpen: false,
            width: 660,
            height:430,
            resizable: false,
            overflow: 'inherit',
            modal: true,
            buttons: [
                {
                    title: "重置",
                    class: 'ser_reset',
                    click: function() {
                        resetBtn_onclick();
                    }
                },
                {
                    title: "保存",
                    class: utListSize>0?'ser_keep':'ser_keep dark_keep',
                    click: function() {
                           saveBtn_onclick();
                        //$( this ).dialog( "close" );
                    }
                },

                {
                    title: "搜索",
                    class: 'ser_search',
                    click: function(){
                        var result = searchBtn_onclick();
                        if(result){
                            $( this ).dialog( "close" );
                        }
                    }
                }

            ]
        });

        $(".search_set_wrap" ).dialog( "open" );
        event.preventDefault();
    });


    // 2.2.1开始对比
    $(".n_contrast").click(function(event){
        var dataArr = [];//对比项
        var dataArr_1 = [];
        $(".constract_check").each(function(){
            dataArr.push($(this).find(".constract ").attr("data-compare-check"));
        });
        $(":checkbox[id^='stra_'][checked]").each(function(){
            dataArr.push(parseInt($(this).val()));
            dataArr_1.push(parseInt($(this).val()));
        });
        if((dataArr.length <1 )){
             $.qicTips({
             target: '.n_contrast',
             level: 2,
             message: '请选择策略',
             mtop: 0,
             mleft:-60
             });
           //alert("请选择策略");
            return false;
        }
        if((dataArr.length > 3 )){
            $.qicTips({
              target: '.n_contrast',
              level: 2,
              message: '对比策略不能大于3个',
              mtop: 0,
              mleft:-60
             });
            return false;
        }
        if(dataArr_1.length<0){
            $.qicLoading({
                target: '.n_contrast',
                text: "努力加载中...",
                modal: false,
                width: 220,
                top: '800%',
                left: '-130%',
                postion: "absolute",
                zIndex: 2000
            });
        }else{
            $.qicLoading({
                target: '.n_contrast',
                text: "努力加载中...",
                modal: false,
                width: 220,
                top: '800%',
                left: '-30%',
                postion: "absolute",
                zIndex: 2000
            });
        }

        $(".stategy_constact").css("visibility", "visible");
        $.get(strategyContrastRoute.url(),
            {idArray:dataArr},
            function (data) {
                $(".stategy_constact").html(data);
                $(".stategy_constact").dialog("open");
            });

        event.preventDefault();
    });


    $(".stategy_constact").dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        width:780
    })

    // 2.2.2要对比的项
    $(".constract_common").click(function(event){//2.3事件委托
        var className = event.target.className; //目标元素class
        var _this = this;

        if( className == 'constract'){//添加对比
            var dataCompare = $(event.target).attr("data-compare");
            //console.log($.inArray(dataCompare, compareArr))
            $(event.target).attr("data-compare-check", dataCompare)
            strtegyCompare(_this);

        }else if(className=="constract constract_2"){//取消对比
            var dataCompare = $(event.target).attr("data-compare");
            $(event.target).removeAttr("data-compare-check")
            strtegyCompare(_this);
        }



        //策略对比 背景切换
        function strtegyCompare(_this){
            $(_this).toggleClass("constract_check")
            $(_this).find(".constract").toggleClass("constract_2")
        }


        var target= event.target;
        // 添加收藏和取消收藏
        if(className == 'add_collect'){
            //添加收藏
            var element = $(this);
            var id = $(event.target).attr("data-strategyid");
            $.get("/userStrategycollects/addstrategycollect", {sid: id}, function(data){

                if(data.isSuccess){
                    $.qicTips({
                        target: target,
                        level: 1,
                        message: '收藏成功'
                    });
                    var text = $(event.target).parent().siblings("li").find('.collect_num').text();
                    $(event.target).parent().siblings("li").find('.collect_num').text(parseInt(text) +1)
                    if($("#fav_count") != undefined){
                        $("#fav_count").html(parseInt($("#fav_count").html()) + 1);
                    }
                    element.find(".add_collect").toggleClass("add_collect_2")
                }else{
                    $.qicTips({
                        target: target,
                        level: 2,
                        message: '收藏失败'
                    });
                }
                //载入成功时回调函数
            }, "json");
            //$(this).find(".add_collect").toggleClass("add_collect_2")

        }else if( className == 'add_collect add_collect_2'){
            //取消收藏
            var element = $(this);
            var id = $(event.target).attr("data-strategyid");
            $.get("/userStrategycollects/deletestrategycollect", {sid: id}, function(data){
                if(data.isSuccess){
                    $.qicTips({
                        target: target,
                        level: 1,
                        message: '取消成功'
                    });
                    var text = $(event.target).parent().siblings("li").find('.collect_num').text();
                    $(event.target).parent().siblings("li").find('.collect_num').text(parseInt(text) -1)
                    if($("#fav_count") != undefined){
                        $("#fav_count").html(parseInt($("#fav_count").html()) - 1);
                    }
                    element.find(".add_collect").toggleClass("add_collect_2")
                }else{
                    $.qicTips({
                        target: target,
                        level: 2,
                        message: '取消失败'
                    });
                }
                //载入成功时回调函数
            }, "json");

        }

    });


    $(".for_center").click(function(event){//2.3事件委托
        var className = event.target.className; //目标元素class
        var _this = this;
        var target= event.target;
        // 添加收藏和取消收藏
        if(className == 'list_collect'){
            //添加收藏
            var element = $(this);
            var id = $(event.target).attr("data-strategyid");
            console.log(id);
            $.get("/userStrategycollects/addstrategycollect", {sid: id}, function(data){
                if(data.isSuccess){
                    $.qicTips({
                        target: target,
                        level: 1,
                        message: '收藏成功'
                    });
                    element.find(".list_collect").removeClass("list_collect").addClass("list_collect_2");
                }else{
                    $.qicTips({
                        target: target,
                        level: 2,
                        message: '收藏失败'
                    });
                }
                //载入成功时回调函数
            }, "json");
            //$(this).find(".add_collect").toggleClass("add_collect_2")

        }else if( className == 'list_collect_2'){
            //取消收藏
            var element = $(this);
            var id = $(event.target).attr("data-strategyid");
            $.get("/userStrategycollects/deletestrategycollect", {sid: id}, function(data){
                if(data.isSuccess){
                    $.qicTips({
                        target: target,
                        level: 1,
                        message: '取消成功'
                    });
                    element.find(".list_collect_2").removeClass("list_collect_2").addClass("list_collect");
                }else{
                    $.qicTips({
                        target: target,
                        level: 2,
                        message: '取消失败'
                    });
                }
                //载入成功时回调函数
            }, "json");

        }

    });

}


/*3.0 UI事件-resize事件
 *Func: 跟随浏览器客户端高度，元素自适应高度
 *For Example: resizeHeight('.right_2_wrap', 475);
 *params: 参素element是元素，trueHeight是已确定元素的高度之和
 */
function resizeHeight(element, trueHeight){
    var clientHeight = document.documentElement.clientHeight; //浏览器客户区高度
    var hElement = $(element); //自适应高度的元素
    hElement.css({"height": clientHeight- trueHeight});
    $(window).bind("resize", function(){
        hElement.css("height",document.documentElement.clientHeight-trueHeight);
    });
}

/*3.1
 *Func: 跟随浏览器客户端宽度，元素自适应宽度
 *For Example: resizeWidth('.right_2_wrap', 475);
 *params: 参素element是元素，trueHeight是已确定元素的宽度之和
 */
function resizeWidth(element, trueWidth){
    var clientWidth = document.documentElement.clientWidth; //浏览器客户区宽度
    var wElement = $(element); //自适应宽度的元素
    wElement.css({"width": clientWidth- trueWidth});

    $(window).bind("resize", function(){
        wElement.css("width",document.documentElement.clientWidth-trueWidth);
    });

}

//4.0 我要评价
function myComment(){
    //评价
    $(".btn_comment,.stable_comment").click(function(event){
        $(".commment_wrap").dialog({
            autoOpen: false,
            width: 250,
            height:170,
            modal: false,
            resizable: false,
            buttons: [
                {
                    title: "取消",
                    class: 'ser_keep',
                    click: function() {
                        $( this ).dialog( "close" );
                    }
                },
                {
                    title: "确定",
                    class: 'ser_search',
                    click: function() {
                        $( this ).dialog( "close" );
                    }
                }
            ]
        }).dialog("open");
    });
}

/*
 *5.0 可编辑元素
 * 这里的type: 1. 策略超市, 2. 股票池
 */
function editElement(element, typeVal){
    var tds = $(element);
    //给元素节点增加点击事件
    //tds.dblclick(tdclick);
    tds.live("dblclick", {"type":typeVal}, utListItemdblclick); //这个函数定义在 condManage.js里了
}

/*6.0
 *侧边栏折叠
 */
function spSiderbar(){
    var folder=$(".folder"),
        flag = true,
        leftWrap = $(".sp_left_wrap"),
        spLeft = $(".sp_left"),
        folderShow = $(".folder_tips_hide"),
        folderList = $(".sp_content_m h3")//栏目;

    //侧边栏折叠
    folder.click(function(){
        if(flag == true){
            leftWrap.slideToggle('fast');
            spLeft.css("width", 8);
            folder.css({"left:": 0});
            folderShow.css({"background-position": '-9px 0'});
            window.resizeWidth('.sub_content', 24);
        }else{
            leftWrap.slideToggle('fast');
            spLeft.css("width", 235);
            //folder.css({"left:": 0});
            folderShow.css({"background-position": '0px 0'});
            window.resizeWidth('.sub_content', 270);
        }
        flag = !flag;
    });

    //栏目折叠
    folderList.click(function(){
        //console.log($(this))
        $(this).siblings(".siderbar_list").toggle();
        $(this).find("span").toggleClass("sp_tip");
    });
}

/* 8.0
 *用户评级， 仿下拉框通用函数()
 * clickWrap=当前点击ID, contentUlId=当前要显示隐藏的选项,hValue设置隐藏域data-value值， hName设置隐藏域data-name值
 *例:starChoose("select_info_year2", "#options_year2", "#reportDate2", "#reportName2");
 *
 */
function starChoose(clickWrap, contentUlId, hValue, hName) {
    $("body").click(function (event) {
        var targetName = $(event.target).attr("id");
        var parentName = $(event.target).parent().attr("id");
        var optionsContent = $(contentUlId);
        var hiddeValue = $(hValue);
        var hiddeName = $(hName);
        if (targetName == clickWrap || parentName == clickWrap) {
            $(event.target).parent().parent().find(".sel_91_option").slideToggle('fast');
        } else {
            $(contentUlId + " li").click(function () {
                var starClass = $(this).attr("class");
                //console.log(starClass);
                var changeStar = $(this).parent().siblings('.sel_84_dialog').find("span");
                var dataValue = $(this).text();
                var dataName = $(this).attr("data-name");
                //changeStar.attr("class", 'star_3')
                switch (starClass) {
                    case 'star_5':
                        changeStar.attr("class", 'star_5');
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                    case 'star_4':
                        changeStar.attr("class", 'star_4');
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                    case 'star_3':
                        changeStar.attr("class", 'star_3')
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                    case 'star_2':
                        changeStar.attr("class", 'star_2')
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                    case 'star_1':
                        changeStar.attr("class", 'star_1')
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                    case 'star_0':
                        changeStar.attr("class", 'star_0')
                        hiddeValue.attr("data-value", dataValue).val(dataValue);
                        hiddeName.attr("data-name", dataName).val(dataName);
                        break;
                }
            });
            optionsContent.slideUp('fast');
        }
    });
}
/* 9.0
 *下拉框通用函数()
 * clickid=当前点击ID, downShow=当前要显示隐藏的选项, reportDate设置隐藏域reportDate值， reportName设置隐藏域reportName值
 *例:downBox('select_info_year1', '#options_year1', 'reportDate', 'reportName');

 *
 */
function downBox(clickId, downShow, hiddenValueId, hiddenNameId){
    $('body').click(function(event){
        var cID = '#' + clickId; //给ID加一个#,以便JQ选中
        var dList = downShow + ' '+'li'; //点击选项列表

        if(event.target.id == clickId){
            //控制再一次点击 当前显示项关闭
            $(downShow).stop(false,true).slideUp("fast");
            $(""+downShow+":hidden").stop(false,true).slideToggle("fast");

            $(dList).click(function(){
                $(cID).text(''); //清空点击ID的值
                var txt = $(this).text();//获得当前点击列表项的值

                var hiddenValue = $(this).attr("data-value");


                $("#reportDate").val(hiddenValue)

                if($(this).is(".open_selected")){
                    $(cID).html(txt).css("color","#7D7D7D"); //设置选择的值

                    $("#" + hiddenValueId).val(hiddenValue);
                    $("#" + hiddenNameId).val(txt);

                }else{
                    $(cID).html(txt).css("color","#000"); //设置选择的值

                    $("#" + hiddenValueId).val(hiddenValue);
                    $("#" + hiddenNameId).val(txt);
                    //$('#options_certificate').css("display", "none");
                    //eventstopPropagation();
                }
            });

        }else{
            //downShow.slideUp();
            $(downShow).slideUp("fast").hide(); //隐藏下拉框
        }

    });
}

/*
 *func: QIC
 *author:jinwei
 *email: awayInner@gmail.com
 *date: 2012-11-05
 */

//正在加载中plugin
;(function($){


    $.qicLoading = function(options){

        var param = $.extend({
            text: "努力加载中...", //文字
            position: "absolute", //定位
            target: 'body', //加载目标
            top: '40%',
            left: '40%',
            modal: false, //遮罩背景
            width: 220,
            zIndex: 2000, //z-index顺序
            remove: false
        }, options || {});

        if(param.remove){
            $(".loading, .bg").remove();
            return false;
        }

        //正在加载中图片
        var loadingImg = '/public/images/loading_new.gif';



        //是否需要遮罩背景
        if(param.modal){
            var scrollWidth = $("body")[0].scrollWidth,
                socrllHeight = $("body")[0].scrollHeight;

            $(".bg").remove();

            $("body").prepend("<div class='bg' style='background-color:#000; opacity:0.5;position:absolute; left:0; top:0; z-index:"
                + param.zIndex + "; width:"
                + scrollWidth +"px; height:"
                + socrllHeight +"px;'></div>");
        }


        $(".loading").remove();

        $(param.target).prepend("<div class='loading' style='text-indent:50px;display: block; border:1px solid #980a0a; color:#000000; font-size:14px; font-weight:bold; line-height:30px; line-height:30px;  box-shadow:0 0 1px #fff;  height:30px; z-index:2"
            + (param.zIndex+1)+";width:"
            + param.width + "px; left:"
            + param.left + "; top:"
            + param.top +";background:url("
            + loadingImg +") no-repeat 20px 3px #fff; position:"
            + param.position + ";'>"
            + param.text + "</div>");


    };

})(jQuery);

//plugin2.0 插件全选or全不选 给jQ对象扩展方法
;(function($){
    $.fn.extend({
        check: function(){
            return this.each(function(){
                this.checked = true;
            });
        },
        uncheck: function(){
            return this.each(function(){
                this.checked = false;
            });
        }

    });

})(jQuery);


/*
 *QIC 弹出文字提示
 	只需要给提示的元素上加上class="tool_tips"
 */
function qicTips(){
    $(".tool_tips").mouseover(function(e){
        $("#tooltip").remove();
        this.myTitle = this.title,
            this.styleCss = 'position:absolute;word-wrap:break-word; border:1px solid #97c3e8; line-height:24px; background:#fff;padding:1px;color:#333;width:200px; padding:10px; overflow:auto; max-height:250px; overflow:auto; top:50px; left:300px; z-index:1000;display:none;',
            this.html = "<div id='tooltip' class='tool_tips_2' style='"+this.styleCss+"'>"+ $(this).html() +"</div>",
            this.x = -220,
            this.y = 0;

        $('body').append(this.html);

        if($.trim($(this).html()).length < 20){
             return;
        }
        var scrollHigh = $("body")[0].scrollHeight;
        var curHigh = e.pageY;

        var scrollHeight = document.body.clientHeight;
        var scrollTop = document.body.scrollTop;
        var clientHalf = document.body.clientHeight /2;
        var elementHigh = $(".tool_tips_2").height();

        //当前元素位置低于滚动条的位置
      if($(this).offset().top - scrollTop -41 >=clientHalf ){
           //弹出内容的高度
          this.y = -elementHigh;
      } else{
          this.y = 0;
      }

        $("#tooltip").css({
            top: ($(this).offset().top + this.y),
            left: ($(this).offset().left + this.x)
        }).slideDown(50);

    })
    //移除提示框
    $("body").mouseover(function(e){
        var id = e.target.id
        var className = e.target.className;

        classNames = className.split(" ")
        for(var i=0; i<classNames.length; i++){
            if(classNames[i] == 'tool_tips' || classNames[i] == 'tool_tips_2'){
                return;
            }
        }
        $("#tooltip").remove();
    });
}