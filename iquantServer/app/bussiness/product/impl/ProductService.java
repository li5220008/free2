package bussiness.product.impl;

import com.google.common.collect.*;
import dbutils.AbstractKeyedMutiMapHandler;
import exceptions.IquantEntityHasBeenUsedException;
import bussiness.common.impl.BaseService;
import bussiness.product.IProductService;
import bussiness.strategy.IUserStrategyManageService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import dbutils.SqlLoader;
import models.iquantCommon.productinfo.*;
import org.apache.commons.dbutils.handlers.AbstractKeyedHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.Codec;
import play.modules.guice.InjectSupport;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static util.CommonUtils.isNotEmptyCollection;

/**
 * User: liujianli
 * Date: 13-9-20
 * Time: 上午9:30
 */
@InjectSupport
public class ProductService extends BaseService implements IProductService {
    @Inject
    static IUserStrategyManageService userStrategyManageService;

    @Override
    public boolean accountHasSameName(long uid, String name, Long id) {
        String sameNameCount = SqlLoader.getSqlById("tradeAccountSameNameCount11");
        if (id != null) {
            sameNameCount += " and id <> " + id.toString();
        }
        return qicDbUtil.queryCount(sameNameCount, uid, name) > 0;
    }
    @Override
    public boolean accountHasSameAccount(String account,int type, Long id) {
        String sameNameCount = SqlLoader.getSqlById("tradeAccountSameAccount11");
        if (id != null) {
            sameNameCount += " and id <> " + id.toString();
        }
        return qicDbUtil.queryCount(sameNameCount, account,type) > 0;
    }

    @Override
    public int deleteProductRiskStocks(long productId, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productId",productId);
        map.put("userId", uid);
        int effectRow = qicDbUtil.updateWithNameParam("deleteProductRiskStocks",map);
        return 0;
    }

    @Override
    public int deleteProductStrategyPlates(List<Long> plateIds, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long plateId:plateIds){
            map.put("id",plateId);
            map.put("userId", uid);
            qicDbUtil.updateWithNameParam("deleteProductStrategyPlates",map);
            ++effectRow;
        }
        return effectRow;
    }

    @Override
    public int deleteProductStrategyCustPlateStocks(List<Long> platesIds) {
        Map<String,Long> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long plateId:platesIds){
            map.put("plateId",plateId);
            qicDbUtil.updateWithNameParam("deleteProductStrategyCustPlateStocks",map);
            ++effectRow;
        }
        return effectRow;
    }

    @Override
    public int deleteProductStrategyCustScurity(List<Long> productStrategyIds, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long productStrategyId : productStrategyIds){
            map.put("productStrategyId",productStrategyId);
            map.put("userId", uid);
            qicDbUtil.updateWithNameParam("deleteProductStrategySecurity",map);
            ++effectRow;
        }
        return effectRow;
    }

    @Override
    public int deleteProductStrategyShieldScurity(List<Long> plateIds) {
        Map<String,Long> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long plateId:plateIds){
            map.put("plateId",plateId);
            qicDbUtil.updateWithNameParam("deleteProductStrategyShieldScurity",map);
            ++effectRow;
        }
        return effectRow;
    }

    @Override
    public int updateTradeAccountUsed(List<Long> tradeAccountIds, Long used) {
        Map<String,Object> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long accountId : tradeAccountIds){
            map.put("accountId",accountId);
            map.put("used",used);
            qicDbUtil.updateWithNameParam("updateTradeAccountUsed",map);
            ++effectRow;
        }
        return effectRow;
    }

    @Override
    public int deleteProductStrategyAccount(List<Long> prodcutStrategyIds, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        int effectRow = 0;
        for(Long productStrategyId : prodcutStrategyIds){
            map.put("productStrategyId",productStrategyId);
            map.put("userId", uid);
            qicDbUtil.updateWithNameParam("deleteProductStrategyAccount",map);
            ++effectRow;
        }
        return effectRow;
    }

    private int deleteProductStrategyByRefId(List<Long> toBeDeleteId,long uid ){
           int effectRow = 0;
        Map<String,Long> map = Maps.newHashMap();
           for(Long refId:toBeDeleteId){
               map.put("id",refId);
               map.put("userId",uid);
               effectRow+=  qicDbUtil.updateWithNameParam("deleteProductStrategyById",map);
           }

           return effectRow;
    }
    @Override
    public int deleteProductStrategy(long prodcutId, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productId",prodcutId);
        map.put("userId",uid);
        int effectRow = qicDbUtil.updateWithNameParam("deleteProductStrategyByPid",map);
        return effectRow;
    }

    @Override
    public int deleteProduct(long prodcutId, long uid) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productId",prodcutId);
        map.put("userId",uid);
        //4. 将product设置成status -100
        int effectRow = qicDbUtil.updateWithNameParam("deleteProduct",map);
        Logger.info("软删除产品,产品id[%s],row[%s]",prodcutId,effectRow);
        effectRow += deleteProductTemplate(prodcutId, uid);
        return effectRow;
    }

    private int deleteProductTemplateForEdit(ProductDto productDto,long prodcutId, long uid){
        List<Long> deleteRuntimeIds = Lists.newArrayList();
        List<Long> newRuntimeIds = Lists.newArrayList();//从客户端传过来的所有待修改的关联id
        for(ProductStrategyDto productStrategyDto:productDto.strategys){
            if(productStrategyDto.id>0){
                newRuntimeIds.add(productStrategyDto.id);
            }
        }
        int effectRow = 0;
        //1. 查询所有该产品与策略关联关系id
        List<Long> productStrategyIds = findProductStrategyIds(prodcutId);
        //2. 从qsm库中删除该产品的每个运行实例id
        for(long refId : productStrategyIds){
            userStrategyManageService.deleteProductFromQsm(refId);
            if(!newRuntimeIds.contains(refId)){//如果客户端传过来的关联id中不包含此id，则表示是要被删除的id
                deleteRuntimeIds.add(refId);
            }
            Logger.info("从qsm中删除运行实例id[%s]",refId);
        }
        //3. 从qic.product_strategy中将要删除的id对应记录的status设置为-100
         //计算哪些id是要被删除的
        effectRow+=deleteProductStrategyByRefId(deleteRuntimeIds,uid);
        Logger.info("prodcut_strategy表中删除了关系集合：",deleteRuntimeIds);

        //5. 删除产品的风控证券
        effectRow+= deleteProductRiskStocks(prodcutId, uid);
        Logger.info("软删除产品风控证券,产品id[%s],row[%s]",prodcutId,effectRow);
        //6. 删除产品策略的证券代码
        effectRow+= deleteProductStrategyCustScurity(productStrategyIds, uid);
        Logger.info("删除产品策略的证券代码,产品id[%s],row[%s]",prodcutId,effectRow);
        //7，将产品策略账号表status设置为-1
        effectRow+= deleteProductStrategyAccount(productStrategyIds, uid);
        Logger.info("删除产品策略账号,产品id[%s],row[%s]",prodcutId,effectRow);
        //8. 将账号的used设置成0;
        List<Long> accountIds = findProductStrategyAccountIds(productStrategyIds);
        effectRow+= updateTradeAccountUsed(accountIds, 0L);//
        Logger.info("将账号的used设置成0,产品id[%s],row[%s]",prodcutId,effectRow);
        //9 根据成产品策略关联id查询版块id集合
        List<Long> plateIds = findProductStrategyPlatesIds(productStrategyIds);
        //10. 删除板块成份股
        effectRow+= deleteProductStrategyCustPlateStocks(plateIds);
        Logger.info("删除板块成份股,产品id[%s],row[%s]",prodcutId,effectRow);
        //11. 删除板块屏蔽代码
        effectRow+= deleteProductStrategyShieldScurity(plateIds);
        Logger.info("删除板块屏蔽代码,产品id[%s],row[%s]",prodcutId,effectRow);
        //12. 删除版块
        effectRow+= deleteProductStrategyPlates(plateIds, uid);
        Logger.info("删除版块,产品id[%s],row[%s]",prodcutId,effectRow);
        //删完了
        return effectRow;
    }


    private int deleteProductTemplate(long prodcutId, long uid){
        int effectRow = 0;
        //1. 查询所有该产品与策略关联关系id
        List<Long> productStrategyIds = findProductStrategyIds(prodcutId);
        //2. 从qsm库中删除该产品的每个运行实例id
        for(long refId : productStrategyIds){
            userStrategyManageService.deleteProductFromQsm(refId);
            Logger.info("从qsm中删除运行实例id[%s]",refId);
        }
        //3. 从qic.product_strategy将status设置为-100
        deleteProductStrategy(prodcutId, uid);
        Logger.info("软删除产品策略,产品id[%s]", prodcutId);

        //5. 删除产品的风控证券
        effectRow+= deleteProductRiskStocks(prodcutId, uid);
        Logger.info("软删除产品风控证券,产品id[%s],row[%s]",prodcutId,effectRow);
        //6. 删除产品策略的证券代码
        effectRow+= deleteProductStrategyCustScurity(productStrategyIds, uid);
        Logger.info("删除产品策略的证券代码,产品id[%s],row[%s]",prodcutId,effectRow);
        //7，将产品策略账号表status设置为-1
        effectRow+= deleteProductStrategyAccount(productStrategyIds, uid);
        Logger.info("删除产品策略账号,产品id[%s],row[%s]",prodcutId,effectRow);
        //8. 将账号的used设置成0;
        List<Long> accountIds = findProductStrategyAccountIds(productStrategyIds);
        effectRow+= updateTradeAccountUsed(accountIds, 0L);//
        Logger.info("将账号的used设置成0,产品id[%s],row[%s]",prodcutId,effectRow);
        //9 根据成产品策略关联id查询版块id集合
        List<Long> plateIds = findProductStrategyPlatesIds(productStrategyIds);
        //10. 删除板块成份股
        effectRow+= deleteProductStrategyCustPlateStocks(plateIds);
        Logger.info("删除板块成份股,产品id[%s],row[%s]",prodcutId,effectRow);
        //11. 删除板块屏蔽代码
        effectRow+= deleteProductStrategyShieldScurity(plateIds);
        Logger.info("删除板块屏蔽代码,产品id[%s],row[%s]",prodcutId,effectRow);
        //12. 删除版块
        effectRow+= deleteProductStrategyPlates(plateIds, uid);
        Logger.info("删除版块,产品id[%s],row[%s]",prodcutId,effectRow);
        //删完了
        return effectRow;
    }

    @Override
    public List<Long> findProductStrategyIds(long productId) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productId",productId);
        List<Long> productStrategyIds = qicDbUtil.queryWithHandlerWithNameParam("findProductStrategyIds", new ColumnListHandler<Long>(), map);
        return productStrategyIds;
    }

    @Override
    public List<Long> findProductStrategyAccountIds(List<Long> productStrategyIds) {
        if (productStrategyIds == null || productStrategyIds.size() == 0) {
            return Lists.newArrayList();
        }
        Joiner joiner = Joiner.on(",");
        String productStrategyId = joiner.join(productStrategyIds);
        String sql = SqlLoader.getSqlById("findProductStrategyUsedAccounts").replace("#ids#", productStrategyId);
        List<Long> accountIds = qicDbUtil.queryWithHandler(sql, new ColumnListHandler<Long>());
        return accountIds;
    }

    @Override
    public List<Long> findProductStrategyPlatesIds(List<Long> productStrategyIds) {
        if (productStrategyIds == null || productStrategyIds.size() == 0) {
            return Lists.newArrayList();
        }
        Joiner joiner = Joiner.on(",");
        String productStrategyId = joiner.join(productStrategyIds);
        String sql = SqlLoader.getSqlById("findProductStrategyPlatesIds").replace("#ids#", productStrategyId);
        List<Long> accountIds = qicDbUtil.queryWithHandler(sql, new ColumnListHandler<Long>());
        return accountIds;
    }

    @Override
    public boolean productHasSameName(long uid, String productName, Long productId) {
        String sameNameCount = SqlLoader.getSqlById("productHasSameName");
        if (productId != null) {
            sameNameCount += " and id <> " + productId.toString();
        }
        return qicDbUtil.queryCount(sameNameCount, uid, productName) > 0;
    }

    @Override
    public List<Map<String, Object>> fetchSystemPlateSecurityInfo(long id){
        String strId = String.valueOf(id) + "%";
        return gtaDataDbUtil.queryMapList(SqlLoader.getSqlById("fetchSystemPlateSecurityInfo"), strId);
    }

    @Override
    public List<Map<String, Object>> fetchCustPlateSecurityInfo(long id){
        return qicDbUtil.queryMapList(SqlLoader.getSqlById("fetchCustPlateSecurityInfo"), id);
    }

    @Override
    public List<ProductDto> findUserProducts(long uid) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("uid",uid);
        List<ProductDto> list = qicDbUtil.queryBeanListWithNameParam("findUserProducts", ProductDto.class, map);
        return list;
    }

    @Override
    public List<ProductStrategyDto> findProductStrategy(long productId) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productId",productId);
        List<ProductStrategyDto>  list = qicDbUtil.queryBeanListWithNameParam("findProductStrategy",ProductStrategyDto.class,map);
        return list;
    }

    @Override
    public List<ProductStrategyAccountDto> findProductStrategyAccount(long productStrategyId) {
        Map<String,Long> map = Maps.newHashMap();
        map.put("productStrategyId", productStrategyId);
        List<ProductStrategyAccountDto> list = qicDbUtil.queryBeanListWithNameParam("findProductStrategyAccount", ProductStrategyAccountDto.class, map);
        return list;
    }

    @Override
    public Long addProduct(ProductDto productDto, long uid) {
        Logger.info("开始新增一个产品");
        productDto.userId = uid;
        productDto.uuid = Codec.UUID().replaceAll("-", "");

        Logger.info("往产品主表(product_info)插入信息");
        final long productId = qicDbUtil.insertWithNameParam("addProductInfoMainTable", productDto);
        addProductTemplate(productDto, productId);

        Logger.info("结束新增一个产品");
        return productId;
    }

    //增加产品的其它信息
    private void addProductTemplate(ProductDto productDto, long productId) {
        addProductRiskSecurity(productDto.riskStocks, productId, productDto.userId);
        addProductStrategy(productDto.strategys, productId, productDto.userId);
    }
    //增加产品的其它信息
    private void editProductTemplate(ProductDto productDto, long productId) {
        addProductRiskSecurity(productDto.riskStocks, productId, productDto.userId);
        editProductStrategy(productDto.strategys, productId, productDto.userId);
    }

    private void addProductRiskSecurity(List<RiskSecurityDto> riskStocks, long productId, long uid) {
        if (isNotEmptyCollection(riskStocks)) {
            Logger.info("往风控证券表(risk_control_secrity)插入信息");
            for (RiskSecurityDto item : riskStocks) {
                item.productId = productId;
                item.userId = uid;
            }
            qicDbUtil.insertBatchWithNameParam("addRiskSecurity", null, riskStocks.toArray());
        } else {
            Logger.info("没有风控证券信息");
        }
    }

    private void editProductStrategy(List<ProductStrategyDto> strategies, long productId, long uid) {
        if ( isNotEmptyCollection(strategies) ) {
            for (ProductStrategyDto strategyDto : strategies) {
                Logger.info("往产品策略关联表(product_strategy)插入信息,strategyId=%d", strategyDto.strategyId);
                strategyDto.productId = productId;
                strategyDto.userId = uid;
                long productStrategyId = -1;
                 if(strategyDto.id==-1){
                   productStrategyId = qicDbUtil.insertWithNameParam("addProductStrategy", strategyDto); //产品策略关联id
                   Logger.info("修改产品操作时,新增产品和策略关系,产品id:[%d],用户id:[%d],关联关系id:[%s],策略id:[%d]",productId,uid,productStrategyId, strategyDto.strategyId);
                }else if(strategyDto.id>0){
                     productStrategyId =  strategyDto.id;
                     strategyDto.userId = uid;
                     //修改操作
                     qicDbUtil.updateWithNameParam("updateProductStrategy",strategyDto);
                     Logger.info("修改产品策略关系表：关联id:[%d]:",strategyDto.id);
                 }else{
                     Logger.info("修改产品id为:[%d]操作时关联关系传错了值,传的值为:[%d]",productId,strategyDto.id);
                 }
                //往qsm库增加信息
                Logger.info("往qsm库增加信息:productStrategyId=%d, strategyId=%d", productStrategyId, strategyDto.strategyId);
                userStrategyManageService.syncProductToQsm(productStrategyId, strategyDto.strategyId);

                addProductStrategyAccount(strategyDto.accounts, productStrategyId, uid);
                addProductStrategySecurity(strategyDto.securities, productStrategyId, uid);
                addProductStrategyPlate(strategyDto.plates, productStrategyId, uid);
            }
        } else {
            Logger.info("没有产品策略信息");
        }
    }

    private void addProductStrategy(List<ProductStrategyDto> strategies, long productId, long uid) {
        if ( isNotEmptyCollection(strategies) ) {
            for (ProductStrategyDto strategyDto : strategies) {
                Logger.info("往产品策略关联表(product_strategy)插入信息,strategyId=%d", strategyDto.strategyId);
                strategyDto.productId = productId;
                strategyDto.userId = uid;
                long productStrategyId = qicDbUtil.insertWithNameParam("addProductStrategy", strategyDto); //产品策略关联id
                //往qsm库增加信息
                Logger.info("往qsm库增加信息:productStrategyId=%d, strategyId=%d", productStrategyId, strategyDto.strategyId);
                userStrategyManageService.syncProductToQsm(productStrategyId, strategyDto.strategyId);

                addProductStrategyAccount(strategyDto.accounts, productStrategyId, uid);
                addProductStrategySecurity(strategyDto.securities, productStrategyId, uid);
                addProductStrategyPlate(strategyDto.plates, productStrategyId, uid);
            }
        } else {
            Logger.info("没有产品策略信息");
        }
    }

    private void addProductStrategyAccount(List<ProductStrategyAccountDto> accounts, long productStrategyId, long uid) {
        if (isNotEmptyCollection(accounts)) {
            Logger.info("往产品策略帐号关联表(product_strategy_trade_account)插入信息, productStrategyId=%d", productStrategyId);
            for (ProductStrategyAccountDto accountDto : accounts) {
                accountDto.productStrategyId = productStrategyId;
                accountDto.userId = uid;

                if(accountDto.accountId!=null && accountDto.accountId!=0){
                    Logger.info("查看是否有产品关联了该账号");
                    Long count =  qicDbUtil.queryCount(SqlLoader.getSqlById("queryIfUsed"),accountDto.accountId);
                    Logger.info("accountId:" +accountDto.accountId +  "===count:" +count + "sql : " + SqlLoader.getSqlById("queryIfUsed") );
                    if(count > 0 ){
                        Logger.warn("该账号已经被关联，退出创建");
                        updateTradeAccountUsed(Lists.newArrayList(accountDto.accountId), 1L);//这个时候,不管trade_account中是不是状态为1都改为1
                        throw new IquantEntityHasBeenUsedException("交易账号已经被绑定");
                    }
                }
                qicDbUtil.insertWithNameParam("addProductStrategyTradeAccount",accountDto);
                updateTradeAccountUsed(Lists.newArrayList(accountDto.accountId), 1L);
                Logger.info("修改资金帐号的状态为已使用, 帐号id为 %s ", accountDto.accountId);
            }
        } else {
            Logger.info("没有产品策略帐号信息");
        }
    }

    private void addProductStrategySecurity(List<ProductStrategySecurityDto> securities, long productStrategyId, long uid) {
        if (isNotEmptyCollection(securities)) {
            Logger.info("往产品策略证券表(product_strategy_security)插入信息, productStrategyId=%d", productStrategyId);
            for (ProductStrategySecurityDto security : securities) {
                security.productStrategyId = productStrategyId;
                security.userId = uid;
            }
            qicDbUtil.insertBatchWithNameParam("addProductStrategySecurity", null, securities.toArray());
        } else {
            Logger.info("没有产品策略证券信息");
        }
    }

    private void addProductStrategyPlate(List<ProductStrategyPlateDto> plates, long productStrategyId, long uid) {
        if (isNotEmptyCollection(plates)) {
            Logger.info("往产品策略板块信息表(product_strategy_plate)插入信息, productStrategyId=%d", productStrategyId);
            for (ProductStrategyPlateDto plate : plates) {
                plate.productStrategyId = productStrategyId;
                plate.userId = uid;
                if (plate.traceSystemPlate == 1 && plate.systemPlateId == -1) {
                    Logger.warn("上传的参数有问题, 设置了跟踪系统板块, 但系统板块值又设置为 -1. (-1表示的是自定义板块)");
                }

                long plateId = qicDbUtil.insertWithNameParam("addProductStrategyPlate", plate); //板块id

                if (isNotEmptyCollection(plate.plateStocks)) { //插入自定义板块成分股
                    for (ProductStrategyPlateSecurityDto plateStock : plate.plateStocks) {
                        plateStock.plateId = plateId;
                    }

                    if (plate.systemPlateId == -1) { //自定义板块要插入成分股. 系统板块不插入
                        Logger.info("往产品策略板块对应的成份股(product_strategy_plate_security)插入数据, plateId=%d", plateId);
                        qicDbUtil.insertBatchWithNameParam("addProductStrategyPlateSecurity", null, plate.plateStocks.toArray());
                    }

                    //过滤出要屏蔽的股票.
                    List<ProductStrategyPlateSecurityDto> shieldSecList = Lists.newArrayList();
                    for (ProductStrategyPlateSecurityDto plateStock : plate.plateStocks) {
                        if (plateStock.status == 1) { //屏蔽
                            shieldSecList.add(plateStock);
                        }
                    }

                    //自定义板块跟系统板块都要插入屏蔽的股票
                    if (shieldSecList.size() > 0) {
                        Logger.info("往产品策略板块屏蔽股票表(product_strategy_plate_shield_secuirty)插入数据, plateId=%d", plateId);
                        qicDbUtil.insertBatchWithNameParam("addProductStrategyPlateShieldSecuirty", null, shieldSecList.toArray());
                    } else {
                        Logger.info("没有要屏蔽的股票");
                    }
                }
            }
        } else {
            Logger.info("没有产品策略板块信息");
        }
    }


    @Override
    public long editProduct(ProductDto productDto, long uid) {
        long productId = productDto.id;
        Logger.info("开始修改一个产品信息, id=%d", productDto.id);
        Map<String, Object> param = Maps.newHashMap();
        param.put("name", productDto.name);
        param.put("id", productDto.id);
        param.put("userId", uid);

        Logger.info("修改产品名称");
        qicDbUtil.updateWithNameParam("renameProduct", param);

        //先删除产品的信息
        Logger.info("先删除产品的其它信息, productId=%d", productId);
        deleteProductTemplateForEdit(productDto,productId, uid);
        Logger.info("插入产品的其它信息, productId=%d", productId);
        editProductTemplate(productDto, productId);

        Logger.info("结束修改一个产品信息");
        return productId;
    }

    //根据产品id查看产品信息
    @Override
    public Map<String,Object> fetchProductById(long productId,long uid){
        Map<String,Object> map = Maps.newHashMap();
        Map<String,Object> straMap = Maps.newHashMap();
        straMap.put("productId",productId);
        map.put("productId",productId);
        map.put("userId",uid);
        Map<String,Object> resultMap = qicDbUtil.querySingleMapWithNameParam("queryProductNameById",map);
        if(resultMap==null){
            Logger.info("产品名称不存在，productId=%d,userId=%d",productId,uid);
            resultMap = Maps.newHashMap();

        }
        List<Map<String, Object>> riskControlSecritiesList = qicDbUtil.queryMapListWithNameParam("queryRiskControlSecrities",map);
        Logger.info("查询风控证券列表，产品ID为%d", productId);
        List<Map<String,Object>>  strategysList = qicDbUtil.queryMapListWithNameParam("queryStrategyByProductId", map);
        Logger.info("查询策略列表，产品ID为%d",productId);
        if(strategysList != null && strategysList.size()>0){
            for(Map<String,Object> paramMap :strategysList){
                long strategyId = (Long)paramMap.get("strategyId");
                straMap.put("strategyId",strategyId);
                List<Map<String,Object>> list = qicDbUtil.queryMapListWithNameParam("queryTradeAccount",straMap);
                Logger.info("查询账户列表，策略ID为%d",strategyId);
                paramMap.put("accounts",list);
            }
        }
        resultMap.put("riskStocks", riskControlSecritiesList);
        resultMap.put("strategys", strategysList);
        Logger.info("组装Json完成");
        return resultMap;

    }

    /**
     * 查询产品策略的标的信息
     * @param productId
     * @param strategyId
     * @return
     */
    @Override
    public Map<String,Object> fetchStrategyStockInfo(long productId,long strategyId){
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("productId", productId);
        paraMap.put("strategyId", strategyId);
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> securitiesList = qicDbUtil.queryMapListWithNameParam("fetchSecuritiesById", paraMap);
        resultMap.put("securities", securitiesList);
        Logger.info("加入策略标的信息 strategyId=%d,productId=%d", strategyId, productId);
        List<Map<String, Object>> platesList = qicDbUtil.queryMapListWithNameParam("fetchPlatesById", paraMap);
        if (platesList != null && platesList.size() > 0) {
            for (Map<String, Object> map : platesList) {
                Long plateId = (Long) map.get("plateId"); //板块id
                long systemPlateId = ((Number) map.get("systemPlateId")).longValue(); //系统板块id
                List<Map<String, Object>> plateStocksList = null;
                if (systemPlateId == -1) { //自定义板块的
                    Logger.debug("查询自定义板块的成份股, plateId=[%d]", plateId);
                    plateStocksList = fetchCustPlateSecurityInfo(plateId);
                } else {
                    Logger.debug("查询系统板块的成份股,plateId=[%d], systemPlateId=[%d]", plateId, systemPlateId);
                    plateStocksList = fetchSystemPlateSecurityInfo(systemPlateId);
                }
                //屏蔽的股票(这个是板块成分股的子集)
                List<Map<String, Object>> shieldStocksList = qicDbUtil.queryMapList(SqlLoader.getSqlById("fetchPlateShieldSecurityInfo"), plateId);

                for (Map<String, Object> secItem : plateStocksList) {
                    if (shieldStocksList.contains(secItem)) { //在屏蔽股票里, 设置状态为1(屏蔽)
                        secItem.put("status", "1");
                    } else { //不在屏蔽股票里, 设置状态为0(正常)
                        secItem.put("status", "0");
                    }
                }

                map.put("plateStocks", plateStocksList);
            }
            Logger.info("加入策略板块信息 strategyId=%d,productId=%d", strategyId, productId);
        } else {
            platesList = Lists.newArrayList();
            Logger.info("没有板块信息 strategyId=%d,productId=%d", strategyId, productId);
        }

        resultMap.put("plates", platesList);

        resultMap.put("strategyId",strategyId);
        Logger.info("构造产品策略的标的Map完成");

        return resultMap;

    }

    /**
     * 根据策略实例id返回策略信息
     * 在这里进行A+B-C操作（注意：这里还需要剔除当日屏蔽的板块成分股）
     * @param productStrategyId
     * @return
     */
    @Override
    public Map<String,Object> fetchProductStrategyBindInfo(long productStrategyId,boolean isCutPrefix ){
        //初始化两个set 分别对应业务逻辑中的A,B
        HashSet<Map<String,Object>> setA = Sets.newHashSet();
        HashSet<Map<String,Object>> setB = Sets.newHashSet();

        Map<String,Object> resultDataMap = null; //用于最后的数据结果
        HashMap<Object,Object> sqlParaMap = Maps.newHashMap(); //用于sql参数

        sqlParaMap.put("productStrategyId", productStrategyId);
        Logger.info("获得策略的资金使用比例，策略实例ID=%d", productStrategyId);
        resultDataMap = qicDbUtil.querySingleMapWithNameParam("queryfundsProportionById",sqlParaMap);
        if (resultDataMap == null) {
            resultDataMap = Maps.newHashMap();
            Logger.info("不存在策略实例id=%d的记录, 直接返回", productStrategyId);
            return resultDataMap;
        }

        Logger.info("获得策略绑定的账户信息，策略实例ID=%d", productStrategyId);
        List<Map<String, Object>> accountsList = qicDbUtil.queryMapListWithNameParam("queryTradeAccountById", sqlParaMap);
        resultDataMap.put("accounts", accountsList);

        Logger.info("获得策略对应的单个证券列表，策略实例ID=%d",productStrategyId);
        List<Map<String,Object>> listA = qicDbUtil.queryMapListWithNameParam("fetchProductStrategySecurity",sqlParaMap);//策略对应的单个证券列表
        setA.addAll(listA);//放入set集合，去掉重复元素

        Logger.info("获得策略对应的板块成分股，策略实例ID=%d",productStrategyId);
        List<Map<String,Object>> platesIdList = qicDbUtil.queryMapListWithNameParam("fetchPlatesIdByProductStrategyId", sqlParaMap);
        for(Map<String,Object> map : platesIdList){
            List<Map<String, Object>> plateStocksList = null;
            Long plateId = (Long) map.get("plateId"); //板块id
            long systemPlateId = ((Number) map.get("systemPlateId")).longValue(); //系统板块id
            if (systemPlateId == -1) { //自定义板块
                Logger.debug("查询自定义板块的成份股, plateId=[%d]", plateId);
                plateStocksList = fetchCustPlateSecurityInfo(plateId);
            } else { //系统板块
                Logger.debug("查询系统板块的成份股, plateId=[%d], systemPlateId=[%d]", plateId, systemPlateId);
                plateStocksList = fetchSystemPlateSecurityInfo(systemPlateId); //取系统板块的成分股
            }
            Logger.debug("查询板块的 maxPosition, plateId=[%d]", plateId);
            Map<String,Object> maxPositionMap = qicDbUtil.querySingleMap(SqlLoader.getSqlById("fetchPlateMaxPosition"),plateId); //取板块的 maxPosition
            for(Map<String,Object> item : plateStocksList){
                item.put("maxPosition", maxPositionMap.get("maxPosition"));
            }
            setB.addAll(plateStocksList);
        }

        Logger.info("按产品策略id获取 风控证券 + 当日已屏蔽的成分股: 策略实例ID=%d", productStrategyId);
        //这里就是C
        List<String> riskSecAddShieldSecList = qicDbUtil.queryWithHandlerWithNameParam("fetchRiskSecAddShieldSecByProductStrategyId", new ColumnListHandler<String>(), sqlParaMap);

        //以下处理 A + B 同时按照业务逻辑，当股票代码和市场相同时，取最大持仓量最小的成分股
        setA.addAll(setB);
        Map<String, Number> secMaxPositionMap = Maps.newHashMap(); //用 symbol + "," + market 作为key, maxPosition作为value
        for (Map<String, Object> item : setA) {
            String symbol = (String) item.get("symbol");
            String market = (String) item.get("market");
            Number maxPosition = (Number) item.get("maxPosition");
            if (!item.containsKey("srcSymbol")) { //如果不存在这样的key值,就放入空字符串做为值
                item.put("srcSymbol", "");
            }
            String key = symbol + "," + market;
            if (secMaxPositionMap.containsKey(key)) {
                Number val = secMaxPositionMap.get(key); // map里的原始值
                if (val.doubleValue() > maxPosition.doubleValue()) { //取小值放入map
                    secMaxPositionMap.put(key, maxPosition);
                }
            }else {
                secMaxPositionMap.put(key, maxPosition);
            }
        }

        //这里实现 (A+B) - C
        secMaxPositionMap.keySet().removeAll(riskSecAddShieldSecList);


        //得到真实映射的map
        Map<String, String> allRealSymbolMap = gtaDataDbUtil.queryWithHandler(SqlLoader.getSqlById("fetchAllMainContractAndContinuingContractRealSymbol"),
                new AbstractKeyedHandler<String, String>() {
                    @Override
                    protected String createKey(ResultSet rs) throws SQLException {
                        return rs.getString("comcode");
                    }

                    @Override
                    protected String createRow(ResultSet rs) throws SQLException {
                        return rs.getString("SYMBOL");
                    }
                });

        Logger.info("把最后的计算结果转化出来..");
        Splitter splitter = Splitter.on(',').limit(2);
        List<Map<String,Object>>  securityResultMap = Lists.newArrayList(); //最后计算出来的证券集合
        for (Map.Entry<String, Number> entry : secMaxPositionMap.entrySet()) {
            Map<String,Object> item = Maps.newHashMap();
            item.put("maxPosition", entry.getValue());
            Iterator<String> iterator = splitter.split(entry.getKey()).iterator();
            String symbol = iterator.next();
            String market = cutMarketPrefix(iterator.next()); //去掉前缀. .net的上传了类型 ExchangeType.CFFEX 这样的字符串
            String key = market + symbol;
            if (allRealSymbolMap.containsKey(key)) { //包含的话,就把真实的证券代码放入
                item.put("srcSymbol", allRealSymbolMap.get(key));
            } else {
                item.put("srcSymbol", "");
            }
            item.put("symbol", symbol);
            item.put("market", isCutPrefix ? cutMarketPrefix(market):market);
            securityResultMap.add(item);
        }

        resultDataMap.put("securities", securityResultMap);
        Logger.info("A+B-C 运算完成");
        return resultDataMap;
    }

    private String cutMarketPrefix(String marketName){
        if(StringUtils.isEmpty(marketName)){
            return marketName;
        }else if(marketName.indexOf(".") <=0){
            return marketName;
        }else{
            return marketName.substring(marketName.indexOf(".")+1);
        }
    }

    /**
     * 返回当日主力连续合约全部选项
     * @return
     */
    @Override
    public List<Map<String,Object>> fetchAllMainContractAndContinuingContract(){
        return  gtaDataDbUtil.queryMapList(SqlLoader.getSqlById("fetchAllMainContractAndContinuingContract"));
    }

    /**
     * 根据用户id查询产品id
     * @param uid
     * @return
     */
    public List<Long> fetchProductIdByUid(long uid){
        Map<String,Object> map = Maps.newHashMap();
        map.put("userId",uid);
        return  qicDbUtil.queryWithHandlerWithNameParam("fetchProductIdByUid",new ColumnListHandler<Long>(),map);
    }

    public List<Map<String,Object>> fetchAllRuntimeStrategyAcconts(){
        List<Map<String,Object>>  list = Lists.newArrayList();

        String sql = SqlLoader.getSqlById("fetchAllRuntimeStrategyAcconts");
        ListMultimap<Long, Map<String, Object>> multimap = qicDbUtil.queryWithHandler(sql, new AbstractKeyedMutiMapHandler<Long, Map<String, Object>>() {

            @Override
            protected Map<String, Object> createRow(ResultSet rs) throws SQLException {
                Map<String, Object> map = Maps.newHashMap();
                map.put("account", rs.getString("account"));
                map.put("initCapital", rs.getBigDecimal("initCapital"));
                return map;
            }

            @Override
            protected Long createKey(ResultSet rs) throws SQLException {
                return rs.getLong("productStrategyId");
            }

            @Override
            protected ListMultimap<Long, Map<String, Object>> createListMultimap() {
                return LinkedListMultimap.create();
            }
        });

        for (Map.Entry<Long, Collection<Map<String, Object>>> entry : multimap.asMap().entrySet()) {
            Map<String,Object> resultMap = Maps.newHashMap();
            resultMap.put("id", entry.getKey());
            resultMap.put("accounts", Lists.newArrayList(entry.getValue()));
            list.add(resultMap);
        }

        return list;
    }


}
