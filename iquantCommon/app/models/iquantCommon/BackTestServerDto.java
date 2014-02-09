package models.iquantCommon;


import com.google.gson.annotations.Expose;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:51
 * 功能描述: 回测服务器地址列表
 */
public class BackTestServerDto extends BaseDtoSupport {

    /**
     * 回测服务器ip
     */
    @Expose
    public String ip;
    /**
     * 状态
     */
    @Expose
    public int status;
    @Expose
    public String name;

    /**
     * 服服务器类型
     */
    @Expose
    public int type;

    public enum ServerStatusEnum{

        VALID{//服务器有效
          public int getValue(){
              return 0;
          }
        },
        DISABLED{//服务器被禁用
            public int getValue(){
                return -1;
            }
        };
        public abstract int  getValue();
    }

    public static enum ServerTypeEnum{

        BACKTEST{//回测服务器
            public int getValue(){
                return 0;
            }
        },
        AGENT{//代理服务器 agentIP 运行
            public int getValue(){
                return 1;
            }
        },
        SIMULATE{//实时模拟服务器 agentIP
            public int getValue(){
                return 2;
            }
        };
        public ServerTypeEnum getByIntValue(int value){
            switch (value){
                case 0:
                    return BACKTEST;
                case 1:
                    return AGENT;
                case 2:
                    return SIMULATE;
                default:
                    return null;
            }
        }
        public abstract int  getValue();
    }
}
