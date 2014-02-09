package bussiness;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.iquantCommon.FunctionInfo;
import models.iquantCommon.UserRoleDto;
import play.Logger;
import play.modules.redis.Redis;
import protoc.URILib;
import util.RedisKeys;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-5-13
 * Time: 下午3:21
 * 功能描述:
 */
public class UserInfoBaseService extends BasicService {
    /**
     * 根据用户ID 查询用户的角色信息
     * @param uid
     * @return
     */
    public  static List<UserRoleDto> findUserRole(long uid){
        return remoteRequestService.getList(URILib.FETCH_ROLEINFO_BY_UID,UserRoleDto.class,uid).data ;
    }
    public static  List<FunctionInfo> findRoleFunctionInfo(long rid){
        return  remoteRequestService.getList(URILib.FETCH_FUNCTIONINFO_BY_ROLEID,FunctionInfo.class,rid).data;
    }
    public static List<FunctionInfo> getUserFunctionInfo(long uid){
        List<UserRoleDto> roleDtoList = PrevilegeRemoteCache.getUserRoles(uid) ;
        List<FunctionInfo> resultList = Lists.newArrayList();
        for(UserRoleDto dto : roleDtoList){
            List<FunctionInfo> functionInfoList = PrevilegeRemoteCache.getRoleFunctions(dto.rid);
            resultList.addAll(functionInfoList);
        }

        return resultList;
    }
    public static void deleteUserFromCache(long uid){
        Logger.warn("删除用户角色缓存:" + uid);
        PrevilegeRemoteCache.deleteUserRoleCache(uid);
    }
    public static void deleteRoleFromCache(long rid){
        Logger.warn("删除角色权限缓存:" + rid);
        PrevilegeRemoteCache.deleteRoleFunctionCache(rid);
    }
    //本地缓存
    private  static class PrevilegeLocalCache{
        public static LoadingCache<String,List<UserRoleDto>> userRoleCache = null ;
        public static LoadingCache<String,List<FunctionInfo> > roleFunctionCache = null ;
        static{
            userRoleCache  =  CacheBuilder.newBuilder().build(new CacheLoader<String,List<UserRoleDto>>(){
                public List<UserRoleDto> load(String uid){
                    List<UserRoleDto> list = findUserRole(Long.valueOf(uid));
                    return list;
                }
            });
            roleFunctionCache  =  CacheBuilder.newBuilder().build(new CacheLoader<String,List<FunctionInfo>>(){
                public List<FunctionInfo> load(String roleId){
                    List<FunctionInfo> list = findRoleFunctionInfo(Long.valueOf(roleId));
                    return list;
                }
            });
        }
        public static void deleteUserRoleCache(long uid){
            userRoleCache.invalidate(String.valueOf(uid));
        }
        public static void deleteRoleFunctionCache(long roleId){
            roleFunctionCache.invalidate(String.valueOf(roleId));
        }
        public static List<FunctionInfo> getRoleFunctions(long roleId){
            List<FunctionInfo> list = null;
            try {
                list =roleFunctionCache.get(String.valueOf(roleId));
            } catch (ExecutionException e) {
                e.printStackTrace();
                list = Lists.newArrayList();
                Logger.error(e.getMessage(),e);
            }
            return list;
        }
        public static List<UserRoleDto> getUserRoles(long uid){
            List<UserRoleDto> list = null;
            try {
                list =userRoleCache.get(String.valueOf(uid));
            } catch (ExecutionException e) {
                e.printStackTrace();
                list = Lists.newArrayList();
                Logger.error(e.getMessage(), e);
            }
            return list;
        }
    }
    //redis缓存
    private  static class PrevilegeRemoteCache{
        public static LoadingCache<String,List<UserRoleDto>> userRoleCache = null ;
        public static LoadingCache<String,List<FunctionInfo> > roleFunctionCache = null ;
        private static   Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        static{
            userRoleCache  =  CacheBuilder.newBuilder().build(new CacheLoader<String,List<UserRoleDto>>(){
                public List<UserRoleDto> load(String uid){
                    String result = Redis.get(RedisKeys.UserFunctionCacheKey.USER_ROLE_PREFIX + uid);
                    List<UserRoleDto> list = null;
                    if(result == null){
                        Logger.info("miss 用户-->角色");
                        list = findUserRole(Long.valueOf(uid));
                        Redis.set(RedisKeys.UserFunctionCacheKey.USER_ROLE_PREFIX + uid, gson.toJson(list));
                    }else{
                        Logger.info("hit 用户-->角色");
                        list = gson.fromJson(result,new TypeToken<List<UserRoleDto>>() {
                        }.getType());
                    }

                    return list;
                }
            });
            roleFunctionCache  =  CacheBuilder.newBuilder().build(new CacheLoader<String,List<FunctionInfo>>(){
                public List<FunctionInfo> load(String roleId){
                    String result = Redis.get(RedisKeys.UserFunctionCacheKey.ROLE_FUNCTION_PREFIX + roleId);
                    List<FunctionInfo> list = null;
                    if(result == null){
                          Logger.info("miss 角色-->权限");
                          list = findRoleFunctionInfo(Long.valueOf(roleId));
                          Redis.set(RedisKeys.UserFunctionCacheKey.ROLE_FUNCTION_PREFIX + roleId, gson.toJson(list));
                    }else{
                        Logger.info("hit 角色-->权限");
                        list = gson.fromJson(result,new TypeToken<List<FunctionInfo>>() {
                        }.getType());
                    }
                    return list;
                }
            });
        }
        public static void deleteUserRoleCache(long uid){
            Redis.del(new String[]{RedisKeys.UserFunctionCacheKey.USER_ROLE_PREFIX + uid});
           // userRoleCache.invalidate(String.valueOf(uid));
        }
        public static void deleteRoleFunctionCache(long roleId){
            Redis.del(new String[]{RedisKeys.UserFunctionCacheKey.ROLE_FUNCTION_PREFIX + roleId});
            //roleFunctionCache.invalidate(String.valueOf(roleId));
        }
        public static List<FunctionInfo> getRoleFunctions(long roleId){
            List<FunctionInfo> list = null;
            try {
                list =roleFunctionCache.get(String.valueOf(roleId));
                //清掉本机的缓存
                roleFunctionCache.invalidate(String.valueOf(roleId));
            } catch (ExecutionException e) {
                e.printStackTrace();
                list = Lists.newArrayList();
                Logger.error(e.getMessage(),e);
            }
            return list;
        }
        public static List<UserRoleDto> getUserRoles(long uid){
            List<UserRoleDto> list = null;
            try {
                list =userRoleCache.get(String.valueOf(uid));
                //清掉本机的缓存
                userRoleCache.invalidate(String.valueOf(uid));
            } catch (ExecutionException e) {
                e.printStackTrace();
                list = Lists.newArrayList();
                Logger.error(e.getMessage(), e);
            }
            return list;
        }
    }
}
