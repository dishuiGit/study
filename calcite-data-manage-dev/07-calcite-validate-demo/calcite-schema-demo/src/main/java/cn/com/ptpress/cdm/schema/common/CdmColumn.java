package cn.com.ptpress.cdm.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.calcite.sql.type.SqlTypeName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdmColumn {
    /**
     * 列名
     */
    private String name;
    /**
     * 列类型,可以使用calcite扩展的sql类型：{@link SqlTypeName}
     */
    private String type;
}
