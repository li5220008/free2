/*股票池js操作文件*/
$(function(){
    //股票池分类点击
    /* $("#spcList h2, #spcList li a").click(function(){
     var $this = $(this);
     var code =  $this.attr("data-code"); //分类编码
     sortQuery(code);
     });*/



    //高级搜索
    $( "#dialog-link" ).click(function( event ) {
        $(".search_set_wrap").css("visibility", "visible");
        $(".search_set_wrap" ).dialog({
            autoOpen: false,
            width: 660,
            height: 430,
            resizable: false,
            overflow: 'inherit',
            modal: true,
            buttons: [
                {
                    title: "重置",
                    class: 'ser_reset',
                    click: function() {
                        //$( this ).dialog( "close" );
                        resetBtn_onclick();
                    }
                },
                {
                    title: "保存",
                    class: utListSize>0?'ser_keep':'ser_keep dark_keep',
                    click: function() {
                        //$( this ).dialog( "close" );
                        saveBtn_onclick();
                    }
                },
                {
                    title: "搜索",
                    class: 'ser_search',
                    click: function() {
                        $( this ).dialog( "close" );
                        searchBtn_onclick();
                    }
                }
            ]
        }).dialog( "open" );
    });

    //研究机构树
    window.tree = $("#reportOrgTree").ligerTree({data:orgData, checkbox: true, parentIcon: null, childIcon: null , getChecked:true, attribute:['id', 'url', 'data-orgId']});
    window.tree.collapseAll();
});

//------

/*tab 选项卡
 *2012-11-29 jinwei
 */

window.resizeTitleWidth('.tab_title', 350);//tab随客户端变宽
function resizeTitleWidth(element, trueWidth){
    var clientWidth = document.documentElement.clientWidth; //浏览器客户区宽度
    var wElement = $(element); //自适应宽度的元素
    wElement.css({"width": clientWidth- trueWidth});

    $(window).bind("resize", function(){
        wElement.css("width",document.documentElement.clientWidth-trueWidth);

        var liWidth = Math.abs(parseInt($(".tab_maximum li").eq(0).css("width")));
        if(liWidth<55 && liWidth>20){
            var clientWidth = clientTotalWidthFunc();
            var startLiLength =  $(".tab_maximum li").length;
            var extraWidth = startLiLength*7 + startLiLength*30;
            var cureentWidth = 78*startLiLength+ extraWidth;

            var averWidth = (clientWidth -extraWidth) / startLiLength;
            $(".tab_maximum li").css({"width": averWidth});
        }
    });

}

//每个选项卡同宽
function averageFunc(removeParam){
    var clientWidth = clientTotalWidthFunc();
    var startLiLength =  $(".tab_maximum li").length;
    var extraWidth = startLiLength*7 + startLiLength*30;
    var cureentWidth = 78*startLiLength+ extraWidth;
    var averWidth = (clientWidth -extraWidth) / startLiLength;

    if(removeParam == 'remove' && averWidth<=88) {
        $(".tab_maximum li").animate({"width": averWidth});
        return false;
    }

    if(averWidth<=88){
        $(".tab_maximum li").css({"width": averWidth});
    }


};

$(".small_h2").click(function(e){
    e.preventDefault();
    var display  = $(this).siblings(".siderbar_list").css("display");
    $(this).closest("dl").find(".siderbar_list").hide('fast');
    if(display == 'none'){
        $(this).siblings(".siderbar_list").stop(false, true).show('fast')
    }else{
        $(this).siblings(".siderbar_list").stop(false, true).hide('fast')
    }
});


//整个tab li的宽度之和
function tabTotalWidthFunc(){
    var liLength = $(".tab_maximum li").length;//li数目
    var liMarginWidth = 7 * (liLength-1);//li的margin总宽度
    var liWidth = $(".tab_maximum li")[liLength-1].offsetWidth; //新加的li宽度

    var arrLiWidth = []; //li的宽度数组
    var liTotalWidth = 10;//必须数值
    //遍历整个tab li的宽度
    $(".tab_maximum li").each(function(i){
        arrLiWidth.push(this.offsetWidth);
        liTotalWidth += parseInt(arrLiWidth[i]);
        return liTotalWidth;

    });

    var tabWidth = liMarginWidth + liTotalWidth;//总宽度
    return tabWidth;
}

//tab 的客户端宽度
function clientTotalWidthFunc(){
    return parseInt($(".tab_title")[0].offsetWidth);
}

$(function () {
    //loading subpage
    $(".stable_load").click(function (event) {
        //var idInde
        var thisId = $(this).attr("id");
        var arrId = [];
        //var liId = $(".sub_content").children("li").attr("id")
        $(".sub_content").children("li").each(function(){
            arrId.push($(this).attr('id'));
        })
        //arrId.push(liId);

        for(var k=0; k< arrId.length; k++){
            if(('showtab_'+thisId) == arrId[k]){
                //$(".tab_li li").eq(k).removeClass().css()
                $(".tab_li").children().removeClass().eq(k).addClass('display');
                $(".sub_content").children().removeClass().eq(k).addClass('display');
                return false;
            }
        }

        //最大选项数
        if($(".tab_maximum li").length >=16){
            $.qicTips({level:2, message:'已达最大选项数.', time: 2000})
            return false;
        }

        event.preventDefault();
        var dataValue = $(this).attr("data-value");
        var name = $(this).attr("name");
        var id = $(this).attr("id");
        $.ajax({
            url:basicInfoRoute.url({stockPoolCode:id}),
            type:'GET',
            data:'dataValue=' + dataValue + '',
            dataType:"html",
            cache:false,
            success:function (data) {
                var dataHtml = data;
                $(".tab_li").children("li").removeClass('display');
                $(".sub_content").children("li").removeClass('display')
                var liNode = "<li class='display'>" + name + "<img src='" + _gQic.ctx + "/public/images/stable_close.png' class='stable_close' entityid='" + id + "'></li>";
                $(".tab_li").append(liNode)
                $(".sub_content").append(dataHtml);
                //用户评级
                starChoose("select_star_" + id, "#options_star_" + id, "#reportDate_" + id, "#reportName_" + id);

                //详细表格高度自适应
                var tblScrollHeight = $("#showtab_" + id).find('.stable_txt')[0].offsetHeight;
                // var tblScrollHeight = $(".stable_txt", dataHtml)[0].scrollHeight;
                var appendHeight =  tblScrollHeight + 144;
                // console.log(tblScrollHeight, appendHeight);
                window.resizeHeight("#showtab_" + id +' .tbl_scroll_2', appendHeight);

                /*  2012-11-29
                 * tab 变化
                 */
                var liTotalWidth = tabTotalWidthFunc();//li 总宽度
                var clientTotalWidth = clientTotalWidthFunc();//li客户端宽度
                var currentLiLength = $(".tab_maximum li").length; //当前li数目
                var currentLiWidth = (157+7+30)*currentLiLength;//当前的宽度
                //li平均值
                var averageWidth = (clientTotalWidth -30*currentLiLength-7*currentLiLength)/currentLiLength;
                if(currentLiWidth>clientTotalWidth){
                    var moveWidth = liTotalWidth -clientTotalWidth; //要移动的宽度
                    //$(".tab_maximum").animate({left: -moveWidth});
                    $(".tab_maximum li").animate({width: averageWidth});
                    $(".control_btn_left").addClass("control_btn_left_2")
                }else{
                    window.averageFunc();
                }
               // console.log(combinQuotationInfo(thisId))
               var result =  qicScriptContext.subscriptionQuotation(combinQuotationInfo(thisId), "updateQuotationInfo");
               $('#resultId').val(result);
            },
            statusCode:{404:function () {
                new QicDialog({
                    message:"404错误,页面未找到",
                    title:"提示"
                }).error();
            }},
            error:function () {
                new QicDialog({
                    message:"请求错误",
                    title:"提示"
                }).error();
            }
        });

    });




    //删除subpage
    $(".stable_close").live('click', function (event) {
        event.stopPropagation(); //阻止事件冒泡
        var id = $(event.target).attr("entityid");
        reportDialogMap[id] = undefined; //删除研报摘要dialog

        //操作提示
        var target = event.target;

        //$.qicTips({target:target, message:'删除成功',level:1});


        var index = $(this).parent("li").index();
        $(this).parent("li").remove();//删除title
        $(".sub_content").children("li").eq(index).remove(); //删除内容

        //把焦点设到第一个(组合浏览)
        $(".tab_li li").removeClass("display").eq(0).addClass('display')
        $(".sub_content li").removeClass("display").eq(0).addClass('display')
        //$(".stable_content").eq(index).addClass('display')

        //tab 即时变化
        var liLength = $(".tab_maximum li").length;
        var liWidth = Math.abs(parseInt($(".tab_maximum li").eq(index).css("width")));
        var liTotalWidth = (liWidth+7+30)*liLength;
        var clientWidth = clientTotalWidthFunc();
        if(liWidth<108){
            window.averageFunc('remove'); //li平均值
        }
        var resultValue = $('#resultId').val()
        if(resultValue != ''){
            qicScriptContext.cancelSubscriptionQuotation(resultValue);
        }
    });

});

//basicInfo页面的评价（注意 与 list.html的评论是分开的）
$(".stable_comment").live('click', function(event){
    var _this = $(this);
    var id = _this.parent().parent().parent().parent().parent().attr("entityid");
    var target = event.target;
    if(_this.attr("iscomment") == "true") {
        $.qicTips({ target: target, level: 2, message: '已评论'});
        return;
    }
    if(commentDialogMap[id] == undefined){
        commentDialogMap[id] = $("#showtab_" + id).find(".comment_wrap").dialog({
            autoOpen: true,
            width: 250,
            height:170,
            modal: true,
            resizable: false,
            buttons: [
                {
                    title: "取消",
                    class: 'cancel',
                    click: function() {
                        $( this ).dialog( "close" );
                    }
                },
                {
                    title: "确定",
                    class: 'submit',
                    click: function() {
                        subPageSaveBtnOnclick(_this, id);
                        $(this).dialog("close")
                    }
                }
            ]
        });
    } else {
        commentDialogMap[id].dialog();
    }
});

//sub page save button click
function subPageSaveBtnOnclick(element, id) {
    var form = $("#spForm" + id);
    if(element.attr("iscomment") == "true"){
        $.qicTips({ target: '.submit', level: 2, message: '重复评论'});
        return;
    }
    $.ajax({
        url:saveRoute.url(),
        type:'post',
        dataType:"json",
        data:form.serialize(),
        success:function (data) {
            if(data.commented) {
                $.qicTips({ target: '.stable_comment', level: 1, message: '提交成功' });
                element.removeClass();
                element.attr("src","/public/images/un_comment.png");
                $('.btn_comment').each(function(){
                    if($(this).attr('data-collectid')==id){
                        $(this).removeClass("btn_comment").addClass("btn_uncomment");
                    }
                })

            } else {
                $.qicTips({ target: '.stable_comment', level: 2, message: '重复评论' });
            }
        }
    });
}

<!--点击研报摘要弹出页面JS开始 -->
$("#mytext3").live('click',function(event){
    var id = $(event.target).attr("entityid");
    var mainContainer = $("#showtab_" + id);
    if(reportDialogMap[id] == undefined) {
        $.get(yanBaoRoute.url(),
            {stockPoolCode: id},
            function (data) {
                mainContainer.find(".research_report").html(data);
                reportDialogMap[id] = mainContainer.find(".research_report").dialog({
                    autoopen: true,
                    modal: true,
                    resizable: false,
                    title:"研报摘要",
                    width:480,
                    height: 390
                });
            });
    } else {
        reportDialogMap[id].dialog("open");
    }
});
<!--点击研报摘要弹出页面JS结束-->

//list页面评价 注意 与 basicInfo.html的评论是分开的）
$(".btn_comment").live('click', function(event){
    var $this = $(this);
    spid = $this.attr("data-collectid");

    $(".comment_wrap_list").dialog({
        autoOpen:true,
        modal:true,
        resizable:false,
        width:230,
        height:150,
        buttons:[
            {
                title:"取消",
                class:'cancel',
                click:function () {
                    $(this).dialog("close");
                }
            },
            {
                title:"提交",
                class:'submit',
                click:function () {
                    listPageSaveBtnOnclick($this);
                    $(this).dialog("close")
                }
            }
        ]
    }).dialog("open")
});


//用户评级
starChoose("select_info_year1", "#options_year1", "#reportDate", "#reportName");
starChoose("select_info_year2", "#options_year2", "#reportDate2", "#reportName2");


function listPageSaveBtnOnclick(element){
    var  comment_flag = false;
    var form = $("#listForm");
    $.ajax({
        url:savecommentRoute.url(),
        type:'post',
        dataType:"json",
        data:form.serialize()+ "&spid=" + spid,
        success:function (data) {
                $.qicTips({target: element, level: 1, message: '评论成功'});
                element.removeClass("btn_comment").addClass("btn_uncomment");

        }
    });
}

//更新行情数据
function updateQuotationInfo(stockInfo){
    var data = eval('(' + stockInfo + ')');
    for(var i=0;i<data.length;i++){
        $('.tbl_common tr').each(function () {
            if($(this).children('td').eq(0).children('a').children('strong').html()==data[i].Code){
               // $(this).children('td').eq(1).html(stockInfo.name);
                $(this).children('td').eq(2).html("<strong style='vertical-align: middle;'>"+data[i].Price.toFixed(2)+"</strong>");
                if(data[i].Change>0){
                    $(this).children('td').eq(3).html("<strong style='vertical-align: middle; color: red'>"+data[i].Change.toFixed(2)+"</strong>");
                }else if(data[i].Change<0){
                    $(this).children('td').eq(3).html("<strong style='vertical-align: middle; color: green'>"+data[i].Change.toFixed(2)+"</strong>");

                }else{
                    $(this).children('td').eq(3).html("<strong style='vertical-align: middle;'>"+data[i].Change.toFixed(2)+"</strong>");

                }
                if(data[i].ChangeRate>0){
                    $(this).children('td').eq(4).html("<strong style='vertical-align: middle;color: red'>"+data[i].ChangeRate.toFixed(2)+"</strong>");

                }else if(data[i].ChangeRate<0){
                    $(this).children('td').eq(4).html("<strong style='vertical-align: middle; color: green'>"+data[i].ChangeRate.toFixed(2)+"</strong>");

                }else{
                    $(this).children('td').eq(4).html("<strong style='vertical-align: middle;'>"+data[i].ChangeRate.toFixed(2)+"</strong>");

                }
                $(this).children('td').eq(5).html("<strong style='vertical-align: middle;'>"+data[i].TotalVol.toFixed(2)+"</strong>");
                $(this).children('td').eq(6).html("<strong style='vertical-align: middle;'>"+data[i].TotalAmount.toFixed(2)+"</strong>");
                $(this).children('td').eq(7).html("<strong style='vertical-align: middle;'>"+data[i].Bid1.toFixed(2) + "/" + data[i].Ask1.toFixed(2)+"</strong>");
                $(this).children('td').eq(8).html("<strong style='vertical-align: middle;'>"+data[i].BidSize1.toFixed(2) + "万/" + data[i].AskSize1.toFixed(2)+"万</strong>");
                $(this).children('td').eq(9).html("<strong style='vertical-align: middle;'>"+data[i].Pre.toFixed(2) + "/" + data[i].Open.toFixed(2)+"</strong>");
                $(this).children('td').eq(10).html("<strong style='vertical-align: middle;'>"+data[i].High.toFixed(2) + "/" + data[i].Low.toFixed(2)+"</strong>");
            }
        });
    }
}
function StockInfo(scode,name,news,upDown,markup,turnover,AMOUNT,price,count,yt,hl){
    this.scode=scode;
    this.name=name;
    this.news=news;
    this.upDown=upDown;
    this.markup=markup;
    this.turnover=turnover;
    this.AMOUNT=AMOUNT;
    this.price=price;
    this.count=count;
    this.v=yt;
    this.hl=hl;
   // this.Ask1=Ask1;
}
//组装行情数据
function combinQuotationInfo(id){
    var type;
    var scode;
    var codeArray = [];
    $('#combindata_'+id+' tr').each(function () {
        scode = $(this).children('td').eq(0).children('a').children('strong').html()
        if( $(this).children('input').eq(0).val()=="SZSE"){
            type = 2;
        }else{
            type = 1;
        }
        //codeArray.push("{\"type\":"+type+",\"code:\""+scode+"\"}");
        codeArray.push({type: type, code: scode});
    });

    //return  jQuery.param(codeArray);
    return JSON.encode(codeArray);
}


var JSON = {
    useHasOwn: ({}.hasOwnProperty ? true: false),
    pad: function(n) {
        return n < 10 ? "0" + n: n;
    },
    m: {
        "\b": '\\b',
        "\t": '\\t',
        "\n": '\\n',
        "\f": '\\f',
        "\r": '\\r',
        '"': '\\"',
        "\\": '\\\\'
    },
    encodeString: function(s) {
        if (/["\\\x00-\x1f]/.test(s)) {
            return '"' + s.replace(/([\x00-\x1f\\"])/g,
                function(a, b) {
                    var c = a[b];
                    if (c) {
                        return c;
                    }
                    c = b.charCodeAt();
                    return "\\u00" + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
                }) + '"';
        }
        return '"' + s + '"';
    },
    encodeArray: function(o) {
        var a = ["["],b, i, l = o.length,v;
        for (i = 0; i < l; i += 1) {
            v = o[i];
            switch (typeof v) {
                case "undefined":
                case "function":
                case "unknown":
                    break;
                default:
                    if (b) {
                        a.push(',');
                    }
                    a.push(v === null ? "null": this.encode(v));
                    b = true;
            }
        }
        a.push("]");
        return a.join("");
    },
    encodeDate: function(o) {
        return '"' + o.getFullYear() + "-" + pad(o.getMonth() + 1) + "-" + pad(o.getDate()) + "T" + pad(o.getHours()) + ":" + pad(o.getMinutes()) + ":" + pad(o.getSeconds()) + '"';},
    encode: function(o) {
        if (typeof o == "undefined" || o === null) {
            return "null";
        } else if (o instanceof Array) {
            return this.encodeArray(o);
        } else if (o instanceof Date) {
            return this.encodeDate(o);
        } else if (typeof o == "string") {
            return this.encodeString(o);
        } else if (typeof o == "number") {
            return isFinite(o) ? String(o) : "null";
        } else if (typeof o == "boolean") {
            return String(o);
        } else {
            var self = this;
            var a = ["{"],b,i,v;
            for (i in o) {
                if (!this.useHasOwn || o.hasOwnProperty(i)) {
                    v = o[i];
                    switch (typeof v) {
                        case "undefined":
                        case "function":
                        case "unknown":
                            break;
                        default:
                            if (b) {
                                a.push(',');
                            }
                            a.push(self.encode(i), ":", v === null ? "null": self.encode(v));
                            b = true;
                    }
                }
            }
            a.push("}");
            return a.join("");
        }
    },
    decode: function(json) {
        return eval("(" + json + ')');
    }
};