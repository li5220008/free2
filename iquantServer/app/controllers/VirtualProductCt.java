package controllers;

import bussiness.product.IVirtualProductService;
import bussiness.strategy.IStrategyService;
import bussiness.tradeAccount.ITradeAccountService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import dbutils.SqlLoader;
import models.iquantCommon.TradeAccountDto;
import play.data.binding.As;
import play.data.validation.Required;
import play.mvc.With;
import util.CommonUtils;
import util.ConstantInterface;
import util.GsonUtil;
import util.LoginTokenCompose;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 产品信息api
 * User: wenzhihong
 * Date: 13-5-2
 * Time: 下午6:45
 */
@With(value = {GeneralIntercept.class})
public class VirtualProductCt extends BaseController {
     @Inject
    static IVirtualProductService virtualProductService;
    @Inject
    static ITradeAccountService tradeAccountService;
    @Inject
    static IStrategyService strategyService;

    /**
     * 根据策略的状态返回用户的策略列表
     * 返回json格式
     * <pre>
     *     {
     *         "id" : 1,
     *         "name" : "策略1",
     *         "status" : 1
     *     }
     * </pre>
     * status 说明: 策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试 3. 回测中 4. 上架 5 下架
     */
    public static void fetchStrategyWithState(int status) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> mapList = strategyService.findStrategyByStatus( status,compose.uid);
        renderJSON(mapList);
    }

    /**
     * 增加一个交易帐号, 用json参数提交
     * json格式为:
     * <pre>
     *{
     *  "name":"test",
     *  "account" : "1232",
     *  "password" : "1332",
     *  "type" : 1,
     *  "clientId" : "232323", //交易柜台
     *  "targetCompId" : "compid_id", //帐号命令路由地址
     *  "hedgeType" : 0
     *}
     * type 取值为: 0:期货, 1:股票
     * hedgeType 取值为: 0. 投机(Speculation), 1. 套保(Hedge)
     * </pre>
     */
    public static void addTradeAccount() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String bodyJson = fetchBody();
        Gson gson = GsonUtil.createGson();
        TradeAccountDto accountDto = gson.fromJson(bodyJson, TradeAccountDto.class);
        accountDto.userId = compose.uid;
       //  去掉, 需求说帐号不要进行重名的判断
        if (virtualProductService.accountHasSameName(accountDto.userId, accountDto.name, null)) {
            addErrorMsg("name", "名称重复");
            rendOpFail();
        }

        Long id = tradeAccountService.addTradeAccount(accountDto);
        opAddSuccess(id);
    }

    /**
     * 删除一个交易帐号
     * @param id 交易帐号id
     */
    public static void delTradeAccount(long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
       int effect =  tradeAccountService.delTradeAccount(id,compose.uid);
        opDelSuccess(id, effect);
    }

    /**
     * 修改一个交易帐号信息. 用json参数提交:
     * json格式为:
     * <pre>
     *{
     *  "id" : 1
     *  "name" : "test",
     *  "account" : "1232",
     *  "password" : "1332",
     *  "type" : 1,
     *  "clientId" : "232323", //交易柜台
     *  "targetCompId" : "compid_id", //帐号命令路由地址
     *  "hedgeType" : 0
     *}
     *  type 取值为: 0:期货, 1:股票
     *  hedgeType 取值为: 0. 投机(Speculation), 1. 套保(Hedge)
     * </pre>
     */
    public static void editTradeAccount() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String bodyJson = fetchBody();
        Gson gson = GsonUtil.createGson();
        TradeAccountDto accountDto = gson.fromJson(bodyJson, TradeAccountDto.class);
        accountDto.userId = compose.uid;
        // 去掉, 需求说不要进行帐户重名的判断
        if (virtualProductService.accountHasSameName(accountDto.userId, accountDto.name, accountDto.id)) {
            addErrorMsg("name", "名称重复");
            rendOpFail();
        }
        int effect = tradeAccountService.updateTradeAccount(accountDto);
        opEditSuccess(accountDto.id);
    }

    /**
     * 返回用户的交易帐号. (还没有绑定产品的, 5.30是要这样, 7.30又要改成全部)
     * 返回json数组格式. 每个项为:
     * <pre>
     * {
     *     "id" : 123,
     *     "name" : "test",
     *     "account" : "2323",
     *     "password" : "2323",
     *     "type" : 1,
     *     "clientId" : "232323", //交易柜台
     *     "targetCompId" : "compid_id", //帐号命令路由地址
     *     "hedgeType" : 0
     * }
     * </pre>
     */
    public static void fetchTradeAccount() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        //String sql = SqlLoader.getSqlById("fetchTradeAccount");  7.30使用
        List<Map<String, Object>> mapList = tradeAccountService.findUserTradeAccount(compose.uid);
        renderJSON(mapList);
    }

    /**
     * 增加一个虚拟产品(全部信息). 用json参数提交
     * json格式为
     <pre>
     {
     	"name":"产品1",
     	"tradeAccounts": [
            {
     		    "refId" : -1,  //关联id, -1表示是新加
     		    "accountId" : 1 //帐号id
     	    }
        ],
     	"strategys":[
     		{
     			"refId" : -1, //关联id, -1表示是新加
     			"strategyId" : 122, //策略id
     			"fundsProportion" : 0.5, //资金使用比例
     			"strategySecInfos" : [
     				{
     					"contractMultiplier": 0.1,  //合约乘数
     					"marginLevel": 1,           //保证金比例
     					"maxShare": 3.3,            //最大持仓量
     					"currency": "CNY",          //币种
     					"exchangeType": "ExchangeType.SZSE", //市场代码
     					"secId": "000001",             //标的代码
     					"secName": "平安银行"          //标的名称
     				}
     			]
     		}
     	]
     }
     </pre>
     返回这个产品的全部信息. json格式为:
     <pre>
     {
         "id" : 1,
         "name" : "产品1",
         "strategys" : [
             {
                 "refId" : 1,
                 "strategyId" : 23, //策略id
                 "name" : "策略1",
                 "fundsProportion" : 0.3, //资金使用比例
                 "strategyParam" : [
                    {
                            "contractMultiplier": 0.1,  //合约乘数
          					"marginLevel": 1,           //保证金比例
          					"maxShare": 3.3,            //最大持仓量
          					"currency": "CNY",          //币种
          					"exchangeType": "ExchangeType.SZSE", //市场代码
          					"secId": "000001",             //标的代码
          					"secName": "平安银行"          //标的名称
                    }
                 ]
             }
         ],
         "tradeAccounts" : [
             {
                 "refId" : 1,
                 "accountId" : 33, //帐号id
                 "name" : "帐号1",
                 "account" : "12323",
                 "password" : "232",
                 "type" : 1
             }
         ]
     }
     </pre>
     */
    public static void addVirtualProductFullInfo() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String body = fetchBody();
        long vpId = virtualProductService.addVirtualProductFullInfo(compose, body);
        if (vpId == CommonUtils.HAS_SAME_NAME_ERROR) {
            addErrorMsg("name", "名称重复");
            rendOpFail();
        }
        Map<String, Object> vpInfo = virtualProductService.findVirtrualProductById(vpId);
        renderJSON(vpInfo);
    }

    /**
     * 修改产品信息. body json格式参数
     <pre>
     {
        "id" : 1, //产品id
        "name":"产品1",
        "tradeAccounts":[
            {
                "refId" : -1,  //关联id, -1表示是新加
                "accountId" : 1 //帐号id
            }
        ],
        "strategys":[
            {
                "refId" : -1, //关联id, -1表示是新加
                "strategyId" : 122, //策略id
                "fundsProportion" : 0.5, //资金使用比例
                "strategySecInfos" : [
                    {
                        "contractMultiplier": 0.1,  //合约乘数
                        "marginLevel": 1,           //保证金比例
                        "maxShare": 3.3,            //最大持仓量
                        "currency": "CNY",          //币种
                        "exchangeType": "ExchangeType.SZSE", //市场代码
                        "secId": "000001",             //标的代码
                        "secName": "平安银行"          //标的名称
                    }
                ]
            }
        ]
     }
     </pre>
     */
    public static void editVirtualProduct(){
        LoginTokenCompose compose = LoginTokenCompose.current();
        String body = fetchBody();
        long productId = virtualProductService.editVirtualProduct(compose, body);
        if (productId == CommonUtils.HAS_SAME_NAME_ERROR) {
            addErrorMsg("name", "名称重复");
            rendOpFail();
        }
        opEditSuccess(productId);
    }


    /**
     * 软删除一个产品.
     * 会软删除产品记录及关联的帐号,策略关联关系
     * @param id 产品id
     */
    public static void delVirtualProduct(Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = virtualProductService.delVirtualProduct(id, compose);
        opDelSuccess(id, effect);
    }

    /**
     * 返回用户的全部的虚拟产品列表.
     * 返回产品信息列表. 上面列出的json数组格式
     */
    public static void fetchUserVirtualProduct() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        String sql = SqlLoader.getSqlById("fetchUserVirtualProductId");
        List<Long> vpids = virtualProductService.fetchUserVitrualProdructIds(compose.uid);
        List<Map<String, Object>> vpInfoList = Lists.newArrayListWithCapacity(vpids.size());
        for (Long id : vpids) {
            vpInfoList.add(virtualProductService.findVirtrualProductById(id));
        }

        renderJSON(vpInfoList);
    }



    /**
     * 返回策略标的信息
     */
    public static void fetchStrategyStockInfo(@As(",") List<Long> ids){
        LoginTokenCompose compose = LoginTokenCompose.current();
        String sql = SqlLoader.getSqlById("fetchStrategyStockInfo");
        List<Map<String,Object>> jsonList =strategyService.findStrategyStockInfo(ids,compose.uid);
        renderJSON(jsonList);
    }
    /**
     * 取本产品(登陆产品)功能列表
     */
    public static void fetchFuction() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> funList = virtualProductService.fetchFunctionByUserAndProduct(compose.uid, compose.pid);
        renderJSON(funList);
    }

    /**
     * 获取其它产品功能列表
     *
     * @param pid 产品id
     */
    public static void fetchOtherProductFunction(@Required(message = ConstantInterface.PID_REQUIRED) Long pid) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String, Object>> funList = virtualProductService.fetchFunctionByUserAndProduct(compose.uid, pid);
        renderJSON(funList);
    }

}
