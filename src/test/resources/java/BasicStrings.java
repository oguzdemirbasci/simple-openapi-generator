package org.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Builder;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BasicStrings {

	@JsonProperty("simpleString")
	@NonNull
	String simpleString;
	@JsonProperty("lengthLimitedString")
	@Size(min = 2, max = 4)
	String lengthLimitedString;
	@JsonProperty("regexEmailString")
	@Pattern(regexp = "^((?!\\\\.)[\\\\w-_.]*[^.])(@\\\\w+)(\\\\.\\\\w+(\\\\.\\\\w+)?[^.\\\\W])$")
	String regexEmailString;
}
