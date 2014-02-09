package bussiness.product;

import util.LoginTokenCompose;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-27
 * Time: 下午5:58
 * 功能描述:
 */
public interface IVirtualProductService {


    public  boolean accountHasSameName(long uid, String name, Long id);

    public  boolean virtualProductHasSameName(long uid, String name, Long id);

    public  long editVirtualProduct(LoginTokenCompose compose, String body);

    public  long addVirtualProductFullInfo(LoginTokenCompose compose, String body);


    public  int delVirtualProduct(Long id, LoginTokenCompose compose) ;

    public List<Long> findrefIdsByProdId(Long prodId, LoginTokenCompose compose);

    public  List<Map<String, Object>> fetchFunctionByUserAndProduct(long uid, long pid);

    public  Map<String, Object> findVirtrualProductById(long id);

    public List<Long> fetchUserVitrualProdructIds(long uid);
}
