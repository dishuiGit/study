package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * pg查询算子接口
 *
 * @author jimo
 */
public interface IPostgreSqlRel extends RelNode {

    /**
     * @param implementor 实现方法
     */
    void implement(IPostgreSqlRel.Implementor implementor);

    /**
     * 获取实例
     */
    Convention CONVENTION = new Convention.Impl("PG", IPostgreSqlRel.class);

    /**
     * 实现类
     */
    class Implementor {
        /**
         * select 字段
         */
        final Map<String, String> selectFields = new LinkedHashMap<>();
        /**
         * where 字段
         */
        final List<String> whereClause = new ArrayList<>();
        /**
         * 其实偏移量
         */
        int offset = 0;
        /**
         * limit字段
         */
        int fetch = -1;
        /**
         * order字段
         */
        final List<String> order = new ArrayList<>();
        /**
         * 聚合字段
         */
        final List<String> agg = new ArrayList<>();
        /**
         * group by字段
         */
        final List<String> group = new ArrayList<>();
        /**
         * 表信息
         */
        RelOptTable table;
        /**
         * pg 表
         */
        PostgreSqlTable postgreSqlTable;

        /**
         * 添加字段
         *
         * @param fields     字段映射
         * @param predicates 过滤条件
         * @since 2.0.0
         */

        public void add(Map<String, String> fields, List<String> predicates) {
            if (fields != null) {
                selectFields.putAll(fields);
            }
            if (predicates != null) {
                whereClause.addAll(predicates);
            }
        }

        /**
         * 添加比较运算符
         *
         * @param aggOp 比较运算符
         */

        public void add(String aggOp) {
            agg.add(aggOp);
        }

        /**
         * 添加聚合
         *
         * @param groupOp 聚合字段
         */

        public void addGroup(String groupOp) {
            group.add(groupOp);
        }

        /**
         * 添加排序列表
         *
         * @param newOrder 排序字段
         */

        public void addOrder(List<String> newOrder) {
            order.addAll(newOrder);
        }

        /**
         * 遍历孩子节点
         *
         * @param ordinal 顺序字段
         * @param input   输入RelNode
         */
        public void visitChild(int ordinal, RelNode input) {
            assert ordinal == 0;
            assert input instanceof IPostgreSqlRel;
            ((IPostgreSqlRel) input).implement(this);
        }
    }
}
