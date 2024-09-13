package com.github.oguzdem.openapi.generator;

import com.github.oguzdem.openapi.generator.gradleplugin.GenerateOpenApiModelsExtension;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Oguz Demirbasci
 */
public final class Config {
  public static final String PACKAGE_NAME = "packageName";
  public static final String OUTPUT_PATH = "outputPath";
  public static final String ENABLE_BEAN_VALIDATION_SUPPORT = "enableBeanValidationSupport";

  private static final Properties PROPERTIES = System.getProperties();

  private Config() {}

  public static void load(GenerateOpenApiModelsExtension pluginConfig) {
    PROPERTIES.setProperty(PACKAGE_NAME, pluginConfig.getPackageName());
    PROPERTIES.setProperty(OUTPUT_PATH, pluginConfig.getOutputPath());
    PROPERTIES.setProperty(
        ENABLE_BEAN_VALIDATION_SUPPORT,
        String.valueOf(pluginConfig.isEnableBeanValidationSupport()));
  }

  public static void load(FileInputStream fileInputStream) throws IOException {
    PROPERTIES.load(fileInputStream);
  }

  public static void load(String propertiesPath) throws IOException {
    try (FileInputStream fileInputStream = new FileInputStream(propertiesPath)) {
      Config.load(fileInputStream);
    }
  }

  public static void load(Path propertiesPath) throws IOException {
    Config.load(String.valueOf(propertiesPath));
  }

  public static String get(String key) {
    return PROPERTIES.getProperty(key);
  }

  public static String getPackageName() {
    return PROPERTIES.getProperty(PACKAGE_NAME, "com.github.oguzdem.openapi.generated");
  }

  public static String getOutputPath() {
    return PROPERTIES.getProperty(OUTPUT_PATH, "build/generated/sources/");
  }

  public static boolean isBeanValidationEnabled() {
    return Boolean.parseBoolean(PROPERTIES.getProperty(ENABLE_BEAN_VALIDATION_SUPPORT, "True"));
  }

  public static String getFullOutputPath() {
    return Config.getOutputPath() + "/" + Config.getPackageName().replace(".", "/");
  }
}
