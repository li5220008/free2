package bussiness.common.impl;

import bussiness.common.IFunctionService;
import dbutils.SqlLoader;
import models.iquantCommon.FunctionInfoDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:15
 * 功能描述:
 */
public class FunctionService extends BaseService implements IFunctionService {

    private static int a = 1;
    private static List<FunctionInfoDto> list =null;

    public FunctionService() {

    }


    public List<FunctionInfoDto> getAllSystemFunctions() {
       // reload();//暂时先每次都加载....
        return findAll();
    }

    private  List<FunctionInfoDto>  findAll() {
        if(list == null || list.size()==0){
        String sql = SqlLoader.getSqlById("findAll");
         list = qicDbUtil.queryBeanList(sql, FunctionInfoDto.class);
        }
        return list;
    }

    public List<FunctionInfoDto> reload() {
        return  findAll();
    }
}
