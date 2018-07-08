package br.com.rooting.roxana.parameter.finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;

import br.com.rooting.roxana.parameter.Parameter;
import org.apache.commons.lang3.StringUtils;

public class ConstraintValidationParameterFinder implements ParameterFinderStrategy {
	
	private static final String EMPYT_VALUE = "";
	
	private static final String INVALID_VALUE_PARAMETER = "invalidValue";
	private static final String PROPERTY_NAME_PARAMETER = "propertyName";
	
	private static final String REMOVED_PARAMETER_GROUP = "groups";
	private static final String REMOVED_PARAMETER_MESSAGE = "message";
	private static final String REMOVED_PARAMETER_PAYLOAD = "payload";
	
	private final ConstraintViolation<?> violation;
	
	public ConstraintValidationParameterFinder(final ConstraintViolation<?> violation) throws IllegalArgumentException {
		if (violation == null) {
			throw new IllegalArgumentException();
		}

		this.violation = violation;
	}

	@Override
	public List<Parameter> findParameters() {
		List<Parameter> parameters = new ArrayList<>();
		String propertyName = this.formatPropertyName(this.getViolation().getPropertyPath().toString());;
		
		// TODO Criar annotation para customizar.
		parameters.add(Parameter.create(PROPERTY_NAME_PARAMETER, propertyName));
		
		Object invalidValue = this.getViolation().getInvalidValue() == null ? EMPYT_VALUE : this.getViolation().getInvalidValue();
		parameters.add(Parameter.create(INVALID_VALUE_PARAMETER, invalidValue));
		
		Map<String, Object> attributes = new HashMap<>();
		attributes.putAll(this.getViolation().getConstraintDescriptor().getAttributes());
		
		attributes.remove(REMOVED_PARAMETER_GROUP);
		attributes.remove(REMOVED_PARAMETER_MESSAGE);
		attributes.remove(REMOVED_PARAMETER_PAYLOAD);
		
		parameters.addAll(attributes.entrySet()
							.stream()
							.map(e -> Parameter.create(e.getKey(), e.getValue()))
							.collect(Collectors.toList())
							);
		return parameters;
	}
	
	private ConstraintViolation<?> getViolation() {
		return this.violation;
	}
	
	private String formatPropertyName(String propertyName) {
		return Stream.of(StringUtils.splitByCharacterTypeCamelCase(propertyName))
								.map(s -> StringUtils.capitalize(s))
								.reduce((s1,s2) -> s1.concat(" ").concat(s2))
								.orElse(EMPYT_VALUE);
	}
	
}