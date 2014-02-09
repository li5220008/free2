package models.iquantCommon;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 营业部信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:35
 */
public class SaleDepartment extends BaseDtoSupport{
    @Expose
    public String name;
}
