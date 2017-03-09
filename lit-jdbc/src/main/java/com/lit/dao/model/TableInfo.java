package com.lit.dao.model;

import com.lit.commons.bean.BeanUtils;
import com.lit.commons.util.NameUtils;
import com.lit.dao.annotation.*;
import com.lit.dao.enums.GenerationType;
import com.lit.dao.generator.EmptyKeyGenerator;
import com.lit.dao.generator.KeyGenerator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * User : liulu
 * Date : 2016-10-4 15:42
 */
@Getter
public class TableInfo {

    /** 表名 */
    private String                                tableName;

    /** 主键属性名 */
    private String                                pkField;

    /** 主键对应的列名 */
    private String                                pkColumn;

    /** 主键是否需要生成 */
    private boolean                               autoGenerateKey;

    /** 主键生成器 */
    private Class<? extends KeyGenerator>         generatorClass;

    /** 主键生成类型 */
    private GenerationType                        generationType;

    /** 如果主键是序列生成, 序列名 */
    private String                                sequenceName;

    /** 属性名和字段名映射关系的 map */
    private Map<String, String>                   fieldColumnMap;

    public TableInfo(Class<?> clazz) {
        fieldColumnMap = new HashMap<>();
        initTableInfo(clazz);
    }

    /**
     * 根据注解初始化表信息，
     *
     * @param clazz 实体类的 class
     */
    private void initTableInfo(Class<?> clazz) {
        tableName = clazz.isAnnotationPresent(Table.class) ? ((Table) clazz.getAnnotation(Table.class)).name()
                : NameUtils.getUnderLineName(clazz.getSimpleName());

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 过滤有 @Transient 注解的字段
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);

            // 主键信息 : 有 @Id 注解的字段，没有默认是 类名+Id
            if (field.isAnnotationPresent(Id.class) || (StringUtils.equalsIgnoreCase(field.getName(), clazz.getSimpleName() + "Id") && pkField == null)) {
                pkField = field.getName();
                pkColumn = column != null ? column.name() : NameUtils.getUnderLineName(field.getName());
                initAutoKeyInfo(field);
            }
            // 将字段对应的列放到 map 中
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(clazz, field.getName());
            if (descriptor != null && descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
                fieldColumnMap.put(field.getName(), column != null ? column.name() : NameUtils.getUnderLineName(field.getName()));
            }
        }
    }

    /**
     * 根据主键的注解 初始化 主键是否需要生成的信息
     *
     * @param field 主键
     */
    private void initAutoKeyInfo(Field field) {
        GeneratedValue generated = field.getAnnotation(GeneratedValue.class);
        if (generated == null || (generated.strategy() == GenerationType.AUTO && generated.generator() == EmptyKeyGenerator.class)) {
            return;
        }
        autoGenerateKey = true;
        if (generated.generator() != EmptyKeyGenerator.class) {
            generatorClass = generated.generator();
            return;
        }
        generationType = generated.strategy();
        if (Objects.equals(generationType, GenerationType.SEQUENCE)) {
            SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
            sequenceName = sequenceGenerator == null ? "seq_" + tableName : sequenceGenerator.sequenceName();
        }
    }

}
