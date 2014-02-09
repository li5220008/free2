package models.iquantCommon;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import java.util.Date;

/**
 * 用户策略评论
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:40
 */
public class UserStrategyDiscuss {
    @Expose
    public long id;

    @Expose
    public long uid;

    @Expose
    public long stid;

    @Expose
    public Date disTime;//可以不附值, 插入数据库时会自动生成

    @Expose
    public String content; //内容

    @Expose
    public Integer star; //星级

    @Expose
    public String userName;//评论人

}
