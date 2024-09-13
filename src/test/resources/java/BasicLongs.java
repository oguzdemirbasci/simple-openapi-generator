package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicLongs {

	@JsonProperty("simpleLong")
	Long simpleLong;
	@JsonProperty("primitiveLong")
	long primitiveLong;
	@JsonProperty("minLong")
	@Min(2)
	Long minLong;
	@JsonProperty("minPrimitiveLong")
	@Min(7)
	long minPrimitiveLong;
	@JsonProperty("maxLong")
	@Max(10)
	Long maxLong;
	@JsonProperty("maxPrimitiveLong")
	@Max(9)
	long maxPrimitiveLong;
	@JsonProperty("minMaxLong")
	@Min(0)
	@Max(20)
	Long minMaxLong;
	@JsonProperty("minMaxPrimitiveLong")
	@Min(1)
	@Max(5)
	long minMaxPrimitiveLong;
}
