package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Value
public class DefaultIntegers {

	@JsonProperty("simpleInt")
	Integer simpleInt;
	@JsonProperty("primitiveInt")
	int primitiveInt;
	@JsonProperty("defaultInt")
	Integer defaultInt;
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

	@Builder
	public DefaultIntegers(Integer simpleInt, int primitiveInt, Integer defaultInt, Integer minInt, int minPrimitiveInt,
			Integer maxInt, int maxPrimitiveInt, Integer minMaxInt, int minMaxPrimitiveInt) {
		this.simpleInt = simpleInt;
		this.primitiveInt = primitiveInt;
		this.defaultInt = Objects.isNull(defaultInt) ? 30 : defaultInt;
		this.minInt = minInt;
		this.minPrimitiveInt = minPrimitiveInt;
		this.maxInt = maxInt;
		this.maxPrimitiveInt = maxPrimitiveInt;
		this.minMaxInt = minMaxInt;
		this.minMaxPrimitiveInt = minMaxPrimitiveInt;
	}
}
