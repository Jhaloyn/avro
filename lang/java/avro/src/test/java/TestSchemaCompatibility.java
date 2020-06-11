import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.SchemaCompatibility;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestSchemaCompatibility {

  @Rule
  public ExpectedException expectedException;
  private List<Schema> schemasList;
  private Object expected;

  public TestSchemaCompatibility(List<Schema> schemasList, Object expected) {

    this.schemasList = schemasList;
    this.expected = expected;
    this.expectedException = ExpectedException.none();
  }

  // Creazione casi di test parametrizzati
  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] {

        { generateSimpleSchemasWithSameType(Type.BOOLEAN), true },
        { generateSchemasWithDifferentType(Type.BYTES, Type.BOOLEAN), false }, // semplici e tipo diverso
        { generateSimpleSchemasWithSameType(Type.DOUBLE), true },
        { generateSimpleSchemasWithSameType(Type.FLOAT), true }, { generateSimpleSchemasWithSameType(Type.INT), true },
        { generateSimpleSchemasWithSameType(Type.LONG), true }, { generateSimpleSchemasWithSameType(Type.NULL), true },
        { generateSimpleSchemasWithSameType(Type.STRING), true },
        { generateComplexSchemasWithSameType(Type.RECORD, true), true }, // complessi stessi tipo e stringa
        { generateComplexSchemasWithSameType(Type.ARRAY, false), false }, // complex stesso tipo diversa stringa
        { generateSchemasWithDifferentType(Type.ENUM, Type.ARRAY), false }, // complex tipo diverso
        { generateComplexSchemasWithSameType(Type.FIXED, true), true },
        { generateComplexSchemasWithSameType(Type.MAP, true), true },
        { generateComplexSchemasWithSameType(Type.UNION, true), true },
        { generateSchemasWithDifferentType(null, null), new NullPointerException()/* new AssertionError() */ }, // entrambi
        // gli
        // schemi
        // sono
        // null
    };

    return Arrays.asList(data);
  }

  @Test
  public void checkReaderWriterCompatibilityTest() {

//    if (expected instanceof AssertionError) {
//      expectedException.expect(AssertionError.class);
//    }

    if (expected instanceof NullPointerException) {
      expectedException.expect(NullPointerException.class);
    }

    List<SchemaCompatibility.Incompatibility> result = SchemaCompatibility
        .checkReaderWriterCompatibility(schemasList.get(0), schemasList.get(1)).getResult().getIncompatibilities();

    assertEquals(expected, result.isEmpty());

  }

  /**
   * Metodo per generare schemi di tipo uguale e complessi, con stringa uguale o
   * diversa
   * 
   * @param typeSchema
   * @param isEquals
   * @return
   */
  private static List<Schema> generateComplexSchemasWithSameType(Type typeSchema, boolean isEquals) {

    switch (typeSchema) {

    case STRING:
      return SchemaUtils.generateStringSchemas();
    case BYTES:
      return SchemaUtils.generateBytesSchemas();
    case INT:
      return SchemaUtils.generateIntSchemas();
    case LONG:
      return SchemaUtils.generateLongSchemas();
    case FLOAT:
      return SchemaUtils.generateFloatSchemas();
    case DOUBLE:
      return SchemaUtils.generateDoubleSchemas();
    case BOOLEAN:
      return SchemaUtils.generateBooleanSchemas();
    case RECORD:
      SchemaUtils.generateRecordSchemas(isEquals);
    case MAP:
      return SchemaUtils.generateMapSchemas(isEquals);
    case FIXED:
      return SchemaUtils.generateFixedSchemas(isEquals);
    case ARRAY:
      return SchemaUtils.generateArraySchemas(isEquals);
    case ENUM:
      return SchemaUtils.generateEnumSchemas(isEquals);
    case UNION:
      return SchemaUtils.generateUnionSchemas(isEquals);
    case NULL:
      return SchemaUtils.generateNullSchemas();

    default:
      return null;
    }
  }

  /**
   * Metodo per generare schemi di tipo uguale e semplici
   * 
   * @param typeSchema
   * @return
   */
  private static List<Schema> generateSimpleSchemasWithSameType(Type typeSchema) {

    switch (typeSchema) {

    case STRING:
      return SchemaUtils.generateStringSchemas();
    case BYTES:
      return SchemaUtils.generateBytesSchemas();
    case INT:
      return SchemaUtils.generateIntSchemas();
    case LONG:
      return SchemaUtils.generateLongSchemas();
    case FLOAT:
      return SchemaUtils.generateFloatSchemas();
    case DOUBLE:
      return SchemaUtils.generateDoubleSchemas();
    case BOOLEAN:
      return SchemaUtils.generateBooleanSchemas();
    case NULL:
      return SchemaUtils.generateNullSchemas();

    default:
      return null;
    }
  }

  /**
   * Metodo per generare schemi di tipo diverso
   * 
   * @param typeSchema1
   * @param typeSchema2
   * @return
   */
  private static List<Schema> generateSchemasWithDifferentType(Type typeSchema1, Type typeSchema2) {

    ArrayList<Schema> schemasList = new ArrayList<Schema>();

    if (typeSchema1 == null && typeSchema2 == null) {

      schemasList.add(null);
      schemasList.add(null);
      return schemasList;

    } else if (typeSchema1 == null || typeSchema2 == null) {

      schemasList.add(null);
      // Un tipo vale l'altro: quando uno dei due schema è null, il
      // controllo di compatibilità lancia un'eccezione
      schemasList.add(Schema.create(Type.BOOLEAN));
      return schemasList;
    }

    if (typeSchema1.equals(Type.BOOLEAN) || typeSchema2.equals(Type.BOOLEAN)) {
      schemasList.add(Schema.create(Type.BOOLEAN));
    }
    if (typeSchema1.equals(Type.BYTES) || typeSchema2.equals(Type.BYTES)) {
      schemasList.add(Schema.create(Type.BYTES));
    }
    if (typeSchema1.equals(Type.DOUBLE) || typeSchema2.equals(Type.DOUBLE)) {
      schemasList.add(Schema.create(Type.DOUBLE));
    }
    if (typeSchema1.equals(Type.FLOAT) || typeSchema2.equals(Type.FLOAT)) {
      schemasList.add(Schema.create(Type.FLOAT));
    }
    if (typeSchema1.equals(Type.INT) || typeSchema2.equals(Type.INT)) {
      schemasList.add(Schema.create(Type.INT));
    }
    if (typeSchema1.equals(Type.LONG) || typeSchema2.equals(Type.LONG)) {
      schemasList.add(Schema.create(Type.LONG));
    }
    if (typeSchema1.equals(Type.NULL) || typeSchema2.equals(Type.NULL)) {
      schemasList.add(Schema.create(Type.NULL));
    }
    if (typeSchema1.equals(Type.STRING) || typeSchema2.equals(Type.STRING)) {
      schemasList.add(Schema.create(Type.STRING));
    }
    if (typeSchema1.equals(Type.RECORD) || typeSchema2.equals(Type.RECORD)) {
      schemasList.add(SchemaUtils.generateRecordSchema());
    }
    if (typeSchema1.equals(Type.MAP) || typeSchema2.equals(Type.MAP)) {
      schemasList.add(SchemaUtils.generateMapSchema());
    }
    if (typeSchema1.equals(Type.FIXED) || typeSchema2.equals(Type.FIXED)) {
      schemasList.add(SchemaUtils.generateFixedSchema());
    }
    if (typeSchema1.equals(Type.ARRAY) || typeSchema2.equals(Type.ARRAY)) {
      schemasList.add(SchemaUtils.generateArraySchema());
    }
    if (typeSchema1.equals(Type.ENUM) || typeSchema2.equals(Type.ENUM)) {
      schemasList.add(SchemaUtils.generateEnumSchema());
    }
    if (typeSchema1.equals(Type.UNION) || typeSchema2.equals(Type.UNION)) {
      schemasList.add(SchemaUtils.generateUnionSchema());
    }

    return schemasList;
  }

}
