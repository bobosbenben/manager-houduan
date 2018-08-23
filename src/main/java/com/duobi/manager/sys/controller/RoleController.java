package com.duobi.manager.sys.controller;

import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.service.RoleService;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("sys/role")
public class RoleController extends CrudController<RoleService, Role, Long> {

    @RequestMapping("/moduleroles")
    public @ResponseBody Object getRolesByModuleId(@RequestBody RequestJson<Role,Long> requestJson){
        ResponseJson responseJson = new ResponseJson();
        try {
            if (requestJson.getEntities().size()>0){
                Role role = requestJson.getEntities().get(0);
                List<Role> roles = service.getByModuleId(role);
                responseJson.setSuccess(true);
                responseJson.setData(roles);
                responseJson.setTotal(new Long(roles.size()));
                responseJson.setMsg("根据系统模块获取角色成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

}
