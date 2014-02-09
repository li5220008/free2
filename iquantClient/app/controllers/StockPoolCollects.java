package controllers;

import annotation.QicFunction;
import bussiness.StockPoolCollectService;

import java.util.HashMap;
import java.util.Map;

/**
 * 股票池收藏
 * User: panzhiwei
 * Date: 12-11-19
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCollects extends BasePlayControllerSupport {
    /**
     * 股票池收藏
     *
     * @param uid
     * @param spid
     */
    @QicFunction(id = 10)
    public static void addcollect(long uid, long spid) {
        try {
            StockPoolCollectService.stockpooladdcollect(uid, spid);
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("isSuccess", true);
            json.put("message", "股票池收藏成功。");
            renderJSON(json);
        } catch (Exception e) {
            Map<String, Object> json2 = new HashMap<String, Object>();
            json2.put("isSuccess", false);
            json2.put("message", e.getMessage());
            renderJSON(json2);
        }
    }

    /**
     * 股票池取消收藏
     *
     * @param uid
     * @param spid
     */
    @QicFunction(id = 10)
    public static void delcollect(long uid, long spid) {
        try {
            StockPoolCollectService.stockpooldeletecollect(uid, spid);
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("isSuccess", true);
            json.put("message", "股票池取消收藏成功。");
            renderJSON(json);
        } catch (Exception e) {
            Map<String, Object> json2 = new HashMap<String, Object>();
            json2.put("isSuccess", false);
            json2.put("message", e.getMessage());
            renderJSON(json2);
        }
    }
}
