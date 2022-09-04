package cn.com.ptpress.cdm.stream;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.TableFactory;

import java.util.Map;

/**
 * 创建流式表的工厂
 *
 * @author jimo
 */
public class StreamLogTableFactory implements TableFactory<Table> {

    @Override
    public Table create(SchemaPlus schema, String name, Map<String, Object> operand, RelDataType rowType) {
        return new StreamLogTable();
    }
}
