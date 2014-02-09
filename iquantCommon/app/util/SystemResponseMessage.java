package util;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 下午3:29
 * 功能描述: 返回客户端的提示信息统一放在这里
 */
public final class SystemResponseMessage {
    public  static final String SYSTEM_DEFAULT_MSG_RSP="操作成功";
    public  static final String SYSTEM_DEFAULT_ERR_RSP="操作失败了，请重试";
    public static final String ADD_USER_SUCCESS_RSP = "用户添加成功,点击确定跳转到授权页面";
    public static final String DEL_USER_SUCCESS_RSP = "用户删除成功";
    public static final String UPDATE_USER_SUCESS_RSP = "用户修改成功";
    public static final String MODIFY_USER_RSP = "修改成功";

    public static final String USER_DELAY = "延期成功";
    public static final String STRATEGY_UP_RSP = "上架成功";
    public static final String STRATEGY_UP_FAILURE_RSP = "只有已回测策略才能上架";

    public static final String UPLOAD_USER_SUCCESS_RSP = "上传成功，一共上传%d个用户，点击确定跳转到授权页面";
    public static final String UPLOAD_USER_ERROR_RSP = "上传失败，请检查文件格式";
    public static final String AUTHORIZE_SUCCESS_RSP = "角色权限修改成功";
    public static final String ILLEGAL_REQUEST_RSP = "非法请求";

    public static final String STRATEGY_UPLOAD_SUCCESS_RSP = "策略上传成功";
    public static final String STRATEGY_UPLOAD_FAILUER_RSP = "策略上传失败";
    public static final String UPLOAD_FILE_EMPTY_RSP = "上传文件不能为空";
    public static final String UPLOAD_FILE_OUT_OF_SIZE_RSP = "文件过大，只能上传最大为%dM的文件";

    public static final String SERVER_DISABLED_RSP = "无权限";

    public static final String STRATEGY_PIC_FAILURE = "生成图片失败";

    public static final String USER_DELAY_FAILURE = "设置日期必须大于当前日期";

    public static final String DEL_MESSAGE_SUCCESS_RSP = "删除消息成功";
    public static final String DATE_ERROR_RSP = "回测时间设置有误!";
    public static final String DATE_RNAGE_ERROR_RSP = "回测时间设置有误,回测时间区间必需大于3天";

    public static final String STRATEGY_COUNT_BIG_30 = "上传策略总数不能超过30个";


}
