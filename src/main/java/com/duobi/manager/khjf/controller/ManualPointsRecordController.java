package com.duobi.manager.khjf.controller;

import com.duobi.manager.khjf.entity.ManualPointsRecord;
import com.duobi.manager.khjf.service.ManualPoinsRecordService;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("khjf/manualpointsrecord")
public class ManualPointsRecordController extends CrudController<ManualPoinsRecordService,ManualPointsRecord,Long> {

    @Override
    @RequestMapping(value = "/create")
    @RequiresPermissions("khjf:manualpointsrecord:add")
    public @ResponseBody Object create(@RequestBody RequestJson<ManualPointsRecord, Long> requestJson) {
        ManualPointsRecord manualPointsRecord = requestJson.getEntities().get(0);
        return service.addHandyPoints(manualPointsRecord);
    }

    @RequestMapping(value = "/audit")
    @RequiresPermissions("khjf:manualpointsrecord:audit")
    public @ResponseBody Object audit(@RequestBody RequestJson<ManualPointsRecord,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        ManualPointsRecord manualPointsRecord = requestJson.getEntities().get(0);

        if (manualPointsRecord != null) {

            try {
                service.auditManualPointsRecordById(manualPointsRecord);
                responseJson.setSuccess(true);
                responseJson.setMsg("审核通过");
            }catch (Exception e){
                responseJson.setMsg("审核通过时失败,错误原因为："+e.getMessage());
                responseJson.setSuccess(false);
            }
        }
        else {
            responseJson.setSuccess(false);
            responseJson.setMsg("未提供要审核的手工积分");
        }

        return responseJson;
    }

    @RequestMapping(value = "/deny")
    @RequiresPermissions("khjf:manualpointsrecord:deny")
    public @ResponseBody Object deny(@RequestBody RequestJson<ManualPointsRecord,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        ManualPointsRecord manualPointsRecord = requestJson.getEntities().get(0);

        if (manualPointsRecord != null) {

            try {
                service.denyManualPointsRecordById(manualPointsRecord);
                responseJson.setSuccess(true);
                responseJson.setMsg("审核拒绝成功");
            }catch (Exception e){
                responseJson.setSuccess(false);
                responseJson.setMsg("审核拒绝时失败,错误原因为："+e.getMessage());
            }
        }
        else {
            responseJson.setSuccess(false);
            responseJson.setMsg("未提供要拒绝审核的手工积分");
        }

        return responseJson;
    }

}
