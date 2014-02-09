package business;

import com.google.inject.name.Named;
import models.iquantCommon.UserDataDto;
import models.iquantCommon.UserInfo;
import play.Logger;
import play.modules.guice.InjectSupport;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;

import javax.inject.Inject;
import java.util.List;


/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午9:50
 * 功能描述:
 */
@InjectSupport
public class UserService {

    @Inject
    @Named("http")
    static IRemoteRequestServiceSupport remoteRequestService;

    public UserInfo findByAccount(String account) {
        ActionResult<UserInfo> result = remoteRequestService.getBean(URILib.FETCH_USER_WITH_ACCOUNT, UserInfo.class, account);
        return result.data;
    }
    public Boolean checkByAccount(String account) {
        return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.CHECK_USER_WITH_ACCOUNT, account).data);
    }
    //根据用户ID查询该用户数据权限
    public List<UserDataDto> findUserDataInfo(long id) {
        ActionResult<List<UserDataDto>> result = remoteRequestService.getList(URILib.FETCH_USER_DATA_BY_UID, UserDataDto.class, id);
        return result.data;
    }
    /**
     * 以下为示例
     */
    public UserInfo test(String account) {
        ActionResult<UserInfo> result = remoteRequestService.getBean(URILib.FETCH_USER_WITH_ACCOUNT, UserInfo.class, account);


        ActionResult<List<UserInfo>> results = remoteRequestService.getList(URILib.FETCH_USER_WITH_ACCOUNT, UserInfo.class, account);


        ResponseHeader responseHeader = result.header;
        UserInfo userInfo = result.data;
        int status = responseHeader.status;//响应状态
        int pageNo = responseHeader.pageNo;//响应当前页
        int pageSize = responseHeader.size;//记录总数
        long totalRow = responseHeader.total;//记录条数
        String message = responseHeader.message;//消息
        List<ResponseHeader.ErrorEntry> errors = responseHeader.errors;//错误信息
        //遍历错误信息
        if(errors != null && errors.size()>0){
            for(ResponseHeader.ErrorEntry error:errors){
            Logger.debug("错误字段：%s,错误消息:%s",error.key,error.msg);
            }
        }
        responseHeader.addError("name","name字段不能为空");
        System.out.println("header:" + responseHeader);
        return result.data;
    }
}
