package models.iquantCommon;

import play.db.jpa.Model;

import javax.persistence.*;

/**
 * 股票池属性扩展表
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午11:53
 */

public class StockPoolExt extends Model {

    public StockPool main;


    public int discussTotal = 0;


    public int discussCount = 0;


    public int collectCount = 0;


}
