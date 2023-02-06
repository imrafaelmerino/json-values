package com.dslplatform.json;

import java.io.IOException;


 abstract class BinaryConverter {

	static final JsonReader.ReadObject<byte[]> Base64Reader = new JsonReader.ReadObject<byte[]>() {
		@Nullable
		@Override
		public byte[] read(JsonReader reader) throws IOException {
			return reader.wasNull() ? null : deserialize(reader);
		}
	};
	static final JsonWriter.WriteObject<byte[]> Base64Writer = new JsonWriter.WriteObject<byte[]>() {
		@Override
		public void write(JsonWriter writer, @Nullable byte[] value) {
			serialize(value, writer);
		}
	};

	public static void serialize(@Nullable final byte[] value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else if (value.length == 0) {
			sw.writeAscii("\"\"");
		} else {
			sw.writeBinary(value);
		}
	}

	public static byte[] deserialize(final JsonReader reader) throws IOException {
		return reader.readBase64();
	}

}
