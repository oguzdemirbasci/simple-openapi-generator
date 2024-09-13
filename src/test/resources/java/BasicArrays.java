package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.google.common.collect.ImmutableList;
import lombok.Singular;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicArrays {

	@Singular("stringArray")
	@JsonProperty("stringArray")
	ImmutableList<String> stringArray;
	@Singular("intArray")
	@JsonProperty("intArray")
	ImmutableList<Integer> intArray;
	@Singular("arrayOfArrays")
	@JsonProperty("arrayOfArrays")
	ImmutableList<ImmutableList<String>> arrayOfArrays;
	@Singular("arrayOfObjects")
	@JsonProperty("arrayOfObjects")
	ImmutableList<ArrayOfObjects> arrayOfObjects;
	@Singular("arrayOfUnknownObjects")
	@JsonProperty("arrayOfUnknownObjects")
	ImmutableList<Object> arrayOfUnknownObjects;
}
