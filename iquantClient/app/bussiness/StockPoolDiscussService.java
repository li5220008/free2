package bussiness;

import protoc.Protocol;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: liuhongjiang
 * Date: 12-11-20
 * Time: 上午9:47
 */
public class StockPoolDiscussService extends BasicService {

    /**
     * 保存该用户对该策略的评论
     *
     * @param star
     * @param uid
     * @param spid
     */
    public static boolean saveDiscuss(int star, Long uid, Long spid) {
        ActionResult result = remoteRequestService.getJustHeader(URILib.STOCKPOOLSAVEDISCUSS, star, uid, spid);
        ResponseHeader responseHeader = result.header;
        int status = responseHeader.status;
        if (status == Protocol.STATU_SSUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断该用户是否已经评论的该股票池
     *
     * @param spid
     * @param uid
     */
    public static Integer judge(String spid, Long uid) {
        ActionResult<String> result = remoteRequestService.getSingleValue(URILib.FETCH_STOCKPOOLIDISCUSS, spid, uid);
        ResponseHeader responseHeader = result.header;
        int isComment;
        if ("true".equals(result.data)) {
            isComment = 1;           //已评论
        } else {
            isComment = 0;          //未评论
        }
        return isComment;
    }

    /**
     * 从股票池列表中查询已评论的股票池
     *
     * @param uid 用户id
     * @return List<Map<String,Object>>
     */
    public static Set<Integer> queryStockPoolDiscussMap(Long uid) {
        ActionResult<List<String>> result = remoteRequestService.getListWithoutField(URILib.FETCH_STOCKPOOLDISCUSS_LIST, uid);
        List<String> list2 = result.data;
        Set<Integer> resultSet = new HashSet<Integer>();
        for (String s : list2) {
            resultSet.add(Integer.parseInt(s));
        }
        return resultSet;
    }

}
