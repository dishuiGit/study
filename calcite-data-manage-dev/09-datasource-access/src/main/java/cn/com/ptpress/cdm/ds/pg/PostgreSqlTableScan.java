package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.*;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.type.RelDataType;

import java.util.List;

/**
 * 扫描表
 *
 * @author jimo
 */
public class PostgreSqlTableScan extends TableScan implements IPostgreSqlRel {
    private PostgreSqlTable postgreSqlTable;
    private RelDataType projectRowType;

    protected PostgreSqlTableScan(PostgreSqlTable postgreSqlTable, RelDataType projectRowType, RelOptCluster cluster,
                                  RelTraitSet traitSet, RelOptTable table) {
        super(cluster, traitSet, ImmutableList.of(), table);
        this.postgreSqlTable = postgreSqlTable;
        this.projectRowType = projectRowType;
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.postgreSqlTable = postgreSqlTable;
        implementor.table = table;
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        assert inputs.isEmpty();
        return this;
    }

    @Override
    public RelDataType deriveRowType() {
        return projectRowType == null ? super.deriveRowType() : projectRowType;
    }

    @Override
    public void register(RelOptPlanner planner) {
        planner.addRule(PostgreSqlToEnumerableConverterRule.INSTANCE);
        for (RelOptRule rule : PostgreSqlRules.RULES) {
            planner.addRule(rule);
        }
    }
}
