package net.skeyurt.lit.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.skeyurt.lit.commons.util.EnumAware;

/**
 * User : liulu
 * Date : 2017/3/18 13:19
 * version $Id: Operator.java, v 0.1 Exp $
 */
@Getter
@AllArgsConstructor
public enum Operator implements EnumAware {

    EQ("equal", " = "),

    NE("not equal", " != "),

    LT("less than", " < "),

    GT("grater than", " > "),

    LE("less than or equal", " <= "),

    GE("grater than or equal", " >= "),

    LIKE("like", " like "),

    NOT_LIKE("not like", " not like "),

    IN("in", " in "),

    NOT_IN("not in", " not in "),

    IS_NULL("is null", " is null "),

    IS_NOT_NULL("is not null", " is not null "),
    ;


    private String text;

    private String value;

}
