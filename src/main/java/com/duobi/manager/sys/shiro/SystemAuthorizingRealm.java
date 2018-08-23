package com.duobi.manager.sys.shiro;

import com.duobi.manager.sys.entity.Menu;
import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.service.MenuService;
import com.duobi.manager.sys.service.RoleService;
import com.duobi.manager.sys.service.UserService;
import com.duobi.manager.sys.utils.CurrentSessionUtil;
import com.duobi.manager.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        System.out.println("开始验证用户名密码");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        User user = userService.getUserByUserCode(username);
        if (user == null) return null;
        if (!user.getLoginUseable()){
            throw new AuthenticationException("msg:该帐号已禁止登录.");
        }
        //注意，下面这条语句中第一个参数必须放入user，否则其他地方使用principal.getCode时将会无结果。这里将整个User变量都放入到principal中。
        return new SimpleAuthenticationInfo(user, user.getLoginPassword(), this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("开始赋予权限");
//        Principal principal = (Principal) getAvailablePrincipal(principals);
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        User user = userService.getUserByUserCode(principal.getCode());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list){
                if (StringUtils.isNotBlank(menu.getPermission())){
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(),",")){
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            for (Role role : user.getRoleList()){
                info.addRole(role.getEnname());
            }
            // 更新登录IP和时间
            //getUserService().updateUserLoginInfo(user);
            // 记录登录日志
            //LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        } else {
            return null;
        }

    }

    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id; // 编号
        private String code; // 登录名,柜员号
        private String name; // 姓名

        public Principal(User user) {
            this.id = user.getId();
            this.code = user.getCode();
            this.name = user.getName();
        }

        public Long getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                HttpSession session = CurrentSessionUtil.getCurrentSession();
                if (session != null) return session.getId();
                else return "";
            } catch (Exception e) {
                return "";
            }
        }
    }

}
