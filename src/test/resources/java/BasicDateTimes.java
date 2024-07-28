package org.oguzdem.openapi.generated;

import lombok.Value;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicDateTimes {

	@JsonProperty("simpleDateTime")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	OffsetDateTime simpleDateTime;
}