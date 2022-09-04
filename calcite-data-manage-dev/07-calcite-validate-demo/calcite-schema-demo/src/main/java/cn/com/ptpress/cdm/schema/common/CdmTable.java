package cn.com.ptpress.cdm.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CdmTable extends AbstractTable {
    /**
     * 表名
     */
    private String name;
    /**
     * 表的列
     */
    private List<CdmColumn> columns;

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        List<String> names = new ArrayList<>();
        List<RelDataType> types = new ArrayList<>();
        for (CdmColumn c : columns) {
            names.add(c.getName());
            RelDataType sqlType = typeFactory.createSqlType(SqlTypeName.get(c.getType().toUpperCase()));
            types.add(sqlType);
        }
        return typeFactory.createStructType(Pair.zip(names, types));
    }
}
