package util;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:27
 * 功能描述:
 */
public class FunctionAuthManager {
    public final static int ALLOW_MANAGE_ALL_STRATEGY_FUN = 26;
    public final static int ONLY_MANAGE_ALL_STRATEGY_FUN = 27;
    public static boolean auth(long fid, long uid) {
        //加上一个特叔权限 27的时候可以管理所有策略
        if(fid == 26){
            System.out.println("=====================");
        }
        if (fid == 23) {
            return SimpleFunctionAuth.getInstance().hasPrivilege(ONLY_MANAGE_ALL_STRATEGY_FUN, uid) || SimpleFunctionAuth.getInstance().hasPrivilege(ALLOW_MANAGE_ALL_STRATEGY_FUN, uid);
        } else {
            return SimpleFunctionAuth.getInstance().hasPrivilege(fid, uid);
        }
    }
}
