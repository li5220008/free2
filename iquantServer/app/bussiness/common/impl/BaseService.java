package bussiness.common.impl;

import dbutils.DBTemplateName;
import dbutils.MyDbUtil;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-27
 * Time: 下午5:55
 * 功能描述:
 */
public class BaseService {

    public final static MyDbUtil qicDbUtil  = new MyDbUtil();
    public final static MyDbUtil qicoreDbUtil = null;// new MyDbUtil(DBTemplateName.QICORE);//没有qicore策略了 不用连接qicore库，
    public final static MyDbUtil qsmDbUtil = new MyDbUtil(DBTemplateName.QSM);
    public final static MyDbUtil gtaDataDbUtil = new MyDbUtil(DBTemplateName.GTADATA);

}
