package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.tools.RelBuilderFactory;

import java.util.function.Predicate;

/**
 * pg到迭代器的转换规则
 *
 * @author jimo
 */
public class PostgreSqlToEnumerableConverterRule extends ConverterRule {

    public static final ConverterRule INSTANCE = new PostgreSqlToEnumerableConverterRule(RelFactories.LOGICAL_BUILDER);

    protected PostgreSqlToEnumerableConverterRule(RelBuilderFactory relBuilderFactory) {
        // TODO
        super(RelNode.class, (Predicate<? super RelNode>) relNode -> true, IPostgreSqlRel.CONVENTION,
                EnumerableConvention.INSTANCE, relBuilderFactory,
                "PostgreSqlToEnumerableConverterRule");
    }

    @Override
    public RelNode convert(RelNode rel) {
        final RelTraitSet newTraits = rel.getTraitSet().replace(getOutConvention());
        return new PostgreSqlToEnumerableConverter(rel.getCluster(), newTraits, rel);
    }
}
