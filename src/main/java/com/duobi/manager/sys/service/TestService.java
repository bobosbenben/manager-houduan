package com.duobi.manager.sys.service;

import com.duobi.manager.sys.testDao.TestDao;
import com.duobi.manager.sys.entity.Role;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class TestService extends CrudService<TestDao, Role, Long> {

    @Transactional(readOnly = true)
    public Role getRoleById(Role role) throws Exception{
        return dao.getRoleById(role);
    }

    @Transactional(value = "secondTransactionManager",readOnly = true)
    public void insertOne(Role role) throws Exception{
        dao.insert(role);
        Role role1 = null;
        role1.getEnname();
    }
}