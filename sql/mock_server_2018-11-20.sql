# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 115.159.41.97 (MySQL 5.5.5-10.2.12-MariaDB-10.2.12+maria~jessie)
# Database: mock_server
# Generation Time: 2018-11-20 06:34:15 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table mock_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mock_data`;

CREATE TABLE `mock_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mock_key` varchar(255) DEFAULT NULL COMMENT '精确到服务版本方法的唯一KEY，相同确定为一个组',
  `service` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `request_type` varchar(255) DEFAULT NULL,
  `mock_express` varchar(1024) DEFAULT NULL,
  `mock_compile_json` varchar(1024) DEFAULT NULL,
  `data` mediumtext DEFAULT NULL,
  `method_id` bigint(20) DEFAULT NULL,
  `sort` bigint(11) DEFAULT NULL,
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table mock_metadata
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mock_metadata`;

CREATE TABLE `mock_metadata` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `simple_name` varchar(255) DEFAULT NULL COMMENT '服务简称',
  `service_name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL COMMENT '元数据版本信息(区别于服务version)',
  `metadata` mediumtext DEFAULT NULL,
  `type` tinyint(4) DEFAULT 0 COMMENT '元数据来源类型',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table mock_method
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mock_method`;

CREATE TABLE `mock_method` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_id` bigint(20) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `request_type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `mock_method` WRITE;
/*!40000 ALTER TABLE `mock_method` DISABLE KEYS */;

INSERT INTO `mock_method` (`id`, `service_id`, `service`, `method`, `request_type`, `url`, `created_at`, `updated_at`)
VALUES
	(27,33,'com.today.api.stock.scala.service.StockService2','createStock','POST','createStock','2018-11-17 01:30:44','2018-11-16 17:30:44'),
	(28,33,'com.today.api.stock.scala.service.StockService2','createOrderStock','POST','com','2018-11-17 12:40:51','2018-11-17 04:40:52'),
	(29,36,'com.today.soa.idgen.scala.service.IDService','genId','POST','s','2018-11-19 15:40:29','2018-11-19 07:40:29'),
	(30,37,'com.today.api.stock.scala.service.StockService2','processStock','POST','1','2018-11-20 10:29:09','2018-11-20 02:29:09');

/*!40000 ALTER TABLE `mock_method` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mock_service
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mock_service`;

CREATE TABLE `mock_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `simple_name` varchar(255) DEFAULT NULL COMMENT '服务简称',
  `service` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT '1.0.0',
  `metadata_id` int(11) DEFAULT 0,
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `mock_service` WRITE;
/*!40000 ALTER TABLE `mock_service` DISABLE KEYS */;

INSERT INTO `mock_service` (`id`, `simple_name`, `service`, `version`, `metadata_id`, `created_at`, `updated_at`)
VALUES
	(13,'DemoService','com.today.soa.idgen.service.DemoService','1.0.0',0,'2018-11-05 15:29:29','2018-11-16 04:33:31'),
	(17,'GoodsService','com.today.soa.idgen.service.GoodsService','1.0.0',0,'2018-11-05 15:10:01','2018-11-16 04:33:33'),
	(20,'OpenSupplierService','com.today.soa.idgen.service.OpenSupplierService','1.0.0',0,'2018-11-05 15:10:01','2018-11-16 04:33:35'),
	(21,'PurchaseService','com.today.soa.idgen.service.PurchaseService','1.0.0',0,'2018-11-05 15:10:01','2018-11-16 04:33:35'),
	(25,'EasyService','com.today.easy.EasyService','1.0.0',0,'2018-11-14 11:08:40','2018-11-16 04:33:37'),
	(26,'EasyService2','com.today.easy.EasyService2','1.0.0',0,'2018-11-14 11:09:09','2018-11-16 04:33:37'),
	(33,'StockService','com.today.api.stock.scala.service.StockService','1.0.0',132,'2018-11-16 10:16:13','2018-11-16 04:34:18'),
	(35,'MapleService','com.today.api.MapleService','1.0.0',0,'2018-11-18 23:33:37','2018-11-18 15:33:37'),
	(36,'IDService','com.today.soa.idgen.scala.service.IDService','1.0.0',140,'2018-11-19 15:40:17','2018-11-19 07:40:17'),
	(37,'StockService2','com.today.api.stock.scala.service.StockService2','1.0.0',139,'2018-11-20 10:28:53','2018-11-20 02:28:53');

/*!40000 ALTER TABLE `mock_service` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
