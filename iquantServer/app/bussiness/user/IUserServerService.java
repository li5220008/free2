package bussiness.user;

import models.iquantCommon.UserServerDto;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-28
 * Time: 下午2:55
 */
public interface IUserServerService {

    /**
     * 添加服务器配置
     * @param userServerDto 用户服务器模板
     * @return
     */
    public UserServerDto addServer(UserServerDto userServerDto);

    /**
     * 更新服务器配置
     * @param userServerDto 用户服务器模板
     * @return
     */
    public int updateServer(UserServerDto userServerDto);

    /**
     * 根据Id获取服务器配置信息
     * @param id
     * @return
     */
    public UserServerDto fetchServerById(long id);

    public void testProductMigration();
}
