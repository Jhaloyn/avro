import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

/**
 * Classe per generare schemi
 * 
 * @author jhaloyn
 *
 */

public class SchemaUtils {

  private static String record1 = "{\"type\": \"record\",\n" + "\"namespace\": \"com.example\",\n"
      + "\"name\": \"FullName\",\n" + "\"fields\": [\n" + "{\"name\": \"first\", \"type\": [\"string\", \"null\"] },\n"
      + "{\"name\": \"last\", \"type\": \"string\", \"default\" : \"Doe\" }\n" + "]\n" + "} ";

  private static String record2 = "{\"type\": \"record\",\n" + "\"namespace\": \"com.example\",\n"
      + "\"name\": \"FullName\",\n" + "\"fields\": [\n" + "{\"name\": \"first\", \"type\": [\"string\", \"null\"] },\n"
      + "{\"name\": \"last\", \"type\": \"int\", \"default\" : 12 }\n" + "]\n" + "} ";

  private static String map1 = "{\"type\" : \"map\", \"values\" : \"int\"}";
  private static String map2 = "{\"type\" : \"map\", \"values\" : \"string\"}";

  private static String array1 = "{\"type\" : \"array\", \"items\" : \"int\"}";
  private static String array2 = "{\"type\" : \"array\", \"items\" : \"string\"}";

  private static String union1 = "{\"type\" : \"record\", \n" + "   \"namespace\" : \"Avro\", \n"
      + "   \"name\" : \"empdetails\", \n" + "\"fields\" : \n" + "[ \n"
      + "      {\"name\" : \"experience\", \"type\": [\"int\", \"null\"] }, {\"name\" : \"age\", \"type\": \"int\" } \n"
      + "   ] \n" + "}";

  private static String union2 = "{\"type\" : \"record\", \n" + "   \"namespace\" : \"Avro\", \n"
      + "   \"name\" : \"empdetails\", \n" + "\"fields\" : \n" + "   [ \n"
      + "      {\"name\" : \"experience\", \"type\": [\"int\", \"null\"] }, {\"name\" : \"age\", \"type\": \"string\" } \n"
      + "   ] \n" + "}";

  private static String fixed1 = "{\"type\" : \"fixed\" , \"name\" : \"bdata\", \"size\" : 1048576}";
  private static String fixed2 = "{\"type\" : \"fixed\" , \"name\" : \"bdata\", \"size\" : 1048567}";

  private static String enum1 = "{\"type\" : \"enum\",\n" + "\"name\" : \"Numbers\", \n" + "\"namespace\": \"data\", \n"
      + "\"symbols\" : [\"ONE\", \"TWO\", \"THREE\"]\n" + "}";

  private static String enum2 = "{\"type\" : \"enum\",\n" + "\"name\" : \"Numbers\", \n" + "\"namespace\": \"data\", \n"
      + "\"symbols\" : [\"UNO\", \"DUE\", \"TRE\"]\n" + "}";

  public static List<Schema> generateIntSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.INT));
    schemasList.add(Schema.create(Type.INT));
    return schemasList;
  }

  public static List<Schema> generateBytesSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.BYTES));
    schemasList.add(Schema.create(Type.BYTES));
    return schemasList;
  }

  public static List<Schema> generateStringSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.STRING));
    schemasList.add(Schema.create(Type.STRING));
    return schemasList;
  }

  public static List<Schema> generateLongSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.LONG));
    schemasList.add(Schema.create(Type.LONG));
    return schemasList;
  }

  public static List<Schema> generateFloatSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.FLOAT));
    schemasList.add(Schema.create(Type.FLOAT));
    return schemasList;
  }

  public static List<Schema> generateDoubleSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.DOUBLE));
    schemasList.add(Schema.create(Type.DOUBLE));
    return schemasList;
  }

  public static List<Schema> generateBooleanSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.BOOLEAN));
    schemasList.add(Schema.create(Type.BOOLEAN));
    return schemasList;
  }

  public static List<Schema> generateNullSchemas() {

    List<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(Schema.create(Type.NULL));
    schemasList.add(Schema.create(Type.NULL));
    return schemasList;
  }

  private static List<Schema> generateComplexSchemas(String string1, String string2, boolean isEquals) {

    ArrayList<Schema> schemasList = new ArrayList<Schema>();
    schemasList.add(new Schema.Parser().parse(string1));

    if (isEquals) {
      schemasList.add(new Schema.Parser().parse(string1));
    }
    schemasList.add(new Schema.Parser().parse(string2));

    return schemasList;
  }

  public static List<Schema> generateRecordSchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(record1, record2, isEquals);
    return schemasList;
  }

  public static Schema generateRecordSchema() {

    Schema schema = new Schema.Parser().parse(record1);
    return schema;
  }

  public static List<Schema> generateMapSchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(map1, map2, isEquals);
    return schemasList;
  }

  public static Schema generateMapSchema() {

    Schema schema = new Schema.Parser().parse(map1);
    return schema;
  }

  public static List<Schema> generateArraySchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(array1, array2, isEquals);
    return schemasList;

  }

  public static Schema generateArraySchema() {

    Schema schema = new Schema.Parser().parse(array1);
    return schema;
  }

  public static List<Schema> generateUnionSchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(union1, union2, isEquals);
    return schemasList;
  }

  public static Schema generateUnionSchema() {

    Schema schema = new Schema.Parser().parse(union1);
    return schema;
  }

  public static List<Schema> generateFixedSchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(fixed1, fixed2, isEquals);
    return schemasList;
  }

  public static Schema generateFixedSchema() {

    Schema schema = new Schema.Parser().parse(fixed1);
    return schema;
  }

  public static List<Schema> generateEnumSchemas(boolean isEquals) {

    List<Schema> schemasList = generateComplexSchemas(enum1, enum2, isEquals);
    return schemasList;
  }

  public static Schema generateEnumSchema() {

    Schema schema = new Schema.Parser().parse(enum1);
    return schema;
  }

}
