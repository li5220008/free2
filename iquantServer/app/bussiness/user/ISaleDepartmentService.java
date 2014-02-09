package bussiness.user;

import models.iquantCommon.SaleDepartMentDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:11
 * 功能描述:
 */
public interface ISaleDepartmentService {

    /**
     * 查询所有的部门信息
     * @return
     */
    public  List<SaleDepartMentDto> findAll();
}
