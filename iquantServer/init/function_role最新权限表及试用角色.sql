
/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`qic_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `qic_db`;

/*Table structure for table `function_info` */

DROP TABLE IF EXISTS `function_info`;

CREATE TABLE `function_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `action` varchar(200) DEFAULT NULL COMMENT '功能点操作相应调用',
  `code` varchar(28) NOT NULL COMMENT '每4个字符表示一层, 可以建立7层. 如 00010002. 同时通过层级里的序号可以反应前后关系',
  `pid` bigint(20) DEFAULT NULL COMMENT '父菜单id, 根节点用null表示',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id, 关联到sys_product_info表, 也就是属于指定产品的功能',
  `status` int(5) DEFAULT '0' COMMENT '0 显示，1 不显示',
  PRIMARY KEY (`id`),
  KEY `fkc_function_parent` (`pid`) USING BTREE,
  CONSTRAINT `function_info_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `function_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='功能点表';

/*Data for the table `function_info` */

LOCK TABLES `function_info` WRITE;

insert  into `function_info`(`id`,`name`,`action`,`code`,`pid`,`product_id`,`status`) values (1,'菜单根节点','001','0',0,1,0),(2,'策略','001','0',1,1,0),(3,'策略库','001','0',40,1,0),(4,'策略订阅及评价','001','0',2,1,0),(5,'我的收藏，(取消) ','001','0',2,1,0),(6,'策略编写','001','0',2,1,0),(7,'策略上传','001','0',2,1,0),(8,'信号查看','001','0',2,1,0),(9,'股票池','001','0',1,1,1),(10,'我的收藏和评价','001','0',9,1,1),(11,'组合浏览','001','0',9,1,1),(12,'数据','001','0',1,1,0),(13,'指数','001','0',12,1,0),(14,'股票','001','0',12,1,0),(15,'债券','001','0',12,1,0),(16,'基金','001','0',12,1,0),(17,'期货','001','0',12,1,0),(18,'外汇','001','0',12,1,0),(19,'f10','001','0',12,1,0),(20,'用户管理','001','0',1,1,0),(21,'用户权限管理','001','0',20,1,0),(22,'角色管理','001','0',20,1,0),(23,'上架管理','001','0',2,1,0),(24,'配置管理','001','0',1,1,0),(25,'操作日志','001','0',1,1,0),(26,'管理所有策略','001','0',23,1,0),(27,'管理个人策略','001','0',23,1,0),(29,'市场全景','001','0',12,1,0),(32,'商品','001','0',12,1,0),(34,'市场资讯','001','0',1,1,0),(35,'今日头条','001','0',34,1,0),(36,'公司公告','001','0',34,1,0),(37,'研究报告','001','0',34,1,0),(38,'资讯索引','001','0',34,1,0),(40,'策略列表','001','0',2,1,0),(41,'本地策略、策略模板','001','0',40,1,0),(43,'新建','001','0',2,1,0),(45,'历史记录','001','0',2,1,0),(46,'交易','001','0',1,1,0),(47,'设置','001','0',46,1,0),(48,'盯市','001','0',46,1,0),(49,'绩效分析','001','0',46,1,0),(50,'投资组合','001','0',1,1,0),(51,'组合管理','001','0',50,1,0),(52,'数据浏览','001','0',1,1,0),(53,'时间序列','001','0',52,1,0),(54,'横截面分析','001','0',52,1,0),(55,'专项分析','001','0',1,1,0),(56,'因子回测','001','0',55,1,0),(57,'区间回测','001','0',55,1,0),(58,'事件分析','001','0',55,1,0),(59,'预测与评级','001','0',55,1,0),(60,'贡献度分析','001','0',55,1,0),(61,'条件选股','001','0',55,1,0),(62,'应用','001','0',1,1,0),(63,'板块管理','001','0',62,1,0);

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

/*role_info创建试用用户角色*/
INSERT INTO role_info(id,NAME,desp) VALUES(7,"试用用户","试用用户");

/*給试用用户赋相关权限*/

/*本地策略，策略模板权限*/
INSERT INTO role_function(rid,fid) VALUES(7,41);
/*新建权限*/
INSERT INTO role_function(rid,fid) VALUES(7,43);
/*历史记录权限*/
INSERT INTO role_function(rid,fid) VALUES(7,45);

/*config_manage表里面增加一条注册发邮箱的模板数据*/
INSERT INTO `config_manage` (`key_name`, `key_value`, `remark`)
VALUES
  (
    'registerUser',
    '尊敬的[$userInfo.Name] 您好！您的账号：[$userInfo.Account],密码:[$userInfo.Pwd],有效日期截止到：[$userInfo.EDate],谢谢使用！',
    '注册用户,发邮件告知用户信息模板'
  ) ;

/*系统管理员拥有所有权限(系统管理员对应id=1的情况下c才能执行)*/
DELETE FROM role_function WHERE rid =1;
INSERT  INTO `role_function`(`rid`,`fid`) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1, 27),(1, 29),(1, 32),(1, 34),(1, 35),(1, 36),(1, 37),(1, 38),(1, 40),(1, 41),(1, 43),(1, 45),(1, 46),(1, 47),(1, 48),(1, 49),(1,50),(1,51),(1,52),(1,53),(1,54),(1,55),(1,56),(1,57),(1,58),(1,59),(1,60),(1,61),(1,62),(1,63)   ;

/*是否开放注册功能*/
INSERT INTO qic_db.config_manage (key_name,key_value,remark) VALUES ("showRegister","yes","是否开放注册功能。yes:开放、no:不开放");

