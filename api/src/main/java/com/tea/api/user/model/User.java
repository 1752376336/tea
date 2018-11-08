package com.tea.api.user.model;

import com.tea.framework.metadata.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.tea.framework.metadata.annotation.Primary;
import lombok.Data;

@Data
@ApiModel(description = "用户-模型")
public class User extends Model {
    @Primary
    @ApiModelProperty(name = "用户ID")
    private Integer id;

    @ApiModelProperty(name = "用户名")
    private String uname;


}
