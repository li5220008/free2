package controllers;

import annotation.QicFunction;
import bussiness.LogInfosService;
import bussiness.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import play.mvc.Util;
import util.SystemLoggerMessage;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理
 * User: liangbing
 * Date: 12-12-12
 * Time: 上午10:31
 * 类容:策略上架通知,策略上架通知,其他通知
 */
//@With({AuthorBaseIntercept.class})
public class ConfigInfos extends BasePlayControllerSupport {
    @Inject
    static SystemConfigService systemConfigService;
    @Inject
    static LogInfosService logInfosService;
    /**
     * 配置管理
     */
    @QicFunction(id=24)
    public static void configManage() {
        Integer tab = params.get("tab", Integer.class);
        String keyName = params.get("keyName",String.class);
        if(StringUtils.isBlank(keyName)){
            keyName="strategyDownMsg";
        }
        switch (tab = tab == null ? 2 : tab) {
            case 1:
                upStrategyMsg(tab,keyName);
                break;
            case 2:
                downStrategyMsg(tab,keyName);
                break;
            case 3:
                otherMsg(tab,keyName);
                break;
            default:
                renderText(SystemResponseMessage.ILLEGAL_REQUEST_RSP);
        }
    }

    /**
     * 策略上架通知
     * @param tab
     */
    @Util
    private static void upStrategyMsg(int tab,String keyName) {
        //ConfigManage cm = ConfigManage.find("byKeyName", keyName).first();
        String keyValue= systemConfigService.get(keyName);
        render(keyValue, tab,keyName);
    }

    /**
     * 策略下架通知
     * @param tab
     */
    @Util
    private static void downStrategyMsg(int tab,String keyName) {
        String keyValue= systemConfigService.get(keyName);
        render(keyValue, tab,keyName);
    }

    /**
     * 其他消息通知
     * @param tab
     */
    @Util
    private static void otherMsg(int tab,String keyName) {
        String keyValue= systemConfigService.get(keyName);
        render(keyValue, tab,keyName);
    }

    /**
     * 保存修改内容
     * @param tab
     * @param content
     */
    public static void saveMsg(int tab, String content,String keyName,long uid) {
        boolean isSuccess = systemConfigService.updateValueByKey(keyName,content);
        Map<String,Object> json = new HashMap<String, Object>();
        if(isSuccess){
            json.put("flag",true);
            json.put("msg","保存成功");
        }else{
            json.put("flag",false);
            json.put("msg","保存失败");
        }
        // 已经移到server端做
        // logInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_USER_STATUS_MODIFY, SystemLoggerMessage.USER_STATUS_MODIFY, SystemLoggerMessage.TYPE);//写入系统日志
        renderJSON(json);
    }
}
