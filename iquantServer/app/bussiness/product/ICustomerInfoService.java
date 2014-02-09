package bussiness.product;

import models.iquantCommon.CustomerIndexDto;
import models.iquantCommon.CustomerSecurityGroupDto;
import models.iquantCommon.CustomerSecurityListDto;
import models.iquantCommon.CustomerTemplateDto;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-19
 * Time: 下午4:44
 * 功能描述:
 */
public interface ICustomerInfoService {
    /**
     *新培自选股
     * @param customerSecurityGroupDto
     * @return
     */
    public long  addCustSecGroup(CustomerSecurityGroupDto customerSecurityGroupDto);


    /**
     * 判断自选股组名称是否存在. 存在返回1, 不存在返回0
     *
     * @param name
     */
    public long existCustSecGroupName(long uid,long pid,String name) ;


    /**
     * 删除同名的自选股组. 0 没有删除. 1 删除成功
     *
     * @param name
     */
    public   List<Long>  delCustSecGroupByName(long uid,long pid,String name) ;


    /**
     * 删除一个自选股组,包括删除其明细数据
     *
     */
    public  int delCustSecGroupById(long uid,long pid,long gid);


    /**
     * 返回用户在这个产品上的自选股组. 只返回每个名称的最新的那条记录
     */
    public  List<Map<String, Object>>  fetchCustSecGroupLatest(long uid,long pid);

    /**
     * 获取指定组名称的历史记录. 按由近及远的方式返回
     */
    public  List<Map<String, Object>> fetchCustSecGroupByNameAll(long uid,long pid,String name) ;

    /**
     * 获取指定自选股组名称,最新的自选股明细
     */
    public List<Map<String, Object>>  fetchCustSecGroupByNameLatestDetail(long uid,long pid,String name);
    /**
     * 增加一条自选股明细数据
     *
     */
    public long addCustSecListByGid(CustomerSecurityListDto customerSecurityListDto);

    /**
     * 批量新建或修改自选股明细数据
     *
     */
    public int updateCustSecListByGid(CustomerSecurityListDto customerSecurityListDto) ;

    /**
     * 删除一条自选股明细数据
     *
     * @param id 明细数据id
     */
    public int delCustSecListById(long uid,long pid, Long id);

    /**
     * 返回自选股组里的股票明细信息
     *
     * @param gid 组id
     */
    public List<Map<String, Object>> fetchCustSecListByGid(long uid,long pid, Long gid) ;


    /**
     * 增加一个用户自定义指标
     *
     */
    public long addCustIndex(CustomerIndexDto customerIndexDto);

    /**
     * 删除一个用户自定义指标
     *
     * @param id
     */
    public int delCustIndex(long uid,long pid, Long id) ;

    /**
     * 修改用户自定义指标
     *
     */
    public int editCustIndex( CustomerIndexDto customerIndexDto) ;


    /**
     * 返回用户自定义指标
     */
    public List<Map<String, Object>> fetchCustIndex(long uid,long pid) ;

    /**
     * 根据自定义指标id,返回指标内容
     *
     * @param id 指标id
     */
    public String fetchCustIndexContentByid(long uid,long pid, Long id) ;

    /**
     * 增加一个用户自定义模板
     */
    public long addCustTemplate(CustomerTemplateDto customerTemplateDto) ;

    /**
     * 删除一个用户自定义模板
     *
     * @param id
     */
    public int  delCustTemplate(long uid,long pid, Long id) ;


    /**
     * 修改用户自定义模板
     */
    public int editCustTemplate(CustomerTemplateDto customerTemplateDto ) ;


    /**
     * 返回用户自定义模板
     */
    public List<Map<String, Object>> fetchCustTemplateListByCategory(long uid,long pid,int category);


    /**
     * 根据自定义指标id,返回模板内容
     *
     * @param id 指标id
     */
    public String fetchCustTemplateContentByid(long uid,long pid, Long id);


    /**
     * 检查模板名称是否重复. 重复输出id值, 不重复输出 -1
     */
    public Long custTemplateExistName(long uid,long pid,int category, String name) ;

    /**
     * 给自选股增加备注信息
     * @param uid
     * @param pid
     * @param id
     * @param comments
     */
    void updateCustSecCommentsById(long uid, long pid, long id, String comments);
}
