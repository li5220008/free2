package models.iquantCommon;

import com.google.gson.annotations.SerializedName;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-5-6
 * Time: 上午10:43
 * 功能描述:
 */
public class QiaBackTestResultDto {
    @SerializedName("Result")
    public boolean result;
    @SerializedName("Message")
    public String message;
    public ResultData data;
    @SerializedName("ServerId")
    public int serverId;
    public class ResultData{
        @SerializedName("Count")
        public int count;
        @SerializedName("EngineStatus")
        public int engineStatus;
        @SerializedName("ErrorCount")
        public int errorCount;
        @SerializedName("FinishCount")
        public int finishCount;

    }

}
