package job;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.redis.Redis;
import util.RedisKeys;

import java.util.Set;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-5-15
 * Time: 下午5:45
 * 功能描述:
 */
@OnApplicationStart(async = true)
public class ClearCacheJob extends Job {
    public void doJob(){
        clearPrevilegeFromRedis();
    }
    private void clearPrevilegeFromRedis(){
        String userRoleKeyPattern = RedisKeys.UserFunctionCacheKey.USER_ROLE_PREFIX + "*";
        String roleFunctionKeyPattern = RedisKeys.UserFunctionCacheKey.ROLE_FUNCTION_PREFIX+ "*";
        Set<String> userRoleKeySet = Redis.keys(userRoleKeyPattern);
        Set<String> roleFunctionKeySet = Redis.keys(roleFunctionKeyPattern);

        for(String key : userRoleKeySet){
           Redis.del(new String[]{key});
        }
        for(String key : roleFunctionKeySet){
            Redis.del(new String[]{key});
        }




    }
}
