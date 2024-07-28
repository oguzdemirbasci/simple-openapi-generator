package org.oguzdem.openapi.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Oguz Demirbasci
 */
public final class Config {

  private static final Properties PROPERTIES = new Properties(System.getProperties());

  private Config() {}

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
    return PROPERTIES.getProperty("packageName", "org.oguzdem.openapi.generated");
  }

  public static String getOutputPath() {
    return PROPERTIES.getProperty("outputPath", "build/generated/sources/");
  }

  public static boolean isBeanValidationEnabled() {
    return Boolean.parseBoolean(PROPERTIES.getProperty("enableBeanValidationSupport", "True"));
  }

  public static String getFullOutputPath() {
    return Config.getOutputPath() + "/" + Config.getPackageName().replace(".", "/");
  }
}
