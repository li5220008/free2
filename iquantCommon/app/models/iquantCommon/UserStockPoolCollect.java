package models.iquantCommon;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户股票池收藏
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午12:11
 */
public class UserStockPoolCollect extends Model {
    public UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    public StockPool stockPool;

    public Date collectTime; //可以不附值, 插入数据库时会自动生成
}