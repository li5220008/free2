package bussiness;

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.iquantCommon.FunctionInfoDto;
import models.iquantCommon.RoleInfoDto;
import models.iquantCommon.TradeAccountDto;
import models.iquantCommon.UserInfoDto;
import play.Logger;
import protoc.HttpBody;
import protoc.Protocol;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:46
 * 功能描述: 用户信息相关业务逻辑处理
 */
public class UserInfoService extends UserInfoBaseService {
    /**
     * 根据用户ID查询用户相关信息
     * @param uid 用户ID
     * @return 用户相关信息
     *
     */
    public static UserInfoDto findUserInfoById(long uid){
        //1. 查询用户基本信息
        UserInfoDto userBaseInfo = remoteRequestService.getBean(URILib.FETCH_USERINFO_BY_UID,UserInfoDto.class,uid).data;
        return userBaseInfo;
    }

    /**
     * 根据用户ID查询 用户的菜单
     * @param uid
     * @return
     */
    public static List<FunctionInfoDto> findUserFunctionInfoById(long uid){
        return remoteRequestService.getList(URILib.FETCH_USERMENU_BY_UID, FunctionInfoDto.class, uid).data;
    }

    /**
     * 根据用户ID 查询用户的角色信息
     * @param uid
     * @return
     */
    public  static List<RoleInfoDto> findUserRoleInfoById(long uid){
        return  remoteRequestService.getList(URILib.FETCH_ROLEINFO_BY_UID,RoleInfoDto.class,uid).data;
    }


    /**
     * 修改用户基本信息
     * @param userInfoDto
     * @return
     */
    public static boolean updateUserInfo(UserInfoDto userInfoDto){
            String json = GsonUtil.createWithoutNulls().toJson(userInfoDto);
            HttpBody httpBody = new HttpBody();
            httpBody.body = json;
            return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.UPDATE_USERINFO,httpBody).data);
    }

    /**
     * 判断当前节点是否在用户的权限列表中
     * @param list
     * @param id
     * @return
     */
    private static boolean isExist(List<FunctionInfoDto> list,long id){
        if(id == FunctionService.TREE_ROOT_ID){
            return true;
        }else{
            for(FunctionInfoDto tmp : list){
                if(tmp.id == id){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除不在用户权限列表中的菜单节点
     * @param subList
     * @param functionInfoDtoList
     * @deprecated  算法有问题 不用了 以后有时间再解决
     */
    private static void filterUserTreeFromSystemTree(List<FunctionInfoDto> subList,List<FunctionInfoDto> functionInfoDtoList )
    {

        for(int i = 0;subList!=null&& i<subList.size();){
            boolean  isExist = isExist(functionInfoDtoList,subList.get(i).id);
            if(!isExist){
                //找到没有权限的节点从树中移掉
                i=0;//之前算法有问题 ，去掉list中的一个元素之后要重新归零 2012-12-10
                subList.remove(i);
            }else{
                //递归判断子节点是否有权限
                filterUserTreeFromSystemTree(subList.get(i).subs, functionInfoDtoList);
                ++i;
            }
        }

    }

    /**
     * 根据账号查找用户(账号在系统中是唯一的)
     * @param account
     * @return
     */
    public static UserInfoDto findUserByAccount(String account){
        return remoteRequestService.getBean(URILib.FETCH_USER_WITH_ACCOUNT,UserInfoDto.class,account).data;
    }

    /**
     * 根据email查找用户(email在系统中是唯一的)
     * @param email
     * @return
     */
    public static UserInfoDto findUserByEmail(String email){

        return remoteRequestService.getBean(URILib.FETCH_USER_BY_EMAIL,UserInfoDto.class,email).data;
    }


    /**
     * 新建用户
     * @param userInfo
     * @return
     */
    public static List<Long> addUser(UserInfoDto userInfo) {

        List<UserInfoDto> userInfoDtos = new ArrayList<UserInfoDto>();
        userInfoDtos.add(userInfo);
        List<Long> idlist = null;
        try {
            idlist = addUserBatch(userInfoDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idlist;
        //QicDbUtil.updateQicDB(addusersql, userInfo.name, userInfo.account, userInfo.password, userInfo.phone, userInfo.email, userInfo.idcard, userInfo.saleDep.id, userInfo.capitalAccount, userInfo.address, userInfo.post, sdate, edate,UserInfoDto.UserStatus.WITHOUTACTIVITY.value);
    }

    /**
     * 删除用户
     * @param strIds      删除用户id数组s
     */
    public static void delUser(String[] strIds) {
        Joiner joiner = Joiner.on(",");
        String ids = joiner.join(strIds);
        remoteRequestService.getSingleValue(URILib.BATCH_DELETE_USER,ids);
    }


    /**
     * 用户状态修改
     * @param strIds        用户id数组
     * @param status     修改状态
     */
    public static void userStateModify(String[] strIds,int status){
        Joiner joiner = Joiner.on(",");
        String ids = joiner.join(strIds);
        remoteRequestService.getSingleValue(URILib.BATCH_UPDATE_USER_STATUS,ids,status);
    }


    /**
     * 批量添加用户 insert ignore into tb......
     * @param userInfos
     * @return  新增的用户数量
     */
    public static List<Long> addUserBatch(List<UserInfoDto> userInfos)throws Exception{
        if(null == userInfos || userInfos.size()==0){
            return new ArrayList<Long>(0);
        }
        String json = GsonUtil.createWithoutNulls().toJson(userInfos);
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        List<Long> keys = new ArrayList<Long>(userInfos.size());
        List<String> strKeys = remoteRequestService.getListWithoutField(URILib.ADD_USER_BATCH,httpBody).data;
        //取得自动生成的主键值的结果集
        if(strKeys!=null&&strKeys.size()>0){
            for(String key :strKeys){
                keys.add(Long.parseLong(key));
                Logger.info(key);
            }
        }
        return keys;
    }

    /**
     * 到期用户延期
     * @param ids
     * @param delaydate
     */
    public static void userdelay(String[] ids,String delaydate){
        String strId = "";
        for (int i = 0; i < ids.length; i++) {
            if(i == ids.length - 1){
                strId += ids[i];
            }
            else{
                strId += ids[i] +",";
            }

        }
        remoteRequestService.getSingleValue(URILib.BATCH_UPDATE_USER_DELAYDATE,strId,delaydate);
    }

   public  static UserInfoDto findUserByEmailExcludeSelf(String newEmail,String selfEmail){
       UserInfoDto userInfoDto = null;
       if(newEmail!=null && !newEmail.equals(selfEmail)){
           String email = newEmail;
           userInfoDto = remoteRequestService.getBean(URILib.FETCH_USER_BY_EMAIL,UserInfoDto.class,email).data;
       }
       return userInfoDto;
   }
    public  static UserInfoDto findUserByAccountExcludeSelf(String newAccount,String selfAccount){
        UserInfoDto userInfoDto = null;
        if(newAccount!=null && !newAccount.equals(selfAccount)){
            String account =  newAccount;
            userInfoDto = remoteRequestService.getBean(URILib.FETCH_USER_WITH_ACCOUNT,UserInfoDto.class,account).data;
        }
        return userInfoDto;
    }
    public static long addTradeAccount(Map<String,Object> map,String token, Long opUid){
      long newAccountId = -1;
       try{
           ActionResult<String> result = remoteRequestService.getSingleFieldValue(URILib.ADD_USER_ACCOUNT, "id", new HttpBody(map), token, opUid);
           if(result.header.status == 0){
               newAccountId = Long.valueOf(result.data);
           }
       }catch (Exception e){
             Logger.error("添加账号失败",e);
       }
       return newAccountId;

    }
    public static long delTradeAccount(long id,String token, Long opUid){
        long returnId = -1;
        try{
            ActionResult<String> result = remoteRequestService.getSingleFieldValue(URILib.DEL_USER_ACCOUNT,"id",id,token, opUid);
            if(result.header.status == 0){
                returnId = Long.valueOf(result.data);
            }
        }catch (Exception e){
            Logger.error("删除账号失败",e);
        }
        return returnId;
    }
    public static long editTradeAccount(Map<String,Object> map,String token, Long opUid){
        long returnId = -1;
        try{
            ActionResult<String> result = remoteRequestService.getSingleFieldValue(URILib.EDIT_USER_ACCOUNT,"id",new HttpBody(map),token, opUid);
            if(result.header.status == 0){
                returnId = Long.valueOf(result.data);
            }
        }catch (Exception e){
            Logger.error("修改账号失败",e);
        }
        return returnId;
    }
    public static List<TradeAccountDto> findTradeAccount(String token, Long opUid){
        List<TradeAccountDto> list = remoteRequestService.getList(URILib.FETCH_USER_ACCOUNT, TradeAccountDto.class, token, opUid).data;
        return list;
    }

    public static boolean verifyTradeAccountNotExist(String accountName, long opUid, String token){
        boolean success = false;
        JsonElement jsonElement = remoteRequestService.getJson(URILib.Verify_ACCOUNT_UNIQUE,accountName,opUid,token);
        if(jsonElement.isJsonObject()){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            success = jsonObject.get("data").getAsBoolean() ? false : true;
        }
        return success;
    }
}
