package com.duobi.manager.sys.controller;

import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.entity.UserOrganization;
import com.duobi.manager.sys.service.RoleService;
import com.duobi.manager.sys.shiro.FormAuthenticationFilter;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.StringUtils;
import com.duobi.manager.sys.utils.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SystemController {

    @RequestMapping(value = "/sys/login")
    public @ResponseBody Object login(HttpServletRequest request, Map<String, Object> map){
        ResponseJson responseJson = new ResponseJson();
        Map<String, Object> retData = new HashMap<>(2);

        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if (null != user) {
            user.setMenuList(UserUtils.getMenuTree());
            responseJson.setSuccess(true);
            responseJson.setMsg("您已经登录!");
            retData.put("loggedIn", true);
            retData.put("userInfo", user);
            responseJson.setData(retData);
        } else {
            String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
            if (StringUtils.isNotBlank(message)) { //登录逻辑执行失败,如输入错误的用户名或密码
                responseJson.setMsg(message);
            } else {
                responseJson.setMsg("您还没有登录,或登录已超时, 请重新登录!");
            }
            responseJson.setSuccess(false);
            retData.put("loggedIn", false);
            retData.put("userInfo", null);
            responseJson.setData(retData);
        }

        return responseJson;
    }

    /**
     * 登录成功后返回给前端的信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sys/success")
    public @ResponseBody Object loginsuccess(HttpServletRequest request, HttpServletResponse response) {
        ResponseJson responseJson = new ResponseJson();
        Map<String, Object> retData = new HashMap<>(2);

//        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        User user = (User)SecurityUtils.getSubject().getPrincipal();

        if (null != user) {
            UserOrganization userOrganization = UserUtils.getUserOrganization();
            if (userOrganization.getUser().getRoleList().size() <= 0) {
                userOrganization.getUser().setRoleList(UserUtils.getRoleList());
            }
            userOrganization.getUser().setMenuList(UserUtils.getMenuTree());

            responseJson.setSuccess(true);
            responseJson.setMsg("登录成功!");
            retData.put("loggedIn", true);
            retData.put("userInfo", userOrganization);
            responseJson.setData(retData);
        } else {
            responseJson.setSuccess(false);
            responseJson.setMsg("登录失败!");
            retData.put("loggedIn", false);
            retData.put("userInfo", null);
            responseJson.setData(retData);
        }

        return responseJson;
    }

//    @RequestMapping(value = "/sys/menu")
//    public @ResponseBody Object getMenu(){
//        ResponseJson responseJson = new ResponseJson();
//        User user = (User)SecurityUtils.getSubject().getPrincipal();
//
//        responseJson.setSuccess(true);
//        responseJson.setData(user);
//        return responseJson;
//    }

    @RequestMapping(value = "/sys/unauthorized")
    public @ResponseBody Object unauthorized(HttpServletRequest request, HttpServletResponse response) {
        ResponseJson responseJson = new ResponseJson();
        responseJson.setSuccess(false);
        responseJson.setMsg("您无权限访问该资源!");
        return responseJson;
    }

}
