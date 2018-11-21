package com.duobi.manager.khjf.dao;

import com.duobi.manager.khjf.entity.HandleConfig;
import com.duobi.manager.khjf.entity.PointsType;
import com.duobi.manager.khjf.entity.PointsTypeChange;
import com.duobi.manager.sys.utils.MyBatisDao;
import com.duobi.manager.sys.utils.crud.CrudDao;

import java.util.List;

public interface PointsTypeDao extends CrudDao<PointsType,Long>{
    public List<PointsType> getAllHandyPointsType(PointsType pointsType);

    public PointsTypeChange getValidPointsTypeChangeByPointsTypeId(PointsType pointsType);

    public PointsTypeChange getCurrentAvailableStatusByPointsTypeId(PointsType pointsType);

    public void updatePointsTypeChangeById(PointsTypeChange pointsTypeChange);

    public void updatePointsTypeAvailableById(PointsTypeChange pointsTypeChange);

    public void deletePointsTypeChangeById(PointsTypeChange pointsTypeChange);

    public void deletePointsTypeAvailableById(PointsTypeChange pointsTypeChange);

    public void insertPointsTypeChange(PointsTypeChange pointsTypeChange);

    public void insertPointsTypeAvailable(PointsTypeChange pointsTypeChange);

    public HandleConfig getHandleConfigByPointsTypeId(PointsType pointsType);
}
