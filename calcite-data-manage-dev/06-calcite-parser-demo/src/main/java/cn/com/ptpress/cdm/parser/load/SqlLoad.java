package cn.com.ptpress.cdm.parser.load;

import lombok.Getter;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;

@Getter
public class SqlLoad extends SqlCall {

    private SqlLoadSource source;
    private SqlLoadSource target;
    private SqlNodeList colMapping;
    private String separator;

    public SqlLoad(SqlParserPos pos) {
        super(pos);
    }

    public SqlLoad(SqlParserPos pos, SqlLoadSource source, SqlLoadSource target, SqlNodeList colMapping,
                   String separator) {
        super(pos);
        this.source = source;
        this.target = target;
        this.colMapping = colMapping;
        this.separator = separator;
    }

    @Nonnull
    @Override
    public SqlOperator getOperator() {
        return null;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }

    @Nonnull
    @Override
    public SqlKind getKind() {
        return SqlKind.OTHER_DDL;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("LOAD");
        source.getType().unparse(writer, leftPrec, rightPrec);
        writer.keyword(":");
        writer.print("'" + source.getObj() + "' ");
        writer.keyword("TO");
        target.getType().unparse(writer, leftPrec, rightPrec);
        writer.keyword(":");
        writer.print("'" + target.getObj() + "' ");

        final SqlWriter.Frame frame = writer.startList("(", ")");
        for (SqlNode n : colMapping.getList()) {
            writer.newlineAndIndent();
            writer.sep(",", false);
            n.unparse(writer, leftPrec, rightPrec);
        }
        writer.endList(frame);
        writer.keyword("SEPARATOR");
        writer.print("'" + separator + "'");
    }
}
