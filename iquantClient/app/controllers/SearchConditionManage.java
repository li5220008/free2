package controllers;

import bussiness.StrategyService;
import bussiness.UserTemplateService;
import com.google.gson.Gson;
import models.iquantCommon.StockPoolSearchCnd;
import models.iquantCommon.StrategySearchCnd;
import models.iquantCommon.UserTemplate;
import org.apache.commons.lang.StringUtils;
import play.mvc.Util;
import util.GsonUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户搜索条件管理
 * User: 梁兵
 * Date: 13-07-02
 * Time: 上午10:09
 */
public class SearchConditionManage extends BasePlayControllerSupport {

    @Inject
    static StrategyService strategyService;
    @Inject
    static UserTemplateService userTemplateService;

    /**
     * 保存策略搜索条件
     */
    public static void saveStrategySearchCond(StrategySearchCnd cnd, Long uid, String cndName) {
        Map<String, Object> json = addCond(cnd, uid, cndName, 1);
        renderJSON(json);
    }

    /**
     * 保存股票池搜索条件
     */
    public static void saveStockPoolSearchCond(StockPoolSearchCnd cnd, Long uid, String cndName) {
        trimRecommendOrg(cnd);


        Map<String, Object> json = addCond(cnd, uid, cndName, 2);
        renderJSON(json);
    }

    /**
     * 去掉空的机构
     * @param cnd
     */
    @Util
    private static void trimRecommendOrg(StockPoolSearchCnd cnd) {
        //去掉空的条件.
        if(cnd.recommendOrgs != null && cnd.recommendOrgs.length > 0){
            List<String> recommendOrgList = new ArrayList<String>(cnd.recommendOrgs.length);
            for (String org : cnd.recommendOrgs) {
                if(StringUtils.isNotBlank(org)){
                    recommendOrgList.add(org);
                }
            }
            cnd.recommendOrgs = recommendOrgList.toArray(new String[recommendOrgList.size()] );
        }
    }

    @Util
    static Map<String, Object> addCond(Object cnd, Long uid, String cndName, int type) {
        Map<String, Object> json = new HashMap<String, Object>();

        //先检查一下是否重名
        if(userTemplateService.hasSameName(uid,type,cndName)){
            json.put("op", false);
            json.put("msg", "名称已存在");
            json.put("cndName", cndName);
            json.put("sameName", true);
        }else{
            UserTemplate ut = new UserTemplate();
            ut.uid = uid;
            ut.name = cndName;
            ut.content = new Gson().toJson(cnd);
            ut.type = type;
            ut = userTemplateService.saveUserTemple(ut);

            if(ut!=null){
                json.put("op", true);
                json.put("msg", "增加成功");
                json.put("id", ut.id);
                json.put("utInfo", cnd);
            }else{
                json.put("op", false);
                json.put("msg", "增加失败");
                json.put("id", ut.id);
            }

        }
        return json;
    }


    /**
     * 修改策略搜索条件
     * @param cnd
     */
    public static void editStrategyCond(StrategySearchCnd cnd, Long id, Long uid) {
        UserTemplate ut =userTemplateService.fetchUserTemplate(id); //todo 根据模板ID查询模板对应的信息
        Map<String, Object> json = new HashMap<String, Object>();
        if (ut == null) {
            json.put("op", false);
            json.put("msg", "自定义查询条件不存在");
        } else if (ut.uid != uid.longValue()) {
            forbidden(AuthBaseIntercept.NO_PERMISSION_RESOURCE);
        } else {
            String content = new Gson().toJson(cnd);
            ut.content = content;
            boolean isSuccess = userTemplateService.editUserTemplate(ut);
            if(isSuccess){
                json.put("op", true);
                json.put("msg", "修改成功");
                json.put("utInfo", cnd); //修改后的条件信息
            }else{
                json.put("op", false);
                json.put("msg", "修改失败");
            }
        }

        renderJSON(json);
    }

    /**
     * 修改股票池搜索条件
     */
    public static void editStockPoolCond(StockPoolSearchCnd cnd, Long id, Long uid) {
        trimRecommendOrg(cnd);
        UserTemplate ut = userTemplateService.fetchUserTemplate(id);// UserTemplate.findById(id);
        Map<String, Object> json = new HashMap<String, Object>();
        if (ut == null) {
            json.put("op", false);
            json.put("msg", "自定义查询条件不存在");
        } else if (ut.uid != uid.longValue()) {
            forbidden(AuthBaseIntercept.NO_PERMISSION_RESOURCE);
        } else {
            ut.content = new Gson().toJson(cnd);
            boolean  isSuccess = userTemplateService.updateUserTemplate(ut);
            //ut.save();
            json.put("op", true);
            json.put("msg", "修改成功");
            json.put("utInfo", cnd);
        }

        renderJSON(json);
    }

    /**
     * 搜索条件重命名或新建
     */
    public static void renameOrNewCond(Long id, String name, Long uid, Integer type) {
        if(id == -999){ //这是新加一个
            Map<String, Object> json = null;
            if(type == 1){
                json = addCond(new StrategySearchCnd(), uid, name, 1);
            }else if(type == 2){
                json = addCond(new StockPoolSearchCnd(), uid, name, 2);
            }
            if(json == null) {
                json = new HashMap<String, Object>();
            }
            renderJSON(json);
        }else {
            UserTemplate ut =userTemplateService.fetchUserTemplate(id);
            Map<String, Object> json = new HashMap<String, Object>();
            if (ut == null) {
                json.put("op", false);
                json.put("msg", "自定义查询条件不存在");
            } else if (ut.uid != uid.longValue()) {
                forbidden(AuthBaseIntercept.NO_PERMISSION_RESOURCE);
            } else {
                ut.name = name;
                userTemplateService.renameUserTemplate(ut);
                //ut.save();
                json.put("op", true);
                json.put("msg", "改名成功");
            }

            renderJSON(json);
        }

    }

    /**
     * 删除搜索条件
     * @param id
     */
    public static void delCond(Long id, Long uid) {
        boolean bool = userTemplateService.deleteUserTemplate(id);
        Map<String, Object> json = new HashMap<String, Object>();
        if (bool) {
            json.put("op", true);
            json.put("msg", "删除成功");
        } else {
            json.put("op", false);
            json.put("msg", "删除失败");
        }
        renderJSON(json);
    }

    /**
     * 返回用户自定义的搜索条件列表
     * type: 1. 策略自定义搜索.   2. 股票池自定义搜索
     * 返回的json包含属性: id, name, type, content
     */
    public static void fetchUserStrategySearchCond(Long uid, Integer type) {
        if (type == null) {
            type = 1;
        }
        List<UserTemplate> templateList = userTemplateService.fetchUserSearchCond(uid, type);
        renderJSON(GsonUtil.toJsonWithOutHibernateProxy(templateList));
    }
}
