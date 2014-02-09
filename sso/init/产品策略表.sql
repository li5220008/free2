USE qic_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS virtual_product;

CREATE TABLE IF NOT EXISTS virtual_product (  
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL COMMENT '名称',
  `uuid` VARCHAR(40) COMMENT 'uuid',
  create_uid  BIGINT NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `status` smallint(6) DEFAULT '1' COMMENT '产品状态: -100:软删除, 1正常. 默认值为1',
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX cuid (create_uid)
) ENGINE=INNODB DEFAULT CHARSET=utf8
COMMENT='虚拟产品表';


DROP TABLE IF EXISTS trade_account;

CREATE TABLE IF NOT EXISTS trade_account (  
  id BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL COMMENT '名称',
  account VARCHAR(60) NOT NULL COMMENT '帐号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码, 目前是明文, 以后考虑使用双向加密', 
  `type`     INT NOT NULL COMMENT '类型, 0:期货, 1:股票',
  `status` smallint(6) DEFAULT '1' COMMENT '帐号状态: -100:软删除, 1正常. 默认值为1',
  create_uid  BIGINT NOT NULL COMMENT '创建用户的id, 关联到用户表',
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX cuid (create_uid)
) ENGINE=INNODB DEFAULT CHARSET=utf8
COMMENT='交易帐号表';

DROP TABLE IF EXISTS virtual_product_trade_account;

CREATE TABLE IF NOT EXISTS virtual_product_trade_account (  
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '虚拟产品id, 关联到virtual_product表的id字段',
  account_id BIGINT NOT NULL COMMENT '帐号id, 关联到trade_account表的id字段',
  `status` smallint(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  create_uid  BIGINT NOT NULL COMMENT '创建用户的id, 关联到用户表',
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX vp_ta_id(product_id, account_id),
  INDEX cuid (create_uid)
) ENGINE=INNODB DEFAULT CHARSET=utf8
COMMENT='虚拟产品与交易帐号关联表';


DROP TABLE IF EXISTS virtual_product_strategy;

CREATE TABLE IF NOT EXISTS virtual_product_strategy (  
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '虚拟产品id, 关联到virtual_product表的id字段',
  strategy_id BIGINT NOT NULL COMMENT '策略id, 关联到 strategy_baseinfo 表的id字段',
  funds_proportion decimal(10,4) DEFAULT NULL COMMENT '资金使用比例',
  strategy_param text COMMENT '策略参数. 主要是那些标(股票代码)的参数',
  `status` smallint(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  create_uid  BIGINT NOT NULL COMMENT '创建用户的id, 关联到用户表',
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX vp_ta_id(product_id, strategy_id),
  INDEX cuid (create_uid)
) ENGINE=INNODB DEFAULT CHARSET=utf8
COMMENT='虚拟产品与策略关联表';
