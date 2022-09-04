package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.calcite.sql.fun.SqlStdOperatorTable.*;

public class PostgreSqlAggregate extends Aggregate implements IPostgreSqlRel {

    protected PostgreSqlAggregate(RelOptCluster cluster, RelTraitSet traitSet, RelNode input,
                                  ImmutableBitSet groupSet, List<ImmutableBitSet> groupSets,
                                  List<AggregateCall> aggCalls) {
        super(cluster, traitSet, ImmutableList.of(), input, groupSet, groupSets, aggCalls);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields =
                getInput().getRowType().getFieldList().stream().map(RelDataTypeField::getName).collect(Collectors.toList());
        List<String> aggFields = new ArrayList<>();
        final List<String> aggs = aggCalls.stream().map(item -> toPgAggSql(item.getAggregation(), inputFields,
                item.getArgList(), aggFields)).collect(Collectors.toList());
        // group by
        if (!groupSet.isEmpty()) {
            groupSet.asSet().forEach(item -> implementor.addGroup(inputFields.get(item)));
        }
        aggs.forEach(implementor::add);
    }

    /**
     * 转成PG的聚合SQL
     */
    private String toPgAggSql(SqlAggFunction aggregation, List<String> names, List<Integer> args,
                              List<String> aggFields) {
        if (aggregation == COUNT) {
            aggFields.add("COUNT");
            return args.isEmpty() ? "count(*)" : String.format("count(%s)", names.get(args.get(0)));
        } else if (aggregation == MIN) {
            aggFields.add("MIN");
            assert args.size() == 1;
            return String.format("min(%s)", names.get(args.get(0)));
        } else if (aggregation == MAX) {
            aggFields.add("MAX");
            assert args.size() == 1;
            return String.format("max(%s)", names.get(args.get(0)));
        } else if (aggregation == AVG) {
            aggFields.add("AVG");
            assert args.size() == 1;
            return String.format("avg(%s)", names.get(args.get(0)));
        } else if (aggregation == SUM) {
            aggFields.add("SUM");
            assert args.size() == 1;
            return String.format("sum(%s)", names.get(args.get(0)));
        }
        throw new RuntimeException("未知聚合类型");
    }

    @Override
    public Aggregate copy(RelTraitSet traitSet, RelNode input, ImmutableBitSet groupSet,
                          List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
        return new PostgreSqlAggregate(getCluster(), traitSet, input, groupSet, groupSets, aggCalls);
    }

    /**
     * CBO开销
     */
    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }
}