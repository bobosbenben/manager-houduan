package com.duobi.manager.sys.controller;

import com.duobi.manager.sys.entity.Menu;
import com.duobi.manager.sys.service.MenuService;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.tree.TreeController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class MenuController extends TreeController<MenuService, Menu, Long> {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    /**
     * 获取所有菜单分组(按系统模块划分),用于前端UI对菜单按模块分组
     * @return
     */
    @RequestMapping("/menugroups")
    @RequiresPermissions(value = "sys:menu:get")
    public  @ResponseBody
    Object menuGroups() {
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Menu> modules = service.getModules();
            responseJson.setData(modules);
            responseJson.setSuccess(true);
            responseJson.setTotal(Long.valueOf(modules.size()));
            responseJson.setMsg("");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            responseJson.setSuccess(false);
            responseJson.setTotal(0L);
            responseJson.setMsg("后台错误, 获取数据时出错!");
        }

        return responseJson;
    }

    /**
     * 获取某一菜单分组对应的所有菜单项,其中buttons封装成
     * @param requestJson
     * @return
     */
    @RequestMapping(value = "/groupmenus")
    @RequiresPermissions(value = "sys:menu:get")
    public @ResponseBody Object groupMenus(@RequestBody RequestJson<Menu, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Menu> groupMenuTree = service.getGroupMenuTree(requestJson.getId());
            responseJson.setSuccess(true);
            responseJson.setMsg("");
            responseJson.setTotal(1L);
            responseJson.setChildren(groupMenuTree);
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg("后台错误, 获取数据时出错!");
            responseJson.setTotal(0L);
        }


        return responseJson;
    }

    @RequestMapping(value = "/getparentmenu")
    @RequiresPermissions(value = "sys:menu:get")
    public @ResponseBody Object possibleParentMenus(@RequestBody RequestJson<Menu, Long> requestJson) {
        ResponseJson responseJson = new ResponseJson();
        Long id = requestJson.getEntities().get(0).getId();

        Menu parentMenu = service.getParentMenu(id);
        responseJson.setSuccess(true);
        responseJson.setMsg("获取父菜单成功");
        responseJson.setTotal(1L);
        responseJson.setData(parentMenu);

        return responseJson;
    }

    @Override
    @RequiresPermissions(value = "sys:menu:get")
    public Object get(HttpServletRequest request, @RequestBody RequestJson<Menu, Long> requestJson) {
        return super.get(request, requestJson);
    }

    @Override
    @RequiresPermissions(value = "sys:menu:create")
    public Object create(@RequestBody RequestJson<Menu, Long> requestJson) {
        return super.create(requestJson);
    }

    @Override
    @RequiresPermissions(value = "sys:menu:update")
    public Object update(@RequestBody RequestJson<Menu, Long> requestJson) {
        return super.update(requestJson);
    }

    @Override
    @RequiresPermissions(value = "sys:menu:delete")
    public Object delete(@RequestBody RequestJson<Menu, Long> requestJson) {
        return super.delete(requestJson);
    }
}
