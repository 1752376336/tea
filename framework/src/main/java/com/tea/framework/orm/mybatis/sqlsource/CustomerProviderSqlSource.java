/*
 * 文  件  名：CustomerProviderSqlSource.java
 * 版         权：Copyright 2014 GSOFT Tech.Co.Ltd.All Rights Reserved.
 * 描         述：
 * 修  改  人：hadoop
 * 修改时间：2015年2月11日
 * 修改内容：新增
 */
package com.tea.framework.orm.mybatis.sqlsource;

import com.tea.framework.orm.mybatis.constant.Const;
import com.tea.framework.orm.mybatis.dialect.Dialect;
import com.tea.framework.orm.mybatis.utils.MybatisUtils;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.Map;


/**
 * @author gj
 * @version 2015年2月11日
 */
public class CustomerProviderSqlSource implements SqlSource {

    private Configuration configuration;

    private ProviderSqlSource providerSqlSource;

    private String constType;

    private Dialect dialect;

    private String sortSql;

    public CustomerProviderSqlSource(Configuration configuration, ProviderSqlSource providerSqlSource, String constType, Dialect dialect, String sortSql) {
        this.configuration = configuration;
        this.providerSqlSource = providerSqlSource;
        this.constType = constType;
        this.dialect = dialect;
        this.sortSql = sortSql == null ? "" : sortSql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = null;
        if (parameterObject instanceof Map && ((Map) parameterObject).containsKey(Const.PROVIDER_OBJECT)) {
            boundSql = providerSqlSource.getBoundSql(((Map) parameterObject).get(Const.PROVIDER_OBJECT));
        } else {
            boundSql = providerSqlSource.getBoundSql(parameterObject);
        }
        if (constType.equals(Const.CACHE_KEY_SORT)) {
            return new BoundSql(configuration, boundSql.getSql() + sortSql, boundSql.getParameterMappings(), parameterObject);
        }
        if (constType.equals(Const.CACHE_KEY_PAGE)) {
            return new BoundSql(configuration, dialect.generatePageSQL(boundSql.getSql() + sortSql), MybatisUtils.plusTwoParameterToMapping(configuration, boundSql), parameterObject);
        }
        if (constType.equals(Const.CACHE_KEY_COUNT)) {
            return new BoundSql(configuration, dialect.generateCountSQL(boundSql.getSql()), boundSql.getParameterMappings(), parameterObject);
        }
        return null;
//        if (count) {
//            return new BoundSql(configuration, dialect.generateCountSQL(boundSql.getSql()) /*parser.getCountSql(boundSql.getSql())*/, boundSql.getParameterMappings(),
//                    parameterObject);
//        } else {
//            return new BoundSql(configuration, dialect.generatePageSQL(boundSql.getSql() + sortSql)/* parser.getPageSql(boundSql.getSql())*/,
//                    MybatisUtils.plusTwoParameterToMapping(configuration, boundSql)
////                    parser.getPageParameterMapping(configuration, boundSql)
//                    , parameterObject);
    }

}
