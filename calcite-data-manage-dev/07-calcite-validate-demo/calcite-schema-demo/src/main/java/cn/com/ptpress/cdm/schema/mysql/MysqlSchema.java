package cn.com.ptpress.cdm.schema.mysql;

import cn.com.ptpress.cdm.schema.common.CdmTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MysqlSchema extends AbstractSchema {

    private String name;
    private List<CdmTable> tables;

    @Override
    protected Map<String, org.apache.calcite.schema.Table> getTableMap() {
        return tables.stream().collect(Collectors.toMap(CdmTable::getName, t -> t));
    }
}
