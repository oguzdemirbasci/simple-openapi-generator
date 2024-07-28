package org.oguzdem.openapi.generator;

import static io.swagger.v3.parser.util.SchemaTypeUtil.DOUBLE_FORMAT;
import static io.swagger.v3.parser.util.SchemaTypeUtil.INTEGER64_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.common.io.Resources;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Oguz Demirbasci
 */
@Slf4j
public class SchemaToJavaTest {
  private static final String JAVA_FILE_RESOURCE_PATH_TEMPLATE = "java/%s.java";

  private static final SchemaClassFilePathTestPair BASIC_SCHEMA_WITH_ADDITIONAL_PROPS =
      SchemaClassFilePathTestPair.of("BasicAdditionalProps");
  private static final SchemaClassFilePathTestPair BASIC_STRING_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicStrings");
  private static final SchemaClassFilePathTestPair DEFAULT_STRING_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultStrings");
  private static final SchemaClassFilePathTestPair BASIC_INT_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicIntegers");
  private static final SchemaClassFilePathTestPair DEFAULT_INT_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultIntegers");
  private static final SchemaClassFilePathTestPair BASIC_FLOAT_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicFloats");
  private static final SchemaClassFilePathTestPair DEFAULT_FLOAT_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultFloats");
  private static final SchemaClassFilePathTestPair BASIC_LONG_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicLongs");
  private static final SchemaClassFilePathTestPair DEFAULT_LONG_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultLongs");
  private static final SchemaClassFilePathTestPair BASIC_DOUBLE_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicDoubles");
  private static final SchemaClassFilePathTestPair DEFAULT_DOUBLE_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultDoubles");
  private static final SchemaClassFilePathTestPair BASIC_DATE_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicDates");
  private static final SchemaClassFilePathTestPair DEFAULT_DATE_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultDates");
  private static final SchemaClassFilePathTestPair BASIC_DATE_TIME_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicDateTimes");
  private static final SchemaClassFilePathTestPair DEFAULT_DATE_TIME_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultDateTimes");
  private static final SchemaClassFilePathTestPair BASIC_BINARY_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicBinaries");
  private static final SchemaClassFilePathTestPair DEFAULT_BINARY_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultBinaries");
  private static final SchemaClassFilePathTestPair BASIC_BYTES_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicBytes");
  private static final SchemaClassFilePathTestPair DEFAULT_BYTES_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultBytes");
  private static final SchemaClassFilePathTestPair BASIC_BOOLEAN_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicBooleans");
  private static final SchemaClassFilePathTestPair DEFAULT_BOOLEAN_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultBooleans");
  private static final SchemaClassFilePathTestPair BASIC_UUID_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicUuids");
  private static final SchemaClassFilePathTestPair DEFAULT_UUID_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultUuids");
  private static final SchemaClassFilePathTestPair BASIC_URI_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicUris");
  private static final SchemaClassFilePathTestPair DEFAULT_URI_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultUris");
  private static final SchemaClassFilePathTestPair BASIC_URL_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicUrls");
  private static final SchemaClassFilePathTestPair DEFAULT_URL_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultUrls");
  private static final SchemaClassFilePathTestPair BASIC_ARRAY_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicArrays");
  private static final SchemaClassFilePathTestPair BASIC_SET_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicSets");
  private static final SchemaClassFilePathTestPair DEFAULT_ARRAY_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultArrays");
  private static final SchemaClassFilePathTestPair BASIC_ENUM_SCHEMA =
      SchemaClassFilePathTestPair.of("BasicEnums");
  private static final SchemaClassFilePathTestPair DEFAULT_ENUM_SCHEMA =
      SchemaClassFilePathTestPair.of("DefaultEnums");

  private static NumberSchema newFloatSchema() {
    NumberSchema schema = new NumberSchema();
    schema.setFormat("float");
    return schema;
  }

  private static NumberSchema newLongNumberSchema() {
    NumberSchema schema = new NumberSchema();
    schema.setFormat(INTEGER64_FORMAT);
    return schema;
  }

  private static IntegerSchema newLongIntSchema() {
    IntegerSchema schema = new IntegerSchema();
    schema.setFormat(INTEGER64_FORMAT);
    return schema;
  }

  private static NumberSchema newDoubleSchema() {
    NumberSchema schema = new NumberSchema();
    schema.setFormat(DOUBLE_FORMAT);
    return schema;
  }

  private static StringSchema newUriSchema() {
    StringSchema schema = new StringSchema();
    schema.setFormat("uri");
    return schema;
  }

  private static StringSchema newUrlSchema() {
    StringSchema schema = new StringSchema();
    schema.setFormat("url");
    return schema;
  }

  @BeforeAll
  public static void setUpBasicStringSchema() {
    BASIC_STRING_SCHEMA.schema().addRequiredItem("simpleString");
    BASIC_STRING_SCHEMA.schema().addProperty("simpleString", new StringSchema());
    BASIC_STRING_SCHEMA
        .schema()
        .addProperty("lengthLimitedString", new StringSchema().minLength(2).maxLength(4));
    BASIC_STRING_SCHEMA
        .schema()
        .addProperty(
            "regexEmailString",
            new StringSchema()
                .pattern(
                    "^((?!\\\\.)[\\\\w-_.]*[^.])(@\\\\w+)(\\\\.\\\\w+(\\\\.\\\\w+)?[^.\\\\W])$"));
  }

  @BeforeAll
  public static void setUpDefaultStringSchema() {
    DEFAULT_STRING_SCHEMA.schema().addRequiredItem("simpleString");
    DEFAULT_STRING_SCHEMA.schema().addProperty("simpleString", new StringSchema());
    DEFAULT_STRING_SCHEMA
        .schema()
        .addProperty("stringWithDefaultValue", new StringSchema()._default("Default Str"));
    DEFAULT_STRING_SCHEMA
        .schema()
        .addProperty("lengthLimitedString", new StringSchema().minLength(2).maxLength(4));
    DEFAULT_STRING_SCHEMA
        .schema()
        .addProperty(
            "regexEmailString",
            new StringSchema()
                .pattern(
                    "^((?!\\\\.)[\\\\w-_.]*[^.])(@\\\\w+)(\\\\.\\\\w+(\\\\.\\\\w+)?[^.\\\\W])$"));
  }

  @BeforeAll
  public static void setUpBasicSchemaWithAdditionalProps() {
    BASIC_SCHEMA_WITH_ADDITIONAL_PROPS.schema().additionalProperties(Boolean.TRUE);
  }

  @BeforeAll
  public static void setUpBasicIntegersSchema() {
    BASIC_INT_SCHEMA.schema().addProperty("simpleInt", new IntegerSchema());
    BASIC_INT_SCHEMA.schema().addProperty("primitiveInt", new IntegerSchema().nullable(false));
    BASIC_INT_SCHEMA.schema().addProperty("minInt", new IntegerSchema().minimum(BigDecimal.TEN));
    BASIC_INT_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveInt", new IntegerSchema().nullable(false).minimum(BigDecimal.ONE));
    BASIC_INT_SCHEMA.schema().addProperty("maxInt", new IntegerSchema().maximum(BigDecimal.TEN));
    BASIC_INT_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveInt", new IntegerSchema().nullable(false).maximum(BigDecimal.TEN));
    BASIC_INT_SCHEMA
        .schema()
        .addProperty(
            "minMaxInt",
            new IntegerSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    BASIC_INT_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveInt",
            new IntegerSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpDefaultIntegersSchema() {
    DEFAULT_INT_SCHEMA.schema().addProperty("simpleInt", new IntegerSchema());
    DEFAULT_INT_SCHEMA.schema().addProperty("primitiveInt", new IntegerSchema().nullable(false));
    DEFAULT_INT_SCHEMA.schema().addProperty("defaultInt", new IntegerSchema()._default(30));
    DEFAULT_INT_SCHEMA.schema().addProperty("minInt", new IntegerSchema().minimum(BigDecimal.TEN));
    DEFAULT_INT_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveInt", new IntegerSchema().nullable(false).minimum(BigDecimal.ONE));
    DEFAULT_INT_SCHEMA.schema().addProperty("maxInt", new IntegerSchema().maximum(BigDecimal.TEN));
    DEFAULT_INT_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveInt", new IntegerSchema().nullable(false).maximum(BigDecimal.TEN));
    DEFAULT_INT_SCHEMA
        .schema()
        .addProperty(
            "minMaxInt",
            new IntegerSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    DEFAULT_INT_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveInt",
            new IntegerSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpBasicFloatsSchema() {

    BASIC_FLOAT_SCHEMA.schema().addProperty("simpleFloat", newFloatSchema());
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty("defaultFloat", newFloatSchema()._default(BigDecimal.TEN));
    BASIC_FLOAT_SCHEMA.schema().addProperty("primitiveFloat", newFloatSchema().nullable(false));
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty("minFloat", newFloatSchema().minimum(BigDecimal.valueOf(2.5)));
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveFloat", newFloatSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    BASIC_FLOAT_SCHEMA.schema().addProperty("maxFloat", newFloatSchema().maximum(BigDecimal.TEN));
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveFloat", newFloatSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minMaxFloat",
            newFloatSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    BASIC_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveFloat",
            newFloatSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpDefaultFloatsSchema() {

    DEFAULT_FLOAT_SCHEMA.schema().addProperty("simpleFloat", newFloatSchema());
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty("defaultFloat", newFloatSchema()._default(BigDecimal.TEN));
    DEFAULT_FLOAT_SCHEMA.schema().addProperty("primitiveFloat", newFloatSchema().nullable(false));
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty("minFloat", newFloatSchema().minimum(BigDecimal.valueOf(2.5)));
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveFloat", newFloatSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    DEFAULT_FLOAT_SCHEMA.schema().addProperty("maxFloat", newFloatSchema().maximum(BigDecimal.TEN));
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveFloat", newFloatSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minMaxFloat",
            newFloatSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    DEFAULT_FLOAT_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveFloat",
            newFloatSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpBasicLongsSchema() {

    BASIC_LONG_SCHEMA.schema().addProperty("simpleLong", newLongIntSchema());
    BASIC_LONG_SCHEMA.schema().addProperty("primitiveLong", newLongIntSchema().nullable(false));
    BASIC_LONG_SCHEMA
        .schema()
        .addProperty("minLong", newLongNumberSchema().minimum(BigDecimal.valueOf(2.5)));
    BASIC_LONG_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveLong",
            newLongNumberSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    BASIC_LONG_SCHEMA.schema().addProperty("maxLong", newLongIntSchema().maximum(BigDecimal.TEN));
    BASIC_LONG_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveLong",
            newLongNumberSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    BASIC_LONG_SCHEMA
        .schema()
        .addProperty(
            "minMaxLong",
            newLongNumberSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    BASIC_LONG_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveLong",
            newLongIntSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpDefaultLongsSchema() {

    DEFAULT_LONG_SCHEMA.schema().addProperty("simpleLong", newLongIntSchema());
    DEFAULT_LONG_SCHEMA.schema().addProperty("defaultLong", newLongIntSchema()._default(10L));
    DEFAULT_LONG_SCHEMA.schema().addProperty("primitiveLong", newLongIntSchema().nullable(false));
    DEFAULT_LONG_SCHEMA
        .schema()
        .addProperty("minLong", newLongNumberSchema().minimum(BigDecimal.valueOf(2.5)));
    DEFAULT_LONG_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveLong",
            newLongNumberSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    DEFAULT_LONG_SCHEMA.schema().addProperty("maxLong", newLongIntSchema().maximum(BigDecimal.TEN));
    DEFAULT_LONG_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveLong",
            newLongNumberSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    DEFAULT_LONG_SCHEMA
        .schema()
        .addProperty(
            "minMaxLong",
            newLongNumberSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    DEFAULT_LONG_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveLong",
            newLongIntSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpBasicDoublesSchema() {

    BASIC_DOUBLE_SCHEMA.schema().addProperty("simpleDouble", newDoubleSchema());
    BASIC_DOUBLE_SCHEMA.schema().addProperty("primitiveDouble", newDoubleSchema().nullable(false));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty("minDouble", newDoubleSchema().minimum(BigDecimal.valueOf(2.5)));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveDouble",
            newDoubleSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty("maxDouble", newDoubleSchema().maximum(BigDecimal.TEN));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveDouble",
            newDoubleSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minMaxDouble",
            newDoubleSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    BASIC_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveDouble",
            newDoubleSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpDefaultDoublesSchema() {

    DEFAULT_DOUBLE_SCHEMA.schema().addProperty("simpleDouble", newDoubleSchema());
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty("defaultDouble", newDoubleSchema()._default(BigDecimal.ZERO));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty("primitiveDouble", newDoubleSchema().nullable(false));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty("minDouble", newDoubleSchema().minimum(BigDecimal.valueOf(2.5)));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minPrimitiveDouble",
            newDoubleSchema().nullable(false).minimum(BigDecimal.valueOf(7.5)));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty("maxDouble", newDoubleSchema().maximum(BigDecimal.TEN));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "maxPrimitiveDouble",
            newDoubleSchema().nullable(false).maximum(BigDecimal.valueOf(9.7)));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minMaxDouble",
            newDoubleSchema().minimum(BigDecimal.ZERO).maximum(BigDecimal.valueOf(20)));
    DEFAULT_DOUBLE_SCHEMA
        .schema()
        .addProperty(
            "minMaxPrimitiveDouble",
            newDoubleSchema()
                .nullable(false)
                .minimum(BigDecimal.ONE)
                .maximum(BigDecimal.valueOf(5)));
  }

  @BeforeAll
  public static void setUpBasicDatesSchema() {
    BASIC_DATE_SCHEMA.schema().addProperty("simpleDate", new DateSchema());
  }

  @BeforeAll
  public static void setUpDefaultDatesSchema() {
    DEFAULT_DATE_SCHEMA.schema().addProperty("simpleDate", new DateSchema());
    DEFAULT_DATE_SCHEMA
        .schema()
        .addProperty(
            "defaultDate",
            new DateSchema()._default(new GregorianCalendar(2024, Calendar.AUGUST, 27).getTime()));
  }

  @BeforeAll
  public static void setUpBasicDateTimesSchema() {
    BASIC_DATE_TIME_SCHEMA.schema().addProperty("simpleDateTime", new DateTimeSchema());
  }

  @BeforeAll
  public static void setUpDefaultDateTimesSchema() {
    DEFAULT_DATE_TIME_SCHEMA.schema().addProperty("simpleDateTime", new DateTimeSchema());
    DEFAULT_DATE_TIME_SCHEMA
        .schema()
        .addProperty(
            "defaultDateTime",
            new DateTimeSchema()
                ._default(new GregorianCalendar(2024, Calendar.AUGUST, 27, 20, 0, 0).getTime()));
  }

  @BeforeAll
  public static void setUpBasicBinariesSchema() {
    BASIC_BINARY_SCHEMA.schema().addProperty("simpleBinary", new BinarySchema());
  }

  @BeforeAll
  public static void setUpDefaultBinariesSchema() {
    DEFAULT_BINARY_SCHEMA.schema().addProperty("simpleBinary", new BinarySchema());
    DEFAULT_BINARY_SCHEMA
        .schema()
        .addProperty("defaultBinary", new BinarySchema()._default(new byte[] {123, 123, 123}));
  }

  @BeforeAll
  public static void setUpBasicBytesSchema() {
    BASIC_BYTES_SCHEMA.schema().addProperty("simpleBytes", new ByteArraySchema());
  }

  @BeforeAll
  public static void setUpDefaultBytesSchema() {
    DEFAULT_BYTES_SCHEMA.schema().addProperty("simpleBytes", new ByteArraySchema());
    DEFAULT_BYTES_SCHEMA
        .schema()
        .addProperty("defaultBytes", new ByteArraySchema()._default(new byte[] {123, 123, 123}));
  }

  @BeforeAll
  public static void setUpBasicBooleansSchema() {
    BASIC_BOOLEAN_SCHEMA.schema().addProperty("simpleBooleans", new BooleanSchema());
    BASIC_BOOLEAN_SCHEMA
        .schema()
        .addProperty("simplePrimitiveBooleans", new BooleanSchema().nullable(false));
  }

  @BeforeAll
  public static void setUpDefaultBooleansSchema() {
    DEFAULT_BOOLEAN_SCHEMA.schema().addProperty("simpleBooleans", new BooleanSchema());
    DEFAULT_BOOLEAN_SCHEMA
        .schema()
        .addProperty("defaultBooleans", new BooleanSchema()._default(true));
    DEFAULT_BOOLEAN_SCHEMA
        .schema()
        .addProperty("simplePrimitiveBooleans", new BooleanSchema().nullable(false));
  }

  @BeforeAll
  public static void setUpBasicUuidsSchema() {
    BASIC_UUID_SCHEMA.schema().addProperty("simpleUuids", new UUIDSchema());
  }

  @BeforeAll
  public static void setUpDefaultUuidsSchema() {
    DEFAULT_UUID_SCHEMA.schema().addProperty("simpleUuids", new UUIDSchema());
    DEFAULT_UUID_SCHEMA
        .schema()
        .addProperty(
            "defaultUuid",
            new UUIDSchema()._default(UUID.fromString("7e30b3b4-80a5-45c3-8762-cec8bd271dc5")));
  }

  @BeforeAll
  public static void setUpBasicUrisSchema() {
    BASIC_URI_SCHEMA.schema().addProperty("simpleUris", newUriSchema());
  }

  @BeforeAll
  public static void setUpDefaultUrisSchema() {
    DEFAULT_URI_SCHEMA.schema().addProperty("simpleUris", newUriSchema());
    DEFAULT_URI_SCHEMA
        .schema()
        .addProperty("defaultUris", newUriSchema()._default("https://example.com"));
  }

  @BeforeAll
  public static void setUpBasicUrlsSchema() {
    BASIC_URL_SCHEMA.schema().addProperty("simpleUrl", newUrlSchema());
  }

  @BeforeAll
  public static void setUpDefaultUrlsSchema() {
    DEFAULT_URL_SCHEMA
        .schema()
        .addProperty("defaultUrl", newUrlSchema()._default("https://example.com"));
  }

  @BeforeAll
  public static void setUpArraysSchema() {
    BASIC_ARRAY_SCHEMA
        .schema()
        .addProperty("stringArray", new ArraySchema().items(new StringSchema()))
        .addProperty("intArray", new ArraySchema().items(new IntegerSchema().nullable(false)))
        .addProperty(
            "arrayOfArrays", new ArraySchema().items(new ArraySchema().items(new StringSchema())))
        .addProperty(
            "arrayOfObjects",
            new ArraySchema()
                .items(
                    new ObjectSchema()
                        .title("MyNewObject")
                        .addProperty("newObjectStringField", new StringSchema())
                        .addProperty("newObjectIntegerField", new IntegerSchema())))
        .addProperty(
            "arrayOfUnknownObjects",
            new ArraySchema()
                .items(new ObjectSchema().type("").addProperty("dateField", new DateSchema())));
  }

  @BeforeAll
  public static void setUpSetsSchema() {
    BASIC_SET_SCHEMA
        .schema()
        .addProperty("stringSet", new ArraySchema().items(new StringSchema()).uniqueItems(true))
        .addProperty(
            "intSet",
            new ArraySchema().items(new IntegerSchema().nullable(false)).uniqueItems(true))
        .addProperty(
            "arrayOfSets",
            new ArraySchema().items(new ArraySchema().items(new StringSchema()).uniqueItems(true)))
        .addProperty(
            "SetOfObjects",
            new ArraySchema()
                .items(
                    new ObjectSchema()
                        .title("MyNewObject")
                        .addProperty("newObjectStringField", new StringSchema())
                        .addProperty("newObjectIntegerField", new IntegerSchema()))
                .uniqueItems(true))
        .addProperty(
            "setOfArrays",
            new ArraySchema()
                .items(new ObjectSchema().type("").addProperty("dateField", new DateSchema()))
                .uniqueItems(true));
  }

  @BeforeAll
  public static void setUpEnumsSchema() {
    BASIC_ENUM_SCHEMA
        .schema()
        .addProperty(
            "simpleEnum",
            new StringSchema().addEnumItem("VALUE_1").addEnumItem("VALUE_2").title("NewEnum"));
  }

  public static Stream<Arguments> objectSchemaToJavaTestArgProvider() {
    return Stream.of(
        Arguments.of(BASIC_STRING_SCHEMA.schema(), BASIC_STRING_SCHEMA.classFilePath()),
        Arguments.of(
            BASIC_SCHEMA_WITH_ADDITIONAL_PROPS.schema(),
            BASIC_SCHEMA_WITH_ADDITIONAL_PROPS.classFilePath()),
        Arguments.of(BASIC_INT_SCHEMA.schema(), BASIC_INT_SCHEMA.classFilePath()),
        Arguments.of(BASIC_FLOAT_SCHEMA.schema(), BASIC_FLOAT_SCHEMA.classFilePath()),
        Arguments.of(BASIC_LONG_SCHEMA.schema(), BASIC_LONG_SCHEMA.classFilePath()),
        Arguments.of(BASIC_DOUBLE_SCHEMA.schema(), BASIC_DOUBLE_SCHEMA.classFilePath()),
        Arguments.of(BASIC_DATE_SCHEMA.schema(), BASIC_DATE_SCHEMA.classFilePath()),
        Arguments.of(BASIC_DATE_TIME_SCHEMA.schema(), BASIC_DATE_TIME_SCHEMA.classFilePath()),
        Arguments.of(BASIC_BINARY_SCHEMA.schema(), BASIC_BINARY_SCHEMA.classFilePath()),
        Arguments.of(BASIC_BYTES_SCHEMA.schema(), BASIC_BYTES_SCHEMA.classFilePath()),
        Arguments.of(BASIC_BOOLEAN_SCHEMA.schema(), BASIC_BOOLEAN_SCHEMA.classFilePath()),
        Arguments.of(BASIC_UUID_SCHEMA.schema(), BASIC_UUID_SCHEMA.classFilePath()),
        Arguments.of(BASIC_URI_SCHEMA.schema(), BASIC_URI_SCHEMA.classFilePath()),
        Arguments.of(BASIC_URL_SCHEMA.schema(), BASIC_URL_SCHEMA.classFilePath()),
        Arguments.of(BASIC_ARRAY_SCHEMA.schema(), BASIC_ARRAY_SCHEMA.classFilePath()),
        Arguments.of(BASIC_SET_SCHEMA.schema(), BASIC_SET_SCHEMA.classFilePath()),
        Arguments.of(BASIC_ENUM_SCHEMA.schema(), BASIC_ENUM_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_STRING_SCHEMA.schema(), DEFAULT_STRING_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_INT_SCHEMA.schema(), DEFAULT_INT_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_FLOAT_SCHEMA.schema(), DEFAULT_FLOAT_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_LONG_SCHEMA.schema(), DEFAULT_LONG_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_DOUBLE_SCHEMA.schema(), DEFAULT_DOUBLE_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_DATE_SCHEMA.schema(), DEFAULT_DATE_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_DATE_TIME_SCHEMA.schema(), DEFAULT_DATE_TIME_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_BINARY_SCHEMA.schema(), DEFAULT_BINARY_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_BYTES_SCHEMA.schema(), DEFAULT_BYTES_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_BOOLEAN_SCHEMA.schema(), DEFAULT_BOOLEAN_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_UUID_SCHEMA.schema(), DEFAULT_UUID_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_URI_SCHEMA.schema(), DEFAULT_URI_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_URL_SCHEMA.schema(), DEFAULT_URL_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_ARRAY_SCHEMA.schema(), DEFAULT_ARRAY_SCHEMA.classFilePath()),
        Arguments.of(DEFAULT_ENUM_SCHEMA.schema(), DEFAULT_ENUM_SCHEMA.classFilePath()));
  }

  @ParameterizedTest
  @MethodSource("objectSchemaToJavaTestArgProvider")
  void objectSchemaToJavaTest(ObjectSchema schema, URL expectedJavaFilePath) throws IOException {
    JavaType<?> expectedClass = Roaster.parse(expectedJavaFilePath);
    assertNotNull(expectedClass);
    String expectedFile = Roaster.format(expectedClass.toUnformattedString());
    String generatedFile =
        Roaster.format(PojoGenerator.generate(schema.getTitle(), schema, new Components()));
    assertNotNull(expectedFile);
    assertNotNull(generatedFile);
    log.info("Expected: {}", expectedFile);
    log.info("Generated: {}", generatedFile);
    assertEquals(
        expectedFile.replaceAll("[\\r\\n\\t\\s]", ""),
        generatedFile.replaceAll("[\\r\\n\\t\\s]", ""));
  }

  private record SchemaClassFilePathTestPair(ObjectSchema schema, URL classFilePath) {
    static SchemaClassFilePathTestPair of(@NonNull String objectName) {
      ObjectSchema schema = new ObjectSchema();
      schema.setTitle(objectName);

      String classFilePath = String.format(JAVA_FILE_RESOURCE_PATH_TEMPLATE, objectName);
      return new SchemaClassFilePathTestPair(schema, Resources.getResource(classFilePath));
    }
  }
}
