package models.iquantCommon;

import play.db.jpa.Model;

import java.util.Date;

/**
 * 用户股票池评论
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午12:14
 */
public class UserStockPoolDiscuss {
    public Long uid;

    public Long spid;

    public Date disTime; //可以不附值, 插入数据库时会自动生成

    public Integer star;
}
