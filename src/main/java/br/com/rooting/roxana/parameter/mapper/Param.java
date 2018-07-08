package br.com.rooting.roxana.parameter.mapper;

import br.com.rooting.roxana.parameter.mapper.Param.Params;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(Params.class)
public @interface Param {

    String DEFAULT_VALUE = "";

    String value() default DEFAULT_VALUE;

    @Documented
    @Target(FIELD)
    @Retention(RUNTIME)
    @interface Params {

        Param[] value();

    }

}