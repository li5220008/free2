package models.iquantCommon;

import play.db.jpa.Model;

import java.util.Date;

/**
 * 用户策略收藏
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:24
 */
public class UserStrategyCollect {

    public long id;

    public long uid;

    public StrategyBaseinfo strategy;

    public Date collectTime; //可以不附值, 插入数据库时会自动生成
}
