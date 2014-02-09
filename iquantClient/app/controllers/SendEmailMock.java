package controllers;

import models.iquantCommon.SendMailDto;
import play.mvc.Controller;
import util.CommonUtils;

/**
 * User: liuhongjiang
 * Date: 13-12-11
 * Time: 下午5:46
 * 功能说明:  测试邮件发送功能
 */
public class SendEmailMock extends Controller {

    public static void index(){
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.name = "iquant";
        sendMailDto.sender ="iquant@gtafe.com";
        sendMailDto.content = "this is a test";
        sendMailDto.title = "test send email";
        sendMailDto.accepterEmail="liangb@gtadata.com";
        CommonUtils.sendMail(sendMailDto);
    }
}
