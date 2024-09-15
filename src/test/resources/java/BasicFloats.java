package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Generated;

@Value
@Generated
public class BasicFloats {

	@JsonProperty("simpleFloat")
	Float simpleFloat;
	@JsonProperty("defaultFloat")
	Float defaultFloat;
	@JsonProperty("primitiveFloat")
	float primitiveFloat;
	@JsonProperty("minFloat")
	@Min(2)
	Float minFloat;
	@JsonProperty("minPrimitiveFloat")
	@Min(7)
	float minPrimitiveFloat;
	@JsonProperty("maxFloat")
	@Max(10)
	Float maxFloat;
	@JsonProperty("maxPrimitiveFloat")
	@Max(9)
	float maxPrimitiveFloat;
	@JsonProperty("minMaxFloat")
	@Min(0)
	@Max(20)
	Float minMaxFloat;
	@JsonProperty("minMaxPrimitiveFloat")
	@Min(1)
	@Max(5)
	float minMaxPrimitiveFloat;

	@Builder
	public BasicFloats(Float simpleFloat, Float defaultFloat, float primitiveFloat, Float minFloat,
			float minPrimitiveFloat, Float maxFloat, float maxPrimitiveFloat, Float minMaxFloat,
			float minMaxPrimitiveFloat) {
		this.simpleFloat = simpleFloat;
		this.defaultFloat = Objects.isNull(defaultFloat) ? 10F : defaultFloat;
		this.primitiveFloat = primitiveFloat;
		this.minFloat = minFloat;
		this.minPrimitiveFloat = minPrimitiveFloat;
		this.maxFloat = maxFloat;
		this.maxPrimitiveFloat = maxPrimitiveFloat;
		this.minMaxFloat = minMaxFloat;
		this.minMaxPrimitiveFloat = minMaxPrimitiveFloat;
	}
}
