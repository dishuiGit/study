package cn.com.ptpress.cdm.stream;

import lombok.SneakyThrows;
import org.apache.calcite.DataContext;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.*;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 模拟日志流数据表，不断随机产生日志
 *
 * @author jimo
 */
public class StreamLogTable implements ScannableTable, StreamableTable {

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return Linq4j.asEnumerable(() -> new Iterator<Object[]>() {
            private Random r = new Random();
            private String[] LEVEL = {"ERROR", "WARN", "INFO", "DEBUG"};

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            @SneakyThrows
            public Object[] next() {
                TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
                final String level = LEVEL[r.nextInt(LEVEL.length)];
                final long time = System.currentTimeMillis();
                return new Object[]{
                        time, level, String.format("This is a %s msg on %s", level, time)
                };
            }
        });
    }

    @Override
    public Table stream() {
        return this;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return typeFactory.builder()
                .add("LOG_TIME", SqlTypeName.TIMESTAMP)
                .add("LEVEL", SqlTypeName.VARCHAR)
                .add("MSG", SqlTypeName.VARCHAR)
                .build();
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.of(100d, new ArrayList<>(1), RelCollations.createSingleton(0));
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.TABLE;
    }

    @Override
    public boolean isRolledUp(String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, SqlNode parent,
                                                CalciteConnectionConfig config) {
        return false;
    }
}
