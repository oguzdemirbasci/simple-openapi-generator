package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import lombok.Generated;

@Value
@Generated
public class DefaultBooleans {

	@JsonProperty("simpleBooleans")
	Boolean simpleBooleans;
	@JsonProperty("defaultBooleans")
	Boolean defaultBooleans;
	@JsonProperty("simplePrimitiveBooleans")
	boolean simplePrimitiveBooleans;

	@Builder
	public DefaultBooleans(Boolean simpleBooleans, Boolean defaultBooleans, boolean simplePrimitiveBooleans) {
		this.simpleBooleans = simpleBooleans;
		this.defaultBooleans = Objects.isNull(defaultBooleans) ? true : defaultBooleans;
		this.simplePrimitiveBooleans = simplePrimitiveBooleans;
	}
}
