package br.com.rooting.roxana.parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.rooting.roxana.parameter.mapper.DateParam;
import br.com.rooting.roxana.parameter.mapper.DateStyle;

final class DateTimeParameterObject {
	
	private final LocalDateTime localDateTime;
	
	private final DateStyle style;
	
	private final Boolean considerTime;
	
	private final String pattern;
	
	private final Integer dayOfMonth;
	
	private final Integer month;
	
	private final Integer year;
	
	private final Integer hour;
	
	private final Integer minute;
	
	private final Integer second;
	
	private final Long instant;
	
	private DateTimeParameterObject(final LocalDateTime localDateTime, 
									final DateStyle style,
									final Boolean considerTime, 
									final String pattern) {
		
		if (localDateTime == null || style == null || considerTime == null || pattern == null) {
			throw new IllegalArgumentException();
		}

		this.localDateTime = localDateTime;
		this.style = style;
		this.considerTime = considerTime;
		this.pattern = pattern;
		this.dayOfMonth = localDateTime.getDayOfMonth();
		this.month = localDateTime.getMonthValue();
		this.year = localDateTime.getYear();
		this.hour = localDateTime.getHour();
		this.minute = localDateTime.getMinute();
		this.second = localDateTime.getSecond();
		this.instant = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
	}
	
	// TODO Tratar erros de formatação, pattern invalido e etc.
	String getFormatterDate(final Locale locale) {
		if (this.getPattern().equals(DateParam.NONE_PATTERN)) {
			if(this.getConsiderTime()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(this.getFormatStyle());
				return this.getLocalDateTime().format(formatter.withLocale(locale));
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(this.getFormatStyle());
				return this.getLocalDateTime().toLocalDate().format(formatter.withLocale(locale));
			}
		} 
		return this.getLocalDateTime().format(DateTimeFormatter.ofPattern(this.getPattern(), locale));
	}
	
	static DateTimeParameterObject create(final Date date, 
										  final DateStyle style, 
										  final Boolean considerTime,
										  final String pattern) {
		
		final LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return new DateTimeParameterObject(localDateTime, style, considerTime, pattern);
	}

	static DateTimeParameterObject create(final Calendar calendar, 
										  final DateStyle style, 
										  final Boolean considerTime,
										  final String pattern) {
		
		final LocalDateTime localDateTime = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
		return new DateTimeParameterObject(localDateTime, style, considerTime, pattern);
	}

	static DateTimeParameterObject create(final LocalDate localDate, 
										  final DateStyle style, 
										  final Boolean considerTime,
										  final String pattern) {
		
		final LocalDateTime localDateTime = localDate.atTime(LocalTime.MIN);
		return new DateTimeParameterObject(localDateTime, style, considerTime, pattern);
	}

	static DateTimeParameterObject create(final LocalDateTime localDateTime, 
										  final DateStyle style,
										  final Boolean considerTime, 
										  final String pattern) {
		
		return new DateTimeParameterObject(localDateTime, style, considerTime, pattern);
	}

	private LocalDateTime getLocalDateTime() {
		return this.localDateTime;
	}
	
	private DateStyle getStyle() {
		return this.style;
	}
	
	private FormatStyle getFormatStyle() {
		return this.getStyle().getFormatStyle();
	}
	
	private Boolean getConsiderTime() {
		return this.considerTime;
	}
	
	private String getPattern() {
		return this.pattern;
	}
	
	public Integer getDayOfMonth() {
		return this.dayOfMonth;
	}

	public Integer getMonth() {
		return this.month;
	}

	public Integer getYear() {
		return this.year;
	}

	public Integer getHour() {
		return this.hour;
	}

	public Integer getMinute() {
		return this.minute;
	}

	public Integer getSecond() {
		return this.second;
	}
	
	public Long getInstant() {
		return this.instant;
	}
	
}