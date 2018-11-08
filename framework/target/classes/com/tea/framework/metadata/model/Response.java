package com.tea.framework.metadata.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "业务响应-模型")
public class Response<T> implements Serializable {
    /**
     * 接口返回码
     */
    @ApiModelProperty(value = "业务代码", notes = "code值为000000时视为成功,其余均为失败")
    private String code;

    /**
     * 结果对应描述
     */
    @ApiModelProperty(value = "业务提示", notes = "code值为非000000时的失败描述")
    private String message;

    /**
     * 返回结果
     */
    @ApiModelProperty(value = "业务数据体", notes = "code值为000000时的成功响应体")
    private T body;

}
