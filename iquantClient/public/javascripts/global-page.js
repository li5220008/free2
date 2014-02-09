//分页要用到的. 根据页面结构
$(function () {
    //前一页
    $(".previous").click(function (e) {
        var $this = $(this);
        var $form = $this.closest("form.pageFrom"); //向上找到第一个匹配的
        var curPageNo = parseInt($("input[name='curPageNo']", $form).val());
        var maxPageNo = parseInt($("input[name='maxPageNo']", $form).val());

        if (curPageNo <= 1) {
            $.qicTips({target:$this, level:2, message:'已是首页' });
            e.stopPropagation();
            return;
        } else {
            buildUrlAndSubmit($form, (curPageNo - 1));
            e.stopPropagation();
        }
    });

    //下一页
    $(".next").click(function (e) {
        var $this = $(this);
        var $form = $this.parentsUntil("form.pageFrom").parent();
        if ($form.size() == 0) {
            $form = $this.parent();
        }
        var curPageNo = parseInt($("input[name='curPageNo']", $form).val());
        var maxPageNo = parseInt($("input[name='maxPageNo']", $form).val());

        if (curPageNo + 1 > maxPageNo) {
            $.qicTips({target:$this, level:2, message:'已是尾页' });
            e.stopPropagation();
            return;
        } else {
            buildUrlAndSubmit($form, (curPageNo + 1));
            e.stopPropagation();
        }
    });

    //具体页
    $(".specificPage").click(function (e) {
        var $this = $(this);
        var $form = $this.closest("form.pageFrom"); //向上找到第一个匹配的
        var pageNum = parseInt($this.attr("pageNum")); //要跳转的页码
        buildUrlAndSubmit($form, pageNum);
        e.stopPropagation();
    });

    //跳转到指定页
    $(".page_ok").click(function (e) {
        var $this = $(this);
        gotoPage($this);
        e.stopPropagation();
    });

    function IsNum(num){
        var reNum=/^\d*$/;
        return(reNum.test(num));
    }

    //跳转到指定页
    function gotoPage($this){
        var $form = $this.closest("form.pageFrom"); //向上找到第一个匹配的
        var curPageNo = parseInt($("input[name='curPageNo']", $form).val());
        var maxPageNo = parseInt($("input[name='maxPageNo']", $form).val());
        var gotoPageNumber = parseInt($("input[name='gotoPageNumber']", $form).val()); //要跳转的页码
        if(!IsNum($("input[name='gotoPageNumber']", $form).val())){
            $.qicTips({target:$this, level:2, message:'输入有误，请输入合理页码' });
        }else if (gotoPageNumber < 1) {
            $.qicTips({target:$this, level:2, message:'输入的页数应该大于零' });
        } else if (gotoPageNumber > maxPageNo) {
            $.qicTips({target:$this, level:2, message:'输入的页数大于最大页' });
        } else if (gotoPageNumber == curPageNo) {
            $.qicTips({target:$this, level:2, message:'当前页就是要显示的数据,不用跳转' });
        } else {
            buildUrlAndSubmit($form, gotoPageNumber);
        }
    }

    //处理回车事件
    $(".gotoPageNumberClass").keypress(function(event){
        console.log("enter");
        var keycode = event.which;
        if (keycode == 13) {
            var $this = $(this);
            gotoPage($this);
            return false;
        }
    });

    //构造url,并提交
    function buildUrlAndSubmit($form, pageNo) {
        var url = $form[0].action;
        var hasParam = url.indexOf('?');
        var reg = /^\d+$/;
        if(!reg.test(pageNo)){
            pageNo = 1;
        }
        if (hasParam!=-1) {
            url = url + "&pageNo=" + pageNo;
            window.location.href = url; //转向
        } else {
            url = url + "?pageNo=" + pageNo;
            window.location.href = url;
        }
    }
});
//分页要用到的end