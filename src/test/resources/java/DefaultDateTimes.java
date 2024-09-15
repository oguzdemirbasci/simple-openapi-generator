package io.github.oguzdem.openapi.generated;

import lombok.Value;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Builder;
import java.util.Objects;
import lombok.Generated;

@Value
@Generated
public class DefaultDateTimes {

	@JsonProperty("simpleDateTime")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	OffsetDateTime simpleDateTime;
	@JsonProperty("defaultDateTime")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	OffsetDateTime defaultDateTime;

	@Builder
	public DefaultDateTimes(OffsetDateTime simpleDateTime, OffsetDateTime defaultDateTime) {
		this.simpleDateTime = simpleDateTime;
		this.defaultDateTime = Objects.isNull(defaultDateTime)
				? OffsetDateTime.parse("2024-08-27T20:00Z")
				: defaultDateTime;
	}
}
