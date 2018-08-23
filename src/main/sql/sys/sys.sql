--菜单表
CREATE TABLE t_sys_menu
(
  id SERIAL PRIMARY KEY NOT NULL,
  parent_id INTEGER NOT NULL,
  parent_ids INTEGER[] NOT NULL,
  name VARCHAR(100) NOT NULL,
  sort INTEGER NOT NULL,
  type VARCHAR(64),
  uri VARCHAR(2000),
  target VARCHAR(100),
  icon_cls VARCHAR(100),
  is_show CHAR(1) NOT NULL,
  permission VARCHAR(200),
  description VARCHAR(255),
  remarks VARCHAR(255),
  create_by INTEGER NOT NULL,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER NOT NULL,
  update_time TIMESTAMPTZ NOT NULL,
  del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

-- t_sys_menu 表注释
COMMENT ON SEQUENCE t_sys_menu_id_seq IS '菜单表id序列';
COMMENT ON TABLE t_sys_menu IS '菜单表';
-- t_sys_menu 表字段注释
COMMENT ON COLUMN t_sys_menu.id IS '编号';
COMMENT ON COLUMN t_sys_menu.parent_id IS '父级编号';
COMMENT ON COLUMN t_sys_menu.parent_ids IS '所有父级编号';
COMMENT ON COLUMN t_sys_menu.name IS '名称';
COMMENT ON COLUMN t_sys_menu.sort IS '排序';
COMMENT ON COLUMN t_sys_menu.type IS '类型:菜单,功能';
COMMENT ON COLUMN t_sys_menu.uri IS 'URI';
COMMENT ON COLUMN t_sys_menu.target IS '目标';
COMMENT ON COLUMN t_sys_menu.icon_cls IS '图标';
COMMENT ON COLUMN t_sys_menu.is_show IS '是否在菜单中显示';
COMMENT ON COLUMN t_sys_menu.permission IS '权限标识';
COMMENT ON COLUMN t_sys_menu.description IS '描述信息';
COMMENT ON COLUMN t_sys_menu.remarks IS '备注信息';
COMMENT ON COLUMN t_sys_menu.create_by IS '创建者';
COMMENT ON COLUMN t_sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_sys_menu.update_by IS '更新者';
COMMENT ON COLUMN t_sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN t_sys_menu.del_flag IS '删除标志';


-- 机构表
DROP TABLE IF EXISTS t_sys_organization;
CREATE TABLE t_sys_organization
(
  id SERIAL PRIMARY KEY NOT NULL,
  parent_id INTEGER NOT NULL,
  parent_ids INTEGER[] NOT NULL,
  hz_org_code VARCHAR(10) DEFAULT NULL ,
  code VARCHAR(10),
  name VARCHAR(100) NOT NULL,
  sort INTEGER NOT NULL,
  icon_cls VARCHAR(100),
  area_id INTEGER DEFAULT NULL,
  type CHARACTER(3) NOT NULL,
  grade CHARACTER(2) NOT NULL DEFAULT '0',
  address VARCHAR(1024),
  zip_code VARCHAR(100),
  master VARCHAR(100),
  phone VARCHAR(200),
  fax VARCHAR(200),
  email VARCHAR(200),
  useable VARCHAR(64),
  primary_person INTEGER,
  deputy_person INTEGER,
  remarks VARCHAR(1024),
  create_by INTEGER NOT NULL,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER NOT NULL,
  update_time TIMESTAMPTZ NOT NULL,
  del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

-- t_sys_organization 表注释
COMMENT ON SEQUENCE t_sys_organization_id_seq IS '机构表id序列';
COMMENT ON TABLE t_sys_organization IS '机构表';
-- t_sys_organization 表字段注释
COMMENT ON COLUMN t_sys_organization.id IS '唯一编号(主键)';
COMMENT ON COLUMN t_sys_organization.parent_id IS '父级编号';
COMMENT ON COLUMN t_sys_organization.parent_ids IS '所有父级编号';
COMMENT ON COLUMN t_sys_organization.hz_org_code IS '汇总机构编号';
COMMENT ON COLUMN t_sys_organization.code IS '机构编号';
COMMENT ON COLUMN t_sys_organization.name IS '机构名称';
COMMENT ON COLUMN t_sys_organization.sort IS '排序';
COMMENT ON COLUMN t_sys_organization.icon_cls IS '图标';
COMMENT ON COLUMN t_sys_organization.area_id IS '归属区域';
COMMENT ON COLUMN t_sys_organization.type IS '机构类型 0:公司/企业; 100:网点分组; 200:部门分组; 101:本部; 102: 汇总; 103:营业部; 104:支行; 105:分理处; 106:信用卡中心; 201:管理部门; 202:下设中心;';
COMMENT ON COLUMN t_sys_organization.grade IS '机构等级';
COMMENT ON COLUMN t_sys_organization.address IS '联系地址';
COMMENT ON COLUMN t_sys_organization.zip_code IS '邮政编码';
COMMENT ON COLUMN t_sys_organization.master IS '负责人';
COMMENT ON COLUMN t_sys_organization.phone IS '电话';
COMMENT ON COLUMN t_sys_organization.fax IS '传真';
COMMENT ON COLUMN t_sys_organization.email IS '邮箱';
COMMENT ON COLUMN t_sys_organization.useable IS '是否启用';
COMMENT ON COLUMN t_sys_organization.primary_person IS '主负责人';
COMMENT ON COLUMN t_sys_organization.deputy_person IS '副负责人';
COMMENT ON COLUMN t_sys_organization.remarks IS '备注信息';
COMMENT ON COLUMN t_sys_organization.create_by IS '创建者';
COMMENT ON COLUMN t_sys_organization.create_time IS '创建时间';
COMMENT ON COLUMN t_sys_organization.update_by IS '更新者';
COMMENT ON COLUMN t_sys_organization.update_time IS '更新时间';
COMMENT ON COLUMN t_sys_organization.del_flag IS '删除标志';


-- 角色表
DROP TABLE IF EXISTS t_sys_role;
CREATE TABLE t_sys_role
(
  id SERIAL PRIMARY KEY NOT NULL,
  module_id INTEGER,
  name VARCHAR(100) NOT NULL,
  enname VARCHAR(255),
  role_type VARCHAR(64),
  data_scope INTEGER,
  is_sys BOOLEAN DEFAULT NULL ,
  useable BOOLEAN NOT NULL DEFAULT TRUE,
  remarks VARCHAR(255),
  create_by INTEGER NOT NULL,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER NOT NULL,
  update_time TIMESTAMPTZ NOT NULL,
  del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

-- t_sys_role 表注释
COMMENT ON SEQUENCE t_sys_role_id_seq IS '角色表id序列';
COMMENT ON TABLE t_sys_role IS '角色表';
-- t_sys_role 表字段注释
COMMENT ON COLUMN t_sys_role.id IS '编号';
COMMENT ON COLUMN t_sys_role.module_id IS '角色归属模块';
COMMENT ON COLUMN t_sys_role.name IS '角色名称';
COMMENT ON COLUMN t_sys_role.enname IS '英文名称';
COMMENT ON COLUMN t_sys_role.role_type IS '角色类型: management: 管理角色; user: 普通角色';
COMMENT ON COLUMN t_sys_role.data_scope IS '数据范围: 1: 所有数据; 2: 所在机构及以下数据; 3: 所在机构数据; 4: 仅本人数据; 5: 按明细设置';
COMMENT ON COLUMN t_sys_role.is_sys IS '是否系统数据: true:此数据只有超级管理员能进行修改; false: 拥有角色修改权限可以进行修改';
COMMENT ON COLUMN t_sys_role.useable IS '是否可用';
COMMENT ON COLUMN t_sys_role.remarks IS '备注信息';
COMMENT ON COLUMN t_sys_role.create_by IS '创建者';
COMMENT ON COLUMN t_sys_role.create_time IS '创建时间';
COMMENT ON COLUMN t_sys_role.update_by IS '更新者';
COMMENT ON COLUMN t_sys_role.update_time IS '更新时间';
COMMENT ON COLUMN t_sys_role.del_flag IS '删除标志';


-- 角色-菜单表
CREATE TABLE t_sys_role_menu
(
  role_id INTEGER NOT NULL,
  menu_id INTEGER NOT NULL,
  PRIMARY KEY (role_id, menu_id)
);
-- t_sys_role_menu 表注释
COMMENT ON TABLE t_sys_role_menu IS '角色-菜单关联表';
-- t_sys_role_menu 表字段注释
COMMENT ON COLUMN t_sys_role_menu.role_id IS '角色编号';
COMMENT ON COLUMN t_sys_role_menu.menu_id IS '菜单编号';

-- 角色-机构表
CREATE TABLE t_sys_role_organization
(
  role_id INTEGER NOT NULL,
  organization_id INTEGER NOT NULL,
  PRIMARY KEY (role_id, organization_id)
);
-- t_sys_role_organization 表注释
COMMENT ON TABLE t_sys_role_organization IS '角色-机构关联表';
-- t_sys_role_organization 表字段注释
COMMENT ON COLUMN t_sys_role_organization.role_id IS '角色编号';
COMMENT ON COLUMN t_sys_role_organization.organization_id IS '机构编号';

-- 用户表
CREATE TABLE t_sys_user
(
  id SERIAL PRIMARY KEY NOT NULL,
  code VARCHAR(64) NOT NULL,
  login_password VARCHAR(100) NOT NULL,
  audit_password VARCHAR(100) NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(200),
  phone VARCHAR(200),
  mobile VARCHAR(200),
  type INTEGER,
  photo VARCHAR(1000),
  id_card VARCHAR(20),
  birthday DATE,
  gender VARCHAR(10),
  address VARCHAR(1000),
  entry_date DATE,
  post VARCHAR(100),
  login_useable BOOLEAN NOT NULL DEFAULT TRUE,
  audit_useable BOOLEAN NOT NULL DEFAULT FALSE,
  remarks VARCHAR(1024) DEFAULT NULL,
  create_by INTEGER NOT NULL,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER NOT NULL,
  update_time TIMESTAMPTZ NOT NULL,
  login_ip VARCHAR(100),
  login_time TIMESTAMPTZ,
  del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

-- t_sys_user 表注释
COMMENT ON SEQUENCE t_sys_user_id_seq IS '用户表id序列';
COMMENT ON TABLE t_sys_user IS '用户表';
-- t_sys_user 表字段注释
COMMENT ON COLUMN t_sys_user.id IS '编号';
COMMENT ON COLUMN t_sys_user.code IS '登录名,柜员号';
COMMENT ON COLUMN t_sys_user.login_password IS '登录密码';
COMMENT ON COLUMN t_sys_user.audit_password IS '授权密码';
COMMENT ON COLUMN t_sys_user.name IS '姓名';
COMMENT ON COLUMN t_sys_user.email IS '邮箱';
COMMENT ON COLUMN t_sys_user.phone IS '电话';
COMMENT ON COLUMN t_sys_user.mobile IS '手机';
COMMENT ON COLUMN t_sys_user.type IS '用户类型';
COMMENT ON COLUMN t_sys_user.photo IS '用户头像';
COMMENT ON COLUMN t_sys_user.id_card IS '身份证号';
COMMENT ON COLUMN t_sys_user.birthday IS '出生日期';
COMMENT ON COLUMN t_sys_user.gender IS '性别';
COMMENT ON COLUMN t_sys_user.address IS '住址';
COMMENT ON COLUMN t_sys_user.entry_date IS '入职日期';
COMMENT ON COLUMN t_sys_user.post IS '职务';
COMMENT ON COLUMN t_sys_user.login_useable IS '是否可登录';
COMMENT ON COLUMN t_sys_user.audit_useable IS '是否可复核';
COMMENT ON COLUMN t_sys_user.remarks IS '备注信息';
COMMENT ON COLUMN t_sys_user.create_by IS '创建者';
COMMENT ON COLUMN t_sys_user.create_time IS '创建时间';
COMMENT ON COLUMN t_sys_user.update_by IS '更新者';
COMMENT ON COLUMN t_sys_user.update_time IS '更新时间';
COMMENT ON COLUMN t_sys_user.login_ip IS '最后登陆IP';
COMMENT ON COLUMN t_sys_user.login_time IS '最后登陆时间';
COMMENT ON COLUMN t_sys_user.del_flag IS '删除标志';


--用户-机构 关联表
CREATE TABLE t_sys_user_organization
(
  id SERIAL PRIMARY KEY NOT NULL,
  user_id INTEGER NOT NULL,
  organization_id INTEGER NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE DEFAULT NULL,
  remarks VARCHAR(1024) DEFAULT NULL,
  status INTEGER NOT NULL,
  create_by INTEGER NOT NULL,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER NOT NULL,
  update_time TIMESTAMPTZ NOT NULL,
  del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

-- t_sys_user_organization 表注释
COMMENT ON SEQUENCE t_sys_user_organization_id_seq IS '用户-机构关联表id序列';
COMMENT ON TABLE t_sys_user_organization IS '用户-机构关联表';
-- t_sys_user_organization 字段注释
COMMENT ON COLUMN t_sys_user_organization.id IS '编号,唯一主键';
COMMENT ON COLUMN t_sys_user_organization.user_id IS '用户ID';
COMMENT ON COLUMN t_sys_user_organization.organization_id IS '机构ID';
COMMENT ON COLUMN t_sys_user_organization.start_date IS '调入机构的日期';
COMMENT ON COLUMN t_sys_user_organization.end_date IS '调离机构的日期';
COMMENT ON COLUMN t_sys_user_organization.remarks IS '备注说明';
COMMENT ON COLUMN t_sys_user_organization.status IS '状态';
COMMENT ON COLUMN t_sys_user_organization.create_by IS '创建者';
COMMENT ON COLUMN t_sys_user_organization.create_time IS '创建时间';
COMMENT ON COLUMN t_sys_user_organization.update_by IS '更新者';
COMMENT ON COLUMN t_sys_user_organization.update_time IS '更新时间';
COMMENT ON COLUMN t_sys_user_organization.del_flag IS '删除标志';


--用户-角色表
CREATE TABLE t_sys_user_role
(
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  PRIMARY KEY (user_id, role_id)
);
-- t_sys_user_role 表注释
COMMENT ON TABLE t_sys_user_role IS '用户-角色关联表';
-- t_sys_user_role 表字段注释
COMMENT ON COLUMN t_sys_user_role.user_id IS '用户编号';
COMMENT ON COLUMN t_sys_user_role.role_id IS '角色编号';


/* Create Indexes */
-- t_sys_dict 表索引
-- CREATE INDEX ON t_sys_dict(name ASC);
-- CREATE INDEX ON t_sys_dict(value ASC);
-- CREATE INDEX ON t_sys_dict(del_flag ASC );

-- t_sys_log 表索引
-- CREATE INDEX ON t_sys_log(create_by);
-- CREATE INDEX ON t_sys_log(request_uri ASC);
-- CREATE INDEX ON t_sys_log(type ASC);
-- CREATE INDEX ON t_sys_log (create_time ASC);

-- t_sys_mdict 表索引
-- CREATE INDEX ON t_sys_mdict (parent_id);
/*CREATE INDEX ON t_sys_mdict (parent_ids);*/
-- CREATE INDEX ON t_sys_mdict (del_flag ASC);

-- t_sys_menu 表索引
CREATE INDEX ON t_sys_menu (parent_id);
/*CREATE INDEX ON t_sys_menu (parent_ids);*/
CREATE INDEX ON t_sys_menu (del_flag ASC);

-- t_sys_organization 表索引
CREATE INDEX ON t_sys_organization(parent_id);
-- CREATE INDEX ON t_sys_organization(parent_ids);
CREATE UNIQUE INDEX ON t_sys_organization(code);
CREATE INDEX ON t_sys_organization (del_flag ASC);
CREATE INDEX ON t_sys_organization (type ASC);

-- t_sys_role 表索引
CREATE INDEX ON t_sys_role (del_flag ASC);
CREATE INDEX ON t_sys_role (enname ASC);

-- t_sys_user 表索引
CREATE UNIQUE INDEX ON t_sys_user(code);
CREATE INDEX ON t_sys_user (update_time ASC);
CREATE INDEX ON t_sys_user (del_flag ASC);

-- t_sys_user_organization 表索引
CREATE INDEX ON t_sys_user_organization(user_id);
CREATE INDEX ON t_sys_user_organization(organization_id);