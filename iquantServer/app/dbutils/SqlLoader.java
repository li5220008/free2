package dbutils;

import org.apache.commons.io.FileUtils;
import play.Logger;
import play.Play;
import play.libs.IO;
import play.vfs.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 用于sql加载.
 * 这里加载的sql都是放在 conf/sql 目录下面的, sql可以放在 .sql文件里(一个文件放一个sql). 文件名做为id值
 * 也可以放在.xml文件里, 这里用到的是http://java.sun.com/dtd/properties.dtd的格式.
 * User: wenzhihong
 * Date: 12-11-1
 * Time: 下午3:34
 */
public class SqlLoader {
    static final String sqlExtent = "sql";
    static final String xmlExtent = "xml";

    static Map<String, String> sqlMap = new HashMap<String, String>();

    static Map<String, String> sqlIdFileMap = new HashMap<String, String>();

    public static void init() {
        loadMainSqlFile();
        loadModulesSqlFile();
    }

    private static void loadMainSqlFile() {
        File sqlDir = Play.getFile("conf/sql");
        doLoad(sqlDir);
        File[] files = sqlDir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                doLoad(f);
            }
        }
    }


    private static void loadModulesSqlFile() {
        for (VirtualFile moduleFile : Play.modules.values()) {
            if (moduleFile.child("conf/sql").exists()) {
                doLoad(moduleFile.child("conf/sql").getRealFile());
            }
        }
    }

    private static void doLoad(File sqlDir) {
        Logger.info("loading sql from [%s]",sqlDir.getAbsolutePath());
        if (Play.mode.isDev()) {
            Iterator<File> fileIterator = FileUtils.iterateFiles(sqlDir, new String[]{sqlExtent}, false);
            while (fileIterator.hasNext()) {
                File f = fileIterator.next();
                //sql语句的话, 用sql文件名, 去掉.sql做为sqlid
                String sqlId = f.getName().substring(0, f.getName().length() - (sqlExtent.length() + 1));
                if (sqlIdFileMap.containsKey(sqlId)) {
                    throw new RuntimeException("有同名[" + f.getName() + "]的sql文件,请检查 conf/sql目录");
                } else {
                    sqlIdFileMap.put(sqlId, f.getPath());
                }
            }

            //处理xml文件
            fileIterator = FileUtils.iterateFiles(sqlDir, new String[]{xmlExtent}, false);
            while (fileIterator.hasNext()) {
                File f = fileIterator.next();
                Properties p = new Properties();
                try {
                    p.loadFromXML(FileUtils.openInputStream(f));
                } catch (IOException e) {
                    throw new RuntimeException("读取xml[" + f.getName() + "]文件错误", e);
                }
                Set<String> keys = p.stringPropertyNames();
                for (String sqlId : keys) {
                    if (sqlIdFileMap.containsKey(sqlId)) {
                        throw new RuntimeException("xml文件[" + f.getName() + "]里包含的entry key[" + sqlId + "]已包含,请检查 conf/sql目录下的xml文件");
                    } else {
                        sqlIdFileMap.put(sqlId, f.getPath());
                    }
                }
            }
            return;
        } else {
            Iterator<File> fileIterator = FileUtils.iterateFiles(sqlDir, new String[]{sqlExtent}, false);
            while (fileIterator.hasNext()) {
                File f = fileIterator.next();
                //sql语句的话, 用sql文件名, 去掉.sql做为sqlid
                String sqlId = f.getName().substring(0, f.getName().length() - (sqlExtent.length() + 1));
                if (sqlMap.containsKey(sqlId)) {
                    throw new RuntimeException("有同名[" + f.getName() + "]的sql文件,请检查 conf/sql目录");
                } else {
                    sqlMap.put(sqlId, IO.readContentAsString(f).trim());
                }
            }

            //处理xml文件
            fileIterator = FileUtils.iterateFiles(sqlDir, new String[]{xmlExtent}, false);
            while (fileIterator.hasNext()) {
                File f = fileIterator.next();
                Properties p = new Properties();
                try {
                    p.loadFromXML(FileUtils.openInputStream(f));
                } catch (IOException e) {
                    throw new RuntimeException("读取xml[" + f.getName() + "]文件错误", e);
                }
                Set<String> keys = p.stringPropertyNames();
                for (String sqlId : keys) {
                    if (sqlMap.containsKey(sqlId)) {
                        throw new RuntimeException("xml文件[" + f.getName() + "]里包含的entry key[" + sqlId + "]已包含,请检查 conf/sql目录下的xml文件");
                    } else {
                        sqlMap.put(sqlId, p.getProperty(sqlId).trim());
                    }
                }
            }
        }
    }

    /**
     * 根据sqlid读取sql内容
     *
     * @param sqlId
     * @return
     */
    public static String getSqlById(String sqlId) {
        String sql = null;
        if (Play.mode.isDev()) {
            String filePath = sqlIdFileMap.get(sqlId);
            if (filePath == null) {
                throw new RuntimeException("没有sqlid为[" + sqlId + "]的sql, 请重启程序试试");
            }

            if (filePath.endsWith("sql")) {
                sql = IO.readContentAsString(new File(filePath));
            } else if (filePath.endsWith("xml")) {
                Properties p = new Properties();
                try {
                    p.loadFromXML(FileUtils.openInputStream(new File(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException("读取xml[" + filePath + "]文件错误", e);
                }
                sql = p.getProperty(sqlId);
                if (sql == null) {
                    throw new RuntimeException("没有sqlid为[" + sqlId + "]的sql, 请重启程序试试");
                }
            }
            if (sql != null) {
                sql = sql.trim();
            }
        } else {
            sql = sqlMap.get(sqlId);
            if (sql == null) {
                throw new RuntimeException("没有sqlid为[" + sqlId + "]的sql");
            }
        }

        return sql;
    }
}
