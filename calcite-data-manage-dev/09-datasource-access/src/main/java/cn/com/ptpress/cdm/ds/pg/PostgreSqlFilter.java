package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤算子
 *
 * @author jimo
 */
public class PostgreSqlFilter extends Filter implements IPostgreSqlRel {

    private RelDataType rowTypes = getRowType();
    private List<String> fieldNames = PostgreSqlRules.postgreSqlFieldNames(getRowType());


    protected PostgreSqlFilter(RelOptCluster cluster, RelTraitSet traits, RelNode child, RexNode condition) {
        super(cluster, traits, child, condition);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        implementor.add(null, Collections.singletonList(translateMatch(condition)));
    }

    @Override
    public Filter copy(RelTraitSet traitSet, RelNode input, RexNode condition) {
        return new PostgreSqlFilter(getCluster(), traitSet, input, condition);
    }

    /**
     * 翻译并匹配
     */
    private String translateMatch(RexNode condition) {
        final List<String> exp =
                RelOptUtil.disjunctions(condition).stream().map(this::translateAnd).collect(Collectors.toList());
        return exp.size() == 1 ? exp.get(0) : String.join(" OR ", exp);
    }

    /**
     * 翻译and条件
     */
    private String translateAnd(RexNode condition) {
        final List<String> exp =
                RelOptUtil.disjunctions(condition).stream().map(this::translateMatch2).collect(Collectors.toList());
        return String.join(" AND ", exp);
    }

    /**
     * 最终翻译
     */
    private String translateMatch2(RexNode node) {
        switch (node.getKind()) {
            case EQUALS:
                return translateBinary(SqlKind.EQUALS.sql, SqlKind.EQUALS.sql, (RexCall) node);
            case LESS_THAN:
                return translateBinary(SqlKind.LESS_THAN.sql, SqlKind.GREATER_THAN.sql, (RexCall) node);
            case LESS_THAN_OR_EQUAL:
                return translateBinary(SqlKind.LESS_THAN_OR_EQUAL.sql, SqlKind.GREATER_THAN_OR_EQUAL.sql,
                        (RexCall) node);
            case GREATER_THAN:
                return translateBinary(SqlKind.GREATER_THAN.sql, SqlKind.LESS_THAN.sql, (RexCall) node);
            case GREATER_THAN_OR_EQUAL:
                return translateBinary(SqlKind.GREATER_THAN_OR_EQUAL.sql, SqlKind.LESS_THAN_OR_EQUAL.sql,
                        (RexCall) node);
            case NOT_EQUALS:
                return translateBinary(SqlKind.NOT_EQUALS.sql, SqlKind.EQUALS.sql, (RexCall) node);
            case LIKE:
                return translateBinary(SqlKind.LIKE.sql, SqlKind.LIKE.sql, (RexCall) node);
            case OR:
            case AND:
                final List<String> express =
                        RelOptUtil.disjunctions(node).stream().map(this::translateMatch2).collect(Collectors.toList());
                return String.join(" " + node.getKind().sql + " ", express);
            case OTHER_FUNCTION:
                return translate(node);
            default:
                throw new RuntimeException("无法转换 ：" + node);
        }
    }

    /**
     * 转译二元操作符
     */
    private String translateBinary(String op, String rop, RexCall call) {
        final RexNode left = call.operands.get(0);
        final RexNode right = call.operands.get(1);
        String expression = translateBinary(op, left, right);
        if (expression != null) return expression;
        expression = translateBinary(rop, right, left);
        if (expression != null) return expression;
        throw new RuntimeException(String.format("无法转换 op=%s, call=%s", op, call));
    }

    private String translateBinary(String op, RexNode left, RexNode right) {
        switch (left.getKind()) {
            case INPUT_REF:
                final String name = fieldNames.get(((RexInputRef) left).getIndex());
                return translateOp(op, name, right);
            case CAST:
                return translateBinary(op, ((RexCall) left).operands.get(0), right);
            case OTHER_FUNCTION:
                return translateOp(op, left, right);
            default:
                throw new RuntimeException("无法找到该SqlKind：" + left.getKind());
        }
    }

    private String translateOp(String op, RexNode name, RexNode right) {
        return String.format("%s %s %s", translate(name), op, translateRexNode(right, null));
    }

    private String translateOp(String op, String name, RexNode right) {
        return String.format("%s %s %s", name, op, translateRexNode(right, null));
    }

    private String translateRexNode(RexNode right, String name) {
        if (right instanceof RexLiteral) {
            final Object v = literalValue((RexLiteral) right);
            String vs = v.toString();
            if (v instanceof String && name != null) {
                final SqlTypeName typeName = rowTypes.getField(name, true, false).getType().getSqlTypeName();
                if (typeName != SqlTypeName.CHAR) {
                    vs = "'" + vs + "'";
                }
            }
            return vs;
        } else if (right instanceof RexCall) {
            return translate(right);
        }
        return "";
    }

    private Object literalValue(RexLiteral literal) {
//        final Comparable value = RexLiteral.value(literal);
        // TODO
        return literal.getValue();
    }

    /**
     * 通过递归遍历所有节点信息
     */
    private String translate(RexNode rexNode) {
        RexCall call = (RexCall) rexNode;
        List<String> arg = new ArrayList<>();
        for (int i = 0; i < call.operands.size(); i++) {
            final RexNode operand = call.operands.get(i);
            if (operand instanceof RexInputRef) {
                final String fieldName = fieldNames.get(((RexInputRef) operand).getIndex());
                arg.add(fieldName);
            } else if (operand instanceof RexCall) {
                arg.add(translate(operand));
            } else {
                arg.add(((RexLiteral) operand).getValue3().toString());
            }
        }
        return String.join(",", arg);
    }
}
