package com.duobi.manager.sys.utils.crud;

import com.duobi.manager.sys.utils.Page;
import com.duobi.manager.sys.utils.Reflections;
import com.duobi.manager.sys.utils.RequestJson;
import com.duobi.manager.sys.utils.ResponseJson;
import com.duobi.manager.sys.utils.base.BaseController;
import com.duobi.manager.sys.utils.base.BaseEntity;
import com.duobi.manager.sys.utils.base.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public class CrudController<S extends CrudService,T extends DataEntity,PK extends Serializable> extends BaseController {

    @Autowired
    protected S service;

    @RequestMapping(value = "/get")
    public @ResponseBody
    Object get(HttpServletRequest request, @RequestBody RequestJson<T, PK> requestJson) {
        ResponseJson responseJson = new ResponseJson();

        PK id = requestJson.getId();
        if (null != id && Long.valueOf(id.toString()).longValue() > 0) {
            BaseEntity model = service.get(id);
            responseJson.setData(model);
            responseJson.setTotal(1L);
        } else {
            T entity = requestJson.getFilter();
            if (null == entity) {
                Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);
                try {
                    entity = entityClass.getConstructor().newInstance();
                } catch (Exception ex) {
                }
            }
            Page<T> page = requestJson.getPage();
            if (null == page) {
                List<T> entities = service.findList(entity);
                responseJson.setData(entities);
                responseJson.setTotal(Long.valueOf(entities.size()));
            } else {
                Page<T> pageInfo = service.findPage(page, entity);
                responseJson.setData(pageInfo.getList());
                responseJson.setTotal(pageInfo.getCount());
            }
        }
        responseJson.setSuccess(true);
        responseJson.setMsg("获取数据成功!");

        return responseJson;
    }

    @RequestMapping(value = "/create")
    public @ResponseBody Object create(@RequestBody RequestJson<T, PK> requestJson) {
        List<T> entities = requestJson.getEntities();
        ResponseJson responseJson = new ResponseJson();

        service.save(entities);
        responseJson.setSuccess(true);
        responseJson.setMsg("添加数据成功!");
        responseJson.setTotal(0L);

        return responseJson;
    }

    @RequestMapping(value = "/update")
    public @ResponseBody Object update(@RequestBody RequestJson<T, PK> requestJson) {
        List<T> entities = requestJson.getEntities();
        ResponseJson responseJson = new ResponseJson();

        service.save(entities);
        responseJson.setSuccess(true);
        responseJson.setMsg("更新数据成功!");
        responseJson.setTotal(0L);

        return responseJson;
    }

    @RequestMapping(value = "/delete")
    public @ResponseBody Object delete(@RequestBody RequestJson<T, PK> requestJson) {
        List<T> entities = requestJson.getEntities();
        ResponseJson responseJson = new ResponseJson();

        service.delete(entities);
        responseJson.setSuccess(true);
        responseJson.setMsg("删除数据成功!");
        responseJson.setTotal(0L);

        return responseJson;
    }

}
