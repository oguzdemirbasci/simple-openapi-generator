package org.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;

@Value
public class DefaultBinaries {

	@JsonProperty("simpleBinary")
	byte[] simpleBinary;
	@JsonProperty("defaultBinary")
	byte[] defaultBinary;

	@Builder
	public DefaultBinaries(byte[] simpleBinary, byte[] defaultBinary) {
		this.simpleBinary = simpleBinary;
		this.defaultBinary = Objects.isNull(defaultBinary) ? new byte[]{123, 123, 123} : defaultBinary;
	}
}
