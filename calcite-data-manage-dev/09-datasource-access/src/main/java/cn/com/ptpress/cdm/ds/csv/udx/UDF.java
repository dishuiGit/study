package cn.com.ptpress.cdm.ds.csv.udx;

import org.apache.calcite.linq4j.function.Parameter;

public class UDF {
    /**
     * 获取首字母
     * @param str 字符串
     * @return
     */
    public String subString(String str){
        return str.substring(0,2);
    }

    public String subString2(
            @Parameter(name = "S") String s,
            @Parameter(name = "N", optional = true) Integer n) {
        return s.substring(0, n == null ? 1 : n);
    }
}
