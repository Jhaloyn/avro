import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.UnresolvedUnionException;
import org.apache.avro.generic.GenericData;
import org.apache.avro.util.Utf8;
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

		this.expectedException = ExpectedException.none();
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

				// ----------------Category Partition--------------------------

				{ Schema.create(Type.INT), 1, 1, true, true }, { Schema.create(Type.FLOAT), 1.0F, 1.1F, true, false },
				{ Schema.create(Type.LONG), 1L, 1L, true, true }, { Schema.create(Type.DOUBLE), 1.0, 1.1, true, false },
				{ Schema.create(Type.STRING), "c", "a", true, false }, // modificato per coverage linea 1153
				{ Schema.create(Type.BYTES), ByteBuffer.allocate(1), ByteBuffer.allocate(2), true, false },
				{ Schema.create(Type.BOOLEAN), true, true, true, true },
				{ SchemaUtils.generateFixedSchema(), CreateDatumUtils.createFixedDatum(0),
						CreateDatumUtils.createFixedDatum(1), true, false },
				{ SchemaUtils.generateRecordDatumSchema(), CreateDatumUtils.createRecordDatum("joe", "black"),
						CreateDatumUtils.createEnumSymbolDatum("ONE"), true, new Exception() },
				{ SchemaUtils.generateMapSchema(), 1, CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3),
						false, new AvroRuntimeException("Can't compare maps!") },
				{ SchemaUtils.generateEnumSchema(), CreateDatumUtils.createEnumSymbolDatum("ONE"),
						CreateDatumUtils.createEnumSymbolDatum("TWO"), true, false },
				{ SchemaUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum(1, 2, 3),
						CreateDatumUtils.createArrayDatum(1, 2, 4), true, false },
				{ SchemaUtils.generateUnionSchema(), CreateDatumUtils.createUnionDatum(null, 23),
						CreateDatumUtils.createUnionDatum(1, 23), true, false },
				{ Schema.create(Type.NULL), null, null, true, true },
				{ null, 1, "c", new NullPointerException(), new NullPointerException() },

				// --------------------------Coverage---------------------------
				{ SchemaUtils.generateRecordDatumSchema(), 1, CreateDatumUtils.createEnumSymbolDatum("ONE"), false,
						new Exception() }, // linea 562
				{ SchemaUtils.generateRecordSchema(), CreateDatumUtils.createRecordDatum("joe", "black"),
						CreateDatumUtils.createEnumSymbolDatum("ONE"), false, new Exception() }, // linea 566
				{ SchemaUtils.generateEnumSchema(), CreateDatumUtils.createFixedDatum(1),
						CreateDatumUtils.createEnumSymbolDatum("ONE"), false, new Exception() }, // linea 571
				{ SchemaUtils.generateArraySchema(), 1, 1, false, true }, // linea 575
				{ SchemaUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum("1", "2", "4"),
						CreateDatumUtils.createArrayDatum("1", "2"), false, false }, // linea 578 e linea 1141
				{ SchemaUtils.generateMapSchema(), CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3),
						CreateDatumUtils.createMapDatum("Pari", "Dispari", 2, 4, 3), true,
						new AvroRuntimeException("Can't compare maps!") }, // linea 588
				{ SchemaUtils.generateMapSchema(),
						CreateDatumUtils.createMapDatum("Vocali", "Consonanti", "a", "e", "s"),
						CreateDatumUtils.createMapDatum("Vocali", "Consonanti", "a", "e", "s"), false,
						new AvroRuntimeException("Can't compare maps!") }, // linea 587
				{ SchemaUtils.generateFixedSchema(), CreateDatumUtils.createFixedDatum(1),
						CreateDatumUtils.createFixedDatum(1), false, true }, // linea 597
				{ SchemaUtils.generateFixedSchema(), 1, 1, false, true }, // linea 597
				{ null, null, 1, new NullPointerException(), new NullPointerException() }, // linea 613
				{ Schema.create(Type.NULL), "a", 1, false, true }, // linee 1151 e 613
				{ Schema.create(Type.STRING), new Utf8("c"), 1, true, false }, // linea 1153
				{ Schema.create(Type.STRING), 1, new Utf8("a"), false, false }, // linea 1153
				{ Schema.create(Type.STRING), 1, 1, false, true }, // linea 1153
				{ Schema.create(Type.STRING), new Utf8("c"), new Utf8("a"), true, false }, // linea 1153
				{ SchemaUtils.generateRecordDatumSchema(), CreateDatumUtils.createRecordDatum("joe", "black"),
						CreateDatumUtils.createRecordDatum("joe", "black"), true, true }, // linea 1127
				{ SchemaUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum("1", "2"),
						CreateDatumUtils.createArrayDatum("1", "2", "3"), false, false }, // linea 1141
				{ SchemaUtils.generateArraySchema(), CreateDatumUtils.createArrayDatum("1", "2", "3"),
						CreateDatumUtils.createArrayDatum("1", "2", "3"), false, true }, // linea 1141
				// linea 876
				{ SchemaUtils.generateUnionSchema(), CreateDatumUtils.createUnionDatum(1, 23),
						CreateDatumUtils.createUnionDatum("s", 23), true,
						new UnresolvedUnionException(SchemaUtils.generateUnionSchema(),
								CreateDatumUtils.createUnionDatum("s", 23)) },
				// linea 594
				{ SchemaUtils.generateUnionSchema(), CreateDatumUtils.createUnionDatum("a", 23),
						CreateDatumUtils.createUnionDatum(1, 23), false,
						new UnresolvedUnionException(SchemaUtils.generateUnionSchema(),
								CreateDatumUtils.createUnionDatum(1, 23)) },

				// --------------------------Mutation..................................
				// mutazione true linea 572
				{ SchemaUtils.generateEnumSchema(), CreateDatumUtils.createEnumSymbolDatum("UNO"),
						CreateDatumUtils.createEnumSymbolDatum("TWO"), false, new NullPointerException() },
				{ Schema.create(Type.BYTES), 1, 1, false, true }, // mutazione true linea 601
				{ Schema.create(Type.LONG), "a", "a", false, true }, // mutazione true linea 605 e 1035
				{ Schema.create(Type.FLOAT), "a", "a", false, true }, // mutazione true linea 607 e 1042
				{ Schema.create(Type.DOUBLE), "a", "a", false, true }, // mutazione true linea 609 e 1049
				{ Schema.create(Type.BOOLEAN), "a", "a", false, true }, // mutazione true linea 611 e 1056
				// mutazioni linea 1003, 1012
				{ SchemaUtils.generateRecordDatumSchemaForMutation(), CreateDatumUtils.createRecordDatumMutation("joe"),
						CreateDatumUtils.createRecordDatumMutation("joe"), false, new UnresolvedUnionException(
								SchemaUtils.generateUnionSchema(), CreateDatumUtils.createRecordDatumMutation("joe")) },

		};

		return Arrays.asList(data);
	}

	// NOTA: mappe uguali resituiscono un'eccezione perchè il confronto tra le
	// mappe viene fatto solo quando il valore "equals" del metodo protected
	// compare, invocato dal metodo pubblico compare che si sta testando, è uguale a
	// true. Dato che all'invocazione del metodo pubblico compare il valore di
	// "equals" è false, il confronto tra le mappe non viene effettuato e viene
	// sempre restituita un'eccezione

	@Test
	public void validateTest() {

		if (expectedValidate instanceof AvroRuntimeException) {
			expectedException.expect(AvroRuntimeException.class);
		}

		if (expectedValidate instanceof NullPointerException) {
			expectedException.expect(NullPointerException.class);
		}

		if (expectedValidate instanceof UnresolvedUnionException) {
			expectedException.expect(UnresolvedUnionException.class);
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
