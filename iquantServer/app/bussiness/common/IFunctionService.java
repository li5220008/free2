package bussiness.common;

import models.iquantCommon.FunctionInfoDto;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:14
 * 功能描述:
 */
public interface IFunctionService {



    public  List<FunctionInfoDto> getAllSystemFunctions();


    public  List<FunctionInfoDto> reload() ;
}
