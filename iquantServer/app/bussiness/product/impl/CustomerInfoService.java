package bussiness.product.impl;

import bussiness.common.impl.BaseService;
import bussiness.product.ICustomerInfoService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dbutils.SqlLoader;
import models.iquantCommon.CustomerIndexDto;
import models.iquantCommon.CustomerSecurityGroupDto;
import models.iquantCommon.CustomerSecurityListDto;
import models.iquantCommon.CustomerTemplateDto;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import util.CommonUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-19
 * Time: 下午5:07
 * 功能描述:
 */
public class CustomerInfoService extends BaseService implements ICustomerInfoService {
    @Override
    public long addCustSecGroup(CustomerSecurityGroupDto customerSecurityGroupDto) {
        long id = qicDbUtil.insertWithNameParam("addCustSecGroup", customerSecurityGroupDto);
        return id;
    }

    @Override
    public long existCustSecGroupName(long uid, long pid, String name) {
        String sql = SqlLoader.getSqlById("custSecGroupNameExist");
        Long count = qicDbUtil.queryCount(sql, uid, pid, name);
        return count;
    }

    @Override
    public List<Long> delCustSecGroupByName(long uid, long pid, String name) {
        String sql1 = SqlLoader.getSqlById("custSecGroupSameName");
        List<Long> gidList = qicDbUtil.queryWithHandler(sql1, new ColumnListHandler<Long>(), uid, pid, name);
        return gidList;
    }

    @Override
    public int delCustSecGroupById(long uid, long pid, long gid) {
        //先删除明细
        String detailSql = SqlLoader.getSqlById("delCustSecDetailByGid");
        qicDbUtil.update(detailSql,uid, pid, gid);

        //再删除本身
        String curSql = SqlLoader.getSqlById("delCustSecGroupById");
        return qicDbUtil.update(curSql, uid, pid, gid);
    }

    @Override
    public List<Map<String, Object>> fetchCustSecGroupLatest(long uid, long pid) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", uid);
        paramMap.put("pid", pid);
        List<Map<String, Object>> custSecGroupList = qicDbUtil.queryMapListWithNameParamMap("fetchCustSecGroupLatest", paramMap);
        return custSecGroupList;
    }

    @Override
    public List<Map<String, Object>> fetchCustSecGroupByNameAll(long uid, long pid, String name) {
        String sql = SqlLoader.getSqlById("fetchCustSecGroupByNameAll");
        List<Map<String, Object>> custSecGroupList = qicDbUtil.queryMapList(sql, uid, pid, name);
        return custSecGroupList;
    }

    @Override
    public List<Map<String, Object>> fetchCustSecGroupByNameLatestDetail(long uid, long pid, String name) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", uid);
        paramMap.put("pid", pid);
        paramMap.put("name", name);
        Joiner joiner = Joiner.on(",").skipNulls();
        List<Long> list = qicDbUtil.queryWithHandlerWithNameParamMap("fetchLatestCustSecGroupIdsByName", new ColumnListHandler<Long>(), paramMap);
        List<Map<String, Object>> custSecGroupList = Lists.newArrayList();
        if(list !=null && list.size()>0){
            String ids = joiner.join(list);
            String fetchCustSecGroupByGroupIdsSql = SqlLoader.getSqlById("fetchCustSecGroupByGroupIds").replaceAll("#ids#", ids);
            custSecGroupList = qicDbUtil.queryMapList(fetchCustSecGroupByGroupIdsSql);
        }
        return custSecGroupList;
    }

    @Override
    public long addCustSecListByGid(CustomerSecurityListDto customerSecurityListDto) {
        if (StringUtils.isEmpty(customerSecurityListDto.ctime)) {
            customerSecurityListDto.ctime = CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date());
        }
        if (StringUtils.isEmpty(customerSecurityListDto.comment)) {
            customerSecurityListDto.comment = "";
        }
        return qicDbUtil.insertWithNameParam("addCustSecListByGid", customerSecurityListDto);
    }

    @Override
    public int updateCustSecListByGid(CustomerSecurityListDto customerSecurityListDto) {
        if (StringUtils.isEmpty(customerSecurityListDto.ctime)) {
            customerSecurityListDto.ctime = CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date());
        }
        if (StringUtils.isEmpty(customerSecurityListDto.comment)) {
            customerSecurityListDto.comment = "";
        }
        int effect = qicDbUtil.updateWithNameParam("updateCustSecListById", customerSecurityListDto);
        return effect;
    }

    @Override
    public int delCustSecListById(long uid, long pid, Long id) {
        String sql = SqlLoader.getSqlById("delCustSecListById");
        int effect = qicDbUtil.update(sql, uid, pid, id);
       return effect;
    }

    @Override
    public List<Map<String, Object>> fetchCustSecListByGid(long uid, long pid, Long gid) {
        String sql = SqlLoader.getSqlById("fetchCustSecListByGid");
        List<Map<String, Object>> custSecGroupList = qicDbUtil.queryMapList(sql, uid, pid, gid);
        return custSecGroupList;
    }

    @Override
    public void updateCustSecCommentsById(long uid, long pid, long id, String comments){
        Map<String, Object> params = Maps.newHashMap();
        params.put("uid", uid);
        params.put("pid", pid);
        params.put("id", id);
        params.put("comments", comments);
        qicDbUtil.updateWithNameParamMap("updateCustSecCommentsById", params);
    }

    @Override
    public long addCustIndex(CustomerIndexDto customerIndexDto) {
        long id = qicDbUtil.insertWithNameParam("addCustIndex", customerIndexDto);
        return id;
    }

    @Override
    public int delCustIndex(long uid, long pid, Long id) {
        String sql = SqlLoader.getSqlById("delCustIndex");
        int effect = qicDbUtil.update(sql, uid,pid, id);
        return effect;
    }

    @Override
    public int editCustIndex(CustomerIndexDto customerIndexDto) {
        int effect = qicDbUtil.updateWithNameParam("editCustIndex", customerIndexDto);
        return effect;
    }

    @Override
    public List<Map<String, Object>> fetchCustIndex(long uid, long pid) {
        String sql = SqlLoader.getSqlById("fetchCustIndex");
        List<Map<String, Object>> custIndexList = qicDbUtil.queryMapList(sql, uid,pid);
        return custIndexList;
    }

    @Override
    public String fetchCustIndexContentByid(long uid, long pid, Long id) {
        String sql = SqlLoader.getSqlById("fetchCustIndexContentByid");
        String content = qicDbUtil.queryWithHandler(sql, new ScalarHandler<String>(), uid, pid, id);
        return content;
     }

    @Override
    public long addCustTemplate(CustomerTemplateDto customerTemplateDto) {
        long id = qicDbUtil.insertWithNameParam("addCustTemplate", customerTemplateDto);
        return id;
    }

    @Override
    public int delCustTemplate(long uid, long pid, Long id) {
        String sql = SqlLoader.getSqlById("delCustTemplate");
        int effect = qicDbUtil.update(sql, uid, pid, id);
        return effect;
    }

    @Override
    public int editCustTemplate(CustomerTemplateDto customerTemplateDto) {
        int effect = qicDbUtil.updateWithNameParam("editCustTemplate", customerTemplateDto);
        return effect;
    }

    @Override
    public List<Map<String, Object>> fetchCustTemplateListByCategory(long uid, long pid, int category) {
        String sql = SqlLoader.getSqlById("fetchCustTemplateListByCategory");
        List<Map<String, Object>> custIndexList = qicDbUtil.queryMapList(sql, uid, pid, category);
       return custIndexList;
    }

    @Override
    public String fetchCustTemplateContentByid(long uid, long pid, Long id) {
        String sql = SqlLoader.getSqlById("fetchCustTemplateContentByid");
        String content = qicDbUtil.queryWithHandler(sql, new ScalarHandler<String>(), uid, pid, id);
        return content;
    }

    @Override
    public Long custTemplateExistName(long uid, long pid, int category, String name) {
        String sql = SqlLoader.getSqlById("custTemplateExistName");
        Long id = qicDbUtil.queryWithHandler(sql, new ScalarHandler<Long>(), uid,pid, category, name);
        return id;
    }
}
