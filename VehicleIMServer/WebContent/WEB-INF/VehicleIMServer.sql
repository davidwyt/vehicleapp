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
  PRIMARY KEY (`id`),
  KEY `ID` (`ID`),
  KEY `SOURCE` (`SOURCE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;