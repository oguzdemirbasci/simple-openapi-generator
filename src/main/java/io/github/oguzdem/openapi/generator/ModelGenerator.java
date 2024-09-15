package io.github.oguzdem.openapi.generator;

import static io.github.oguzdem.openapi.generator.utils.JavaClassSourceUtils.isRef;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.core.Response.Status;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ModelGenerator {
  private static final String DESERIALIZABLE_ERROR_TEMP =
      "Media Type is not json deserializable. Skipping... [ path: {}, method: {}, responseType: {}, mediaType: {} ]";

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

    if (Objects.nonNull(openapi.getPaths())) {
      Map<String, PathItem> paths = openapi.getPaths();
      paths.forEach(
          (pathString, pathItem) ->
              pathItem
                  .readOperationsMap()
                  .forEach(
                      (httpMethod, operation) -> {
                        if (Objects.nonNull(operation.getResponses())) {
                          operation
                              .getResponses()
                              .forEach(
                                  (responseType, responseItem) -> {
                                    if (Objects.nonNull(responseItem.getContent())) {
                                      responseItem
                                          .getContent()
                                          .forEach(
                                              (mediaTypeString, mediaType) -> {
                                                if (mediaTypeString.contains("json")) {
                                                  if (isRef(mediaType.getSchema())) {
                                                    TypeGenerator.getOrGenerateType(
                                                        createClassName(
                                                            pathString,
                                                            httpMethod,
                                                            responseType,
                                                            operation.getOperationId(),
                                                            mediaType.getSchema()),
                                                        Objects.nonNull(mediaType.getSchema())
                                                            ? (Schema<?>) mediaType.getSchema()
                                                            : new Schema<>(),
                                                        Objects.nonNull(openapi.getComponents())
                                                            ? openapi.getComponents()
                                                            : new Components());
                                                  } else {
                                                    PojoGenerator.generate(
                                                        createClassName(
                                                            pathString,
                                                            httpMethod,
                                                            responseType,
                                                            operation.getOperationId(),
                                                            mediaType.getSchema()),
                                                        Objects.nonNull(mediaType.getSchema())
                                                            ? (Schema<?>) mediaType.getSchema()
                                                            : new Schema<>(),
                                                        Objects.nonNull(openapi.getComponents())
                                                            ? openapi.getComponents()
                                                            : new Components());
                                                  }
                                                } else {
                                                  log.debug(
                                                      DESERIALIZABLE_ERROR_TEMP,
                                                      pathString,
                                                      httpMethod.toString(),
                                                      responseType,
                                                      mediaTypeString);
                                                }
                                              });
                                    }
                                  });
                        }
                      }));
    }

    PojoGenerator.writeJavaSources();
  }

  private static String createClassName(
      @NonNull String pathString,
      @NonNull PathItem.HttpMethod httpMethod,
      @NonNull String responseType,
      String operationId,
      Schema<?> schema) {

    if (Objects.nonNull(schema) && StringUtils.isNotBlank(schema.getTitle())) {
      return schema.getTitle();
    }

    String httpStatusMessage = responseType;
    try {
      httpStatusMessage = Status.fromStatusCode(Integer.parseInt(responseType)).getReasonPhrase();
    } catch (NumberFormatException e) {
      log.debug("Status code is not a valid http status. {}", responseType);
    }

    if (StringUtils.isNotBlank(operationId)) {
      return String.join("_", operationId, httpStatusMessage, "Response");
    }

    return String.join("_", pathString, httpMethod.name(), httpStatusMessage, "Response");
  }
}
