package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexNode;

import java.util.List;
import java.util.stream.Collectors;

public class PostgreSqlSort extends Sort implements IPostgreSqlRel {

    public PostgreSqlSort(RelOptCluster cluster, RelTraitSet traits, RelNode child, RelCollation collation) {
        super(cluster, traits, child, collation);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<RelDataTypeField> fields = getRowType().getFieldList();
        final List<RelFieldCollation> sortCollation = collation.getFieldCollations();
        final List<String> orders = sortCollation.isEmpty() ? ImmutableList.of() :
                sortCollation.stream().map(f -> {
                    final String name = fields.get(f.getFieldIndex()).getName();
                    return "DESC".equals(f.getDirection().shortString) ?
                            name + " DESC" : name + "ASC";
                }).collect(Collectors.toList());
        implementor.addOrder(orders);
    }

    @Override
    public Sort copy(RelTraitSet traitSet, RelNode newInput, RelCollation newCollation, RexNode offset, RexNode fetch) {
        return new PostgreSqlSort(getCluster(), traitSet, input, collation);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        final RelOptCost cost = super.computeSelfCost(planner, mq);
        return collation.getFieldCollations().isEmpty() ? cost : cost.multiplyBy(0.05);
    }
}
