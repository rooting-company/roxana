package br.com.rooting.roxana.parameter.mapper;

import br.com.rooting.roxana.parameter.mapper.DateParam.DateParams;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(DateParams.class)
public @interface DateParam {

    String NONE_PATTERN = "none";

    String DEFAULT_VALUE = "";

    String value() default DEFAULT_VALUE;

    String pattern() default NONE_PATTERN;

    DateStyle style() default DateStyle.SHORT;

    boolean considerTime() default false;

    @Documented
    @Target(FIELD)
    @Retention(RUNTIME)
    @interface DateParams {

        DateParam[] value();

    }

}