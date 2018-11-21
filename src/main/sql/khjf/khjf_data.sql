INSERT INTO t_khjf_type (name, short_name, remarks, type, del_flag, create_by, create_time) VALUES ('定期存款日均','DQCKRJ',null,'1',FALSE,1,now());
INSERT INTO t_khjf_type (name, short_name, remarks, type, del_flag, create_by, create_time) VALUES ('活期存款日均','HQCKRJ',null,'1',FALSE,1,now());
INSERT INTO t_khjf_type (name, short_name, remarks, type, del_flag, create_by, create_time) VALUES ('微信付款','WXFK',null,'1',FALSE,1,now());
INSERT INTO t_khjf_type (name, short_name, remarks, type, del_flag, create_by, create_time) VALUES ('POS消费','POS',null,'1',FALSE,1,now());
INSERT INTO t_khjf_type (name, short_name, remarks, type, del_flag, create_by, create_time) VALUES ('短信通扣费','DXT',null,'1',FALSE,1,now());

INSERT INTO t_khjf_handle_config(points_type_id, start_date, end_date, next_start_date, next_end_date, execute_type, create_by) VALUES (1,null,null,'2017-01-01','2017-01-31','2',1);
INSERT INTO t_khjf_handle_config(points_type_id, start_date, end_date, next_start_date, next_end_date, execute_type, create_by) VALUES (2,null,null,'2017-01-01','2017-01-31','2',1);
INSERT INTO t_khjf_handle_config(points_type_id, start_date, end_date, next_start_date, next_end_date, execute_type, create_by) VALUES (3,null,null,'2017-01-01','2017-01-01','1',1);
INSERT INTO t_khjf_handle_config(points_type_id, start_date, end_date, next_start_date, next_end_date, execute_type, create_by) VALUES (4,null,null,'2017-01-01','2017-01-01','1',1);
INSERT INTO t_khjf_handle_config(points_type_id, start_date, end_date, next_start_date, next_end_date, execute_type, create_by) VALUES (5,null,null,'2017-01-01','2017-01-01','1',1);

INSERT INTO t_khjf_points_type_change(points_type_id, value, start_date, end_date, valid_flag, create_by, update_by, update_time) VALUES (1,10,null,null,TRUE,1,1,now());
INSERT INTO t_khjf_points_type_change(points_type_id, value, start_date, end_date, valid_flag, create_by, update_by, update_time) VALUES (2,10,null,null,TRUE,1,1,now());
INSERT INTO t_khjf_points_type_change(points_type_id, value, start_date, end_date, valid_flag, create_by, update_by, update_time) VALUES (3,1,null,null,TRUE,1,1,now());
INSERT INTO t_khjf_points_type_change(points_type_id, value, start_date, end_date, valid_flag, create_by, update_by, update_time) VALUES (4,1,null,null,TRUE,1,1,now());
INSERT INTO t_khjf_points_type_change(points_type_id, value, start_date, end_date, valid_flag, create_by, update_by, update_time) VALUES (5,1,null,null,TRUE,1,1,now());

INSERT INTO t_khjf_points_type_available(points_type_id, start_date, end_date, available_status, valid_flag, create_by, update_by, update_time) VALUES (1,null,null,'0',true,1,1,now());
INSERT INTO t_khjf_points_type_available(points_type_id, start_date, end_date, available_status, valid_flag, create_by, update_by, update_time) VALUES (2,null,null,'0',true,1,1,now());
INSERT INTO t_khjf_points_type_available(points_type_id, start_date, end_date, available_status, valid_flag, create_by, update_by, update_time) VALUES (3,null,null,'0',true,1,1,now());
INSERT INTO t_khjf_points_type_available(points_type_id, start_date, end_date, available_status, valid_flag, create_by, update_by, update_time) VALUES (4,null,null,'0',true,1,1,now());
INSERT INTO t_khjf_points_type_available(points_type_id, start_date, end_date, available_status, valid_flag, create_by, update_by, update_time) VALUES (5,null,null,'0',true,1,1,now());

INSERT INTO t_khjf_global_parameter(name, value, valid_flag, remarks, create_by, update_by, update_time) VALUES ('consumeAllOrgPointsFlag','true',TRUE,'初始化时默认允许跨网点兑换积分',1,1,now());
INSERT INTO t_khjf_global_parameter(name, value, valid_flag, remarks, create_by, update_by, update_time) VALUES ('clearAllOrgPointsFlag','false',TRUE,'初始化时默认不允许跨网点清理积分',1,1,now());
