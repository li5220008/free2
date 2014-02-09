package controllers;

import business.UserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import models.iquantCommon.UserInfo;
import play.Logger;
import play.Play;
import play.db.jpa.NoTransaction;
import play.libs.Codec;
import play.libs.IO;
import play.modules.redis.Redis;
import play.mvc.Controller;
import util.RedisKeys;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;

public class Application extends Controller {

    @Inject
    static UserService userService;
    public static void index() {
        render();
    }

    //把记录用户登陆的mac地址清除掉
    public static void gotoClearUserMacSet(){
        render();
    }

    public static void clearUserMacSet(String account) {
        Logger.info("消除帐号[%s]的macset", account);
        UserInfo userInfo = userService.findByAccount(account);
        if (userInfo != null) {
            String userMacSetKey = RedisKeys.userMacSetKey(userInfo.account);
            Redis.del(new String[]{userMacSetKey});
            renderText("清除了%s的macSet", account);
        }else{
            renderText("没有找到用户");
        }
    }

    static Joiner joiner = Joiner.on("\r\n").skipNulls();
    //列出account的mac地址集信息
    public static void listUserMacSet(String account){
        UserInfo userInfo = userService.findByAccount(account);
        if (userInfo != null) {
            String userMacSetKey = RedisKeys.userMacSetKey(userInfo.account);
            Set<String> macSets = Redis.smembers(userMacSetKey);
            if (macSets == null) {
                macSets = Sets.newHashSet();
            }
            renderText(joiner.join(macSets));
        } else {
            renderText("没有找到用户");
        }
    }

   public static void fetchUser(String account){
       UserInfo userInfo = userService.findByAccount(account);
       renderJSON(userInfo);
   }
}