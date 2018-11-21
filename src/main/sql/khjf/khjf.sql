-- 客户积分类型表
DROP TABLE IF EXISTS t_khjf_type;
CREATE TABLE t_khjf_type
(
  id BIGSERIAL NOT NULL,
  name VARCHAR(128),
  short_name VARCHAR(32) UNIQUE,
  remarks VARCHAR(128),
  all_org_valid BOOLEAN DEFAULT TRUE ,
  type VARCHAR(1),
  del_flag BOOLEAN DEFAULT FALSE ,
  create_by BIGINT,
  create_time TIMESTAMP,
  update_by BIGINT,
  update_time TIMESTAMP,
  PRIMARY KEY (id)
);
-- t_khjf_type 表注释
COMMENT ON TABLE t_khjf_type IS '客户积分类型表';
COMMENT ON SEQUENCE t_khjf_type_id_seq IS '客户积分类型表主键序列';
-- t_khjf_type 表字段注释
COMMENT ON COLUMN t_khjf_type.id IS '编号';
COMMENT ON COLUMN t_khjf_type.name IS '名称';
COMMENT ON COLUMN t_khjf_type.short_name IS '简称';
COMMENT ON COLUMN t_khjf_type.remarks IS '备注信息';
COMMENT ON COLUMN t_khjf_type.all_org_valid IS '是否所有网点都可为客户积累该项，如果为false，则去另外一张表寻找拥有该项的网点';
COMMENT ON COLUMN t_khjf_type.type IS '类型：1-自动，2-手动';
COMMENT ON COLUMN t_khjf_type.del_flag IS '删除标志';
COMMENT ON COLUMN t_khjf_type.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_type.create_time IS '创建时间';

-- 客户积分明细表
DROP TABLE IF EXISTS t_khjf_customer_points_record;
CREATE TABLE t_khjf_customer_points_record
(
  id BIGSERIAL NOT NULL,
  customer_no VARCHAR(10) NOT NULL ,
  account_no VARCHAR(22) NOT NULL ,
  child_account_no VARCHAR(6),
  card_no VARCHAR(19),
  balance NUMERIC(17,2) NOT NULL ,
  subject_no VARCHAR(8),
  org_code VARCHAR(10) NOT NULL ,
  date DATE,
  month VARCHAR(2),
  year VARCHAR(4),
  start_date DATE NOT NULL ,
  end_date DATE NOT NULL ,
  points_type_id BIGINT NOT NULL ,
  value NUMERIC(10,2),
  valid_flag VARCHAR(1) NOT NULL ,
  clear_date TIMESTAMP DEFAULT NULL ,
  teller_code VARCHAR(10) NOT NULL ,
  remarks TEXT,
  create_by INT,
  create_time TIMESTAMP DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL,
  PRIMARY KEY (id)
);
-- t_bldk_non_performing_loan 表注释
COMMENT ON TABLE t_khjf_customer_points_record IS '客户积分明细表';
COMMENT ON SEQUENCE t_khjf_customer_points_record_id_seq IS '客户积分明细表主键序列';
-- t_bldk_non_performing_loan 表字段注释
COMMENT ON COLUMN t_khjf_customer_points_record.id IS '编号';
COMMENT ON COLUMN t_khjf_customer_points_record.customer_no IS '客户号';
COMMENT ON COLUMN t_khjf_customer_points_record.account_no IS '账号';
COMMENT ON COLUMN t_khjf_customer_points_record.child_account_no IS '子账号';
COMMENT ON COLUMN t_khjf_customer_points_record.card_no IS '卡号';
COMMENT ON COLUMN t_khjf_customer_points_record.balance IS '1-存款日均额 2-交易发生额';
COMMENT ON COLUMN t_khjf_customer_points_record.subject_no IS '科目号';
COMMENT ON COLUMN t_khjf_customer_points_record.org_code IS '机构号';
COMMENT ON COLUMN t_khjf_customer_points_record.date IS '积分日期，适用于非区间段类型的积分';
COMMENT ON COLUMN t_khjf_customer_points_record.month IS '月份，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_customer_points_record.year IS '年，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_customer_points_record.start_date IS '起始时间，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_customer_points_record.end_date IS '终止时间，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_customer_points_record.points_type_id IS '积分类型';
COMMENT ON COLUMN t_khjf_customer_points_record.value IS '积分值';
COMMENT ON COLUMN t_khjf_customer_points_record.valid_flag IS '有效标志(0-正常 1-自动清理 2-手工清理)';
COMMENT ON COLUMN t_khjf_customer_points_record.clear_date IS '清理该条积分的日期';
COMMENT ON COLUMN t_khjf_customer_points_record.teller_code IS '积分操作人员 (888888-系统)';
COMMENT ON COLUMN t_khjf_customer_points_record.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_customer_points_record.create_time IS '创建时间';

CREATE INDEX ON t_khjf_customer_points_record(points_type_id);
CREATE INDEX ON t_khjf_customer_points_record(org_code);

-- 处理日志表
DROP TABLE IF EXISTS t_khjf_handle_config;
CREATE TABLE t_khjf_handle_config
(
  id BIGSERIAL NOT NULL,
  points_type_id BIGINT,
  start_date DATE,
  end_date DATE,
  next_start_date DATE,
  next_end_date DATE,
  execute_type VARCHAR(1),
  create_by BIGINT,
  create_time TIMESTAMP NOT NULL DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL ,
  PRIMARY KEY (id)
);
-- 表注释
COMMENT ON TABLE t_khjf_handle_config IS '客户积分配置表';
COMMENT ON SEQUENCE t_khjf_handle_config_id_seq IS '客户积分配置表主键序列';
-- 表字段注释
COMMENT ON COLUMN t_khjf_handle_config.id IS '编号';
COMMENT ON COLUMN t_khjf_handle_config.points_type_id IS '积分类型id';
COMMENT ON COLUMN t_khjf_handle_config.start_date IS '已经处理完毕的开始处理日期';
COMMENT ON COLUMN t_khjf_handle_config.end_date IS '已经处理完毕的终止处理日期（含）';
COMMENT ON COLUMN t_khjf_handle_config.next_start_date IS '下一个开始如理的日期';
COMMENT ON COLUMN t_khjf_handle_config.next_end_date IS '下一个终止处理的日期（含）';
COMMENT ON COLUMN t_khjf_handle_config.execute_type IS '执行类型：1-按日执行 2-按月执行 3-按年执行';
COMMENT ON COLUMN t_khjf_handle_config.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_handle_config.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_handle_config.del_flag IS '删除标志';

--积分类型的积分值修改表
DROP TABLE IF EXISTS t_khjf_points_type_change;
CREATE TABLE t_khjf_points_type_change
(
  id BIGSERIAL NOT NULL,
  points_type_id BIGINT,
  value NUMERIC(10,2),
  start_date DATE,
  end_date DATE,
  valid_flag BOOLEAN,
  remarks TEXT,
  create_by BIGINT,
  create_time TIMESTAMP NOT NULL DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL ,
  PRIMARY KEY (id)
);
--表注释
COMMENT ON TABLE t_khjf_points_type_change IS '单项积分类型的积分值修改表';
COMMENT ON SEQUENCE t_khjf_points_type_change_id_seq IS '单项积分类型的积分值修改表的主键序列';
--表字段注释
COMMENT ON COLUMN t_khjf_points_type_change.id IS '主键';
COMMENT ON COLUMN t_khjf_points_type_change.points_type_id IS '修改了积分值的积分类型的id';
COMMENT ON COLUMN t_khjf_points_type_change.value IS '积分值';
COMMENT ON COLUMN t_khjf_points_type_change.start_date IS '该项生效的起始日期,为null则不限定起始期限';
COMMENT ON COLUMN t_khjf_points_type_change.end_date IS '该项生效的终止日期，为null则不限定终止期限';
COMMENT ON COLUMN t_khjf_points_type_change.valid_flag IS '生效标志';
COMMENT ON COLUMN t_khjf_points_type_change.remarks IS '备注';
COMMENT ON COLUMN t_khjf_points_type_change.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_points_type_change.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_points_type_change.update_by IS '修改者';
COMMENT ON COLUMN t_khjf_points_type_change.update_time IS '修改时间';
COMMENT ON COLUMN t_khjf_points_type_change.del_flag IS '删除标志';


--积分类型的启用停用修改表
DROP TABLE IF EXISTS t_khjf_points_type_available;
CREATE TABLE t_khjf_points_type_available
(
  id BIGSERIAL NOT NULL,
  points_type_id BIGINT,
  start_date DATE,
  end_date DATE,
  available_status VARCHAR(1),
  valid_flag BOOLEAN NOT NULL,
  remarks TEXT,
  create_by BIGINT,
  create_time TIMESTAMP NOT NULL DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL ,
  PRIMARY KEY (id)
);
--表注释
COMMENT ON TABLE t_khjf_points_type_available IS '单项积分类型的启用停用修改表';
COMMENT ON SEQUENCE t_khjf_points_type_available_id_seq IS '单项积分类型的启用停用修改表的主键序列';
--表字段注释
COMMENT ON COLUMN t_khjf_points_type_available.id IS '主键';
COMMENT ON COLUMN t_khjf_points_type_available.points_type_id IS '修改了启用标志的积分类型的id';
COMMENT ON COLUMN t_khjf_points_type_available.start_date IS '该项生效的起始日期,为null则不限定起始期限';
COMMENT ON COLUMN t_khjf_points_type_available.end_date IS '该项生效的终止日期，为null则不限定终止期限';
COMMENT ON COLUMN t_khjf_points_type_available.available_status IS '生效标志: 0-正常 1-停用';
COMMENT ON COLUMN t_khjf_points_type_available.valid_flag IS '是否是最后一条修改的记录';
COMMENT ON COLUMN t_khjf_points_type_available.remarks IS '备注';
COMMENT ON COLUMN t_khjf_points_type_available.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_points_type_available.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_points_type_available.update_by IS '修改者';
COMMENT ON COLUMN t_khjf_points_type_available.update_time IS '修改时间';
COMMENT ON COLUMN t_khjf_points_type_available.del_flag IS '删除标志';

--待审核的手工积分表，审核通过手工积分将被转入t_khjf_customer_record表中
DROP TABLE IF EXISTS t_khjf_manual_points_record;
CREATE TABLE t_khjf_manual_points_record
(
  id BIGSERIAL NOT NULL,
  customer_no VARCHAR(10) NOT NULL ,
  account_no VARCHAR(22) NOT NULL ,
  child_account_no VARCHAR(6),
  card_no VARCHAR(19),
  balance NUMERIC(17,2) NOT NULL ,
  subject_no VARCHAR(8),
  org_code VARCHAR(10) NOT NULL ,
  date DATE,
  month VARCHAR(2),
  year VARCHAR(4),
  start_date DATE NOT NULL ,
  end_date DATE NOT NULL ,
  points_type_id BIGINT NOT NULL ,
  value NUMERIC(10,2),
  valid_flag VARCHAR(1) NOT NULL ,
  teller_code VARCHAR(10) NOT NULL ,
  status VARCHAR(1),
  remarks TEXT,
  create_by INT,
  create_time TIMESTAMP DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL,
  PRIMARY KEY (id)
);
-- t_bldk_non_performing_loan 表注释
COMMENT ON TABLE t_khjf_manual_points_record IS '待审核的手工积分表';
COMMENT ON SEQUENCE t_khjf_manual_points_record_id_seq IS '待审核的手工积分表主键序列';
-- t_bldk_non_performing_loan 表字段注释
COMMENT ON COLUMN t_khjf_manual_points_record.id IS '编号';
COMMENT ON COLUMN t_khjf_manual_points_record.customer_no IS '客户号';
COMMENT ON COLUMN t_khjf_manual_points_record.account_no IS '账号';
COMMENT ON COLUMN t_khjf_manual_points_record.child_account_no IS '子账号';
COMMENT ON COLUMN t_khjf_manual_points_record.card_no IS '卡号';
COMMENT ON COLUMN t_khjf_manual_points_record.balance IS '1-存款日均额 2-交易发生额';
COMMENT ON COLUMN t_khjf_manual_points_record.subject_no IS '科目号';
COMMENT ON COLUMN t_khjf_manual_points_record.org_code IS '机构号';
COMMENT ON COLUMN t_khjf_manual_points_record.date IS '积分日期，适用于非区间段类型的积分';
COMMENT ON COLUMN t_khjf_manual_points_record.month IS '月份，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_manual_points_record.year IS '年，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_manual_points_record.start_date IS '起始时间，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_manual_points_record.end_date IS '终止时间，适用于区间段类型的积分';
COMMENT ON COLUMN t_khjf_manual_points_record.points_type_id IS '积分类型';
COMMENT ON COLUMN t_khjf_manual_points_record.value IS '积分值';
COMMENT ON COLUMN t_khjf_manual_points_record.valid_flag IS '有效标志(0-正常 1-自动清理 2-手工清理)';
COMMENT ON COLUMN t_khjf_manual_points_record.teller_code IS '积分操作人员 (888888-系统)';
COMMENT ON COLUMN t_khjf_manual_points_record.status IS '状态：0-已提交待审核 1-审核通过  2-审核不通过';
COMMENT ON COLUMN t_khjf_manual_points_record.create_by IS '创建者';
COMMENT ON COLUMN t_khjf_manual_points_record.create_time IS '创建时间';


DROP TABLE IF EXISTS t_khjf_consume_points;
CREATE TABLE t_khjf_consume_points
(
  id BIGSERIAL NOT NULL,
  customer_no VARCHAR(10),
  identity_no VARCHAR(21),
  all_org_points NUMERIC(10,2),
  org_code VARCHAR(10),
  current_org_points NUMERIC(10,2),
  consume_all_org_points BOOLEAN,
  points_value NUMERIC(10,2),
  valid_flag VARCHAR(1),
  date TIMESTAMP WITHOUT TIME ZONE,
  remarks TEXT,
  create_by INT,
  create_time TIMESTAMP DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_khjf_consume_points IS '积分兑换使用记录表';
COMMENT ON SEQUENCE t_khjf_consume_points_id_seq IS '积分兑换使用表主键序列';

COMMENT ON COLUMN t_khjf_consume_points.id IS '主键序列';
COMMENT ON COLUMN t_khjf_consume_points.customer_no IS '客户号';
COMMENT ON COLUMN t_khjf_consume_points.identity_no IS '客户证件号码';
COMMENT ON COLUMN t_khjf_consume_points.all_org_points IS '该客户在全行的积分';
COMMENT ON COLUMN t_khjf_consume_points.org_code IS '当前为客户做积分兑换的网点';
COMMENT ON COLUMN t_khjf_consume_points.current_org_points IS '在当前网点的积分';
COMMENT ON COLUMN t_khjf_consume_points.consume_all_org_points IS '积分是否通兑，即是否可以使用全行的积分';
COMMENT ON COLUMN t_khjf_consume_points.points_value IS '本次要兑换使用的积分值';
COMMENT ON COLUMN t_khjf_consume_points.valid_flag IS '清理标志：0-正常 1-自动清理 2-手工清理';
COMMENT ON COLUMN t_khjf_consume_points.date IS '日期';
COMMENT ON COLUMN t_khjf_consume_points.remarks IS '备注';
COMMENT ON COLUMN t_khjf_consume_points.create_by IS '创建人';
COMMENT ON COLUMN t_khjf_consume_points.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_consume_points.update_by IS '更新人';
COMMENT ON COLUMN t_khjf_consume_points.update_time IS '更新时间';
COMMENT ON COLUMN t_khjf_consume_points.del_flag IS '删除标志';


DROP TABLE IF EXISTS t_khjf_global_parameter;
CREATE TABLE t_khjf_global_parameter
(
  id BIGSERIAL NOT NULL,
  name VARCHAR(64) UNIQUE,
  value text,
  valid_flag BOOLEAN,
  remarks TEXT,
  create_by INT,
  create_time TIMESTAMP DEFAULT now(),
  update_by BIGINT,
  update_time TIMESTAMP,
  del_flag BOOLEAN DEFAULT FALSE NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_khjf_global_parameter IS '客户积分配置参数表';
COMMENT ON SEQUENCE t_khjf_global_parameter_id_seq IS '客户积分配置参数表主键序列';

COMMENT ON COLUMN t_khjf_global_parameter.id IS '主键序列';
COMMENT ON COLUMN t_khjf_global_parameter.name IS '属性名称';
COMMENT ON COLUMN t_khjf_global_parameter.value IS '属性值';
COMMENT ON COLUMN t_khjf_global_parameter.valid_flag IS '有效标志';
COMMENT ON COLUMN t_khjf_global_parameter.remarks IS '备注';
COMMENT ON COLUMN t_khjf_global_parameter.create_by IS '创建人';
COMMENT ON COLUMN t_khjf_global_parameter.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_global_parameter.update_by IS '更新人';
COMMENT ON COLUMN t_khjf_global_parameter.update_time IS '更新时间';
COMMENT ON COLUMN t_khjf_global_parameter.del_flag IS '删除标志';

DROP TABLE IF EXISTS t_khjf_clear_points;
CREATE TABLE t_khjf_clear_points
(
  id BIGSERIAL NOT NULL,
  customer_points_record_id BIGINT,
  org_code VARCHAR(10),
  identity_no VARCHAR(21),
  type VARCHAR(1),
  remarks TEXT,
  create_by INT,
  create_time TIMESTAMP DEFAULT now(),
  del_flag BOOLEAN DEFAULT FALSE NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_khjf_clear_points IS '清理客户积分的记录表';
COMMENT ON SEQUENCE t_khjf_clear_points_id_seq IS '表主键序列';

COMMENT ON COLUMN t_khjf_clear_points.id IS '主键序列';
COMMENT ON COLUMN t_khjf_clear_points.customer_points_record_id IS '被清理的积分的id';
COMMENT ON COLUMN t_khjf_clear_points.org_code IS '网点号，只有单户或批量清理时生效';
COMMENT ON COLUMN t_khjf_clear_points.identity_no IS '证件号，只有单户清理时生效';
COMMENT ON COLUMN t_khjf_clear_points.type IS '类型：1-自动清理 2-单条清理 3-单户清理 4-批量清理';
COMMENT ON COLUMN t_khjf_clear_points.remarks IS '备注';
COMMENT ON COLUMN t_khjf_clear_points.create_by IS '创建人';
COMMENT ON COLUMN t_khjf_clear_points.create_time IS '创建时间';
COMMENT ON COLUMN t_khjf_clear_points.del_flag IS '删除标志';


