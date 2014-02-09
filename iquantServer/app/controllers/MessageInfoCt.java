package controllers;

import bussiness.user.IMessageInfoService;
import models.iquantCommon.UserMessagesDto;
import play.data.binding.As;
import play.libs.F;
import util.Page;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-8
 * Time: 上午10:27
 */
public class MessageInfoCt extends BaseController {
    @Inject
    static IMessageInfoService messageInfoService;

    /**
     * 获取用户列表
     * @param uid
     * @param orderFlag
     * @param pageNo
     */
    public static void fetchUserMsgList(Long uid, int orderFlag, int pageNo){
        F.T2<List<UserMessagesDto>, Page> t2 = messageInfoService.fetchUserMsgLists(uid, orderFlag, pageNo);
        responseJSON(t2._1, t2._2);
    }

    /**
     * 获取单条消息
     * @param msgId
     */
    public static void fetchMsgInfoById(long msgId){
        UserMessagesDto userMessagesDto = messageInfoService.fetchUserMsg(msgId);
        responseJSON(userMessagesDto);
    }

    /**
     * 更新消息
     * @param msgId
     * @param status
     */
    public static void updateUserMsg(long msgId, int status){
        try {
            messageInfoService.updateUserMsg(msgId, status);
            responseJSON(true);
        } catch (Exception e) {
            responseError(SystemResponseMessage.SYSTEM_DEFAULT_ERR_RSP);
        }
    }

    /**
     * 批量删除消息
     * @param ids
     */
    public static void batchDeleteMsgByIds(@As(",")String[] ids){
        try {
            messageInfoService.bathDeleteMsg(ids);
            responseJSON(true);
        } catch (Exception e) {
            responseError(SystemResponseMessage.SYSTEM_DEFAULT_ERR_RSP);
        }
    }

}
