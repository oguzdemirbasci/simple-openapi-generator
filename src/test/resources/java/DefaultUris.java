package io.github.oguzdem.openapi.generated;

import lombok.Value;
import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;

@Value
public class DefaultUris {

	@JsonProperty("simpleUris")
	URI simpleUris;
	@JsonProperty("defaultUris")
	URI defaultUris;

	@Builder
	public DefaultUris(URI simpleUris, URI defaultUris) {
		this.simpleUris = simpleUris;
		this.defaultUris = Objects.isNull(defaultUris) ? URI.create("https://example.com") : defaultUris;
	}
}
