package br.com.rooting.roxana;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class UnitTest<T> {

    @SuppressWarnings("unchecked")
    protected Class<T> getUnitTestClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualArgument = parameterizedType.getActualTypeArguments()[0];

        if (ParameterizedType.class.isAssignableFrom(actualArgument.getClass())) {
            return (Class<T>) ((ParameterizedType) actualArgument).getRawType();
        }

        return (Class<T>) actualArgument;
    }

}