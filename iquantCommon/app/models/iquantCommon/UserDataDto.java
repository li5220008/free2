package models.iquantCommon;

import com.google.gson.annotations.Expose;


/**
 * User: liuhongjiang
 * Date: 13-8-27
 * Time: 下午1:06
 */
public class UserDataDto {
    public long did;//数据权限ID
    @Expose
    public String logiccode;  //数据权限编码类别
    @Expose
    public String content; //内容

}
