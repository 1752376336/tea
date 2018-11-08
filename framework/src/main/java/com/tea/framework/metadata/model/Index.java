package com.tea.framework.metadata.model;

import lombok.Data;

@Data
public class Index {

    private String clusterName;//集群名称

    private String indexName;//索引名称 op_logger_v1

    private String typeName;//根据indexName处理后赋值


}
