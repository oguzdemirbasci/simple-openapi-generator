package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicDoubles {

	@JsonProperty("simpleDouble")
	Double simpleDouble;
	@JsonProperty("primitiveDouble")
	double primitiveDouble;
	@JsonProperty("minDouble")
	@Min(2)
	Double minDouble;
	@JsonProperty("minPrimitiveDouble")
	@Min(7)
	double minPrimitiveDouble;
	@JsonProperty("maxDouble")
	@Max(10)
	Double maxDouble;
	@JsonProperty("maxPrimitiveDouble")
	@Max(9)
	double maxPrimitiveDouble;
	@JsonProperty("minMaxDouble")
	@Min(0)
	@Max(20)
	Double minMaxDouble;
	@JsonProperty("minMaxPrimitiveDouble")
	@Min(1)
	@Max(5)
	double minMaxPrimitiveDouble;
}
