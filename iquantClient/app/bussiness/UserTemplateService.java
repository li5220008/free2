package bussiness;

import models.iquantCommon.UserTemplate;
import protoc.HttpBody;
import protoc.Protocol;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.List;

import static protoc.URILib.FETCH_USER_SEARCHCOND_LIST;

/**
 * UserTemplate service
 * User: panzhiwei
 * Date: 13-7-1
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class UserTemplateService extends BasicService {

    /**
     * 返回用户已定义的模板
     *
     * @param uid
     * @param type 1. 策略 2. 股票池
     * @return 返回的UserTemplate包含属性: id, name, type, content
     */
    public static List<UserTemplate> fetchUserSearchCond(Long uid, int type) {
        ActionResult<List<UserTemplate>> result = remoteRequestService.getList(FETCH_USER_SEARCHCOND_LIST, UserTemplate.class,uid, type);
        ResponseHeader responseHeader = result.header;
        List<UserTemplate> templateList = null;
        if (responseHeader.status == Protocol.STATU_SSUCCESS) {
            templateList = result.data;
        }
        return templateList;
    }


    public UserTemplate fetchUserTemplate(long id){
        UserTemplate  userTemplate = new UserTemplate();
        String url = URILib.FETCH_USER_TEMPLATE_INFO;//todo
        ActionResult<UserTemplate> userTemplateActionResult = remoteRequestService.getBean(url,UserTemplate.class,id);
        if(userTemplateActionResult!=null){
            userTemplate = userTemplateActionResult.data;
        }
        return userTemplate;
    }
    /**
     * 保存用户定义模板
     * @param ut
     * @return
     */
    public UserTemplate saveUserTemple(UserTemplate ut){
        String url = URILib.ADD_USER_TEMPLATE;
        long uid = ut.uid;
        int type = ut.type;
        String content = ut.content;
        String name = ut.name;
        ActionResult<UserTemplate> result =remoteRequestService.getBean(url,UserTemplate.class,uid,type,name,content);
        return result.data;
    }

    public boolean editUserTemplate(UserTemplate ut) {
        String url = URILib.SAVE_USER_TEMPLATE;
        HttpBody httpBody = new HttpBody(ut);
        boolean bool = Boolean.valueOf(remoteRequestService.getSingleValue(url,httpBody).data);
        return bool;
    }

    /**
     * 删除用户订阅模板
     * @param id
     * @return
     */
    public boolean deleteUserTemplate(long id){
        return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.DETELE_USER_TEMPLATE,id).data);
    }
    /**
     * 是否有重名. 有的话返回true, 没有则返回false
     * @param uid  用户id
     * @param type 1. 策略 2. 股票池
     * @return
     */
    public  boolean hasSameName(Long uid, int type, String name) {
        boolean bool = Boolean.valueOf(remoteRequestService.getSingleValue(URILib.JUDGE_SAME_NAME,uid,type,name).data);
        return bool;
    }
    public boolean updateUserTemplate(UserTemplate userTemplate){
        HttpBody body = new HttpBody(userTemplate);
        boolean bool = Boolean.valueOf(remoteRequestService.getSingleValue(URILib.UPDATE__TEMPLATE,body).data);
        return bool;
    }
    public boolean  renameUserTemplate(UserTemplate userTemplate){
        HttpBody body = new HttpBody(userTemplate);
        boolean bool = Boolean.valueOf(remoteRequestService.getSingleValue(URILib.RENAME__TEMPLATE,body).data);
        return bool;
    }

}
