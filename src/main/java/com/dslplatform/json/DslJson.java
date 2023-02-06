package com.dslplatform.json;


import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;

/**
 * Main DSL-JSON class.
 * Easiest way to use the library is to create an DslJson&lt;Object&gt; instance and reuse it within application.
 * DslJson has optional constructor for specifying default readers/writers.
 * <p>
 * During initialization DslJson will use ServiceLoader API to load registered services.
 * This is done through `META-INF/services/com.dslplatform.json.CompiledJson` file.
 * <p>
 * DslJson can fallback to another serializer in case when it doesn't know how to handle specific type.
 * This can be specified by Fallback interface during initialization.
 * <p>
 * If you wish to use compile time databinding @CompiledJson annotation must be specified on the target class
 * or implicit reference to target class must exists from a class with @CompiledJson annotation.
 * <p>
 * Usage example:
 * <pre>
 *     DslJson&lt;Object&gt; dsl = new DslJson&lt;&gt;();
 *     dsl.serialize(instance, OutputStream);
 *     POJO pojo = dsl.deserialize(POJO.class, InputStream);
 * </pre>
 * <p>
 * For best performance use serialization API with JsonWriter and byte[] as target.
 * JsonWriter is reused via thread local variable. When custom JsonWriter's are used, reusing them will yield maximum performance.
 * JsonWriter can be reused via reset methods.
 * For best deserialization performance prefer byte[] API instead of InputStream API.
 * JsonReader is reused via thread local variable. When custom JsonReaders are used, reusing them will yield maximum performance.
 * JsonReader can be reused via process methods.
 * <p>
 * During deserialization TContext can be used to pass data into deserialized classes.
 * This is useful when deserializing domain objects which require state or service provider.
 * For example DSL Platform entities require service locator to be able to perform lazy load.
 * <p>
 * DslJson doesn't have a String or Reader API since it's optimized for processing bytes and streams.
 * If you wish to process String, use String.getBytes("UTF-8") as argument for DslJson.
 * Only UTF-8 is supported for encoding and decoding JSON.
 * <pre>
 *     DslJson&lt;Object&gt; dsl = new DslJson&lt;&gt;();
 *     JsonWriter writer = dsl.newWriter();
 *     dsl.serialize(writer, instance);
 *     String json = writer.toString(); //JSON as string - avoid using JSON as Strings whenever possible
 *     byte[] input = json.getBytes("UTF-8");
 *     POJO pojo = dsl.deserialize(POJO.class, input, input.length);
 * </pre>
 *
 * @param <TContext> used for library specialization. If unsure, use Object
 */
 class DslJson<TContext> implements UnknownSerializer, TypeLookup {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * The context of this instance.
	 * Can be used for library specialization
	 */
	@Nullable
	public final TContext context;

	/**
	 * Should properties with default values be omitted from the resulting JSON?
	 * This will leave out nulls, empty collections, zeros and other attributes with default values
	 * which can be reconstructed from schema information
	 */
	public final boolean omitDefaults;
	/**
	 * When object supports array format, eg. [prop1, prop2, prop3] this value must be enabled before
	 * object will be serialized in such a way. Regardless of this value deserialization will support all formats.
	 */
	public final boolean allowArrayFormat;

	protected final StringCache keyCache;
	protected final StringCache valuesCache;
	private final JsonReader.ErrorInfo errorInfo;
	private final JsonReader.DoublePrecision doublePrecision;
	private final JsonReader.UnknownNumberParsing unknownNumbers;
	private final int maxNumberDigits;
	private final int maxStringSize;
	protected final ThreadLocal<JsonWriter> localWriter;
	protected final ThreadLocal<JsonReader> localReader;





	/**
	 * Configuration for DslJson options.
	 * By default key cache is enabled. Everything else is not configured.
	 * To load `META-INF/services` call `includeServiceLoader()`
	 *
	 * @param <TContext> DslJson context
	 */
	public static class Settings<TContext> {
		private TContext context;
		private boolean omitDefaults;
		private boolean allowArrayFormat;
		private StringCache keyCache = new SimpleStringCache();
		private StringCache valuesCache;
		private JsonReader.ErrorInfo errorInfo = JsonReader.ErrorInfo.WITH_STACK_TRACE;
		private JsonReader.DoublePrecision doublePrecision = JsonReader.DoublePrecision.DEFAULT;
		private JsonReader.UnknownNumberParsing unknownNumbers = JsonReader.UnknownNumberParsing.LONG_AND_BIGDECIMAL;
		private int maxNumberDigits = 512;
		private int maxStringBuffer = 128 * 1024 * 1024;
		private final List<Configuration> configurations = new ArrayList<Configuration>();
		private final Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();
		private final Map<Class<? extends Annotation>, Boolean> creatorMarkers = new HashMap<Class<? extends Annotation>, Boolean>();

		/**
		 * Pass in context for DslJson.
		 * Context will be available in JsonReader for objects which needs it.
		 *
		 * @param context context propagated to JsonReaders
		 * @return itself
		 */
		public Settings<TContext> withContext(@Nullable TContext context) {
			this.context = context;
			return this;
		}

		/**
		 * DslJson can exclude some properties from resulting JSON which it can reconstruct fully from schema information.
		 * Eg. int with value 0 can be omitted since that is default value for the type.
		 * Null values can be excluded since they are handled the same way as missing property.
		 *
		 * @param omitDefaults should exclude default values from resulting JSON
		 * @return itself
		 */
		public Settings<TContext> skipDefaultValues(boolean omitDefaults) {
			this.omitDefaults = omitDefaults;
			return this;
		}

		/**
		 * Some encoders/decoders support writing objects in array format.
		 * For encoder to write objects in such format, Array format must be defined before the Default and minified formats
		 * and array format must be allowed via this setting.
		 * If objects support multiple formats decoding will work regardless of this setting.
		 *
		 * @param allowArrayFormat allow serialization via array format
		 * @return itself
		 */
		public Settings<TContext> allowArrayFormat(boolean allowArrayFormat) {
			this.allowArrayFormat = allowArrayFormat;
			return this;
		}

		/**
		 * Use specific key cache implementation.
		 * Key cache is enabled by default and it's used when deserializing unstructured objects such as Map&lt;String, Object&gt;
		 * to avoid allocating new String key instance. Instead StringCache will provide a new or an old instance.
		 * This improves memory usage and performance since there is usually small number of keys.
		 * It does have some performance overhead, but this is dependant on the implementation.
		 * <p>
		 * To disable key cache, provide null for it.
		 *
		 * @param keyCache which key cache to use
		 * @return itself
		 */
		public Settings<TContext> useKeyCache(@Nullable StringCache keyCache) {
			this.keyCache = keyCache;
			return this;
		}

		/**
		 * Use specific string values cache implementation.
		 * By default string values cache is disabled.
		 * <p>
		 * To support memory restricted scenarios where there is limited number of string values,
		 * values cache can be used.
		 * <p>
		 * Not every "JSON string" will use this cache... eg UUID, LocalDate don't create an instance of string
		 * and therefore don't use this cache.
		 *
		 * @param valuesCache which values cache to use
		 * @return itself
		 */
		public Settings<TContext> useStringValuesCache(@Nullable StringCache valuesCache) {
			this.valuesCache = valuesCache;
			return this;
		}




		/**
		 * By default doubles are not deserialized into an exact value in some rare edge cases.
		 *
		 * @param errorInfo information about error in parsing exception
		 * @return itself
		 */
		public Settings<TContext> errorInfo(JsonReader.ErrorInfo errorInfo) {
			if (errorInfo == null) throw new IllegalArgumentException("errorInfo can't be null");
			this.errorInfo = errorInfo;
			return this;
		}

		/**
		 * By default doubles are not deserialized into an exact value in some rare edge cases.
		 *
		 * @param precision type of double deserialization
		 * @return itself
		 */
		public Settings<TContext> doublePrecision(JsonReader.DoublePrecision precision) {
			if (precision == null) throw new IllegalArgumentException("precision can't be null");
			this.doublePrecision = precision;
			return this;
		}

		/**
		 * When processing JSON without a schema numbers can be deserialized in various ways:
		 *
		 *  - as longs and decimals
		 *  - as longs and doubles
		 *  - as decimals only
		 *  - as doubles only
		 *
		 *  Default is as long and BigDecimal
		 *
		 * @param unknownNumbers how to deserialize numbers without a schema
		 * @return itself
		 */
		public Settings<TContext> unknownNumbers(JsonReader.UnknownNumberParsing unknownNumbers) {
			if (unknownNumbers == null) throw new IllegalArgumentException("unknownNumbers can't be null");
			this.unknownNumbers = unknownNumbers;
			return this;
		}

		/**
		 * Specify maximum allowed size for digits buffer. Default is 512.
		 * Digits buffer is used when processing strange/large input numbers.
		 *
		 * @param size maximum allowed size for digit buffer
		 * @return itself
		 */
		public Settings<TContext> limitDigitsBuffer(int size) {
			if (size < 1) throw new IllegalArgumentException("size can't be smaller than 1");
			this.maxNumberDigits = size;
			return this;
		}

		/**
		 * Specify maximum allowed size for string buffer. Default is 128MB
		 * To protect against malicious inputs, maximum allowed string buffer can be reduced.
		 *
		 * @param size maximum size of buffer in bytes
		 * @return itself
		 */
		public Settings<TContext> limitStringBuffer(int size) {
			if (size < 1) throw new IllegalArgumentException("size can't be smaller than 1");
			this.maxStringBuffer = size;
			return this;
		}


		/**
		 * Configure DslJson with custom Configuration during startup.
		 * Configurations are extension points for setting up readers/writers during DslJson initialization.
		 *
		 * @param conf custom extensibility point
		 * @return itself
		 */
		public Settings<TContext> with(Configuration conf) {
			configurations.add(conf);
			return this;
		}

		private Settings<TContext> with(Iterable<Configuration> confs) {
			for (Configuration c : confs)
				configurations.add(c);
			return this;
		}
	}

	/**
	 * Simple initialization entry point.
	 * Will provide null for TContext
	 * Java graphics readers/writers will not be registered.
	 * Fallback will not be configured.
	 * Key cache will be enables, values cache will be disabled.
	 * Default ServiceLoader.load method will be used to setup services from META-INF
	 */
	public DslJson() {
		this(new Settings<TContext>());
	}

	/**
	 * Will be removed. Use DslJson(Settings) instead.
	 * Fully configurable entry point.
	 *
	 * @param context       context instance which can be provided to deserialized objects. Use null if not sure
	 * @param omitDefaults  should serialization produce minified JSON (omit nulls and default values)
	 * @param keyCache      parsed keys can be cached (this is only used in small subset of parsing)
	 * @param serializers   additional serializers/deserializers which will be immediately registered into readers/writers
	 */
	@Deprecated
	public DslJson(
			@Nullable final TContext context,
			final boolean omitDefaults,
			@Nullable final StringCache keyCache,
			final Iterable<Configuration> serializers) {
		this(new Settings<TContext>()
				.withContext(context)
				.skipDefaultValues(omitDefaults)
				.useKeyCache(keyCache)
				.with(serializers)
		);
	}

	/**
	 * Fully configurable entry point.
	 * Provide settings for DSL-JSON initialization.
	 *
	 * @param settings DSL-JSON configuration
	 */
	public DslJson(final Settings<TContext> settings) {
		final DslJson<TContext> self = this;
		this.localWriter = ThreadLocal.withInitial(() -> newWriter(512));
		this.localReader = new ThreadLocal<>() {
			@Override
			protected JsonReader initialValue() {
				return new JsonReader<>(new byte[4096], 4096, self.context, new char[64], self.keyCache, self.valuesCache, self, self.errorInfo, self.doublePrecision, self.unknownNumbers, self.maxNumberDigits, self.maxStringSize);
			}
		};
		this.context = settings.context;
		this.omitDefaults = settings.omitDefaults;
		this.allowArrayFormat = settings.allowArrayFormat;
		this.keyCache = settings.keyCache;
		this.valuesCache = settings.valuesCache;
		this.unknownNumbers = settings.unknownNumbers;
		this.errorInfo = settings.errorInfo;
		this.doublePrecision = settings.doublePrecision;
		this.maxNumberDigits = settings.maxNumberDigits;
		this.maxStringSize = settings.maxStringBuffer;

		registerReader(byte[].class, BinaryConverter.Base64Reader);
		registerWriter(byte[].class, BinaryConverter.Base64Writer);
		registerReader(boolean.class, BoolConverter.READER);
		registerWriter(boolean.class, BoolConverter.WRITER);
		registerDefault(boolean.class, false);
		registerReader(boolean[].class, BoolConverter.ARRAY_READER);
		registerWriter(boolean[].class, BoolConverter.ARRAY_WRITER);
		registerReader(Boolean.class, BoolConverter.NULLABLE_READER);
		registerWriter(Boolean.class, BoolConverter.WRITER);

		registerDefault(double.class, 0.0);

		registerDefault(float.class, 0.0f);

		registerDefault(int.class, 0);

		registerDefault(short.class, (short)0);

		registerDefault(long.class, 0L);

		registerReader(String.class, StringConverter.READER);
		registerWriter(String.class, StringConverter.WRITER);


		for (Configuration serializer : settings.configurations) {
			serializer.configure(this);
		}

	}

	/**
	 * Simplistic string cache implementation.
	 * It uses a fixed String[] structure in which it caches string value based on it's hash.
	 * Eg, hash &amp; mask provide index into the structure. Different string with same hash will overwrite the previous one.
	 */
	public static class SimpleStringCache implements StringCache {

		private final int mask;
		private final String[] cache;

		/**
		 * Will use String[] with 1024 elements.
		 */
		public SimpleStringCache() {
			this(10);
		}

		public SimpleStringCache(int log2Size) {
			int size = 2;
			for (int i = 1; i < log2Size; i++) {
				size *= 2;
			}
			mask = size - 1;
			cache = new String[size];
		}

		/**
		 * Calculates hash of the provided "string" and looks it up from the String[]
		 * It it doesn't exists of a different string is already there a new String instance is created
		 * and saved into the String[]
		 *
		 * @param chars buffer into which string was parsed
		 * @param len the string length inside the buffer
		 * @return String instance matching the char[]/int pair
		 */
		@Override
		public String get(char[] chars, int len) {
			long hash = 0x811c9dc5;
			for (int i = 0; i < len; i++) {
				hash ^= (byte) chars[i];
				hash *= 0x1000193;
			}
			final int index = (int) hash & mask;
			final String value = cache[index];
			if (value == null) return createAndPut(index, chars, len);
			if (value.length() != len) return createAndPut(index, chars, len);
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) != chars[i]) return createAndPut(index, chars, len);
			}
			return value;
		}

		private String createAndPut(int index, char[] chars, int len) {
			final String value = new String(chars, 0, len);
			cache[index] = value;
			return value;
		}
	}

	/**
	 * Create a writer bound to this DSL-JSON.
	 * Ideally it should be reused.
	 * Bound writer can use lookups to find custom writers.
	 * This can be used to serialize unknown types such as Object.class
	 *
	 * @return bound writer
	 */
	public JsonWriter newWriter() {
		return new JsonWriter();
	}

	/**
	 * Create a writer bound to this DSL-JSON.
	 * Ideally it should be reused.
	 * Bound writer can use lookups to find custom writers.
	 * This can be used to serialize unknown types such as Object.class
	 *
	 * @param size initial buffer size
	 * @return bound writer
	 */
	public JsonWriter newWriter(int size) {
		return new JsonWriter(size);
	}

	/**
	 * Create a writer bound to this DSL-JSON.
	 * Ideally it should be reused.
	 * Bound writer can use lookups to find custom writers.
	 * This can be used to serialize unknown types such as Object.class
	 *
	 * @param buffer initial buffer
	 * @return bound writer
	 */
	public JsonWriter newWriter(byte[] buffer) {
		if (buffer == null) throw new IllegalArgumentException("null value provided for buffer");
		return new JsonWriter(buffer);
	}

	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * This reader can be reused via process method.
	 *
	 * @return bound reader
	 */
	public JsonReader<TContext> newReader() {
		return new JsonReader<TContext>(new byte[4096], 4096, context, new char[64], keyCache, valuesCache, this, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringSize);
	}

	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * This reader can be reused via process method.
	 *
	 * @param bytes input bytes
	 * @return bound reader
	 */
	public JsonReader<TContext> newReader(byte[] bytes) {
		return new JsonReader<TContext>(bytes, bytes.length, context, new char[64], keyCache, valuesCache, this, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringSize);
	}

	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * This reader can be reused via process method.
	 *
	 * @param bytes  input bytes
	 * @param length use input bytes up to specified length
	 * @return bound reader
	 */
	public JsonReader<TContext> newReader(byte[] bytes, int length) {
		return new JsonReader<TContext>(bytes, length, context, new char[64], keyCache, valuesCache, this, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringSize);
	}


	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * Pass in initial string buffer.
	 * This reader can be reused via process method.
	 *
	 * @param bytes  input bytes
	 * @param length use input bytes up to specified length
	 * @param tmp string parsing buffer
	 * @return bound reader
	 */
	public JsonReader<TContext> newReader(byte[] bytes, int length, char[] tmp) {
		return new JsonReader<TContext>(bytes, length, context, tmp, keyCache, valuesCache, this, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringSize);
	}

	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * Created reader can be reused (using process method).
	 * This is convenience method for creating a new reader and binding it to stream.
	 *
	 * @param stream input stream
	 * @param buffer temporary buffer
	 * @return bound reader
	 * @throws IOException unable to read from stream
	 */
	public JsonReader<TContext> newReader(InputStream stream, byte[] buffer) throws IOException {
		final JsonReader<TContext> reader = newReader(buffer);
		reader.process(stream);
		return reader;
	}

	/**
	 * Create a reader bound to this DSL-JSON.
	 * Bound reader can reuse key cache (which is used during Map deserialization)
	 * This method id Deprecated since it should be avoided.
	 * It's better to use byte[] or InputStream based readers
	 *
	 * @param input JSON string
	 * @return bound reader
	 */
	@Deprecated
	public JsonReader<TContext> newReader(String input) {
		final byte[] bytes = input.getBytes(UTF8);
		return new JsonReader<TContext>(bytes, bytes.length, context, new char[64], keyCache, valuesCache, this, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringSize);
	}





	private final Map<Type, Object> defaults = new ConcurrentHashMap<>();

	public <T> void registerDefault(Class<T> manifest, T instance) {
		defaults.put(manifest, instance);
	}




	private final ConcurrentMap<Type, JsonReader.ReadObject> readers = new ConcurrentHashMap<Type, JsonReader.ReadObject>();
	private final ConcurrentMap<Type, JsonWriter.WriteObject> writers = new ConcurrentHashMap<Type, JsonWriter.WriteObject>();


	/**
	 * Register custom reader for specific type (JSON -&gt; instance conversion).
	 * Reader is used for conversion from input byte[] -&gt; target object instance
	 * <p>
	 * Types registered through @CompiledJson annotation should be registered automatically through
	 * ServiceLoader.load method and you should not be registering them manually.
	 * <p>
	 * If null is registered for a reader this will disable deserialization of specified type
	 *
	 * @param manifest specified type
	 * @param reader   provide custom implementation for reading JSON into an object instance
	 * @param <T>      type
	 * @param <S>      type or subtype
	 */
	public <T, S extends T> void registerReader(final Class<T> manifest, @Nullable final JsonReader.ReadObject<S> reader) {
		if (reader == null) readers.remove(manifest);
		else readers.put(manifest, reader);
	}


	/**
	 * Register custom writer for specific type (instance -&gt; JSON conversion).
	 * Writer is used for conversion from object instance -&gt; output byte[]
	 * <p>
	 * Types registered through @CompiledJson annotation should be registered automatically through
	 * ServiceLoader.load method and you should not be registering them manually.
	 * <p>
	 * If null is registered for a writer this will disable serialization of specified type
	 *
	 * @param manifest specified type
	 * @param writer   provide custom implementation for writing JSON from object instance
	 * @param <T>      type
	 */
	public <T> void registerWriter(final Class<T> manifest, @Nullable final JsonWriter.WriteObject<T> writer) {
		if (writer == null) {
			writerMap.remove(manifest);
			writers.remove(manifest);
		} else {
			writerMap.put(manifest, manifest);
			writers.put(manifest, writer);
		}
	}



	private final ConcurrentMap<Class<?>, Class<?>> writerMap = new ConcurrentHashMap<>();


}
