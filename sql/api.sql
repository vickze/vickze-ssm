-- 用户
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `password` varchar(100) COMMENT '密码',
  `salt` varchar(20) COMMENT '盐',
  `mobile` varchar(100) COMMENT '手机号',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';