package bussiness.product;

import models.iquantCommon.productinfo.ProductDto;
import models.iquantCommon.productinfo.ProductStrategyAccountDto;
import models.iquantCommon.productinfo.ProductStrategyDto;

import java.util.List;
import java.util.Map;

/**
 * 给1.1用的产品服务接口
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 上午9:29
 */
public interface IProductService {

    /**
     * 删除某个产品的风控证券
     * @param productId 产品id
     * @return
     */
    public int deleteProductRiskStocks(long productId, long uid);

    /**
     * 删除产品策略板块
     * @param platesId 产品策略板块表的自增长id
     * @return
     */
    public int deleteProductStrategyPlates(List<Long> platesId, long uid);
    /**
     * 删除产品策略自定义板块成份股
     * @param platesId 产品策略板块表的自增长id
     * @return
     */
    public int deleteProductStrategyCustPlateStocks(List<Long> platesId);

    /**
     * 批量删除产品策略自定义证券
     * @param productStrategyId 产品策略关联id
     * @return
     */
    public int deleteProductStrategyCustScurity(List<Long> productStrategyId, long uid);
    /**
     * 批量删除产品策略屏蔽的股票集合
     * @param productStrategyId 产品策略关联id
     * @return
     */
    public int deleteProductStrategyShieldScurity(List<Long> productStrategyId);

    /**
     * 修改账号使用状态
     * @param tradeAccountIds
     * @return
     */
    public int updateTradeAccountUsed(List<Long> tradeAccountIds, Long status);
    /**
     * 删除产品策略账号
     * @param prodcutStrategyIds 产品策略关联关系id
     * @return
     */
    public int deleteProductStrategyAccount(List<Long> prodcutStrategyIds, long uid);
    /**
     * 删除产品策略关联关系
     * @param prodcutId 产品id
     * @return
     */
    public int deleteProductStrategy(long prodcutId, long uid);
    /**
     * 删除产品(删除产品相关的账号，证券信息)
     *
     * @param prodcutId
     * @param uid
     * @return
     */
    public int deleteProduct(long prodcutId, long uid);

    /**
     * 查询产品策略关联关系id列表
     * @param productId 产品id
     * @return
     */
    public List<Long> findProductStrategyIds(long productId);

    /**
     * 查询产品策略使用的账号列表
     * @param productStrategyIds 产品策略关联关系id列表
     * @return 账号id列表
     */
    public List<Long> findProductStrategyAccountIds(List<Long> productStrategyIds);

    /**
     * 查询产品策略对应的板块id集合
     * @param plateIds
     * @return
     */
    public List<Long> findProductStrategyPlatesIds(List<Long> plateIds);

    /**
     * 查询账号名是否存在
     * @param uid
     * @param name
     * @param id
     * @return
     */
    public  boolean accountHasSameName(long uid, String name, Long id);

    /**
     * 查询account在整个表中是否存在
     * @param account
     * @param id
     * @return
     */
    public boolean accountHasSameAccount(String account,int type, Long id);


    /**
     * 检查产品名称是否重复
     * @param uid 用户id
     * @param productName 产品名称
     * @param productId 产品id
     * @return 有重名, 返回true, 否则返回false
     */
    public boolean productHasSameName(long uid, String productName, Long productId);

    /**
     * 根据系统板块id查成分股
     * @param id 系统板块id
     * @return
     */
    public List<Map<String, Object>> fetchSystemPlateSecurityInfo(long id);

    /**
     * 根据自定义板块id查成份股
     * @param id 自定义板块id
     * @return
     */
    public List<Map<String, Object>> fetchCustPlateSecurityInfo(long id);

    /**
     * 查询用户的产品列表
     * @param uid
     * @return
     */
    public List<ProductDto> findUserProducts(long uid);

    /**
     * 查询产品的策略列表
     * @param productId
     * @return
     */
    public List<ProductStrategyDto> findProductStrategy(long productId);
    /**
     * 查询产品策略的账号列表
     * @param productStrategyId  产品策略关联表id
     * @return
     */
    public List<ProductStrategyAccountDto> findProductStrategyAccount(long productStrategyId);

    /**
     * 新增产品信息
     *
     * @param productDto    产品dto
     * @param uid
     * @return 返回新加的产品的id
     */
    Long addProduct(ProductDto productDto, long uid);

    /**
     * 修改产品信息
     * @param productDto 产品dto
     * @return
     */
    long editProduct(ProductDto productDto, long uid);

    /**
     * 根据产品id查看产品信息
     * @param id
     * @return
     */
    public Map<String,Object> fetchProductById(long id,long uid);

    /**
     * 查询产品策略的标的信息
     * @param productId
     * @param strategyId
     * @return
     */
    public Map<String,Object> fetchStrategyStockInfo(long productId,long strategyId);

    /**
     * 根据策略实例id返回策略信息
     * 在这里进行A+B-C操作（注意：这里还需要剔除当日屏蔽的板块成分股）
     * @param productStrategyId
     * @return
     */
    public Map<String,Object> fetchProductStrategyBindInfo(long productStrategyId,boolean isCutPrefix );

    /**
     * 返回当日主力连续合约全部选项
     * @return
     */
    public  List<Map<String,Object>> fetchAllMainContractAndContinuingContract();

    /**
     * 根据用户id查询产品id
     * @param uid
     * @return
     */
    public List<Long> fetchProductIdByUid(long uid);

    public List<Map<String,Object>> fetchAllRuntimeStrategyAcconts();
}
