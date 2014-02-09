package controllers;

import annotation.QicFunction;
import bussiness.StrategyService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略收藏
 * User: 梁兵
 * Date: 13-07-02
 * Time: 下午3:24
 */

public class UserStrategyCollects extends BasePlayControllerSupport {

    @Inject
    static StrategyService strategyService;

    //加入收藏
    @QicFunction(id = 5)
    public static void addstrategycollect(long sid, Long uid) {
        boolean bool =strategyService.addstrategycollect(sid, uid);
        Map<String, Object> json = new HashMap<String, Object>();
        if (bool) {
            json.put("isSuccess", true);
            json.put("message", "策略收藏成功。");
        } else {
            json.put("isSuccess", false);
            json.put("message", "策略收藏失败,请查看代码");
        }
        renderJSON(json);

    }

    //取消收藏
    @QicFunction(id = 5)
    public static void deletestrategycollect(long sid, Long uid) {
        boolean bool = strategyService.deletestrategycollect(sid, uid);
        Map<String, Object> json = new HashMap<String, Object>();
        if (bool) {
            json.put("isSuccess", true);
            json.put("message", "取消收藏成功。");
        } else {
            json.put("isSuccess", false);
            json.put("message", "策略取消失败,请查看代码");
        }
        renderJSON(json);
    }
}

