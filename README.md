# Simple OpenAPI Generator

## Overview
This repository provides a straightforward and easy-to-use OpenAPI model generator for Java. The generated models leverage [Lombok](https://projectlombok.org/) and [Jackson](https://github.com/FasterXML/jackson) for enhanced functionality and simplicity. To maintain ease of use, configurable model generation is not supported. The tool supports **Java 17** and **OpenAPI 3.0** specifications.

> [!IMPORTANT]
> This project is still under development, can be cloned and tested with the following code example. Any feedback or bug report would be apprecieted. A CLI tool and a gradle plugin is to be provided in the final version.

```Java
// Init Parser
OpenAPIParser parser = new OpenAPIParser();
SwaggerParseResult result = parser.readLocation([OSA file path], null, null);
OpenAPI openapi = result.getOpenAPI();

// Load Config - 'packageName' and 'outputPath'
Config.load([Properties File Path]);

if (Objects.nonNull(openapi.getComponents())
    && Objects.nonNull(openapi.getComponents().getSchemas())) {
  openapi
      .getComponents()
      .getSchemas()
      .forEach(
          (s, schema) -> TypeGenerator.getOrGenerateType(s, schema, openapi.getComponents()));
}

PojoGenerator.writeJavaSources();
```

## Example

```Yaml
openapi: 3.0.0
info:
  title: Simple API
  version: 1.0.0
paths:
  /example:
    get:
      summary: Example endpoint
      responses:
        '200':
          description: Successful response
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ExampleModel'
components:
  schemas:
    ExampleModel:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
      additionalProperties: true

```

Following model is generated for the OAS file above:
```Java
@Value
@Builder
@Jacksonized
public class ExampleModel {

  @JsonProperty("id")
  Integer id;
  
  @JsonProperty("name")
  String name;
  
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
```

## Dependencies
 - Java 17
 - Lombok: For reducing boilerplate code in Java models.
 - Jackson: For JSON processing.
 - Swagger OpenAPI Parser: For parsing OpenAPI specifications.
 - Roaster API: For creating Java files programmatically.
   
## Contributing
Contributions are welcome! Please open an issue or submit a pull request with your enhancements or bug fixes.
