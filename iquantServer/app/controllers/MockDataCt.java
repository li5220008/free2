package controllers;

import bussiness.common.impl.MocoDataService;
import play.mvc.Controller;

/**
 * User: wenzhihong
 * Date: 12-11-10
 * Time: 上午11:10
 */
public class MockDataCt extends Controller {

    /**
     * 模拟生成收益率数据
     */
    public static void mockYieldData()  {
        MocoDataService.mockYieldData();
    }

    /**
     * 策略超市扩展表 和 股票池扩展表 绩效数据指标 手动更新数据调用方法
     */
    public static void createSmallPic() {
        MocoDataService.createSmallPic();
        renderText("生成数据成功!");
    }


}
