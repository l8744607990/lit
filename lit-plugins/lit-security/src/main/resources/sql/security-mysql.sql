CREATE TABLE lit_authority
(
  authority_id    INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '权限码ID',
  authority_code  VARCHAR(32) NOT NULL DEFAULT '' COMMENT '权限码',
  authority_name  VARCHAR(128) NOT NULL DEFAULT '' COMMENT '权限码名称',
  authority_type  VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '权限类型',
  memo   VARCHAR(256) NOT NULL DEFAULT '' COMMENT '备注',
  sys_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限码';

CREATE TABLE lit_role(
  role_id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '角色Id',
  role_code VARCHAR(32) NOT NULL DEFAULT '' COMMENT '角色编号',
  role_name VARCHAR(128) NOT NULL DEFAULT '' COMMENT '角色名',
  memo VARCHAR(512) DEFAULT '' COMMENT '备注',
  sys_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

CREATE TABLE lit_role_authority(
  role_authority_id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  role_id INT UNSIGNED NOT NULL COMMENT '角色Id',
  role_code VARCHAR(32) DEFAULT '' COMMENT '角色编号',
  authority_id INT UNSIGNED NOT NULL COMMENT '权限码Id',
  authority_code VARCHAR(32) DEFAULT '' COMMENT '权限码',
  sys_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

CREATE TABLE lit_user_role(
  user_role_id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL COMMENT '用户Id',
  user_name VARCHAR(64) DEFAULT '' COMMENT '用户名',
  role_id INT UNSIGNED NOT NULL COMMENT '角色Id',
  role_code VARCHAR(32) DEFAULT '' COMMENT '角色code',
  sys_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';