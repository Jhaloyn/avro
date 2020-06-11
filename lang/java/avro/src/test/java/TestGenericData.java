import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenericData {

  @Rule
  public ExpectedException expectedException;
  private Schema schema;
  private Object object1;
  private Object object2;
  private Object expectedValidate;
  private Object expectedCompare;

  public TestGenericData(Schema schema, Object object1, Object object2, Object expectedValidate,
      Object expectedCompare) {

    this.expectedException = expectedException.none();
    this.schema = schema;
    this.object1 = object1;
    this.object2 = object2;
    this.expectedValidate = expectedValidate;
    this.expectedCompare = expectedCompare;
  }

  // Creazione casi di test parametrizzati
  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] {

        // Test senza metodo compare

//				{ Schema.create(Type.INT), 1, true }, { Schema.create(Type.DOUBLE), 1.2, true },
//				{ Schema.create(Type.FLOAT), 1.2F, true }, { Schema.create(Type.BOOLEAN), false, true },
//				{ Schema.create(Type.LONG), 1L, true }, { Schema.create(Type.NULL), null, true },
//				{ Schema.create(Type.STRING), "string", true },
//				{ Schema.create(Type.BYTES), ByteBuffer.allocate(1), true },
//				{ SchemaCompatibilityUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum(1, 2, 3), true },
//				{ SchemaCompatibilityUtils.generateMapSchema(),
//						CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3), true },
//				{ SchemaCompatibilityUtils.generateRecordSchema(), CreateDatumUtils.createRecordDatum("joe", "black"),
//						true },
//				{ SchemaCompatibilityUtils.generateEnumSchema(), CreateDatumUtils.createEnumSymbolDatum("ONE"), true },
//				{ SchemaCompatibilityUtils.generateFixedSchema(), CreateDatumUtils.createFixedDatum(1048576), true },
//				{ SchemaCompatibilityUtils.generateUnionSchema(), CreateDatumUtils.createUnionDatum(null, 23), true },
//				{ Schema.create(Type.NULL), null, true },
        // ----------------------------------------------------------------------------

        { Schema.create(Type.INT), 1, 1, true, true }, { Schema.create(Type.FLOAT), 1.0F, 1.1F, true, false },
        { Schema.create(Type.LONG), 1L, 1L, true, true }, { Schema.create(Type.DOUBLE), 1.0, 2.0, true, false },
        { Schema.create(Type.STRING), "c", "c", true, true },
        { Schema.create(Type.BYTES), ByteBuffer.allocate(1), ByteBuffer.allocate(2), true, false },
        { Schema.create(Type.BOOLEAN), true, true, true, true },
        { SchemaUtils.generateFixedSchema(), CreateDatumUtils.createFixedDatum(1048576),
            CreateDatumUtils.createFixedDatum(1048545), true, false },
        { SchemaUtils.generateRecordSchema(), CreateDatumUtils.createRecordDatum("joe", "black"),
            CreateDatumUtils.createEnumSymbolDatum("ONE"), true, new Exception() },
        { SchemaUtils.generateMapSchema(), 1, CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3), false,
            new Exception() },
        { SchemaUtils.generateEnumSchema(), CreateDatumUtils.createEnumSymbolDatum("ONE"),
            CreateDatumUtils.createEnumSymbolDatum("TWO"), true, false },
        { SchemaUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum(1, 2, 3),
            CreateDatumUtils.createArrayDatum(1, 2, 4), true, false },
        { SchemaUtils.generateUnionSchema(), CreateDatumUtils.createUnionDatum(null, 23),
            CreateDatumUtils.createUnionDatum(1, 23), true, false },
        { Schema.create(Type.NULL), null, null, true, true },
        { null, 1, "c", new NullPointerException(), new NullPointerException() }

        // Test particolari magari da aggiungere con la coverage

        // { SchemaCompatibilityUtils.generateMapSchema(),
//						CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3),
//						CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3), true,
//						new AvroRuntimeException("Can't compare maps!") }, // che senso ha non poter comparare mai due
//																			// mappe?
//				{ SchemaCompatibilityUtils.generateEnumSchema(), CreateDatumUtils.createEnumSymbolDatum("ONE"),
//				CreateDatumUtils.createEnumSymbolDatum("UNO"), true, new NullPointerException() }

    };
    return Arrays.asList(data);
  }

  @Test
  public void validateTest() {

    if (expectedValidate instanceof NullPointerException) {
      expectedException.expect(NullPointerException.class);
    }

    GenericData genericData = new GenericData();
    boolean result = genericData.validate(schema, object1);

    assertEquals(expectedValidate, result);

  }

  @Test
  public void compareTest() {

    GenericData genericData = new GenericData();

    if (expectedCompare instanceof NullPointerException) {
      expectedException.expect(NullPointerException.class);
    }
    if (expectedCompare instanceof AvroRuntimeException) {
      expectedException.expect(AvroRuntimeException.class);
    }

    if (expectedCompare instanceof Exception) {
      expectedException.expect(Exception.class);
    }

    int differences = genericData.compare(object1, object2, schema);

    if (differences == 0) {
      assertEquals(expectedCompare, true);
    } else {
      assertEquals(expectedCompare, false);
    }

  }

}
