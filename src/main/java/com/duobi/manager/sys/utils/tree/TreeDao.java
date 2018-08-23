package com.duobi.manager.sys.utils.tree;

import com.duobi.manager.sys.utils.crud.CrudDao;
import com.duobi.manager.sys.utils.persistence.TreeEntity;

import java.io.Serializable;
import java.util.List;

public interface TreeDao<T extends TreeEntity<T, PK>, PK extends Serializable> extends CrudDao<T, PK> {

    /**
     * 找到所有子节点
     * @param entity
     * @return
     */
    public List<T> findByParentId(T entity);

    /**
     * 找到所有子节点
     * @param entity
     * @return
     */
    public List<T> findByParentIdsLike(T entity);

    /**
     * 更新所有父节点字段
     * @param entity
     * @return
     */
    public int updateParentIds(T entity);

}
