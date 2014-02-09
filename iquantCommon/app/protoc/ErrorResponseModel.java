package protoc;

import util.GsonUtil;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-3-28
 * Time: 下午3:05
 * 功能描述:
 */
public class ErrorResponseModel extends ResponseHeader {


    public ErrorResponseModel() {
        this.status = Protocol.STATUS_FAILRE;
    }
    public ErrorResponseModel(String message) {
        this.status = Protocol.STATUS_FAILRE;
        this.message = message;
    }


    public String encode() {
        return GsonUtil.createWithoutNulls().toJson(this);
    }


}
