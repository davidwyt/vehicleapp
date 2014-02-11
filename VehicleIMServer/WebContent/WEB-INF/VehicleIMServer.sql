/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50533
Source Host           : localhost:3306
Source Database       : vehicleimserver

Target Server Type    : MYSQL
Target Server Version : 50533
File Encoding         : 65001

Date: 2014-01-26 10:44:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `filetransmission`
-- ----------------------------
DROP TABLE IF EXISTS `filetransmission`;
CREATE TABLE `filetransmission` (
  `TOKEN` varchar(255) NOT NULL,
  `PATH` varchar(255) DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `TRANSMISSIONTIME` BIGINT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`TOKEN`),
  KEY `SOURCE` (`SOURCE`),
  KEY `TARGET` (`TARGET`),
  KEY `STATUS` (`STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of filetransmission
-- ----------------------------

-- ----------------------------
-- Table structure for `followship`
-- ----------------------------
DROP TABLE IF EXISTS `followship`;
CREATE TABLE `followship` (
  `FOLLOWER` varchar(255) NOT NULL,
  `FOLLOWEE` varchar(255) NOT NULL,
  PRIMARY KEY (`FOLLOWER`,`FOLLOWEE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of followship
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `ID` varchar(255) NOT NULL,
  `CONTENT` varchar(255) DEFAULT NULL,
  `SENTTIME` BIGINT UNSIGNED DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `TYPE` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `SOURCE` (`SOURCE`),
  KEY `TARGET` (`TARGET`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `offlinemessage`
-- ----------------------------
DROP TABLE IF EXISTS `offlinemessage`;
CREATE TABLE `offlinemessage` (
  `ID` varchar(255) NOT NULL,
  `CONTENT` varchar(255) DEFAULT NULL,
  `SENTTIME` BIGINT UNSIGNED DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `TYPE` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `SOURCE` (`SOURCE`),
  KEY `TARGET` (`TARGET`),
  KEY `SENTTIME` (`SENTTIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of offlinemessage
-- ----------------------------

--
-- Table structure for table `followshipinvitation`
--

DROP TABLE IF EXISTS `followshipinvitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `followshipinvitation` (
  `ID` char(40) NOT NULL,
  `SOURCE` char(100) NOT NULL,
  `TARGET` char(100) NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `REQTIME` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `SOURCE` (`SOURCE`),
  KEY `TARGET` (`TARGET`),
  KEY `STATUS` (`STATUS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
