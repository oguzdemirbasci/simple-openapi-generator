package org.oguzdem.json;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.net.URL;
import java.util.Objects;
import org.junit.jupiter.api.Test;

/**
 * @author Oguz Demirbasci
 */
public class OpenApiToJavaTest {

  @Test
  public void generateClasses() {
    URL input = ClassLoader.getSystemResource("openapi/basic-schemas.json");
    OpenAPIParser parser = new OpenAPIParser();
    SwaggerParseResult result = parser.readLocation(input.getPath(), null, null);
    OpenAPI openapi = result.getOpenAPI();
    if (Objects.nonNull(openapi.getComponents())
        && Objects.nonNull(openapi.getComponents().getSchemas())) {
      openapi
          .getComponents()
          .getSchemas()
          .forEach(
              (s, schema) -> TypeGenerator.getOrGenerateType(s, schema, openapi.getComponents()));
    }
    PojoGenerator.writeJavaSources();
  }
}
