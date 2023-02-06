package com.dslplatform.json;

import java.io.IOException;
import java.util.Arrays;

 abstract class BoolConverter {

	 final static boolean[] EMPTY_ARRAY = new boolean[0];

	 static final JsonReader.ReadObject<Boolean> READER = BoolConverter::deserialize;
	 static final JsonReader.ReadObject<Boolean> NULLABLE_READER = new JsonReader.ReadObject<>() {
		 @Nullable
		 @Override
		 public Boolean read(JsonReader reader) throws IOException {
			 return reader.wasNull() ? null : deserialize(reader);
		 }
	 };
	 static final JsonWriter.WriteObject<Boolean> WRITER = (writer, value) -> serializeNullable(value, writer);
	 static final JsonReader.ReadObject<boolean[]> ARRAY_READER = new JsonReader.ReadObject<>() {
		 @Nullable
		 @Override
		 public boolean[] read(JsonReader reader) throws IOException {
			 if (reader.wasNull()) return null;
			 if (reader.last() != '[') throw reader.newParseError("Expecting '[' for boolean array start");
			 reader.getNextToken();
			 return deserializeBoolArray(reader);
		 }
	 };
	 static final JsonWriter.WriteObject<boolean[]> ARRAY_WRITER = (writer, value) -> serialize(value, writer);

	 static void serializeNullable(@Nullable final Boolean value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else if (value) {
			sw.writeAscii("true");
		} else {
			sw.writeAscii("false");
		}
	}

	 static void serialize(final boolean value, final JsonWriter sw) {
		if (value) {
			sw.writeAscii("true");
		} else {
			sw.writeAscii("false");
		}
	}

	 static void serialize(@Nullable final boolean[] value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else if (value.length == 0) {
			sw.writeAscii("[]");
		} else {
			sw.writeByte(JsonWriter.ARRAY_START);
			sw.writeAscii(value[0] ? "true" : "false");
			for(int i = 1; i < value.length; i++) {
				sw.writeAscii(value[i] ? ",true" : ",false");
			}
			sw.writeByte(JsonWriter.ARRAY_END);
		}
	}

	 static <TContext> boolean deserialize(final JsonReader<TContext> reader) throws IOException {
		if (reader.wasTrue()) {
			return true;
		} else if (reader.wasFalse()) {
			return false;
		}
		throw reader.newParseErrorAt("Found invalid boolean value", 0);
	}

	 static <TContext> boolean[] deserializeBoolArray(final JsonReader<TContext> reader) throws IOException {
		if (reader.last() == ']') {
			return EMPTY_ARRAY;
		}
		boolean[] buffer = new boolean[4];
		buffer[0] = deserialize(reader);
		int i = 1;
		while (reader.getNextToken() == ',') {
			reader.getNextToken();
			if (i == buffer.length) {
				buffer = Arrays.copyOf(buffer, buffer.length << 1);
			}
			buffer[i++] = deserialize(reader);
		}
		reader.checkArrayEnd();
		return Arrays.copyOf(buffer, i);
	}


}
