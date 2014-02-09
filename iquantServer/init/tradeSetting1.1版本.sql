/*1.1版本交易设置的sql表结构, 新加的表*/

USE qic_db;

/*策略原始配制的交易标的, 从策略的配制文件里提取的*/
DROP TABLE IF EXISTS strategy_security_original;
CREATE TABLE `strategy_security_original` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` BIGINT(20) not null COMMENT '策略id, 关联到 strategy_baseinfo 表的 id 字段',
  `symbol` VARCHAR(26) COMMENT '股票代码, 当type=0/1/3时有值',
  `market` VARCHAR(40) COMMENT '市场',
  `system_plate_id` DECIMAL(22,0) DEFAULT NULL COMMENT '系统板块id, 关联到 gta_data.plate_platetree 表的plateid字段. 当type=2时有值',
  `type`  int DEFAULT '1' COMMENT '证券类型,0:期货, 1:股票, 2:板块， 3:主力/连续',
  `max_position`  DECIMAL(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sid` (`strategy_id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='策略原始配制的交易标的, 从策略的配制文件里提取的';

/*策略配制帐号表.记录策略xml文件里配制的帐号模板信息 */
DROP TABLE IF EXISTS strategy_account_template;
CREATE TABLE `strategy_account_template` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` BIGINT(20) COMMENT '策略id, 关联到 strategy_baseinfo 表的 id 字段',
  `account_order` int COMMENT '帐号的序号',
   `account_type` int COMMENT '帐号的类型 0:期货 1：股票',
  `create_uid` BIGINT(20) COMMENT '创建用户的id, 关联到用户表',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` SMALLINT(6) DEFAULT '1' COMMENT '帐号状态: -100:软删除, 1正常. 默认值为1',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='策略配制帐号表. 记录策略xml文件里配制的帐号模板信息';


/*交易帐号表*/
DROP TABLE IF EXISTS trade_account;
CREATE TABLE `trade_account` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL COMMENT '名称',
  `account` VARCHAR(60) NOT NULL COMMENT '帐号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码, 目前是明文, 以后考虑使用双向加密',
  `type` INT(11) NOT NULL COMMENT '类型, 0:期货, 1:股票',
  `init_capital` DECIMAL(20,4) DEFAULT 0 COMMENT '初始资金',
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` SMALLINT(6) DEFAULT '1' COMMENT '帐号状态: -100:软删除, 1正常. 默认值为1',
  `client_id` VARCHAR(50) DEFAULT NULL COMMENT '交易柜台',
  `target_comp_id` VARCHAR(50) DEFAULT NULL COMMENT '帐号命令路由地址,由ORS配置并提供',
  `hedge_type` INT(11) DEFAULT NULL COMMENT '0. 投机(Speculation), 1. 套保(Hedge)',
  `used` INT(11) DEFAULT NULL COMMENT '是否使用. 0: 没有使用, 1: 使用',
  PRIMARY KEY (`id`),
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='交易帐号表';

/*产品表*/
DROP TABLE IF EXISTS product_info;
CREATE TABLE `product_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL COMMENT '名称',
  `uuid` VARCHAR(40) DEFAULT NULL COMMENT 'uuid',
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` SMALLINT(6) DEFAULT '1' COMMENT '产品状态: -100:软删除, 1正常. 默认值为1',
  PRIMARY KEY (`id`),
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品表';

/*风控证券表*/
DROP TABLE IF EXISTS risk_control_secrity;
CREATE TABLE risk_control_secrity (
    `id` BIGINT (20) NOT NULL AUTO_INCREMENT,
    `product_id` BIGINT (20) NOT NULL COMMENT '产品id, 关联到 product_info 表的id字段',
    `symbol` VARCHAR (10) COMMENT '股票代码',
    `market` VARCHAR (40) COMMENT '市场',
    `ctime` DATETIME NOT NULL COMMENT '创建时间',
    `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_uid` BIGINT (20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
    PRIMARY KEY (`id`),
    KEY `p_s_m_id` (`product_id`, `symbol`, `market`) USING BTREE,
    KEY `cuid` (`create_uid`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '风控证券表' ;

/*产品与策略对应关系表*/
DROP TABLE IF EXISTS product_strategy;
CREATE TABLE `product_strategy` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT(20) NOT NULL COMMENT '产品id, 关联到 product_info 表的id字段',
  `strategy_id` BIGINT(20) NOT NULL COMMENT '策略id, 关联到 strategy_baseinfo 表的id字段',
  `funds_proportion` DECIMAL(10,4) DEFAULT NULL COMMENT '资金使用比例',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` SMALLINT(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `p_st_id` (`product_id`,`strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品与策略对应关系表';

/*产品策略与交易帐号对应关系表*/
DROP TABLE IF EXISTS product_strategy_trade_account;
CREATE TABLE product_strategy_trade_account (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` BIGINT(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的id字段',
  `trade_account_id` BIGINT(20) NOT NULL COMMENT '交易帐号id, 关联到 trade_account 表的id字段',
  `account_order` int not null COMMENT '交易帐号的序号. 这个来源于 strategy_account_template 表的 account_order 字段',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` SMALLINT(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `p_st_ta_id` (`product_strategy_id`,`trade_account_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品策略与交易帐号对应关系表';

/*产品策略证券表 ( product_strategy_security ) 也就是交易标的的一部分 (A) */
DROP TABLE IF EXISTS product_strategy_security;
CREATE TABLE product_strategy_security (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` BIGINT(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的id字段',
  `symbol` VARCHAR(10)  COMMENT '股票代码',
  `market` VARCHAR(40)  COMMENT '市场',
  `max_position`  DECIMAL(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `psid` (`product_strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品策略证券表';

/*产品策略板块信息表( product_strategy_plate ) 也就是交易标的的一部分 (B)*/
DROP TABLE IF EXISTS product_strategy_plate;
CREATE TABLE product_strategy_plate (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` BIGINT(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的 id 字段',
  `name` VARCHAR(40) NOT NULL COMMENT '名称',
  `max_position` DECIMAL(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `system_plate_id` DECIMAL(22,0) DEFAULT NULL COMMENT '系统板块id, 关联到 gta_data.plate_platetree 表的plateid字段. 当为-1时不关联, -1表示是自定义板块',
  `trace_system_plate` INT COMMENT '是否跟踪系统板块.0不跟踪, 1跟踪 (只有在 systemPlateId 不为-1时有意义)',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_uid` BIGINT(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `psid` (`product_strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=88888888888888 DEFAULT CHARSET=utf8 COMMENT='产品策略板块信息表';

/*产品策略板块对应的成份股 ( product_strategy_plate_security ) */
DROP TABLE IF EXISTS product_strategy_plate_security;
CREATE TABLE product_strategy_plate_security (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `plate_id` BIGINT(20) NOT NULL COMMENT '板块id, 关联到 product_strategy_plate 表的 id 字段',
  `symbol` VARCHAR(10)  COMMENT '股票代码',
  `market` VARCHAR(40)  COMMENT '市场',
  PRIMARY KEY (`id`),
  KEY `pid` (`plate_id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品策略板块对应的成份股';


/* 产品策略板块屏蔽股票表 ( product_strategy_plate_shield_secuirty ) */
DROP TABLE IF EXISTS product_strategy_plate_shield_secuirty;
CREATE TABLE product_strategy_plate_shield_secuirty (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `plate_id` BIGINT(20) NOT NULL COMMENT '板块id, 关联到 product_strategy_plate 表的 id 字段',
  `symbol` VARCHAR(10)  COMMENT '股票代码',
  `market` VARCHAR(40)  COMMENT '市场',
  `shield_date` DATE    COMMENT '屏蔽日期',
  PRIMARY KEY (`id`),
  KEY `pid` (`plate_id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品策略板块屏蔽股票表';
