package com.duobi.manager.khjf.service;

import com.duobi.manager.khjf.dao.ManualPointsRecordDao;
import com.duobi.manager.khjf.entity.ManualPointsRecord;
import com.duobi.manager.khjf.entity.PointsType;
import com.duobi.manager.sys.utils.*;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class ManualPoinsRecordService extends CrudService<ManualPointsRecordDao,ManualPointsRecord,Long> {

    public Page<ManualPointsRecord> findPage(Page<ManualPointsRecord> page, ManualPointsRecord manualPointsRecord){

        manualPointsRecord.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(Global.getKhjfModuleId(), manualPointsRecord.getCurrentUserOrganization(), "o",null));

        page.setAutoPage(false);
        manualPointsRecord.setPage(page);
        page.setCount(dao.findCount(manualPointsRecord));
        if (page.getCount() > 0) {
            page.setList(dao.findList(manualPointsRecord));
        }
        return page;
    }

    /**
     * 为客户手工积分
     * @param manualPointsRecord 从前端获取的积分记录
     * @return responseJson
     */
    public ResponseJson addHandyPoints(ManualPointsRecord manualPointsRecord){
        ResponseJson responseJson = new ResponseJson();

        if (manualPointsRecord.getPointsTypeId() == null){
            responseJson.setSuccess(false);
            responseJson.setMsg("产品类型为空");
            return responseJson;
        }
        //判断产品是否为手工积分的类型
        String type = dao.getPointsTypeTypeById(manualPointsRecord.getPointsTypeId());
        if (type == null || !type.equals(PointsType.TYPE_MANUAL)){
            responseJson.setSuccess(false);
            responseJson.setMsg("积分类型不存在或者不为手工积分类型");
            return responseJson;
        }
        //判断产品是否停用
        String availableStatus = dao.getAvailableStatus(manualPointsRecord);
        if (availableStatus == null || !availableStatus.equals(PointsType.AVAILABLE_STATUS_NORMAL)){
            responseJson.setSuccess(false);
            responseJson.setMsg("积分类型停用，不能积分");
            return responseJson;
        }

        //账号、卡号必须有一个存在
        if (StringUtils.isBlank(manualPointsRecord.getCardNo()) && StringUtils.isBlank(manualPointsRecord.getAccountNo())){
            responseJson.setSuccess(false);
            responseJson.setMsg("必须输入一个账号或卡号");
            return responseJson;
        }

        if (manualPointsRecord.getStartDate()==null || manualPointsRecord.getEndDate()==null){
            responseJson.setSuccess(false);
            responseJson.setMsg("请设置积分的时间区间段");
        }

        if(manualPointsRecord.getStartDate().after(manualPointsRecord.getEndDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("起始时间大于终止时间，请修改");
        }

        //存在账号时，判断该账号是否是一个卡对应的账号
        if (StringUtils.isNotBlank(manualPointsRecord.getAccountNo())){
            String cardNo = dao.getCardNoByAccountNo(manualPointsRecord.getAccountNo());
            //非卡，就是一个普通账户
            if (StringUtils.isBlank(cardNo)){
                String customerNo = dao.getCustomerNoByCardNoOrAccountNo(manualPointsRecord.getAccountNo());
                if (customerNo == null){
                    responseJson.setSuccess(false);
                    responseJson.setMsg("无法通过账号获取客户信息，可能是账户错误");
                    return responseJson;
                }
                else {
                    manualPointsRecord.setCustomerNo(customerNo);
                    manualPointsRecord.setOrgCode(dao.getOrgCodeByAccountNo(manualPointsRecord.getAccountNo()));
                }
            }
            //是卡
            else {
                //同时存在卡号时判断账号与卡号是否相符
                if (StringUtils.isNotBlank(manualPointsRecord.getCardNo())){
                    if (!cardNo.equals(manualPointsRecord.getCardNo())){
                        responseJson.setSuccess(false);
                        responseJson.setMsg("卡号与账号不符");
                        return responseJson;
                    }
                    //卡号与账号相符
                    else {
                        String customerNo = dao.getCustomerNoByCardNoOrAccountNo(cardNo);
                        manualPointsRecord.setCustomerNo(customerNo);
                        manualPointsRecord.setOrgCode(dao.getOrgCodeByAccountNo(manualPointsRecord.getAccountNo()));
                    }
                }
                //是卡，但是只存在账号，无卡号
                else {
                    String customerNo = dao.getCustomerNoByCardNoOrAccountNo(cardNo);
                    manualPointsRecord.setCustomerNo(customerNo);
                    manualPointsRecord.setCardNo(cardNo);
                    manualPointsRecord.setOrgCode(dao.getOrgCodeByAccountNo(manualPointsRecord.getAccountNo()));
                }
            }
        }
        //只存在卡号时
        else {
            String accountNo = dao.getAccountNoByCardNo(manualPointsRecord.getCardNo());
            if (StringUtils.isBlank(accountNo)){
                responseJson.setSuccess(false);
                responseJson.setMsg("卡号对应的账号不存在，卡号错误");
                return responseJson;
            }
            else {
                String customerNo = dao.getCustomerNoByCardNoOrAccountNo(manualPointsRecord.getCardNo());
                if (StringUtils.isBlank(customerNo)){
                    responseJson.setSuccess(false);
                    responseJson.setMsg("通过卡号获取客户号失败，可能是后台账号与客户号对照表错误，请联系管理员");
                    return responseJson;
                }
                else {
                    manualPointsRecord.setAccountNo(accountNo);
                    manualPointsRecord.setCustomerNo(customerNo);
                    manualPointsRecord.setOrgCode(dao.getOrgCodeByAccountNo(accountNo));
                }
            }
        }

        //设置积分的有效标志为：‘0’，即有效
        manualPointsRecord.setValidFlag("0");
        //设置状态未待审核
        manualPointsRecord.setStatus("0");
        //设置柜员号为当前登录的用户
        manualPointsRecord.setTellerCode(UserUtils.getUser().getCode());
        //如果时间区间段的起始时间与终止时间相同，则设置date为startDate,表示这条积分记录是单一一天的积分，不是区间段积分
        if (manualPointsRecord.getStartDate().equals(manualPointsRecord.getEndDate())) {
            manualPointsRecord.setDate(manualPointsRecord.getStartDate());
        }
        //设置区间段积分的“月”为终止日期所在的月，“年”为终止日期所在的年
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(manualPointsRecord.getEndDate());
        manualPointsRecord.setMonth(String.valueOf(endDateCalendar.get(Calendar.MONTH)+1));
        manualPointsRecord.setYear(String.valueOf(endDateCalendar.get(Calendar.YEAR)));
        //设置该条记录的积分值
        manualPointsRecord.setValue(dao.getPointsTypeValueByPointsTypeId(manualPointsRecord));
        save(manualPointsRecord);
        responseJson.setSuccess(true);
        responseJson.setMsg("添加数据成功!");
        responseJson.setTotal(0L);

        return responseJson;
    }

    /**
     * 审核通过手工积分
     * @param manualPointsRecord 包含id
     */
    public void auditManualPointsRecordById(ManualPointsRecord manualPointsRecord) throws Exception{

        //判断原始状态是否为待审核
        ManualPointsRecord original = dao.get(manualPointsRecord);
        ManualPointsRecord original2 = new ManualPointsRecord(); //备用，因为insertToCustomerPointsRecord函数会更改id属性的值
        original2.setId(original.getId());
        if (!original.getStatus().equals(ManualPointsRecord.MANUAL_POINTS_RECORD_SUBMITTED)){
            throw new Exception("该积分的原始状态不为待审核");
        }
        //将积分记录挪入t_khjf_customer_points_record,删除t_khjf_manual_points_record表中的该条记录
        dao.insertToCustomerPointsRecord(original);
        dao.delete(original2);
    }

    /**
     * 审核拒绝手工积分
     * @param manualPointsRecord 包含id
     */
    public void denyManualPointsRecordById(ManualPointsRecord manualPointsRecord) throws Exception{

        //判断原始状态是否为待审核
        ManualPointsRecord original = dao.get(manualPointsRecord);
        if (!original.getStatus().equals(ManualPointsRecord.MANUAL_POINTS_RECORD_SUBMITTED)){
            throw new Exception("该积分的原始状态不为待审核");
        }
        //修改数据库中记录的状态字段
        dao.denyManualPointsRecord(manualPointsRecord);
    }

}
