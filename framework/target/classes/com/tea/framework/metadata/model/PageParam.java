package com.tea.framework.metadata.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端 - 分页对象参数
 *
 * @author 龚健
 */
@ApiModel(description = "分页参数-模型")
@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码 从1开始
     */
    @ApiModelProperty(value = "页码", notes = "默认为第1页")
    private int pageNo = 1;
    /**
     * 每页条数 从10开始
     */
    @ApiModelProperty(value = "每页条数", notes = "默认为10条")
    private int pageSize = 10;

    public PageParam() {
        super();
    }

    public PageParam(int pageSize) {
        this.pageSize = pageSize;
    }


}
