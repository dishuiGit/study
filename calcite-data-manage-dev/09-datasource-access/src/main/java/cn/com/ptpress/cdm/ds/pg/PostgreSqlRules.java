package cn.com.ptpress.cdm.ds.pg;

import com.google.common.base.Predicate;
import org.apache.calcite.adapter.enumerable.EnumerableLimit;
import org.apache.calcite.plan.*;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.logical.LogicalAggregate;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rel.logical.LogicalProject;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexVisitorImpl;
import org.apache.calcite.sql.validate.SqlValidatorUtil;

import java.util.List;

/**
 * 定义所有自定义的优化规则以及规则的匹配原则
 *
 * @author jimo
 */
public class PostgreSqlRules {

    /**
     * 构造方法
     */
    private PostgreSqlRules() {
    }

    /**
     * 优化规则
     */
    public static final RelOptRule[] RULES = {
            PostgreSqlFilterRule.INSTANCE,
            AbstractPostgreSqlProjectRule.INSTANCE,
            PostgreSqlAggregateRule.INSTANCE,
            PostgreSqlSortRule.INSTANCE,
            PostgreSqlLimitRule.INSTANCE,
    };

    /**
     * 获得所有列名
     *
     * @param rowType 列的类型
     * @return 列名列表
     */
    public static List<String> postgreSqlFieldNames(final RelDataType rowType) {
        return SqlValidatorUtil.uniquify(rowType.getFieldNames(), SqlValidatorUtil.EXPR_SUGGESTER,
                true);
    }

    /**
     * 常量节点转换方法
     */
    static class RexToPostgreSqlTranslator extends RexVisitorImpl<String> {

        /**
         * 输入字段名列表
         */
        private final List<String> inFields;


        /**
         * 构造方法
         *
         * @param inFields 输入字段名称
         */
        protected RexToPostgreSqlTranslator(List<String> inFields) {
            super(true);
            this.inFields = inFields;
        }

        @Override
        public String visitInputRef(RexInputRef inputRef) {
            return inFields.get(inputRef.getIndex());
        }
    }

    /**
     * @author jimo
     */
    abstract static class AbstractPostgreSqlConverterRule extends ConverterRule {
        /**
         * 转换结果
         */
        protected final Convention out;

        /**
         * 构造方法
         *
         * @param clazz       类型class
         * @param description 描述信息
         * @since 2.0.0
         */
        AbstractPostgreSqlConverterRule(Class<? extends RelNode> clazz, String description) {
            this(clazz, relNode -> true, description);
        }

        /**
         * 构造方法
         *
         * @param clazz       类型class
         * @param predicate   谓词
         * @param description 描述信息
         * @param <R>         泛型
         * @since 2.0.0
         */
        <R extends RelNode> AbstractPostgreSqlConverterRule(Class<R> clazz,
                                                            java.util.function.Predicate<? super R> predicate,
                                                            String description) {
            super(clazz, predicate,
                    Convention.NONE, IPostgreSqlRel.CONVENTION, RelFactories.LOGICAL_BUILDER, description);
            this.out = IPostgreSqlRel.CONVENTION;
        }
    }

    /**
     * 列裁剪规则
     */
    private static class AbstractPostgreSqlProjectRule extends AbstractPostgreSqlConverterRule {
        /**
         * 获取实例
         */
        private static final AbstractPostgreSqlProjectRule INSTANCE = new AbstractPostgreSqlProjectRule();

        /**
         * 构造方法
         */
        private AbstractPostgreSqlProjectRule() {
            super(LogicalProject.class, "GMProjectRule");
        }

        /**
         * 判断是否与条件进行转换
         *
         * @param call 匹配的节点
         * @return 返回是否匹配成功
         */
        @Override
        public boolean matches(RelOptRuleCall call) {
            LogicalProject project = call.rel(0);
            for (RexNode e : project.getProjects()) {
                if (!(e instanceof RexInputRef)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 转换
         *
         * @param rel 原有的RelNode
         * @return 经过转换的RelNode
         */
        @Override
        public RelNode convert(RelNode rel) {
            assert rel instanceof LogicalProject;
            final LogicalProject project = (LogicalProject) rel;
            final RelTraitSet traitSet = project.getTraitSet().replace(out);
            return new PostgreSqlProject(
                    project.getCluster(),
                    traitSet,
                    convert(project.getInput(), out),
                    project.getProjects(),
                    project.getRowType());
        }
    }

    /**
     * geomesa 过滤规则
     */
    private static class PostgreSqlFilterRule extends RelOptRule {
        /**
         * 实例
         */
        private static final PostgreSqlFilterRule INSTANCE = new PostgreSqlFilterRule();

        /**
         * 构造方法
         */
        private PostgreSqlFilterRule() {
            super(operand(LogicalFilter.class, operand(PostgreSqlTableScan.class, none())), "PGFilterRule");
        }

        /**
         * 转换节点
         *
         * @param call 表达节点
         */
        @Override
        public void onMatch(RelOptRuleCall call) {
            LogicalFilter filter = call.rel(0);
            if (filter.getTraitSet().contains(Convention.NONE)) {
                final RelNode converted = convert(filter);
                if (converted != null) {
                    call.transformTo(converted);
                }
            }
        }

        /**
         * @param filter filter节点
         * @return 返回转换后的节点
         */
        public RelNode convert(LogicalFilter filter) {
            final RelTraitSet traitSet = filter.getTraitSet().replace(IPostgreSqlRel.CONVENTION);
            return new PostgreSqlFilter(
                    filter.getCluster(),
                    traitSet,
                    convert(filter.getInput(), IPostgreSqlRel.CONVENTION),
                    filter.getCondition());
        }
    }

    /**
     * limit 下推规则
     */
    private static class PostgreSqlLimitRule extends RelOptRule {
        /**
         * 获取实例
         */
        private static final PostgreSqlLimitRule INSTANCE = new PostgreSqlLimitRule();

        /**
         * 构造方法
         */
        private PostgreSqlLimitRule() {
            super(operand(EnumerableLimit.class, operand(PostgreSqlToEnumerableConverter.class, any())), "PGLimitRule");
        }

        /**
         * 转换方法
         *
         * @param limit limit节点
         * @return 转换后的节点
         */
        public RelNode convert(EnumerableLimit limit) {
            final RelTraitSet traitSet = limit.getTraitSet().replace(IPostgreSqlRel.CONVENTION);
            return new PostgreSqlLimit(
                    limit.getCluster(),
                    traitSet,
                    convert(limit.getInput(), IPostgreSqlRel.CONVENTION),
                    limit.offset,
                    limit.fetch);
        }

        /**
         * @see ConverterRule
         */
        @Override
        public void onMatch(RelOptRuleCall call) {
            final EnumerableLimit limit = call.rel(0);
            final RelNode converted = convert(limit);
            if (converted != null) {
                call.transformTo(converted);
            }
        }
    }


    /**
     * geomesa 聚合函数规则
     */
    private static class PostgreSqlAggregateRule extends RelOptRule {
        /**
         * 聚合函数规则实例
         */
        private static final PostgreSqlAggregateRule INSTANCE = new PostgreSqlAggregateRule();

        /**
         * 空构造函数，同时也是匹配规则
         */
        private PostgreSqlAggregateRule() {
            super(operand(LogicalAggregate.class, any()),
                    "AggregateRule");
        }

        /**
         * @param rel 原本rel
         * @return 转换后的RelNode
         * @since 2.0.0
         */

        public RelNode convert(RelNode rel) {
            assert rel instanceof LogicalAggregate;
            final LogicalAggregate agg = (LogicalAggregate) rel;
            final RelTraitSet traitSet =
                    agg.getTraitSet().replace(IPostgreSqlRel.CONVENTION);
            return new PostgreSqlAggregate(
                    rel.getCluster(),
                    traitSet,
                    convert(agg.getInput(), traitSet.simplify()),
                    agg.getGroupSet(),
                    agg.getGroupSets(),
                    agg.getAggCallList());
        }

        /**
         * @see ConverterRule
         */
        @Override
        public void onMatch(RelOptRuleCall call) {
            assert call.rels[0] instanceof LogicalAggregate;
            RelNode rel;
            rel = call.rel(0);
            final RelNode converted = convert(rel);
            if (converted != null) {
                call.transformTo(converted);
            }
        }

    }

    /**
     * geomesa 排序函数规则
     */

    private static class PostgreSqlSortRule extends RelOptRule {
        /**
         * 排序谓词
         */
        private static final Predicate<Sort> SORT_PREDICATE = input -> {
            assert input != null;
            return input.offset == null && input.fetch == null;
        };

        /**
         * 静态实例
         */
        private static final PostgreSqlSortRule INSTANCE = new PostgreSqlSortRule();

        /**
         * 构造方法
         */
        private PostgreSqlSortRule() {
            super(operand(Sort.class, null, SORT_PREDICATE, any()),
                    "CKSortRule");
        }

        /**
         * 排序节点转换
         *
         * @param sort 排序节点
         * @return 转换后的节点
         * @since 2.0.0
         */
        public RelNode convert(Sort sort) {
            final RelTraitSet traitSet =
                    sort.getTraitSet().replace(IPostgreSqlRel.CONVENTION)
                            .replace(sort.getCollation());
            return new PostgreSqlSort(sort.getCluster(), traitSet,
                    convert(sort.getInput(), traitSet.replace(RelCollations.EMPTY)),
                    sort.getCollation());
        }


        /**
         * @see ConverterRule
         */
        @Override
        public void onMatch(RelOptRuleCall call) {
            final Sort sort = call.rel(0);
            final RelNode converted = convert(sort);
            if (converted != null) {
                call.transformTo(converted);
            }
        }
    }
}

