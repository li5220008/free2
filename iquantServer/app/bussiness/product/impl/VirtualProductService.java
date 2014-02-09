package bussiness.product.impl;

import bussiness.common.impl.BaseService;
import bussiness.product.IVirtualProductService;
import bussiness.strategy.IStrategyService;
import bussiness.strategy.IUserStrategyManageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import dbutils.SqlLoader;
import models.iquantCommon.VirtualProductDto;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import play.Logger;
import play.libs.Codec;
import play.modules.guice.InjectSupport;
import util.CommonUtils;
import util.LoginTokenCompose;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-27
 * Time: 下午6:02
 * 功能描述: 产品操作类 sso 迁移过来的
 */
@InjectSupport
public class VirtualProductService extends BaseService implements IVirtualProductService {
    @Inject
    static IStrategyService strategyService;
    @Inject
    static IUserStrategyManageService userStrategyManageService;
    @Override
    public boolean accountHasSameName(long uid, String name, Long id) {
        String sameNameCount = SqlLoader.getSqlById("tradeAccountSameNameCount");
        if (id != null) {
            sameNameCount += " and id <> " + id.toString();
        }
        return qicDbUtil.queryCount(sameNameCount, uid, name) > 0;
    }

    @Override
    public boolean virtualProductHasSameName(long uid, String name, Long id) {
        String sameNameCount = SqlLoader.getSqlById("virtualProductSameNameCount");
        if (id != null) {
            sameNameCount += " and id <> " + id.toString();
        }
        return qicDbUtil.queryCount(sameNameCount, uid, name) > 0;
    }

    @Override
    public long addVirtualProductFullInfo(LoginTokenCompose compose, String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        String name = jsonObject.getString("name");
        Logger.info("增加产品信息开始");
        if (virtualProductHasSameName(compose.uid, name, null)) {
            Logger.info("产品名称重复");
            return CommonUtils.HAS_SAME_NAME_ERROR;
        }

        VirtualProductDto vpDto = new VirtualProductDto();
        vpDto.userId = compose.uid;
        vpDto.name = name;
        vpDto.uuid = Codec.UUID().replaceAll("-", "");
        //String addVpSql = SqlLoader.getSqlById("addVirtualProduct");
        long vpId = qicDbUtil.insertWithNameParam("addVirtualProduct", null);
        Logger.info("往产品表增加记录, name=%s, id=%d", name, vpId);

        Map<String, Object> vpParam = Maps.newHashMap();
        vpParam.put("productId", vpId);
        vpParam.put("userId", compose.uid);
        vpParam.put("ctime", new Date());
        //解析strategys信息
        JSONArray strategys = jsonObject.getJSONArray("strategys");
        for (Object o : strategys) {
            JSONObject strategyObj = (JSONObject) o;
            Long refId = strategyObj.getLong("refId");
            if (refId == null || refId == -1) {
                vpParam.put("strategyId", strategyObj.getLongValue("strategyId")); //策略id
                vpParam.put("fundsProportion", strategyObj.getDoubleValue("fundsProportion")); //资金比例
                String strategyParam = strategyObj.getJSONArray("strategySecInfos").toJSONString();
                vpParam.put("strategyParam", strategyParam);
                long id = qicDbUtil.insertWithNameParam("addVirtualProductAndStrategyRelevance", vpParam);//返回产品策略关联关系id
                userStrategyManageService.syncProductToQsm(id, strategyObj.getLongValue("strategyId"));
                Logger.info("增加产品策略关联关系, strategyId=%d, 返回id值=%d", strategyObj.getLongValue("strategyId"), id);
            }
        }

        //帐号
        JSONArray tradeAccounts = jsonObject.getJSONArray("tradeAccounts");
        for (Object o : tradeAccounts) {
            JSONObject tradeAccount = (JSONObject) o;
            Long refId = tradeAccount.getLong("refId");
            if (refId == null || refId == -1) {
                vpParam.put("accountId", tradeAccount.getLong("accountId"));
                long id = qicDbUtil.insertWithNameParam("addVirtualProductAndAccountRelevance", vpParam);
                Logger.info("增加产品帐号关联关系, accountId=%d, 返回id值=%d", tradeAccount.getLong("accountId"), id);
            }
        }
        Logger.info("增加产品信息结束");
        return vpId;
    }
    public  long editVirtualProduct(LoginTokenCompose compose, String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        long productId = jsonObject.getLongValue("id"); //产品id
        String name = jsonObject.getString("name");
        Logger.info("修改产品信息开始");
        if (virtualProductHasSameName(compose.uid, name, productId)) {
            Logger.info("产品名称重复");
            return CommonUtils.HAS_SAME_NAME_ERROR;
        }

        //修改名称
        String sql = SqlLoader.getSqlById("renameVirtualProduct");
        qicDbUtil.update(sql, name, productId, compose.uid);
        Logger.info("修改产品名称,新名称为:%s", name);

        Map<String, Object> param = Maps.newHashMap();
        param.put("productId", productId);
        param.put("userId", compose.uid);
        param.put("ctime", new Date());

        String fetchAccountByVpIdSql = SqlLoader.getSqlById("fetchAccountByVpId");
        //从数据库里加载帐号关联id
        List<Long> accountRefIdListFromDb = qicDbUtil.queryWithHandler(fetchAccountByVpIdSql, new AbstractListHandler<Long>() {
            @Override
            protected Long handleRow(ResultSet rs) throws SQLException {
                return rs.getLong("refId");
            }
        }, productId);
        Logger.info("修改产品,从数据库里加载产品与账号关联关系,refIds=" + accountRefIdListFromDb);

        //修改帐号关联信息
        JSONArray tradeAccountsFromUpload = jsonObject.getJSONArray("tradeAccounts"); //用户上传的
        for (Object o : tradeAccountsFromUpload) {
            JSONObject accountUploadItem = (JSONObject) o;
            Long accountRefId = accountUploadItem.getLong("refId");
            if (accountRefId != null) {
                if (accountRefId == -1) { //说明是新加
                    Long accountId = accountUploadItem.getLong("accountId");
                    if (accountId != null) { //帐号不为空
                        param.put("accountId", accountId);
                        long id = qicDbUtil.insertWithNameParam("addVirtualProductAndAccountRelevance", param);
                        Logger.info("修改产品,增加产品与帐号的关系, accountId=%d, 返回id=%d", accountId, id);
                    }
                } else { //存在, 则说明要修改. 不过这里没有要修改的操作
                    accountRefIdListFromDb.remove(accountRefId); //移除掉已出现的, 则剩下的就是要删除的
                }
            }
        }
        //删除掉不要的关联关系
        String softDelSql = SqlLoader.getSqlById("softDelVirtualProductTradeAccountRelevance");
        for (Long refId : accountRefIdListFromDb) {
            qicDbUtil.update(softDelSql, refId, compose.uid);
        }
        if (accountRefIdListFromDb.size() > 0) {
            Logger.info("修改产品,删除产品与帐号的关系, refIds=" + accountRefIdListFromDb);
        }

        //从数据库里加载策略关联的id
        String fetchStrategyByVpIdSql = SqlLoader.getSqlById("fetchStrategyByVpId");
        List<Long> strategyRefIdListFromDb = qicDbUtil.queryWithHandler(fetchStrategyByVpIdSql, new AbstractListHandler<Long>() {
            @Override
            protected Long handleRow(ResultSet rs) throws SQLException {
                return rs.getLong("refId");
            }
        }, productId);
        Logger.info("修改产品,从数据库里加载产品与策略关联关系,refIds=" + strategyRefIdListFromDb);

        //修改策略关联信息
        JSONArray strategyFromUpload = jsonObject.getJSONArray("strategys"); //用户上传的
        for (Object o : strategyFromUpload) {
            JSONObject strategyUploadItem = (JSONObject) o;
            Long strategyRefId = strategyUploadItem.getLong("refId");
            if (strategyRefId != null) {
                Long strategyId = strategyUploadItem.getLong("strategyId");
                param.put("refId", strategyRefId);
                param.put("strategyId", strategyId);
                param.put("fundsProportion", strategyUploadItem.getDouble("fundsProportion"));
                if (strategyUploadItem.getJSONArray("strategySecInfos") != null) {
                    param.put("strategyParam", strategyUploadItem.getJSONArray("strategySecInfos").toJSONString());
                }else {
                    param.put("strategyParam", "[]"); //TODO 如果没有的话, 先放个空数组
                }

                if (strategyRefId == -1) { //新加
                    if (strategyId != null) { //策略不为空
                        long id = qicDbUtil.insertWithNameParam("addVirtualProductAndStrategyRelevance", param);
                        userStrategyManageService.syncProductToQsm(id, strategyId);
                        Logger.info("修改产品,增加产品与策略的关系, strategyId=%d, 返回id=%d", strategyId, id);
                    }
                } else { //修改
                    strategyRefIdListFromDb.remove(strategyRefId);
                    sql = SqlLoader.getSqlById("editVirtualProductAndStrategyRelevance");
                    qicDbUtil.updateWithNameParamMap(sql, param);
                    Logger.info("修改产品,修改产品与策略的关系, strategyId=%d, refId=%d", strategyId, strategyRefId);
                }
            }
        }

        //删除掉不要的关联关系
        softDelSql = SqlLoader.getSqlById("softDelVirtualProductStrategyRelevance");
        for (Long refId : strategyRefIdListFromDb) {
            qicDbUtil.update(softDelSql, refId, compose.uid);
            userStrategyManageService.deleteProductFromQsm(refId);
        }

        if (strategyRefIdListFromDb.size() > 0) {
            Logger.info("修改产品, 删除产品与策略的关系, refIds=" + strategyRefIdListFromDb);
        }

        Logger.info("修改产品信息结束");
        return productId;
    }
    @Override
    public int delVirtualProduct(Long id, LoginTokenCompose compose) {
        Long productId = id;
        Logger.info("删除产品信息(%d)开始", productId);
        //先软删除关联关系
        //String sql = SqlLoader.getSqlById("delVirtualProductStrategyRefByPid");
        String sql = SqlLoader.getSqlById("softDelVirtualProductStrategyRefByPid");
        qicDbUtil.update(sql, productId, compose.uid);
        Logger.info("软删除产品与策略关联关系");

        //删除qsm qstrategy 策略运行列表
        List<Long> refIds = findrefIdsByProdId(productId,compose);
        for(long refId : refIds){
            userStrategyManageService.deleteProductFromQsm(refId);
        }
        //sql = SqlLoader.getSqlById("delVirtualProductTradeAccountRefByPid");
        sql = SqlLoader.getSqlById("softDelVirtualProductTradeAccountRefByPid");
        qicDbUtil.update(sql, productId, compose.uid);
        Logger.info("软删除产品与帐号关联关系");

        //在删除产品信息
        //sql = SqlLoader.getSqlById("delVirtualProduct");
        sql = SqlLoader.getSqlById("softDelVirtualProduct");
        int update = qicDbUtil.update(sql, productId, compose.uid);
        Logger.info("删除产品信息结束");
        return update;
    }

    @Override
    public List<Long> findrefIdsByProdId(Long prodId, LoginTokenCompose compose) {
        String    sql = SqlLoader.getSqlById("findrefIdsByProdId");
        return qicDbUtil.queryWithHandler(sql, new ColumnListHandler<Long>(), prodId,compose.uid);
    }
    public  List<Map<String, Object>> fetchFunctionByUserAndProduct(long uid, long pid) {
        String sql = SqlLoader.getSqlById("fetchUserFunctionInfoWithProduct");
        return qicDbUtil.queryMapList(sql, uid, pid);
    }

    @Override
    public Map<String, Object> findVirtrualProductById(long id) {
        Map<String, Object> vpInfo = Maps.newHashMap();
        String sql = SqlLoader.getSqlById("fetchVpNameById");
        String vpName = qicDbUtil.queryWithHandler(sql, new ScalarHandler<String>(), id);

        if (vpName == null) { //不允许访问

        } else {
            vpInfo.put("name", vpName);
            vpInfo.put("id", id);

            String strategySql = SqlLoader.getSqlById("fetchStrategyByVpId");
            List<Map<String, Object>> strategy = qicDbUtil.queryWithHandler(strategySql, new AbstractListHandler<Map<String, Object>>() {
                @Override
                protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("refId" , rs.getLong("refId"));
                    map.put("strategyId" , rs.getLong("strategyId"));
                    map.put("name" , rs.getString("name"));
                    Date ctime = rs.getDate("ctime");
                    if (ctime != null) {
                        map.put("ctime", CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", ctime));
                    }

                    map.put("fundsProportion" , rs.getDouble("fundsProportion"));
                    String strategyParamStr = rs.getString("strategyParam");
                    map.put("strategyParam" , JSON.parseArray(strategyParamStr));
                    return map;
                }
            }, id);

            String accountSql = SqlLoader.getSqlById("fetchAccountByVpId");
            List<Map<String, Object>> tradeAccount = qicDbUtil.queryMapList(accountSql, id);

            vpInfo.put("strategys", strategy);
            vpInfo.put("tradeAccounts", tradeAccount);
        }

        return vpInfo;
    }

    @Override
    public List<Long> fetchUserVitrualProdructIds(long uid) {
        String sql = SqlLoader.getSqlById("fetchUserVirtualProductId");
        List<Long> vpids = qicDbUtil.queryWithHandler(sql, new AbstractListHandler<Long>() {

            @Override
            protected Long handleRow(ResultSet rs) throws SQLException {
                return rs.getLong(1);
            }
        }, uid);
        return vpids;
    }
}
