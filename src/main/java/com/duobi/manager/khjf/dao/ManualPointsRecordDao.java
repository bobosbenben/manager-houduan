package com.duobi.manager.khjf.dao;

import com.duobi.manager.khjf.entity.ManualPointsRecord;
import com.duobi.manager.sys.utils.crud.CrudDao;

public interface ManualPointsRecordDao extends CrudDao<ManualPointsRecord,Long> {

    public String getAccountNoByCardNo(String cardNo);

    public String getCardNoByAccountNo(String accountNo);

    public String getCustomerNoByCardNoOrAccountNo(String cardNoOrAccountNo);

    public String getOrgCodeByAccountNo(String accountNo);

    public Double getPointsTypeValueByPointsTypeId(ManualPointsRecord manualPointsRecord);

    String getPointsTypeTypeById(Long pointsTypeId);

    String getAvailableStatus(ManualPointsRecord manualPointsRecord);

    void auditManualPointsRecord(ManualPointsRecord manualPointsRecord) throws Exception;

    void denyManualPointsRecord(ManualPointsRecord manualPointsRecord) throws Exception;

    void insertToCustomerPointsRecord(ManualPointsRecord manualPointsRecord) throws Exception;
}
