/*1.1版本数据权限相关, 新加的表*/

USE qic_db;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `qdb_data_permission`;
/*数据权限表*/
CREATE TABLE `qdb_data_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(28) NOT NULL COMMENT '对应逻辑数据库的编码类别',
  `pid` bigint(20) DEFAULT NULL COMMENT '父菜单id, 关联本身的id，根节点用null表示',
  PRIMARY KEY (`id`),
  KEY `fkc_data_parent` (`pid`) USING BTREE,
  CONSTRAINT `qdb_data_permission_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `qdb_data_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='数据权限表';

DROP TABLE IF EXISTS `user_qdb_data`;
/*用户数据权限关联表 关联用户表和数据权限表*/
CREATE TABLE `user_qdb_data` (
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `did` bigint(20) NOT NULL DEFAULT '0' COMMENT '功能权限ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `content` text DEFAULT NULL COMMENT '自定义内容',
  PRIMARY KEY (`uid`,`did`),
  KEY `fkc_user_data_uid` (`uid`) USING BTREE,
  KEY `fkc_user_data_did` (`did`) USING BTREE,
  CONSTRAINT `user_qdb_data_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`),
  CONSTRAINT `user_qdb_data_ibfk_2` FOREIGN KEY (`did`) REFERENCES `qdb_data_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据权限关联表';


