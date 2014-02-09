package controllers;

import bussiness.user.IUserInfoService;
import bussiness.user.IUserServerService;
import models.iquantCommon.UserServerDto;
import play.data.validation.Required;

import javax.inject.Inject;

/**
 * desc: 后台服务器管理管理功能
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-28
 * Time: 上午10:51
 */
public class UserServerCt extends BaseController {
    @Inject
    static IUserServerService iUserServerService;
    /**
     * 添加服务器
     * @param ip
     * @param port
     * @param uid
     */
    public static void addUserServer(@Required String ip, @Required int port, @Required long uid){
        UserServerDto userServerDto = iUserServerService.addServer(new UserServerDto(ip,port,uid));
        responseJSON(userServerDto);
    }
    /**
     * 更新服务器
     * @param ip
     * @param port
     * @param uid
     */
    public static void updateUserServer(@Required String ip, @Required int port, @Required long uid){
        responseJSON(iUserServerService.updateServer(new UserServerDto(ip,port,uid))>0);
    }

    /**
     * 根据ID获取服务器配置
     * @param id
     */
    public static void fetchServerById(long id){
        UserServerDto userServerDto = iUserServerService.fetchServerById(id);
        responseJSON(userServerDto);
    }

    public static void test(){
        iUserServerService.testProductMigration();
    }

}
