package csv.udx;

import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.BaseQueryable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.linq4j.tree.Types;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.SchemaPlus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UDTF {
    public static final Method UDTF_METHOD =
            Types.lookupMethod(UDTF.class, "explode", String.class, String.class);

    public static QueryableTable explode(final String str, final String regex) {
        return new AbstractQueryableTable(String.class) {

            public RelDataType getRowType(RelDataTypeFactory typeFactory) {
                Arrays.asList(typeFactory.createJavaType(String.class));
                Arrays.asList("c");
                return typeFactory.createStructType(Arrays.asList(typeFactory.createJavaType(String.class)),
                        Arrays.asList("c"));
            }

            public <T> Queryable<T> asQueryable(QueryProvider queryProvider,
                                                SchemaPlus schema, String tableName) {

                BaseQueryable<String> queryable =
                        new BaseQueryable<String>(null, String.class, null) {
                            public Enumerator<String> enumerator() {
                                return new Enumerator<String>() {
                                    int i = -1;
                                    String[] res = null;

                                    public String current() {
                                        return res[i];
                                    }

                                    public boolean moveNext() {
                                        if (i == -1) {
                                            res = str.split(regex);
                                        }
                                        if (i < res.length - 1) {
                                            i++;
                                            return true;
                                        } else
                                            return false;
                                    }

                                    public void reset() {
                                        i = -1;
                                    }

                                    public void close() {
                                    }
                                };
                            }
                        };
                //noinspection unchecked
                return (Queryable<T>) queryable;
            }
        };
    }

}
