package com.github.oguzdem.openapi.generated;

import lombok.Value;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Builder;
import java.util.Objects;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Value
public class DefaultStrings {

	@JsonProperty("simpleString")
	@NonNull
	String simpleString;
	@JsonProperty("stringWithDefaultValue")
	String stringWithDefaultValue;
	@JsonProperty("lengthLimitedString")
	@Size(min = 2, max = 4)
	String lengthLimitedString;
	@JsonProperty("regexEmailString")
	@Pattern(regexp = "^((?!\\\\.)[\\\\w-_.]*[^.])(@\\\\w+)(\\\\.\\\\w+(\\\\.\\\\w+)?[^.\\\\W])$")
	String regexEmailString;

	@Builder
	public DefaultStrings(String simpleString, String stringWithDefaultValue, String lengthLimitedString,
			String regexEmailString) {
		this.simpleString = simpleString;
		this.stringWithDefaultValue = Objects.isNull(stringWithDefaultValue) ? "Default Str" : stringWithDefaultValue;
		this.lengthLimitedString = lengthLimitedString;
		this.regexEmailString = regexEmailString;
	}
}
