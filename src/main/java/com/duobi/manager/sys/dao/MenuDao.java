package com.duobi.manager.sys.dao;

import com.duobi.manager.sys.entity.Menu;
import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.tree.TreeDao;

import java.util.List;

@MyBatisDao
public interface MenuDao extends TreeDao<Menu, Long> {

    public List<Menu> findByUserId(Menu menu);


    public int updateSort(Menu menu);

    public List<Menu> findChildMenus(Menu menu);

}