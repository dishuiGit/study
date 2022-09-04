package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 列裁剪算子
 *
 * @author jimo
 */
public class PostgreSqlProject extends Project implements IPostgreSqlRel {

    protected PostgreSqlProject(RelOptCluster cluster, RelTraitSet traits, RelNode input, List<?
            extends RexNode> projects, RelDataType rowType) {
        super(cluster, traits, ImmutableList.of(), input, projects, rowType);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        assert getCluster().getTypeFactory() instanceof JavaTypeFactory;
        final PostgreSqlRules.RexToPostgreSqlTranslator translator =
                new PostgreSqlRules.RexToPostgreSqlTranslator(PostgreSqlRules.postgreSqlFieldNames(getInput().getRowType()));
        final LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        getNamedProjects().forEach(f -> fields.put(f.left.accept(translator), f.right));
        implementor.add(fields, null);
    }

    @Override
    public Project copy(RelTraitSet traitSet, RelNode input, List<RexNode> projects, RelDataType rowType) {
        return new PostgreSqlProject(getCluster(), traitSet, input, projects, rowType);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }
}
