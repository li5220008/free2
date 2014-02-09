package bussiness.user;

import models.iquantCommon.UserMessagesDto;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-8
 * Time: 上午10:29
 */
public interface IMessageInfoService {
    /**
     * 获取用户消息列表
     * @param uid 用户Id
     * @param orderFlag 排序标识
     * @param pageNo 页码
     * @return
     */
    public F.T2<List<UserMessagesDto>, Page> fetchUserMsgLists (long uid, int orderFlag, int pageNo);

    /**
     * 根据消息Id获取单条消息
     * @param msgId
     * @return
     */
    public UserMessagesDto fetchUserMsg(long msgId);

    /**
     * 更新消息状态
     * @param msgId
     * @param status
     */
    public void updateUserMsg(long msgId, int status);

    /**
     * 批量删除消息
     * @param ids
     */
    public void bathDeleteMsg(String[]  ids);

}
