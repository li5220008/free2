package models.iquantCommon;

/**
 * 发送邮件需要的内容
 * User: Administrator
 * Date: 13-12-9
 * Time: 下午2:45
 */
public class SendMailDto {

   public String sender;  //发送者邮箱

   public String name;//发送者姓名

   public String accepterEmail;//接收者邮箱

   public String title;//邮件标题

   public String content;//发送内容

   public String userAccount;//用户账号

   public String userEmail = "该用户邮件信息缺失";//用户邮箱

}
