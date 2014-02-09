package util;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 下午3:25
 * 功能描述: 系统的一些常量配置放在这里
 */
public final class Constants {


    public static final String USER_SDATE = "1911-10-10";
    public static final String USER_EDATE = "2112-12-12";
    /**
     * 用户信息Excel模板存目录,在配置中对应的key
     */
   public  static final String USER_EXCEL_TEMPLATE_KEY = "user.excel.template.dir";
    /**
     * excel文件上传临时保存目录
     */
   public static final String USER_EXCEL_UPLOAD_TEMP_DIR = "user.excel.upload.tmp.dir";
    /**
     * excel文件上传正式目录
     */
   public static final String USER_EXCEL_UPLOAD_OFFICIAL_DIR = "user.excel.upload.official.dir";
    /**
     * 策略文件上传临时保存目录
     */
    public static final String STRATEGY_UPLOAD_TEMP_DIR = "strategy.upload.temp.dir";
    /**
     * 策略文件上传正式目录
     */
    public static final String STRATEGY_UPLOAD_OFFICIAL_DIR = "strategy.upload.official.dir";

    /**
     * iquant策略文件上传的目录
     */
    public static final String iquantUploadDir = "iquant.strategy.upload.dir";

    /**
     * 第三方加载策略文件的基路径
     */
    public static final String OTHERS_LOAD_STRATEGY_BASE_DIR = "others.load.strategy.base.dir";
    /**
     * 策略下架模板 key值
     */
    public static final String STRATEGY_DOWN_TEMPLATE_KEY = "strategyDownMsg";


    //文件限制
    public static final long MAX_SIZE_OF_ZIP_STRATEGY_FILE = 100*1024*1024;//20M

    //生成指定策略小图片和画图数据
    public static final String show_createOneStrategyPic_path = "show.createOneStrategyPic.path";

    //发信箱、名称、主题
    public static final String EMAIL_SENDER = "iquant.email.sender";

    public static final String EMALI_NAME = "iquant.email.name";

    public static final String EMALI_TITLE = "iquant.email.title";


    //注册和激活发邮件需要用到的常量
    public static final int REGISTER_USER =1;
    public static final int ACTIVATE_USER =2;

}
