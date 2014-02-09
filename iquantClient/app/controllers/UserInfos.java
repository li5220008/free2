package controllers;

import annotation.QicFunction;
import bussiness.LogInfosService;
import bussiness.SaleDepartmentService;
import bussiness.UserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import models.iquantCommon.RoleInfoDto;
import models.iquantCommon.SaleDepartMentDto;
import models.iquantCommon.TradeAccountDto;
import models.iquantCommon.UserInfoDto;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import util.CommonUtils;
import util.GsonUtil;
import util.SystemLoggerMessage;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:21
 * 功能描述:  用户信息控制器
 */
public class UserInfos extends BasePlayControllerSupport {
    @Inject
    static UserInfoService userInfoService;

    @Inject
    static SaleDepartmentService saleDepartmentService;

    /**
     * 查询用户信息
     * @param id
     */
    @QicFunction(id=21)
    public  static  void  show(long id){
        UserInfoDto udto = userInfoService.findUserInfoById(id);
        List<SaleDepartMentDto> saleDepartments  = saleDepartmentService.findAll();
        List<RoleInfoDto> roleInfoDtos = userInfoService.findUserRoleInfoById(id);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userInfo",udto);
        map.put("saleDepartmentsInfo",saleDepartments);
        map.put("roleInfo",roleInfoDtos);
        map.put("functionInfo",userInfoService.findUserFunctionInfoById(id));
        renderJSON(map);
    }

    /**
     * 修改用户信息
     * @param userInfoDto
     */
    @QicFunction(id=21)
    public static void update(UserInfoDto userInfoDto,long uid){
       boolean isSuccess =  userInfoService.updateUserInfo(userInfoDto);
        //后期类似的成功返回json串,放到公共文件
        setMessage(SystemResponseMessage.UPDATE_USER_SUCESS_RSP);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPDATE_USERINFO, SystemLoggerMessage.UPDATE_USERINFO, SystemLoggerMessage.TYPE);//写入操作日志
        renderJSON(getSampleResponseMap());
    }

    /**
     * 新建用户
     * @param userInfo
     */
    @QicFunction(id=21)
    public static void adduser(UserInfoDto userInfo, String userTradeAccountStr, Long uid) {
        String depId = params.get("depId", String.class);
        userInfo.saleDept = depId;
        List<Long> keys = userInfoService.addUser(userInfo);
        setExtraData("uids", keys);
        setMessage(SystemResponseMessage.ADD_USER_SUCCESS_RSP);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_ADD_USER, SystemLoggerMessage.ADD_USER, SystemLoggerMessage.TYPE);

        Long opUid = keys.get(0);
        Logger.info("创建用户account[%s]name[%s], opUid为[%s]", userInfo.account, userInfo.name, opUid);
        if (StringUtils.isNotBlank(userTradeAccountStr)) {

            List<Map<String,Object>> userTradeAccounts = Lists.newArrayList();
            try {
                userTradeAccounts = GsonUtil.createGson().fromJson(userTradeAccountStr, new TypeToken<List<Map<String,Object>>>(){}.getType());
            } catch (JsonSyntaxException e) {
                Logger.error("解析json语法出错 %s", userTradeAccountStr);
            }

            for (Map<String, Object> item : userTradeAccounts) {
                String itemJson = GsonUtil.createGson().toJson(item);
                Logger.info("创建交易帐号json:" + itemJson);
                long id =userInfoService.addTradeAccount(item,fetchNtToken(), opUid);
                Logger.info("创建交易帐号返回id:" + id);
                //TODO 这里先不处理失败的情况
            }

        }

        renderJSON(getSampleResponseMap());
        //render(idlist);
    }

    /**
     * 删除用户
     * @param ids
     */
    @QicFunction(id=21)
    public static void deluser(String[] ids,Long uid){
        userInfoService.delUser(ids);
        setMessage(SystemResponseMessage.DEL_USER_SUCCESS_RSP);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 用户状态修改
     * @param ids
     * @param status
     */
    @QicFunction(id=21)
    public static void userStateModify(String[] ids,int status,Long uid){
        userInfoService.userStateModify(ids,status);
        setMessage(SystemResponseMessage.MODIFY_USER_RSP);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UPDATE_USERSTATUS, SystemLoggerMessage.UPDATE_USERSTATUS, SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 验证email
     * @param email
     */
    public static void findUserByEmail(String email){
        if(  userInfoService.findUserByEmail(email)==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 验证账户
     * @param account
     */
    public static void findUserByAccount(String account){
        if(  userInfoService.findUserByAccount(account)==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 到期用户延期
     * @param ids
     * @param delaydate
     */
    @QicFunction(id=21)
    public static void userDelay(String[] ids,String delaydate,long uid){
        Date date = CommonUtils.parseDate(delaydate);
        if(date.before(new Date())){
            setMessage(SystemResponseMessage.USER_DELAY_FAILURE);
            setSuccessFlag(false);
            renderJSON(getSampleResponseMap());
        }
        userInfoService.userdelay(ids,delaydate);
        setMessage(SystemResponseMessage.USER_DELAY);
        LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_USER_DELAY, SystemLoggerMessage.USER_DELAY, SystemLoggerMessage.TYPE);//写入操作日志
        renderJSON(getSampleResponseMap());

    }

    /**
     * 验证email
     * @param email
     */
    public static void findUserByEmailExcludeSelf(String email){
        String[] emails = email.split(",");
        if(  userInfoService.findUserByEmailExcludeSelf(emails[0],emails[1])==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    /**
     * 验证账户
     * @param account
     */
    public static void findUserByAccountExcludeSelf(String account){
        String[] accounts = account.split(",");
        if(  userInfoService.findUserByAccountExcludeSelf(accounts[0],accounts[1])==null)    {
            renderText(true);
        }
        else
            renderText(false);
    }

    public static void newUser(){
        List<SaleDepartMentDto> saleDepartments = saleDepartmentService.findAll();
        render(saleDepartments);
    }

    /**
     * 验证资金账号名的唯一性
     * @param opUid
     * @param accountName
     */
    public static void verifyTradeAccountNotExist(String accountName, long opUid){
        Logger.info("fetchTradeAccount 要操作的opUid为[%s]", opUid);
        renderJSON(userInfoService.verifyTradeAccountNotExist(accountName,opUid,fetchNtToken()));
    }

    //获取用户的交易帐号
    public static void fetchTradeAccount(Long opUid) {
        Logger.info("fetchTradeAccount 要操作的opUid为[%s]", opUid);
        List<TradeAccountDto> list = userInfoService.findTradeAccount(fetchNtToken(), opUid);
        renderJSON(list);
    }

    //删除用户的交易账号
    public static void delTradeAccount(String idVal, Long opUid){
        Logger.info("delTradeAccount 要操作的opUid为[%s]", opUid);
        long delId = userInfoService.delTradeAccount(Long.valueOf(idVal),fetchNtToken(), opUid);
        Logger.info("删除用户的交易帐号,删除id = %s", delId);
        Map<String,Object> map = Maps.newHashMap();
        map.put("success",delId>0?true:false);
        map.put("id",delId);
        renderJSON(map);
    }

    public static void editTradeAccount(Long id, String name, String account, String password, int type, String clientId, String targetCompId, int hedgeType, Long opUid){
        Logger.info("editTradeAccount 要操作的opUid为[%s]", opUid);
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", id);
        param.put("name", name);
        param.put("account", account);
        param.put("password", password);
        param.put("type", type);
        param.put("clientId", clientId);
        param.put("targetCompId", targetCompId);
        param.put("hedgeType", hedgeType);
        String gson = GsonUtil.createGson().toJson(param);
        Logger.info("修改交易帐号gson=%s", gson);
        long editId = userInfoService.editTradeAccount(param,fetchNtToken(), opUid);
        Map<String,Object> map = Maps.newHashMap();
        map.put("success",editId>0?true:false);
        map.put("id", editId);
        renderJSON(map);
    }

    public static void addSingleTradeAccount(String name, String account, String password, int type, String clientId, String targetCompId, int hedgeType, Long opUid) {
        Logger.info("addSingleTradeAccount 要操作的opUid为[%s]", opUid);
        Map<String, Object> param = Maps.newHashMap();
        param.put("name", name);
        param.put("account", account);
        param.put("password", password);
        param.put("type", type);
        param.put("clientId", clientId);
        param.put("targetCompId", targetCompId);
        param.put("hedgeType", hedgeType);
        String gson = GsonUtil.createGson().toJson(param);
        Logger.info("增加交易帐号gson=%s", gson);
        long id = userInfoService.addTradeAccount(param,fetchNtToken(), opUid);
        Logger.info("增加用户的交易帐号,返回id=%s", id);
        Map<String,Object> map = Maps.newHashMap();
        map.put("success",id>0?true:false);
        map.put("id", id);
        renderJSON(map);
    }

}
