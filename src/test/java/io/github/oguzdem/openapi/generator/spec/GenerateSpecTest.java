package io.github.oguzdem.openapi.generator.spec;

import io.github.oguzdem.openapi.generator.ModelGenerator;
import org.junit.jupiter.api.Test;

public class GenerateSpecTest {

  @Test
  void testModelGenerate() {
    ModelGenerator.generate("src/test/resources/specs/basic.yaml");
  }
}
