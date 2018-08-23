package com.duobi.manager.sys.controller;

import com.duobi.manager.sys.entity.Organization;
import com.duobi.manager.sys.entity.UserOrganization;
import com.duobi.manager.sys.service.OrganizationService;
import com.duobi.manager.sys.service.UserService;
import com.duobi.manager.sys.utils.*;
import com.duobi.manager.sys.utils.base.BaseEntity;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by chenjianjun on 16/8/4.
 */
@Controller
@RequestMapping("sys/user")
public class UserController extends CrudController<UserService, UserOrganization, Long> {

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping("/updatePassword")
    public @ResponseBody
    Object updatePassword(@RequestBody RequestJson<UserOrganization, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        try{
            service.updatePassword(requestJson.getEntities());
            responseJson.setSuccess(true);
            responseJson.setMsg("修改密码成功!");
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    @RequestMapping("/organization")
    public @ResponseBody
    Object getUserOrganization(@RequestBody RequestJson<Organization, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        long total = 0L;
        Long id = requestJson.getId();
        if (null != id && Long.valueOf(id.toString()).longValue() > 0) {
            BaseEntity model = organizationService.get(id);
            responseJson.setData(model);
        } else {
            Organization entity = requestJson.getFilter();
            if (null == entity) {
                entity = new Organization();
            }
            List<Organization> tree = organizationService.findAsTree(entity);
            responseJson.setChildren(tree);
            total = tree.size();
        }
        responseJson.setSuccess(true);
        responseJson.setMsg("获取数据成功!");
        responseJson.setTotal(total);

        return responseJson;
    }

    /**
     * 重置用户密码
     */
    @RequestMapping("/resetPassword")
    public @ResponseBody
    Object resetPassword(@RequestBody RequestJson<UserOrganization, Long> requestJson) {

        ResponseJson responseJson = new ResponseJson();

        service.resetPassword(requestJson.getEntities());

        responseJson.setSuccess(true);
        responseJson.setMsg("密码重置成功!");
        return responseJson;
    }

    @RequestMapping("/alterOrganization")
    public @ResponseBody
    Object updateOrganization(@RequestBody RequestJson<UserOrganization, Long> requestJson) throws ServiceException {
        ResponseJson responseJson = new ResponseJson();

        try {
            service.alterOrganization(requestJson.getEntities());
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("用户机构调入/注销成功!");
        return responseJson;
    }

    @RequestMapping("/updateRole")
    public @ResponseBody
    Object updateRole(@RequestBody RequestJson<UserOrganization, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        service.alterRole(requestJson.getEntities());

        responseJson.setSuccess(true);
        responseJson.setMsg("用户角色修改成功!");
        return responseJson;
    }
}
