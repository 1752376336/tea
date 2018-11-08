package com.tea.service.user.repository;


import com.tea.api.user.model.User;
import com.tea.framework.core.repository.IRepository;
import com.tea.framework.metadata.model.PageWrapper;
import org.apache.poi.ss.formula.functions.T;

public interface IUserRepository extends IRepository<User>{
    /**
     * 查询分页
     */
    PageWrapper<T> page(T model);
}
