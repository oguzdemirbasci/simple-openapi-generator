package io.github.oguzdem.openapi.generator.gradleplugin;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GenerateOpenApiModelsExtension {
  String packageName;
  String outputPath;
  boolean enableBeanValidationSupport;
  String inputFile;
}
