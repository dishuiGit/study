package cn.com.ptpress.cdm.ds.csv;

import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CsvSchema extends AbstractSchema {
    private Map<String, Table> tableMap = new HashMap<>();

    private String dataFiles;

    public CsvSchema(String dataFile) {
        this.dataFiles = dataFile;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        //获取resources下的每隔csv文件，并为其创建CSV表结构
        for (String dataFile : dataFiles.split(",")) {
            URL url = ClassLoader.getSystemClassLoader().getResource(dataFile);
            assert url != null;
            Source source = Sources.of(url);
            tableMap.put(dataFile.split("\\.")[0], new CsvTable(source));
        }
        return tableMap;
    }
}