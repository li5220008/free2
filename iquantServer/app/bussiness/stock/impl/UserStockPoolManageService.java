package bussiness.stock.impl;

import bussiness.common.impl.BaseService;
import bussiness.stock.IUserStockPoolManageService;
import dbutils.SqlLoader;
import models.iquantCommon.UserStockPoolCollect;
import play.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: liuhongjiang
 * Date: 13-7-26
 * Time: 下午4:35
 */
public class UserStockPoolManageService extends BaseService implements IUserStockPoolManageService {

    /**
     * 保存该用户对该策略的评论
     * @param star
     * @param uid
     * @param spid
     */
    public  boolean saveDiscuss(int star,Long uid,Long spid){

        if(star==0||uid==0||spid==0){
            return false;
        }
        String is_stock_pool_comment_sql = SqlLoader.getSqlById("is_stock_pool_comment");
        String is_user_comment_sql = SqlLoader.getSqlById("is_user_comment");
        String updateStockPoolCommentSql = SqlLoader.getSqlById("update_stock_pool_comment1");
        String insertStockPoolCommentSql = SqlLoader.getSqlById("insert_stock_pool_comment");
        String insertUserCommentSql = SqlLoader.getSqlById("insert_user_comment");

        Map<String, Object>  user_map = qicDbUtil.querySingleMap(is_user_comment_sql, uid, spid);
        if(user_map==null||user_map.size()==0){
            Map<String, Object>  stock_pool_map = qicDbUtil.querySingleMap(is_stock_pool_comment_sql, spid);
            if(stock_pool_map==null||stock_pool_map.size()==0){
                qicDbUtil.update(insertStockPoolCommentSql, spid, star, 1);
            } else{
                qicDbUtil.update(updateStockPoolCommentSql, star, 1, spid);
            }

            if(Logger.isDebugEnabled()){
                Logger.debug(is_user_comment_sql);
            }
            qicDbUtil.update(insertUserCommentSql, uid, spid, star);
            return true;

        } else{
            return false;
        }


    }


    /**
     * 判断该用户是否已经评论的该股票池
     * @param spid
     * @param uid
     */
    public  Integer judge(String spid,Long uid){
        int result=0;
        String is_user_comment_sql = SqlLoader.getSqlById("is_user_comment");
        Map<String, Object>  user_map = qicDbUtil.querySingleMap(is_user_comment_sql, uid, spid);
        if(user_map!=null&&user_map.size()>0){
            result=1;
        }
        return result;
    }

    /**
     * 从股票池列表中查询已评论的股票池
     *
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public Set<Integer> queryStockPoolDiscussMap( Long uid) {
        String sql = "SELECT spid as spid FROM user_stock_pool_discuss  WHERE uid =" + uid;
        List<Map<String, Object>> stockmapList = qicDbUtil.queryMapList(sql);
        Set<Integer> result = new HashSet<Integer>();
        if(stockmapList!=null){
            for (Map<String, Object> map : stockmapList) {
                result.add(Integer.parseInt(map.get("spid").toString()));
            }
        }
        return result;
    }
    /**
     * 股票池收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public void stockpooladdcollect(long uid, long spid) throws Exception {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` a WHERE a.`uid`=? AND a.`spid`=?";
        UserStockPoolCollect userStockPoolCollect = qicDbUtil.querySingleBean(sql, UserStockPoolCollect.class, uid, spid);
        if (userStockPoolCollect == null) {
            String sql2 = "INSERT INTO qic_db.`user_stock_pool_collect`(uid,spid) VALUES(?,?)";
            qicDbUtil.update(sql2, uid, spid);

            String sql4 = "SELECT * FROM stock_pool_ext a WHERE spid = ?";//查看stock_pool_ext是否有这个股票池的数据
            Map<String, Object> map = qicDbUtil.querySingleMap(sql4, spid);
            if (map == null) {
                String sql5 = "INSERT INTO qic_db.`stock_pool_ext` (collect_count, spid) VALUES (?, ?)";
                qicDbUtil.update(sql5, 1, spid);
            } else {
                String sql3 = "UPDATE qic_db.`stock_pool_ext` a SET a.`collect_count` = a.`collect_count` + 1 WHERE a.`spid`=?";
                qicDbUtil.update(sql3, spid);
            }
        } else {
            throw new Exception("已经收藏股票池.");
        }

    }

    /**
     * 股票池取消收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public void stockpooldeletecollect(long uid, long spid) throws Exception {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` a WHERE a.`uid`=? AND a.`spid`=?";
        UserStockPoolCollect userStockPoolCollect = qicDbUtil.querySingleBean(sql, UserStockPoolCollect.class, uid, spid);
        if (userStockPoolCollect != null) {
            String sql2 = "DELETE  FROM qic_db.`user_stock_pool_collect` WHERE uid=? AND spid=?";
            qicDbUtil.update(sql2, uid, spid);

            String sql3 = "UPDATE qic_db.`stock_pool_ext` a SET a.`collect_count` = a.`collect_count` - 1 WHERE a.`spid`=?";
            qicDbUtil.update(sql3, spid);
        } else {
            throw new Exception("已经取消收藏.");
        }


    }
}
