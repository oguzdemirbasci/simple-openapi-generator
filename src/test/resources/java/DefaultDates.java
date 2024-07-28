package org.oguzdem.openapi.generated;

import lombok.Value;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Builder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Value
public class DefaultDates {

	@JsonProperty("simpleDate")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	Date simpleDate;
	@JsonProperty("defaultDate")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	Date defaultDate;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Builder
	public DefaultDates(Date simpleDate, Date defaultDate) throws ParseException {
		this.simpleDate = simpleDate;
		this.defaultDate = Objects.isNull(defaultDate) ? FORMAT.parse("2024-08-27") : defaultDate;
	}
}
