package cn.com.ptpress.cdm.ds.csv;

import cn.com.ptpress.cdm.ds.csv.udx.UDAF;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AggregateFunctionImpl;

import java.util.Map;

public class CsvSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        //注册 udf
        // parentSchema.add("EXAMPLE", ScalarFunctionImpl.create(UDF.class, "eval"));
        //注册 udaf
        parentSchema.add("COLLECT_LIST", AggregateFunctionImpl.create(UDAF.class));
        return new CsvSchema(operand.get("dataFile").toString());
    }
}
