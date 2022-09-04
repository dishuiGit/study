package cn.com.ptpress.cdm.schema.csv;

import cn.com.ptpress.cdm.schema.common.CdmColumn;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TableFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import static cn.com.ptpress.cdm.schema.util.JsonUtil.JSON_MAPPER;


public class CsvTableFactory implements TableFactory<CsvTable> {

    @Override
    @SneakyThrows
    public CsvTable create(SchemaPlus schema, String name, Map operand, RelDataType rowType) {
        //schema.add("test", ScalarFunctionImpl.create(MyFunction.class, "test"));
        final String colTypePath = String.valueOf(operand.get("colPath"));
        final List<CdmColumn> columns = JSON_MAPPER.readValue(new File(colTypePath),
                new TypeReference<List<CdmColumn>>() {
                });
        return new CsvTable(name, columns, String.valueOf(operand.get("dataPath")));
    }
}
