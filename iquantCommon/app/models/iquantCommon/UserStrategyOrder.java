package models.iquantCommon;

import play.db.jpa.Model;

import java.util.Date;

/**
 * 用户策略订阅
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:38
 */
public class UserStrategyOrder extends Model {
    public UserInfo user;

    public StrategyBaseinfo strategy;

    public Date orderSTime; //可以不附值, 插入数据库时会自动生成

    public Date orderETime;

}
