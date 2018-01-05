package br.com.rooting.roxana.parameter;

import static br.com.rooting.roxana.parameter.ParameterType.CURRENCY;
import static br.com.rooting.roxana.parameter.ParameterType.DATE;
import static br.com.rooting.roxana.parameter.ParameterType.STRING;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Parameter {

	private final ParameterType type;
	
	private final String name;
	
	private final Object value;
	
	private Parameter(final ParameterType type, final String name, final Object value) {
		super();
		if(type == null || name == null || value == null) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	public static Parameter create(final String name, final Object value) {
		return new Parameter(STRING, name, value);
	}

	// Verificar se tem como não deixa publico.
	public static Parameter createDateParameter(final String name, final Object o, final String pattern) {
		
		if (o instanceof Date) {
			return Parameter.createDateParameter(name, (Date) o, pattern);
		} else if (o instanceof Calendar) {
			return Parameter.createDateParameter(name, (Calendar) o, pattern);
		} else if(o instanceof LocalDate) {
			return Parameter.createDateParameter(name, (LocalDate) o, pattern);
		} else if (o instanceof LocalDateTime) {
			return Parameter.createDateParameter(name, (LocalDateTime) o, pattern);
		}
		throw new IllegalArgumentException();
	}
	
	public static Parameter createDateParameter(final String name, final Date date) {
		return new Parameter(DATE, name, DateTimeParameter.create(date, null));
	}
	
	public static Parameter createDateParameter(final String name, final Date date, final String pattern) {
		return new Parameter(DATE, name, DateTimeParameter.create(date, pattern));
	}
	
	public static Parameter createDateParameter(final String name, final Calendar calendar) {
		return new Parameter(DATE, name, DateTimeParameter.create(calendar, null));
	}
	
	public static Parameter createDateParameter(final String name, final Calendar calendar, final String pattern) {
		return new Parameter(DATE, name, DateTimeParameter.create(calendar, pattern));
	}
	
	public static Parameter createDateParameter(final String name, final LocalDate localDate) {
		return new Parameter(DATE, name, DateTimeParameter.create(localDate, null));
	}
	
	public static Parameter createDateParameter(final String name, final LocalDate localDate, final String pattern) {
		return new Parameter(DATE, name, DateTimeParameter.create(localDate, pattern));
	}
	
	public static Parameter createDateParameter(final String name, final LocalDateTime localDateTime) {
		return new Parameter(DATE, name, DateTimeParameter.create(localDateTime, null));
	}
	
	public static Parameter createDateParameter(final String name, final LocalDateTime localDateTime, final String pattern) {
		return new Parameter(DATE, name, DateTimeParameter.create(localDateTime, pattern));
	}
	
	// Verificar se tem como não deixa publico.
	public static Parameter createCurrencyParameter(final String name, final Object o) {
		
		if (o instanceof BigDecimal) {
			return Parameter.createCurrencyParameter(name, (BigDecimal) o);
		} else if (o instanceof Double) {
			return Parameter.createCurrencyParameter(name, (Double) o);
		} else if (o instanceof Float) {
			return Parameter.createCurrencyParameter(name, (Float) o);
		}
		throw new IllegalArgumentException();
	}
	
	public static Parameter createCurrencyParameter(final String name, final BigDecimal bigDecimal) {
		Double moneyValue = Parameter.treatBigDecimal(bigDecimal).doubleValue();
		return new Parameter(CURRENCY, name, moneyValue);
	}
	
	// TODO Verificar se conversão é segura.
	public static Parameter createCurrencyParameter(final String name, final Double doubleParam) {
		Double moneyValue = Parameter.treatBigDecimal(new BigDecimal(doubleParam)).doubleValue();
		return new Parameter(CURRENCY, name, moneyValue);
	}
	
	// TODO Verificar se conversão é segura.
	public static Parameter createCurrencyParameter(final String name, final Float floatParam) {
		Double moneyValue = Parameter.treatBigDecimal(new BigDecimal(floatParam)).doubleValue();
		return new Parameter(CURRENCY, name, moneyValue);
	}
	
	private static BigDecimal treatBigDecimal(BigDecimal bigDecimal) {
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	public String getFormattedValue(Locale locale) {
		switch(this.getType()) {
		case STRING : {
			return this.getValue().toString();
		}
		
		case DATE : {
			return ((DateTimeParameter) this.getValue()).getFormatterDate(locale);
		}
		
		case CURRENCY : {
			return NumberFormat.getCurrencyInstance(locale).format((Double) this.getValue());
		}
		
		default : return this.getValue().toString();
		}
	}
	
	private ParameterType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Object getValue() {
		return this.value;
	}
	
}