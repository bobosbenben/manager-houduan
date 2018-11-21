package com.duobi.manager.khjf.controller;

import com.duobi.manager.khjf.entity.ClearPoints;
import com.duobi.manager.khjf.entity.CustomerPointsRecord;
import com.duobi.manager.khjf.service.CustomerPoinsRecordService;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("khjf/customerpointsrecord")
public class CustomerPointsRecordController extends CrudController<CustomerPoinsRecordService,CustomerPointsRecord,Long> {

    @RequestMapping(value = "/singlerecordclear")
    @RequiresPermissions("khjf:customerpointsrecord:singlerecordclear")
    public @ResponseBody Object singleRecordClear(@RequestBody RequestJson<CustomerPointsRecord,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();

        if (requestJson == null || requestJson.getEntities() == null || requestJson.getEntities().get(0)==null){
            responseJson.setSuccess(false);
            responseJson.setMsg("清理单条积分时未提供积分记录的id");
            return responseJson;
        }

        ClearPoints clearPoints = new ClearPoints();
        clearPoints.setCustomerPointsRecordId(requestJson.getEntities().get(0).getId());
        clearPoints.setType(ClearPoints.CLEAR_POINTS_TYPE_MANUAL_SINGLE_RECORD);//设置类型为单条清理

        try {
            service.clearPoints(clearPoints);
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("清理积分成功");
        return responseJson;
    }

    @RequestMapping(value = "/singlecustomerclear")
    @RequiresPermissions("khjf:customerpointsrecord:singlecustomerclear")
    public @ResponseBody Object singleCustomerClear(@RequestBody RequestJson<CustomerPointsRecord,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();

        if (requestJson == null || requestJson.getEntities() == null || requestJson.getEntities().get(0)==null|| requestJson.getEntities().get(0).getCustomerIdentityNo()==null){
            responseJson.setSuccess(false);
            responseJson.setMsg("清理单户积分时未提供客户证件号码");
            return responseJson;
        }

        ClearPoints clearPoints = new ClearPoints();
        clearPoints.setIdentityNo(requestJson.getEntities().get(0).getCustomerIdentityNo());
        clearPoints.setType(ClearPoints.CLEAR_POINTS_TYPE_MANUAL_SINGLE_CUSTOMER);//设置类型为单户清理
        if (requestJson.getEntities() != null && requestJson.getEntities().get(0) != null) //设置orgCode
            clearPoints.setOrgCode(requestJson.getEntities().get(0).getOrgCode());

        try {
            service.clearPoints(clearPoints);
        }catch (Exception e){
            responseJson.setMsg(e.getMessage());
            responseJson.setSuccess(false);
            return responseJson;
        }

        responseJson.setMsg("清理积分成功");
        responseJson.setSuccess(true);
        return responseJson;
    }

    @RequestMapping(value = "/batchclear")
    @RequiresPermissions("khjf:customerpointsrecord:batchclear")
    public @ResponseBody Object batchClear(@RequestBody RequestJson<CustomerPointsRecord,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();

        ClearPoints clearPoints = new ClearPoints();
        clearPoints.setType(ClearPoints.CLEAR_POINTS_TYPE_MANUAL_BATCH);//设置类型为批量清理
        if (requestJson!=null && requestJson.getEntities() != null && requestJson.getEntities().get(0) != null)  //设置orgCode
            clearPoints.setOrgCode(requestJson.getEntities().get(0).getOrgCode());

        try {
            service.clearPoints(clearPoints);
        }catch (Exception e){
            responseJson.setMsg(e.getMessage());
            responseJson.setSuccess(false);
            return responseJson;
        }

        responseJson.setMsg("清理积分成功");
        responseJson.setSuccess(true);
        return responseJson;
    }


}
