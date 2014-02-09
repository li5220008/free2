package bussiness.user;

import models.iquantCommon.UserTemplate;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-2
 * Time: 上午9:29
 * 功能描述:
 */
public interface IUserDefineTemplateService {

    /**
     * 查询用户自定义模板
     *
     * @param uid          用户id
     * @param templateType 模板类型
     * @return
     */
    List<UserTemplate> findUserTemplate(long uid, UserTemplate.TemplateType templateType);

    /**
     * 检查模板名称是否存在。同一用户同一类型的模板不允许存在同名
     *
     * @param uid          用户id
     * @param templateType 模板类型
     * @param templateName 模板名称
     * @return
     */
    boolean isTemplateNameExist(long uid, UserTemplate.TemplateType templateType, String templateName);

    /**
     * 根据主键id查找模板
     *
     * @param id 主键id
     * @return
     */
    UserTemplate findTemplateById(long id);

    /**
     * 添加模板
     *
     * @param userTemplate 新的模板
     * @return
     */

    UserTemplate addTemplate(UserTemplate userTemplate);

    /**
     * 删除自定义模板
     * @param id
     * @return
     */

    boolean deleteTemplateById(long  id);

    /**
     * 修改模板
     * @param userTemplate
     * @return
     */
    int updateUserTemplate(UserTemplate userTemplate);

    int renameUserTemplate(UserTemplate userTemplate);


}
