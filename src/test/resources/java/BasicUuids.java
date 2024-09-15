package io.github.oguzdem.openapi.generated;

import lombok.Value;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Generated;

@Value
@Builder
@Jacksonized
@Generated
public class BasicUuids {

	@JsonProperty("simpleUuids")
	UUID simpleUuids;
}
