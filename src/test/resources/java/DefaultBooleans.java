package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;

@Value
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
