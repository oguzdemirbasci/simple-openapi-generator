package io.github.oguzdem.openapi.generated;

import lombok.Value;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Objects;
import lombok.Generated;

@Value
@Generated
public class DefaultUuids {

	@JsonProperty("simpleUuids")
	UUID simpleUuids;
	@JsonProperty("defaultUuid")
	UUID defaultUuid;

	@Builder
	public DefaultUuids(UUID simpleUuids, UUID defaultUuid) {
		this.simpleUuids = simpleUuids;
		this.defaultUuid = Objects.isNull(defaultUuid) ? null : defaultUuid;
	}
}
