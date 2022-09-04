package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.adapter.enumerable.*;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.linq4j.tree.MethodCallExpression;
import org.apache.calcite.plan.*;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterImpl;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.runtime.Hook;
import org.apache.calcite.util.BuiltInMethod;
import org.apache.calcite.util.Pair;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现pg迭代器
 *
 * @author jimo
 */
public class PostgreSqlToEnumerableConverter extends ConverterImpl implements EnumerableRel {

    PostgreSqlToEnumerableConverter(RelOptCluster cluster, RelTraitSet traitSet, RelNode child) {
        super(cluster, ConventionTraitDef.INSTANCE, traitSet, child);
    }

    /**
     * 形成表达式树
     */
    @Override
    public Result implement(EnumerableRelImplementor implementor, Prefer pref) {
        final BlockBuilder list = new BlockBuilder();
        final IPostgreSqlRel.Implementor pgImplementor = new IPostgreSqlRel.Implementor();
        pgImplementor.visitChild(0, getInput());

        final RelDataType rowType = getRowType();
        final PhysType physType = PhysTypeImpl.of(implementor.getTypeFactory(), rowType,
                pref.prefer(JavaRowFormat.ARRAY));
        final Expression fields = list.append("FIELDS",
                constantArrayList(Pair.zip(PostgreSqlRules.postgreSqlFieldNames(rowType),
                        new AbstractList<Class<?>>() {
                            @Override
                            public Class<?> get(int index) {
                                return physType.fieldClass(index);
                            }

                            @Override
                            public int size() {
                                return rowType.getFieldCount();
                            }
                        }), Pair.class));
        List<Map.Entry<String, String>> selectList = new ArrayList<>();
        Pair.zip(pgImplementor.selectFields.keySet(), pgImplementor.selectFields.values()).forEach(selectList::add);
        final Expression table = list.append("TABLE", pgImplementor.table.getExpression(PostgreSqlQueryable.class));
        final Expression selectFields = list.append("SELECT_FIELDS", constantArrayList(selectList, Pair.class));
        final Expression predicates = list.append("PREDICATES", constantArrayList(pgImplementor.whereClause,
                String.class));
        final Expression offset = list.append("OFFSET", Expressions.constant(pgImplementor.offset));
        final Expression fetch = list.append("FETCH", Expressions.constant(pgImplementor.fetch));
        final Expression order = list.append("ORDER", constantArrayList(pgImplementor.order, String.class));
        final Expression aggregate = list.append("AGGREGATE", constantArrayList(pgImplementor.agg, String.class));
        final Expression group = list.append("GROUP", constantArrayList(pgImplementor.group, String.class));
        final Expression enumerable = list.append("ENUMERABLE", Expressions.call(table,
                PostgreSqlMethod.PGMethod_QUERYABLE_QUERY.method,
                fields, selectFields, offset, fetch, aggregate, group, predicates, order));
        list.add(Expressions.return_(null, enumerable));
        Hook.QUERY_PLAN.run(predicates);
        return implementor.result(physType, list.toBlock());
    }

    /**
     * 将list转为表达式方法
     */
    private <T> MethodCallExpression constantArrayList(List<T> values, Class<?> clazz) {
        return Expressions.call(BuiltInMethod.ARRAYS_AS_LIST.method, Expressions.newArrayInit(clazz,
                constantList(values)));
    }

    /**
     * 将list转为常量表达式
     */
    private <T> List<? extends Expression> constantList(List<T> values) {
        return values.stream().map(Expressions::constant).collect(Collectors.toList());
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        return new PostgreSqlToEnumerableConverter(getCluster(), traitSet, sole(inputs));
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }
}
