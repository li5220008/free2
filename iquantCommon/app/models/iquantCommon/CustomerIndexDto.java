package models.iquantCommon;

/**
 * User: wenzhihong
 * Date: 13-5-2
 * Time: 上午8:28
 */
public class CustomerIndexDto extends BaseUserResourceDtoSupport {
    public String name;

    //指标内容, xml方式, 依赖于客户端的自解析
    public String content;
}
