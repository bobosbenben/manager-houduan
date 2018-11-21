package com.duobi.manager.khjf.dao;

import com.duobi.manager.khjf.entity.ConsumePoints;
import com.duobi.manager.khjf.entity.CustomerPointsRecord;
import com.duobi.manager.khjf.entity.KhjfParameter;
import com.duobi.manager.sys.utils.crud.CrudDao;

public interface ConsumePointsDao extends CrudDao<ConsumePoints,Long> {

    KhjfParameter getKhjfGlobalParameter(KhjfParameter khjfParameter);

    void updateValidFlagOfKhjfGlobalParameter(KhjfParameter khjfParameter);

    void insertKhjfGlobalParameter(KhjfParameter khjfParameter);

    ConsumePoints getCustomerConsumePointsInGivenOrg(CustomerPointsRecord customerPointsRecord);

    ConsumePoints getCustomerConsumePointsInAllOrg(CustomerPointsRecord customerPointsRecord);

    String getCustomerNameByIdentityNo(String identityNo);

    String getCustomerNoByIdentityNo(String identityNo);

    ConsumePoints getCustomerAlreadyConsumedPointsInGivenOrg(CustomerPointsRecord customerPointsRecord);

    ConsumePoints getCustomerAlreadyConsumedPointsInAllOrg(CustomerPointsRecord customerPointsRecord);

}
