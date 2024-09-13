package io.github.oguzdem.openapi.generator.utils;

import com.google.common.base.CaseFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import joptsimple.internal.Strings;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for converting names to different formats.
 *
 * @author Oguz Demirbasci
 */
public class NameUtils {

  private static final String JSON_SUB_TYPE_ANNOTATION_TEMPLATE =
      "@JsonSubTypes.Type(value = %s.class)";

  /**
   * Converts the input string to a format that is suitable for enum values. It replaces all
   * non-alphanumeric characters except {@code $} with underscores and converts the string to
   * uppercase.
   *
   * @param input the input string
   * @return the formatted string
   */
  public static String toEnumValueFormat(@NonNull String input) {
    String camelCaseHandled =
        input
            .replaceAll("([a-z])([A-Z])", "$1_$2") // Insert underscore before each uppercase letter
            .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2"); // Handle cases like "XMLHttpRequest"

    // Convert to uppercase
    String upperCased = camelCaseHandled.toUpperCase();
    // Replace all non-alphanumeric characters with underscores
    String withUnderscores = upperCased.replaceAll("[^A-Z0-9$_]", "_");
    if (withUnderscores.matches("^[0-9].*+")) {
      withUnderscores = "$" + withUnderscores;
    }
    // Remove repeating underscores
    return withUnderscores.replaceAll("_+", "_");
  }

  /**
   * Converts the input string to a format that is suitable for Java field names. It replaces all
   * non-alphanumeric characters except {@code $} with underscores and converts the string to lower
   * camel case.
   *
   * @param input the input string
   * @return the formatted string
   */
  public static String toJavaFieldNameFormat(@NonNull String input) {
    // Insert underscore before each uppercase letter
    String camelCaseHandled =
        input.replaceAll("([a-z])([A-Z])", "$1_$2").replaceAll("([A-Z])([A-Z][a-z])", "$1_$2");
    // Replace non-alphanumeric or $ characters with underscore
    String sanitized = camelCaseHandled.replaceAll("[^a-zA-Z0-9_$]", "_");
    // Add $ prefix to leading digits or leading digits followed by _
    sanitized = sanitized.replaceAll("^(?:_+(\\d+)|(\\d+))", "\\$$1$2");
    String preservedFirstChar =
        !sanitized.isEmpty() && sanitized.charAt(0) == '_' ? "_" : Strings.EMPTY;
    String leadingUnderscoreRemoved = sanitized.replaceFirst("^_+", "");
    String camelCased =
        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, leadingUnderscoreRemoved);
    return StringUtils.isNotBlank(preservedFirstChar)
        ? preservedFirstChar + camelCased.substring(0, 1).toUpperCase() + camelCased.substring(1)
        : camelCased;
  }

  /**
   * Converts the input string to a format that is suitable for Java type names. It replaces all
   * non-alphanumeric characters except {@code $} with underscores and converts the string to upper
   * camel case.
   *
   * @param input the input string
   * @return the formatted string
   */
  public static String toJavaTypeNameFormat(@NonNull String input) {
    String fieldNameFormatted = toJavaFieldNameFormat(input);

    return StringUtils.isNotBlank(fieldNameFormatted)
        ? fieldNameFormatted.substring(0, 1).toUpperCase() + fieldNameFormatted.substring(1)
        : fieldNameFormatted;
  }

  public static String toJsonSubTypesList(List<String> subTypes, String existingSubTypesVal) {
    if (ObjectUtils.isEmpty(subTypes)) {
      return existingSubTypesVal;
    }
    Set<String> existingSubTypes = new HashSet<>();
    if (StringUtils.isNotBlank(existingSubTypesVal)) {
      String parsed = existingSubTypesVal.replaceAll("^[{]| |[}]$", "");
      existingSubTypes.addAll(Arrays.asList(parsed.split(",")));
    }
    existingSubTypes.addAll(
        subTypes.stream()
            .map(str -> String.format(JSON_SUB_TYPE_ANNOTATION_TEMPLATE, str))
            .toList());
    return "{%s}".formatted(String.join(", ", new ArrayList<>(existingSubTypes)));
  }
}
