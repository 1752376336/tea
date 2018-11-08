package com.tea.framework.metadata.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tea.framework.metadata.annotation.Primary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
public abstract class Model implements Serializable {

    /**
     * 分页接受参数实例
     *
     *    {
     *       "messageReceiveId":2,
     *        "page":{
     *            "pageNo":1,
     *            "pageSize":3
     *        },
     *        "sorts":[
     *            {"index":1,"property":"OutsideMessageReceive.operate_time","direction":"desc"},
     *            {"index":0,"property":"message_config_id","direction":"desc"}
     *        ]
     *    }
     *
     */

    /**
     * 索引参数
     */
    @ApiModelProperty(value = "索引参数")
    @JsonIgnore
    private Index index;
    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数")
    private PageParam page;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序参数")
    public List<Sort> sorts;

    @JsonIgnore
    @JSONField(serialize = false)
    public String getIdentified() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Primary primary = field.getAnnotation(Primary.class);
            if (primary != null) {
                field.setAccessible(true);
                try {
                    return (String) field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public List<String> getIdentifiedArray() {
        if (this.getIdentified() == null) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(this.getIdentified().split(",")));
    }


}
