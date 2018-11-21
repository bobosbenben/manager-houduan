package com.duobi.manager.khjf.service;

import com.duobi.manager.khjf.dao.PointsTypeDao;
import com.duobi.manager.khjf.entity.HandleConfig;
import com.duobi.manager.khjf.entity.PointsType;
import com.duobi.manager.khjf.entity.PointsTypeChange;
import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.utils.*;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class PointsTypeService extends CrudService<PointsTypeDao,PointsType,Long>{

//    public Page<PointsType> findPage(Page<PointsType> page, PointsType pointsType){
//
//        pointsType.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(Global.getKhjfModuleId(), pointsType.getCurrentUserOrganization(), null,null));
//
//        page.setAutoPage(false);
//        pointsType.setPage(page);
//        page.setCount(dao.findCount(pointsType));
//        if (page.getCount() > 0) {
//            page.setList(dao.findList(pointsType));
//        }
//        return page;
//    }

    @Transactional(readOnly = true)
    public List<PointsType> getAllHandyPointsType(PointsType pointsType) {
        return dao.getAllHandyPointsType(pointsType);
    }

    @Transactional(readOnly = true)
    public PointsTypeChange getValidPointsTypeChangeByPointsTypeId(PointsType pointsType) {
        return dao.getValidPointsTypeChangeByPointsTypeId(pointsType);
    }

    @Transactional(readOnly = true)
    public PointsTypeChange getCurrentAvailableStatusByPointsTypeId(PointsType pointsType) {
        return dao.getCurrentAvailableStatusByPointsTypeId(pointsType);
    }

    public void updatePointsTypeChangeById(PointsTypeChange pointsTypeChange){
        dao.updatePointsTypeChangeById(pointsTypeChange);
    }

    public void updatePointsTypeAvailableById(PointsTypeChange pointsTypeChange){
        dao.updatePointsTypeAvailableById(pointsTypeChange);
    }

    public void deletePointsTypeChangeById(PointsTypeChange pointsTypeChange){
        dao.deletePointsTypeChangeById(pointsTypeChange);
    }

    public void deletePointsTypeAvailableById(PointsTypeChange pointsTypeChange){
        dao.deletePointsTypeAvailableById(pointsTypeChange);
    }

    public void insertPointsTypeChange(PointsTypeChange pointsTypeChange){
        dao.insertPointsTypeChange(pointsTypeChange);
    }

    public void insertPointsTypeAvailable(PointsTypeChange pointsTypeChange){
        dao.insertPointsTypeAvailable(pointsTypeChange);
    }

    public HandleConfig getHandleConfigByPointsTypeId(PointsType pointsType){
        return dao.getHandleConfigByPointsTypeId(pointsType);
    }

    public ResponseJson updatePointsTypeAvailable(PointsType p){
        ResponseJson responseJson = new ResponseJson();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前正在生效的积分值记录
        PointsTypeChange currentValidPointsTypeChange = getCurrentAvailableStatusByPointsTypeId(p);
        if (p == null) {
            responseJson.setMsg("前端传入的对象为空");
            responseJson.setSuccess(false);
            return responseJson;
        }

        if (p.getStartDate()==null){
            responseJson.setMsg("起始生效日期为空");
            responseJson.setSuccess(false);
            return responseJson;
        }
        if (p.getEndDate()!=null&&p.getStartDate().after(p.getEndDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("新设置的起始日期大于终止日期");
            return responseJson;
        }
        if (currentValidPointsTypeChange.getStartDate()!=null&&currentValidPointsTypeChange.getStartDate().after(p.getStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效起始日期必须大于上次的生效的起始日期:"+sdf.format(currentValidPointsTypeChange.getStartDate()));
            return responseJson;
        }

        if (currentValidPointsTypeChange.getEndDate()!=null&&!currentValidPointsTypeChange.getEndDate().before(p.getStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效起始日期必须大于上次的生效的终止日期:"+sdf.format(currentValidPointsTypeChange.getStartDate()));
            return responseJson;
        }

        if (p.getStartDate().before(getHandleConfigByPointsTypeId(p).getNextStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("该起始日期的积分已经计算完成，无法修改");
            return responseJson;
        }

        if (p.getStartDate().before(DateHelper.getCurrentPureDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效的起始日期不得小于当前时间");
            return responseJson;
        }


        //新设置的起始日期与当前生效记录的起始日期相同，即当日多次修改积分值
        if (p.getStartDate().equals(currentValidPointsTypeChange.getStartDate())){
            //删除旧记录
            deletePointsTypeAvailableById(currentValidPointsTypeChange);
        }
        else {
            //修改旧记录的endDate，并设置validFlag为false
            PointsTypeChange tempForUpateEndDate = new PointsTypeChange();
            tempForUpateEndDate.setId(currentValidPointsTypeChange.getId());
            tempForUpateEndDate.setEndDate(DateHelper.getYesterdayDate(p.getStartDate()));
            tempForUpateEndDate.setValidFlag(false);
            updatePointsTypeAvailableById(tempForUpateEndDate);
        }
        //新增新记录
        PointsTypeChange temp = new PointsTypeChange();
        temp.setPointsTypeId(p.getId());
        temp.setStartDate(p.getStartDate());
        temp.setEndDate(p.getEndDate());
        temp.setValidFlag(true);
        temp.setAvailableStatus(p.getAvailableStatus());
        temp.preInsert();
        insertPointsTypeAvailable(temp);

        responseJson.setSuccess(true);
        responseJson.setMsg("启用或停用积分类型成功");
        return responseJson;
    }

    /**
     * 修改积分值
     * @param p 前端传入的含有新startDate，endDate和value的PointsType对象
     * @return responseJson
     */
    public ResponseJson updatePointsTypeValue(PointsType p){
        ResponseJson responseJson =  new ResponseJson();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //获取当前正在生效的积分值记录
        PointsTypeChange currentValidPointsTypeChange = getValidPointsTypeChangeByPointsTypeId(p);
        if (p == null) {
            responseJson.setMsg("前端传入的对象为空");
            responseJson.setSuccess(false);
            return responseJson;
        }

        if (p.getStartDate()==null){
            responseJson.setMsg("起始生效日期为空");
            responseJson.setSuccess(false);
            return responseJson;
        }
        if (p.getEndDate()!=null&&p.getStartDate().after(p.getEndDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("新设置的起始日期大于终止日期");
            return responseJson;
        }
        if (currentValidPointsTypeChange.getStartDate()!=null&&currentValidPointsTypeChange.getStartDate().after(p.getStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效起始日期必须大于上次的生效的起始日期:"+sdf.format(currentValidPointsTypeChange.getStartDate()));
            return responseJson;
        }

        if (currentValidPointsTypeChange.getEndDate()!=null&&!currentValidPointsTypeChange.getEndDate().before(p.getStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效起始日期必须大于上次的生效的终止日期:"+sdf.format(currentValidPointsTypeChange.getStartDate()));
            return responseJson;
        }

        if (p.getStartDate().before(getHandleConfigByPointsTypeId(p).getNextStartDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("该起始日期的积分已经计算完成，无法修改");
            return responseJson;
        }

        if (p.getStartDate().before(DateHelper.getCurrentPureDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("生效的起始日期不得小于当前时间");
            return responseJson;
        }

        if (p.getValue().equals(currentValidPointsTypeChange.getValue()) && p.getStartDate().equals(currentValidPointsTypeChange.getStartDate()) && p.getEndDate().equals(currentValidPointsTypeChange.getEndDate())){
            responseJson.setSuccess(false);
            responseJson.setMsg("新的积分值与当前积分值相同，不予修改");
            return responseJson;
        }


        //新设置的起始日期与当前生效记录的起始日期相同，即当日多次修改积分值
        if (p.getStartDate().equals(currentValidPointsTypeChange.getStartDate())){
            //删除旧记录
            deletePointsTypeChangeById(currentValidPointsTypeChange);
        }
        else {
            //修改旧记录的endDate，并设置validFlag为false
            PointsTypeChange tempForUpateEndDate = new PointsTypeChange();
            tempForUpateEndDate.setId(currentValidPointsTypeChange.getId());
            tempForUpateEndDate.setEndDate(DateHelper.getYesterdayDate(p.getStartDate()));
            tempForUpateEndDate.setValidFlag(false);
            updatePointsTypeChangeById(tempForUpateEndDate);
        }
        //新增新记录
        PointsTypeChange temp = new PointsTypeChange();
        temp.setPointsTypeId(p.getId());
        temp.setStartDate(p.getStartDate());
        temp.setEndDate(p.getEndDate());
        temp.setValidFlag(true);
        temp.setValue(p.getValue());
        temp.preInsert();
        insertPointsTypeChange(temp);

        responseJson.setSuccess(true);
        responseJson.setMsg("修改积分值成功");
        return responseJson;
    }
}
