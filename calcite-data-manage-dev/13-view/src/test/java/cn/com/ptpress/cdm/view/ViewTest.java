package cn.com.ptpress.cdm.view;

import cn.com.ptpress.cdm.util.PrintUtil;
import org.junit.jupiter.api.Test;

class ViewTest {

    @Test
    void test() {
        PrintUtil.executeAndPrint("select money from (select money from sale_order order by money limit 10)");
    }

    @Test
    void testView() {
        PrintUtil.executeAndPrint("select * from v_order_1",
                "explain plan for select * from v_order_1");
    }

    @Test
    void testMaterialize() {
        PrintUtil.executeAndPrint("explain plan for select money from sale_order where user_id=1",
                "select money from sale_order where user_id=1",
                "select s.id,p.name from product p join (select id,product_id from sale_order where id=1) as s on s" +
                        ".product_id=p.id",
                "explain plan for select s.id,p.name from product p join (select id,product_id from sale_order where " +
                        "id=1) as s on s.product_id=p.id",
                "select product_id,sum(money) from sale_order where product_id <> 2 group by product_id",
                "explain plan for select product_id,sum(money) from sale_order where product_id <> 2 group by " +
                        "product_id"
        );
    }

    @Test
    void testLattices() {
        PrintUtil.showTables();
        PrintUtil.executeAndPrint("select count(*) from product",
                "explain plan for select count(*) from product",
                "select * from LAT.\"m{0}\"",
                "select * from LAT.\"m{1}\""
        );
        PrintUtil.descTable("LAT", "m{0}");
    }
}