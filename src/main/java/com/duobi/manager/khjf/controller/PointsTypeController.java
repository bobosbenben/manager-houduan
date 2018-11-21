package com.duobi.manager.khjf.controller;

import com.duobi.manager.khjf.entity.PointsType;
import com.duobi.manager.khjf.entity.PointsTypeChange;
import com.duobi.manager.khjf.service.PointsTypeService;
import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.utils.DateHelper;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("khjf/pointstype")
public class PointsTypeController extends CrudController<PointsTypeService,PointsType,Long>{

    @RequestMapping("/updatevalue") //更新当特积分类型的积分值
    @RequiresPermissions("khjf:pointstype:valuealter")
    public @ResponseBody Object updateValue(@RequestBody RequestJson<PointsType,Long> requestJson) {
        return service.updatePointsTypeValue(requestJson.getEntities().get(0));
    }

    @RequestMapping("/updateavailable") //更新当特积分类型的可用状态
    @RequiresPermissions("khjf:pointstype:availablealter")
    public @ResponseBody Object updateAvailable(@RequestBody RequestJson<PointsType,Long> requestJson) {
        return service.updatePointsTypeAvailable(requestJson.getEntities().get(0));
    }

    @RequestMapping("/handypointstype") //获取所有可以手动积分的类型
    public @ResponseBody Object getAllHandyPointsType() {
        ResponseJson responseJson = new ResponseJson();
        responseJson.setSuccess(true);
        responseJson.setData(service.getAllHandyPointsType(new PointsType()));
        responseJson.setMsg("获取手动积分类型成功");

        return responseJson;
    }

    @RequestMapping(value = "/create")
    @Override
    @RequiresPermissions("khjf:pointstype:add")
    public @ResponseBody Object create(@RequestBody RequestJson<PointsType, Long> requestJson) {
        List<PointsType> entities = requestJson.getEntities();
        ResponseJson responseJson = new ResponseJson();

        for (PointsType entity:entities){
            //将记录插入t_khjf_type表
            service.save(entity);
            //将记录插入t_khjf_points_type_change表，该表是积分值改变日志，如某一个积分类型由1分改为2分
            PointsTypeChange pointsTypeChange = new PointsTypeChange();
            pointsTypeChange.setValue(entity.getValue());
            pointsTypeChange.setStartDate(entity.getStartDate());
            pointsTypeChange.setEndDate(entity.getEndDate());
            pointsTypeChange.setPointsTypeId(entity.getId());
            pointsTypeChange.setValidFlag(true);
            pointsTypeChange.preInsert();
            service.insertPointsTypeChange(pointsTypeChange);
            //将记录插入t_khjf_points_type_available表，该表是积分停用与启用日志
            pointsTypeChange.setAvailableStatus(entity.getAvailableStatus());
            service.insertPointsTypeAvailable(pointsTypeChange);
        }


        responseJson.setSuccess(true);
        responseJson.setMsg("添加数据成功!");
        responseJson.setTotal(0L);

        return responseJson;
    }


}
