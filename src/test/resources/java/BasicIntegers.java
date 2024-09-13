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
public class BasicIntegers {

	@JsonProperty("simpleInt")
	Integer simpleInt;
	@JsonProperty("primitiveInt")
	int primitiveInt;
	@JsonProperty("minInt")
	@Min(10)
	Integer minInt;
	@JsonProperty("minPrimitiveInt")
	@Min(1)
	int minPrimitiveInt;
	@JsonProperty("maxInt")
	@Max(10)
	Integer maxInt;
	@JsonProperty("maxPrimitiveInt")
	@Max(10)
	int maxPrimitiveInt;
	@JsonProperty("minMaxInt")
	@Min(0)
	@Max(20)
	Integer minMaxInt;
	@JsonProperty("minMaxPrimitiveInt")
	@Min(1)
	@Max(5)
	int minMaxPrimitiveInt;
}
