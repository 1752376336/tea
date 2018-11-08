package com.tea.framework.core.service.impl;


import com.tea.framework.core.repository.IRepository;
import com.tea.framework.core.service.IService;
import com.tea.framework.exception.ServiceException;
import com.tea.framework.metadata.consts.ResultCode;
import com.tea.framework.metadata.model.Page;
import com.tea.framework.metadata.model.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.util.List;

@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Throwable.class)
public abstract class GenericService<T> implements IService<T> {

    @Autowired
    private IRepository<T> repository;


    public IRepository<T> getRepository() {
        return repository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public T insert(@RequestBody T model) throws ServiceException {
        try {
            int result = getRepository().insert(model);
            if (result <= 0) {
                throw new ServiceException().code(ResultCode.Default.ERROR_SAVE);
            }
            return model;
        } catch (Exception e) {
            throw new ServiceException(e).code(ResultCode.Default.ERROR_SAVE);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public List<T> inserts(@RequestBody List<T> models) throws ServiceException {
        for (T model : models) {
            this.insert(model);
        }
        return models;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public boolean update(@RequestBody T model) throws ServiceException {
        try {
            int result = getRepository().update(model);
            if (result <= 0)
                return false;
        } catch (Exception e) {
            throw new ServiceException(e).code(ResultCode.Default.ERROR_UPDATE);
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public boolean delete(@RequestBody T model) throws ServiceException {
        try {
            int result = getRepository().delete(model);
            if (result <= 0)
                return false;
        } catch (Exception e) {
            throw new ServiceException(e).code(ResultCode.Default.ERROR_DELETE);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public boolean deletes(@RequestBody List<T> models) throws ServiceException {
        for (T model : models) {
            this.delete(model);
        }
        return true;
    }

    public T get(@RequestBody T model) throws ServiceException {
        try {
            return getRepository().get(model);
        } catch (Exception e) {
            throw new ServiceException(e).code(ResultCode.Default.ERROR_SEARCH);
        }
    }

    public Page<T> page(@RequestBody T model) throws ServiceException {
        try {
            Field pageField = ReflectionUtils.findField(model.getClass(), "page");
            ReflectionUtils.makeAccessible(pageField);
            PageParam page = (PageParam) ReflectionUtils.getField(pageField, model);
            if (page == null) {
                page = new PageParam(10);
            }
            ReflectionUtils.setField(pageField, model, page);
            return getRepository().page(model).toPage();
        } catch (Exception e) {
            throw new ServiceException(e).code(ResultCode.Default.ERROR_SEARCH);
        }
    }
}
