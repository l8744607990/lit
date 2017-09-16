package net.skeyurt.lit.jdbc.sta;

/**
 * User : liulu
 * Date : 2017/6/4 16:38
 * version $Id: Insert.java, v 0.1 Exp $
 */
public interface Insert extends Statement {

    /**
     * insert 语句操作的字段和值
     * @param fieldName 字段名
     * @param value 字段值
     * @return
     */
    Insert into(String fieldName, Object value);

    /**
     * insert 语句操作的字段和值
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @param isNative  为 true 将不采用 ? 占位符方式, 将值直接拼到 sql 中
     * @return
     */
    Insert into(String fieldName, Object value, boolean isNative);

    /**
     * 初始化 inset，将entity 中不为空的属性添加到 insert 的字段中
     *
     * @param entity 实体
     * @return Insert
     */
    Insert initEntity(Object entity);

    /**
     * @return 执行 insert 后的 id 值
     */
    Object execute();

}
