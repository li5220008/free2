package bussiness.licences;

import models.iquantCommon.UserInfoDto;

/**
 * licences相关接口
 * User: 潘志威
 * Date: 13-8-27
 * Time: 下午4:33
 * To change this template use File | Settings | File Templates.
 */
public interface ILicencesService {
    /**
     * 生成秘钥
     * @param uid
     * @param account
     * @param count
     * @return
     */
    public String writeLicences(Long uid, String account, int count, String email);

    public boolean canCreateUser();

    public boolean isCheckSumValid(String userUuid, Long uid, String account, String email, String oldCheckSum);

    public String generateCheckSum(String userUuid, Long uid, String account, String email);
}
