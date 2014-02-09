package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.user.ISaleDepartmentService;
import dbutils.SqlLoader;
import models.iquantCommon.SaleDepartMentDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:12
 * 功能描述:
 */
public class SaleDepartmentService extends BaseService implements ISaleDepartmentService {


    /**
     * 查询所有的部门信息
     *
     * @return
     */
    public List<SaleDepartMentDto> findAll() {
        String sql = SqlLoader.getSqlById("findAllDepts");
        return qicDbUtil.queryBeanList(sql, SaleDepartMentDto.class);
    }

}
