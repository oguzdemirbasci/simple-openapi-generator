package com.github.oguzdem.openapi.generated;

import lombok.Value;
import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicUris {

	@JsonProperty("simpleUris")
	URI simpleUris;
}
