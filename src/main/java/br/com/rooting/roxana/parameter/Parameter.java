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

import br.com.rooting.roxana.parameter.mapper.DateStyle;

public final class Parameter {

	private final ParameterType type;
	
	private final String name;
	
	private final Object value;
	
	private Parameter(final ParameterType type, final String name, final Object value) {
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

	public static Parameter createDateParameter(final String name,
												final Object o,
												final DateStyle style,
												final Boolean considerTime,
												final String pattern)
												throws UnsupportedParameterConversionException {
		
		if (o instanceof Date) {
			return Parameter.createDateParameter(name, (Date) o, style, considerTime, pattern);
		} else if (o instanceof Calendar) {
			return Parameter.createDateParameter(name, (Calendar) o, style, considerTime, pattern);
		} else if(o instanceof LocalDate) {
			return Parameter.createDateParameter(name, (LocalDate) o, style, considerTime, pattern);
		} else if (o instanceof LocalDateTime) {
			return Parameter.createDateParameter(name, (LocalDateTime) o, style, considerTime, pattern);
		}
		throw new UnsupportedParameterConversionException(DATE, o);
	}
	
	public static Parameter createDateParameter(final String name,
												final Date date,
												final DateStyle style,
												final Boolean considerTime,
												final String pattern) {
		
		return new Parameter(DATE, name, DateTimeParameterObject.create(date, style, considerTime, pattern));
	}
	
	public static Parameter createDateParameter(final String name,
												final Calendar calendar,
												final DateStyle style,
												final Boolean considerTime,
												final String pattern) {
		
		return new Parameter(DATE, name, DateTimeParameterObject.create(calendar, style, considerTime, pattern));
	}
	
	public static Parameter createDateParameter(final String name,
												final LocalDate localDate,
												final DateStyle style,
												final Boolean considerTime,
												final String pattern) {
		
		return new Parameter(DATE, name, DateTimeParameterObject.create(localDate, style, considerTime, pattern));
	}
	
	public static Parameter createDateParameter(final String name,
												final LocalDateTime localDateTime,
												final DateStyle style,
												final Boolean considerTime,
												final String pattern) {
		
		return new Parameter(DATE, name, DateTimeParameterObject.create(localDateTime, style, considerTime, pattern));
	}
	
	public static Parameter createCurrencyParameter(final String name, final Object o) throws UnsupportedParameterConversionException {
		if (o instanceof BigDecimal) {
			return Parameter.createCurrencyParameter(name, (BigDecimal) o);
		} else if (o instanceof Double) {
			return Parameter.createCurrencyParameter(name, (Double) o);
		} else if (o instanceof Float) {
			return Parameter.createCurrencyParameter(name, (Float) o);
		}
		throw new UnsupportedParameterConversionException(CURRENCY, o);
	}
	
	public static Parameter createCurrencyParameter(final String name, final BigDecimal bigDecimalParam) {
		Double moneyValue = Parameter.treatBigDecimal(bigDecimalParam).doubleValue();
		return new Parameter(CURRENCY, name, moneyValue);
	}
	
	public static Parameter createCurrencyParameter(final String name, final Double doubleParam) {
		return Parameter.createCurrencyParameter(name,  BigDecimal.valueOf(doubleParam));
	}
	
	public static Parameter createCurrencyParameter(final String name, final Float floatParam) {
		return Parameter.createCurrencyParameter(name, BigDecimal.valueOf(floatParam));
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
			return ((DateTimeParameterObject) this.getValue()).getFormatterDate(locale);
		}
		
		case CURRENCY : {
			return NumberFormat.getCurrencyInstance(locale).format((Double) this.getValue());
		}
		
		default : return this.getValue().toString();
		}
	}
	
	public ParameterType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null) {
			return false;
		} else if (!(object instanceof Parameter)) {
			return false;
		}
		
		Parameter parameter = (Parameter) object;
		if(this.getType().equals(parameter.getType()) 
				&& this.getName().equals(parameter.getName()) 
				&& this.getValue().equals(parameter.getValue())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 7 * (this.getType().hashCode() + this.getName().hashCode());
	}
	
}