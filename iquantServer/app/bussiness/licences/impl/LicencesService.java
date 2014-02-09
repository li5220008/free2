package bussiness.licences.impl;

import bussiness.common.impl.BaseService;
import bussiness.licences.ILicencesService;
import bussiness.user.impl.UserInfoService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import play.Logger;
import play.Play;
import play.exceptions.UnexpectedException;
import play.libs.Crypto;
import play.libs.F;
import play.libs.IO;


import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * licence相关service
 * User: 潘志威
 * Date: 13-8-27
 * Time: 下午5:49
 * To change this template use File | Settings | File Templates.
 */
public class LicencesService extends BaseService implements ILicencesService {
    public static final String SPLIT_TXT = "@@";
    public static final String LICENCES_PATH = "conf/licences.text";
    public static final Joiner JOINER = Joiner.on(SPLIT_TXT).useForNull("null");
    public static final Splitter SPLITTER = Splitter.on(SPLIT_TXT);

    /**
     * licence生成方法：密钥的前8位+密文+密钥的后8位
     * @param uid
     * @param account
     * @param count
     * @return
     */
    @Override
    public String writeLicences(Long uid, String account, int count, String email) {
        //产生licences
        String LicencesText = generateLicence(uid, account, count, email);
        //写入文件
        File file = new File(LICENCES_PATH);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(LicencesText);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LicencesText;
    }

    private String generateLicence(Long uid, String account, int count, String email){
        //密钥
        String securityKey = UUID.randomUUID().toString().replace("-","").substring(0, 16);

        //密文
        String encryTxt = Crypto.encryptAES(JOINER.join(uid, account, email, count), securityKey);

        String b8key = securityKey.substring(0, 8);
        String a8key = securityKey.substring(8, 16);

        //licences
        return  b8key + encryTxt + a8key;
    }

    /**
     * 检查checksum是否合法
     * @param userUuid  对应于user_info表的user_uuid字段
     * @param account
     * @param email
     * @param oldCheckSum
     * @return
     */
    public boolean isCheckSumValid(String userUuid, Long uid, String account, String email, String oldCheckSum){
        String checksum = generateCheckSum(userUuid, uid, account, email);
        return checksum.equals(oldCheckSum);
    }

    /**
     * 生成checksum值
     * @param userUuid 用户表存的user_uuid值
     * @param uid
     * @param account
     * @param email
     * @return
     */
    public String generateCheckSum(String userUuid, Long uid, String account, String email){
        String joinTxt = JOINER.join(uid, account, email);
        return Crypto.encryptAES(joinTxt, userUuid.length() > 16 ? userUuid.substring(0, 16) : userUuid);
    }

    /**
     * 返回licence解密. _1表示key, _2表示加密内容
     * @return
     */
    private F.T2<String,String> fetchFromLicence(){
        String licence = IO.readContentAsString(Play.getFile(LICENCES_PATH));
        licence = licence.replace("\r\n","");
        if (licence != null && licence.length() > 16) {
            String key = licence.substring(0,8) + licence.substring(licence.length()-8);
            String encryTxt = licence.substring(8, licence.length()-8);
            return F.T2(key, encryTxt);
        }else {
            throw new RuntimeException("licence文件错误");
        }
    }

    /**
     * 新建用户时解密licences,验证新建用户是否超过限定用户数
     * @param
     * @return
     */
    @Override
    public boolean canCreateUser(){
        F.T2<String, String> t2 = fetchFromLicence();
        String decryptionText = null;
        try {
            decryptionText = Crypto.decryptAES(t2._2, t2._1);
        } catch (UnexpectedException e) {
            Logger.info("解密licence失败");
            return false;
        }

        Logger.info("解密后的字符串:%s", decryptionText);

        ArrayList<String> itemList = Lists.newArrayList(SPLITTER.split(decryptionText));

        if (itemList.size() >= 4) {
            UserInfoService userInfoService = new UserInfoService();
            long realCount = userInfoService.getUserCount();
            long count = Long.parseLong(itemList.get(3)); //第4个是个数
            if (realCount < count) {
                Logger.info("用户数还没有达到最大,可以创建.当前用户数%d", realCount);
                return true;
            } else {
                Logger.info("用户数达到最大,不可以创建.当前用户数%d", realCount);
                return false;
            }
        }else {
            Logger.warn("解密的字符串不符合格式, 返回false");
            return false;
        }
    }

}
