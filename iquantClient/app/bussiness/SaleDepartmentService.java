package bussiness;

import models.iquantCommon.SaleDepartMentDto;
import protoc.URILib;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午4:01
 * 功能描述: 部门相关业务逻辑操作类
 */
public class SaleDepartmentService extends BasicService {
    /**
     * 查询所有的部门信息
     * @return
     */
    public static List<SaleDepartMentDto> findAll(){
        return  remoteRequestService.getList(URILib.FETCH_DEPARTMENT_INFO,SaleDepartMentDto.class).data;
    }
}
