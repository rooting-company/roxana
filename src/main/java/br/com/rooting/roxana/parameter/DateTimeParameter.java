package br.com.rooting.roxana.parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import org.springframework.lang.Nullable;

public final class DateTimeParameter {
	
	private final LocalDateTime localDateTime;
	
	private final Optional<String> pattern;

	private final Integer dayOfMonth;
	
	private final Integer month;
	
	private final Integer year;
	
	private final Integer hour;
	
	private final Integer minute;
	
	private final Integer second;
	
	private final Long instant;
	
	DateTimeParameter(final LocalDateTime localDateTime, @Nullable final String pattern) {
		this.localDateTime = localDateTime;
		this.pattern = Optional.ofNullable(pattern);
		this.dayOfMonth = localDateTime.getDayOfMonth();
		this.month = localDateTime.getMonthValue();
		this.year = localDateTime.getYear();
		this.hour = localDateTime.getHour();
		this.minute = localDateTime.getMinute();
		this.second = localDateTime.getSecond();
		this.instant = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
	}
	
	String getFormatterDate(final Locale locale) {
		return this.getPattern()
				.map(p -> this.getLocalDateTime().format(DateTimeFormatter.ofPattern(p, locale)))
				.orElse(this.getLocalDateTime().format(DateTimeFormatter.ISO_DATE.withLocale(locale)));
	}
	
	static DateTimeParameter create(final Date date, @Nullable final String pattern) {
		final LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return new DateTimeParameter(localDateTime, pattern);
	}
	
	static DateTimeParameter create(final Calendar calendar, @Nullable final String pattern) {
		final LocalDateTime localDateTime = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return new DateTimeParameter(localDateTime, pattern);
	}
	
	static DateTimeParameter create(final LocalDate localDate, @Nullable final String pattern) {
		final LocalDateTime localDateTime = localDate.atTime(LocalTime.MIN);
		return new DateTimeParameter(localDateTime, pattern);
	}

	static DateTimeParameter create(final LocalDateTime localDateTime, @Nullable final String pattern) {
		return new DateTimeParameter(localDateTime, pattern);
	}

	private LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	
	private Optional<String> getPattern() {
		return pattern;
	}

	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getHour() {
		return hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public Integer getSecond() {
		return second;
	}
	
	public Long getInstant() {
		return instant;
	}
	
}
