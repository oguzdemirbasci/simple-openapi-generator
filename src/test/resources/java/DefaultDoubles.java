package org.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Value
public class DefaultDoubles {

	@JsonProperty("simpleDouble")
	Double simpleDouble;
	@JsonProperty("defaultDouble")
	Double defaultDouble;
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

	@Builder
	public DefaultDoubles(Double simpleDouble, Double defaultDouble, double primitiveDouble, Double minDouble,
			double minPrimitiveDouble, Double maxDouble, double maxPrimitiveDouble, Double minMaxDouble,
			double minMaxPrimitiveDouble) {
		this.simpleDouble = simpleDouble;
		this.defaultDouble = Objects.isNull(defaultDouble) ? 0D : defaultDouble;
		this.primitiveDouble = primitiveDouble;
		this.minDouble = minDouble;
		this.minPrimitiveDouble = minPrimitiveDouble;
		this.maxDouble = maxDouble;
		this.maxPrimitiveDouble = maxPrimitiveDouble;
		this.minMaxDouble = minMaxDouble;
		this.minMaxPrimitiveDouble = minMaxPrimitiveDouble;
	}
}
