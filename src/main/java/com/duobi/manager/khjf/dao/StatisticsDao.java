package com.duobi.manager.khjf.dao;

import com.duobi.manager.khjf.entity.Statistics;
import com.duobi.manager.sys.utils.crud.CrudDao;

import java.util.List;

public interface StatisticsDao extends CrudDao<Statistics,Long> {

    List<Statistics> getOrgConsumedPointsValue();

}
