import cn.com.ptpress.cdm.optimization.RelBuilder.Utils.SqlToRelNode;
import cn.com.ptpress.cdm.optimization.RelBuilder.optimizer.CSVProjectRule;
import cn.com.ptpress.cdm.optimization.RelBuilder.optimizer.CSVProjectRuleWithCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.rules.FilterJoinRule;
import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.jupiter.api.Test;


class PlannerTest {
    @Test
    public void testCustomRule() throws SqlParseException {
        final String sql = "select Id from data ";
        HepProgramBuilder programBuilder = HepProgram.builder();
        HepPlanner hepPlanner =
                new HepPlanner(
                        programBuilder.addRuleInstance(CSVProjectRule.Config.DEFAULT.toRule())
                                .addRuleInstance(CSVProjectRuleWithCost.Config.DEFAULT.toRule())
                                .build());
        RelNode relNode = SqlToRelNode.getSqlNode(sql, hepPlanner);
        //未优化算子树结构
        System.out.println(RelOptUtil.toString(relNode));
        RelOptPlanner planner = relNode.getCluster().getPlanner();
        planner.setRoot(relNode);
        RelNode bestExp = planner.findBestExp();
        //优化后接结果
        System.out.println("===========RBO优化结果============");
        System.out.println(RelOptUtil.toString(bestExp));
        RelOptPlanner relOptPlanner = relNode.getCluster().getPlanner();
        relOptPlanner.addRule(CSVProjectRule.Config.DEFAULT.toRule());
        relOptPlanner.addRule(CSVProjectRuleWithCost.Config.DEFAULT.toRule());
        relOptPlanner.setRoot(relNode);
        RelNode exp = relOptPlanner.findBestExp();
        System.out.println("===========CBO优化结果============");
        System.out.println(RelOptUtil.toString(exp));


    }

    @Test
    public void testHepPlanner() throws SqlParseException {
        final String sql = "select a.Id from data as a  join data b on a.Id = b.Id where a.Id>1";
        HepProgramBuilder programBuilder = HepProgram.builder();
        HepPlanner hepPlanner =
                new HepPlanner(
                        programBuilder.addRuleInstance(FilterJoinRule.FilterIntoJoinRule.Config.DEFAULT.toRule())
                                .build());
        RelNode relNode = SqlToRelNode.getSqlNode(sql, hepPlanner);
        //未优化算子树结构
        System.out.println(RelOptUtil.toString(relNode));
        RelOptPlanner planner = relNode.getCluster().getPlanner();
        planner.setRoot(relNode);
        RelNode bestExp = planner.findBestExp();
        //优化后接结果
        System.out.println(RelOptUtil.toString(bestExp));

    }

    @Test
    public void testGraph() throws SqlParseException {
        final String sql = "select * from data where Id=1";
        HepProgramBuilder programBuilder = HepProgram.builder();
        HepPlanner hepPlanner =
                new HepPlanner(
                        programBuilder.build());
        RelNode relNode = SqlToRelNode.getSqlNode(sql, hepPlanner);
        //未转化Dag算子树结构
        System.out.println(RelOptUtil.toString(relNode));
        //转化为Dag图
        hepPlanner.setRoot(relNode);
        //查看需要把log4j.properties级别改为trace

    }
}
