package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.user.IUserDefineTemplateService;
import models.iquantCommon.UserTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-2
 * Time: 下午3:25
 * 功能描述:
 */
public class UserDefineTemplateService extends BaseService implements IUserDefineTemplateService {
    @Override
    public List<UserTemplate> findUserTemplate(long uid, UserTemplate.TemplateType templateType) {
        UserTemplate userTemplate = new UserTemplate();
        userTemplate.uid = uid;
        userTemplate.type = templateType.getValue();

        List<UserTemplate> list = qicDbUtil.queryBeanListWithNameParam("findUserTemplate", UserTemplate.class, userTemplate);
        return list;
    }

    @Override
    public boolean isTemplateNameExist(long uid, UserTemplate.TemplateType templateType, String templateName) {
        UserTemplate userTemplate = new UserTemplate();
        userTemplate.uid = uid;
        userTemplate.type = templateType.getValue();
        userTemplate.name = templateName;
        UserTemplate template = qicDbUtil.querySingleBeanWithNameParam("findTemplateByType", UserTemplate.class, userTemplate);

        return template != null;
    }

    @Override
    public UserTemplate findTemplateById(long id) {
        Map map = new HashMap();
        map.put("id",id);
        UserTemplate template = qicDbUtil.querySingleBeanWithNameParamMap("findTemplateById", UserTemplate.class, map);
        return template;
    }

    @Override
    public UserTemplate addTemplate(UserTemplate userTemplate) {
        long id = qicDbUtil.insertWithNameParam("addTemplate", userTemplate);
        userTemplate.id = id;
        return userTemplate;
    }

    @Override
    public boolean deleteTemplateById(long id) {
        Map map = new HashMap();
        map.put("id",id);
        int effectRow = qicDbUtil.updateWithNameParamMap("deleteTemplateById", map);
        return effectRow > 0;
    }

    @Override
    public int updateUserTemplate(UserTemplate userTemplate) {
        return qicDbUtil.updateWithNameParam("updateUserTemplate", userTemplate);
    }

    @Override
    public int renameUserTemplate(UserTemplate userTemplate) {
        return qicDbUtil.updateWithNameParam("renameUserTemplate", userTemplate);
    }
}
