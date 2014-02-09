package models.iquantCommon;

/**
 * 用户自选股组dto
 * User: wenzhihong
 * Date: 13-4-28
 * Time: 下午2:37
 */
public class CustomerSecurityGroupDto extends BaseUserResourceDtoSupport {
    public String name;

    public String sname;

    //1.条件股; 2.自选股; 3.其它
    public int flag;
}
