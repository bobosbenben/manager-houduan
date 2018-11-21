--计算存款日均
DROP FUNCTION IF EXISTS khjf_compute_dqckrj(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR);
CREATE OR REPLACE FUNCTION khjf_compute_dqckrj(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR) RETURNS BOOLEAN AS $$
DECLARE
  sql VARCHAR;
  r RECORD;
  days INTEGER;
  curr_date DATE;
  count_num INTEGER;
  dqckrj_id BIGINT;
  arg_start_date DATE;
  arg_end_date DATE;
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
  record_value NUMERIC(10,2);
BEGIN
  arg_start_date := start_date;
  arg_end_date := end_date;

  --计算相差天数
  SELECT end_date::date - start_date::date INTO days;
  days := days + 1;
  IF days <= 0 THEN
    RAISE NOTICE '终止日期小于起始日期';
    RETURN FALSE ;
  END IF;

  --判断t_hxsj_deposit_account_detail表是否导入成功（特别注意跨年的时候，更改了该表表名）
  curr_date := start_date;
  FOR i IN 1..days LOOP
    SELECT count(id) FROM t_hxsj_deposit_account_detail WHERE date=curr_date::date INTO count_num;
    IF count_num <= 100000 THEN
      RAISE NOTICE '%日t_hxsj_deposit_account_detail表不足100000条记录，计算存款日均终止',curr_date;
      RETURN FALSE ;
    END IF;
    curr_date := curr_date + INTERVAL '1 day';
  END LOOP;

  --获取定期存款日均points_type_id
  SELECT id FROM t_khjf_type WHERE short_name = 'DQCKRJ' AND del_flag = FALSE INTO dqckrj_id;
  IF  dqckrj_id ISNULL THEN
    RAISE NOTICE '定期存款日均的积分类型不存在，请检查客户积分类型表';
    RETURN FALSE ;
  END IF;

  --删除已经存在的记录
  DELETE FROM t_khjf_customer_points_record WHERE month = arg_month AND year = arg_year AND points_type_id = dqckrj_id;

  --创建临时表
  DROP TABLE IF EXISTS tmp_account_no;
  CREATE TEMP TABLE tmp_account_no(account_no VARCHAR(22), child_account_no VARCHAR(6), subject_no VARCHAR(10), org_code VARCHAR(10), dem_regl_flag VARCHAR(1), balance NUMERIC(17,2));

  --将存款日均存入临时表
  INSERT INTO tmp_account_no SELECT account_no, child_account_no, subject_no, org_code, dem_regl_flag, round(sum(before_day_balance)/days,2) FROM t_hxsj_deposit_account_detail WHERE date>=arg_start_date::date AND date<= arg_end_date::date AND before_day_balance>0 AND (subject_no LIKE '2011%' OR subject_no LIKE '2012%' OR subject_no LIKE '2013%' OR subject_no LIKE '2014%' OR subject_no LIKE '2015%' OR subject_no LIKE '2016%' OR subject_no LIKE '2017%' OR subject_no LIKE '2051%' OR subject_no LIKE '2052%' OR subject_no LIKE '2111%' OR subject_no LIKE '2151%' OR subject_no LIKE '2511%') AND account_type = '1' GROUP BY account_no,child_account_no,subject_no,org_code,dem_regl_flag HAVING sum(before_day_balance)/days >=5000;

  --遍历临时表
  sql := 'SELECT a.dem_regl_flag,a.account_no,a.child_account_no,a.subject_no,a.org_code,a.balance FROM tmp_account_no a';
  FOR r IN EXECUTE sql LOOP
    --不选择通过表left join的方式来获取customer_no和card_no是因为有以下情况存在：一张卡已经销户，且该卡做过换卡。因为换过卡，则t_hxsj_card_account中存在2条卡号账号对应记录，但是这两条的valid_flag都为'1'
    --leftjoin连接时，如果设置valid_flag='0'则找不到卡号，导致找不到客户号；如果不设置valid_flag的连接条件，那么记录会产生2条，导致重复。
    SELECT get_card_no_by_account_no(r.account_no) INTO card_no_result;
    SELECT get_customer_no_by_account_no(r.account_no) INTO customer_no_result;

    --定期存款积分
    IF r.dem_regl_flag = '2' AND (customer_no_result is not null AND customer_no_result != '''') THEN
      SELECT get_customer_points_record_value(dqckrj_id,arg_start_date,arg_end_date) INTO record_value;
      record_value := record_value * floor(r.balance/5000);
      INSERT INTO t_khjf_customer_points_record (customer_no, account_no, child_account_no, card_no, balance, subject_no, org_code, date, month, year, start_date, end_date, points_type_id, value, valid_flag, teller_code, create_by) VALUES (customer_no_result,r.account_no,r.child_account_no,card_no_result,r.balance,r.subject_no,r.org_code,null,arg_month,arg_year,arg_start_date,arg_end_date,dqckrj_id,record_value,'0','888888',1);
    END IF;
  END LOOP;

  RETURN TRUE ;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_compute_dqckrj:执行错误, 错位信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;

--计算存款活期日均
DROP FUNCTION IF EXISTS khjf_compute_hqckrj(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR);
CREATE OR REPLACE FUNCTION khjf_compute_hqckrj(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR) RETURNS BOOLEAN AS $$
DECLARE
  sql VARCHAR;
  r RECORD;
  days INTEGER;
  curr_date DATE;
  count_num INTEGER;
  hqckrj_id BIGINT;
  arg_start_date DATE;
  arg_end_date DATE;
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
  record_value NUMERIC(10,2);
BEGIN
  arg_start_date := start_date;
  arg_end_date := end_date;

  --计算相差天数
  SELECT end_date::date - start_date::date INTO days;
  days := days + 1;
  IF days <= 0 THEN
    RAISE NOTICE '终止日期小于起始日期';
    RETURN FALSE ;
  END IF;

  --判断t_hxsj_deposit_account_detail表是否导入成功（特别注意跨年的时候，更改了该表表名）
  curr_date := start_date;
  FOR i IN 1..days LOOP
    SELECT count(id) FROM t_hxsj_deposit_account_detail WHERE date=curr_date::date INTO count_num;
    IF count_num <= 100000 THEN
      RAISE NOTICE '%日t_hxsj_deposit_account_detail表不足100000条记录，计算存款日均终止',curr_date;
      RETURN FALSE ;
    END IF;
    curr_date := curr_date + INTERVAL '1 day';
  END LOOP;

  --分别获取活期存款日均、定期存款日均points_type_id
  SELECT id FROM t_khjf_type WHERE short_name = 'HQCKRJ' AND del_flag = FALSE INTO hqckrj_id;
  IF hqckrj_id ISNULL THEN
    RAISE NOTICE '活期存款日均的积分类型不存在，请检查客户积分类型表';
    RETURN FALSE ;
  END IF;

  --删除已经存在的记录
  DELETE FROM t_khjf_customer_points_record WHERE month = arg_month AND year = arg_year AND points_type_id = hqckrj_id;

  --创建临时表
  DROP TABLE IF EXISTS tmp_account_no;
  CREATE TEMP TABLE tmp_account_no(account_no VARCHAR(22), child_account_no VARCHAR(6), subject_no VARCHAR(10), org_code VARCHAR(10), dem_regl_flag VARCHAR(1), balance NUMERIC(17,2));

  --将存款日均存入临时表
  INSERT INTO tmp_account_no SELECT account_no, child_account_no, subject_no, org_code, dem_regl_flag, round(sum(before_day_balance)/days,2) FROM t_hxsj_deposit_account_detail WHERE date>=arg_start_date::date AND date<= arg_end_date::date AND before_day_balance>0 AND (subject_no LIKE '2011%' OR subject_no LIKE '2012%' OR subject_no LIKE '2013%' OR subject_no LIKE '2014%' OR subject_no LIKE '2015%' OR subject_no LIKE '2016%' OR subject_no LIKE '2017%' OR subject_no LIKE '2051%' OR subject_no LIKE '2052%' OR subject_no LIKE '2111%' OR subject_no LIKE '2151%' OR subject_no LIKE '2511%') AND account_type = '1' GROUP BY account_no,child_account_no,subject_no,org_code,dem_regl_flag HAVING sum(before_day_balance)/days>=1000;

  --遍历临时表
  sql := 'SELECT a.dem_regl_flag,a.account_no,a.child_account_no,a.subject_no,a.org_code,a.balance FROM tmp_account_no a';
  FOR r IN EXECUTE sql LOOP
    --不选择通过表left join的方式来获取customer_no和card_no是因为有以下情况存在：一张卡已经销户，且该卡做过换卡。因为换过卡，则t_hxsj_card_account中存在2条卡号账号对应记录，但是这两条的valid_flag都为'1'
    --leftjoin连接时，如果设置valid_flag='0'则找不到卡号，导致找不到客户号；如果不设置valid_flag的连接条件，那么记录会产生2条，导致重复。
    SELECT get_card_no_by_account_no(r.account_no) INTO card_no_result;
    SELECT get_customer_no_by_account_no(r.account_no) INTO customer_no_result;
    -- 活期存款积分
    IF r.dem_regl_flag = '1' AND (customer_no_result is not null AND customer_no_result != '''') THEN
      SELECT get_customer_points_record_value(hqckrj_id,arg_start_date,arg_end_date) INTO record_value;
      record_value := record_value * floor(r.balance/1000);
      INSERT INTO t_khjf_customer_points_record (customer_no, account_no, child_account_no, card_no, balance, subject_no, org_code, date, month, year, start_date, end_date, points_type_id, value, valid_flag, teller_code, create_by) VALUES (customer_no_result,r.account_no,r.child_account_no,card_no_result,r.balance,r.subject_no,r.org_code,null,arg_month,arg_year,arg_start_date,arg_end_date,hqckrj_id,record_value,'0','888888',1);
    END IF;
  END LOOP;

  RETURN TRUE ;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_compute_hqckrj:执行错误, 错位信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;

--计算微信付款
DROP FUNCTION IF EXISTS khjf_compute_wxfk(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR);
CREATE OR REPLACE FUNCTION khjf_compute_wxfk(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR) RETURNS BOOLEAN AS $$
DECLARE
  days INTEGER;
  curr_date DATE;
  check_result BOOLEAN;
  wxfk_id BIGINT;
  sql VARCHAR;
  r RECORD;
  arg_start_date DATE;
  arg_end_date DATE;
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
  record_value NUMERIC(10,2);
BEGIN
  arg_start_date := start_date;
  arg_end_date := end_date;

  --计算相差天数
  SELECT end_date::date - start_date::date INTO days;
  days := days + 1;
  IF days <= 0 THEN
    RAISE NOTICE '终止日期小于起始日期';
    RETURN FALSE ;
  END IF;

  --检测dpfm30表是否正常
  curr_date := start_date;
  FOR i IN 1..days LOOP
    SELECT hxsj_check_deposit_detail(curr_date) INTO check_result;
    IF check_result = FALSE THEN
      RAISE NOTICE '%日dpfm30表不正确，请检查',curr_date;
      RETURN FALSE ;
    END IF;
    curr_date := curr_date + INTERVAL '1 day';
  END LOOP;

  --分别获取活期存款日均、定期存款日均points_type_id
  SELECT id FROM t_khjf_type WHERE short_name = 'WXFK' AND del_flag = FALSE INTO wxfk_id;
  IF wxfk_id ISNULL OR wxfk_id ISNULL THEN
    RAISE NOTICE '微信付款类型不存在，请检查客户积分类型表';
    RETURN FALSE ;
  END IF;

  --删除已经存在的记录
  DELETE FROM t_khjf_customer_points_record WHERE month = arg_month AND year = arg_year AND date = arg_start_date AND points_type_id = wxfk_id;

  --在dpfm30表中查找微信支付记录，遍历该记录，将记录插入t_khjf_customer_record表中
  sql := 'SELECT a.account_no,a.child_account_no,a.tx_date,a.tx_amt as balance,a.open_acct_org_code as org_code FROM t_hxsj_deposit_detail a WHERE a.summary_code =''NX0'' AND a.summary= ''网上银行协议付款'' AND a.tx_src = ''MP'' AND a.orig_tx_code = ''PYS040'' AND a.deb_cred_flag = ''D'' AND a.customer_type=''1'' AND a.tx_date>= $1 and a.tx_date<= $2';
  FOR r IN EXECUTE sql USING arg_start_date,arg_end_date LOOP
    SELECT get_card_no_by_account_no(r.account_no) INTO card_no_result;
    SELECT get_customer_no_by_account_no(r.account_no) INTO customer_no_result;
    SELECT get_customer_points_record_value(wxfk_id,arg_start_date,arg_end_date) INTO record_value;
    IF r.balance <= 100 THEN record_value := 0; END IF;
    INSERT INTO t_khjf_customer_points_record (customer_no, account_no, child_account_no, card_no, balance, subject_no, org_code, date, month, year, start_date, end_date, points_type_id, value, valid_flag, teller_code, create_by) VALUES (customer_no_result,r.account_no,r.child_account_no,card_no_result,r.balance,null,r.org_code,r.tx_date,arg_month,arg_year,arg_start_date,arg_end_date,wxfk_id,record_value,'0','888888',1);
  END LOOP;

  RETURN TRUE ;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_compute_wxfk:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;

--计算POS收单
DROP FUNCTION IF EXISTS khjf_compute_pos(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR);
CREATE OR REPLACE FUNCTION khjf_compute_pos(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR) RETURNS BOOLEAN AS $$
DECLARE
  days INTEGER;
  curr_date DATE;
  check_result BOOLEAN;
  pos_id BIGINT;
  sql VARCHAR;
  r RECORD;
  arg_start_date DATE;
  arg_end_date DATE;
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
  record_value NUMERIC(10,2);
BEGIN
  arg_start_date := start_date;
  arg_end_date := end_date;

  --计算相差天数
  SELECT end_date::date - start_date::date INTO days;
  days := days + 1;
  IF days <= 0 THEN
    RAISE NOTICE '终止日期小于起始日期';
    RETURN FALSE ;
  END IF;

  --检测dpfm30表是否正常
  curr_date := start_date;
  FOR i IN 1..days LOOP
    SELECT hxsj_check_deposit_detail(curr_date) INTO check_result;
    IF check_result = FALSE THEN
      RAISE NOTICE '%日dpfm30表不正确，请检查',curr_date;
      RETURN FALSE ;
    END IF;
    curr_date := curr_date + INTERVAL '1 day';
  END LOOP;

  --获取POS消费的points_type_id
  SELECT id FROM t_khjf_type WHERE short_name = 'POS' AND del_flag = FALSE INTO pos_id;
  IF pos_id ISNULL OR pos_id ISNULL THEN
    RAISE NOTICE 'POS消费类型不存在，请检查客户积分类型表';
    RETURN FALSE ;
  END IF;

  --删除已经存在的记录
  DELETE FROM t_khjf_customer_points_record WHERE month = arg_month AND year = arg_year AND date = arg_start_date AND points_type_id = pos_id;

  --在dpfm30表中查找POS支付记录，遍历该记录，将记录插入t_khjf_customer_record表中
  sql := 'SELECT a.account_no,a.child_account_no,a.tx_date,a.tx_amt as balance,a.open_acct_org_code as org_code FROM t_hxsj_deposit_detail a WHERE a.summary_code =''E09'' AND a.summary= ''POS消费'' AND a.tx_src = ''UP'' AND a.orig_tx_code = ''UPS005'' AND a.deb_cred_flag = ''D'' AND customer_type=''1'' AND a.tx_date>=$1 and a.tx_date<=$2';
  FOR r IN EXECUTE sql USING arg_start_date,arg_end_date LOOP
    SELECT get_card_no_by_account_no(r.account_no) INTO card_no_result;
    SELECT get_customer_no_by_account_no(r.account_no) INTO customer_no_result;
    SELECT get_customer_points_record_value(pos_id,arg_start_date,arg_end_date) INTO record_value;
    IF r.balance <=50 THEN record_value := 0; END IF;
    INSERT INTO t_khjf_customer_points_record (customer_no, account_no, child_account_no, card_no, balance, subject_no, org_code, date, month, year, start_date, end_date, points_type_id, value, valid_flag, teller_code, create_by) VALUES (customer_no_result,r.account_no,r.child_account_no,card_no_result,r.balance,null,r.org_code,r.tx_date,arg_month,arg_year,arg_start_date,arg_end_date,pos_id,record_value,'0','888888',1);
  END LOOP;

  RETURN TRUE ;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_compute_pos:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;

--计算短信通
DROP FUNCTION IF EXISTS khjf_compute_dxt(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR);
CREATE OR REPLACE FUNCTION khjf_compute_dxt(start_date date, end_date date, arg_month VARCHAR, arg_year VARCHAR) RETURNS BOOLEAN AS $$
DECLARE
  days INTEGER;
  curr_date DATE;
  check_result BOOLEAN;
  dxt_id BIGINT;
  sql VARCHAR;
  r RECORD;
  arg_start_date DATE;
  arg_end_date DATE;
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
  account_no_length INTEGER;
  account_no_result VARCHAR;
  child_account_no_result VARCHAR;
  flag BOOLEAN;
  record_value NUMERIC(10,2);
BEGIN
  arg_start_date := start_date;
  arg_end_date := end_date;

  --计算相差天数
  SELECT end_date::date - start_date::date INTO days;
  days := days + 1;
  IF days <= 0 THEN
    RAISE NOTICE '终止日期小于起始日期';
    RETURN FALSE ;
  END IF;

  --检测dpfm30表是否正常
  curr_date := start_date;
  FOR i IN 1..days LOOP
    SELECT hxsj_check_deposit_detail(curr_date) INTO check_result;
    IF check_result = FALSE THEN
      RAISE NOTICE '%日dpfm30表不正确，请检查',curr_date;
      RETURN FALSE ;
    END IF;
    curr_date := curr_date + INTERVAL '1 day';
  END LOOP;

  --分别获取活期存款日均、定期存款日均points_type_id
  SELECT id FROM t_khjf_type WHERE short_name = 'DXT' AND del_flag = FALSE INTO dxt_id;
  IF dxt_id ISNULL OR dxt_id ISNULL THEN
    RAISE NOTICE 'POS消费类型不存在，请检查客户积分类型表';
    RETURN FALSE ;
  END IF;

  --删除已经存在的记录
  DELETE FROM t_khjf_customer_points_record WHERE month = arg_month AND year = arg_year AND date = arg_start_date AND points_type_id = dxt_id;

  --在dpfm30表中查找短信通支付记录，遍历该记录，将记录插入t_khjf_customer_record表中
  sql := 'select other_side_acct_no as account_no,tx_date,tx_amt as balance,tx_org_code as org_code from t_hxsj_deposit_detail where summary_code =''SM1'' and account_no like ''780__01511102040000003'' and summary=''短信包月费批量扣收（个人对公）'' and tx_src=''BT'' and tx_amt=2 and tx_date>=$1 and tx_date<=$2';
  FOR r IN EXECUTE sql USING arg_start_date,arg_end_date LOOP
    flag := FALSE ;
    SELECT char_length(r.account_no) INTO account_no_length;
    --卡开通短信通
    IF account_no_length = 19 THEN
      SELECT a.account_no FROM t_hxsj_card_account a LEFT JOIN t_hxsj_deposit_account b ON a.account_no = b.account_no WHERE a.card_no = r.account_no AND b.org_code LIKE '780%' INTO account_no_result;
      IF account_no_result IS NOT NULL AND account_no_result != '' THEN
        flag := TRUE ;
        SELECT customer_no FROM t_hxsj_customer_account WHERE account_no = r.account_no INTO customer_no_result;
        child_account_no_result := '000000';
        card_no_result := r.account_no;
      END IF;
    END IF;

    --存折开通短信通
    IF account_no_length = 22 THEN
      SELECT account_no FROM t_hxsj_deposit_account WHERE account_no = r.account_no AND org_code LIKE '780%' INTO account_no_result;
      IF account_no_result IS NOT NULL AND account_no_result != '' THEN
        flag := TRUE ;
        SELECT customer_no FROM t_hxsj_customer_account WHERE account_no = r.account_no INTO customer_no_result;
        child_account_no_result := NULL;
        card_no_result := NULL ;
      END IF;
    END IF;

    --他行卡或账户在我行开通短信通，不为该客户建立积分，因为找不到该客户的客户号
    IF flag = TRUE THEN
      SELECT get_customer_points_record_value(dxt_id,arg_start_date,arg_end_date) INTO record_value;
      INSERT INTO t_khjf_customer_points_record (customer_no, account_no, child_account_no, card_no, balance, subject_no, org_code, date, month, year, start_date, end_date, points_type_id, value, valid_flag, teller_code, create_by) VALUES (customer_no_result,account_no_result,child_account_no_result,card_no_result,r.balance,null,r.org_code,r.tx_date,arg_month,arg_year,arg_start_date,arg_end_date,dxt_id,record_value,'0','888888',1);
    END IF;
  END LOOP;

  RETURN TRUE ;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_compute_dxt:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;


--通过账号获取卡号：如果该账号的所有卡号均已销户，则返回最后一个使用的卡号
DROP FUNCTION IF EXISTS get_card_no_by_account_no(para_account_no VARCHAR);
CREATE OR REPLACE FUNCTION get_card_no_by_account_no(para_account_no VARCHAR) RETURNS VARCHAR AS $$
DECLARE
  cards_count INTEGER;
  card_no_result VARCHAR;
BEGIN

  card_no_result := NULL ;
  SELECT count(DISTINCT card_no) FROM t_hxsj_card_account WHERE account_no = para_account_no INTO cards_count;
  IF cards_count = 0 THEN
    RETURN null;
  ELSEIF cards_count = 1 THEN
    SELECT card_no FROM t_hxsj_card_account WHERE account_no = para_account_no INTO card_no_result;
    RETURN card_no_result;
  ELSEIF cards_count >=2 THEN
    SELECT card_no FROM t_hxsj_card_account WHERE account_no = para_account_no AND valid_flag='0' INTO card_no_result;
    IF card_no_result IS NOT NULL THEN
      RETURN card_no_result;
      ELSE
        SELECT card_no FROM t_hxsj_card_account WHERE account_no = para_account_no ORDER BY id DESC LIMIT 1 INTO card_no_result;
        RETURN card_no_result;
    END IF;
  END IF;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'get_card_no_by_account_no:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;

--通过账号获取客户号
DROP FUNCTION IF EXISTS get_customer_no_by_account_no(para_account_no VARCHAR);
CREATE OR REPLACE FUNCTION get_customer_no_by_account_no(para_account_no VARCHAR) RETURNS VARCHAR AS $$
DECLARE
  card_no_result VARCHAR;
  customer_no_result VARCHAR;
BEGIN

  SELECT get_card_no_by_account_no(para_account_no) INTO card_no_result;
  IF card_no_result IS NULL THEN
    SELECT customer_no FROM t_hxsj_customer_account WHERE account_no = para_account_no INTO customer_no_result;
  ELSE
    SELECT customer_no FROM t_hxsj_customer_account WHERE account_no = card_no_result INTO customer_no_result;
  END IF;

  RETURN customer_no_result;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'get_customer_no_by_account_no:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;


--通过pointsTypeId和date获取value值，即获取积分类型的当前积分值
DROP FUNCTION IF EXISTS get_customer_points_record_value(para_points_type_id BIGINT,para_start_date DATE,para_end_date DATE);
CREATE OR REPLACE FUNCTION get_customer_points_record_value(para_points_type_id BIGINT,para_start_date DATE,para_end_date DATE) RETURNS NUMERIC(10,2) AS $$
DECLARE
  value_result NUMERIC(10,2);
  sql VARCHAR;
  r RECORD;
BEGIN
  IF para_start_date::date > para_end_date::date THEN
    RAISE EXCEPTION '起始日期大于终止日期';
    RETURN 0;
  END IF;

  sql := 'select start_date,end_date,value from t_khjf_points_type_change where points_type_id=$1 and del_flag=false';

  value_result := 0 ;
  FOR r IN EXECUTE sql USING para_points_type_id LOOP
    IF r.start_date ISNULL AND r.end_date ISNULL THEN
      value_result := r.value;EXIT;
    END IF;
    IF r.start_date ISNULL AND para_end_date::date <= r.end_date::date THEN
      value_result := r.value;EXIT;
    END IF;
    IF r.start_date::date <= para_end_date::date AND r.end_date ISNULL THEN
      value_result := r.value;EXIT;
    END IF;
    IF r.start_date::date <= para_end_date::date AND r.end_date::date >= para_end_date::date THEN
      value_result := r.value;EXIT;
    END IF;
  END LOOP;

  RETURN value_result;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'get_customer_points_record_value:执行错误, 错误信息为：%',SQLERRM;
    RETURN 0 ;
END;
$$ LANGUAGE plpgsql;

--获取积分类型的当前停用启用状态
DROP FUNCTION IF EXISTS get_customer_points_record_available(para_points_type_id BIGINT,para_start_date DATE,para_end_date DATE);
CREATE OR REPLACE FUNCTION get_customer_points_record_available(para_points_type_id BIGINT,para_start_date DATE,para_end_date DATE) RETURNS VARCHAR AS $$
DECLARE
  value_result VARCHAR(1);
  sql VARCHAR;
  r RECORD;
BEGIN
  IF para_start_date::date > para_end_date::date THEN
    RAISE EXCEPTION '起始日期大于终止日期';
    RETURN '1';
  END IF;

  sql := 'select start_date,end_date,available_status from t_khjf_points_type_available where points_type_id=$1 and del_flag=false';

  value_result := '1' ;  --默认返回停用，即t_khjf_points_type_available表里的时间段没有该类型时，返回'1'：停用
  FOR r IN EXECUTE sql USING para_points_type_id LOOP
    IF r.start_date ISNULL AND r.end_date ISNULL THEN
      value_result := r.available_status;EXIT;
    END IF;
    IF r.start_date ISNULL AND para_end_date::date <= r.end_date::date THEN
      value_result := r.available_status;EXIT;
    END IF;
    IF r.start_date::date <= para_end_date::date AND r.end_date ISNULL THEN
      value_result := r.available_status;EXIT;
    END IF;
    IF r.start_date::date <= para_end_date::date AND r.end_date::date >= para_end_date::date THEN
      value_result := r.available_status;EXIT;
    END IF;
  END LOOP;

  RETURN value_result;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'get_customer_points_record_available:执行错误, 错误信息为：%',SQLERRM;
    RETURN '1' ;
END;
$$ LANGUAGE plpgsql;

--清理积分
DROP FUNCTION IF EXISTS khjf_clear_points(para_type VARCHAR, para_org_code VARCHAR, para_identity_no VARCHAR, para_customer_points_record_id BIGINT, para_create_by_id BIGINT);
CREATE OR REPLACE FUNCTION khjf_clear_points(para_type VARCHAR, para_org_code VARCHAR, para_identity_no VARCHAR, para_customer_points_record_id BIGINT, para_create_by_id BIGINT) RETURNS BOOLEAN AS $$
DECLARE
  sql VARCHAR;
  r RECORD;
  temp_id BIGINT;
BEGIN
  IF para_type ISNULL OR '' = para_type THEN
    RAISE EXCEPTION '清理类型为空';
    RETURN FALSE ;
  END IF;

  --1、单条清理
  IF para_type = '2' THEN
    IF para_customer_points_record_id ISNULL THEN
      RAISE EXCEPTION '清理单条积分时未提供积分记录的id';
      RETURN FALSE ;
    END IF;
    --判断该条记录的原始状态是否为正常
    SELECT id FROM t_khjf_customer_points_record WHERE id = para_customer_points_record_id AND del_flag = FALSE AND valid_flag = '0' INTO temp_id;
    --如果正常，则将该记录的状态修改为‘2’，即单户清理；同时，在t_khjf_clear_points中写入清理记录
    IF temp_id IS NOT NULL THEN
      UPDATE t_khjf_customer_points_record SET valid_flag = '2' WHERE id = para_customer_points_record_id;
      INSERT INTO t_khjf_clear_points(customer_points_record_id,org_code,identity_no,type,remarks,create_by,create_time) VALUES(para_customer_points_record_id,para_org_code,para_identity_no,para_type,null,para_create_by_id,now());
    END IF;
  END IF;

  --2、单户清理
  IF para_type = '3' THEN
    IF para_identity_no ISNULL OR para_identity_no = '' THEN
      RAISE EXCEPTION '清理单户积分时未提供客户的证件号码';
      RETURN FALSE ;
    END IF;

    IF para_org_code IS NULL OR para_org_code = '' THEN
      --机构号为空或null时，清理该客户在全部机构的积分
      sql := 'select a.id from t_khjf_customer_points_record a left join t_hxsj_customer_public_base c on a.customer_no = c.no where c.identity_no = $1 and a.del_flag=false and a.valid_flag = ''0''';
      FOR r IN EXECUTE sql USING para_identity_no LOOP
        UPDATE t_khjf_customer_points_record SET valid_flag = '3' WHERE id = r.id;
        INSERT INTO t_khjf_clear_points(customer_points_record_id,org_code,identity_no,type,remarks,create_by,create_time) VALUES(r.id,para_org_code,para_identity_no,para_type,null,para_create_by_id,now());
      END LOOP;
      --相应清理积分兑换表t_khjf_consume_points中的记录
      UPDATE t_khjf_consume_points SET valid_flag = '3' WHERE identity_no = para_identity_no AND valid_flag = '0' AND del_flag = FALSE;
      --机构号不为空时，清理该客户在指定机构的积分
    ELSE
        sql := 'select a.id from t_khjf_customer_points_record a left join t_hxsj_customer_public_base c on a.customer_no = c.no where c.identity_no = $1 and a.del_flag=false and a.valid_flag = ''0'' and a.org_code = $2';
        FOR r IN EXECUTE sql USING para_identity_no,para_org_code LOOP
          UPDATE t_khjf_customer_points_record SET valid_flag = '3' WHERE id = r.id;
          INSERT INTO t_khjf_clear_points(customer_points_record_id,org_code,identity_no,type,remarks,create_by,create_time) VALUES(r.id,para_org_code,para_identity_no,para_type,null,para_create_by_id,now());
        END LOOP;
      --相应清理积分兑换表t_khjf_consume_points中的记录
      UPDATE t_khjf_consume_points SET valid_flag = '3' WHERE identity_no = para_identity_no AND valid_flag = '0' AND del_flag = FALSE AND org_code = para_org_code;
    END IF;
  END IF;

  --3、批量清理
  IF para_type = '4' THEN
    IF para_org_code IS NULL OR para_org_code = '' THEN
      sql := 'select id from t_khjf_customer_points_record where del_flag = false and valid_flag = ''0''';
      FOR r IN EXECUTE sql LOOP
        UPDATE t_khjf_customer_points_record SET valid_flag = '4' WHERE id = r.id;
        INSERT INTO t_khjf_clear_points(customer_points_record_id,org_code,identity_no,type,remarks,create_by,create_time) VALUES(r.id,para_org_code,para_identity_no,para_type,null,para_create_by_id,now());
      END LOOP;
      --相应清理积分兑换表t_khjf_consume_points中的记录
      UPDATE t_khjf_consume_points SET valid_flag = '4' WHERE identity_no = para_identity_no AND valid_flag = '0' AND del_flag = FALSE;
    ELSE
      sql := 'select id from t_khjf_customer_points_record where del_flag = false and valid_flag = ''0'' and org_code = $1';
      FOR r IN EXECUTE sql USING para_org_code LOOP
        UPDATE t_khjf_customer_points_record SET valid_flag = '4' WHERE id = r.id;
        INSERT INTO t_khjf_clear_points(customer_points_record_id,org_code,identity_no,type,remarks,create_by,create_time) VALUES(r.id,para_org_code,para_identity_no,para_type,null,para_create_by_id,now());
      END LOOP;
      --相应清理积分兑换表t_khjf_consume_points中的记录
      UPDATE t_khjf_consume_points SET valid_flag = '4' WHERE identity_no = para_identity_no AND valid_flag = '0' AND del_flag = FALSE AND org_code = para_org_code;
    END IF;
  END IF;

  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'khjf_clear_points:执行错误, 错误信息为：%',SQLERRM;
    RETURN FALSE ;
END;
$$ LANGUAGE plpgsql;


