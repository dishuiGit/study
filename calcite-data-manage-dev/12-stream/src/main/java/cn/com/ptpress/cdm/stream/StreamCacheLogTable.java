package cn.com.ptpress.cdm.stream;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.schema.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 带有缓存的日志流表
 *
 * @author jimo
 */
public class StreamCacheLogTable extends StreamLogTable {

    private static List<Object[]> CACHE = new ArrayList<>();

    public StreamCacheLogTable() {
        new Thread(() -> {
            Random r = new Random();
            String[] LEVEL = {"ERROR", "WARN", "INFO", "DEBUG"};
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String level = LEVEL[r.nextInt(LEVEL.length)];
                final long time = System.currentTimeMillis();
                final Object[] row = {
                        time, level, String.format("This is a %s msg on %s", level, time)
                };
                CACHE.add(row);
            }
        }).start();
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return Linq4j.asEnumerable(CACHE);
    }

    @Override
    public Table stream() {
        return this;
    }
}
