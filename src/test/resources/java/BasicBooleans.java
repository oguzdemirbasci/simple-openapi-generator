package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Generated;

@Value
@Builder
@Jacksonized
@Generated
public class BasicBooleans {

	@JsonProperty("simpleBooleans")
	Boolean simpleBooleans;
	@JsonProperty("simplePrimitiveBooleans")
	boolean simplePrimitiveBooleans;
}
