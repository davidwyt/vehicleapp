--
-- Table structure for table `MESSAGE`
--

DROP TABLE IF EXISTS `MESSAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MESSAGE` (
  `ID` CHAR(40) NOT NULL,
  `SOURCE` CHAR(100) NOT NULL,
  `TARGET` CHAR(100) NOT NULL,
  `STATUS` VARCHAR(255) NOT NULL,
  `CONTENT` TEXT NOT NULL,
  `SENTTIME` DATETIME NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID` (`ID`),
  KEY `SOURCE` (`SOURCE`)
) ENGINE = MYISAM DEFAULT CHARSET = UTF8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `FOLLOWSHIP`;
CREATE TABLE `FOLLOWSHIP`(
	`FOLLOWER` CHAR(100) NOT NULL,
	`FOLLOWEE` CHAR(100) NOT NULL,
	
	PRIMARY KEY (`FOLLOWER`, `FOLLOWEE`),
	KEY `FOLLOWER` (`FOLLOWER`)
) ENGINE = MYISAM DEFAULT CHARSET = UTF8;
