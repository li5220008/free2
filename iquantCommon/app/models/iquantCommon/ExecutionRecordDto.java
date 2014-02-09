package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 创建成交记录dto
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionRecordDto {
    //策略id
    @SerializedName("id")
    @Expose
    public long id;
    //时间
    @SerializedName("sendingtime")
    @Expose
    public Date sendingtime;
    //策略名称
    @SerializedName("name")
    @Expose
    public String name;
    //方向
    @SerializedName("side")
    @Expose
    public String side;
    //交易品种
    @SerializedName("trade_variety")
    @Expose
    public Integer trade_variety;
    //标的代码
    @SerializedName("securityid")
    @Expose
    public String securityid;
    //标的名称
    @SerializedName("symbol")
    @Expose
    public String symbol;
    //最新成交量
    @SerializedName("lastpx")
    @Expose
    public Float lastpx;
    //成交价格
    @SerializedName("avgpx")
    @Expose
    public Float avgpx;
    //交易所
    @SerializedName("securityexchange")
    @Expose
    public String securityexchange;
    //累计成交量
    @Expose
    public Float cumqty;
    //资金账号
    @Expose
    public String account;

    public String isStock() {
        if (securityid.length() == 6 && (securityid.startsWith("002") || securityid.startsWith("000") || securityid.startsWith("300") || securityid.startsWith("200") || securityid.startsWith("900"))) {
            return "1";
        } else if (isCharacter(securityid)) {
            return "2";
        } else {
            return null;
        }
    }

    public boolean isCharacter(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    public String getSide() {
        if (isStock().equals("1")) {
            if ("1".equals(side.trim())) {
                return "买";
            }  else if ("2".equals(side.trim())) {
                return "卖";
            } else {
                return "未定义";
            }
        } else if (isStock().equals("2")){
            if ("1".equals(side.trim())) {
                return "多";
            }  else if ("2".equals(side.trim())) {
                return "空";
            } else {
                return "未定义";
            }
        }  else {
            return "";
        }
    }
}
