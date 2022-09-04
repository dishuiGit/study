package csv;

import csv.udx.UDAF;
import csv.udx.UDTF;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AggregateFunctionImpl;
import org.apache.calcite.schema.impl.TableFunctionImpl;

import java.util.Map;

public class CsvSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        //注册 udf
        //parentSchema.add("EXAMPLE", ScalarFunctionImpl.create(UDF.class,"eval"));
        //注册 udaf
        parentSchema.add("COLLECT_LIST", AggregateFunctionImpl.create(UDAF.class));
        parentSchema.add("EXPLODE",TableFunctionImpl.create(UDTF.UDTF_METHOD));
        return new CsvSchema(operand.get("dataFile").toString());
    }
}
