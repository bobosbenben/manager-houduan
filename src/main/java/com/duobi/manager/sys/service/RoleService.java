package com.duobi.manager.sys.service;

import com.duobi.manager.sys.dao.MenuDao;
import com.duobi.manager.sys.dao.RoleDao;
import com.duobi.manager.sys.dao.UserOrganizationDao;
import com.duobi.manager.sys.entity.Menu;
import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.entity.UserOrganization;
import com.duobi.manager.sys.utils.UserUtils;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(value = "serviceTransactionManager",readOnly = true)
public class RoleService extends CrudService<RoleDao, Role, Long> {

    @Autowired
    private UserOrganizationDao userOrganizationDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Role entity) {
        logger.info("organizationList: {}", entity.getOrganizationList());
        super.save(entity);
        //更新角色与菜单关联
        if (entity.getMenuList().size() > 0) {
            dao.deleteRoleMenu(entity);

            //单条菜单的所有父菜单和子菜单都默认被选中
            List<Long> menuIds = new ArrayList<>();
            for (Menu temp:entity.getMenuList()){
                menuIds.add(temp.getId());
            }
//            for (Menu menu:entity.getMenuList()){
//                Menu currentMenu = menuDao.get(menu.getId());
//                //将单条菜单的所有父菜单添加到角色中去
//                String []stringArrayParentIds = currentMenu.getParentIds().split(",");
//                for (int i=0;i<stringArrayParentIds.length;i++){
//                    Long parentId = Long.parseLong(stringArrayParentIds[i]);
//                    if (!menuIds.contains(parentId)) menuIds.add(parentId);
//                }
//                //将单条菜单的子菜单添加到角色中去
//                List<Menu> childMenus = menuDao.findChildMenus(currentMenu);
//                for (int i=0;i<childMenus.size();i++){
//                    Long childMenuId = childMenus.get(i).getId();
//                    if (!menuIds.contains(childMenuId)) menuIds.add(childMenuId);
//                }
//            }
            List<Menu> menuList = new ArrayList<>();
            for(int i=0;i<menuIds.size();i++){
                Menu menu = new Menu();
                menu.setId(menuIds.get(i));
                menuList.add(menu);
            }
            entity.setMenuList(menuList);


            dao.insertRoleMenu(entity);
        }
        //更新角色与部门关联
        if (entity.getDataScope() != null) {
            dao.deleteRoleOrganization(entity);
            if (entity.getDataScope().equals(Role.DATA_SCOPE_CUSTOM) && entity.getOrganizationList().size() > 0) {
                dao.insertRoleOrganization(entity);
            }
        }

        //清除当前用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);//shiro需要授权时，查找缓存没有权限信息，将会再次调用realm的doGetAuthorizationInfo去load用户的权限，再次放入缓存中

        //如果修改了角色,则清除拥有该角色的所有用户缓存
        if (!entity.getIsNewRecord()) {
            List<UserOrganization> userList = userOrganizationDao.findUserListByRoleId(entity.getId());
            for (UserOrganization uo : userList) {
                UserUtils.clearCache(uo);
            }
        }
    }


    @Override
    @Transactional(readOnly = false)
    public void delete(Role entity) {
        //删除角色与菜单关联
        dao.deleteRoleMenu(entity);

        //删除角色与部门关联
        dao.deleteRoleOrganization(entity);

        //删除角色
        super.delete(entity);

        //清除当前用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);

        //如果修改了角色,则清除拥有该角色的所有用户缓存
        if (!entity.getIsNewRecord()) {
            List<UserOrganization> userList = userOrganizationDao.findUserListByRoleId(entity.getId());
            for (UserOrganization uo : userList) {
                UserUtils.clearCache(uo);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Role> getByModuleId(Role role) throws Exception{
        return dao.getByModuleId(role);
    }
}