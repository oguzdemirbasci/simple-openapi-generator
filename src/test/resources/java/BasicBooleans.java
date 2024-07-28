package org.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicBooleans {

	@JsonProperty("simpleBooleans")
	Boolean simpleBooleans;
	@JsonProperty("simplePrimitiveBooleans")
	boolean simplePrimitiveBooleans;
}
