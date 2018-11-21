package com.duobi.manager.khjf.service;

import com.duobi.manager.khjf.dao.CustomerPointsRecordDao;
import com.duobi.manager.khjf.entity.ClearPoints;
import com.duobi.manager.khjf.entity.CustomerPointsRecord;
import com.duobi.manager.khjf.entity.KhjfParameter;
import com.duobi.manager.sys.entity.UserOrganization;
import com.duobi.manager.sys.utils.*;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class CustomerPoinsRecordService extends CrudService<CustomerPointsRecordDao,CustomerPointsRecord,Long> {

    public Page<CustomerPointsRecord> findPage(Page<CustomerPointsRecord> page, CustomerPointsRecord customerPointsRecord){

        customerPointsRecord.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(Global.getKhjfModuleId(), customerPointsRecord.getCurrentUserOrganization(), "o", null));

        page.setAutoPage(false);
        customerPointsRecord.setPage(page);
        page.setCount(dao.findCount(customerPointsRecord));
        if (page.getCount() > 0) {
            page.setList(dao.findList(customerPointsRecord));
        }
        return page;
    }

    /**
     * 清理积分
     * @param clearPoints orgCode为null时默认为全部网点
     */
    public void clearPoints(ClearPoints clearPoints) throws Exception{

        if (clearPoints == null || clearPoints.getType() == null) throw new Exception("未提供清理的积分类型");

        //单户清理、批量清理时判断是否允许跨机构清理
        String orgCode = UserUtils.getUserOrganization().getOrganizationCode();
        Boolean clearAllOrgPointsFlag = getClearAllOrgPointsFlag();

        if (ClearPoints.CLEAR_POINTS_TYPE_MANUAL_SINGLE_RECORD.equals(clearPoints.getType())){
            if (clearPoints.getCustomerPointsRecordId() == null) throw new Exception("清理单条记录时未提供积分记录的id");
        }
        if (ClearPoints.CLEAR_POINTS_TYPE_MANUAL_SINGLE_CUSTOMER.equals(clearPoints.getType())){
            if (clearPoints.getIdentityNo() == null) throw new Exception("单户清理积分时未提供客户证件号码");
            //非超级管理员清理积分时，判断当前用户所在机构与清理积分的机构是否相符
            if (!UserUtils.getUserOrganization().getUser().isAdmin()){
                if (!clearAllOrgPointsFlag && !orgCode.equals(clearPoints.getOrgCode())) throw new Exception("不允许跨网点清理积分");
            }
        }
        if (ClearPoints.CLEAR_POINTS_TYPE_MANUAL_BATCH.equals(clearPoints.getType())){
            //非超级管理员清理积分时，判断当前用户所在机构与清理积分的机构是否相符
            if (!UserUtils.getUserOrganization().getUser().isAdmin()){
                if (!clearAllOrgPointsFlag && !orgCode.equals(clearPoints.getOrgCode())) throw new Exception("不允许跨网点清理积分");
            }
        }

        clearPoints.preInsert();
        Boolean result = dao.clearPoints(clearPoints);

        if (!result) throw new Exception("清理积分时错误，请联系管理员");
    }

    /**
     * 获取当前是否允许跨网点清理积分的标志
     * @return boolean
     */
    public Boolean getClearAllOrgPointsFlag() throws Exception{
        KhjfParameter khjfParameter = new KhjfParameter();
        khjfParameter.setName("clearAllOrgPointsFlag");
        KhjfParameter result = dao.getClearAllOrgPointsFlag(khjfParameter);

        if (result == null || result.getValue() == null) throw new Exception("客户积分参数设置中不存在以下有效属性：是否允许跨网点清理积分标志(clearAllOrgPoints)");
        if (result.getValue().equals("true")) return true;
        else if (result.getValue().equals("false")) return false;
        else throw new Exception("返回非法值，请检查客户积分参数表中clearAllOrgPoints的value值");

    }

}
