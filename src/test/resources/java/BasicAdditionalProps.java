package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Singular;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicAdditionalProps {

	@JsonProperty("additionalProperties")
	@Singular
	@JsonAnyGetter
	@JsonAnySetter
	ImmutableMap<String, Object> additionalProperties;

	@JsonIgnore
	ImmutableMap<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
}
