package io.github.oguzdem.openapi.generator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Oguz Demirbasci
 */
class NameUtilsTest {

  public static Stream<Arguments> toEnumValueFormatTestArgProvider() {
    return Stream.of(
        Arguments.of("", ""),
        Arguments.of("test", "TEST"),
        Arguments.of("$test", "$TEST"),
        Arguments.of("test value", "TEST_VALUE"),
        Arguments.of("test 123", "TEST_123"),
        Arguments.of("123 test", "$123_TEST"),
        Arguments.of("123 $ test", "$123_$_TEST"),
        Arguments.of("123 test - value", "$123_TEST_VALUE"),
        Arguments.of("[123 test - value]", "_123_TEST_VALUE_"),
        Arguments.of("testValue", "TEST_VALUE"),
        Arguments.of("TestValue", "TEST_VALUE"),
        Arguments.of("TEST_Value", "TEST_VALUE"),
        Arguments.of("123_test", "$123_TEST"),
        Arguments.of("123_test____value", "$123_TEST_VALUE"),
        Arguments.of("_TEST_VALUE", "_TEST_VALUE"));
  }

  public static Stream<Arguments> toJavaFieldNameFormatTestArgProvider() {
    return Stream.of(
        Arguments.of("", ""),
        Arguments.of("test", "test"),
        Arguments.of("$test", "$test"),
        Arguments.of("test value", "testValue"),
        Arguments.of("test 123", "test123"),
        Arguments.of("123 test", "$123Test"),
        Arguments.of("123 $ test", "$123$Test"),
        Arguments.of("123 test - value", "$123TestValue"),
        Arguments.of("[123 test - value]", "$123TestValue"),
        Arguments.of("testValue", "testValue"),
        Arguments.of("TestValue", "testValue"),
        Arguments.of("TEST_Value", "testValue"),
        Arguments.of("123_test", "$123Test"),
        Arguments.of("123_test____value", "$123TestValue"),
        Arguments.of("_TEST_VALUE", "_TestValue"),
        Arguments.of("_TestValue", "_TestValue"),
        Arguments.of("_____testValue", "_TestValue"));
  }

  public static Stream<Arguments> toJavaTypeNameFormatTestArgProvider() {
    return Stream.of(
        Arguments.of("", ""),
        Arguments.of("test", "Test"),
        Arguments.of("$test", "$test"),
        Arguments.of("test value", "TestValue"),
        Arguments.of("test 123", "Test123"),
        Arguments.of("123 test", "$123Test"),
        Arguments.of("123 $ test", "$123$Test"),
        Arguments.of("123 test - value", "$123TestValue"),
        Arguments.of("[123 test - value]", "$123TestValue"),
        Arguments.of("testValue", "TestValue"),
        Arguments.of("TestValue", "TestValue"),
        Arguments.of("TEST_Value", "TestValue"),
        Arguments.of("123_test", "$123Test"),
        Arguments.of("123_test____value", "$123TestValue"),
        Arguments.of("_TEST_VALUE", "TestValue"),
        Arguments.of("_TestValue", "TestValue"),
        Arguments.of("_____testValue", "TestValue"));
  }

  public static Stream<Arguments> toJsonSubTypesListTestArgProvider() {
    return Stream.of(
        Arguments.of(null, null, null),
        Arguments.of(List.of(), null, null),
        Arguments.of(List.of("A"), null, "{@JsonSubTypes.Type(value = A.class)}"),
        Arguments.of(
            List.of("A", "B"),
            null,
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}"),
        Arguments.of(
            null, "{@JsonSubTypes.Type(value = A.class)}", "{@JsonSubTypes.Type(value = A.class)}"),
        Arguments.of(
            null,
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}",
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}"),
        Arguments.of(
            List.of(),
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}",
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}"),
        Arguments.of(
            List.of("C"),
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}",
            "{@JsonSubTypes.Type(value=B.class), @JsonSubTypes.Type(value = C.class), @JsonSubTypes.Type(value=A.class)}"),
        Arguments.of(
            List.of("C", "D"),
            "{@JsonSubTypes.Type(value = A.class), @JsonSubTypes.Type(value = B.class)}",
            "{@JsonSubTypes.Type(value=B.class), @JsonSubTypes.Type(value = C.class), @JsonSubTypes.Type(value = D.class), @JsonSubTypes.Type(value=A.class)}"));
  }

  @ParameterizedTest
  @MethodSource("toEnumValueFormatTestArgProvider")
  void toEnumValueFormatTest(String input, String expected) {
    assertEquals(expected, NameUtils.toEnumValueFormat(input));
  }

  @ParameterizedTest
  @MethodSource("toJavaFieldNameFormatTestArgProvider")
  void toJavaFieldNameFormatTest(String input, String expected) {
    assertEquals(expected, NameUtils.toJavaFieldNameFormat(input));
  }

  @ParameterizedTest
  @MethodSource("toJavaTypeNameFormatTestArgProvider")
  void toJavaTypeNameFormatTest(String input, String expected) {
    assertEquals(expected, NameUtils.toJavaTypeNameFormat(input));
  }

  @ParameterizedTest
  @MethodSource("toJsonSubTypesListTestArgProvider")
  void toJsonSubTypesList(
      List<String> subTypes, String existingSubTypesVal, String expectedSubtypes) {
    assertEquals(expectedSubtypes, NameUtils.toJsonSubTypesList(subTypes, existingSubTypesVal));
  }
}
