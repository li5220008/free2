package bussiness.common;

import models.iquantCommon.ConfigDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 上午10:01
 * 功能描述:
 */
public interface ISystemConfigService {


    public List<ConfigDto> loadConfig();

    public boolean updateValueByKey(String key, String value);

    public String get(String key);
}
