package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 创建策略持仓dto
 * User: panzhiwei
 * Date: 12-11-10
 * Time: 上午11:48
 */
public class StrategyPositionDto {

    //标的名称//策略id
    @SerializedName("id")
    @Expose
    public long id;
    //时间
    @SerializedName("updatetime")
    @Expose
    public Date updatetime;
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
    public int trade_variety;
    //标的代码
    @SerializedName("securityid")
    @Expose
    public String securityid;
    //标的名称
    @SerializedName("symbol")
    @Expose
    public String symbol;
    //交易所
    @SerializedName("securityexchange")
    @Expose
    public String securityexchange;
    //持仓量
    @SerializedName("positionqty")
    @Expose
    public int positionqty;

    //持仓率(QIA)
    @Expose
    public float positions;

    public String isStock() {
        if (securityid.length() == 6 && (securityid.startsWith("002") || securityid.startsWith("000") || securityid.startsWith("300") || securityid.startsWith("200") || securityid.startsWith("900"))) {
            return "1";
        } else if (isCharacter(securityid)) {
            return "2";
        } else {
            return "--";
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
            if (side.equals("1")) {
                return "买";
            } else if (side.equals("2")) {
                return "卖";
            } else {
                return "未定义";
            }
        } else if (isStock().equals("2")){
            if (side.equals("1")) {
                return "多";
            } else if (side.equals("2")) {
                return "空";
            } else {
                return "未定义";
            }
        } else {
            return "";
        }

    }


}
