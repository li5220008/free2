package controllers;

import annotation.QicFunction;
import play.mvc.Before;
import play.mvc.Controller;
import util.FunctionAuthManager;

import java.lang.annotation.Annotation;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-25
 * Time: 下午1:37
 * 功能描述:
 */
public class FunctionInterceptor extends Controller {
    @Before(priority = 2)
    public static void doAuth(){


        Annotation annotation =  request.invokedMethod.getAnnotation(QicFunction.class);
        if(annotation !=null ){
           //鉴权开始
              QicFunction function =  (QicFunction)annotation;
         long fid = function.id();
         long uid = params.get(AuthorBaseIntercept.USER_ID_SESSION_KEY,Long.class);
        if(!FunctionAuthManager.auth(fid, uid)){
            if(fid == 21){//当没有用户权限管理的时候尝试访问角色权限管理.这个因为页面比较特别
            // RoleInfos.roleList();
                redirect("/roleInfos/roleList");
            }
            //判断子菜单
            boolean  hasAuth = false;
            long[] dependencys = function.dependencies();
            for(long subid :dependencys){
                if(FunctionAuthManager.auth(subid, uid)){
                    hasAuth=true;
                }
            }
            if(!hasAuth){
                forbidden("无权限");
            }

           }

        }else {
            //放行
        }

    }

}
