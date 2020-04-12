/*
Navicat MySQL Data Transfer

Source Server         : MySQL：000000
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : smart_control

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2020-04-12 22:44:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '设备名称',
  `unit` varchar(20) NOT NULL COMMENT '调节单位',
  `value` varchar(20) DEFAULT NULL COMMENT '设备状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='设备表';

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('1', 'aa', '%', '12');
INSERT INTO `device` VALUES ('3', 'ssh', '%', '12');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `key` varchar(50) NOT NULL COMMENT '键',
  `value` varchar(100) DEFAULT NULL COMMENT '值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('enviro', '嘈杂|明亮哦|29|78', '室内环境状态，使用“|”分割参数');
