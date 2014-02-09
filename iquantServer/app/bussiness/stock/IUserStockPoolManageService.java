package bussiness.stock;

import java.util.Set;

/**
 * User: liuhongjiang
 * Date: 13-7-26
 * Time: 下午4:33
 */
public interface IUserStockPoolManageService {
    /**
     * 从股票池列表中查询已评论的股票池
     *
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public Set<Integer> queryStockPoolDiscussMap( Long uid) ;
    /**
     * 保存该用户对该策略的评论
     * @param star
     * @param uid
     * @param spid
     */
    public  boolean saveDiscuss(int star,Long uid,Long spid);


    /**
     * 判断该用户是否已经评论该股票池
     * @param spid
     * @param uid
     */
    public  Integer judge(String spid,Long uid);
    /**
     * 股票池收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public  void stockpooladdcollect(long uid, long spid)throws Exception;

    /**
     * 股票池取消收藏
     *
     * @param uid        用户id
     * @param spid       股票池id
     */
    public  void stockpooldeletecollect(long uid, long spid) throws Exception ;
}
