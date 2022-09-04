package cn.com.ptpress.cdm.ds.pg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * model
 *
 * @author jimo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostgreSqlInfo {

    private String url;
    private String user;
    private String password;
}
