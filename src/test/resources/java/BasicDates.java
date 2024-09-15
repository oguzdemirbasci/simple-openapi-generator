package io.github.oguzdem.openapi.generated;

import lombok.Value;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Generated;

@Value
@Builder
@Jacksonized
@Generated
public class BasicDates {

	@JsonProperty("simpleDate")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	Date simpleDate;
}
