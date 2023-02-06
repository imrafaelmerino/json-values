package com.dslplatform.json;

import java.io.IOException;

 abstract class StringConverter {

	public static final JsonReader.ReadObject<String> READER = new JsonReader.ReadObject<>() {
		@Nullable
		@Override
		public String read(JsonReader reader) throws IOException {
			if (reader.wasNull()) return null;
			return reader.readString();
		}
	};
	public static final JsonWriter.WriteObject<String> WRITER = (writer, value) -> serializeNullable(value, writer);



	public static void serializeNullable(@Nullable final String value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeString(value);
		}
	}


	public static  String deserialize(final JsonReader reader) throws IOException {
		return reader.readString();
	}



}
