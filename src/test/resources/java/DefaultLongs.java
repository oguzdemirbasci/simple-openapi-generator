package org.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Value
public class DefaultLongs {

	@JsonProperty("simpleLong")
	Long simpleLong;
	@JsonProperty("defaultLong")
	Long defaultLong;
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

	@Builder
	public DefaultLongs(Long simpleLong, Long defaultLong, long primitiveLong, Long minLong, long minPrimitiveLong,
			Long maxLong, long maxPrimitiveLong, Long minMaxLong, long minMaxPrimitiveLong) {
		this.simpleLong = simpleLong;
		this.defaultLong = Objects.isNull(defaultLong) ? 10L : defaultLong;
		this.primitiveLong = primitiveLong;
		this.minLong = minLong;
		this.minPrimitiveLong = minPrimitiveLong;
		this.maxLong = maxLong;
		this.maxPrimitiveLong = maxPrimitiveLong;
		this.minMaxLong = minMaxLong;
		this.minMaxPrimitiveLong = minMaxPrimitiveLong;
	}
}
