package controllers;

import bussiness.user.IUserDefineTemplateService;
import models.iquantCommon.UserTemplate;
import util.GsonUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-2
 * Time: 下午3:45
 * 功能描述:  用户自定义模板API
 */
public class UserTemplateCt extends BaseController {

    @Inject
    static IUserDefineTemplateService userDefineTemplateService;

    /**
     * 查询用户的自定义模板
     *
     * @param uid  用户id
     * @param type 模板类型
     */
    public static void fetchUserTemplate(long uid, int type) {
        List<UserTemplate> userTemplateList = userDefineTemplateService.findUserTemplate(uid, UserTemplate.TemplateType.STOCKPOOLTEMPLATE.getTemplateByValue(type));
        responseJSON(userTemplateList);
    }

    /**
     * 根据模板id查询模板详情
     *
     * @param id 模板id
     */
    public static void findTemplateById(long id) {
        UserTemplate userTemplate = userDefineTemplateService.findTemplateById(id);
        responseJSON(userTemplate);
    }

    /**
     * 根据模板id删除模板
     *
     * @param id
     */
    public static void deleteTemplateById(long id) {
        boolean isSuccess = userDefineTemplateService.deleteTemplateById(id);
        responseJSON(isSuccess);
    }

    /**
     * 判定模板名称是否重复
     *
     * @param type 模板类型
     * @param name 模板名称
     * @param uid  用户id
     */
    public static void isTemplateNameExist(int type, String name, long uid) {
        boolean isTemplateExist = userDefineTemplateService.isTemplateNameExist(uid, UserTemplate.TemplateType.STOCKPOOLTEMPLATE.getTemplateByValue(type), name);
        responseJSON(isTemplateExist);
    }

    /**
     * 添加自定义模板
     *
     * @param name    模板名称
     * @param type    模板类型
     * @param content 模板内容
     * @param uid     用户id
     */
    public static void addTemplate(String name, int type, String content, long uid) {
        UserTemplate returnUserTemplate = userDefineTemplateService.addTemplate(new UserTemplate(name, UserTemplate.TemplateType.STOCKPOOLTEMPLATE.getTemplateByValue(type), uid, content));
        responseJSON(returnUserTemplate);
    }

    /**
     * 修改模板
     */
    public static void updateTemplate() {
        String templateJson = fetchBody();
        UserTemplate userTemplate = GsonUtil.createWithoutNulls().fromJson(templateJson, UserTemplate.class);
        responseJSON(userDefineTemplateService.updateUserTemplate(userTemplate) > 0);
    }    /**
     * 修改模板
     */
    public static void renameUserTemplate() {
        String templateJson = fetchBody();
        UserTemplate userTemplate = GsonUtil.createWithoutNulls().fromJson(templateJson, UserTemplate.class);
        responseJSON(userDefineTemplateService.updateUserTemplate(userTemplate) > 0);
    }


}
