DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user
(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(64) COMMENT '用户名',
  password VARCHAR(64) COMMENT '密码',
  user_sex VARCHAR(10) COMMENT '性别',
  nick_name VARCHAR(64) COMMENT '昵称',
  enable BOOLEAN DEFAULT TRUE COMMENT '启用标志'
) AUTO_INCREMENT=1;

DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role
(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) COMMENT '角色名称',
  data_scope INTEGER COMMENT '数据范围：1-所有数据 2-所在机构及以下数据 3-所在机构 4-仅本人 5-按明细',
  enable BOOLEAN DEFAULT TRUE COMMENT '是否启用'
);

DROP TABLE IF EXISTS t_user_role;
CREATE TABLE t_user_role
(
  user_id BIGINT COMMENT 'userId',
  role_id BIGINT COMMENT 'roleId',
  PRIMARY KEY (user_id,role_id)
);

DROP TABLE IF EXISTS t_menu;
CREATE TABLE t_menu
(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT NOT NULL COMMENT '父菜单id',
  name VARCHAR(64) COMMENT '名称',
  type VARCHAR(64) COMMENT '类型',
  target VARCHAR(128) COMMENT '目标',
  icon_cls VARCHAR(100) COMMENT '图标',
  uri VARCHAR(512) COMMENT 'uri',
  permission VARCHAR(200) COMMENT '权限标识',
  remarks VARCHAR(128) COMMENT '描述',
  enable BOOLEAN DEFAULT TRUE COMMENT '启用标志',
  create_by BIGINT COMMENT '创建人id',
  create_time TIMESTAMP DEFAULT now() COMMENT '创建时间',
  update_by BIGINT COMMENT '修改人id',
  update_time TIMESTAMP DEFAULT now() COMMENT '修改时间'
);

DROP TABLE IF EXISTS t_role_menu;
CREATE TABLE t_role_menu
(
  role_id BIGINT NOT NULL ,
  menu_id BIGINT NOT NULL ,
  PRIMARY KEY (role_id,menu_id)
);