package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;

@Value
public class DefaultBytes {

	@JsonProperty("simpleBytes")
	byte[] simpleBytes;
	@JsonProperty("defaultBytes")
	byte[] defaultBytes;

	@Builder
	public DefaultBytes(byte[] simpleBytes, byte[] defaultBytes) {
		this.simpleBytes = simpleBytes;
		this.defaultBytes = Objects.isNull(defaultBytes) ? new byte[]{123, 123, 123} : defaultBytes;
	}
}
