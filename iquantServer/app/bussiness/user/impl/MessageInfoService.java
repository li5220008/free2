package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.user.IMessageInfoService;
import dbutils.SqlLoader;
import models.iquantCommon.UserMessagesDto;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-8
 * Time: 上午10:41
 */
public class MessageInfoService extends BaseService implements IMessageInfoService {
    @Override
    public F.T2<List<UserMessagesDto>, Page> fetchUserMsgLists(long uid, int orderFlag, int pageNo) {
        String sql = SqlLoader.getSqlById("userMsg");
        List<UserMessagesDto> userMsgList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString(), uid);
        if(orderFlag == 1){
            sql+= " order by msgTime desc ";
        }else{
            sql+= " order by msgTime asc ";
        }

        Page page = new Page(total.intValue(), pageNo);
        sql += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if(total > 0){
            userMsgList = qicDbUtil.queryBeanList(sql, UserMessagesDto.class, uid);
        }
        return F.T2(userMsgList, page);
    }

    @Override
    public UserMessagesDto fetchUserMsg(long msgId){
        String sql = SqlLoader.getSqlById("get_msgStatus_byId");
        UserMessagesDto userMessagesDto = qicDbUtil.querySingleBean(sql, UserMessagesDto.class, msgId);
        return userMessagesDto;
    }

    @Override
    public void updateUserMsg(long msgId, int status){
        String sql2 = SqlLoader.getSqlById("update_msgStatus_byId");
        qicDbUtil.update(sql2, status, msgId);
    }

    @Override
    public void bathDeleteMsg(String[] ids){
        String sql = SqlLoader.getSqlById("delMsg");
        Object[][] params = new Object[ids.length][1];
        for(int i = 0; i<ids.length; i ++){
            params[i][0] = ids[i];
        }
        qicDbUtil.batch(sql, params);
    }
}
