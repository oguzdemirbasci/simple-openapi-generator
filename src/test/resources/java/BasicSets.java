package io.github.oguzdem.openapi.generated;

import lombok.Value;
import com.google.common.collect.ImmutableSet;
import lombok.Singular;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import com.google.common.collect.ImmutableList;
import lombok.extern.jackson.Jacksonized;
import lombok.Generated;

@Value
@Builder
@Jacksonized
@Generated
public class BasicSets {

	@Singular("stringSet")
	@JsonProperty("stringSet")
	ImmutableSet<String> stringSet;
	@Singular("intSet")
	@JsonProperty("intSet")
	ImmutableSet<Integer> intSet;
	@Singular("arrayOfSets")
	@JsonProperty("arrayOfSets")
	ImmutableList<ImmutableSet<String>> arrayOfSets;
	@Singular("setOfObjects")
	@JsonProperty("setOfObjects")
	ImmutableSet<ArrayOfObjects> setOfObjects;
	@Singular("setOfArrays")
	@JsonProperty("setOfArrays")
	ImmutableSet<Object> setOfArrays;
}
