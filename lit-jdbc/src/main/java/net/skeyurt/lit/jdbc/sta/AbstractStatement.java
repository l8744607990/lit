package net.skeyurt.lit.jdbc.sta;

import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.skeyurt.lit.jdbc.StatementExecutor;
import net.skeyurt.lit.jdbc.model.TableInfo;
import net.skeyurt.lit.jdbc.pager.StatementPageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User : liulu
 * Date : 2017/6/4 9:31
 * version $Id: AbstractStatement.java, v 0.1 Exp $
 */
@NoArgsConstructor
public abstract class AbstractStatement implements Statement {

    protected static final Expression PARAM_EXPR = new JdbcParameter();

    protected TableInfo tableInfo;

    @Setter
    protected StatementExecutor executor;

    @Setter
    protected StatementPageHandler pageHandler;

    @Setter
    protected String dbName;

    protected Table table;

    protected List<Object> params = new ArrayList<>();

    AbstractStatement(Class<?> clazz) {
        this.tableInfo = new TableInfo(clazz);
        table = new Table(tableInfo.getTableName());
    }

    public Column buildColumn(String fieldName) {
        fieldName = fieldName.trim();
        String column = tableInfo.getFieldColumnMap().get(fieldName);
        return column == null || column.isEmpty()? new Column(fieldName): new Column(table, column);
    }

}
