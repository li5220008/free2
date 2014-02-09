package models.iquantCommon;

/**
 * User: wenzhihong
 * Date: 13-5-2
 * Time: 上午8:42
 */
public class CustomerTemplateDto extends BaseUserResourceDtoSupport {
    public String name;

    //模板内容, xml方式, 依赖于客户端的自解析
    public String content;

    public int category = 1;
}
