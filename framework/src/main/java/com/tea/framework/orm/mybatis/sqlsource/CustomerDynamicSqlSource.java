package com.tea.framework.orm.mybatis.sqlsource;

import com.tea.framework.orm.mybatis.constant.Const;
import com.tea.framework.orm.mybatis.dialect.Dialect;
import com.tea.framework.orm.mybatis.utils.MybatisUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.Map;


public class CustomerDynamicSqlSource implements SqlSource {

    private Configuration configuration;

    private SqlNode rootSqlNode;

    private String constType;

    private Dialect dialect;

    private String sortSql;

    public CustomerDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, String constType, Dialect dialect, String sortSql) {
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.constType = constType;
        this.dialect = dialect;
        this.sortSql = sortSql == null ? "" : sortSql;
    }

    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context;
        if (parameterObject != null && parameterObject instanceof Map
                && ((Map) parameterObject).containsKey(Const.ORIGINAL_PARAMETER_OBJECT)) {
            context = new DynamicContext(configuration, ((Map) parameterObject).get(Const.ORIGINAL_PARAMETER_OBJECT));
        } else {
            context = new DynamicContext(configuration, parameterObject);
        }
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());

        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        if (constType.equals(Const.CACHE_KEY_SORT)) {
            sqlSource = new StaticSqlSource(configuration, boundSql.getSql() + sortSql, boundSql.getParameterMappings());
        }
        if (constType.equals(Const.CACHE_KEY_PAGE)) {
            sqlSource = new StaticSqlSource(configuration, dialect.generatePageSQL(boundSql.getSql() + sortSql), MybatisUtils.plusTwoParameterToMapping(configuration, boundSql));
        }
        if (constType.equals(Const.CACHE_KEY_COUNT)) {
            sqlSource = new StaticSqlSource(configuration, dialect.generateCountSQL(boundSql.getSql()), boundSql.getParameterMappings());
        }
//        if (count) {
//            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//            sqlSource = new StaticSqlSource(configuration, dialect.generateCountSQL(boundSql.getSql()), boundSql.getParameterMappings());
//        } else {
//            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//            sqlSource = new StaticSqlSource(configuration, dialect.generatePageSQL(boundSql.getSql() + sortSql), MybatisUtils.plusTwoParameterToMapping(configuration, boundSql));
//        }
        boundSql = sqlSource.getBoundSql(parameterObject);
        // 设置条件参数
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }
}
