package com.github.oguzdem.openapi.generated;

import lombok.Value;
import java.net.URL;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.net.MalformedURLException;
import java.util.Objects;

@Value
public class DefaultUrls {

	@JsonProperty("defaultUrl")
	URL defaultUrl;

	@Builder
	public DefaultUrls(URL defaultUrl) throws MalformedURLException {
		this.defaultUrl = Objects.isNull(defaultUrl) ? new URL("https://example.com") : defaultUrl;
	}
}
