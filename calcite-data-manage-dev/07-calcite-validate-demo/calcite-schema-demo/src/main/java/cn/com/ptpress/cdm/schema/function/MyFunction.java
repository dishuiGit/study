package cn.com.ptpress.cdm.schema.function;

public class MyFunction {
    public MyFunction() {
    }

    /**
     * 计算1的个数
     */
    public int myLen(int permission) {
        int c = 0;
        for (; permission > 0; c++) {
            permission &= (permission - 1);
        }
        return c;
    }

    public String test(String in) {
        return "hh";
    }
}
