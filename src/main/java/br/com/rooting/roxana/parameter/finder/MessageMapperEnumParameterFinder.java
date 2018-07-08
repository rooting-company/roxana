package br.com.rooting.roxana.parameter.finder;

import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam.CurrencyParams;
import br.com.rooting.roxana.parameter.mapper.DateParam;
import br.com.rooting.roxana.parameter.mapper.DateParam.DateParams;
import br.com.rooting.roxana.parameter.mapper.Param;
import br.com.rooting.roxana.parameter.mapper.Param.Params;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

// TODO Rewrite the class to avoid many switches.
public class MessageMapperEnumParameterFinder implements ParameterFinderStrategy {

    private static final String EXCEPTION_WHEN_PASSING_PARAMETER_OF_ENUM_MAPPER = "Exception when passing the parameters of the Enum message mapper: ";

    private final List<Object> values;

    private final List<Annotation> parametersMappers;

    public MessageMapperEnumParameterFinder(final MessageMapperEnum enumMapper, final List<Object> values)
            throws FailToFindParameterException, IllegalArgumentException {

        if (enumMapper == null || values == null || !enumMapper.getClass().isEnum()) {
            throw new IllegalArgumentException();
        }

        List<Annotation> parametersMappers = getParameterMapperAnnotationList(enumMapper);
        validParameters(enumMapper, parametersMappers, values);

        this.values = values;
        this.parametersMappers = parametersMappers;
    }

    private static void validParameters(final MessageMapperEnum messageEnumMapper,
                                        final List<Annotation> parametersMappers,
                                        final List<Object> values)
            throws MissingParametersValuesException,
            OverflowingParametersValuesException {

        if (parametersMappers.size() > values.size()) {
            throw new MissingParametersValuesException(messageEnumMapper.getKey(), parametersMappers.size(), values.size());
        } else if (parametersMappers.size() < values.size()) {
            throw new OverflowingParametersValuesException(messageEnumMapper.getKey(), parametersMappers.size(), values.size());
        }
    }

    private static List<Annotation> getParameterMapperAnnotationList(final MessageMapperEnum messageMapperEnum) {
        try {
            Annotation[] annotations = messageMapperEnum.getClass().getField(messageMapperEnum.name()).getAnnotations();
            List<Annotation> parametersMappers = new ArrayList<>();

            Stream.of(annotations).forEach(a -> {
                if (isParameterMapper(a)) {
                    parametersMappers.add(a);
                } else {
                    parametersMappers.addAll(extractParameterMapperList(a));
                }
            });

            return parametersMappers;
        } catch (NoSuchFieldException | SecurityException e) {
            // It is not going to enter here, because this class only accepts enums as mappers
            // and the method name of enum can not be overwritten.
            throw new FailToFindParameterException(EXCEPTION_WHEN_PASSING_PARAMETER_OF_ENUM_MAPPER + messageMapperEnum.name(), e);
        }
    }

    private static boolean isParameterMapper(final Annotation annotation) {
        return annotation instanceof Param ||
                annotation instanceof DateParam ||
                annotation instanceof CurrencyParam;
    }

    private static List<Annotation> extractParameterMapperList(final Annotation annotation) {
        if (annotation instanceof Params) {
            return Arrays.asList(((Params) annotation).value());
        } else if (annotation instanceof DateParams) {
            return Arrays.asList(((DateParams) annotation).value());
        } else if (annotation instanceof CurrencyParams) {
            return Arrays.asList(((CurrencyParams) annotation).value());
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Parameter> findParameters() {

        List<Parameter> parameters = new ArrayList<>(this.getValues().size());
        Iterator<Annotation> parametersMappersIterator = this.getParametersMappersIterator();
        Iterator<Object> valuesIterator = this.getValuesIterator();

        Integer index = 0;
        while (parametersMappersIterator.hasNext()) {
            parameters.add(this.createParameter(parametersMappersIterator.next(), valuesIterator.next(), index.toString()));
            index++;
        }
        return parameters;
    }

    private Parameter createParameter(final Annotation annotation, final Object value, final String defaultKey) {
        if (annotation instanceof Param) {
            return this.createParameterBaseOn((Param) annotation, value, defaultKey);

        } else if (annotation instanceof DateParam) {
            DateParam dateParameter = (DateParam) annotation;
            return this.createParameterBaseOn(dateParameter, value, defaultKey);

        } else {
            CurrencyParam currencyParameter = (CurrencyParam) annotation;
            return this.createParameterBaseOn(currencyParameter, value, defaultKey);
        }
    }

    private Parameter createParameterBaseOn(final Param param, final Object value, final String defaultKey) {
        String key = Param.DEFAULT_VALUE.equals(param.value()) ? defaultKey : param.value();
        return Parameter.create(key, value);
    }

    private Parameter createParameterBaseOn(final DateParam dateParam, final Object value, final String defaultKey) {
        String key = DateParam.DEFAULT_VALUE.equals(dateParam.value()) ? defaultKey : dateParam.value();
        return Parameter.createDateParameter(key, value, dateParam.style(), dateParam.considerTime(), dateParam.pattern());
    }

    private Parameter createParameterBaseOn(final CurrencyParam currencyParam, final Object value, final String defaultKey) {
        String key = CurrencyParam.DEFAULT_VALUE.equals(currencyParam.value()) ? defaultKey : currencyParam.value();
        return Parameter.createCurrencyParameter(key, value);
    }

    private List<Object> getValues() {
        return this.values;
    }

    private List<Annotation> getParametersMappers() {
        return this.parametersMappers;
    }

    private Iterator<Annotation> getParametersMappersIterator() {
        return this.getParametersMappers().iterator();
    }

    private Iterator<Object> getValuesIterator() {
        return this.getValues().iterator();
    }

}