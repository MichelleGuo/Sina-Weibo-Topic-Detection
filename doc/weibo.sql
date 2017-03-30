-- MySQL dump 10.13  Distrib 5.5.22, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: weibo
-- ------------------------------------------------------
-- Server version	5.5.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `relations`
--

DROP TABLE IF EXISTS `relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relations` (
  `uid1` int(11) NOT NULL AUTO_INCREMENT,
  `uid2` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relations`
--

LOCK TABLES `relations` WRITE;
/*!40000 ALTER TABLE `relations` DISABLE KEYS */;
/*!40000 ALTER TABLE `relations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `url`
--

DROP TABLE IF EXISTS `url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `http` varchar(1000) DEFAULT NULL,
  `type` tinyint(3) DEFAULT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `url`
--

LOCK TABLES `url` WRITE;
/*!40000 ALTER TABLE `url` DISABLE KEYS */;
INSERT INTO `url` VALUES (1,'http://www.baidu.com/',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `url` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `common` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xwb_user_follow`
--

DROP TABLE IF EXISTS `xwb_user_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xwb_user_follow` (
  `friend_uid` bigint(20) NOT NULL COMMENT '被关注用户ID',
  `fans_uid` bigint(20) NOT NULL COMMENT '粉丝用户ID',
  `datetime` int(10) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`friend_uid`,`fans_uid`),
  KEY `datetime_idx` (`datetime`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='关注表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xwb_user_follow`
--

LOCK TABLES `xwb_user_follow` WRITE;
/*!40000 ALTER TABLE `xwb_user_follow` DISABLE KEYS */;
/*!40000 ALTER TABLE `xwb_user_follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xwb_weibo`
--

DROP TABLE IF EXISTS `xwb_weibo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xwb_weibo` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(20) DEFAULT NULL COMMENT '用户ID',
  `mid` bigint(20) DEFAULT NULL COMMENT '微博ID',
  `content` varchar(500) DEFAULT '' COMMENT '微博内容',
  `isforward` tinyint(1) DEFAULT NULL COMMENT '0:原创,1:转发',
  `username` varchar(150) DEFAULT NULL COMMENT '用户名',
  `forward` int(11) DEFAULT NULL COMMENT '转发数量',
  `favorite` int(11) DEFAULT NULL COMMENT '收藏数量',
  `comment` int(11) DEFAULT NULL COMMENT '评论数量',
  `heart` int(11) DEFAULT NULL COMMENT '称赞数量',
  `addtime` bigint(20) DEFAULT NULL COMMENT '发布时间',
  `comeFrom` varchar(150) DEFAULT NULL COMMENT '来源',
  `href` varchar(255) DEFAULT NULL COMMENT '微博访问地址',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xwb_weibo`
--

LOCK TABLES `xwb_weibo` WRITE;
/*!40000 ALTER TABLE `xwb_weibo` DISABLE KEYS */;
/*!40000 ALTER TABLE `xwb_weibo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-13 10:35:18
