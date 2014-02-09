package protoc;

import play.Play;
import util.Tokens;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午11:42
 * 功能描述:
 */
public class URILib {
    //region sso相关的api
    public static final String SSO_HOST = Play.configuration.getProperty("sso.url", "http://localhost:9800");

    public static final String SSO_LOGIN = SSO_HOST + "/api/loginWithoutCryp?u={name}&p={pwd}&mac={mac}&pid={pid}";
    //endregion

    //region manage相关的api
    public static final String IQUNAT_SERVER_HOST = Play.configuration.getProperty("server.url", "http://localhost:9100");

    public static final String FETCH_USER_WITH_ACCOUNT = IQUNAT_SERVER_HOST + "/UserinfoCt/fetchUserByAcccount?account={account}";
    public static final String CHECK_USER_WITH_ACCOUNT = IQUNAT_SERVER_HOST + "/UserinfoCt/checkSumValid?account={account}";
    public static final String FETCH_USER_DATA_BY_UID = IQUNAT_SERVER_HOST + "/UserinfoCt/fetchUserDataInfoById?id={id}";
    public static final String ADD_USER_ACCOUNT = IQUNAT_SERVER_HOST + "/productCt/addTradeAccount?ntToken={token}&" + Tokens.OVERRIDE_TOKEN_UID_PARAM_NAME + "={opUid}";
    public static final String DEL_USER_ACCOUNT = IQUNAT_SERVER_HOST + "/productCt/delTradeAccount?id={id}&ntToken={token}&" + Tokens.OVERRIDE_TOKEN_UID_PARAM_NAME + "={opUid}";
    public static final String EDIT_USER_ACCOUNT = IQUNAT_SERVER_HOST + "/productCt/editTradeAccount?ntToken={token}&" + Tokens.OVERRIDE_TOKEN_UID_PARAM_NAME + "={opUid}";
    public static final String FETCH_USER_ACCOUNT = IQUNAT_SERVER_HOST + "/productCt/fetchTradeAccountList?ntToken={token}&" + Tokens.OVERRIDE_TOKEN_UID_PARAM_NAME + "={opUid}";
    public static final String Verify_ACCOUNT_UNIQUE = IQUNAT_SERVER_HOST + "/productCt/verifyTradeAccountisExist?ntToken={token}&" + Tokens.OVERRIDE_TOKEN_UID_PARAM_NAME + "={opUid}&accountName={accountName}";

    //endregion

    //region 策略超市列表

    public static final String FETCH_STRATEGY_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyList?orderType={orderType}&keyword={keyword}&pageNo={pageNo}";
    //endregion

    //region 高级搜索列表

    public static final String FETCH_USER_SEARCHCOND_LIST = IQUNAT_SERVER_HOST + "/userTemplateCt/fetchUserTemplate?uid={uid}&type={type}";
    //endregion

    //region 根据用户ID获取已收藏的策略

    public static final String QUERY_USER_COLLECT_LIST = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserCollectById?ids={ids}&uid={uid}";
    //endregion

    //region 用户添加策略收藏

    public static final String ADD_STRATEGY_COLLECT = IQUNAT_SERVER_HOST + "/userStrategyManageCt/addStrategyCollect?sid={sid}&uid={uid}";
    //endregion

    //region 用户取消策略收藏

    public static final String DELECT_STRATEGY_COLLECT = IQUNAT_SERVER_HOST + "/userStrategyManageCt/delStrategyCollect?sid={sid}&uid={uid}";
    //endregion

    //region 修改查询模板

    public static final String UPDATE__TEMPLATE = IQUNAT_SERVER_HOST + "/UserTemplateCt/updateTemplate";
    //endregion

    //region 重命名查询模板

    public static final String RENAME__TEMPLATE = IQUNAT_SERVER_HOST + "/UserTemplateCt/renameUserTemplate";
    //endregion

    //region 判断搜索条件是否重名

    public static final String JUDGE_SAME_NAME = IQUNAT_SERVER_HOST + "/userTemplateCt/IsTemplateNameExist?uid={uid}&type={type}&name={name}";
    //endregion

    //region 保存搜索条件

    public static final String ADD_USER_TEMPLATE = IQUNAT_SERVER_HOST + "/userTemplateCt/addTemplate?uid={uid}&type={type}&name={name}&content={content}";
    //endregion

    //region 编辑搜索条件
    public static final String SAVE_USER_TEMPLATE = IQUNAT_SERVER_HOST + "/userTemplateCt/updateTemplate";
    //endregion

    //region 删除搜索条件

    public static final String DETELE_USER_TEMPLATE = IQUNAT_SERVER_HOST + "/userTemplateCt/deleteTemplateById?id={id}";
    //endregion

    //region 获取单个模板的内容

    public static final String FETCH_USER_TEMPLATE_INFO = IQUNAT_SERVER_HOST + "/userTemplateCt/findTemplateById?id={id}";
    //endregion

    //region 高级搜索

    public static final String FETCH_ADVANCE_SEARCH_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyAdvance?orderType={orderType}&pageNo={pageNo}";
    //endregion

    //region 获取策略对比策略列表信息

    public static final String STRATEGY_COMPARE_STRATEGY_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchCompareStrategyList?stids={stids}";
    //endregion

    //region 获取策略列表对比x轴日期信息

    public static final String STRATEGY_CONTRAST_CHART_INFO = IQUNAT_SERVER_HOST + "/strategyCt/fetchCompareStrategyDates?stids={stids}";
    //endregion

    //region 绩效展示 周线数据

    public static final String STRATEGY_STRATEGY_DETAIL_WEEK_DATA = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyDetailWeekData?stid={stid}&isBackTest={isBackTest}&upTime={upTime}";
    //endregion

    //region 绩效展示 日线数据

    public static final String STRATEGY_STRATEGY_DETAIL_DAILY_DATA = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyDetailDailyData?stid={stid}&isBackTest={isBackTest}&upTime={upTime}";
    //endregion

    //region 获取策略的基本信息

    public static final String FETCH_STRATEGY_BASEINFO = IQUNAT_SERVER_HOST + "/strategyCt/getStrategyBaseInfo?stid={stid}";
    //endregion
    //region 按策略名搜索策略

    public static final String FETCH_STRATEGY_BY_NAME = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyByName?sname={sname}";
    //endregion

    //region 判断策略是否被收藏

    public static final String JUDGE_IS_CONLLECT = IQUNAT_SERVER_HOST + "/userStrategyManageCt/hasCollected?stid={stid}&uid={uid}";
    //endregion


    //region 获取策略绩效指标(QIC)

    public static final String FETCH_QIC_INDICATOR = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyIndicator?stype={stype}&stid={stid}&ctype={ctype}";
    //endregion


    //region 获取策略绩效指标(QIA)

    public static final String FETCH_QIA_INDICATOR = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyIndicator?stype={stype}&stid={stid}&ctype={ctype}";
    //endregion


    //region 获取策略委托信号

    public static final String STRATEGY_AUTHORIZE_RECORD = IQUNAT_SERVER_HOST + "/strategyCt/fetchAuthorizeRecord?stid={stid}";
    //endregion


    //region 判读策略是否订阅

    public static final String JUDGE_IS_ORDER = IQUNAT_SERVER_HOST + "/userStrategyManageCt/hasOrdered?uid={uid}&stid={stid}";
    //endregion

    //region 获取策略订阅到期时间

    public static final String ORDER_DUE_DATE = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserOrderEndDate?uid={uid}&stid={stid}";
    //endregion

    //region 判断用户是否评论了该策略

    public static final String JUDGE_DISCUESS_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/hasComment?uid={uid}&stid={stid}";
    //endregion

    //region 策略持仓(qic)

    public static final String FETCH_QIC_HOLD_POSITION_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategHoldPosition?stype={stype}&stid={stid}&pageNo={pageNo}";
    //endregion

    //region 策略持仓(qia)

    public static final String FETCH_QIA_HOLD_POSITION_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategHoldPosition?stype={stype}&stid={stid}&pageNo={pageNo}";
    //endregion

    //region 委托记录

    public static final String FETCH_ENTRUST_RECORD_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchEntrustRecord?stid={stid}&pageNo={pageNo}";
    //endregion

    //region 成交记录

    public static final String FETCH_BARGAIN_RECORD_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchBargainRecord?stid={stid}&pageNo={pageNo}";
    //endregion

    //region 策略的评论信息列表

    public static final String FETCH_USER_CONTENT_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchUserCommentList?stid={stid}";
    //endregion

    //region 保存策略评论

    public static final String SAVE_USER_DISCUSS = IQUNAT_SERVER_HOST + "/userStrategyManageCt/addUserDiscuss?uid={uid}&stid={stid}";
    //endregion

    //region 订阅

    public static final String ORDER_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/orderStrategy?month={month}&uid={uid}&stid={stid}";
    //endregion

    //region 延迟订阅

    public static final String ORDER_DELAY_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/delayStrategyOrder?month={month}&uid={uid}&stid={stid}";
    //endregion

    //region 获取我的收藏策略信息

    public static final String FETCH_FAVORITE_STRATEGY_BASEINFO = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserCollectList?sortType={sortType}&keyword={keyword}&pageNo={pageNo}&uid={uid}";
    //endregion

    //region 高级搜索获取我的收藏策略信息

    public static final String FETCH_FAVORITE_ADVANCE_SEARCH_LIST = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserCollectionListAdvance?sortType={sortType}&pageNo={pageNo}&uid={uid}";
    //endregion

    //region 获取我的订阅策略信息

    public static final String FETCH_SUBSCRIPTION_STRATEGY_BASEINFO = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserOrderList?sortType={sortType}&keyword={keyword}&pageNo={pageNo}&uid={uid}";
    //endregion

    //region 高级搜索获取我的订阅策略信息 fetchUserOrderListAdvance

    public static final String FETCH_SUBSCRIPTION_ADVANCE_SEARCH_LIST = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchUserOrderListAdvance?sortType={sortType}&pageNo={pageNo}&uid={uid}";
    //endregion




    //region 股票池列表

    public static final String FETCH_STOCKPOOL_LIST = IQUNAT_SERVER_HOST + "/stockpoolCt/fetchStockPoolList?keyword={keyword}&strategyName={strategyName}&orderType={orderType}&pageNo={pageNo}&flag={flag}";
    //endregion

    //region 我的收藏   股票池列表


    public static final String FETCH_COLLECTED_STOCKPOOL_LIST = IQUNAT_SERVER_HOST + "/userStockpoolManageCt/fetchCollectedStockPoolList?keyword={keyword}&strategyName={strategyName}&orderType={orderType}&pageNo={pageNo}&uid={uid}&flag={flag}";
    //endregion

   /* //region 股票池高级搜索列表

    public static final String FETCH_USERTEMPLATE_LIST = IQUNAT_SERVER_HOST + "/stockpoolCt/fetchAdvanceSearchList?uid=%s&type=%s";
    //endregion
*/
    //region 股票池高级搜索功能

    public static final String FETCH_STOCKPOOLADVANCESEARCH_LIST = IQUNAT_SERVER_HOST + "/stockpoolCt/advanceSearch?pageNo={pageNo}";
    //endregion

    //region 股票池我的收藏高级搜索功能

    public static final String FETCH_STOCKPOOL_COLLECTION_ADVANCESEARCH_LIST = IQUNAT_SERVER_HOST + "/userStockpoolManageCt/advanceSearch?pageNo={pageNo}&uid={uid}";
    //endregion

    //region 股票池收藏列表

    public static final String FETCH_STOCKPOOLCOLLECT_LIST = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/fetchCollectIdList?ids={ids}&uid={uid}";
    //endregion

    //region 股票池评论列表

    public static final String FETCH_STOCKPOOLDISCUSS_LIST = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/fetchCommentList?uid={uid}";
    //endregion

    //region 股票池热门搜索列表（和股票池列表一样 可以合并 有修改 将orderSort改成ordeType 不明白为什么会写orderSort这样的单词出来）

    public static final String FETCH_STOCKPOOLHOT_LIST = IQUNAT_SERVER_HOST + "/stockpoolCt/fetchHotStockPoolList?keyword={keyword}&strategyName={strategyName}&orderType={orderType}&pageNo={pageNo}";
    //endregion

    //region 股票池保存评论

    public static final String STOCKPOOLSAVEDISCUSS = IQUNAT_SERVER_HOST + "/userStockpoolManageCt/addComment?star={star}&uid={uid}&spid={spid}";
    //endregion
    //region 判断股票池是否评论

    public static final String FETCH_STOCKPOOLIDISCUSS = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/hasComment?spid={spid}&uid={uid}";
    //endregion

    //region 股票池是否收藏

    public static final String JUDGESTOCKPOOLISCOLLECT = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/hasCollected?spid={spid}&uid={uid}";
    //endregion

    //region 股票池基本信息

    public static final String FETCH_STOCKPOOLBASICINFO = IQUNAT_SERVER_HOST + "/stockpoolCt/fetchStockpoolInfo?stockPoolCode={stockPoolCode}";
    //endregion

    //region 股票池添加收藏

    public static final String ADD_STOCKPOOLCOLLECT = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/addCollections?uid={uid}&spid={spid}";
    //endregion

    //region 股票池取消收藏

    public static final String DELETE_STOCKPOOLCOLLECT = IQUNAT_SERVER_HOST + "/userstockpoolManageCt/delCollections?uid={uid}&spid={spid}";
    //endregion

    //region 股票池组合列表信息展示

    public static final String FETCH_STOCKPOOLCOMBINEINFO_LIST = IQUNAT_SERVER_HOST + "/stockpoolCt/fetchCombinInfo?stockPoolCode={stockPoolCode}";
    //endregion







    //region修改用户密码

    public static final String UPDATE_USER_PWD = IQUNAT_SERVER_HOST + "/userInfoCt/updateUserPWD?uid={uid}&newPwd={newPwd}";
    //endregion

    //region根据关键字获取系统配置信息

    //public static final String FETCH_SYSTEM_CONFIG_VALUE = IQUNAT_SERVER_HOST + "/usersCt/fetchSystemConfig?keyword=%s";
    public static final String FETCH_SYSTEM_CONFIG_VALUE = IQUNAT_SERVER_HOST + "/sysConfigCt/fetchValue?key={key}";
    //endregion

    //region获取用户消息列表
    public static final String FETCH_USER_MSG_LISTS = IQUNAT_SERVER_HOST + "/MessageInfoCt/fetchUserMsgList?uid={uid}&pageNo={pageNo}&orderFlag={orderFlag}";
    //endregion

    //region获取单条消息
    public static final String FETCH_USER_MSG = IQUNAT_SERVER_HOST+ "/MessageInfoCt/fetchMsgInfoById?msgId={msgId}";
    //endregion

    //region 更新消息状态
    public static final String UPDATE_USER_MSG = IQUNAT_SERVER_HOST+ "/MessageInfoCt/updateUserMsg?msgId={msgId}&status={status}";
    //endregion

    //region批量删除消息
    public static final String BATCH_DELETE_MSG = IQUNAT_SERVER_HOST+ "/MessageInfoCt/batchDeleteMsgByIds?ids={ids}";
    //endregion



    //region获取操作日志 魏贵礼
    public static final String FETCH_LOG_LIST = IQUNAT_SERVER_HOST +"/logInfoCt/fetchloglist?begindate={begindate}&enddate={enddate}&pageNo={pageNo}";


    //写入系统日志
    public static final String WRITE_SYSTEM_LOG = IQUNAT_SERVER_HOST + "/logInfoCt/writeSystemLog?uid={uid}&function={function}&content={content}&type={type}";

    //根据关键字更新系统配置值
    public static final String UPDATE_SYSVALUE_BY_KEY = IQUNAT_SERVER_HOST + "/sysConfigCt/updateSysValueByKey?key={key}&value={value}";

    //根据Key获取value信息
    //public static final String FETCH_SYSCONFIG_VALUE = IQUNAT_SERVER_HOST + "/sysConfigCt/fetchValue?key=%s";

    //加载配置信息
    public static final String LOAD_CONFIG_LIST = IQUNAT_SERVER_HOST + "/sysConfigCt/loadConfig";

    //endregion

    //region  潘志威






    //策略加入回测(完成)
    public static final String ADD_STRATEGY_BACKTEST = IQUNAT_SERVER_HOST+"/userStrategyManageCt/addBackTest?qicoreServerId={qicoreServerId}&qiaServerId={qiaServerId}&stids={stids}";

    public static final String FETCH_STRATEGY_SERVER = IQUNAT_SERVER_HOST+"/StrategyServerManageCt/fetchStategyServer?serverType={serverType}&egineType={egineType}";

    public static final String FETCH_ALL_SERVER = IQUNAT_SERVER_HOST+"/StrategyServerManageCt/fetchAllServer";







    //查询回收站的策略列表(完成)
    public static final String FETCH_RETRIEVE_STRATEGY_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchRecycleList?pageNo={pageNo}&uid={uid}";
    //查询已上架的策略列表(完成)
    public static final String FETCH_UP_STRATEGY_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchUpStrategyList?pageNo={pageNo}&uid={uid}";
    //查询待上架策略列表(完成)
    public static final String FETCH_WAIT_LIST = IQUNAT_SERVER_HOST + "/strategyCt/fetchWaitList?pageNo={pageNo}&uid={uid}";
    //加入回测( 废弃)
    public static final String ADD_LOOKBACK = IQUNAT_SERVER_HOST + "/strategyCt/addLookback?stids={stids}&status={status}";
    //删除策略(完成)
    public static final String DELETE_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/deleteStrategy?stids={stids}";
    public static final String EMPTY_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/emptyStrategy";//完成
    //策略下架（完成）
    public static final String STRATEGY_DOWN = IQUNAT_SERVER_HOST + "/userStrategyManageCt/downstrategy?stids={stids}&downTime={downTime}&message={message}&uid={uid}&flag={flag}";
    //策略上架(完成)
    public static final String UP_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/upStrategy?stids={stids}&qicoreServerId={qicoreServerId}&qiaServerId={qiaServerId}&qiaAgentId={qiaAgentId}";
    //updateStategyStatus(没用)
    public static final  String UPDATE_STATEGY_STATUS = IQUNAT_SERVER_HOST + "/strategyInfos/updateStategyStatus?status={status}&suuid={suuid}";
    //updateStategyStatusbyId(没用)
    public static final  String UPDATE_STATEGY_STATUS_BY_ID = IQUNAT_SERVER_HOST + "/strategyInfos/updateStategyStatusbyId?status={status}&id={id}";
    //judgeStrategyIsOut30(完成)
    public static final String JUDGE_STRATEGY_IS_OUT_30 = IQUNAT_SERVER_HOST + "/userStrategyManageCt/countUserRunntimeStrategy?uid={uid}";
    //查询一批策略(完成)
    public static final String FIND_STRATEGYS_BY_IDS = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyByIds?ids={ids}";
    //findStrategyById 查询一个策略(完成)
    public static final String FIND_STRATEGY_BY_ID = IQUNAT_SERVER_HOST + "/strategyCt/fetchStrategyById?id={id}";
    //GetUpTime(没用)
    public static final  String GET_UP_TIME = IQUNAT_SERVER_HOST + "/strategyInfos/getUpTime?strategyId={strategyId}";


    //endregion

    //region  刘泓江
    //userinfo
    //查询所有的部门信息
    public static final String FETCH_DEPARTMENT_INFO = IQUNAT_SERVER_HOST + "/UserInfoCt/findAllDepartment";
    //根据用户ID 查询角色信息
    public static final String FETCH_ROLEINFO_BY_UID = IQUNAT_SERVER_HOST + "/UserInfoCt/findUserRoleInfoById?uid={uid}";
    //根据用户ID查询 用户的菜单 complete
    public static final String FETCH_FUNCTIONINFO_BY_ROLEID =IQUNAT_SERVER_HOST + "/RoleInfoCt/findRoleFunctionInfo?rid={rid}";
    //根据用户ID查询用户信息
    public static final String FETCH_USERINFO_BY_UID = IQUNAT_SERVER_HOST + "/UserInfoCt/findUserInfoById?uid={uid}";
    //根据用户ID查询 用户的菜单
    public static final String FETCH_USERMENU_BY_UID = IQUNAT_SERVER_HOST +"/UserInfoCt/findUserFunctionInfoById?uid={uid}";
    //修改用户基本信息
    public static final String UPDATE_USERINFO = IQUNAT_SERVER_HOST +"/UserInfoCt/updateUserInfo";
    //获取功能列表信息
    public static final String FETCH_FUNCTION_INFO =IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchFunctionInfo";
    //根据email查找用户
    public static final String FETCH_USER_BY_EMAIL = IQUNAT_SERVER_HOST + "/UserInfoCt/findUserByEmail?email={email}";
    //根据用户ID，批量删除用户
    public static final String BATCH_DELETE_USER = IQUNAT_SERVER_HOST + "/UserInfoCt/delUserById?ids={ids}";
    //根据用户ID，批量修改用户状态
    public static final String  BATCH_UPDATE_USER_STATUS = IQUNAT_SERVER_HOST + "/UserInfoCt/userStateModifyById?ids={ids}&status={status}";
    //到期用户延期
    public static final String BATCH_UPDATE_USER_DELAYDATE = IQUNAT_SERVER_HOST + "/UserInfoCt/userdelayById?strId={strId}&delaydate={delaydate}";
    //批量添加用户
    public static final String ADD_USER_BATCH = IQUNAT_SERVER_HOST + "/UserInfoCt/addUserBatch";
     //查看用户信息
    public static final String FETCH_PERSONAL_MODIFY = IQUNAT_SERVER_HOST + "/UserInfoCt/fetchPersonalmodify?uid={uid}";
     //更新用户信息
    public static final String UPDATE_PERSONAL_MODIFY = IQUNAT_SERVER_HOST + "/UserInfoCt/updatePersonalmodify?uid={uid}";
    //根据uid找密码
    public static final String FIND_PWD_BYID = IQUNAT_SERVER_HOST + "/UserInfoCt/findPwdById?password={password}&uid={uid}";
    //检查email是否存在
    public static final String CHECK_EMAIL = IQUNAT_SERVER_HOST + "/UserInfoCt/checkEmail?newEmail={newEmail}&selfEmail={selfEmail}";
    public static final String ADD_USER = IQUNAT_SERVER_HOST + "/UserInfoCt/addUser";

    public static final String FETCH_ALL_SALEDEPARTMENT = IQUNAT_SERVER_HOST + "/UserInfoCt/fetchAllDepartment";


    //roleinfo
    //根据roleId 获取用户信息
    public static final String FETCH_USERINFO_BY_ROLEID = IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchRoleListByRid?roleId={roleId}&pageNo={pageNo}";
    //查找所有角色
    public static final String FETCH_ALL_ROLE = IQUNAT_SERVER_HOST + "/RoleInfoCt/findAllRole";
    //删除角色的权限
    public static final String DELETE_ROLEFUNCTION_BY_ROLEID = IQUNAT_SERVER_HOST + "/RoleInfoCt/deleteFunctionInfoByRoleId?rid={rid}";
    //批量添加角色权限
    public static final String BATCH_FUNCTIONINFO_BY_ROLEID = IQUNAT_SERVER_HOST + "/RoleInfoCt/addFunctionInfoByRoleId";
    //角色基本信息修改
    public static final String UPDATE_ROLEINFO_BY_RID = IQUNAT_SERVER_HOST + "/RoleInfoCt/saveRoleBasicInfo?txtarea={txtarea}&id={id}";
    //角色基本信息查询
    public static final String FETCH_ROLEINFO_BY_ROLEID = IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchRoleBasicInfo?id={id}";
    //查询最近20个已授权用户信息
    public static final String FETCH_USERINFO_BY_ROLEID_AND_TYPE  = IQUNAT_SERVER_HOST + "/RoleInfoCt/queryLastTwentyUser?rid={rid}&type={type}";
    //给定账号或姓名 查询已授权用户
    public static final String FETCH_USERINFO_BY_NAME_OR_ACCOUNT = IQUNAT_SERVER_HOST + "/RoleInfoCt/queryUserByCondition?keyword={keyword}";
    //给定账号或姓名 查询当前角色用户
    public static final String FETCH_ROLEUSER_BY_NAME_OR_ACCOUNT = IQUNAT_SERVER_HOST + "/RoleInfoCt/queryRoleUserByCondition?keyword={keyword}&roleId={roleId}";
    //-----------更换用户角色 begin----------------
  /*  //根据用户ID 角色ID 获取用户角色信息
    public static final String CHECK_USERROLE_BY_ID =IQUNAT_SERVER_HOST + "/RoleInfoCt/checkUserroleById?urid=%s&roleId=%s";
    //添加用户角色信息
    public static final String ADD_USERROLE =IQUNAT_SERVER_HOST + "/RoleInfoCt/insertUserRolebyId?urid=%s&roleId=%s";
    //删除用户角色信息
    public static final String insertUserRolebyId = IQUNAT_SERVER_HOST + "/RoleInfoCt/deleteUserRolebyId?urid=%s&roleId=%s";*/

    public static final String CHANGE_USER_ROLE = IQUNAT_SERVER_HOST + "/RoleInfoCt/changeRole?roleId={roleId}&sysUid={sysUid}";
    //-----------更换用户角色 end----------------
    //删除角色
    public static final String DELETE_ROLE = IQUNAT_SERVER_HOST + "/RoleInfoCt/deleteRole?id={id}&uid={uid}";

    //RoleInfoCt 2
    //批量插入用户权限
    public static final String BATCH_INSERT_USER_ROLE = IQUNAT_SERVER_HOST + "/RoleInfoCt/insertUserRole?edate={edate}&flag={flag}";

     //新增/修改用户数据权限
    public static final String CHANGE_USER_DATA_PERMISSION = IQUNAT_SERVER_HOST + "/RoleInfoCt/changeDataPermission?uid={uid}";

    //根据用户ID 查找用户数据权限
    public static final String FIND_USER_DATA_PERMISSION = IQUNAT_SERVER_HOST + "/RoleInfoCt/findUserDatePermission?uid={uid}";

    //用户授权 用户列表展示
    public static final String FETCH_USER_LIST_BY_USERIDARRAY = IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchUserListByUserIdArray?userIdArray={userIdArray}";

    //根据用户ID范围 查找用户功能信息
    public static final String FETCH_DEPARTMENTUSER  = IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchUserListBetweenId?startId={startId}&endId={endId}";

    //根据角色ID 更新角色名
    public static final String UPDATE_ROLENAME_BY_RID = IQUNAT_SERVER_HOST + "/RoleInfoCt/updateRoleNameByRid?name={name}&id={id}";

    //新增角色名
    public static final String ADD_ROLENAME = IQUNAT_SERVER_HOST + "/RoleInfoCt/addRoleName?name={name}";

    //用户管理列表
    //待激活用户
    public static final  String FETCH_ACTIVATE_USER_LIST = IQUNAT_SERVER_HOST + "/UserInfoCt/activateUsersList?pageNo={pageNo}";
    //用户列表
    public static final String FETCH_USER_LIST = IQUNAT_SERVER_HOST + "/UserInfoCt/usersList?pageNo={pageNo}";
    //到期用户列表
    public static final String FETCH_DUE_USER_LIST = IQUNAT_SERVER_HOST + "/UserInfoCt/dueUsersList?pageNo={pageNo}";

    //根据角色名查角色信息
    public static final String FETCH_ROLEINFO_BY_NAME  = IQUNAT_SERVER_HOST + "/RoleInfoCt/fetchRoleInfoByName?name={name}";

    //endregion
    //查找用户上传的策略
    public static final String FIND_STRATEGYS_BY_USER =IQUNAT_SERVER_HOST +"/userStrategyManageCt/fetchUserStrategyList";
    //insertStrategy
    public static final String INSERT_STRATEGY = IQUNAT_SERVER_HOST + "/userStrategyManageCt/addStrategy?uid={uid}&filePath={filePath}";

    public static final String FETCH_STRATEGY_BY_SERVICEID = IQUNAT_SERVER_HOST + "/userStrategyManageCt/fetchStartegyNumByServiceId?serviceId={serviceId}";
}
