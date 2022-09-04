package cn.com.ptpress.cdm.parser.select;

public class TestParser {

    public static void main(String[] args) throws ParseException {
        parseSelect("select 1+1");
        parseSelect("select 1+1+1");
        parseSelect("select 1 + 3 - 5");
    }

    private static void parseSelect(String sql) throws ParseException {
        final SimpleSelectParser parser = new SimpleSelectParser(sql);
        parser.parse();
    }
}
