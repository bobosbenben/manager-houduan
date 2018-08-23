package com.duobi.manager.sys.dao;

import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.crud.CrudDao;

import java.util.List;

@MyBatisDao
public interface RoleDao extends CrudDao<Role, Long> {
    public Role getByName(Role role);

    public Role getByEnname(Role role);

    /**
     * 维护角色与菜单权限关系
     * @param role
     * @return
     */
    public int deleteRoleMenu(Role role);

    public int insertRoleMenu(Role role);

    /**
     * 维护角色与公司部门关系
     * @param role
     * @return
     */
    public int deleteRoleOrganization(Role role);

    public int insertRoleOrganization(Role role);

    public List<Role> getByModuleId(Role role) throws Exception;
}
