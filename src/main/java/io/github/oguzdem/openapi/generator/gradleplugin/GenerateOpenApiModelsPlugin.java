package io.github.oguzdem.openapi.generator.gradleplugin;

import io.github.oguzdem.openapi.generator.Config;
import io.github.oguzdem.openapi.generator.ModelGenerator;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GenerateOpenApiModelsPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    GenerateOpenApiModelsExtension configExtension =
        project.getExtensions().create("modelGenerator", GenerateOpenApiModelsExtension.class);

    Config.load(configExtension);

    project
        .task("generateModels")
        .doLast(
            task -> {
              ModelGenerator.generate(configExtension.getInputFile());
            });
  }
}
