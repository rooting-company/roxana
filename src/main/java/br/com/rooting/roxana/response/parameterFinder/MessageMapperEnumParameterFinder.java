package br.com.rooting.roxana.response.parameterFinder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.message.mapper.parameter.CurrencyMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.DateMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.MessageParameter;
import br.com.rooting.roxana.parameter.Parameter;

public class MessageMapperEnumParameterFinder implements ParameterFinderStrategy {

	private static final String ERROR_EXCETION_WHEN_PASSING_ANNOTATIONS = "Exception when passing the parameters of the message: ";
	
	private final MessageMapperEnum enumMapper;
	
	private final List<Object> values;
	
	public MessageMapperEnumParameterFinder(final MessageMapperEnum enumMapper, final List<Object> values) {
		this.enumMapper = enumMapper;
		this.values = values;
	}
	
	@Override
	public List<Parameter> findParameters() {
		List<Parameter> parameters = new ArrayList<>();
		String enumName = this.getEnumMapper().name();
		Iterator<Annotation> paramIterator = null;
		
		try {
			paramIterator = Arrays.asList(this.getEnumMapper().getClass().getField(enumName).getAnnotations()).iterator();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new FailToFindParameterException(ERROR_EXCETION_WHEN_PASSING_ANNOTATIONS + enumName, e);
		}
		
		// TODO Pode ser necessario garantir a ordenacao dos parametros.
		for(Object value : this.getValues()) {
			if(paramIterator.hasNext()) {
				Annotation a = paramIterator.next();
				if(a instanceof MessageParameter) {
					MessageParameter p = (MessageParameter) a;
					parameters.add(Parameter.create(p.value(), value));
				} else if(a instanceof DateMessageParameter) {
					DateMessageParameter d = (DateMessageParameter) a;
					parameters.add(Parameter.createDateParameter(d.value(), value, d.pattern()));
				} else if(a instanceof CurrencyMessageParameter) {
					CurrencyMessageParameter c = (CurrencyMessageParameter) a;
					parameters.add(Parameter.createCurrencyParameter(c.value(), value));
				}
			} else {
				break;
			}
		}
		
		// Caso o programador não tenha passado os valores de todos os parametros da messagem.
		if(paramIterator.hasNext()) {
			throw new MissingParameterValuesForMessageException(this.getEnumMapper().getKey());
		}
		return parameters;
	}

	protected MessageMapperEnum getEnumMapper() {
		return enumMapper;
	}

	protected List<Object> getValues() {
		return values;
	}

}