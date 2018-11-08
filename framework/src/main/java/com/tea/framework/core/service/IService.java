package com.tea.framework.core.service;


import com.tea.framework.exception.ServiceException;
import com.tea.framework.metadata.model.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface IService<T> {
    /*
      RequestMapping consumes 决定client的content type
      只有实现类参数列表 上的@RequestBody @RequestParam 起作用
      实现类若需要兼顾browser上传和client上传  则需要@Controller
     */

    /**
     * 插入对象
     */
    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "创建单条-操作")
    T insert(T model) throws ServiceException;

    /**
     * 批量插入 复用insert
     */
    @RequestMapping(path = "/inserts", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "创建批量-操作")
    List<T> inserts(List<T> models) throws ServiceException;

    /**
     * 更新对象
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新-操作")
    boolean update(T model) throws ServiceException;

    /**
     * 删除对象
     * 主键使用identifiedArray会按,逗号分割主键以批量更新
     */
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除-操作")
    boolean delete(T model) throws ServiceException;

    /**
     * 批量删除
     * 循环复用delete方法
     * 若仅根据主键按逗号分割 更新数据相同delete方法即可支持
     * 用次方法目的仅在于复用delete方法更新数据体不同时
     */
    @RequestMapping(path = "/deletes", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除批量-操作")
    boolean deletes(List<T> model) throws ServiceException;

    /**
     * 查询单个对象
     */
    @RequestMapping(path = "/get", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "查询单条-操作")
    T get(T model) throws ServiceException;

    /**
     * 查询分页方法
     */
    @RequestMapping(path = "/page", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "查询分页-操作")
    Page<T> page(T model) throws ServiceException;
}
