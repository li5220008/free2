package bussiness;

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.iquantCommon.UserMessagesDto;
import play.libs.F;
import protoc.Protocol;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.Page;

import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-2
 * Time: 下午5:56
 */
public class MessagesInfosService extends BasicService {
    /**
     * 消息列表
     * @param pageNo
     * @return
     */
    public static F.T2<List<UserMessagesDto>,Page> msgList(Long uid, int orderFlag, int pageNo){
        List<UserMessagesDto> userMsgList = null;
        ActionResult<List<UserMessagesDto>> result = remoteRequestService.getList(URILib.FETCH_USER_MSG_LISTS, UserMessagesDto.class,uid,pageNo,orderFlag);
        Page page = new Page(result.header.total.intValue(), pageNo);
        userMsgList = result.data;
        return F.T2(userMsgList, page);
    }

    /**
     * 更新消息状态
     * @param msgId
     */
    public static boolean updateMsgStatus(Long msgId){
        boolean success = false;
        UserMessagesDto userMessagesDto = remoteRequestService.getBean(URILib.FETCH_USER_MSG, UserMessagesDto.class, msgId).data;
        if(userMessagesDto.status == UserMessagesDto.MessagesStatus.UNREAD.value){
            userMessagesDto.status = UserMessagesDto.MessagesStatus.READ.value;
        }
        int status = userMessagesDto.status;
        JsonElement json = remoteRequestService.getJson(URILib.UPDATE_USER_MSG, msgId,status);
        if(json.isJsonObject()){
            JsonObject jsonObject = json.getAsJsonObject();
            success = jsonObject.get(Protocol.GlobalFieldName.STATUS).getAsInt() == 0 ? true : false;
        }
        return success;
    }

    /**
     * 删除消息
     * @param idArr
     */
    public static boolean delMsg(String[] idArr){
        boolean success = false;
        Joiner joiner = Joiner.on(",");
        String ids = joiner.join(idArr);
        JsonElement jsonElement = remoteRequestService.getJson(URILib.BATCH_DELETE_MSG, ids);
        if(jsonElement.isJsonObject()){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            success = jsonObject.get(Protocol.GlobalFieldName.STATUS).getAsInt() == 0 ? true : false;
        }
        return success;
    }

}
