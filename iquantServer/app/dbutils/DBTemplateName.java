package dbutils;

import play.db.DBConfig;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-25
 * Time: 下午2:40
 * 功能描述:
 */
public enum DBTemplateName {

    QIC(DBConfig.defaultDbConfigName),
    QICORE("qiCore"),
    QSM("qsm"),
    GTADATA("gtaData");

    private DBTemplateName(String configName) {
        this.dbConfigName = configName;
    }

    //数据库在play的application里的配制的名字
    public final String dbConfigName;
}
