
CREATE TABLE lit_menu
(
    id INT UNSIGNED PRIMARY KEY  NOT NULL AUTO_INCREMENT COMMENT '菜单Id',
    parent_id INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级菜单Id',
    code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '菜单编码',
    name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '菜单名称',
    icon VARCHAR(64) NOT NULL DEFAULT '' COMMENT '菜单图标',
    url VARCHAR (256) NOT NULL  DEFAULT '' COMMENT '链接',
    order_num SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '顺序号',
    remark VARCHAR(512) NOT NULL  DEFAULT '' COMMENT '备注',
    type VARCHAR(64) NOT NULL DEFAULT ''  COMMENT '菜单类型',
    module VARCHAR(64) NOT NULL  DEFAULT '' COMMENT '所属模块',
    is_enable TINYINT(1) NOT NULL default 1 COMMENT '是否启用',
    sys_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE INDEX uk_pid_code ON lit_menu (parent_id, code);