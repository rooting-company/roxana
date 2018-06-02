package br.com.rooting.roxana.parameter.mapper;

import java.time.format.FormatStyle;

public enum DateStyle {
	SHORT(FormatStyle.SHORT), 
	MEDIUM(FormatStyle.MEDIUM);
	
	private final FormatStyle formatStyle;
	
	private DateStyle(final FormatStyle formatStyle) {
		this.formatStyle = formatStyle;
	}
	
	public FormatStyle getFormatStyle() {
		return this.formatStyle;
	}
	
}