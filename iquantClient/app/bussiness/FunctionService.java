package bussiness;

import business.DefaultRemoteRequestServiceSupport;
import models.iquantCommon.FunctionInfoDto;
import play.modules.guice.InjectSupport;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-5
 * Time: 上午9:39
 * 功能描述:  获取系统树
 */
public class FunctionService extends BasicService {
    private  List<FunctionInfoDto> list = null;
    public  static int TREE_ROOT_ID = 1;

  /*   {
        reload();
    }*/
    public FunctionService(){
      //  reload();
    }


    public  List<FunctionInfoDto> getAllSystemFunctions() {
        reload();//暂时先每次都加载....
        return list;
    }

    private  void findAll() {
        ActionResult<List<FunctionInfoDto>> actionResult = new DefaultRemoteRequestServiceSupport().getList(URILib.FETCH_FUNCTION_INFO,FunctionInfoDto.class);
        list = actionResult.data;
    }

    public  List<FunctionInfoDto> reload() {
        findAll();
        return list;
    }
}
