package controllers;

import play.mvc.Controller;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 上午10:06
 * 功能描述: 下载的方法写在这里
 */
public class DownLoads extends Controller {
    /**
     * 下载用户信息模板
     */
    public  static void downLoadExcelTemplate(){
      /*  File templateFile = Play.getFile(QicConfigProperties .getString(Constants.USER_EXCEL_TEMPLATE_KEY));
        InputStream is =null;
        try{
            is = new FileInputStream(templateFile);
        }catch (Exception e){
             e.printStackTrace();
        }
        renderBinary(is,templateFile.getName());*/
    }
}
