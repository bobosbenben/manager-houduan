package com.duobi.manager.sys.dao;

import com.duobi.manager.sys.entity.Organization;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.tree.TreeDao;

@MyBatisDao
public interface OrganizationDao extends TreeDao<Organization, Long> {


}