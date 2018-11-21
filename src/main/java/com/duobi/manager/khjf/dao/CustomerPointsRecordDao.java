package com.duobi.manager.khjf.dao;

import com.duobi.manager.khjf.entity.ClearPoints;
import com.duobi.manager.khjf.entity.CustomerPointsRecord;
import com.duobi.manager.khjf.entity.KhjfParameter;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.crud.CrudDao;

public interface CustomerPointsRecordDao extends CrudDao<CustomerPointsRecord,Long> {

    Boolean clearPoints(ClearPoints clearPoints);

    KhjfParameter getClearAllOrgPointsFlag(KhjfParameter khjfParameter);
}
