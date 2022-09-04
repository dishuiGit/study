import cn.com.ptpress.cdm.ds.csv.CsvSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.RelBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

class MyRelBuilder {

    /**
     * select
     *  *
     * from
     *  (
     *      select
     *        *
     *      from
     *        student
     *        join score on student.id = score.id
     *  ) t1
     * join
     *  (
     *      select
     *              *
     *            from
     *              school
     *              join city on school.id = city.id
     *  ) t2
     * on t1.id = t2.id
     */

    @Test
    public void joinTest() {
        final FrameworkConfig config = MyRelBuilder.config().build();
        final RelBuilder builder = RelBuilder.create(config);
        final RelNode left = builder
                .scan("STUDENG")
                .scan("SCORE")
                .join(JoinRelType.INNER, "ID")
                .build();

        final RelNode right = builder
                .scan("CITY")
                .scan("SCHOOL")
                .join(JoinRelType.INNER, "ID")
                .build();

        final RelNode result = builder
                .push(left)
                .push(right)
                .join(JoinRelType.INNER, "ID")
                .build();
        System.out.println(RelOptUtil.toString(result));
    }

    /**
     * select * from data where score > 90
     */
    @Test
    public void projectWithFilterTest() {
        final FrameworkConfig config = MyRelBuilder.config().build();
        final RelBuilder builder = RelBuilder.create(config);
        final RelNode node = builder
                .scan("data")
                .project(builder.field("Name"), builder.field("Score"))
                .filter(builder.call(SqlStdOperatorTable.GREATER_THAN,
                        builder.field("Score"),
                        builder.literal(90)))
                .build();
        System.out.println(RelOptUtil.toString(node));
    }

    /**
     * select * from data
     */
    @Test
    public void scanTest() {
        final FrameworkConfig config = MyRelBuilder.config().build();
        final RelBuilder builder = RelBuilder.create(config);
        final RelNode node = builder
                .scan("data")
                .build();
        System.out.println(RelOptUtil.toString(node));
    }

    public static Frameworks.ConfigBuilder config() {
        final SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        return Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(rootSchema.add("csv", new CsvSchema("data.csv")))
                .traitDefs((List<RelTraitDef>) null);
    }
}
