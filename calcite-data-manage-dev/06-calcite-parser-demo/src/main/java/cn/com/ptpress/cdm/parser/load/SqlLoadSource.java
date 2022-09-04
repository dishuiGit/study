package cn.com.ptpress.cdm.parser.load;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.calcite.sql.SqlIdentifier;

@Data
@AllArgsConstructor
public class SqlLoadSource {
    private SqlIdentifier type;
    private String obj;
}
