package com.duobi.manager.khjf.controller;

import com.duobi.manager.khjf.entity.ConsumePoints;
import com.duobi.manager.khjf.service.ConsumePointsService;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.StringUtils;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/khjf/consumepoints")
public class ConsumePointsController extends CrudController<ConsumePointsService,ConsumePoints,Long> {

    @Override
    @RequestMapping(value = "/create")
    public @ResponseBody Object create(@RequestBody RequestJson<ConsumePoints, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        if (requestJson.getEntities() == null || requestJson.getEntities().size()<1 || StringUtils.isBlank(requestJson.getEntities().get(0).getIdentityNo())){
            responseJson.setSuccess(false);
            responseJson.setMsg("未提供证件号码");
            return responseJson;
        }

        ConsumePoints consumePoints;
        try {
            consumePoints = service.executeConsumePoints(requestJson.getEntities().get(0));
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        service.save(consumePoints);
        responseJson.setSuccess(true);
        responseJson.setMsg("兑换积分成功!");
        return responseJson;
    }

    @RequestMapping(value = "/getinitialdata")
    public @ResponseBody
    Object getConsumePointsInitialData(@RequestBody RequestJson<ConsumePoints,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        if (requestJson.getEntities() == null || requestJson.getEntities().size()<1 || StringUtils.isBlank(requestJson.getEntities().get(0).getIdentityNo())){
            responseJson.setSuccess(false);
            responseJson.setMsg("未提供证件号码");
            return responseJson;
        }

        try {
            ConsumePoints initialConsumePoints = service.getIntialConsumePointsByIdentityNo(requestJson.getEntities().get(0).getIdentityNo());
            responseJson.setSuccess(true);
            responseJson.setData(initialConsumePoints);
            responseJson.setMsg("获取客户积分成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    @RequestMapping(value = "/getconsumeallorgpointsflag")
    public @ResponseBody
    Object getConsumeAllOrgPointsFlag(){
        ResponseJson responseJson = new ResponseJson();
        Boolean flag;
        try {
            flag = service.getConsumeAllOrgPointsFlag();
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("获取当前跨网点通兑积分标志成功");
        responseJson.setData(flag);
        return responseJson;
    }

    @RequestMapping(value = "/updateconsumeallorgpointsflag")
    public @ResponseBody
    Object updateConsumeAllOrgPointsFlag(@RequestBody RequestJson<ConsumePoints,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        if (requestJson.getEntities() == null || requestJson.getEntities().get(0) == null){
            responseJson.setSuccess(false);
            responseJson.setMsg("未提交新的积分通兑标志");
            return responseJson;
        }

        try {
            service.updateConsumeAllOrgPointsFlag(requestJson.getEntities().get(0).getConsumeAllOrgPoints());
        }catch (Exception e){
            responseJson.setMsg(e.getMessage());
            responseJson.setSuccess(false);
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("设置当前跨网点通兑积分标志成功");
        return responseJson;
    }

    @RequestMapping(value = "/getclearallorgpointsflag")
    public @ResponseBody
    Object getClearAllOrgPointsFlag(){
        ResponseJson responseJson = new ResponseJson();
        Boolean flag;
        try {
            flag = service.getClearAllOrgPointsFlag();
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("获取当前跨网点清理积分标志成功");
        responseJson.setData(flag);
        return responseJson;
    }

    @RequestMapping(value = "/updateclearallorgpointsflag")
    public @ResponseBody
    Object updateClearAllOrgPointsFlag(@RequestBody RequestJson<ConsumePoints,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        if (requestJson.getEntities() == null || requestJson.getEntities().get(0) == null){
            responseJson.setSuccess(false);
            responseJson.setMsg("未提交新的跨机构清理积分标志");
            return responseJson;
        }

        try {
            service.updateClearAllOrgPointsFlag(requestJson.getEntities().get(0).getClearAllOrgPoints());
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("设置当前跨网点跨机构清理积分标志成功");
        return responseJson;
    }




}
