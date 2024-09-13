package io.github.oguzdem.openapi.generator;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.Objects;

public class ModelGenerator {
  public static void generate(String inputFilePath) {
    OpenAPIParser parser = new OpenAPIParser();
    SwaggerParseResult result = parser.readLocation(inputFilePath, null, null);
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
