package cn.com.ptpress.cdm.schema.csv;

import cn.com.ptpress.cdm.schema.common.CdmColumn;
import cn.com.ptpress.cdm.schema.common.CdmTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CsvTable extends CdmTable {
    /**
     * 数据路径
     */
    private String dataPath;

    public CsvTable(String name, List<CdmColumn> columns, String dataPath) {
        super(name, columns);
        this.dataPath = dataPath;
    }
}
