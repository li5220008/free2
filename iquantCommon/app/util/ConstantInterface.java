package util;

/**
 * 常量接口, 描述系统里的常量
 * User: wenzhihong
 * Date: 12-5-18
 * Time: 下午1:58
 */
public interface ConstantInterface {
    //<editor-fold desc="定义校验信息的常量名, 方便直接引用">
    //字段必填
    public static final String FIELD_REQUIRED = "validation.required";
    //用户名
    public static final String USER_NAME_REQUIRED = "valid.username.required";
    //密码
    public static final String PWD_REQUIRED = "valid.pwd.required";
    //mac地址
    public static final String MAC_REQUIRED = "valid.mac.required";
    //产品id
    public static final String PID_REQUIRED = "valid.pid.required";
    //名称
    public static final String NAME_REQUIRED = "valid.name.required";
    //id
    public static final String ID_REQUIRED = "valid.id.required";
    //组id
    public static final String GROUPID_REQUIRED = "valid.groupId.required";
    //证券代码
    public static final String SECCODE_REQUIRED = "valid.secCode.required";
    //token
    public static final String TOKEN_REQUIRED = "valid.token.required";
    //自定义指标内容
    public static final String INDEX_CONTENT_REQUIRED = "valid.indexContent.required";

    public static final String CATEGORY_REQUIRED = "valid.category.required";

    //json格式错误
    public static final String JSON_FORMAT_ERROR = "valid.json.error";

    //</editor-fold>

}
