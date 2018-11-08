package com.tea.framework.orm.mybatis.interceptor;

import com.tea.framework.metadata.model.Model;
import com.tea.framework.metadata.model.PageParam;
import com.tea.framework.metadata.model.PageWrapper;
import com.tea.framework.metadata.model.Sort;
import com.tea.framework.orm.mybatis.constant.Const;
import com.tea.framework.orm.mybatis.dialect.Dialect;
import com.tea.framework.orm.mybatis.dialect.DialectFactory;
import com.tea.framework.orm.mybatis.enums.MappingEnum;
import com.tea.framework.orm.mybatis.sqlsource.CustomerDynamicSqlSource;
import com.tea.framework.orm.mybatis.sqlsource.CustomerProviderSqlSource;
import com.tea.framework.orm.mybatis.utils.MetaObjectUtils;
import com.tea.framework.orm.mybatis.utils.MybatisUtils;
import com.tea.framework.utils.CamelCaseUtils;
import com.tea.framework.utils.SecurityUtils;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;


/**
 * MyBatis分页拦截
 *
 * @author gj
 * @version 2015年2月10日
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args =
        {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class ExecutorQueryHandlerInterceptor implements Interceptor {
    final static Logger log = LoggerFactory.getLogger(ExecutorQueryHandlerInterceptor.class);

    Dialect dialect = null;

    public Object doPage(Invocation invocation) throws Throwable {
        log.debug("进入Executor");
        final Object[] args = invocation.getArgs();
        PageParam pageParam = null;// PageContextHolder.get();
        List<Sort> sortsParam = null;
//        if (args[1] instanceof MapperMethod.ParamMap<?>) {
//            MapperMethod.ParamMap<?> map = (MapperMethod.ParamMap<?>) args[1];
//            for (String key : map.keySet()) {
//                if (key.startsWith("param")) {
//                    Object obj = map.get(key);
//                    if (obj instanceof PageParam) {
//                        pageParam = (PageParam) obj;
//                    } else if (args[1] instanceof MapperMethod.ParamMap<?>) {
//                        args[1] = obj;
//                    }
//
//                }
//            }
//        }
        if (args[1] instanceof Model) {
            //Get查询也支持排序

            Field pageField = ReflectionUtils.findField(args[1].getClass(), "page");
            ReflectionUtils.makeAccessible(pageField);
            pageParam = (PageParam) ReflectionUtils.getField(pageField, args[1]);

            Field sortsField = ReflectionUtils.findField(args[1].getClass(), "sorts");
            ReflectionUtils.makeAccessible(sortsField);
            sortsParam = (List<Sort>) ReflectionUtils.getField(sortsField, args[1]);

        }

        MappedStatement ms = (MappedStatement) args[0];
        dialect = DialectFactory.buildDialect(ms.getConfiguration());
        SqlSource sqlSource = ((MappedStatement) args[0]).getSqlSource();
        if (sortsParam != null && !sortsParam.isEmpty()) {
            args[0] = getSortMappedStatement(ms, sqlSource, args[1], sortsParam);
        }


        // 无分页参数
        if (pageParam == null) {
            log.debug("默认查询");
            return invocation.proceed();
        }
        log.debug("分页查询");
        PageWrapper page = new PageWrapper();
        args[2] = RowBounds.DEFAULT;
        // 无需分页
        if (pageParam.getPageSize() == 0) {
            log.debug("全量查询");
            Object result = invocation.proceed();
            page.addAll((List) result);
            page.setPageNo(1);
            page.setPageSize(page.size());
            page.setTotalPage(1);
            page.setTotalCount(page.size());
            return page;
        }

        // 需总数
        args[0] = getCountMappedStatement(ms, sqlSource, args[1]);
        Object resultCount = invocation.proceed();
        int totalCount = ((Integer) ((List) resultCount).get(0));
        log.debug("总条数查询为 {}", totalCount);
        page.setTotalCount(totalCount);
        page.setPageNo(pageParam.getPageNo());
        page.setPageSize(pageParam.getPageSize());
        // 提升效率：count为0不再分页查
        if (page.getTotalCount() == 0) {
            return page;
        }

        // 需分页
        // 便捷查询：pageSize<=0 仅查询count
        if (pageParam.getPageSize() > 0) {
            args[0] = getPageMappedStatement(ms, sqlSource, args[1], sortsParam);
            args[1] = setParameter((MappedStatement) args[0], args[1], pageParam, dialect);
            Object result = invocation.proceed();
            page.addAll((List) result);
            log.debug("分页查询结束 {}", pageParam);
        }
        return page;
    }

    private String getSortSql(List<Sort> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return null;
        }
        String sortSql = null;
        Collections.sort(sorts);
        StringBuilder builder = new StringBuilder();
        builder.append(" order by ".toUpperCase());
        for (Sort sort : sorts) {
            int idx = sort.getProperty().indexOf("\\.");
            String field = null;
            if (idx == -1) {
                field = CamelCaseUtils.toUnderline(sort.getProperty());
            }
            if (idx != -1) {
                String[] fields = sort.getProperty().split("\\.");
                field = fields[0] + "." + CamelCaseUtils.toUnderline(fields[1]);
            }
            builder.append(field).append(" ").append(sort.getDirection().toUpperCase()).append(" ,");
        }

        sortSql = builder.substring(0, builder.length() - 1);
        return sortSql;
    }

    private MappedStatement getSortMappedStatement(MappedStatement ms, SqlSource sqlSource, Object parameterObject, List<Sort> sorts) {

        String sortSql = this.getSortSql(sorts);
        String hash = SecurityUtils.md5(sortSql);
        String mappedStatementId = ms.getId() + "_" + hash + Const.CACHE_KEY_SORT;
        MappedStatement qs = MybatisUtils.getMappedStatement(ms, mappedStatementId);
        if (qs == null) {
            // 创建一个新的MappedStatement
            SqlSource ss = getSqlSource(ms, sqlSource, parameterObject, Const.CACHE_KEY_SORT, sortSql);
            qs = MybatisUtils.createMappedStatement(ms, ss, mappedStatementId, MappingEnum.DEFAULT);
            MybatisUtils.addMappedStatement(ms.getConfiguration(), ms);
        }
        return qs;
    }

    private MappedStatement getPageMappedStatement(MappedStatement ms, SqlSource sqlSource, Object parameterObject, List<Sort> sorts) {
        String mappedStatementId = ms.getId() + Const.CACHE_KEY_PAGE;


        String sortSql = this.getSortSql(sorts);
        if (sortSql != null) {
            String hash = SecurityUtils.md5(sortSql);
            mappedStatementId = ms.getId() + "_" + hash + Const.CACHE_KEY_PAGE;
        }

        MappedStatement qs = MybatisUtils.getMappedStatement(ms, mappedStatementId);
        if (qs == null) {
            // 创建一个新的MappedStatement
            SqlSource ss = getSqlSource(ms, sqlSource, parameterObject, Const.CACHE_KEY_PAGE, sortSql);
            qs = MybatisUtils.createMappedStatement(ms, ss, mappedStatementId, MappingEnum.DEFAULT);
            MybatisUtils.addMappedStatement(ms.getConfiguration(), ms);
        }
        return qs;

    }

    private MappedStatement getCountMappedStatement(MappedStatement ms, SqlSource sqlSource, Object parameterObject) {
        String mappedStatementId = ms.getId() + Const.CACHE_KEY_COUNT;
        MappedStatement qs = MybatisUtils.getMappedStatement(ms, mappedStatementId);
        if (qs == null) {
            // 创建一个新的MappedStatement
            SqlSource ss = getSqlSource(ms, sqlSource, parameterObject, Const.CACHE_KEY_COUNT, null);
            qs = MybatisUtils.createMappedStatement(ms, ss, mappedStatementId, MappingEnum.INT);
            MybatisUtils.addMappedStatement(ms.getConfiguration(), ms);
        }
        return qs;
    }

//    private MappedStatement getMappedStatement(MappedStatement ms, SqlSource sqlSource, Object parameterObject, String cacheSuffixKey, String cacheHash, String sortSql, MappingEnum mappingType) {
//        String mappedStatementId = ms.getId() + "_" + cacheHash + cacheSuffixKey;
//        MappedStatement qs = MybatisUtils.getMappedStatement(ms, mappedStatementId);
//        if (qs == null) {
//            // 创建一个新的MappedStatement
//            SqlSource ss = getSqlSource(ms, sqlSource, parameterObject, cacheSuffixKey == Const.CACHE_KEY_COUNT, sortSql);
//            qs = MybatisUtils.createMappedStatement(ms, ss, mappedStatementId, mappingType);
//            MybatisUtils.addMappedStatement(ms.getConfiguration(), ms);
//        }
//        return qs;
//    }

    private SqlSource getSqlSource(MappedStatement ms, SqlSource sqlSource, Object parameterObject, String constType, String sortSql) {
        if (ms.getSqlSource() instanceof DynamicSqlSource) {
            MetaObject msObject = MetaObjectUtils.forObject(ms);
            SqlNode sqlNode = (SqlNode) msObject.getValue("sqlSource.rootSqlNode");
            MixedSqlNode mixedSqlNode = null;
            if (sqlNode instanceof MixedSqlNode) {
                mixedSqlNode = (MixedSqlNode) sqlNode;
            } else {
                List<SqlNode> contents = new ArrayList<>(1);
                contents.add(sqlNode);
                mixedSqlNode = new MixedSqlNode(contents);
            }
            return new CustomerDynamicSqlSource(ms.getConfiguration(), mixedSqlNode, constType, dialect, sortSql);
        }
        if (sqlSource instanceof ProviderSqlSource) {
            return new CustomerProviderSqlSource(ms.getConfiguration(), (ProviderSqlSource) sqlSource, constType, dialect, sortSql);
        }

        if (constType.equals(Const.CACHE_KEY_SORT)) {
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            return new StaticSqlSource(ms.getConfiguration(), boundSql.getSql() + sortSql == null ? "" : sortSql, boundSql.getParameterMappings());
        }
        if (constType.equals(Const.CACHE_KEY_PAGE)) {
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            return new StaticSqlSource(ms.getConfiguration(), dialect.generatePageSQL(boundSql.getSql()), MybatisUtils.plusTwoParameterToMapping(ms.getConfiguration(), boundSql));
        }
        if (constType.equals(Const.CACHE_KEY_COUNT)) {
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            return new StaticSqlSource(ms.getConfiguration(), dialect.generateCountSQL(boundSql.getSql()), boundSql.getParameterMappings());
        }
        return null;
    }

    public Map setParameter(MappedStatement ms, Object parameterObject, PageParam page, Dialect dialect) {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        Map paramMap = null;
        if (parameterObject == null) {
            paramMap = new HashMap();
        } else if (parameterObject instanceof Map) {
            paramMap = (Map) parameterObject;
        } else {
            paramMap = new HashMap();
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry()
                    .hasTypeHandler(parameterObject.getClass());
            MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
            if (ms.getSqlSource() instanceof CustomerProviderSqlSource) {
                paramMap.put(Const.PROVIDER_OBJECT, parameterObject);
            }
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name = parameterMapping.getProperty();
                    if (!name.equals(Const.PARAMETER_FIRST) && !name.equals(Const.PARAMETER_SECOND)
                            && paramMap.get(name) == null) {
                        if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                            paramMap.put(name, parameterObject);
                            break;
                        }
                    }
                }
            }
        }
        // 备份原始参数对象
        paramMap.put(Const.ORIGINAL_PARAMETER_OBJECT, parameterObject);
        // 设置分页参数
        Map<String, Object> customerPageParams = dialect.getPageParameter(page);
        if (customerPageParams != null) {
            for (String key : customerPageParams.keySet()) {
                paramMap.put(key, customerPageParams.get(key));
            }
        }
        return paramMap;
    }

    public Object intercept(Invocation invocation) throws Throwable {
//        try
//        {
        return doPage(invocation);
//        }
//        finally
//        {
//            PageContextHolder.clear();
//        }
    }

    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties p) {
    }
}
