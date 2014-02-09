package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-28
 * Time: 下午1:26
 */
public class UserServerDto extends BaseDtoSupport {
    @Expose
    public String ip;
    @Expose
    public int port;
    @Expose
    public long uid;

    public UserServerDto(String ip, int port, long uid) {
        this.ip = ip;
        this.port = port;
        this.uid = uid;
    }
}
