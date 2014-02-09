package util;

import play.Play;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-11
 * Time: 下午1:41
 * 功能描述: 初始化一些配置,比如文件上传的路径，数据始化在load里面做,启动时用@OnApplicationStart标识下
 */
public class QicConfigProperties extends AbstractConfigProperties {
    private static QicConfigProperties config = new QicConfigProperties();

    private QicConfigProperties(){
    }
    //初始化一些配置,
    public   void load(){
        //这里的key可能是放在数据库配置表的
      /*  List<ConfigDto> list = SystemConfigService.loadConfig();
        for(ConfigDto configer: list){
            map.put(configer.key,configer.value);
        }*/
    }
    public static QicConfigProperties getInstance(){
        return config;
    }

    //策略小图生成路径
    public static String getStrategySmallPictureDir(){
        return Play.configuration.getProperty("iquant.strategy.smallpictrue.dir");
    }

    public static String get(String key){
        return Play.configuration.getProperty(key);
    }

}
