package com.duobi.manager.khjf.service;

import com.duobi.manager.khjf.dao.ConsumePointsDao;
import com.duobi.manager.khjf.entity.ConsumePoints;
import com.duobi.manager.khjf.entity.CustomerPointsRecord;
import com.duobi.manager.khjf.entity.KhjfParameter;
import com.duobi.manager.sys.utils.*;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class ConsumePointsService extends CrudService<ConsumePointsDao,ConsumePoints,Long> {

    public Page<ConsumePoints> findPage(Page<ConsumePoints> page, ConsumePoints consumePoints){

        consumePoints.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(Global.getKhjfModuleId(), consumePoints.getCurrentUserOrganization(), "o",null));

        page.setAutoPage(false);
        consumePoints.setPage(page);
        page.setCount(dao.findCount(consumePoints));
        if (page.getCount() > 0) {
            page.setList(dao.findList(consumePoints));
        }
        return page;
    }

    /**
     * 执行积分兑换操作
     * @param consumePoints 前端参数
     * @throws Exception
     */
    public ConsumePoints executeConsumePoints(ConsumePoints consumePoints) throws Exception{

        //判断是否提供了证件号码、兑换值
        if (consumePoints == null||consumePoints.getIdentityNo()==null||consumePoints.getPointsValue()==null) throw new Exception("兑换积分时未提供证件号或兑换值");
        //兑换积分为0时不予积分
        Double pointsValue = consumePoints.getPointsValue();
        if (pointsValue<=0) throw new Exception("要兑换的积分值不正确，请重新输入");
        //判断客户信息是否正确
        String identityNo = consumePoints.getIdentityNo();
        String customerName = getCustomerNameByIdentityNo(identityNo);
        String customerNo = getCustomerNoByIdentityNo(identityNo);
        if (StringUtils.isBlank(customerName) || StringUtils.isBlank(customerNo)) throw new Exception("客户信息不存在，请检查证件号码");

        //获取当前是否允许通兑积分的标志
        Boolean consumeAllOrgPointsFlag = getConsumeAllOrgPointsFlag();

        //获取客户在所有网点的总积分
        Double allOrgPoints = 0D;
        ConsumePoints resultConsumePoints = getCustomerConsumePointsInAllOrgByIdentityNo(identityNo);
        if (resultConsumePoints != null && resultConsumePoints.getAllOrgPoints() != null) allOrgPoints = resultConsumePoints.getAllOrgPoints();

        //获取客户在当前网点的总积分
        String orgCode = UserUtils.getUserOrganization().getOrganizationCode();
        Double currentOrgPoints = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerConsumePointsInGivenOrgByIdentityNoAndOrgCode(identityNo,orgCode);
        if (resultConsumePoints != null) currentOrgPoints = resultConsumePoints.getCurrentOrgPoints();

        //获取客户在全部网点已兑换的积分
        Double customerAlreadyConsumedPointsInAllOrg = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerAlreadyConsumedPointsInAllOrgByIdentityNo(identityNo);
        if (resultConsumePoints != null) customerAlreadyConsumedPointsInAllOrg = resultConsumePoints.getPointsValue();

        //获取客户在当前网点已兑换的积分
        Double customerAlreadyConsumedPointsInGivenOrg = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerAlreadyConsumedPointsInGivenOrgByIdentityNoAndOrgCode(identityNo,orgCode);
        if (resultConsumePoints != null) customerAlreadyConsumedPointsInGivenOrg = resultConsumePoints.getPointsValue();

        //判断在所有网点的可用积分是否足够兑换
        if (allOrgPoints - customerAlreadyConsumedPointsInAllOrg - pointsValue < 0) throw new Exception("客户总积分不足，无法兑换");

        //如果不允许通兑，判断在当前网点积分是否足够兑换
        if (!consumeAllOrgPointsFlag && currentOrgPoints - customerAlreadyConsumedPointsInGivenOrg - pointsValue < 0) throw new Exception("不支持通兑积分，客户在当前网点积分不足，无法兑换");

        ConsumePoints returnConsumePoints = new ConsumePoints();
        returnConsumePoints.setIdentityNo(identityNo);
        returnConsumePoints.setAllOrgPoints(allOrgPoints-customerAlreadyConsumedPointsInAllOrg);
        returnConsumePoints.setCurrentOrgPoints(currentOrgPoints - customerAlreadyConsumedPointsInGivenOrg);
        returnConsumePoints.setCustomerName(customerName);
        returnConsumePoints.setPointsValue(pointsValue);
        returnConsumePoints.setConsumeAllOrgPoints(consumeAllOrgPointsFlag);
        returnConsumePoints.setOrgCode(orgCode);
        returnConsumePoints.setCustomerNo(customerNo);
        returnConsumePoints.setDate(new Date());
        returnConsumePoints.setValidFlag(ConsumePoints.VALID_FLAG_NORMAL);
        returnConsumePoints.setIsNewRecord(true);

        return returnConsumePoints;
    }

    /**
     * 根据证件号码获取客户当前全行可用积分、当前网点、网点可用积分、是否通兑积分标志
     * @return consumePoints
     * @throws Exception
     */
    public ConsumePoints getIntialConsumePointsByIdentityNo(String identityNo) throws Exception{
        ConsumePoints consumePoints = new ConsumePoints();
        //设置客户证件号码
        consumePoints.setIdentityNo(identityNo);
        //获取客户名称
        consumePoints.setCustomerName(getCustomerNameByIdentityNo(identityNo));

        //获取当前是否允许通兑积分的标志
        consumePoints.setConsumeAllOrgPoints(getConsumeAllOrgPointsFlag());

        //获取客户在所有网点的总积分
        Double allOrgPoints = 0D;
        ConsumePoints resultConsumePoints = getCustomerConsumePointsInAllOrgByIdentityNo(identityNo);
        if (resultConsumePoints != null && resultConsumePoints.getAllOrgPoints() != null) allOrgPoints = resultConsumePoints.getAllOrgPoints();

        //获取客户在当前网点的总积分
        String orgCode = UserUtils.getUserOrganization().getOrganizationCode();
        Double currentOrgPoints = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerConsumePointsInGivenOrgByIdentityNoAndOrgCode(identityNo,orgCode);
        if (resultConsumePoints != null) currentOrgPoints = resultConsumePoints.getCurrentOrgPoints();

        //获取客户在全部网点已兑换的积分
        Double customerAlreadyConsumedPointsInAllOrg = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerAlreadyConsumedPointsInAllOrgByIdentityNo(identityNo);
        if (resultConsumePoints != null) customerAlreadyConsumedPointsInAllOrg = resultConsumePoints.getPointsValue();

        //获取客户在当前网点已兑换的积分
        Double customerAlreadyConsumedPointsInGivenOrg = 0D;
        resultConsumePoints = null;
        resultConsumePoints = getCustomerAlreadyConsumedPointsInGivenOrgByIdentityNoAndOrgCode(identityNo,orgCode);
        if (resultConsumePoints != null) customerAlreadyConsumedPointsInGivenOrg = resultConsumePoints.getPointsValue();


        //当前网点
        consumePoints.setOrgCode(orgCode);

        //全部网点可用积分
        Double temp = allOrgPoints - customerAlreadyConsumedPointsInAllOrg;
        consumePoints.setAllOrgPoints(temp < 0?0:temp );//可用积分小于0时设置可用积分为0

        //当前网点可用积分
        temp = currentOrgPoints - customerAlreadyConsumedPointsInGivenOrg;
        consumePoints.setCurrentOrgPoints(temp <0?0:temp);

        return consumePoints;
    }

    /**
     * 获取当前“是否允许通兑积分”的标志（该标志存储于数据库t_khjf_global_parameter表中的consumeAllOrgPointsFlag中）
     * @return Boolean的true或者false
     * @throws Exception
     */
    public Boolean getConsumeAllOrgPointsFlag() throws Exception{
        KhjfParameter khjfParameter = new KhjfParameter();
        khjfParameter.setName("consumeAllOrgPointsFlag");
        KhjfParameter result = dao.getKhjfGlobalParameter(khjfParameter);

        if (result == null || result.getValue() == null) throw new Exception("客户积分参数设置中不存在以下有效属性：是否允许跨网点兑换积分标志(consumeAllOrgPoints)");
        if (result.getValue().equals("true")) return true;
        else if (result.getValue().equals("false")) return false;
        else throw new Exception("返回非法值，请检查客户积分参数表中consumeAllOrgPoints的value值");
    }

    /**
     * 更新通兑标志为与原始标志相反的标志
     * @throws Exception
     */
    public void updateConsumeAllOrgPointsFlag(Boolean flag) throws Exception{
        KhjfParameter khjfParameter = new KhjfParameter();
        khjfParameter.setName("consumeAllOrgPointsFlag");
        Boolean currentConsumeAllOrgPointsFlag = getConsumeAllOrgPointsFlag();

        if (flag == currentConsumeAllOrgPointsFlag) return; //新标志与原始标志相同则不做处理
        khjfParameter.setValidFlag(false); //原记录的validFLag为false

        //设置原始标志的validFlag为false
        dao.updateValidFlagOfKhjfGlobalParameter(khjfParameter);
        //插入新记录
        khjfParameter.setValidFlag(true);//新记录的validFlag为true
        if (currentConsumeAllOrgPointsFlag) khjfParameter.setValue("false");//如果原始标志为true，则设置新标志为false
        else khjfParameter.setValue("true");
        khjfParameter.preInsert();
        dao.insertKhjfGlobalParameter(khjfParameter);
    }

    /**
     * 获取当前是否允许跨网点清理积分的标志
     * @return boolean
     */
    public Boolean getClearAllOrgPointsFlag() throws Exception{
        KhjfParameter khjfParameter = new KhjfParameter();
        khjfParameter.setName("clearAllOrgPointsFlag");
        KhjfParameter result = dao.getKhjfGlobalParameter(khjfParameter);

        if (result == null || result.getValue() == null) throw new Exception("客户积分参数设置中不存在以下有效属性：是否允许跨网点清理积分标志(clearAllOrgPoints)");
        if (result.getValue().equals("true")) return true;
        else if (result.getValue().equals("false")) return false;
        else throw new Exception("返回非法值，请检查客户积分参数表中clearAllOrgPoints的value值");

    }

    /**
     * 设置跨机构清理积分标志
     * @param flag true=可以跨机构清理积分；false=不可以跨机构清理积分
     * @throws Exception
     */
    public void updateClearAllOrgPointsFlag(Boolean flag) throws Exception{
        KhjfParameter khjfParameter = new KhjfParameter();
        khjfParameter.setName("clearAllOrgPointsFlag");
        Boolean currentClearAllOrgPointsFlag = getClearAllOrgPointsFlag();

        if (flag == currentClearAllOrgPointsFlag) return; //新标志与原始标志相同则不做处理
        khjfParameter.setValidFlag(false); //原记录的validFLag为false

        //设置原始标志的validFlag为false
        dao.updateValidFlagOfKhjfGlobalParameter(khjfParameter);
        //插入新记录
        khjfParameter.setValidFlag(true);//新记录的validFlag为true
        if (currentClearAllOrgPointsFlag) khjfParameter.setValue("false");//如果原始标志为true，则设置新标志为false
        else khjfParameter.setValue("true");
        khjfParameter.preInsert();
        dao.insertKhjfGlobalParameter(khjfParameter);
    }

    /**
     * 在数据库中，通过汇总t_khjf_customer_points_record表中的积分记录，计算得出给定证件号码客户在当前机构的总积分
     * @param identityNo,orgCode 因为是从t_khjf_customer_points_record表中计算积分，所以参数通过CustomerPointsRecord类型来给定
     *                             参数中需提供：机构号、客户证件号码
     */
    public ConsumePoints getCustomerConsumePointsInGivenOrgByIdentityNoAndOrgCode(String identityNo,String orgCode){
        CustomerPointsRecord customerPointsRecord = new CustomerPointsRecord();
        customerPointsRecord.setCustomerIdentityNo(identityNo);
        customerPointsRecord.setOrgCode(orgCode);
        return dao.getCustomerConsumePointsInGivenOrg(customerPointsRecord);
    }

    /**
     * 获取客户在指定网点已经兑换的积分（当前生效，已清理的不计算在内）
     * @param identityNo 证件号码
     * @param orgCode 机构号
     * @return pointsValue属性包含已兑换的积分值
     */
    public ConsumePoints getCustomerAlreadyConsumedPointsInGivenOrgByIdentityNoAndOrgCode(String identityNo,String orgCode){
        CustomerPointsRecord customerPointsRecord = new CustomerPointsRecord();
        customerPointsRecord.setCustomerIdentityNo(identityNo);
        customerPointsRecord.setOrgCode(orgCode);
        return dao.getCustomerAlreadyConsumedPointsInGivenOrg(customerPointsRecord);
    }

    /**
     * 获取客户在所有网点已兑换的积分（当前生效，已清理的不计算在内）
     * @param identityNo 证件号码
     * @return pointsValue属性包含已兑换的积分
     */
    public ConsumePoints getCustomerAlreadyConsumedPointsInAllOrgByIdentityNo(String identityNo){
        CustomerPointsRecord customerPointsRecord = new CustomerPointsRecord();
        customerPointsRecord.setCustomerIdentityNo(identityNo);
        return dao.getCustomerAlreadyConsumedPointsInAllOrg(customerPointsRecord);
    }

    /**
     * 在数据库中，通过汇总t_khjf_customer_points_record表中的积分记录，计算得出给定证件号码客户在所有机构的总积分
     * @param identityNo 因为是从t_khjf_customer_points_record表中计算积分，所以参数通过CustomerPointsRecord类型来给定
     *                             参数中需提供：客户证件号码
     */
    public ConsumePoints getCustomerConsumePointsInAllOrgByIdentityNo(String identityNo){
        CustomerPointsRecord customerPointsRecord = new CustomerPointsRecord();
        customerPointsRecord.setCustomerIdentityNo(identityNo);
        return dao.getCustomerConsumePointsInAllOrg(customerPointsRecord);
    }

    /**
     * 通过证件号码获取客户名称
     * @param identityNo 证件号码
     */
    public String getCustomerNameByIdentityNo(String identityNo){
        return dao.getCustomerNameByIdentityNo(identityNo);
    }

    /**
     * 根据证件号码获取客户号
     * @param identityNo 证件号码
     * @return 客户号
     */
    public String getCustomerNoByIdentityNo(String identityNo){
        return dao.getCustomerNoByIdentityNo(identityNo);
    }

}
