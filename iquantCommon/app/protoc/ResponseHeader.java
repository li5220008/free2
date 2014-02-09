package protoc;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import util.GsonUtil;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-3-28
 * Time: 下午2:08
 * 功能描述:
 */
public class ResponseHeader<T> {
    @SerializedName( Protocol.GlobalFieldName.STATUS)
    @Expose
    public int status;
    @SerializedName( Protocol.GlobalFieldName.MESSAGE)
    @Expose
    public String message;
    @SerializedName( Protocol.GlobalFieldName.ERRORS)
    @Expose
    public List<ErrorEntry> errors;

    @SerializedName(Protocol.GlobalFieldName.TOTAL)
    @Expose
    public Integer total;
    @SerializedName(Protocol.GlobalFieldName.PAGESIZE)
    @Expose
    public Integer size;
    @SerializedName(Protocol.GlobalFieldName.PAGENO)
    @Expose
    public Integer pageNo;

    public void addError(String key,String msg){
       if(errors == null){
           errors = Lists.newArrayList() ;
       }
        errors.add(new ErrorEntry(key,msg));
    }

    public String encode(){
        return null;
    }
    public String decode(){
        return null;
    }

    public static class ErrorEntry{
        @SerializedName( Protocol.GlobalFieldName.FIELD)
        @Expose
       public String key;
        @SerializedName( Protocol.GlobalFieldName.MESSAGE)
        @Expose
        public String msg;
       public  ErrorEntry(String key,String value){
           this.key = key;
           this.msg = value;
       }

    }

    public String toString(){
        return GsonUtil.createWithoutNulls().toJson(this);
    }


}
