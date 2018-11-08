package com.tea.framework.metadata.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 序列化Page - 分页对象参数
 *
 * @author 龚健
 */
@ApiModel(description = "分页响应-模型")
@Data
public class Page<E> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序集合")
    public List<Sort> sorts;
    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数")
    protected int totalCount;
    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    protected int totalPage;
    @ApiModelProperty(value = "数据集", notes = "内部结构见相关类型")
    private List<E> results;
    /**
     * 页码 从1开始
     */
    @ApiModelProperty(value = "页码", notes = "默认为第1页")
    private int pageNo;
    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", notes = "默认为10条")
    private int pageSize;
    /**
     * 起始行
     */
    @ApiModelProperty(value = "起始行数")
    private int startRow;
    /**
     * 末行
     */
    @ApiModelProperty(value = "结束行数")
    private int endRow;
    /**
     * 上一页
     */
    @ApiModelProperty(value = "上一页 页码")
    private int prevPage;
    /**
     * 下一页
     */
    @ApiModelProperty(value = "下一页 页码")
    private int nextPage;
    /**
     * 是否为第一页
     */
    @ApiModelProperty(value = "是否为第一页")
    private boolean isFirstPage;
    /**
     * 是否为最后一页
     */
    @ApiModelProperty(value = "是否为最后一页")
    private boolean isLastPage;
    /**
     * 是否有前一页
     */
    @ApiModelProperty(value = "是否有上一页")
    private boolean hasPrevPage;
    /**
     * 是否有下一页
     */
    @ApiModelProperty(value = "是否有下一页")
    private boolean hasNextPage;


}
