ALTER TABLE qsm.qstrategy CHANGE StrategyName StrategyFunctionName VARCHAR(100);

ALTER TABLE qsm.qstrategy  ADD COLUMN StrategyName VARCHAR(100);

UPDATE 
  qsm.`qstrategy` a,
  (SELECT 
    sb.`name` AS sname,
    qs.`StrategyID` AS sid 
  FROM
    qsm.`qstrategy` qs 
    INNER JOIN qic_db.`product_strategy` ps 
      ON qs.`StrategyID` = ps.`id` 
    INNER JOIN qic_db.`strategy_baseinfo` sb 
      ON sb.`id` = ps.`strategy_id`) AS b 
SET
  a.`StrategyName` = b.sname 
WHERE a.`StrategyID` = b.sid ;