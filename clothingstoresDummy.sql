-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: clothingstoresdb
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clothingstores`
--

DROP TABLE IF EXISTS `clothingstores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `clothingstores` (
  `store_id` int(11) NOT NULL AUTO_INCREMENT,
  `store_name` varchar(100) NOT NULL,
  `category` enum('SPA','명품','빈티지','캐주얼','스트릿','아메카지','고프코어','미니멀') NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `contact` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clothingstores`
--

LOCK TABLES `clothingstores` WRITE;
/*!40000 ALTER TABLE `clothingstores` DISABLE KEYS */;
INSERT INTO `clothingstores` VALUES (1,'SPA Store Alpha','SPA','Seoul, Seongsu-dong','010-1111-1111'),(2,'SPA Store Beta','SPA','Seoul, Gangnam','010-2222-2222'),(3,'Luxury Brand Alpha','명품','Seoul, Apgujeong','010-3333-3333'),(4,'Luxury Brand Beta','명품','Seoul, Seongsu-dong','010-4444-4444'),(5,'Vintage Shop Alpha','빈티지','Seoul, Hongdae','010-5555-5555'),(6,'Vintage Shop Beta','빈티지','Seoul, Seongsu-dong','010-6666-6666'),(7,'Casual Store Alpha','캐주얼','Seoul, Itaewon','010-7777-7777'),(8,'Casual Store Beta','캐주얼','Seoul, Seongsu-dong','010-8888-8888'),(9,'Streetwear Store Alpha','스트릿','Seoul, Garosu-gil','010-9999-9999'),(10,'Streetwear Store Beta','스트릿','Seoul, Seongsu-dong','010-0000-0000'),(11,'Amekaji Store Alpha','아메카지','Seoul, Seongsu-dong','010-1212-1212'),(12,'Amekaji Store Beta','아메카지','Seoul, Jongno','010-1313-1313'),(13,'Gorp Core Store Alpha','고프코어','Seoul, Seongsu-dong','010-1414-1414'),(14,'Gorp Core Store Beta','고프코어','Seoul, Gangnam','010-1515-1515'),(15,'Minimalist Store Alpha','미니멀','Seoul, Seongsu-dong','010-1616-1616'),(16,'Minimalist Store Beta','미니멀','Seoul, Apgujeong','010-1717-1717');
/*!40000 ALTER TABLE `clothingstores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'clothingstoresdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-04 23:27:31
