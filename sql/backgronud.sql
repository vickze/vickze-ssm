-- 菜单
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) COMMENT '菜单名称',
  `url` varchar(200) COMMENT '菜单URL',
  `perms` varchar(500) COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) COMMENT '菜单图标',
  `order_num` int COMMENT '排序',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单';

-- 系统用户
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) COMMENT '密码',
  `salt` varchar(20) COMMENT '盐',
  `email` varchar(100) COMMENT '邮箱',
  `mobile` varchar(100) COMMENT '手机号',
  `status` tinyint COMMENT '状态  0：禁用   1：正常',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- 角色
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) COMMENT '角色名称',
  `remark` varchar(100) COMMENT '备注',
  `dept_id` bigint(20) COMMENT '部门ID',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

-- 用户与角色对应关系
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID',
  `role_id` bigint COMMENT '角色ID',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色对应关系';

-- 角色与菜单对应关系
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `menu_id` bigint COMMENT '菜单ID',
  `gmt_create` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单对应关系';

-- 初始化数据

-- 初始用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `gmt_create`, `gmt_modified`) VALUES ('1', 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'zyk@yk95.top', '18898874899', '1', now(), now());

-- 初始菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES ('0', '0', '系统管理', NULL, NULL, '0', 'fa fa-cog', '0', now(), now());

-- 用户管理菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    VALUES ('1', '系统用户', 'sys/user.html', NULL, '1', 'fa fa-file-code-o', '6', now(), now());

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '查看', null, 'sys:user:list,sys:user:info', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '新增', null, 'sys:user:save,sys:user:select,sys:role:select', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '修改', null, 'sys:user:update,sys:user:select,sys:role:select', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '删除', null, 'sys:user:delete', '2', null, '6', now(), now();

-- 角色管理菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    VALUES ('1', '角色', 'sys/role.html', NULL, '1', 'fa fa-file-code-o', '6', now(), now());

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '查看', null, 'sys:role:list,sys:role:info', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '新增', null, 'sys:role:save,sys:role:select,sys:menu:list', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '修改', null, 'sys:role:update,sys:role:select,sys:menu:list', '2',null, '6',  now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '删除', null, 'sys:role:delete', '2', null, '6', now(), now();

-- 菜单管理菜单SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    VALUES ('1', '菜单', 'sys/menu.html', NULL, '1', 'fa fa-file-code-o', '6', now(), now());

-- 按钮父菜单ID
set @parentId = @@identity;

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '新增', null, 'sys:menu:save,sys:menu:select', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '修改', null, 'sys:menu:update,sys:menu:select', '2', null, '6', now(), now();
INSERT INTO `sys_menu` (`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`)
    SELECT @parentId, '删除', null, 'sys:menu:delete', '2', null, '6', now(), now();


