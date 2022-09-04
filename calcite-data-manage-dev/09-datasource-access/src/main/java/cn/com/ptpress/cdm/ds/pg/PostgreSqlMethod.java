package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableMap;
import org.apache.calcite.linq4j.tree.Types;

import java.lang.reflect.Method;
import java.util.List;

/**
 * pg 方法
 *
 * @author jimo
 */
public enum PostgreSqlMethod {

    /**
     * {@link PostgreSqlQueryable#query}
     */
    PGMethod_QUERYABLE_QUERY(PostgreSqlQueryable.class, "query", List.class, List.class,
            Integer.class, Integer.class, List.class, List.class, List.class, List.class);

    public final Method method;

    public static final ImmutableMap<Method, PostgreSqlMethod> MAP;

    static {
        final ImmutableMap.Builder<Method, PostgreSqlMethod> builder = ImmutableMap.builder();
        for (PostgreSqlMethod m : PostgreSqlMethod.values()) {
            builder.put(m.method, m);
        }
        MAP = builder.build();
    }

    PostgreSqlMethod(Class<?> clazz, String methodName, Class<?>... argumentTypes) {
        this.method = Types.lookupMethod(clazz, methodName, argumentTypes);
    }
}
