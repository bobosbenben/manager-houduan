package com.duobi.manager.sys.testDao;

import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.crud.CrudDao;

@MyBatisDao
public interface TestDao extends CrudDao<Role, Long> {

    public Role getRoleById(Role role);

}