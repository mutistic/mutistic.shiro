/*
Navicat MySQL Data Transfer

Source Server         : mydb
Source Server Version : 50716
Source Host           : 127.0.0.1:3306
Source Database       : xfrj

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2019-05-05 16:04:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id_` bigint(20) NOT NULL,
  `username` varchar(60) NOT NULL,
  `password_` varchar(60) NOT NULL,
  `author_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '角色: 0-用户,1-管理员',
  PRIMARY KEY (`id_`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;
