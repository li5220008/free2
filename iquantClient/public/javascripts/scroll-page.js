/**
 * Created with IntelliJ IDEA.
 * User: liujl
 * Date: 13-1-18
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
function ScrollPage(config){
    this.identifyingId = new Date().valueOf();
    this.loadingDivId = "default_loading_" + this.identifyingId;
    this.loadFinishDivId = "default_finish_loading_" + this.identifyingId;
    this.loadErrorDivId = "error_loading_" + this.identifyingId;
    this.config = config;
    this.defaultLoadingTemplate = "<div id='"+this.loadingDivId +"'  class='p_loading'><span >正在加载，请稍后...</span></div>";
    this.defaultFinishLoadingTemplate = "<div id='"+this.loadFinishDivId+"'  class='p_loading'><span >没有更多了...</span></div>";
    this.defaultErrorLoadingTemplate = "<div id='"+this.loadErrorDivId +"'  class='p_loading'><span >加载出错...</span></div>";
    this.scrollTarget = config.scrollTarget;
    this.hasLoad = true;
    this.hasNext = true;
    this.pageNo  = config.pageNo ? config.pageNo : 1;
    this.data = !config.data ? {"pageNo": this.pageNo} : config.data;
    this.dataType = config.dataType ? config.dataType : "html";
    this.url = config.url ? config.url : config.getUrl;
    this.type = config.type ? config.type : "get";
    this.appendTarget = config.appendTarget;
    this.responseData  = "";
    this.responseHandler = "";
    this.httpCode = 200;
    this.hasError = false;
    this.loadingTarget = config.loadingTarget ? config.loadingTarget : this.defaultLoadingTemplate;
    this.isContinue = true;

    this.before = {};

    this.after = {};

    this.finally = {};

    this.init();

}
ScrollPage.prototype.init = function(){
    //绑定事件,添加滚动监听器
    this.addScrollListener(this);
    var obj = this;
    //before涵数初始化
    if(!this.config.before){//添加默认执行前的拦截器
        this.addBefore(function(){
            $("#" + obj.loadingDivId).remove();//在用了默认加载样式的情况下
            $(obj.appendTarget).after(obj.loadingTarget);
        });
    }else{
        this.addBefore(obj.config.before);////用户自定义的拦截器
    }
    //after函数初始化

    if(!this.config.responseHandler){//默认的处理器
        this.addAfter(function(){
            $(obj.appendTarget).append(obj.responseData);
            obj.increamentPageNo();
        })

    }else{
        this.addAfter(this.config.responseHandler);//用户自定义的拦截器
    }

    //finally涵数初始化 默认必需处理的
    this.addFinally(function(){
        $("#" + obj.loadingDivId).remove();//在用了默认加载样式的情况下
        var s_top = $(obj.scrollTarget).get(0).scrollTop;
        $(obj.scrollTarget).get(0).scrollTop = s_top-5;//往上调一点
    })
    //用户自定义的 final
    if(this.config.complete){
        this.addFinally(this.config.complete)
    }

}
ScrollPage.prototype.addScrollListener= function(obj){
    $(obj.scrollTarget).scroll(function(){
        var targetHight = $(obj.scrollTarget).height();
        var scrollHight = $(obj.scrollTarget)[0].scrollHeight;
        var scrollTop = $(obj.scrollTarget)[0].scrollTop;
        //触底了 要加载数据了

        if(scrollTop + targetHight >= scrollHight && obj.getHasLoad()){
            if(!obj.getHasLoad()){
                return;
            }
            obj.execute(obj);
        }
    });
};
//设置是否加载了
ScrollPage.prototype.setHasLoad = function(hasLoad){this.hasLoad = hasLoad;};
ScrollPage.prototype.getHasLoad = function(){ return this.hasLoad;};
//设置继续加载
ScrollPage.prototype.setIsContinue = function(isContinue){this.isContinue = isContinue;};
ScrollPage.prototype.getIsContinue= function(){ return this.isContinue;};

//post数据
ScrollPage.prototype.setData = function(data){
    this.data = data;
    this.data.pageNo = this.pageNo;

};
ScrollPage.prototype.getData = function(){
    return this.data;
};

//pageNo+1
ScrollPage.prototype.increamentPageNo = function(){
    this.pageNo ++ ;
    this.data.pageNo = this.pageNo;
};
ScrollPage.prototype.getPageNo = function(){ return this.pageNo};

//添加请求前的处理器
ScrollPage.prototype.addBefore = function(before){
    var key = "before_" + this.before.length;
    this.before.key = before;
};
//添加响应后的
ScrollPage.prototype.addAfter = function(after){
    var key = "after_" + this.after.length;
    this.after.key = after;
};
//添所有最后的处理器
ScrollPage.prototype.addFinally = function(final){

    var key = "finally_" + this.finally.length;
    this.finally.key = final;
};

ScrollPage.prototype.setResponseData = function(data){this.responseData = data;};
ScrollPage.prototype.getResponseData = function(){ return this.responseData;};

ScrollPage.prototype.setUrl = function(url){this.url = url;};
ScrollPage.prototype.getUrl = function(){
    return typeof this.url == "string" ? this.url :  this.url();
};

ScrollPage.prototype.setHasNext = function(hasNext){this.hasNext = hasNext;};
ScrollPage.prototype.getHasNext = function(){ return this.hasNext;};

ScrollPage.prototype.handleBefore = function(){
    this.setHasLoad(false);
    for(var fun in this.before){
        this.before[fun]();
    }
}
ScrollPage.prototype.handleAfter = function(){
    if(!this.getResponseData() || /^\s*$/.test(this.getResponseData())){
        this.setHasNext(false);//没有下一页了
        $(this.scrollTarget).unbind("scroll");
        $(this.appendTarget).after(this.defaultFinishLoadingTemplate);
        this.setIsContinue(false);
        return;
    }
    for(var fun in this.after){
        this.after[fun]();
    }
    this.setHasLoad(true);
}
ScrollPage.prototype.handleFinally = function(){
    for(var fun in this.finally){
        this.finally[fun]();
    }
}
ScrollPage.prototype.handleError = function(){
    alert("加载出错");
}
//设置当前页
//查询
ScrollPage.prototype.execute = function(obj){
    $.ajax({
        url :obj.getUrl(),//ajaxUrl,
        type:"post",
        data : obj.getData(),
        dataType:obj.dataType,
        beforeSend:function(){
            //开始后打开loading效果
            if(!obj.getHasLoad() || !obj.getIsContinue()){
                return false;
            }
            obj.handleBefore();

        },
        success:function(data){
            obj.setResponseData(data);
            obj.handleAfter();
        },
        error:function(){
            obj.handleError();
        },
        complete:function(data){
            //结束后清掉loading效果
            obj.handleFinally();
            $("#" + obj.loadingDivId).remove();
        }
    });
};
