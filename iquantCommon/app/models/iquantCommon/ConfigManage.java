package models.iquantCommon;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 配置管理
 * User: liangbing
 * Date: 12-12-12
 * Time: 下午1:50
 */

public class ConfigManage extends Model {


    public String keyName;

    public String keyValue;

    public String remark;

}
