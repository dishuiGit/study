package cn.com.ptpress.cdm.parser.load;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;

@Getter
public class SqlColMapping extends SqlCall {

    protected static final SqlOperator OPERATOR =
            new SqlSpecialOperator("SqlColMapping", SqlKind.OTHER);

    private SqlIdentifier fromCol;
    private SqlIdentifier toCol;

    public SqlColMapping(SqlParserPos pos) {
        super(pos);
    }

    public SqlColMapping(SqlParserPos pos, SqlIdentifier fromCol, SqlIdentifier toCol) {
        super(pos);
        this.fromCol = fromCol;
        this.toCol = toCol;
    }

    @Nonnull
    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableList.of(fromCol, toCol);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        fromCol.unparse(writer, leftPrec, rightPrec);
        writer.print(" ");
        toCol.unparse(writer, leftPrec, rightPrec);
    }
}
