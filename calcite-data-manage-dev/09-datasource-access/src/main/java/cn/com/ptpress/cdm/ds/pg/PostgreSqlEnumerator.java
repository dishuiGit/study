package cn.com.ptpress.cdm.ds.pg;

import lombok.SneakyThrows;
import org.apache.calcite.linq4j.Enumerator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 迭代器
 *
 * @author jimo
 */
public class PostgreSqlEnumerator implements Enumerator<Object> {

    private ResultSet rs;
    private List<Map.Entry<String, Class<?>>> fields;
    /**
     * 当前行
     */
    private Object curr;

    public PostgreSqlEnumerator(ResultSet rs, List<Map.Entry<String, Class<?>>> fields) {
        this.rs = rs;
        this.fields = fields;
    }

    @SneakyThrows
    @Override
    public Object current() {
        if (fields.size() == 1) {
            return rs.getObject(1);
        }
        // 解构
        List<Object> row = new ArrayList<>(fields.size());
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            row.add(rs.getObject(i));
        }
        return row;
    }

    @Override
    @SneakyThrows
    public boolean moveNext() {
        return rs.next();
    }

    @SneakyThrows
    @Override
    public void reset() {
        rs.relative(0);
    }

    @Override
    @SneakyThrows
    public void close() {
        rs.getStatement().getConnection().close();
    }
}
