package br.com.rooting.roxana.parameter.finder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam.CurrencyParams;
import br.com.rooting.roxana.parameter.mapper.DateParam;
import br.com.rooting.roxana.parameter.mapper.DateParam.DateParams;
import br.com.rooting.roxana.parameter.mapper.Param;
import br.com.rooting.roxana.parameter.mapper.Param.Params;

// TODO Rewrite the class to avoid many swiches.
public class MessageMapperEnumParameterFinder implements ParameterFinderStrategy {

	private static final String EXCETION_WHEN_PASSING_PARAMETER_OF_ENUM_MAPPER = "Exception when passing the parameters of the Enum message mapper: ";
	
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
			// Não devera entrar aqui, pois esta classe so aceita enuns como mapper 
			// e o metodo name do enum nao pode ser sobreescrito.
			throw new FailToFindParameterException(EXCETION_WHEN_PASSING_PARAMETER_OF_ENUM_MAPPER + messageMapperEnum.name(), e);
		}
	}
	
	private static boolean isParameterMapper(final Annotation annotation) {
		if(annotation instanceof Param || 
		   annotation instanceof DateParam || 
		   annotation instanceof CurrencyParam) {
			return true;
		}
		return false;
	}
	
	private static List<Annotation> extractParameterMapperList(final Annotation annotation) {
		if(annotation instanceof Params) {
			return Arrays.asList(((Params) annotation).value());
		} else if (annotation instanceof DateParams) {
			return Arrays.asList(((DateParams) annotation).value());
		} else if (annotation instanceof CurrencyParams) {
			return Arrays.asList(((CurrencyParams) annotation).value());
		}
		return new ArrayList<>(0);
	}
	
	@Override
	public List<Parameter> findParameters() throws OverflowingParametersValuesException,
												   MissingParametersValuesException {
		
		List<Parameter> parameters = new ArrayList<>(this.getValues().size());
		Iterator<Annotation> parametersMappersIterator = this.getParametersMappersIterator();
		Iterator<Object> valuesIterator = this.getValuesIterator();
		
		Integer index = 0;
		while(parametersMappersIterator.hasNext()) {
			parameters.add(this.createParameter(parametersMappersIterator.next(), valuesIterator.next(), index.toString()));
			index++;
		}
		return parameters;
	}
	
	private Parameter createParameter(final Annotation annotation, final Object value, final String defautKey) {
		if(annotation instanceof Param) {
			return this.createParameterBaseOn((Param) annotation, value, defautKey);
			
		} else if(annotation instanceof DateParam) {
			DateParam dateParameter = (DateParam) annotation;
			return this.createParameterBaseOn(dateParameter, value, defautKey);
			
		} else {
			CurrencyParam currencyParameter = (CurrencyParam) annotation;
			return this.createParameterBaseOn(currencyParameter, value, defautKey);
		}
	}
	
	private Parameter createParameterBaseOn(final Param param, final Object value, final String defautKey) {
		String key = Param.DEFAULT_VALUE.equals(param.value()) ? defautKey : param.value(); 
		return Parameter.create(key, value);
	}
	
	private Parameter createParameterBaseOn(final DateParam dateParam, final Object value, final String defautKey) {
		String key = DateParam.DEFAULT_VALUE.equals(dateParam.value()) ? defautKey : dateParam.value(); 
		return Parameter.createDateParameter(key, value, dateParam.style(), dateParam.considerTime(), dateParam.pattern());
	}
	
	private Parameter createParameterBaseOn(final CurrencyParam currencyParam, final Object value, final String defautKey) {
		String key = CurrencyParam.DEFAULT_VALUE.equals(currencyParam.value()) ? defautKey : currencyParam.value(); 
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