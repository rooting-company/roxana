package br.com.rooting.roxana.response.parameter_finder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.message.mapper.parameter.CurrencyMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.CurrencyMessageParameters;
import br.com.rooting.roxana.message.mapper.parameter.DateMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.DateMessageParameters;
import br.com.rooting.roxana.message.mapper.parameter.MessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.MessageParameters;
import br.com.rooting.roxana.parameter.Parameter;

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
		if(annotation instanceof MessageParameter 			|| 
		   annotation instanceof DateMessageParameter 		|| 
		   annotation instanceof CurrencyMessageParameter     ) {
			return true;
		}
		return false;
	}
	
	private static List<Annotation> extractParameterMapperList(final Annotation annotation) {
		if(annotation instanceof MessageParameters) {
			return Arrays.asList(((MessageParameters) annotation).value());
		} else if (annotation instanceof DateMessageParameters) {
			return Arrays.asList(((DateMessageParameters) annotation).value());
		} else if (annotation instanceof CurrencyMessageParameters) {
			return Arrays.asList(((CurrencyMessageParameters) annotation).value());
		}
		return new ArrayList<>(0);
	}
	
	@Override
	public List<Parameter> findParameters() throws OverflowingParametersValuesException,
												   MissingParametersValuesException {
		
		List<Parameter> parameters = new ArrayList<>(this.getValues().size());
		Iterator<Annotation> parametersMappersIterator = this.getParametersMappersIterator();
		Iterator<Object> valuesIterator = this.getValuesIterator();
		
		while(parametersMappersIterator.hasNext()) {
			parameters.add(this.createParameter(parametersMappersIterator.next(), valuesIterator.next()));
		}
		return parameters;
	}
	
	private Parameter createParameter(final Annotation annotation, final Object value) {
		if(annotation instanceof MessageParameter) {
			MessageParameter messageParameter = (MessageParameter) annotation;
			return this.createParameterBaseOn(messageParameter, value);
			
		} else if(annotation instanceof DateMessageParameter) {
			DateMessageParameter dateMessageParameter = (DateMessageParameter) annotation;
			return this.createParameterBaseOn(dateMessageParameter, value);
			
		} else {
			CurrencyMessageParameter currencyMessageParameter = (CurrencyMessageParameter) annotation;
			return this.createParameterBaseOn(currencyMessageParameter, value);
		}
	}
	
	private Parameter createParameterBaseOn(final MessageParameter messageParameter, final Object values) {
		return Parameter.create(messageParameter.value(), values);
	}
	
	private Parameter createParameterBaseOn(final DateMessageParameter dateMessageParameter, 
											final Object value) {
		
		return Parameter.createDateParameter(dateMessageParameter.value(), 
											 value,
											 dateMessageParameter.style(),
											 dateMessageParameter.considerTime(),
											 dateMessageParameter.pattern());
	}
	
	private Parameter createParameterBaseOn(final CurrencyMessageParameter currencyMessageParameter, 
											final Object value) {
		
		return Parameter.createCurrencyParameter(currencyMessageParameter.value(), value);
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