package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 用户自定义模板
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:44
 */
public class UserTemplate {

    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    public Integer type; //1. 自定义策略查询 2. 自定义股票池查询
    @Expose
    public String content; //保存的内容,只提供存储,里面的内容自定义
    @Expose
    public long uid;

/*    public String getContent() {
        return new String(Base64.decodeBase64(content));
    }

    public void setContent(String content) {
        this.content = new String(Base64.encodeBase64(content.getBytes()));
    }*/

    public UserTemplate() {

    }

    public UserTemplate(String name, TemplateType type, long uid, String content) {
        this.name = name;
        this.type = type.getValue();
        this.uid = uid;
        this.content= content;
    }

    @Override
    public String toString() {
        return "UserTemplate{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public static enum TemplateType {
        STRATEGYTEMPLATE,
        STOCKPOOLTEMPLATE;

        public int getValue() {
            switch (this) {
                case STRATEGYTEMPLATE:
                    return 1;
                case STOCKPOOLTEMPLATE:
                    return 2;
                default:
                    return -1;
            }
        }

        public TemplateType getTemplateByValue(int type) {
            switch (type) {
                case 1:
                    return STRATEGYTEMPLATE;
                case 2:
                    return STOCKPOOLTEMPLATE;
                default:
                    return null;
            }
        }
    }
}
