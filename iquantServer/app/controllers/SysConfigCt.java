package controllers;

import bussiness.common.impl.SystemConfigService;

import javax.inject.Inject;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-4
 * Time: 下午5:06
 */
public class SysConfigCt extends BaseController {
    @Inject
    static SystemConfigService systemConfigService;

    /**
     * 根据关键字获取系统配置信息
     *
     * @param key
     */
    public static void fetchValue(String key) {

        String value = systemConfigService.get(key);
        responseJSON(value);
    }

    /**
     * 修改配置
     * @param key
     * @param value
     */
    public static void updateSysValueByKey(String key, String value) {
        responseJSON(systemConfigService.updateValueByKey(key, value));
    }
}
