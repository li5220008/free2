/*
SQLyog Ultimate v9.63 
MySQL - 5.5.25 : Database - qic_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`qic_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `qic_db`;

/*Table structure for table `c_ann_announcementinfo_lst` */

DROP TABLE IF EXISTS `c_ann_announcementinfo_lst`;

CREATE TABLE `c_ann_announcementinfo_lst` (
  `ANNOUNCEMENTID` bigint(20) NOT NULL COMMENT '公告ID',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公告日期',
  `TITLE` varchar(510) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告标题',
  `FILESIZE` int(11) DEFAULT NULL COMMENT '公告大小',
  `ANNOUNCEMENTTYPE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告格式',
  `ANNOUNCEMENTROUTE` varchar(800) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告路径',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ANNOUNCEMENTID`),
  KEY `dd_in_1` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089333` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_ann_announcementinfo_lst` */

LOCK TABLES `c_ann_announcementinfo_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_ann_classify_lst` */

DROP TABLE IF EXISTS `c_ann_classify_lst`;

CREATE TABLE `c_ann_classify_lst` (
  `ANNOUNCEMENTID` bigint(20) NOT NULL COMMENT '公告ID',
  `CLASSIFYID` varchar(24) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '公告分类编码',
  `CLASSIFYNAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告分类名称',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公告日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ANNOUNCEMENTID`,`CLASSIFYID`),
  KEY `dd_in_2` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089328` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_ann_classify_lst` */

LOCK TABLES `c_ann_classify_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_ann_security_lst` */

DROP TABLE IF EXISTS `c_ann_security_lst`;

CREATE TABLE `c_ann_security_lst` (
  `ANNOUNCEMENTID` bigint(20) NOT NULL COMMENT '公告ID',
  `SECURITYID` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '相关证券ID',
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券代码',
  `SECURITYTYPEID` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券类别编码',
  `SECURITYTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券类别',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公告日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ANNOUNCEMENTID`,`SECURITYID`),
  KEY `dd_in_3` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089337` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_ann_security_lst` */

LOCK TABLES `c_ann_security_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_ann_summaryinfo_lst` */

DROP TABLE IF EXISTS `c_ann_summaryinfo_lst`;

CREATE TABLE `c_ann_summaryinfo_lst` (
  `ANNOUNCEMENTID` bigint(20) NOT NULL COMMENT '公告ID',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公告日期',
  `SUMMARYTITLE` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '摘要标题',
  `SUMMARYCONTENT` longtext COMMENT '摘要内容',
  `SUMMARYSIZE` int(11) DEFAULT NULL COMMENT '摘要大小',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ANNOUNCEMENTID`),
  KEY `dd_in_4` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089349` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_ann_summaryinfo_lst` */

LOCK TABLES `c_ann_summaryinfo_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_news_accessory_lst` */

DROP TABLE IF EXISTS `c_news_accessory_lst`;

CREATE TABLE `c_news_accessory_lst` (
  `NEWSID` bigint(20) NOT NULL COMMENT '新闻ID',
  `RANK` smallint(6) NOT NULL COMMENT '附件序号',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公布日期',
  `FULLNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '附件名称',
  `BRIEF` varchar(510) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '附件简介',
  `ACCESSORYTYPE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '附件格式',
  `ACCESSORYROUTE` varchar(800) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '附件路径',
  `ACCESSORYSIZE` int(11) DEFAULT NULL COMMENT '附件大小',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`NEWSID`,`RANK`),
  KEY `dd_in_5` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089367` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_news_accessory_lst` */

LOCK TABLES `c_news_accessory_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_news_classify_lst` */

DROP TABLE IF EXISTS `c_news_classify_lst`;

CREATE TABLE `c_news_classify_lst` (
  `NEWSID` bigint(20) NOT NULL COMMENT '新闻ID',
  `CLASSIFYID` varchar(24) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '新闻分类编码',
  `CLASSIFYNAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '新闻分类名称',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`NEWSID`,`CLASSIFYID`),
  KEY `dd_in_6` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089363` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_news_classify_lst` */

LOCK TABLES `c_news_classify_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_news_industry_lst` */

DROP TABLE IF EXISTS `c_news_industry_lst`;

CREATE TABLE `c_news_industry_lst` (
  `NEWSID` bigint(20) NOT NULL COMMENT '新闻ID',
  `INDUSTRYCODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '行业编码',
  `INDUSTRYNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '行业名称',
  `INDUSTRYSYSTEMCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '行业分类标准编码',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`NEWSID`,`INDUSTRYCODE`,`INDUSTRYSYSTEMCODE`),
  KEY `dd_in_7` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089375` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_news_industry_lst` */

LOCK TABLES `c_news_industry_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_news_newsinfo_lst` */

DROP TABLE IF EXISTS `c_news_newsinfo_lst`;

CREATE TABLE `c_news_newsinfo_lst` (
  `NEWSID` bigint(20) NOT NULL COMMENT '新闻ID',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公布日期',
  `TITLE` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `NEWSSUMMARY` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '摘要',
  `NEWSCONTENT` longtext COMMENT '正文',
  `KEYWORD` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '关键词',
  `NEWSSOURCE` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '来源',
  `AUTOR` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '作者',
  `ISACCESSORY` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否附带附件',
  `FILESIZE` int(11) DEFAULT NULL COMMENT '文本大小',
  `NEWSSOURCEID` bigint(20) DEFAULT NULL COMMENT '来源ID',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`NEWSID`),
  KEY `dd_in_8` (`DECLAREDATE`) USING BTREE,
  KEY `IUM51089380` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_news_newsinfo_lst` */

LOCK TABLES `c_news_newsinfo_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_news_security_lst` */

DROP TABLE IF EXISTS `c_news_security_lst`;

CREATE TABLE `c_news_security_lst` (
  `NEWSID` bigint(20) NOT NULL COMMENT '新闻ID',
  `SECURITYID` bigint(20) NOT NULL COMMENT '相关证券ID',
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券代码',
  `SECURITYTYPEID` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '证券类别编码',
  `SECURITYTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券类别',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '公布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`NEWSID`,`SECURITYID`,`SECURITYTYPEID`),
  KEY `IUM51089388` (`UTSID`) USING BTREE,
  KEY `dd_in_9` (`DECLAREDATE`) USING BTREE,
  KEY `news_secid` (`NEWSID`,`SECURITYID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_news_security_lst` */

LOCK TABLES `c_news_security_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_category_lst` */

DROP TABLE IF EXISTS `c_rep_category_lst`;

CREATE TABLE `c_rep_category_lst` (
  `REPORTID` bigint(20) NOT NULL DEFAULT '0' COMMENT '研究报告编码',
  `CATEGORYCODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '发布机构ID',
  `CATEGORY` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '发布机构名称',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`,`CATEGORYCODE`),
  KEY `dd_in_10` (`DECLAREDATE`) USING BTREE,
  KEY `IUM45735859` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_category_lst` */

LOCK TABLES `c_rep_category_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_industry_lst` */

DROP TABLE IF EXISTS `c_rep_industry_lst`;

CREATE TABLE `c_rep_industry_lst` (
  `REPORTID` bigint(20) NOT NULL DEFAULT '0' COMMENT '研究报告编码',
  `INDUSTRYCODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '行业编码',
  `INDUSTRYNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '行业名称',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`,`INDUSTRYCODE`),
  KEY `dd_in_11` (`DECLAREDATE`) USING BTREE,
  KEY `IUM55433531` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_industry_lst` */

LOCK TABLES `c_rep_industry_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_institution_lst` */

DROP TABLE IF EXISTS `c_rep_institution_lst`;

CREATE TABLE `c_rep_institution_lst` (
  `REPORTID` bigint(20) NOT NULL COMMENT '研究报告编码',
  `INSTITUTIONID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '发布机构ID',
  `INSTITUTIONNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '发布机构名称',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`,`INSTITUTIONID`),
  KEY `dd_in_12` (`DECLAREDATE`) USING BTREE,
  KEY `IUM55433511` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_institution_lst` */

LOCK TABLES `c_rep_institution_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_person_lst` */

DROP TABLE IF EXISTS `c_rep_person_lst`;

CREATE TABLE `c_rep_person_lst` (
  `REPORTID` bigint(20) NOT NULL DEFAULT '0' COMMENT '研究报告编码',
  `RESEARCHERNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '研究员姓名',
  `RESEARCHERID` decimal(21,0) NOT NULL DEFAULT '0' COMMENT '研究员ID',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`,`RESEARCHERID`),
  KEY `dd_in_13` (`DECLAREDATE`) USING BTREE,
  KEY `IUM55433543` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_person_lst` */

LOCK TABLES `c_rep_person_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_reportinfo_lst` */

DROP TABLE IF EXISTS `c_rep_reportinfo_lst`;

CREATE TABLE `c_rep_reportinfo_lst` (
  `REPORTID` bigint(20) NOT NULL COMMENT '研究报告编码',
  `TITLE` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `REPORTDATE` datetime DEFAULT NULL COMMENT '报告日期',
  `SUMMARY` longtext COMMENT '摘要',
  `KEYWORDS` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '关键词',
  `FILESTORAGEPATH` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件存贮路径',
  `FILETYPE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件类型',
  `FILESIZE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '文件大小',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`),
  KEY `dd_in_14` (`DECLAREDATE`) USING BTREE,
  KEY `IUM55433515` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_reportinfo_lst` */

LOCK TABLES `c_rep_reportinfo_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `c_rep_security_lst` */

DROP TABLE IF EXISTS `c_rep_security_lst`;

CREATE TABLE `c_rep_security_lst` (
  `REPORTID` bigint(20) NOT NULL COMMENT '研究报告编码',
  `SECURITYID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '证券ID',
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '证券代码',
  `DECLAREDATE` datetime DEFAULT NULL COMMENT '发布日期',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS专用字段',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REPORTID`,`SECURITYID`),
  KEY `dd_in_15` (`DECLAREDATE`) USING BTREE,
  KEY `IUM55433547` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `c_rep_security_lst` */

LOCK TABLES `c_rep_security_lst` WRITE;

UNLOCK TABLES;

/*Table structure for table `capital_change_list` */

DROP TABLE IF EXISTS `capital_change_list`;

CREATE TABLE `capital_change_list` (
  `SN` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据据的自增ID',
  `ReportID` varchar(32) NOT NULL COMMENT '用来约束一个回报所引起的资金与单据流水的变动关\n系',
  `StrategyID` varchar(40) DEFAULT NULL,
  `AccountType` char(1) DEFAULT NULL,
  `TotalCapital` decimal(16,4) DEFAULT NULL,
  `AffectType` int(11) DEFAULT NULL COMMENT '1 开仓部分成交\n            2 开仓完全成交\n            3 平仓部分成交\n            4 平仓完全成交 \n            5 平仓部分撤单 已有一部分成交量，该状态为最终状态\n            6 平仓部分过期 已有一部分成交量，该状态为最终状态 \n            7 无影响 该状态表示一次交易中该持仓品种没有影响 ',
  `TotalAsset` decimal(16,4) DEFAULT NULL,
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `TransactTime` datetime DEFAULT NULL,
  `Account` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`SN`),
  KEY `FK_Account1` (`StrategyID`) USING BTREE,
  CONSTRAINT `capital_change_list_ibfk_1` FOREIGN KEY (`StrategyID`) REFERENCES `capital_list` (`StrategyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金实时变动流水表';

/*Data for the table `capital_change_list` */

LOCK TABLES `capital_change_list` WRITE;

UNLOCK TABLES;

/*Table structure for table `checknewstable` */

DROP TABLE IF EXISTS `checknewstable`;

CREATE TABLE `checknewstable` (
  `table_name` varchar(100) DEFAULT NULL,
  `declare_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `checknewstable` */

LOCK TABLES `checknewstable` WRITE;

UNLOCK TABLES;

/*Table structure for table `config_manage` */

DROP TABLE IF EXISTS `config_manage`;

CREATE TABLE `config_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `key_name` varchar(40) NOT NULL COMMENT 'key_name 键',
  `key_value` varchar(200) DEFAULT NULL COMMENT 'key_value 值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='配置管理表';

/*Data for the table `config_manage` */

LOCK TABLES `config_manage` WRITE;

insert  into `config_manage`(`id`,`key_name`,`key_value`,`remark`) values (1,'strategyUpMsg','尊敬的用户您好！ [$strategy.Name] 策略将于[$strategy.DownTime]上架，请知悉。1地方撒尊敬的用户您好！ [$strategy.Name] 策略将于[$strategy.DownTime]上架，请知悉。1地方撒asdfsadf a尊敬的用户您好！ [$strategy.Name] 策略将于[$strategy.DownTime]上架，请知悉。1地方撒asdfsad','策略上架通知'),(2,'strategyDownMsg','尊敬的用户您好！ [$strategy.Name] 策略将于[$strategy.DownTime]下架','策略下架通知'),(3,'otherMsg','尊敬的[$userInfo.Name] 用户您好！您的新密码：[$userInfo.Pwd]。谢谢。。。','其他消息通知'),(9,'user.excel.template.dir','public/attachment/template.xls','用户上传模板文件路径'),(10,'user.excel.upload.tmp.dir','/data/excel/tmp/','excel保存的临时目录'),(11,'user.excel.upload.official.dir','/data/excel/official/','excel保存的正工目录'),(12,'strategy.upload.temp.dir','/data/strategy/tmp/','策略文件保存手临时目录'),(13,'strategy.upload.official.dir','/var/data/iquantUploadDir','策略文件保存的正式目录'),(14,'others.load.strategy.base.dir','\\\\192.168.102.153\\strategies\\','第三方加载策略文件的基路径'),(15,'show.createOneStrategyPic.path','http://localhost:9100/StrategyCt/generatePic?stids=%s','策略上加通知服务器'),(16,'activateMsg','尊敬的[$userInfo.Name] 您好！您的账号：[$userInfo.Account]已经成功激活,有效日期截止到：[$userInfo.EDate],谢谢使用！','账号激活邮件通知'),(17,'srategy.up.server.switch','3','1 只能设置实时agent服务器 2只能设置实时模拟服务器 3前两个服务器都可设置');

UNLOCK TABLES;

/*Table structure for table `cust_index` */

DROP TABLE IF EXISTS `cust_index`;

CREATE TABLE `cust_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `content` longtext COMMENT '用json格式存放内容',
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `p_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `u_p_id` (`u_id`,`p_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户自定义指标';

/*Data for the table `cust_index` */

LOCK TABLES `cust_index` WRITE;

UNLOCK TABLES;

/*Table structure for table `cust_sec_group` */

DROP TABLE IF EXISTS `cust_sec_group`;

CREATE TABLE `cust_sec_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `sname` varchar(255) DEFAULT NULL,
  `flag` int(11) NOT NULL COMMENT '1.条件股; 2.自选股; 3.其它',
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `p_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `u_p_id` (`u_id`,`p_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户自选股组';

/*Data for the table `cust_sec_group` */

LOCK TABLES `cust_sec_group` WRITE;

UNLOCK TABLES;

/*Table structure for table `cust_sec_list` */

DROP TABLE IF EXISTS `cust_sec_list`;

CREATE TABLE `cust_sec_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `scode` varchar(255) DEFAULT NULL,
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `p_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `comments` varchar(200) DEFAULT NULL COMMENT '备注信息',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_group_id` (`group_id`) USING BTREE,
  KEY `i_u_p_id` (`u_id`,`p_id`) USING BTREE,
  KEY `i_g_id` (`group_id`) USING BTREE,
  CONSTRAINT `cust_sec_list_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `cust_sec_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户自选股组的明细列表';

/*Data for the table `cust_sec_list` */

LOCK TABLES `cust_sec_list` WRITE;

UNLOCK TABLES;

/*Table structure for table `cust_template` */

DROP TABLE IF EXISTS `cust_template`;

CREATE TABLE `cust_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `content` longtext,
  `category` int(11) DEFAULT NULL,
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `p_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `u_p_id` (`u_id`,`p_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户自定义模板';

/*Data for the table `cust_template` */

LOCK TABLES `cust_template` WRITE;

UNLOCK TABLES;

/*Table structure for table `data_permission` */

DROP TABLE IF EXISTS `data_permission`;

CREATE TABLE `data_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(28) NOT NULL COMMENT '对应逻辑数据库的编码类别',
  `pid` bigint(20) DEFAULT NULL COMMENT '父菜单id, 根节点用null表示',
  PRIMARY KEY (`id`),
  KEY `fkc_data_parent` (`pid`) USING BTREE,
  CONSTRAINT `data_permission_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `data_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='数据权限表';

/*Data for the table `data_permission` */

LOCK TABLES `data_permission` WRITE;

insert  into `data_permission`(`id`,`name`,`code`,`pid`) values (1,'股票','LC000001',NULL),(2,'基金','LC000002',NULL),(3,'期货','LC000003',NULL),(4,'债券','LC000004',NULL),(5,'公司/财务','LC000005',NULL);

UNLOCK TABLES;

/*Table structure for table `function_info` */

DROP TABLE IF EXISTS `function_info`;

CREATE TABLE `function_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `action` varchar(200) DEFAULT NULL COMMENT '功能点操作相应调用',
  `code` varchar(28) NOT NULL COMMENT '每4个字符表示一层, 可以建立7层. 如 00010002. 同时通过层级里的序号可以反应前后关系',
  `pid` bigint(20) DEFAULT NULL COMMENT '父菜单id, 根节点用null表示',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id, 关联到sys_product_info表, 也就是属于指定产品的功能',
  PRIMARY KEY (`id`),
  KEY `fkc_function_parent` (`pid`) USING BTREE,
  CONSTRAINT `function_info_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `function_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='功能点表';

/*Data for the table `function_info` */

LOCK TABLES `function_info` WRITE;

insert  into `function_info`(`id`,`name`,`action`,`code`,`pid`,`product_id`) values (1,'菜单根节点','001','0',0,1),(2,'策略超市','001','0',1,1),(3,'策略浏览','001','0',2,1),(4,'策略订阅及评价','001','0',2,1),(5,'我的收藏，(取消) ','001','0',2,1),(6,'策略编写','001','0',2,1),(7,'策略上传','001','0',2,1),(8,'信号查看','001','0',2,1),(9,'股票池','001','0',1,1),(10,'我的收藏和评价','001','0',9,1),(11,'组合浏览','001','0',9,1),(12,'行情报价','001','0',1,1),(13,'指数','001','0',12,1),(14,'沪深股票','001','0',12,1),(15,'债券','001','0',12,1),(16,'基金','001','0',12,1),(17,'期货','001','0',12,1),(18,'外汇','001','0',12,1),(19,'f10','001','0',12,1),(20,'用户管理','001','0',1,1),(21,'用户权限管理','001','0',20,1),(22,'角色管理','001','0',20,1),(23,'策略管理','001','0',1,1),(24,'配置管理','001','0',1,1),(25,'操作日志','001','0',1,1),(26,'管理所有策略','001','0',23,1),(27,'管理个人策略','001','0',23,1);

UNLOCK TABLES;

/*Table structure for table `operation_log_info` */

DROP TABLE IF EXISTS `operation_log_info`;

CREATE TABLE `operation_log_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增长',
  `cdate` datetime NOT NULL COMMENT '操作时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `func` varchar(200) NOT NULL COMMENT '操作功能',
  `content` varchar(200) NOT NULL COMMENT '操作内容',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1168 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

/*Data for the table `operation_log_info` */

LOCK TABLES `operation_log_info` WRITE;

UNLOCK TABLES;

/*Table structure for table `plate_info` */

DROP TABLE IF EXISTS `plate_info`;

CREATE TABLE `plate_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '板块信息表ID',
  `name` varchar(100) DEFAULT NULL COMMENT '自定义板块名',
  `maxShare` decimal(20,0) DEFAULT NULL COMMENT '最大持仓量',
  `system_plate_code` varchar(20) DEFAULT 'null' COMMENT '系统板块代码',
  `produce_strategy_id` bigint(20) NOT NULL COMMENT '外键,关联produce_strategy表的ID',
  PRIMARY KEY (`produce_strategy_id`),
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `plate_info_ibfk_1` FOREIGN KEY (`produce_strategy_id`) REFERENCES `product_strategy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `plate_info` */

LOCK TABLES `plate_info` WRITE;

UNLOCK TABLES;

/*Table structure for table `plate_stock` */

DROP TABLE IF EXISTS `plate_stock`;

CREATE TABLE `plate_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '板块股票对应表ID',
  `plate_id` bigint(20) NOT NULL COMMENT '外键,对应plate_info表的ID',
  `symbol` varchar(20) DEFAULT NULL COMMENT '股票代码',
  `name` varchar(20) DEFAULT NULL COMMENT '股票名称',
  `type` int(2) DEFAULT '0' COMMENT '是否屏蔽当天：1是；0否',
  PRIMARY KEY (`plate_id`),
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `plate_stock_ibfk_1` FOREIGN KEY (`plate_id`) REFERENCES `plate_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `plate_stock` */

LOCK TABLES `plate_stock` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_info` */

DROP TABLE IF EXISTS `product_info`;

CREATE TABLE `product_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL COMMENT '名称',
  `uuid` varchar(40) DEFAULT NULL COMMENT 'uuid',
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT '1' COMMENT '产品状态: -100:软删除, 1正常. 默认值为1',
  PRIMARY KEY (`id`),
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8 COMMENT='产品表';

/*Data for the table `product_info` */

LOCK TABLES `product_info` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy` */

DROP TABLE IF EXISTS `product_strategy`;

CREATE TABLE `product_strategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '产品id, 关联到 product_info 表的id字段',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略id, 关联到 strategy_baseinfo 表的id字段',
  `funds_proportion` decimal(10,4) DEFAULT NULL COMMENT '资金使用比例',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `p_st_id` (`product_id`,`strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10538 DEFAULT CHARSET=utf8 COMMENT='产品与策略对应关系表';

/*Data for the table `product_strategy` */

LOCK TABLES `product_strategy` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy_plate` */

DROP TABLE IF EXISTS `product_strategy_plate`;

CREATE TABLE `product_strategy_plate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` bigint(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的 id 字段',
  `name` varchar(40) NOT NULL COMMENT '名称',
  `max_position` decimal(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `system_plate_id` decimal(22,0) DEFAULT NULL COMMENT '系统板块id, 关联到 gta_data.plate_platetree 表的plateid字段. 当为-1时不关联, -1表示是自定义板块',
  `trace_system_plate` int(11) DEFAULT NULL COMMENT '是否跟踪系统板块.0不跟踪, 1跟踪 (只有在 systemPlateId 不为-1时有意义)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `psid` (`product_strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 COMMENT='产品策略板块信息表';

/*Data for the table `product_strategy_plate` */

LOCK TABLES `product_strategy_plate` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy_plate_security` */

DROP TABLE IF EXISTS `product_strategy_plate_security`;

CREATE TABLE `product_strategy_plate_security` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate_id` bigint(20) NOT NULL COMMENT '板块id, 关联到 product_strategy_plate 表的 id 字段',
  `symbol` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `market` varchar(40) DEFAULT NULL COMMENT '市场',
  PRIMARY KEY (`id`),
  KEY `pid` (`plate_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23976 DEFAULT CHARSET=utf8 COMMENT='产品策略板块对应的成份股';

/*Data for the table `product_strategy_plate_security` */

LOCK TABLES `product_strategy_plate_security` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy_plate_shield_secuirty` */

DROP TABLE IF EXISTS `product_strategy_plate_shield_secuirty`;

CREATE TABLE `product_strategy_plate_shield_secuirty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate_id` bigint(20) NOT NULL COMMENT '板块id, 关联到 product_strategy_plate 表的 id 字段',
  `symbol` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `market` varchar(40) DEFAULT NULL COMMENT '市场',
  `shield_date` date DEFAULT NULL COMMENT '屏蔽日期',
  PRIMARY KEY (`id`),
  KEY `pid` (`plate_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 COMMENT='产品策略板块屏蔽股票表';

/*Data for the table `product_strategy_plate_shield_secuirty` */

LOCK TABLES `product_strategy_plate_shield_secuirty` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy_security` */

DROP TABLE IF EXISTS `product_strategy_security`;

CREATE TABLE `product_strategy_security` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` bigint(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的id字段',
  `symbol` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `market` varchar(40) DEFAULT NULL COMMENT '市场',
  `max_position` decimal(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `psid` (`product_strategy_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1238 DEFAULT CHARSET=utf8 COMMENT='产品策略证券表';

/*Data for the table `product_strategy_security` */

LOCK TABLES `product_strategy_security` WRITE;

UNLOCK TABLES;

/*Table structure for table `product_strategy_trade_account` */

DROP TABLE IF EXISTS `product_strategy_trade_account`;

CREATE TABLE `product_strategy_trade_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` bigint(20) NOT NULL COMMENT '产品策略id(也就是那个绑定的id),关联到 product_strategy 表的id字段',
  `trade_account_id` bigint(20) NOT NULL COMMENT '交易帐号id, 关联到 trade_account 表的id字段',
  `account_order` int(11) NOT NULL COMMENT '交易帐号的序号. 这个来源于 strategy_account_template 表的 account_order 字段',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `p_st_ta_id` (`product_strategy_id`,`trade_account_id`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=805 DEFAULT CHARSET=utf8 COMMENT='产品策略与交易帐号对应关系表';

/*Data for the table `product_strategy_trade_account` */

LOCK TABLES `product_strategy_trade_account` WRITE;

UNLOCK TABLES;

/*Table structure for table `qdb_data_permission` */

DROP TABLE IF EXISTS `qdb_data_permission`;

CREATE TABLE `qdb_data_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(28) NOT NULL COMMENT '对应逻辑数据库的编码类别',
  `pid` bigint(20) DEFAULT NULL COMMENT '父菜单id, 根节点用null表示',
  PRIMARY KEY (`id`),
  KEY `fkc_data_parent` (`pid`) USING BTREE,
  CONSTRAINT `qdb_data_permission_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `qdb_data_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='数据权限表';

/*Data for the table `qdb_data_permission` */

LOCK TABLES `qdb_data_permission` WRITE;

UNLOCK TABLES;

/*Table structure for table `qstrategy` */

DROP TABLE IF EXISTS `qstrategy`;

CREATE TABLE `qstrategy` (
  `StrategyID` varchar(40) NOT NULL,
  `StrategyName` varchar(100) DEFAULT NULL,
  `FilePath` varchar(500) DEFAULT NULL COMMENT '策略下载并解压后存储的完整路径''',
  `FtpPath` varchar(500) DEFAULT NULL COMMENT '策略所存放的ftp的完整路径',
  `AgentIP` varchar(20) DEFAULT NULL COMMENT '分配的Agent的IP地址',
  `IsAutoRun` bit(1) DEFAULT NULL COMMENT '是否自动启动',
  `TransactTime` datetime DEFAULT NULL COMMENT '数据插入时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`StrategyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储当前的策略信息、配置信息、运行信息';

/*Data for the table `qstrategy` */

LOCK TABLES `qstrategy` WRITE;

UNLOCK TABLES;

/*Table structure for table `rep_institution` */

DROP TABLE IF EXISTS `rep_institution`;

CREATE TABLE `rep_institution` (
  `REPORTID` bigint(20) DEFAULT NULL,
  `INSTITUTIONID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `INSTITUTIONNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `STOCKPOOLCODE` bigint(20) DEFAULT NULL,
  `TRADINGDATE` datetime DEFAULT NULL,
  `RETURNDAILY` decimal(8,4) DEFAULT NULL,
  `RETURNWEEKLY` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTWEEK` decimal(8,4) DEFAULT NULL,
  `RETURNMONTHLY` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTMONTH` decimal(8,4) DEFAULT NULL,
  `RETURN3MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTQUARTER` decimal(8,4) DEFAULT NULL,
  `RETURN6MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENT6MONTH` decimal(8,4) DEFAULT NULL,
  `YEARRTD` decimal(8,4) DEFAULT NULL,
  `RETURNANNUAL` decimal(8,4) DEFAULT NULL,
  `RETURN2YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN3YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN5YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURNTOTAL` decimal(8,4) DEFAULT NULL,
  `ANNUALIZEDYIELD` decimal(25,4) DEFAULT NULL,
  `YEARSHARPERATIO` decimal(8,4) DEFAULT NULL,
  KEY `IUM55433511` (`UTSID`) USING BTREE,
  KEY `UQ_REP_INSTITUTION` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `rep_institution` */

LOCK TABLES `rep_institution` WRITE;

UNLOCK TABLES;

/*Table structure for table `rep_reportinfo` */

DROP TABLE IF EXISTS `rep_reportinfo`;

CREATE TABLE `rep_reportinfo` (
  `REPORTID` bigint(20) NOT NULL,
  `TITLE` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `DECLAREDATE` datetime DEFAULT NULL,
  `REPORTDATE` datetime DEFAULT NULL,
  `SUMMARY` longtext COMMENT 'ժҪ',
  `KEYWORDS` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILESTORAGEPATH` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILETYPE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILESIZE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `STOCKPOOLCODE` bigint(20) DEFAULT NULL,
  `STOCKPOOLNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYNAME` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`REPORTID`),
  KEY `IUM55433515` (`UTSID`) USING BTREE,
  KEY `UQ_REP_REPORTINFO` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `rep_reportinfo` */

LOCK TABLES `rep_reportinfo` WRITE;

UNLOCK TABLES;

/*Table structure for table `risk_control_secrity` */

DROP TABLE IF EXISTS `risk_control_secrity`;

CREATE TABLE `risk_control_secrity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL COMMENT '产品id, 关联到 product_info 表的id字段',
  `symbol` varchar(10) DEFAULT NULL COMMENT '股票代码',
  `market` varchar(40) DEFAULT NULL COMMENT '市场',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  PRIMARY KEY (`id`),
  KEY `p_s_m_id` (`product_id`,`symbol`,`market`) USING BTREE,
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8 COMMENT='风控证券表';

/*Data for the table `risk_control_secrity` */

LOCK TABLES `risk_control_secrity` WRITE;

UNLOCK TABLES;

/*Table structure for table `role_function` */

DROP TABLE IF EXISTS `role_function`;

CREATE TABLE `role_function` (
  `rid` bigint(20) NOT NULL COMMENT '对应于 role_info 表的id, 也就是角色id',
  `fid` bigint(20) NOT NULL COMMENT '对应于 function_info 表的id, 也就是功能点id',
  PRIMARY KEY (`rid`,`fid`),
  KEY `fkc_role_function_fid` (`fid`) USING BTREE,
  CONSTRAINT `role_function_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `role_info` (`id`),
  CONSTRAINT `role_function_ibfk_2` FOREIGN KEY (`fid`) REFERENCES `function_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能点与角色关联表';

/*Data for the table `role_function` */

LOCK TABLES `role_function` WRITE;

insert  into `role_function`(`rid`,`fid`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27);

UNLOCK TABLES;

/*Table structure for table `role_info` */

DROP TABLE IF EXISTS `role_info`;

CREATE TABLE `role_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `desp` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `role_info` */

LOCK TABLES `role_info` WRITE;

insert  into `role_info`(`id`,`name`,`desp`) values (1,'系统管理员','超级管理员拥有所有权限');

UNLOCK TABLES;

/*Table structure for table `sale_department` */

DROP TABLE IF EXISTS `sale_department`;

CREATE TABLE `sale_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(200) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='营业部表';

/*Data for the table `sale_department` */

LOCK TABLES `sale_department` WRITE;

UNLOCK TABLES;

/*Table structure for table `scheduling_task` */

DROP TABLE IF EXISTS `scheduling_task`;

CREATE TABLE `scheduling_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  `parameter` text NOT NULL COMMENT '调度数据',
  `module` varchar(300) NOT NULL COMMENT '调度方法',
  `runtime` datetime NOT NULL COMMENT '调度时间',
  `status` smallint(6) NOT NULL DEFAULT '1' COMMENT '1.未执行，2已执行，3，执行中，4执行失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `scheduling_task` */

LOCK TABLES `scheduling_task` WRITE;

UNLOCK TABLES;

/*Table structure for table `security_info` */

DROP TABLE IF EXISTS `security_info`;

CREATE TABLE `security_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '证券表',
  `produce_strategy_id` bigint(20) NOT NULL COMMENT '外键,关联到produce_strategy表的id',
  `symbol` varchar(20) DEFAULT NULL COMMENT '股票代码',
  `exchangeType` varchar(20) DEFAULT NULL COMMENT '市场代码',
  `maxShare` decimal(20,0) DEFAULT NULL COMMENT '最大持仓量',
  PRIMARY KEY (`produce_strategy_id`),
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `security_info_ibfk_1` FOREIGN KEY (`produce_strategy_id`) REFERENCES `product_strategy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `security_info` */

LOCK TABLES `security_info` WRITE;

UNLOCK TABLES;

/*Table structure for table `stk_stockinfo` */

DROP TABLE IF EXISTS `stk_stockinfo`;

CREATE TABLE `stk_stockinfo` (
  `SECURITYID` bigint(20) DEFAULT NULL,
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTITUTIONID` bigint(20) DEFAULT NULL,
  `SHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PYSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `LISTEDDATE` datetime NOT NULL,
  `DELISTEDDATE` datetime DEFAULT NULL,
  `IPOSHARES` bigint(20) DEFAULT NULL,
  `PARVALUE` decimal(10,4) DEFAULT NULL,
  `ISSUEPRICE` decimal(10,4) DEFAULT NULL,
  `CURRENCYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `EXCHANGECODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BOARDID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SHARETYPE` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ISIN` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STATUSID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL,
  `BUSINESSTIME` datetime DEFAULT NULL,
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTS',
  `FORMERNAME` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`SYMBOL`,`LISTEDDATE`,`EXCHANGECODE`),
  KEY `IUM42085400` (`UTSID`) USING BTREE,
  KEY `UQ_STK_STOCKINFO` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stk_stockinfo` */

LOCK TABLES `stk_stockinfo` WRITE;

UNLOCK TABLES;

/*Table structure for table `stk_stockinfo_view_1` */

DROP TABLE IF EXISTS `stk_stockinfo_view_1`;

CREATE TABLE `stk_stockinfo_view_1` (
  `SECURITYID` bigint(20) DEFAULT NULL,
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTITUTIONID` bigint(20) DEFAULT NULL,
  `SHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PYSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `LISTEDDATE` datetime NOT NULL,
  `DELISTEDDATE` datetime DEFAULT NULL,
  `IPOSHARES` bigint(20) DEFAULT NULL,
  `PARVALUE` decimal(10,4) DEFAULT NULL,
  `ISSUEPRICE` decimal(10,4) DEFAULT NULL,
  `CURRENCYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `EXCHANGECODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BOARDID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SHARETYPE` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ISIN` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STATUSID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL,
  `BUSINESSTIME` datetime DEFAULT NULL,
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSֶ',
  `SHAREHOLDERNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `BEFOREMATCHEDSHARES` bigint(20) DEFAULT NULL,
  `BEFOREMATCHEDPERCENTAGE` decimal(10,4) DEFAULT NULL,
  `VOTINGNUMBER` bigint(20) DEFAULT NULL,
  `VOTINGSTATUS` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `AFTERMATCHEDSHARES` bigint(20) DEFAULT NULL,
  `AFTERMATCHEDPERCENTAGE` decimal(10,4) DEFAULT NULL,
  `COMPLETIONDATE` datetime DEFAULT NULL,
  `LISTEDSHARES` bigint(20) DEFAULT NULL,
  `LOCKSHARES` bigint(20) DEFAULT NULL,
  `LISTEDSHARESPERCENTAGE` decimal(10,4) DEFAULT NULL,
  `RANK` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_latvian_ci;

/*Data for the table `stk_stockinfo_view_1` */

LOCK TABLES `stk_stockinfo_view_1` WRITE;

UNLOCK TABLES;

/*Table structure for table `stock_pool_ext` */

DROP TABLE IF EXISTS `stock_pool_ext`;

CREATE TABLE `stock_pool_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `spid` bigint(20) NOT NULL COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
  `discuss_total` int(11) NOT NULL DEFAULT '0' COMMENT '总评论分(也就是所有的评论总分)',
  `discuss_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论人数',
  `collect_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COMMENT='股票池扩展属性';

/*Data for the table `stock_pool_ext` */

LOCK TABLES `stock_pool_ext` WRITE;

UNLOCK TABLES;

/*Table structure for table `stockpool_ext` */

DROP TABLE IF EXISTS `stockpool_ext`;

CREATE TABLE `stockpool_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `stockPoolCode` varchar(40) NOT NULL COMMENT '股票stockPoolCode',
  `source` varchar(500) DEFAULT NULL COMMENT '股票来源',
  `annualizedYield` decimal(16,4) DEFAULT NULL COMMENT '年化收益率',
  `yearJensenRatio` decimal(16,4) DEFAULT NULL COMMENT '夏普比率',
  `updateDate` date DEFAULT NULL COMMENT '更新时间',
  `orgId` varchar(40) DEFAULT NULL COMMENT 'orgId',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140420 DEFAULT CHARSET=utf8 COMMENT='股票池拓展信息表';

/*Data for the table `stockpool_ext` */

LOCK TABLES `stockpool_ext` WRITE;

UNLOCK TABLES;

/*Table structure for table `stp_dailyreturn` */

DROP TABLE IF EXISTS `stp_dailyreturn`;

CREATE TABLE `stp_dailyreturn` (
  `STOCKPOOLCODE` bigint(20) NOT NULL,
  `TRADINGDATE` datetime NOT NULL,
  `RETURNDAILY` decimal(8,4) DEFAULT NULL,
  `RETURNWEEKLY` decimal(8,4) DEFAULT NULL COMMENT 'һ',
  `RETURNCURRENTWEEK` decimal(8,4) DEFAULT NULL,
  `RETURNMONTHLY` decimal(8,4) DEFAULT NULL COMMENT 'һ',
  `RETURNCURRENTMONTH` decimal(8,4) DEFAULT NULL,
  `RETURN3MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTQUARTER` decimal(8,4) DEFAULT NULL,
  `RETURN6MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENT6MONTH` decimal(8,4) DEFAULT NULL,
  `YEARRTD` decimal(8,4) DEFAULT NULL,
  `RETURNANNUAL` decimal(8,4) DEFAULT NULL COMMENT 'һ',
  `RETURN2YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN3YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN5YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURNTOTAL` decimal(8,4) DEFAULT NULL,
  `ANNUALIZEDYIELD` decimal(8,4) DEFAULT NULL,
  `YEARSHARPERATIO` decimal(8,4) DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `REPORTID` bigint(20) DEFAULT NULL,
  `TITLE` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `DECLAREDATE` datetime DEFAULT NULL,
  `REPORTDATE` datetime DEFAULT NULL,
  `SUMMARY` longtext,
  `KEYWORDS` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILESTORAGEPATH` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILETYPE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `FILESIZE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`STOCKPOOLCODE`,`TRADINGDATE`),
  KEY `IUM54500473` (`UTSID`) USING BTREE,
  KEY `UQ_STP_DAILYRETURN` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stp_dailyreturn` */

LOCK TABLES `stp_dailyreturn` WRITE;

UNLOCK TABLES;

/*Table structure for table `stp_parameters` */

DROP TABLE IF EXISTS `stp_parameters`;

CREATE TABLE `stp_parameters` (
  `UPPERSTRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `SECURITYID` bigint(20) DEFAULT NULL,
  `SYMBOL` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `INSTITUTIONID` bigint(20) DEFAULT NULL,
  `SHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PYSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENSHORTNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ENNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `LISTEDDATE` datetime DEFAULT NULL,
  `DELISTEDDATE` datetime DEFAULT NULL,
  `IPOSHARES` bigint(20) DEFAULT NULL,
  `PARVALUE` decimal(10,4) DEFAULT NULL,
  `ISSUEPRICE` decimal(10,4) DEFAULT NULL,
  `CURRENCYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `EXCHANGECODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `BOARDID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SHARETYPE` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ISIN` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STATUSID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STOCKPOOLCODE` bigint(20) DEFAULT NULL,
  `TRADINGDATE` datetime DEFAULT NULL,
  `RETURNDAILY` decimal(8,4) DEFAULT NULL,
  `RETURNWEEKLY` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTWEEK` decimal(8,4) DEFAULT NULL,
  `RETURNMONTHLY` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTMONTH` decimal(8,4) DEFAULT NULL,
  `RETURN3MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENTQUARTER` decimal(8,4) DEFAULT NULL,
  `RETURN6MONTH` decimal(8,4) DEFAULT NULL,
  `RETURNCURRENT6MONTH` decimal(8,4) DEFAULT NULL,
  `YEARRTD` decimal(8,4) DEFAULT NULL,
  `RETURNANNUAL` decimal(8,4) DEFAULT NULL,
  `RETURN2YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN3YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURN5YEARANNUALIZED` decimal(8,4) DEFAULT NULL,
  `RETURNTOTAL` decimal(8,4) DEFAULT NULL,
  `ANNUALIZEDYIELD` decimal(25,4) DEFAULT NULL,
  `YEARSHARPERATIO` decimal(8,4) DEFAULT NULL,
  PRIMARY KEY (`STRATEGYCODE`),
  KEY `IUM54500469` (`UTSID`) USING BTREE,
  KEY `UQ_STP_PARAMETERS` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stp_parameters` */

LOCK TABLES `stp_parameters` WRITE;

UNLOCK TABLES;

/*Table structure for table `stp_sample` */

DROP TABLE IF EXISTS `stp_sample`;

CREATE TABLE `stp_sample` (
  `STOCKPOOLCODE` bigint(20) NOT NULL,
  `STOCKPOOLNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '֤ȯID',
  `SYMBOL` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '֤ȯ',
  `SECURITYTYPECODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '֤ȯ',
  `SECURITYTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '֤ȯ',
  `ADJUSTDATE` datetime NOT NULL,
  `REPORTID` bigint(20) DEFAULT NULL,
  `ADJUSTTYPECODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ADJUSTTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ADJUSTREASON` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `WEIGHT` decimal(7,4) DEFAULT NULL COMMENT 'Ȩ',
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `UPPERSTRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`STOCKPOOLCODE`,`SECURITYID`,`ADJUSTDATE`,`ADJUSTTYPECODE`),
  KEY `IUM54500480` (`UTSID`) USING BTREE,
  KEY `UQ_STP_SAMPLE` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stp_sample` */

LOCK TABLES `stp_sample` WRITE;

UNLOCK TABLES;

/*Table structure for table `stp_stockpool` */

DROP TABLE IF EXISTS `stp_stockpool`;

CREATE TABLE `stp_stockpool` (
  `STOCKPOOLCODE` bigint(20) NOT NULL,
  `STOCKPOOLNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `STRATEGY` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'ѡ',
  `DESCRIPTION` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEFREQUENCY` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STOCKNUM` bigint(20) DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  `SECURITYID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SYMBOL` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYTYPECODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ADJUSTDATE` datetime DEFAULT NULL,
  `REPORTID` bigint(20) DEFAULT NULL,
  `ADJUSTTYPECODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ADJUSTTYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ADJUSTREASON` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `WEIGHT` decimal(7,4) DEFAULT NULL,
  PRIMARY KEY (`STOCKPOOLCODE`),
  KEY `IUM54500484` (`UTSID`) USING BTREE,
  KEY `UQ_STP_STOCKPOOL` (`UPDATEID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stp_stockpool` */

LOCK TABLES `stp_stockpool` WRITE;

UNLOCK TABLES;

/*Table structure for table `stp_style` */

DROP TABLE IF EXISTS `stp_style`;

CREATE TABLE `stp_style` (
  `STOCKPOOLCODE` bigint(20) NOT NULL,
  `STOCKPOOLNAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `STRATEGYCODE` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `STRATEGYNAME` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATEID` bigint(20) DEFAULT NULL,
  `UPDATESTATE` smallint(6) DEFAULT NULL COMMENT 'Update״̬',
  `UPDATETIME` datetime DEFAULT NULL COMMENT 'Updateʱ',
  `BUSINESSTIME` datetime DEFAULT NULL COMMENT 'ͨѶ',
  `UTSID` bigint(20) DEFAULT NULL COMMENT 'UTSר',
  PRIMARY KEY (`STOCKPOOLCODE`,`STRATEGYCODE`),
  KEY `IUM54500488` (`UTSID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `stp_style` */

LOCK TABLES `stp_style` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_account_template` */

DROP TABLE IF EXISTS `strategy_account_template`;

CREATE TABLE `strategy_account_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` bigint(20) DEFAULT NULL COMMENT '策略id, 关联到 strategy_baseinfo 表的 id 字段',
  `account_order` int(11) DEFAULT NULL COMMENT '帐号的序号',
  `account_type` int(11) DEFAULT NULL COMMENT '帐号的类型 0:期货 1：股票',
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT '1' COMMENT '帐号状态: -100:软删除, 1正常. 默认值为1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8 COMMENT='策略配制帐号表. 记录策略xml文件里配制的帐号模板信息';

/*Data for the table `strategy_account_template` */

LOCK TABLES `strategy_account_template` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_baseinfo` */

DROP TABLE IF EXISTS `strategy_baseinfo`;

CREATE TABLE `strategy_baseinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `st_uuid` varchar(40) NOT NULL COMMENT '策略的uuid,用java代码生成, qicore要用到这个值,通过这个值进行关联',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `trade_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '交易类型: 1. 选股型 2. 择时型 3. 交易型 4. 其他',
  `trade_variety` smallint(6) NOT NULL DEFAULT '1' COMMENT '交易品种: 1. 股票 2. 期货 3. 混合',
  `up_time` datetime DEFAULT NULL COMMENT '上架时间',
  `down_time` datetime DEFAULT NULL COMMENT '下架时间',
  `provider` varchar(50) DEFAULT NULL COMMENT '策略提供者',
  `provider_desp` varchar(200) DEFAULT NULL COMMENT '策略提供者的简单描述',
  `desp` varchar(310) DEFAULT NULL COMMENT '策略简介',
  `lookback_stime` datetime DEFAULT NULL COMMENT '策略回测开始时间',
  `lookback_etime` datetime DEFAULT NULL COMMENT '策略回测结束时间',
  `customer_lookback_etime` datetime DEFAULT NULL,
  `customer_lookback_stime` datetime DEFAULT NULL,
  `data_sync_time` datetime DEFAULT NULL,
  `status` smallint(6) NOT NULL DEFAULT '1' COMMENT '策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架',
  `discuss_total` int(11) NOT NULL DEFAULT '0' COMMENT '总评论分(也就是所有的评论总分)',
  `discuss_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论人数',
  `collect_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏人数',
  `order_count` int(11) NOT NULL DEFAULT '0' COMMENT '订阅人数',
  `stup_uid` bigint(20) DEFAULT NULL COMMENT '策略上传用户, 关联到用户表. 这个字段可以为空',
  `pass_time` datetime DEFAULT NULL COMMENT '审核通过时间',
  `upload_time` datetime DEFAULT NULL COMMENT '策略上传时间 ',
  `back_test_sid` int(11) DEFAULT '-1' COMMENT '回测服务器id',
  `del_time` datetime DEFAULT NULL COMMENT '策略删除时间',
  `enginetype_id` int(20) unsigned NOT NULL DEFAULT '1' COMMENT '策略引擎类型id',
  `run_server_id` int(11) NOT NULL DEFAULT '-1' COMMENT '运行服务器id',
  `param` mediumtext COMMENT '策略参数:从配制文件中提取出来的.xml格式',
  `fundsProportion` double DEFAULT NULL COMMENT '资金使用比例,从配制文件中提取',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE,
  KEY `i_strategy_info_uuid` (`st_uuid`) USING BTREE,
  KEY `fkc_strategy_baseinfo_stuid` (`stup_uid`) USING BTREE,
  CONSTRAINT `strategy_baseinfo_ibfk_1` FOREIGN KEY (`stup_uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10230 DEFAULT CHARSET=utf8 COMMENT='策略基本信息表';

/*Data for the table `strategy_baseinfo` */

LOCK TABLES `strategy_baseinfo` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_daily_yield` */

DROP TABLE IF EXISTS `strategy_daily_yield`;

CREATE TABLE `strategy_daily_yield` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `strategyID` varchar(40) NOT NULL COMMENT '策略id. uuid',
  `yield` decimal(16,4) DEFAULT NULL COMMENT '收益率',
  `updateDate` date NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略日收益率表';

/*Data for the table `strategy_daily_yield` */

LOCK TABLES `strategy_daily_yield` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_enginetype` */

DROP TABLE IF EXISTS `strategy_enginetype`;

CREATE TABLE `strategy_enginetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` char(50) NOT NULL COMMENT '引擎名称',
  `summary` char(255) DEFAULT NULL COMMENT '引擎简介说明',
  `status` smallint(6) NOT NULL COMMENT '引擎启用状态：1.已启用，0.禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `strategy_enginetype` */

LOCK TABLES `strategy_enginetype` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_ext` */

DROP TABLE IF EXISTS `strategy_ext`;

CREATE TABLE `strategy_ext` (
  `tradeCount` int(11) DEFAULT NULL COMMENT 'Select count “资金变动流水表中，影响因素为平仓完全成交或平仓部\n分撤销的记录条数”',
  `Yield` decimal(16,4) DEFAULT NULL COMMENT '净利润 / 初始资金',
  `ProfitRatio` decimal(16,4) DEFAULT NULL COMMENT '盈利次数 / 总交易次数',
  `YieldOfMonthStandardDeviation` decimal(16,4) DEFAULT NULL COMMENT 's^2=[(x1-x)^2+(x2-x)^2 +...(xn-x)^2]/（n-1）\n；其中，当交易天数小于30天时，xi是每日收益率的月化值，当交易天数大于30天时，xi是月收益率，x为xi序列均\n值',
  `st_uuid` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `strategy_ext` */

LOCK TABLES `strategy_ext` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_high_low` */

DROP TABLE IF EXISTS `strategy_high_low`;

CREATE TABLE `strategy_high_low` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `StrategyID` varchar(40) DEFAULT NULL,
  `RetainedProfits` decimal(16,4) DEFAULT NULL COMMENT '总盈利+总亏损',
  `GrossProfit` decimal(16,4) DEFAULT NULL COMMENT '净利润+总手续费',
  `OverallProfitability` decimal(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额;\n            一次交易的获利金额大于0，则该次交易为盈利交易\n            ',
  `OverallDeficit` decimal(16,4) DEFAULT NULL COMMENT '∑每次亏损交易的金额；\n            一次交易的获利金额小于0，则该次交易为亏损交易\n            ',
  `CanhsiedRatio` decimal(16,4) DEFAULT NULL COMMENT '总盈利/总亏损',
  `TradeCount` int(11) DEFAULT NULL COMMENT 'Select count “资金变动流水表中，影响因素为平仓完全成交或平仓部\n分撤销的记录条数”',
  `LongPositionTradeCount` int(11) DEFAULT NULL COMMENT '持仓方向为多头的交易次数',
  `ShortPositionTradeCount` int(11) DEFAULT NULL COMMENT '持仓方向为空头的交易次数',
  `ProfitRatio` decimal(16,4) DEFAULT NULL COMMENT '盈利次数 / 总交易次数',
  `ProfitCount` int(11) DEFAULT NULL COMMENT '一次交易的获利大于0，记为1次盈利，将该次数汇总',
  `DeficitCount` int(11) DEFAULT NULL COMMENT '一次交易的获利小于0，记为1次亏损，将该次数汇总',
  `PositionCloseCount` int(11) DEFAULT NULL COMMENT '一次交易的获利等于0，记为1次亏损，将该次数汇总',
  `MAXSingleProfit` decimal(16,4) DEFAULT NULL COMMENT 'Max(所有盈利交易的金额)',
  `MAXSingleDeficit` decimal(16,4) DEFAULT NULL COMMENT 'Max{abs(所有亏交易的金额)|}',
  `MAXSingleProfitRatio` decimal(16,4) DEFAULT NULL COMMENT '单次最大盈利/总盈利',
  `MAXSingleDeficitRatio` decimal(16,4) DEFAULT NULL COMMENT '单次最大亏损/总亏损',
  `ProfitLossRatio` decimal(16,4) DEFAULT NULL COMMENT '净利润/单次最大亏损',
  `SumOfCommission` decimal(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额',
  `Yield` decimal(16,4) DEFAULT NULL COMMENT '净利润 / 初始资金',
  `AvgProfitOfMonth` decimal(16,4) DEFAULT NULL COMMENT '(净利润 / 总交易的天数) ×21.75',
  `FloatingProfitAndLoss` decimal(16,4) DEFAULT NULL COMMENT '回验结束时，如尚有未平持仓用结束点持仓的最新成交价计\n算浮动盈亏',
  `TotalAsset` decimal(16,4) DEFAULT NULL,
  `YieldOfMonth` decimal(16,4) DEFAULT NULL COMMENT '(收益率/总交易天数)× 21.75',
  `YieldOfYear` decimal(16,4) DEFAULT NULL COMMENT '(收益率 / 总交易的天数)×252',
  `MAXSequentialDeficitCapital` decimal(16,4) DEFAULT NULL COMMENT 'Max(连续亏损产生的亏损金额)',
  `LastSequentialDeficitCapital` decimal(16,4) DEFAULT NULL,
  `MAXSequentialProfitCount` int(11) DEFAULT NULL COMMENT 'Max（连续盈利的交易次数）',
  `LastSequentialProfitCount` int(11) DEFAULT NULL,
  `MAXSequentialDeficitCount` int(11) DEFAULT NULL COMMENT 'Max（连续亏损的交易次数）',
  `LastSequentialDeficitCount` int(11) DEFAULT NULL,
  `TradeDays` int(11) DEFAULT NULL COMMENT '依据交易时间区间和交易日历计算(SELECT  COUNT)',
  `MAXShortPositionTime` int(11) DEFAULT NULL COMMENT '连续空仓的最大天数',
  `YieldOfMonthStandardDeviation` decimal(16,4) DEFAULT NULL COMMENT 's^2=[(x1-x)^2+(x2-x)^2 +...(xn-x)^2]/（n-1）\n；其中，当交易天数小于30天时，xi是每日收益率的月化值，当交易天数大于30天时，xi是月收益率，x为xi序列均\n值',
  `SharpeIndex` decimal(16,4) DEFAULT NULL COMMENT '√12（月化收益率-年无风险收益率/12）/月度收益率标准\n差',
  `MovingCost` decimal(16,4) DEFAULT NULL COMMENT '(∑|理论成交价-实际成交价|)/交易次数',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绩效指标扩展表';

/*Data for the table `strategy_high_low` */

LOCK TABLES `strategy_high_low` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_performance_qia` */

DROP TABLE IF EXISTS `strategy_performance_qia`;

CREATE TABLE `strategy_performance_qia` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `strategy_id` int(11) NOT NULL COMMENT '策略id',
  `type` int(11) NOT NULL COMMENT '类型：1.策略回验期收益，2：策略运行期收益',
  `sharp_ratio` double DEFAULT NULL COMMENT '夏普比率SR=  (E(r)-r_f)/(σ(r))',
  `volatility` double DEFAULT NULL COMMENT '波动率σ(r)= √(((∑_1^T▒〖(r_i-E(r))〗^2 ))/(T-1))',
  `beta` double DEFAULT NULL COMMENT 'beta值',
  `average_simple_rate_of_return` double DEFAULT NULL COMMENT 'E(r)= (∑_1^T▒r_i )/T',
  `calmar_ratio` double DEFAULT NULL COMMENT 'Calmar_i= (E(r_i )-r_f)/〖-MD〗_il ',
  `conditional_sharp_ratio` double DEFAULT NULL COMMENT 'Conditional SR=  (E(r)-r_f)/CVaR_i ',
  `excess_return_on_var` double DEFAULT NULL COMMENT 'excess Return On VaR=  (E(r)-r_f)/VaR_i ',
  `highter_partial_moments` double DEFAULT NULL COMMENT 'HPM_ni (ζ)= (∑_1^T▒〖max⁡〖(r_it-ζ,0)〗^n 〗)/T',
  `jensen_ratio` double DEFAULT NULL COMMENT 'α_i=E(r_i)-r_f-β_i (r_M-r_f)',
  `kappa3` double DEFAULT NULL COMMENT '〖K3〗_i=(E(r_i )-ζ)/〖〖(LPM〗_3i (ζ))〗^(1⁄3) ',
  `conditional_var` double DEFAULT NULL COMMENT 'CVaR_i=E(-r_it  ┤| r_it≤-VaR_i) ',
  `lower_partial_moments` double DEFAULT NULL COMMENT 'LPM_ni (ζ)=(∑_1^T▒〖max⁡〖(ζ-r_it,0)〗^n 〗)/T',
  `maximum_drawdown` double DEFAULT NULL COMMENT '最大回撤：上一个全局最大值之后，下一个全局最大值（超出前一个全局最大值）之前出现的最小值所带来的波峰到波谷的最低收益',
  `modified_sharp_ratio` double DEFAULT NULL COMMENT 'modifiedSharpRatio=  (E(r)-r_f)/(MVaR_i )',
  `mvar` double DEFAULT NULL COMMENT ' MVaR_i=-(E(r_i )+σ_i (z_α+((〖z_α〗^2-1) S_i)/6+(〖z_α〗^3-3z_α ) EK_i/24 –(2〖z_α〗^3-5z_α)S_i/36))',
  `omega` double DEFAULT NULL COMMENT 'Ω_i=(E(r_i )-ζ)/(LPM_1i (ζ))+1',
  `sortino_ratio` double DEFAULT NULL COMMENT 'Sortino_i=(E(r_i )-ζ)/〖〖(LPM〗_2i (ζ))〗^(1⁄2) ',
  `treynor_ratio` double DEFAULT NULL COMMENT 'Treynor_i= (E(r_i)-r_f)/β_i ',
  `upside_potential_ratio` double DEFAULT NULL COMMENT 'UPR_i= (HPM_1I (ζ))/〖(LPM_2i (ζ))〗^(1⁄2)  ',
  `var` double DEFAULT NULL COMMENT 'VaR_i=-(E(r_i )+z_α σ_i)',
  `skewness` double DEFAULT NULL COMMENT '偏度',
  `kurtosis` double DEFAULT NULL COMMENT '峰度 ',
  `corr_with_market` double DEFAULT NULL COMMENT '与市场收益相关系数 ',
  `hit_rate` double DEFAULT NULL COMMENT '命中率，即累计盈利次数 ',
  `cumsum_simple_return` double DEFAULT NULL COMMENT '累计收益率',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  KEY `strategy_id` (`strategy_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;

/*Data for the table `strategy_performance_qia` */

LOCK TABLES `strategy_performance_qia` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_performance_qicore` */

DROP TABLE IF EXISTS `strategy_performance_qicore`;

CREATE TABLE `strategy_performance_qicore` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `strategy_id` int(11) NOT NULL COMMENT '策略id',
  `type` int(11) NOT NULL COMMENT '类型：1.策略回验期收益，2：策略运行期收益',
  `retained_profits` decimal(16,4) DEFAULT NULL COMMENT '总盈利+总亏损',
  `gross_profit` decimal(16,4) DEFAULT NULL COMMENT '净利润+总手续费',
  `overall_profitability` decimal(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额; 一次交易的获利金额大于0，则该次交易为盈利交易',
  `overall_deficit` decimal(16,4) DEFAULT NULL COMMENT '∑每次亏损交易的金额； 一次交易的获利金额小于0，则该次交易为亏损交易',
  `canhsied_ratio` decimal(16,4) DEFAULT NULL COMMENT '总盈利/总亏损',
  `trade_count` int(11) DEFAULT NULL COMMENT 'Select count “资金变动流水表中，影响因素为平仓完全成交或平仓部 分撤销的记录条数”',
  `long_position_trade_count` int(11) DEFAULT NULL COMMENT '持仓方向为多头的交易次数',
  `short_position_trade_count` int(11) DEFAULT NULL COMMENT '持仓方向为空头的交易次数',
  `profit_ratio` decimal(16,4) DEFAULT NULL COMMENT '盈利次数 / 总交易次数',
  `profit_count` int(11) DEFAULT NULL COMMENT '一次交易的获利大于0，记为1次盈利，将该次数汇总',
  `deficit_count` int(11) DEFAULT NULL COMMENT '一次交易的获利小于0，记为1次亏损，将该次数汇总',
  `position_close_count` int(11) DEFAULT NULL COMMENT '一次交易的获利等于0，记为1次亏损，将该次数汇总',
  `max_single_profit` decimal(16,4) DEFAULT NULL COMMENT 'Max(所有盈利交易的金额)',
  `max_single_deficit` decimal(16,4) DEFAULT NULL COMMENT 'Max{abs(所有亏交易的金额)|}',
  `max_single_profit_ratio` decimal(16,4) DEFAULT NULL COMMENT '单次最大盈利/总盈利',
  `max_single_deficit_ratio` decimal(16,4) DEFAULT NULL COMMENT '单次最大亏损/总亏损',
  `profit_loss_ratio` decimal(16,4) DEFAULT NULL COMMENT '净利润/单次最大亏损',
  `sum_of_commission` decimal(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额',
  `yield` decimal(16,4) DEFAULT NULL COMMENT '净利润 / 初始资金',
  `avg_profit_of_month` decimal(16,4) DEFAULT NULL COMMENT '(净利润 / 总交易的天数) ×21.75',
  `floating_profit_and_loss` decimal(16,4) DEFAULT NULL COMMENT '回验结束时，如尚有未平持仓用结束点持仓的最新成交价计 算浮动盈亏',
  `total_asset` decimal(16,4) DEFAULT NULL,
  `yield_of_month` decimal(16,4) DEFAULT NULL COMMENT '(收益率/总交易天数)× 21.75',
  `yield_of_year` decimal(16,4) DEFAULT NULL COMMENT '(收益率 / 总交易的天数)×252',
  `max_sequential_deficit_capital` decimal(16,4) DEFAULT NULL COMMENT 'Max(连续亏损产生的亏损金额)',
  `last_sequential_deficit_capital` decimal(16,4) DEFAULT NULL,
  `max_sequential_profit_count` int(11) DEFAULT NULL COMMENT 'Max（连续盈利的交易次数）',
  `last_sequential_profit_count` int(11) DEFAULT NULL,
  `max_sequential_deficit_count` int(11) DEFAULT NULL COMMENT 'Max（连续亏损的交易次数）',
  `last_sequential_deficit_count` int(11) DEFAULT NULL,
  `trade_days` int(11) DEFAULT NULL COMMENT '依据交易时间区间和交易日历计算(SELECT COUNT)',
  `max_short_position_time` int(11) DEFAULT NULL COMMENT '连续空仓的最大天数',
  `yield_of_month_standard_deviation` decimal(16,4) DEFAULT NULL COMMENT 's^2=[(x1-x)^2+(x2-x)^2 +...(xn-x)^2]/（n-1） ；其中，当交易天数小于30天时，xi是每日收益率的月化值，当交易天数大于30天时，xi是月收益率，x为xi序列均值',
  `sharpe_index` decimal(16,4) DEFAULT NULL COMMENT '√12（月化收益率-年无风险收益率/12）/月度收益率标准差',
  `moving_cost` decimal(16,4) DEFAULT NULL COMMENT '(∑|理论成交价-实际成交价|)/交易次数',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `strategy_performance_qicore` */

LOCK TABLES `strategy_performance_qicore` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_position` */

DROP TABLE IF EXISTS `strategy_position`;

CREATE TABLE `strategy_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) NOT NULL COMMENT '策略关联id',
  `security_id` varchar(10) NOT NULL COMMENT '标的代码',
  `security_exchange` smallint(6) NOT NULL COMMENT '交易所信息，按规则进行编码：1.上交所，2.深交所等',
  `positions` double NOT NULL COMMENT '持仓量（或比率）',
  `update_time` datetime NOT NULL COMMENT '更新时间（或日期）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5131 DEFAULT CHARSET=utf8;

/*Data for the table `strategy_position` */

LOCK TABLES `strategy_position` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_security_original` */

DROP TABLE IF EXISTS `strategy_security_original`;

CREATE TABLE `strategy_security_original` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_id` bigint(20) NOT NULL COMMENT '策略id, 关联到 strategy_baseinfo 表的 id 字段',
  `symbol` varchar(26) DEFAULT NULL COMMENT '股票代码, 当type=1时有值',
  `market` varchar(40) DEFAULT NULL COMMENT '市场',
  `system_plate_id` decimal(22,0) DEFAULT NULL COMMENT '系统板块id, 关联到 gta_data.plate_platetree 表的plateid字段. 当type=2时有值',
  `type` int(11) DEFAULT '1' COMMENT '证券类型, 1:股票, 2:板块',
  `max_position` decimal(20,4) DEFAULT NULL COMMENT '最大持仓量',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sid` (`strategy_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20166 DEFAULT CHARSET=utf8 COMMENT='策略原始配制的交易标的, 从策略的配制文件里提取的';

/*Data for the table `strategy_security_original` */

LOCK TABLES `strategy_security_original` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_server` */

DROP TABLE IF EXISTS `strategy_server`;

CREATE TABLE `strategy_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自动增长',
  `enginetype_id` int(11) NOT NULL COMMENT '对应引擎id',
  `server_type` smallint(6) NOT NULL COMMENT '服务器类型：1.回验服务器，2.运行服务器',
  `name` varchar(50) NOT NULL COMMENT '服务器友好名称',
  `ip` varchar(64) NOT NULL COMMENT 'IP地址',
  `status` smallint(6) NOT NULL COMMENT '启用状态：1.已启用，0.禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

/*Data for the table `strategy_server` */

LOCK TABLES `strategy_server` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_trade_account` */

DROP TABLE IF EXISTS `strategy_trade_account`;

CREATE TABLE `strategy_trade_account` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `product_strategy_id` bigint(15) NOT NULL COMMENT '产品策略关联关系id',
  `account_id` bigint(15) NOT NULL COMMENT '账号id',
  `create_uid` bigint(15) DEFAULT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` smallint(6) DEFAULT '1' COMMENT '状态: -100:软删除, 1正常. 默认值为1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `strategy_trade_account` */

LOCK TABLES `strategy_trade_account` WRITE;

UNLOCK TABLES;

/*Table structure for table `strategy_yield` */

DROP TABLE IF EXISTS `strategy_yield`;

CREATE TABLE `strategy_yield` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `strategy_id` int(11) NOT NULL COMMENT '策略id',
  `run_type` smallint(6) DEFAULT NULL COMMENT '运行类型：1.策略回验期收益，2：策略运行期收益',
  `yield_type` smallint(6) DEFAULT NULL COMMENT '收益类型：1.多空收益（即总收益），2.多头收益，3.空头收益',
  `yield` double DEFAULT NULL COMMENT '收益值',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61146 DEFAULT CHARSET=utf8;

/*Data for the table `strategy_yield` */

LOCK TABLES `strategy_yield` WRITE;

UNLOCK TABLES;

/*Table structure for table `sys_product_info` */

DROP TABLE IF EXISTS `sys_product_info`;

CREATE TABLE `sys_product_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统产品信息表';

/*Data for the table `sys_product_info` */

LOCK TABLES `sys_product_info` WRITE;

UNLOCK TABLES;

/*Table structure for table `trade_account` */

DROP TABLE IF EXISTS `trade_account`;

CREATE TABLE `trade_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL COMMENT '名称',
  `account` varchar(60) NOT NULL COMMENT '帐号',
  `password` varchar(100) NOT NULL COMMENT '密码, 目前是明文, 以后考虑使用双向加密',
  `type` int(11) NOT NULL COMMENT '类型, 0:期货, 1:股票',
  `init_capital` decimal(20,4) DEFAULT '0.0000' COMMENT '初始资金',
  `create_uid` bigint(20) NOT NULL COMMENT '创建用户的id, 关联到用户表',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT '1' COMMENT '帐号状态: -100:软删除, 1正常. 默认值为1',
  `client_id` varchar(50) DEFAULT NULL COMMENT '交易柜台',
  `target_comp_id` varchar(50) DEFAULT NULL COMMENT '帐号命令路由地址,由ORS配置并提供',
  `hedge_type` int(11) DEFAULT NULL COMMENT '0. 投机(Speculation), 1. 套保(Hedge)',
  `used` int(11) DEFAULT NULL COMMENT '是否使用. 0: 没有使用, 1: 使用',
  PRIMARY KEY (`id`),
  KEY `cuid` (`create_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=701 DEFAULT CHARSET=utf8 COMMENT='交易帐号表';

/*Data for the table `trade_account` */

LOCK TABLES `trade_account` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_data` */

DROP TABLE IF EXISTS `user_data`;

CREATE TABLE `user_data` (
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID 关联到user_info表',
  `did` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据权限ID 关联到data_permission表',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id, 关联到sys_product_info表',
  `start_time` datetime DEFAULT NULL COMMENT '启用时间',
  `end_time` datetime DEFAULT NULL COMMENT '到期时间',
  PRIMARY KEY (`uid`,`did`),
  KEY `fkc_user_data_uid` (`uid`) USING BTREE,
  KEY `fkc_user_data_did` (`did`) USING BTREE,
  CONSTRAINT `user_data_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`),
  CONSTRAINT `user_data_ibfk_2` FOREIGN KEY (`did`) REFERENCES `data_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据权限关联表';

/*Data for the table `user_data` */

LOCK TABLES `user_data` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `name` varchar(200) NOT NULL COMMENT '名字',
  `account` varchar(200) NOT NULL COMMENT '帐号',
  `pwd` varchar(200) NOT NULL COMMENT '密码, md5加密',
  `phone` varchar(100) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) NOT NULL COMMENT 'email',
  `idcard` varchar(100) DEFAULT NULL COMMENT '身份证号码',
  `sale_dep` bigint(20) DEFAULT NULL COMMENT '营业部id',
  `capital_account` varchar(100) DEFAULT NULL COMMENT '资金帐号',
  `address` varchar(200) DEFAULT NULL COMMENT '联系地址',
  `post` varchar(50) DEFAULT NULL COMMENT '邮编',
  `sdate` date NOT NULL COMMENT '启用日期',
  `edate` date NOT NULL COMMENT '结束日期',
  `apply_date` date DEFAULT NULL COMMENT '申请日期',
  `status` int(11) NOT NULL DEFAULT '10' COMMENT '状态, 1 禁用, 10 正常',
  `utype` int(11) NOT NULL DEFAULT '1' COMMENT '用户类型. 1. 营业部用户, 2. 系统用户',
  `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间,这个字段会在数据插入与更新时自动更新',
  `max_login` int(11) DEFAULT '1' COMMENT '最大登陆数',
  `institution_name` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '机构联系人',
  `limit_count` int(100) DEFAULT NULL COMMENT '授权个数',
  `check_sum` varchar(100) DEFAULT NULL,
  `user_uuid` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE,
  KEY `fk_user_sale_dep` (`sale_dep`) USING BTREE,
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`sale_dep`) REFERENCES `sale_department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=948 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

/*Data for the table `user_info` */

LOCK TABLES `user_info` WRITE;

insert  into `user_info`(`id`,`name`,`account`,`pwd`,`phone`,`email`,`idcard`,`sale_dep`,`capital_account`,`address`,`post`,`sdate`,`edate`,`apply_date`,`status`,`utype`,`utime`,`max_login`,`institution_name`,`contact_name`,`limit_count`,`check_sum`,`user_uuid`) values (1,'管理员','admin','ICy5YqxZB1uWSwcVLSNLcA==','','admin@admin.com','',1,'','','','2013-03-01','2099-01-01','2013-03-01',10,1,'2013-09-29 10:32:24',100,NULL,NULL,10000,'133c26f4f650dd4c08b12f6fd34f2208888e4a0092392f6296f99a262c02a142','9fb83f3a297844b8');

UNLOCK TABLES;

/*Table structure for table `user_message` */

DROP TABLE IF EXISTS `user_message`;

CREATE TABLE `user_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长，用于主键',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `message` text NOT NULL,
  `messge_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '通知时间,这个字段会在数据插入与更新时自动更新',
  `status` smallint(6) NOT NULL DEFAULT '1' COMMENT '1.未读，2.已读',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

/*Data for the table `user_message` */

LOCK TABLES `user_message` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_qdb_data` */

DROP TABLE IF EXISTS `user_qdb_data`;

CREATE TABLE `user_qdb_data` (
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `did` bigint(20) NOT NULL DEFAULT '0' COMMENT '功能权限ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id, 关联到sys_product_info表',
  `content` text,
  PRIMARY KEY (`uid`,`did`),
  KEY `fkc_user_data_uid` (`uid`) USING BTREE,
  KEY `fkc_user_data_did` (`did`) USING BTREE,
  CONSTRAINT `user_qdb_data_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`),
  CONSTRAINT `user_qdb_data_ibfk_2` FOREIGN KEY (`did`) REFERENCES `qdb_data_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据权限关联表';

/*Data for the table `user_qdb_data` */

LOCK TABLES `user_qdb_data` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `uid` bigint(20) NOT NULL,
  `rid` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`,`rid`),
  KEY `fk_user_role_uid` (`uid`) USING BTREE,
  KEY `fk_user_role_rid` (`rid`) USING BTREE,
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `role_info` (`id`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关联表';

/*Data for the table `user_role` */

LOCK TABLES `user_role` WRITE;

insert  into `user_role`(`uid`,`rid`) values (1,1);

UNLOCK TABLES;

/*Table structure for table `user_server` */

DROP TABLE IF EXISTS `user_server`;

CREATE TABLE `user_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id;',
  `ip` varchar(100) NOT NULL COMMENT '用户服务器IP',
  `port` int(20) NOT NULL COMMENT '用户服务器端口',
  PRIMARY KEY (`id`),
  KEY `fk_user_server_uid` (`uid`) USING BTREE,
  CONSTRAINT `user_server_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户服务器配置表';

/*Data for the table `user_server` */

LOCK TABLES `user_server` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_stock_pool_collect` */

DROP TABLE IF EXISTS `user_stock_pool_collect`;

CREATE TABLE `user_stock_pool_collect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id',
  `spid` bigint(20) NOT NULL COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
  `collect_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uspcollect_uid_stid` (`uid`,`spid`) USING BTREE,
  CONSTRAINT `user_stock_pool_collect_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='用户收藏股票池关联表';

/*Data for the table `user_stock_pool_collect` */

LOCK TABLES `user_stock_pool_collect` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_stock_pool_discuss` */

DROP TABLE IF EXISTS `user_stock_pool_discuss`;

CREATE TABLE `user_stock_pool_discuss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id',
  `spid` bigint(20) NOT NULL COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
  `dis_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
  `star` smallint(6) NOT NULL DEFAULT '0' COMMENT '星级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uspdiscuss_uid_stid` (`uid`,`spid`) USING BTREE,
  CONSTRAINT `user_stock_pool_discuss_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户订阅策略关联表';

/*Data for the table `user_stock_pool_discuss` */

LOCK TABLES `user_stock_pool_discuss` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_strategy_collect` */

DROP TABLE IF EXISTS `user_strategy_collect`;

CREATE TABLE `user_strategy_collect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id',
  `stid` bigint(20) NOT NULL COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
  `collect_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uscollect_uid_stid` (`uid`,`stid`) USING BTREE,
  KEY `fkc_user_strategy_collect_stid` (`stid`) USING BTREE,
  CONSTRAINT `user_strategy_collect_ibfk_1` FOREIGN KEY (`stid`) REFERENCES `strategy_baseinfo` (`id`),
  CONSTRAINT `user_strategy_collect_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏策略关联表';

/*Data for the table `user_strategy_collect` */

LOCK TABLES `user_strategy_collect` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_strategy_discuss` */

DROP TABLE IF EXISTS `user_strategy_discuss`;

CREATE TABLE `user_strategy_discuss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id',
  `stid` bigint(20) NOT NULL COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
  `dis_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
  `content` varchar(300) DEFAULT NULL COMMENT '评论内容',
  `star` smallint(6) NOT NULL DEFAULT '0' COMMENT '星级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_usdiscuss_uid_stid` (`uid`,`stid`) USING BTREE,
  KEY `fkc_user_strategy_discuss_stid` (`stid`) USING BTREE,
  CONSTRAINT `user_strategy_discuss_ibfk_1` FOREIGN KEY (`stid`) REFERENCES `strategy_baseinfo` (`id`),
  CONSTRAINT `user_strategy_discuss_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订阅策略关联表';

/*Data for the table `user_strategy_discuss` */

LOCK TABLES `user_strategy_discuss` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_strategy_order` */

DROP TABLE IF EXISTS `user_strategy_order`;

CREATE TABLE `user_strategy_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id',
  `stid` bigint(20) NOT NULL COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
  `order_stime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订阅时间,在insert时插入,在update时不会变',
  `order_etime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '订阅到期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_usorder_uid_stid` (`uid`,`stid`) USING BTREE,
  KEY `fkc_user_strategy_order_stid` (`stid`) USING BTREE,
  CONSTRAINT `user_strategy_order_ibfk_1` FOREIGN KEY (`stid`) REFERENCES `strategy_baseinfo` (`id`),
  CONSTRAINT `user_strategy_order_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订阅策略关联表';

/*Data for the table `user_strategy_order` */

LOCK TABLES `user_strategy_order` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_template` */

DROP TABLE IF EXISTS `user_template`;

CREATE TABLE `user_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
  `uid` bigint(20) NOT NULL COMMENT '对应于 user_info 表id, 也就是用户id; 这边也要存储nt用户的id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type` smallint(6) NOT NULL DEFAULT '1' COMMENT '1. 自定义策略查询 2. 自定义股票池查询',
  `content` mediumtext NOT NULL COMMENT '保存的内容,只提供存储,里面的内容自定义',
  `utype` varchar(20) DEFAULT NULL COMMENT '用户类型. nt:nt用户; qic:qic用户',
  PRIMARY KEY (`id`),
  KEY `fk_user_template_uid` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=881 DEFAULT CHARSET=utf8 COMMENT='用户自定义模板';

/*Data for the table `user_template` */

LOCK TABLES `user_template` WRITE;

UNLOCK TABLES;

/*Table structure for table `v_user_functions` */

DROP TABLE IF EXISTS `v_user_functions`;

/*!50001 DROP VIEW IF EXISTS `v_user_functions` */;
/*!50001 DROP TABLE IF EXISTS `v_user_functions` */;

/*!50001 CREATE TABLE  `v_user_functions`(
 `account` varchar(200) ,
 `email` varchar(100) ,
 `sdate` date ,
 `edate` date ,
 `status` int(11) ,
 `id` bigint(20) ,
 `action` varchar(200) ,
 `code` varchar(28) ,
 `name` varchar(50) ,
 `pid` bigint(20) 
)*/;

/*View structure for view v_user_functions */

/*!50001 DROP TABLE IF EXISTS `v_user_functions` */;
/*!50001 DROP VIEW IF EXISTS `v_user_functions` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`qic`@`%` SQL SECURITY DEFINER VIEW `v_user_functions` AS (select distinct `u`.`account` AS `account`,`u`.`email` AS `email`,`u`.`sdate` AS `sdate`,`u`.`edate` AS `edate`,`u`.`status` AS `status`,`f`.`id` AS `id`,`f`.`action` AS `action`,`f`.`code` AS `code`,`f`.`name` AS `name`,`f`.`pid` AS `pid` from ((((`user_info` `u` join `user_role` `ur` on((`u`.`id` = `ur`.`uid`))) join `role_info` `r` on((`ur`.`rid` = `r`.`id`))) join `role_function` `rf` on((`rf`.`rid` = `r`.`id`))) join `function_info` `f` on((`rf`.`fid` = `f`.`id`)))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
