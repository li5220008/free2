package controllers;

import annotation.UnCheck;
import bussiness.product.IProductService;
import bussiness.strategy.IStrategyService;
import bussiness.tradeAccount.ITradeAccountService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.iquantCommon.TradeAccountDto;
import models.iquantCommon.productinfo.ProductDto;
import models.iquantCommon.productinfo.ProductStrategyAccountDto;
import models.iquantCommon.productinfo.ProductStrategyDto;
import play.Logger;
import play.mvc.With;
import protoc.ResponseHeader;
import util.GsonUtil;
import util.LoginTokenCompose;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 1.1版本交易设置 交易设置
 * User: wenzhihong
 * Date: 13-9-18
 * Time: 下午4:30
 */
@With(value = {LoginTokenCheckIntercept.class})
public class ProductCt extends BaseController {

    @Inject
    static IProductService productService;

    @Inject
    static IStrategyService strategyService;

    @Inject
    static ITradeAccountService tradeAccountService;

    public static void test(){
        LoginTokenCompose compose = LoginTokenCompose.current();
        ProductDto dto = ProductDto.demo();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();
        String aa = gson.toJson(dto);

        Long productId = productService.addProduct(dto, compose.uid);

        //renderJSON(aa);
        renderText("产品id=%d", productId);
    }

    public static void testEdit(){
        LoginTokenCompose compose = LoginTokenCompose.current();
        ProductDto dto = ProductDto.demo();
        dto.id = 1L;
        dto.name = "我的修改";
        productService.editProduct(dto, compose.uid);

        renderText("修改成功");
    }

    /**
     * 查询策略原始配制的标的信息
     * @param id 策略id
     */
    public static void fetchStrategySecurityOriginal(long id){
        Map<String,Object> item =  strategyService.fetchStrategySecurityOriginal(id);
        responseJSON(item);
    }

    /**
     * 新增一个产品
     */
    public static void addProduct(){

        String body = fetchBody();
        Logger.info("新建产品, 接收到的json字符串:\n%s", body);
        Gson gson = GsonUtil.createGson();
        ProductDto productDto = null;
        try {
            productDto = gson.fromJson(body, ProductDto.class);
        } catch (Exception e) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("jsonerror", "json格式不符合协议要求的格式");
            responseError(errorEntry);
        }
        LoginTokenCompose compose = LoginTokenCompose.current();
        if (productService.productHasSameName(compose.uid, productDto.name, null)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "产品名称已存在");
            responseError(errorEntry);
        }
        Long productId = productService.addProduct(productDto, compose.uid);
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",productId);
        responseJSON(map);
    }

    public static void editProduct(){
        String body = fetchBody();
        Logger.info("修改产品, 接收到的json字符串为:\n%s", body);
        Gson gson = GsonUtil.createGson();
        ProductDto productDto = null;
        try{
            productDto = gson.fromJson(body, ProductDto.class);
        }catch (Exception e){
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("jsonerror", "json格式不符合协议要求的格式");
            responseError(errorEntry);
        }

        LoginTokenCompose compose = LoginTokenCompose.current();
        productDto.userId = compose.uid;
        if (productService.productHasSameName(compose.uid, productDto.name, productDto.id)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "产品名称已存在");
            responseError(errorEntry);
        }

        long productId = productService.editProduct(productDto, compose.uid);
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",productId);
        responseJSON(map);
    }

    /**
     * 根据系统板块id查成分股 (自定义板块也可以查询)
     *
     * @param id 板块id
     */
    public static void fetchPlateSecurityInfo(long id) {
        List<Map<String, Object>> secList = productService.fetchSystemPlateSecurityInfo(id);
        responseJSON(secList);
    }

    /**
     * 软删除一个产品.
     * 会软删除产品记录及关联的帐号,策略关联关系
     * @param id 产品id
     */
    public static void delProduct(Long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect = productService.deleteProduct(id, compose.uid);
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",id);
        map.put("effect",effect);
        responseJSON(map);
    }

    /**
     * 查询用户的产品列表
     */
    public static void fetchShortProductInfo(){
      LoginTokenCompose compose = LoginTokenCompose.current();
      List<ProductDto> productDtoList = productService.findUserProducts(compose.uid);
      for(ProductDto productDto:productDtoList){
           List<ProductStrategyDto> productStrategyDtos = productService.findProductStrategy(productDto.id);
          productDto.strategys = productStrategyDtos;
          for(ProductStrategyDto productStrategyDto :productStrategyDtos){
             List<ProductStrategyAccountDto> productStrategyAccountDtoList =productService.findProductStrategyAccount(productStrategyDto.id);
              productStrategyDto.accounts = productStrategyAccountDtoList;
          }
      }
        responseJSON(productDtoList);
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
     *  "targetCompId" : "compid_id", //期货公司客户号
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
        //重名判断
        if (productService.accountHasSameName(accountDto.userId, accountDto.name, null)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "名称已存在");
            responseError(errorEntry);
        }
        if (productService.accountHasSameAccount(accountDto.account,accountDto.type,null)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "账号已存在");
            responseError(errorEntry);
        }
        tradeAccountService.addTradeAccount11(accountDto);
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",accountDto.id);
        responseJSON(map);
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
     *  "targetCompId" : "compid_id", //期货公司客户号
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
        // 重名判断
        if (productService.accountHasSameName(accountDto.userId, accountDto.name, accountDto.id)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "名称已存在");
            responseError(errorEntry);
        }
        if (productService.accountHasSameAccount(accountDto.account,accountDto.type,accountDto.id)) {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", "账号已存在");
            responseError(errorEntry);
        }
        int effect = tradeAccountService.updateTradeAccount11(accountDto);
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",accountDto.id);
        responseJSON(map);
    }


    /**
     * 删除一个交易帐号
     * @param id 交易帐号id
     */
    public static void delTradeAccount(long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        int effect =  tradeAccountService.delTradeAccount11(id, compose.uid);
        if(effect>0){
            Map<String,Object> map = Maps.newHashMap();
            map.put("id",id);
            map.put("effect",effect);
            responseJSON(map);
        }else {
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("used", "该账号处于占用状态");
            responseError(errorEntry);
        }
    }

    /**
     * 返回用户的交易帐号.
     * 返回json数组格式. 每个项为:
     * <pre>
     * {
     *     "id" : 123,
     *     "name" : "test",
     *     "type" : 1,
     *     "account" : "2323",
     *     "userd" : 0,//是否使用. 0: 没有使用, 1: 使用
     * }
     * </pre>
     */
    public static void fetchTradeAccountList() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<TradeAccountDto> list = tradeAccountService.findUserTradeAccountList(compose.uid);
        responseJSON(list);
    }

    /**
     * 验证账号的唯一性
     * @param accountName
     */
    public static void verifyTradeAccountisExist(String accountName){
        LoginTokenCompose compose = LoginTokenCompose.current();
        Map<String,Object> map = Maps.newHashMap();
        responseJSON(productService.accountHasSameName(compose.uid,accountName, null));
    }

    /**
     * 根据账户ID 查询该账户信息
     * @param id
     */
    public static void fetchTradeAccount(long id) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        TradeAccountDto tradeAccountDto = tradeAccountService.findTradeAccount(id, compose.uid);
        responseJSON(tradeAccountDto);
    }


    //region 给QSE提供的接口
    /**
     * 查询当前系统已使用的全部帐号信息
     */
    public static void fetchAllInUsedTradeAccount(){
       List<TradeAccountDto> list = tradeAccountService.fetchAllInUsedTradeAccount();
        responseJSON(list);
    }

    /**
     * 根据策略实现id(也就是产品策略绑定id)返回这个策略的详细信息(包含资金使用比例,帐号列表,交易标的(已经做了 A + B – C))
     * @param productStrategyId 策略实例id
     */
    public static void fetchProductStrategyBindInfo(long productStrategyId){
        Map<String,Object> map = productService.fetchProductStrategyBindInfo(productStrategyId,false);
        responseJSON(map);
    }
    //endregion

    public static void fetchStrategyByUserId(){
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Map<String,Object>> list = strategyService.fetchStrategyByUserId(compose.uid);
        responseJSON(list);
    }

    /**
     * 根据产品id查看产品信息
     * @param id
     */
    public static void fetchProductById(long id){
        LoginTokenCompose compose = LoginTokenCompose.current();
        Map<String,Object> map = productService.fetchProductById(id,compose.uid);
        responseJSON(map);

    }

    /**
     * 查询产品策略的标的信息
     * @param productId
     * @param strategyId
     */
    public static void fetchStrategyStockInfo(long productId,long strategyId){
        Map<String,Object> map = productService.fetchStrategyStockInfo(productId,strategyId);
        responseJSON(map);
    }

    /**
     * 返回当日主力连续合约全部选项
     */
    public static void fetchAllMainContractAndContinuingContract(){
        List<Map<String,Object>> list = productService.fetchAllMainContractAndContinuingContract();
        responseJSON(list);
    }

    public static void fetchProductIdByUid(){
        LoginTokenCompose compose = LoginTokenCompose.current();
        List<Long> list = productService.fetchProductIdByUid(compose.uid);
        responseJSON(list);
    }
    @UnCheck
    public static void fetchAllRuntimeStrategyAcconts(){
        List<Map<String,Object>> list = productService.fetchAllRuntimeStrategyAcconts();
        responseJSON(list);
    }

}
