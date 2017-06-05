package net.skeyurt.lit.jdbc.sta;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.skeyurt.lit.commons.bean.BeanUtils;
import net.skeyurt.lit.jdbc.model.StatementContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User : liulu
 * Date : 2017/6/4 9:35
 * version $Id: UpdateImpl.java, v 0.1 Exp $
 */
class UpdateImpl extends AbstractCondition<Update> implements Update{

    private net.sf.jsqlparser.statement.update.Update update = new net.sf.jsqlparser.statement.update.Update();

    private List<Column> columns = new ArrayList<>();

    private List<Expression> values = new ArrayList<>();

    UpdateImpl(Class<?> clazz) {
        super(clazz);
        update.setTables(Collections.singletonList(new Table(tableInfo.getTableName())));
        update.setColumns(columns);
        update.setExpressions(values);
    }

    @Override
    public Update set(String... fieldNames) {
        for (String fieldName : fieldNames) {
            columns.add(new Column(getColumn(fieldName)));
        }
        return this;
    }

    @Override
    public UpdateImpl values(Object... values) {
        for (Object value : values) {
            this.values.add(PARAM_EXPR);
            params.add(value);
        }
        return this;
    }


    @Override
    public UpdateImpl initEntity(Object entity, boolean isIgnoreNull) {
        if (entity == null) {
            return this;
        }
        Object key = BeanUtils.invokeReaderMethod(entity, tableInfo.getPkField());
        if (key == null) {
            throw new NullPointerException("entity [" + entity + "] id is null, can not update!");
        }

        Map<String, String> fieldColumnMap = tableInfo.getFieldColumnMap();

        for (Map.Entry<String, String> entry : fieldColumnMap.entrySet()) {
            if (StringUtils.equals(tableInfo.getPkField(), entry.getKey())) {
                continue;
            }

            Object obj = BeanUtils.invokeReaderMethod(entity, entry.getKey());
            if (!isIgnoreNull || obj != null && (!(obj instanceof String) || StringUtils.isNotBlank((String) obj))) {
                columns.add(new Column(entry.getValue()));
                values.add(PARAM_EXPR);
                params.add(obj);
            }
        }
        id(key);

        return this;
    }

    @Override
    public int execute() {
        update.setWhere(where);
        return (int) executor.execute(new StatementContext(update.toString(), params, StatementContext.StatementType.UPDATE));
    }
}