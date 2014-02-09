package models.iquantCommon.JobDto;

import java.util.Date;

/**
 * desc: 虚拟产品交易账号对应表
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-30
 * Time: 下午4:05
 */
public class VirtualProductTradeAccountDto {
    public long id;
    public long productId;
    public long accountId;
    public long createUid;
    public Date ctime;
    public Date utime;
    public int  status;

    @Override
    public String toString() {
        return "VirtualProductTradeAccountDto{" +
                "id=" + id +
                ", productId=" + productId +
                ", accountId=" + accountId +
                ", createUid=" + createUid +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", status=" + status +
                '}';
    }
}
