CREATE DATABASE  IF NOT EXISTS `socialnetwork` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `socialnetwork`;
-- MySQL dump 10.13  Distrib 8.0.33, for macos13 (arm64)
--
-- Host: localhost    Database: socialnetwork
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  `id_parent_com` bigint DEFAULT NULL,
  `id_post` bigint NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_parrent_idx` (`id_parent_com`),
  KEY `fk_comment_user_idx` (`id_user`),
  KEY `fk_comment_post_idx` (`id_post`),
  CONSTRAINT `fk_comment_parrent` FOREIGN KEY (`id_parent_com`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`id_post`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `place` varchar(50) NOT NULL,
  `id_user` bigint NOT NULL,
  `id_social_group` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_sg_idx` (`id_social_group`),
  KEY `fk_event_user_idx` (`id_user`),
  CONSTRAINT `fk_event_sg` FOREIGN KEY (`id_social_group`) REFERENCES `socialgroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_event_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendrequest`
--

DROP TABLE IF EXISTS `friendrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendrequest` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` tinyint NOT NULL,
  `id_user1` bigint NOT NULL,
  `id_user2` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fr_user1_idx` (`id_user1`),
  KEY `fk_fr_user2_idx` (`id_user2`),
  CONSTRAINT `fk_fr_user1` FOREIGN KEY (`id_user1`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fr_user2` FOREIGN KEY (`id_user2`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendrequest`
--

LOCK TABLES `friendrequest` WRITE;
/*!40000 ALTER TABLE `friendrequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `friendrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_user1` bigint NOT NULL,
  `id_user2` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_friends_user2_idx` (`id_user2`),
  CONSTRAINT `fk_friends_user1` FOREIGN KEY (`id_user1`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_friends_user2` FOREIGN KEY (`id_user2`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupmember`
--

DROP TABLE IF EXISTS `groupmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groupmember` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_joined` datetime NOT NULL,
  `id_user` bigint NOT NULL,
  `id_social_group` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gm_user_idx` (`id_user`),
  KEY `fk_gm_sg_idx` (`id_social_group`),
  CONSTRAINT `fk_gm_sg` FOREIGN KEY (`id_social_group`) REFERENCES `socialgroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_gm_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupmember`
--

LOCK TABLES `groupmember` WRITE;
/*!40000 ALTER TABLE `groupmember` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupmember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membershiprequest`
--

DROP TABLE IF EXISTS `membershiprequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membershiprequest` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` tinyint NOT NULL,
  `id_social_group` bigint NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mr_user_idx` (`id_user`),
  KEY `fk_mr_sg_idx` (`id_social_group`),
  CONSTRAINT `fk_mr_sg` FOREIGN KEY (`id_social_group`) REFERENCES `socialgroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_mr_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membershiprequest`
--

LOCK TABLES `membershiprequest` WRITE;
/*!40000 ALTER TABLE `membershiprequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `membershiprequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `text` varchar(255) NOT NULL,
  `type` tinyint NOT NULL,
  `deleted` tinyint NOT NULL,
  `id_user` bigint NOT NULL,
  `id_social_group` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_user_idx` (`id_user`),
  KEY `fk_post_sg_idx` (`id_social_group`),
  CONSTRAINT `fk_post_sg` FOREIGN KEY (`id_social_group`) REFERENCES `socialgroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_post_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialgroup`
--

DROP TABLE IF EXISTS `socialgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socialgroup` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `id_admin` bigint NOT NULL,
  `type` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sg_admin_idx` (`id_admin`),
  CONSTRAINT `fk_sg_admin` FOREIGN KEY (`id_admin`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialgroup`
--

LOCK TABLES `socialgroup` WRITE;
/*!40000 ALTER TABLE `socialgroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `active` tinyint NOT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `donotdisturb` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-24  9:54:10
