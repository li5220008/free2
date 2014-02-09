package bussiness.common.impl;

import bussiness.common.ISystemConfigService;
import dbutils.SqlLoader;
import models.iquantCommon.ConfigDto;
import utils.QicConfigProperties;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-13
 * Time: 下午5:59
 * 功能描述:读取config_manage中的配置
 */
public class SystemConfigService extends BaseService implements ISystemConfigService {
    /**
     * 加载配置表
     */
    public  List<ConfigDto> loadConfig(){
        String sql = SqlLoader.getSqlById("loadConfig");
        List<ConfigDto> list = qicDbUtil.queryBeanList(sql, ConfigDto.class);
        return list;

    }
    public   boolean updateValueByKey(String key,String value){
        String sql = SqlLoader.getSqlById("updateValueByKey");
        int row = qicDbUtil.update(sql, value, key);
        if(row>0){//更新缓存
            QicConfigProperties.set(key, value);
        }
        return qicDbUtil.update(sql, value, key)>0;
    }
    public  String get(String key){
        return QicConfigProperties.getString(key);
    }
}
