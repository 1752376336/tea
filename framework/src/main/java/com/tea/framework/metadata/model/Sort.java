package com.tea.framework.metadata.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "排序-模型")
@NoArgsConstructor
@AllArgsConstructor
public class Sort implements Comparable<Sort> {

    @ApiModelProperty(value = "排序字段 field", notes = "字段名称")
    private String property;

    @ApiModelProperty(value = "排序类型 asc|desc", notes = "升序或降序")
    private String direction;

    @ApiModelProperty(value = "排序顺序 0...n", notes = "字段顺序")
    private Integer index;

    @Override
    public int compareTo(Sort o) {
        if (this.index == null || o.index == null) {
            return 0;
        }
        if (this.index > o.index) {
            return 1;
        }
        if (this.index < o.index) {
            return -1;
        }
        return 0;
    }
}
