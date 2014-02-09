package models.iquantCommon.JobDto;

/**
 * desc: 这个是产品迁移时候用到的一个临时DTO  满足sql映射用:  SELECT id, MIN(strategy_id) FROM virtual_product_strategy WHERE product_id =?
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-9-2
 * Time: 下午3:59
 */
public class ProductStrategyIdDto {
    public Long id;
    public Long strategyId;
}
