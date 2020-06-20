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

				// -------------------Category Partition-----------------------
				{ generateSimpleSchemasWithSameType(Type.BOOLEAN), true },
				{ generateSchemasWithDifferentType(Type.BYTES, Type.STRING), true }, // semplici e tipo diverso
				{ generateSimpleSchemasWithSameType(Type.DOUBLE), true },
				{ generateSimpleSchemasWithSameType(Type.FLOAT), true },
				{ generateSimpleSchemasWithSameType(Type.INT), true },
				{ generateSimpleSchemasWithSameType(Type.LONG), true },
				{ generateSimpleSchemasWithSameType(Type.NULL), true },
				{ generateSimpleSchemasWithSameType(Type.STRING), true },
				{ generateComplexSchemasWithSameType(Type.RECORD, true), true }, // complessi stessi tipo e stringa
				{ generateComplexSchemasWithSameType(Type.ARRAY, false), false }, // complex stesso tipo diversa stringa
				{ generateSchemasWithDifferentType(Type.ENUM, Type.ARRAY), false }, // complex tipo diverso
				{ generateComplexSchemasWithSameType(Type.FIXED, true), true },
				{ generateComplexSchemasWithSameType(Type.MAP, true), true },
				{ generateComplexSchemasWithSameType(Type.UNION, false), false },
//				{ generateSchemasWithDifferentType(null, null), new AssertionError() }, // entrambi gli schemi sono null
				{ generateSchemasWithDifferentType(null, null), new NullPointerException() },

				// -------------------Coverage Strutturale-----------------------

				{ generateSimpleSchemasWithSameType(Type.BYTES), true }, // (linea 276)
				{ generateSchemasWithDifferentType(Type.BOOLEAN, Type.STRING), false },
				{ generateSchemasWithDifferentType(Type.LONG, Type.INT), true }, // (linea 341)
				{ generateSchemasWithDifferentType(Type.LONG, Type.FLOAT), false },
				{ generateSchemasWithDifferentType(Type.FLOAT, Type.INT), true }, // (linea 344)
				{ generateSchemasWithDifferentType(Type.FLOAT, Type.DOUBLE), false },
				{ generateSchemasWithDifferentType(Type.FLOAT, Type.LONG), true },
				{ generateSchemasWithDifferentType(Type.DOUBLE, Type.LONG), true },
				{ generateSchemasWithDifferentType(Type.DOUBLE, Type.FLOAT), true },
				{ generateSchemasWithDifferentType(Type.DOUBLE, Type.INT), true }, // (linea 349)
				{ generateSchemasWithDifferentType(Type.DOUBLE, Type.STRING), false },
				{ generateSchemasWithDifferentType(Type.BYTES, Type.INT), false },
				{ generateSchemasWithDifferentType(Type.STRING, Type.BYTES), true }, // (linea 357)
				{ generateSchemasWithDifferentType(Type.RECORD, Type.BOOLEAN), false }, // (linea 369)
				{ generateComplexSchemasWithSameType(Type.RECORD, false), false }, // introdotto alias record (linea
				// 124)
				{ generateSchemasWithDifferentType(Type.MAP, Type.BOOLEAN), false }, // (linea 363)
				{ generateSchemasWithDifferentType(Type.ARRAY, Type.INT), false },
				{ generateComplexSchemasWithSameType(Type.ENUM, false), false }, // enum stesso tipo (linea 292)
				{ generateSchemasWithDifferentType(Type.FIXED, Type.BOOLEAN), false }, // (linea 365)
				{ generateSchemasWithDifferentType(Type.NULL, Type.UNION), false }, // (linea 327 e 335)

				// ---------------------------Mutation testing-----------------------
				{ SchemaUtils.generateMutationRecordSchemas(1), true }, // mutazione linea 102
				{ SchemaUtils.generateMutationRecordSchemas(2), false },// mutazioni linee 102 e 901

		};

		return Arrays.asList(data);
	}

	/*
	 * Schemi di tipo String e Bytes sono compatibili. Schemi di tipo Long, Float e
	 * Double sono compatibili con schemi Int. Schemi di tipo Float e Double sono
	 * compatibili con schemi Long. Double è compatibile con Float
	 * 
	 */

	// Junit vuole AssertionError, Maven vuole NullPointerException

	@Test
	public void checkReaderWriterCompatibilityTest() {

//		if (expected instanceof AssertionError) {
//			 expectedException.expect(AssertionError.class);
//		}

		if (expected instanceof NullPointerException) {
			expectedException.expect(NullPointerException.class);
		}

		List<SchemaCompatibility.Incompatibility> result = SchemaCompatibility
				.checkReaderWriterCompatibility(schemasList.get(0), schemasList.get(1)).getResult()
				.getIncompatibilities();

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

		case RECORD:
			return SchemaUtils.generateRecordSchemas(isEquals);
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

		}
		schemasList.add(generateSchema(typeSchema1));
		schemasList.add(generateSchema(typeSchema2));

		return schemasList;
	}

	private static Schema generateSchema(Type typeSchema) {

		switch (typeSchema) {

		case STRING:
			return Schema.create(Type.STRING);
		case BYTES:
			return Schema.create(Type.BYTES);
		case INT:
			return Schema.create(Type.INT);
		case LONG:
			return Schema.create(Type.LONG);
		case FLOAT:
			return Schema.create(Type.FLOAT);
		case DOUBLE:
			return Schema.create(Type.DOUBLE);
		case BOOLEAN:
			return Schema.create(Type.BOOLEAN);
		case RECORD:
			return SchemaUtils.generateRecordSchema();
		case MAP:
			return SchemaUtils.generateMapSchema();
		case FIXED:
			return SchemaUtils.generateFixedSchema();
		case ARRAY:
			return SchemaUtils.generateArraySchema();
		case ENUM:
			return SchemaUtils.generateEnumSchema();
		case UNION:
			return SchemaUtils.generateUnionSchema();
		case NULL:
			return Schema.create(Type.NULL);

		default:
			return null;
		}
	}
}
