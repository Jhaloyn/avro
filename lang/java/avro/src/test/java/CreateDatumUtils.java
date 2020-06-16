import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.EnumSymbol;
import org.apache.avro.generic.GenericFixed;
import org.apache.avro.generic.GenericRecord;

public class CreateDatumUtils {

	public static <T> ArrayList<T> createArrayDatum(T arg1, T arg2, T arg3) {

		ArrayList<T> list = new ArrayList<T>();
		list.add(arg1);
		list.add(arg2);
		list.add(arg3);

		return list;
	}

	public static <T> ArrayList<T> createArrayDatum(T arg1, T arg2) {

		ArrayList<T> list = new ArrayList<T>();
		list.add(arg1);
		list.add(arg2);

		return list;
	}

	public static <T> Map<T, T> createMapDatum(T key1, T key2, T value1, T value2, T value3) {

		Map<T, T> map = new HashMap<T, T>();

		map.put(key1, value1);
		map.put(key1, value2);
		map.put(key2, value3);

		return map;
	}

	public static <T> GenericRecord createRecordDatum(T field1, T field2) {

		GenericRecord genRec = new GenericData.Record(SchemaUtils.generateRecordDatumSchema());

		// Inserire i dati in accordo con lo schema
		genRec.put("first", field1);
		genRec.put("last", field2);

		return genRec;
	}

	public static <T> GenericRecord createRecordDatumMutation(T field1) {

		GenericRecord genRec = new GenericData.Record(SchemaUtils.generateRecordDatumSchemaForMutation());

		// Inserire i dati in accordo con lo schema
		GenericFixed genericFixed = new GenericData.Fixed(SchemaUtils.generateFixedSchema(), new byte[0]);
		genRec.put("bytes", genericFixed);
		genRec.put("first", field1);

		return genRec;
	}

	public static <T> GenericRecord createUnionDatum(T field1, T field2) {

		GenericRecord genRec = new GenericData.Record(SchemaUtils.generateUnionSchema());

		// Inserire i dati in accordo con lo schema
		genRec.put("experience", field1);
		genRec.put("age", field2);

		return genRec;
	}

	public static EnumSymbol createEnumSymbolDatum(String enumSymbol) {
		return new GenericData.EnumSymbol(SchemaUtils.generateEnumSchema(), enumSymbol);
	}

	public static GenericFixed createFixedDatum(int size) {
		return new GenericData.Fixed(SchemaUtils.generateFixedSchema(), new byte[size]);

	}

}
