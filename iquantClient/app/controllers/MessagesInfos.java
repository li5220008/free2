package controllers;

import bussiness.MessagesInfosService;
import models.iquantCommon.UserMessagesDto;
import play.libs.F;
import util.Page;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-2
 * Time: 下午5:37
 */
public class MessagesInfos extends BasePlayControllerSupport{
    @Inject
    static MessagesInfosService messagesInfosService;
    /**
     * 用户消息列表
     * @param pageNo
     */
    public static void msgList(int pageNo,int orderFlag,Long uid){
        F.T2<List<UserMessagesDto>,Page> t2 = messagesInfosService.msgList(uid, orderFlag, pageNo);
        List<UserMessagesDto> userMsgList= t2._1;
        Page page = t2._2;
        render(userMsgList,orderFlag, page);
    }

    /**
     * 更新消息状态
     * @param msgId
     */
    public static void updateMsgStatus(Long msgId){
        messagesInfosService.updateMsgStatus(msgId);
    }

    /**
     * 删除消息
     * @param ids
     * @param uid
     */
    public static void delMsg(String[] ids,Long uid){
        if(messagesInfosService.delMsg(ids)){
            setMessage(SystemResponseMessage.DEL_MESSAGE_SUCCESS_RSP);
        }else{
            setMessage(SystemResponseMessage.SYSTEM_DEFAULT_ERR_RSP);
        }
        renderJSON(getSampleResponseMap());
    }
}
