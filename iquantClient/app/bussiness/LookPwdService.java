package bussiness;

import models.iquantCommon.SendMailDto;
import models.iquantCommon.UserInfo;
import util.CommonUtils;
import util.Constants;
import util.MessageBuilder;
import util.QicConfigProperties;

import javax.inject.Inject;

/**
 * 找回密码
 * User: liangbing
 * Date: 12-12-26
 * Time: 上午11:03
 */
public class LookPwdService {
    @Inject
    static SystemConfigService systemConfigService;

    /**
     * 发邮箱方法
     * @param userInfo 用户信息对象
     * @param newPwd 新的密码
     */
    public static void sendMsg(UserInfo userInfo,String newPwd) {
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.accepterEmail =  userInfo.email;// 接收者

        final String sender = QicConfigProperties.get(Constants.EMAIL_SENDER);
        final String name = QicConfigProperties.get(Constants.EMALI_NAME);
        final String title = QicConfigProperties.get(Constants.EMALI_TITLE);

        sendMailDto.sender = sender;//发送者邮箱
        sendMailDto.name =name; //发送者姓名
        sendMailDto.title = title;//邮件标题
        try {
            String message = systemConfigService.get("otherMsg");//得到模板类容
            MessageBuilder messageBuilder = new MessageBuilder(message);
            messageBuilder.addParameter("userInfo",userInfo);
            String inputVal = messageBuilder.execute();
            inputVal=inputVal.replace(userInfo.pwd, newPwd);
            sendMailDto.content = inputVal;
            //发送邮箱
            CommonUtils.sendMail(sendMailDto);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
