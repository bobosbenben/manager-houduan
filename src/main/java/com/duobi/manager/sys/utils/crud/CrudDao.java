package com.duobi.manager.sys.utils.crud;

import com.duobi.manager.sys.utils.base.BaseDao;
import com.duobi.manager.sys.utils.base.DataEntity;

import java.io.Serializable;
import java.util.List;

public interface CrudDao<T extends DataEntity<T,PK>,PK extends Serializable> extends BaseDao {

    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public T get(PK id);

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public T get(T entity);

    /**
     * 查询用户数量
     * @param entity
     * @return
     */
    public Long findCount(T entity);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    public List<T> findList(T entity);

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<T> findAllList(T entity);

    /**
     * 查询所有数据列表
     * @see public List<T> findAllList(T entity)
     * @return
     */
    @Deprecated
    public List<T> findAllList();

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(T entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    public int update(T entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param entity
     * @return
     */
    public int delete(T entity);


}
