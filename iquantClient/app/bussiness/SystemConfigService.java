package bussiness;

import bussiness.BasicService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.iquantCommon.ConfigDto;
import protoc.Protocol;
import protoc.URILib;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-2
 * Time: 上午11:49
 * 功能描述:
 */
public class SystemConfigService extends BasicService {

    /**
     * 获取配置值
     * @param key
     * @return
     */
    public static String get(String key){
        return  remoteRequestService.getSingleValue(URILib.FETCH_SYSTEM_CONFIG_VALUE,key).data;
    }

    /**
     * 加载配置表
     */
    public static List<ConfigDto> loadConfig(){
        return remoteRequestService.getList(URILib.LOAD_CONFIG_LIST, ConfigDto.class).data;
    }

    /**
     * 根据key更新用户配置值value
     * @param key
     * @param value
     * @return
     */
    public static boolean updateValueByKey (String key,String value){
        boolean success = false;
        JsonElement jsonElement = remoteRequestService.getJson(URILib.UPDATE_SYSVALUE_BY_KEY, key, value);
        if(jsonElement.isJsonObject()){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            success = jsonObject.get(Protocol.GlobalFieldName.STATUS).getAsInt() == 0 ? true : false;
        }
        return success;
    }
}
