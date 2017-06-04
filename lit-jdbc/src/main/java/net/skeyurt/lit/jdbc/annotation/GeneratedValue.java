package net.skeyurt.lit.jdbc.annotation;

import net.skeyurt.lit.jdbc.enums.GenerationType;
import net.skeyurt.lit.jdbc.generator.EmptyKeyGenerator;
import net.skeyurt.lit.jdbc.generator.KeyGenerator;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface GeneratedValue {

    /**
     * (Optional) The primary key generation strategy
     * that the persistence provider must use to
     * generate the annotated entity primary key.
     */
    GenerationType strategy() default GenerationType.AUTO;

    /**
     * 自定主键生成器（可以自定义）
     *
     * @return
     */
    Class<? extends KeyGenerator> generator() default EmptyKeyGenerator.class;

    /**
     * 当主键生成策略为 序列时 有效
     *
     * @return
     */
    String sequenceName() default "";
}
