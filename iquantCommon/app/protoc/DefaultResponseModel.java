package protoc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import util.GsonUtil;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-3-26
 * Time: 下午2:15
 * 功能描述: 响应模型
 */
public class DefaultResponseModel<T> extends ResponseHeader<T> {




    public DefaultResponseModel() {

    }

    public DefaultResponseModel(T data) {
        this.data = data;
    }

    @SerializedName(Protocol.GlobalFieldName.DATA)
    @Expose
    public T data;


    public T getResult() {
        return data;
    }

    public String encode() {
        return GsonUtil.createWithoutNullsDisableHtmlEscaping().toJson(this);
    }


}
