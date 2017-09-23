package br.com.rooting.roxana.response.parameter.finder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import br.com.rooting.roxana.message.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.annotation.CurrencyParameterMapper;
import br.com.rooting.roxana.parameter.annotation.DateParameterMapper;
import br.com.rooting.roxana.parameter.annotation.ParameterMapper;

public class MessageMapperEnumParameterFinder implements ParameterFinderStrategy {

	private static final String ERROR_EXCETION_WHEN_PASSING_ANNOTATIONS = "Exception when passing the parameters of the message: ";
	
	private final MessageMapperEnum mapper;
	
	private final List<Object> values;
	
	public MessageMapperEnumParameterFinder(final MessageMapperEnum mapper, final List<Object> values) {
		this.mapper = mapper;
		this.values = values;
	}
	
	@Override
	public List<Parameter> findParameters() {
		List<Parameter> parameters = new ArrayList<>();
		String enumField = this.getMapper().name();
		Iterator<Annotation> paramIterator = null;
		
		try {
			paramIterator = Arrays.asList(this.getMapper().getClass().getField(enumField).getAnnotations()).iterator();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new FailToFindParameterException(ERROR_EXCETION_WHEN_PASSING_ANNOTATIONS + this.getMapper().name(), e);
		}
		
		if(paramIterator != null) {
			for(Object value : this.getValues()) {
				if(paramIterator.hasNext()) {
					Annotation a = paramIterator.next();
					if(a instanceof ParameterMapper) {
						ParameterMapper p = (ParameterMapper) a;
						parameters.add(Parameter.create(p.value(), value));
					} else if(a instanceof DateParameterMapper) {
						DateParameterMapper d = (DateParameterMapper) a;
						parameters.add(Parameter.createDateParameter(d.value(), value, d.pattern()));
					} else if(a instanceof CurrencyParameterMapper) {
						CurrencyParameterMapper c = (CurrencyParameterMapper) a;
						parameters.add(Parameter.createCurrencyParameter(c.value(), value));
					}
				} else {
					break;
				}
			}
			
			// Caso o programador não tenha passado os valores de todos os parametros da messagem.
			if(paramIterator.hasNext()) {
				throw new MissingParameterValuesForMessageException(this.getMapper().getMessageKey());
			}
		}
		return parameters;
	}

	public MessageMapperEnum getMapper() {
		return mapper;
	}

	public List<Object> getValues() {
		return values;
	}

}
