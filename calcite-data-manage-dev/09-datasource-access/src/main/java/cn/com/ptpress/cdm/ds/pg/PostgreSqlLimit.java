package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelWriter;
import org.apache.calcite.rel.SingleRel;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;

import java.util.List;

public class PostgreSqlLimit extends SingleRel implements IPostgreSqlRel {
    private RexNode offset;
    private RexNode fetch;

    protected PostgreSqlLimit(RelOptCluster cluster, RelTraitSet traits, RelNode input, RexNode offset, RexNode fetch) {
        super(cluster, traits, input);
        assert getConvention() == input.getConvention();
        this.offset = offset;
        this.fetch = fetch;
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        if (offset != null) {
            implementor.offset = RexLiteral.intValue(offset);
        }
        if (fetch != null) {
            implementor.fetch = RexLiteral.intValue(fetch);
        }
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        return new PostgreSqlLimit(getCluster(), traitSet, sole(inputs), offset, fetch);
    }

    /**
     * 打印节点
     */
    @Override
    public RelWriter explainTerms(RelWriter pw) {
        super.explainTerms(pw);
        pw.itemIf("offset", offset, offset != null);
        pw.itemIf("fetch", fetch, fetch != null);
        return pw;
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return planner.getCostFactory().makeZeroCost();
    }
}
