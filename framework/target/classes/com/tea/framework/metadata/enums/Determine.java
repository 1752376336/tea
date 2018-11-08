package com.tea.framework.metadata.enums;

public enum Determine {

    NO(0, "否"),
    YES(1, "是"),;
    /**
     * 编号
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;

    Determine(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}