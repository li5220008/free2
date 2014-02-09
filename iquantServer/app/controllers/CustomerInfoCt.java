package controllers;

import bussiness.product.ICustomerInfoService;
import models.iquantCommon.CustomerIndexDto;
import models.iquantCommon.CustomerSecurityGroupDto;
import models.iquantCommon.CustomerSecurityListDto;
import models.iquantCommon.CustomerTemplateDto;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Util;
import play.mvc.With;
import util.CommonUtils;
import util.GsonUtil;
import util.LoginTokenCompose;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static util.ConstantInterface.*;

/**
 * 自定义信息接口. 如自选股, 投资组合,自定义指标等
 * 需要loginToken校验
 * User: wenzhihong
 * Date: 12-5-14
 * Time: 上午11:22
 */
@With(value = {LoginTokenCheckIntercept.class})
public class CustomerInfoCt extends BaseController {
    @Inject
    static ICustomerInfoService customerInfoService;
    /**
     * 增加自选股组
     */
    public static void addCustSecGroup(@Required(message = NAME_REQUIRED) String name, String sname, int flag) {
        CustomerSecurityGroupDto group = new CustomerSecurityGroupDto();
        group.name = name;
        group.sname = sname;
        group.flag = flag;
        group.fillUserAndProductProperty();
        long id = customerInfoService.addCustSecGroup(group);
        opAddSuccess(id);
    }


    /**
     * 判断自选股组名称是否存在. 存在返回1, 不存在返回0
     *
     * @param name
     */
    public static void existCustSecGroupName(String name) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        Long count = customerInfoService.existCustSecGroupName(compose.uid, compose.pid, name);
        if (count > 0) {
            renderText("1");
        } else {
            renderText("0");
        }
    }

    /**
     * 删除同名的自选股组. 0 没有删除. 1 删除成功
     *
     * @param name
     */
    public static void delCustSecGroupByName(String name) {
        if (name == null) { //防止name没有传的情况
            renderText("0");
        }

        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Long> gidList = customerInfoService.delCustSecGroupByName( compose.uid, compose.pid, name);

        for (Long gid : gidList) {
            delCustSecGroup(gid, compose);
        }

        renderText("1");
    }

    /**
     * 删除一个自选股组,包括删除其明细数据
     *
     * @param id
     */
    public static void delCustSecGroupById(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = delCustSecGroup(id, compose);
        opDelSuccess(id, effect);
    }

    @Util
    private static int delCustSecGroup(Long gid, LoginTokenCompose compose) {
        return customerInfoService.delCustSecGroupById(compose.uid, compose.pid, gid);
    }

    /**
     * 返回用户在这个产品上的自选股组. 只返回每个名称的最新的那条记录
     */
    public static void fetchCustSecGroupLatest() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custSecGroupList = customerInfoService. fetchCustSecGroupLatest(compose.uid, compose.pid);
        renderJSON(custSecGroupList);
    }

    /**
     * 获取指定组名称的历史记录. 按由近及远的方式返回
     */
    public static void fetchCustSecGroupByNameAll(String name) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custSecGroupList = customerInfoService.fetchCustSecGroupByNameAll( compose.uid, compose.pid, name);
        renderJSON(custSecGroupList);
    }

    /**
     * 获取指定自选股组名称,最新的自选股明细
     */
    public static void fetchCustSecGroupByNameLatestDetail(String name) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custSecGroupList = customerInfoService.fetchCustSecGroupByNameLatestDetail(compose.uid, compose.pid, name);
        renderJSON(custSecGroupList);
    }

    /**
     * 修改自选股的备注信息
     * @param comment
     * @param id
     */
    public static void updateCustSecCommentsById(String comment, Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        customerInfoService.updateCustSecCommentsById(compose.uid, compose.pid, id, comment);
        opSuccess();
    }

    /**
     * 增加一条自选股明细数据
     *
     * @param gid   自选股组id
     * @param scode 证券代码
     */
    public static void addCustSecListByGid(@Required(message = GROUPID_REQUIRED) Long gid,
                                           @Required(message = SECCODE_REQUIRED) String scode,
                                           @Required(message = SECCODE_REQUIRED) String exchange) {

        long id = addCustSecListByGidUtil(gid, scode, exchange, CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()), "");
        opAddSuccess(id);
    }

    @Util
    private static long addCustSecListByGidUtil(Long gid, String scode, String exchange, String ctime, String comment) {
        CustomerSecurityListDto secList = new CustomerSecurityListDto();
        secList.fillUserAndProductProperty();
        secList.groupId = gid;
        secList.scode = scode;
        secList.exchange = exchange;
        secList.ctime = ctime;
        secList.comment = comment;
        return customerInfoService.addCustSecListByGid(secList);
    }

    /**
     * 批量新建或修改自选股明细数据
     *
     * @param gid       自选股组id
     * @param paramJson json数组, 表示
     */
    public static void batchAddOrEditCustSecListByGid(@Required(message = GROUPID_REQUIRED) Long gid, @Required String paramJson) {
        CustomerSecurityListDto[] objArr = GsonUtil.createGson().fromJson(paramJson, CustomerSecurityListDto[].class);
        for (CustomerSecurityListDto item : objArr) {
            Logger.info("----------------id=" + item.id);
            if (StringUtils.isEmpty(item.ctime)) {
                item.ctime = CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date());
            }
            if (StringUtils.isEmpty(item.comment)) {
                item.comment = "";
            }

            addCustSecListByGidUtil(gid, item.scode, item.exchange, item.ctime, item.comment);

            /* 以下代码屏蔽,因为现在需求改了. 要求自选股要保留历史, 所以对于自选股来说修改也是新加
            if (item.id == null || item.id.longValue() < 1) { //新建
                addCustSecListByGidUtil(gid, item.scode, item.exchange, CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()), "");
            } else { //修改
                customerInfoService.updateCustSecListByGid(item);
            }*/
        }

        batchOpSuccess();
    }

    /**
     * 删除一条自选股明细数据
     *
     * @param id 明细数据id
     */
    public static void delCustSecListById(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = customerInfoService.delCustSecListById(compose.uid, compose.pid, id);
        opDelSuccess(id, effect);
    }

    /**
     * 返回自选股组里的股票明细信息
     *
     * @param gid 组id
     */
    public static void fetchCustSecListByGid(@Required(message = GROUPID_REQUIRED) Long gid) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custSecGroupList = customerInfoService.fetchCustSecListByGid( compose.uid, compose.pid, gid);
        renderJSON(custSecGroupList);
    }

    //</editor-fold>

    /**
     * 增加一个用户自定义指标
     *
     * @param name
     * @param content
     */
    public static void addCustIndex(@Required(message = NAME_REQUIRED) String name,
                                    @Required(message = INDEX_CONTENT_REQUIRED) String content) {
        CustomerIndexDto c = new CustomerIndexDto();
        c.fillUserAndProductProperty();
        c.name = name;
        c.content = content;
        long id = customerInfoService.addCustIndex( c);
        opAddSuccess(id);
    }

    /**
     * 删除一个用户自定义指标
     *
     * @param id
     */
    public static void delCustIndex(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = customerInfoService.delCustIndex(compose.uid, compose.pid, id);
        opDelSuccess(id, effect);
    }

    /**
     * 修改用户自定义指标
     *
     * @param id
     * @param name    名称
     * @param content 内容
     */
    public static void editCustIndex(@Required(message = ID_REQUIRED) Long id,
                                     String name, String content) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        CustomerIndexDto c = new CustomerIndexDto();
        c.id = id;
        c.content = content;
        c.name = name;
        c.userId = compose.uid;
        int effect = customerInfoService.editCustIndex(c);
        if (effect > 0) {
            opEditSuccess(id);
        } else {
            entityNotFound();
        }
    }

    /**
     * 返回用户自定义指标
     */
    public static void fetchCustIndex() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custIndexList = customerInfoService.fetchCustIndex(compose.uid, compose.pid);
        renderJSON(custIndexList);
    }

    /**
     * 根据自定义指标id,返回指标内容
     *
     * @param id 指标id
     */
    public static void fetchCustIndexContentByid(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String content = customerInfoService.fetchCustIndexContentByid( compose.uid, compose.pid, id);
        if (content == null) {
            entityNotFound();
        }
        renderText(content);
    }

    /**
     * 增加一个用户自定义模板
     *
     * @param name
     * @param content
     */
    public static void addCustTemplate(@Required(message = NAME_REQUIRED) String name,
                                       @Required(message = GROUPID_REQUIRED) int category,
                                       @Required(message = INDEX_CONTENT_REQUIRED) String content) {
        CustomerTemplateDto c = new CustomerTemplateDto();
        c.fillUserAndProductProperty();
        c.name = name;
        c.content = content;
        c.category = category;
        long id = customerInfoService.addCustTemplate( c);
        opAddSuccess(id);
    }

    /**
     * 删除一个用户自定义模板
     *
     * @param id
     */
    public static void delCustTemplate(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = customerInfoService.delCustTemplate(compose.uid, compose.pid, id);
        opDelSuccess(id, effect);
    }

    /**
     * 修改用户自定义模板
     *
     * @param id
     * @param name    名称
     * @param content 内容
     */
    public static void editCustTemplate(@Required(message = ID_REQUIRED) Long id,
                                        String name, String content) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        CustomerTemplateDto c = new CustomerTemplateDto();
        c.id = id;
        c.name = name;
        c.content = content;
        c.userId = compose.uid;
        int effect = customerInfoService.editCustTemplate( c);
        if (effect > 0) {
            opEditSuccess(id);
        } else {
            entityNotFound();
        }
    }

    /**
     * 返回用户自定义模板
     */
    public static void fetchCustTemplateListByCategory(Integer category) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> custIndexList = customerInfoService.fetchCustTemplateListByCategory(compose.uid, compose.pid, category);
        renderJSON(custIndexList);
    }

    /**
     * 根据自定义指标id,返回模板内容
     *
     * @param id 指标id
     */
    public static void fetchCustTemplateContentByid(@Required(message = ID_REQUIRED) Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String content = customerInfoService.fetchCustTemplateContentByid( compose.uid, compose.pid, id);
        if (content == null) {
            entityNotFound();
        }

        renderText(content);
    }

    /**
     * 检查模板名称是否重复. 重复输出id值, 不重复输出 -1
     */
    public static void custTemplateExistName(Integer category, String name) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        Long id = customerInfoService.custTemplateExistName(compose.uid, compose.pid, category, name);
        renderText(id == null ? "-1" : id);
    }

}
