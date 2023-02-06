package com.dslplatform.json;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Object for processing JSON from byte[] and InputStream.
 * DSL-JSON works on byte level (instead of char level).
 * Deserialized instances can obtain TContext information provided with this reader.
 * <p>
 * JsonReader can be reused by calling process methods.
 *
 * @param <TContext> context passed to deserialized object instances
 */
class JsonReader<TContext> {

    private static final boolean[] WHITESPACE = new boolean[256];
    private static final Charset utf8 = StandardCharsets.UTF_8;

    static {
        WHITESPACE[9 + 128] = true;
        WHITESPACE[10 + 128] = true;
        WHITESPACE[11 + 128] = true;
        WHITESPACE[12 + 128] = true;
        WHITESPACE[13 + 128] = true;
        WHITESPACE[32 + 128] = true;
        WHITESPACE[-96 + 128] = true;
        WHITESPACE[-31 + 128] = true;
        WHITESPACE[-30 + 128] = true;
        WHITESPACE[-29 + 128] = true;
    }

    private int tokenStart;
    private int nameEnd;
    private int currentIndex = 0;
    private long currentPosition = 0;
    private byte last = ' ';

    private int length;
    private final char[] tmp;

    public TContext context;
    byte[] buffer;
    char[] chars;

    private InputStream stream;
    private int readLimit;
    //always leave some room for reading special stuff, so that buffer contains enough padding for such optimizations
    private int bufferLenWithExtraSpace;

    private final StringCache keyCache;
    private final StringCache valuesCache;
    private final TypeLookup typeLookup;

    private final byte[] originalBuffer;
    private final int originalBufferLenWithExtraSpace;

    public enum ErrorInfo {
        WITH_STACK_TRACE,
        DESCRIPTION_AND_POSITION,
        DESCRIPTION_ONLY,
        MINIMAL
    }

    public enum DoublePrecision {
        EXACT(0),
        HIGH(1),
        DEFAULT(3),
        LOW(4);

        final int level;

        DoublePrecision(int level) {
            this.level = level;
        }
    }

    public enum UnknownNumberParsing {
        LONG_AND_BIGDECIMAL,
        LONG_AND_DOUBLE,
        BIGDECIMAL,
        DOUBLE
    }

    private final ErrorInfo errorInfo;
    DoublePrecision doublePrecision;
    int doubleLengthLimit;
    UnknownNumberParsing unknownNumbers;
    int maxNumberDigits;
    private final int maxStringBuffer;

    private JsonReader(
            char[] tmp,
            byte[] buffer,
            int length,
            @Nullable TContext context,
            @Nullable StringCache keyCache,
            @Nullable StringCache valuesCache,
            @Nullable TypeLookup typeLookup,
            ErrorInfo errorInfo,
            DoublePrecision doublePrecision,
            UnknownNumberParsing unknownNumbers,
            int maxNumberDigits,
            int maxStringBuffer
                      ) {
        this.tmp = tmp;
        this.buffer = buffer;
        this.length = length;
        this.bufferLenWithExtraSpace = buffer.length - 38; //currently maximum padding is for uuid
        this.context = context;
        this.chars = tmp;
        this.keyCache = keyCache;
        this.valuesCache = valuesCache;
        this.typeLookup = typeLookup;
        this.errorInfo = errorInfo;
        this.doublePrecision = doublePrecision;
        this.unknownNumbers = unknownNumbers;
        this.maxNumberDigits = maxNumberDigits;
        this.maxStringBuffer = maxStringBuffer;
        this.doubleLengthLimit = 15 + doublePrecision.level;
        this.originalBuffer = buffer;
        this.originalBufferLenWithExtraSpace = bufferLenWithExtraSpace;
    }

    /**
     * Prefer creating reader through DslJson#newReader since it will pass several arguments (such as key/string value cache)
     * First byte will not be read.
     * It will allocate new char[64] for string buffer.
     * Key and string vales cache will be null.
     *
     * @param buffer  input JSON
     * @param context context
     */
    @Deprecated
    public JsonReader(byte[] buffer, @Nullable TContext context) {
        this(buffer, context, null, null);
    }

    @Deprecated
    public JsonReader(byte[] buffer, @Nullable TContext context, @Nullable StringCache keyCache, @Nullable StringCache valuesCache) {
        this(buffer, buffer.length, context, new char[64], keyCache, valuesCache);
    }

    @Deprecated
    public JsonReader(byte[] buffer, TContext context, char[] tmp) {
        this(buffer, buffer.length, context, tmp);
    }

    @Deprecated
    public JsonReader(byte[] buffer, int length, TContext context) {
        this(buffer, length, context, new char[64]);
    }

    @Deprecated
    public JsonReader(byte[] buffer, int length, TContext context, char[] tmp) {
        this(buffer, length, context, tmp, null, null);
    }

    @Deprecated
    public JsonReader(byte[] buffer, int length, @Nullable TContext context, char[] tmp, @Nullable StringCache keyCache, @Nullable StringCache valuesCache) {
        this(tmp, buffer, length, context, keyCache, valuesCache, null, ErrorInfo.WITH_STACK_TRACE, DoublePrecision.DEFAULT, UnknownNumberParsing.LONG_AND_BIGDECIMAL, 512, 256 * 1024 * 1024);
        if (length > buffer.length) {
            throw new IllegalArgumentException("length can't be longer than buffer.length");
        } else if (length < buffer.length) {
            buffer[length] = '\0';
        }
    }

    JsonReader(
            byte[] buffer,
            int length,
            @Nullable TContext context,
            char[] tmp,
            @Nullable StringCache keyCache,
            @Nullable StringCache valuesCache,
            @Nullable TypeLookup typeLookup,
            ErrorInfo errorInfo,
            DoublePrecision doublePrecision,
            UnknownNumberParsing unknownNumbers,
            int maxNumberDigits,
            int maxStringBuffer
              ) {
        this(tmp, buffer, length, context, keyCache, valuesCache, typeLookup, errorInfo, doublePrecision, unknownNumbers, maxNumberDigits, maxStringBuffer);
        if (length > buffer.length) {
            throw new IllegalArgumentException("length can't be longer than buffer.length");
        } else if (length < buffer.length) {
            buffer[length] = '\0';
        }
    }


    /**
     * Will be removed. Exists only for backward compatibility
     *
     * @param stream process stream
     * @throws IOException error reading from stream
     */
    @Deprecated
    public void reset(InputStream stream) throws IOException {
        process(stream);
    }

    /**
     * Will be removed. Exists only for backward compatibility
     *
     * @param size size of byte[] input to use
     */
    @Deprecated
    void reset(int size) {
        process(null, size);
    }

    /**
     * Reset reader after processing input
     * It will release reference to provided byte[] or InputStream input
     */
    void reset() {
        this.buffer = this.originalBuffer;
        this.bufferLenWithExtraSpace = this.originalBufferLenWithExtraSpace;
        currentIndex = 0;
        this.length = 0;
        this.readLimit = 0;
        this.stream = null;
    }

    /**
     * Bind input stream for processing.
     * Stream will be processed in byte[] chunks.
     * If stream is null, reference to stream will be released.
     *
     * @param stream set input stream
     * @return itself
     * @throws IOException unable to read from stream
     */
    public JsonReader<TContext> process(@Nullable InputStream stream) throws IOException {
        this.currentPosition = 0;
        this.currentIndex = 0;
        this.stream = stream;
        if (stream != null) {
            this.readLimit = Math.min(this.length, bufferLenWithExtraSpace);
            int available = readFully(buffer, stream, 0);
            readLimit = Math.min(available, bufferLenWithExtraSpace);
            this.length = available;
        }
        return this;
    }

    /**
     * Bind byte[] buffer for processing.
     * If this method is used in combination with process(InputStream) this buffer will be used for processing chunks of stream.
     * If null is sent for byte[] buffer, new length for valid input will be set for existing buffer.
     *
     * @param newBuffer new buffer to use for processing
     * @param newLength length of buffer which can be used
     * @return itself
     */
    public JsonReader<TContext> process(@Nullable byte[] newBuffer, int newLength) {
        if (newBuffer != null) {
            this.buffer = newBuffer;
            this.bufferLenWithExtraSpace = buffer.length - 38; //currently maximum padding is for uuid
        }
        if (newLength > buffer.length) {
            throw new IllegalArgumentException("length can't be longer than buffer.length");
        }
        currentIndex = 0;
        this.length = newLength;
        this.stream = null;
        this.readLimit = newLength;
        return this;
    }

    /**
     * Valid length of the input buffer.
     *
     * @return size of JSON input
     */
    public int length() {
        return length;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, length, utf8);
    }

    private static int readFully(byte[] buffer, InputStream stream, int offset) throws IOException {
        int read;
        int position = offset;
        while (position < buffer.length
                && (read = stream.read(buffer, position, buffer.length - position)) != -1) {
            position += read;
        }
        return position;
    }

    private static class EmptyEOFException extends EOFException {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    private static final EOFException eof = new EmptyEOFException();

    boolean withStackTrace() {
        return errorInfo == ErrorInfo.WITH_STACK_TRACE;
    }

    /**
     * Read next byte from the JSON input.
     * If buffer has been read in full IOException will be thrown
     *
     * @return next byte
     * @throws IOException when end of JSON input
     */
    public byte read() throws IOException {
        if (stream != null && currentIndex > readLimit) {
            prepareNextBlock();
        }
        if (currentIndex >= length) {
            throw ParsingException.create("Unexpected end of JSON input", eof, withStackTrace());
        }
        return last = buffer[currentIndex++];
    }

    private int prepareNextBlock() throws IOException {
        int len = length - currentIndex;
        System.arraycopy(buffer, currentIndex, buffer, 0, len);
        int available = readFully(buffer, stream, len);
        currentPosition += currentIndex;
        if (available == len) {
            readLimit = length - currentIndex;
            length = readLimit;
            currentIndex = 0;
        } else {
            readLimit = Math.min(available, bufferLenWithExtraSpace);
            this.length = available;
            currentIndex = 0;
        }
        return available;
    }

    boolean isEndOfStream() throws IOException {
        if (stream == null) {
            return length == currentIndex;
        }
        if (length != currentIndex) {
            return false;
        }
        return prepareNextBlock() == 0;
    }

    /**
     * Which was last byte read from the JSON input.
     * JsonReader doesn't allow to go back, but it remembers previously read byte
     *
     * @return which was the last byte read
     */
    public byte last() {
        return last;
    }

    public String positionDescription() {
        return positionDescription(0);
    }

    public String positionDescription(int offset) {
        StringBuilder error = new StringBuilder(60);
        positionDescription(offset, error);
        return error.toString();
    }

    private void positionDescription(int offset, StringBuilder error) {
        error.append("at position: ").append(positionInStream(offset));
        if (currentIndex > offset) {
            try {
                int maxLen = Math.min(currentIndex - offset, 20);
                String prefix = new String(buffer, currentIndex - offset - maxLen, maxLen, utf8);
                error.append(", following: `");
                error.append(prefix);
                error.append('`');
            } catch (Exception ignore) {
            }
        }
        if (currentIndex - offset < readLimit) {
            try {
                int maxLen = Math.min(readLimit - currentIndex + offset, 20);
                String suffix = new String(buffer, currentIndex - offset, maxLen, utf8);
                error.append(", before: `");
                error.append(suffix);
                error.append('`');
            } catch (Exception ignore) {
            }
        }
    }

    private StringBuilder error = new StringBuilder(0);
    private Formatter errorFormatter = new Formatter(error);

    public ParsingException newParseError(String description) {
        return newParseError(description, 0);
    }

    public ParsingException newParseError(String description, int positionOffset) {
        if (errorInfo == ErrorInfo.MINIMAL) return ParsingException.create(description, false);
        error.setLength(0);
        error.append(description);
        error.append(". Found ");
        error.append((char) last);
        if (errorInfo == ErrorInfo.DESCRIPTION_ONLY) return ParsingException.create(error.toString(), false);
        error.append(" ");
        positionDescription(positionOffset, error);
        return ParsingException.create(error.toString(), withStackTrace());
    }

    public ParsingException newParseErrorAt(String description, int positionOffset) {
        if (errorInfo == ErrorInfo.MINIMAL || errorInfo == ErrorInfo.DESCRIPTION_ONLY) {
            return ParsingException.create(description, false);
        }
        error.setLength(0);
        error.append(description);
        error.append(" ");
        positionDescription(positionOffset, error);
        return ParsingException.create(error.toString(), withStackTrace());
    }

    public ParsingException newParseErrorAt(String description, int positionOffset, Exception cause) {
        if (errorInfo == ErrorInfo.MINIMAL) return ParsingException.create(description, cause, false);
        error.setLength(0);
        String msg = cause.getMessage();
        if (msg != null && msg.length() > 0) {
            error.append(msg);
            if (!msg.endsWith(".")) {
                error.append(".");
            }
            error.append(" ");
        }
        error.append(description);
        if (errorInfo == ErrorInfo.DESCRIPTION_ONLY) return ParsingException.create(error.toString(), cause, false);
        error.append(" ");
        positionDescription(positionOffset, error);
        return ParsingException.create(error.toString(), withStackTrace());
    }

    public ParsingException newParseErrorFormat(String shortDescription, int positionOffset, String longDescriptionFormat, Object... arguments) {
        if (errorInfo == ErrorInfo.MINIMAL) return ParsingException.create(shortDescription, false);
        error.setLength(0);
        errorFormatter.format(longDescriptionFormat, arguments);
        if (errorInfo == ErrorInfo.DESCRIPTION_ONLY) return ParsingException.create(error.toString(), false);
        error.append(" ");
        positionDescription(positionOffset, error);
        return ParsingException.create(error.toString(), withStackTrace());
    }

    public ParsingException newParseErrorWith(
            String description, @Nullable Object argument
                                             ) {
        return newParseErrorWith(description, 0, "", description, argument, "");
    }

    public ParsingException newParseErrorWith(
            String shortDescription,
            int positionOffset,
            String longDescriptionPrefix,
            String longDescriptionMessage, @Nullable Object argument,
            String longDescriptionSuffix
                                             ) {
        if (errorInfo == ErrorInfo.MINIMAL) return ParsingException.create(shortDescription, false);
        error.setLength(0);
        error.append(longDescriptionPrefix);
        error.append(longDescriptionMessage);
        if (argument != null) {
            error.append(": '");
            error.append(argument);
            error.append("'");
        }
        error.append(longDescriptionSuffix);
        if (errorInfo == ErrorInfo.DESCRIPTION_ONLY) return ParsingException.create(error.toString(), false);
        error.append(" ");
        positionDescription(positionOffset, error);
        return ParsingException.create(error.toString(), withStackTrace());
    }


    public int getCurrentIndex() {
        return currentIndex;
    }



    public int scanNumber() {
        tokenStart = currentIndex - 1;
        int i = 1;
        int ci = currentIndex;
        byte bb = last;
        while (ci < length) {
            bb = buffer[ci++];
            if (bb == ',' || bb == '}' || bb == ']') break;
            i++;
        }
        currentIndex += i - 1;
        last = bb;
        return tokenStart;
    }

    char[] prepareBuffer(int start, int len) throws ParsingException {
        if (len > maxNumberDigits) {
            throw newParseErrorWith("Too many digits detected in number", len, "", "Too many digits detected in number", len, "");
        }
        while (chars.length < len) {
            chars = Arrays.copyOf(chars, chars.length * 2);
        }
        char[] _tmp = chars;
        byte[] _buf = buffer;
        for (int i = 0; i < len; i++) {
            _tmp[i] = (char) _buf[start + i];
        }
        return _tmp;
    }

    boolean allWhitespace(int start, int end) {
        byte[] _buf = buffer;
        for (int i = start; i < end; i++) {
            if (!WHITESPACE[_buf[i] + 128]) return false;
        }
        return true;
    }




    /**
     * Read simple "ascii string" into temporary buffer.
     * String length must be obtained through getTokenStart and getCurrentToken
     *
     * @return temporary buffer
     * @throws ParsingException unable to parse string
     */
    public char[] readSimpleQuote() throws ParsingException {
        if (last != '"') throw newParseError("Expecting '\"' for string start");
        int ci = tokenStart = currentIndex;
        try {
            for (int i = 0; i < tmp.length; i++) {
                byte bb = buffer[ci++];
                if (bb == '"') break;
                tmp[i] = (char) bb;
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
            throw newParseErrorAt("JSON string was not closed with a double quote", 0);
        }
        if (ci > length) throw newParseErrorAt("JSON string was not closed with a double quote", 0);
        currentIndex = ci;
        return tmp;
    }

    /**
     * Read string from JSON input.
     * If values cache is used, string will be looked up from the cache.
     * <p>
     * String value must start and end with a double quote (").
     *
     * @return parsed string
     * @throws IOException error reading string input
     */
    public String readString() throws IOException {
        int len = parseString();
        return valuesCache == null ? new String(chars, 0, len) : valuesCache.get(chars, len);
    }


    int parseString() throws IOException {
        int startIndex = currentIndex;
        if (last != '"') throw newParseError("Expecting '\"' for string start");
        else if (currentIndex == length) throw newParseErrorAt("Premature end of JSON string", 0);

        byte bb;
        int ci = currentIndex;
        char[] _tmp = chars;
        int remaining = length - currentIndex;
        int _tmpLen = Math.min(_tmp.length, remaining);
        int i = 0;
        while (i < _tmpLen) {
            bb = buffer[ci++];
            if (bb == '"') {
                currentIndex = ci;
                return i;
            }
            // If we encounter a backslash, which is a beginning of an escape sequence
            // or a high bit was set - indicating an UTF-8 encoded multibyte character,
            // there is no chance that we can decode the string without instantiating
            // a temporary buffer, so quit this loop
            if ((bb ^ '\\') < 1) break;
            _tmp[i++] = (char) bb;
        }
        if (i == _tmp.length) {
            int newSize = chars.length * 2;
            if (newSize > maxStringBuffer) {
                throw newParseErrorWith("Maximum string buffer limit exceeded", maxStringBuffer);
            }
            _tmp = chars = Arrays.copyOf(chars, newSize);
        }
        _tmpLen = _tmp.length;
        currentIndex = ci;
        int soFar = --currentIndex - startIndex;

        while (!isEndOfStream()) {
            int bc = read();
            if (bc == '"') {
                return soFar;
            }

            if (bc == '\\') {
                if (soFar >= _tmpLen - 6) {
                    int newSize = chars.length * 2;
                    if (newSize > maxStringBuffer) {
                        throw newParseErrorWith("Maximum string buffer limit exceeded", maxStringBuffer);
                    }
                    _tmp = chars = Arrays.copyOf(chars, newSize);
                    _tmpLen = _tmp.length;
                }
                bc = buffer[currentIndex++];

                switch (bc) {
                    case 'b':
                        bc = '\b';
                        break;
                    case 't':
                        bc = '\t';
                        break;
                    case 'n':
                        bc = '\n';
                        break;
                    case 'f':
                        bc = '\f';
                        break;
                    case 'r':
                        bc = '\r';
                        break;
                    case '"':
                    case '/':
                    case '\\':
                        break;
                    case 'u':
                        bc = (hexToInt(buffer[currentIndex++]) << 12) +
                                (hexToInt(buffer[currentIndex++]) << 8) +
                                (hexToInt(buffer[currentIndex++]) << 4) +
                                hexToInt(buffer[currentIndex++]);
                        break;

                    default:
                        throw newParseErrorWith("Invalid escape combination detected", bc);
                }
            } else if ((bc & 0x80) != 0) {
                if (soFar >= _tmpLen - 4) {
                    int newSize = chars.length * 2;
                    if (newSize > maxStringBuffer) {
                        throw newParseErrorWith("Maximum string buffer limit exceeded", maxStringBuffer);
                    }
                    _tmp = chars = Arrays.copyOf(chars, newSize);
                    _tmpLen = _tmp.length;
                }
                int u2 = buffer[currentIndex++];
                if ((bc & 0xE0) == 0xC0) {
                    bc = ((bc & 0x1F) << 6) + (u2 & 0x3F);
                } else {
                    int u3 = buffer[currentIndex++];
                    if ((bc & 0xF0) == 0xE0) {
                        bc = ((bc & 0x0F) << 12) + ((u2 & 0x3F) << 6) + (u3 & 0x3F);
                    } else {
                        int u4 = buffer[currentIndex++];
                        if ((bc & 0xF8) == 0xF0) {
                            bc = ((bc & 0x07) << 18) + ((u2 & 0x3F) << 12) + ((u3 & 0x3F) << 6) + (u4 & 0x3F);
                        } else {
                            // there are legal 5 & 6 byte combinations, but none are _valid_
                            throw newParseErrorAt("Invalid unicode character detected", 0);
                        }

                        if (bc >= 0x10000) {
                            // check if valid unicode
                            if (bc >= 0x110000) {
                                throw newParseErrorAt("Invalid unicode character detected", 0);
                            }

                            // split surrogates
                            int sup = bc - 0x10000;
                            _tmp[soFar++] = (char) ((sup >>> 10) + 0xd800);
                            _tmp[soFar++] = (char) ((sup & 0x3ff) + 0xdc00);
                            continue;
                        }
                    }
                }
            } else if (soFar >= _tmpLen) {
                int newSize = chars.length * 2;
                if (newSize > maxStringBuffer) {
                    throw newParseErrorWith("Maximum string buffer limit exceeded", maxStringBuffer);
                }
                _tmp = chars = Arrays.copyOf(chars, newSize);
                _tmpLen = _tmp.length;
            }

            _tmp[soFar++] = (char) bc;
        }
        throw newParseErrorAt("JSON string was not closed with a double quote", 0);
    }

    private int hexToInt(byte value) throws ParsingException {
        if (value >= '0' && value <= '9') return value - 0x30;
        if (value >= 'A' && value <= 'F') return value - 0x37;
        if (value >= 'a' && value <= 'f') return value - 0x57;
        throw newParseErrorWith("Could not parse unicode escape, expected a hexadecimal digit", value);
    }

    private boolean wasWhiteSpace() {
        switch (last) {
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 32:
            case -96:
                return true;
            case -31:
                if (currentIndex + 1 < length && buffer[currentIndex] == -102 && buffer[currentIndex + 1] == -128) {
                    currentIndex += 2;
                    last = ' ';
                    return true;
                }
                return false;
            case -30:
                if (currentIndex + 1 < length) {
                    byte b1 = buffer[currentIndex];
                    byte b2 = buffer[currentIndex + 1];
                    if (b1 == -127 && b2 == -97) {
                        currentIndex += 2;
                        last = ' ';
                        return true;
                    }
                    if (b1 != -128) return false;
                    switch (b2) {
                        case -128:
                        case -127:
                        case -126:
                        case -125:
                        case -124:
                        case -123:
                        case -122:
                        case -121:
                        case -120:
                        case -119:
                        case -118:
                        case -88:
                        case -87:
                        case -81:
                            currentIndex += 2;
                            last = ' ';
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            case -29:
                if (currentIndex + 1 < length && buffer[currentIndex] == -128 && buffer[currentIndex + 1] == -128) {
                    currentIndex += 2;
                    last = ' ';
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * Read next token (byte) from input JSON.
     * Whitespace will be skipped and next non-whitespace byte will be returned.
     *
     * @return next non-whitespace byte in the JSON input
     * @throws IOException unable to get next byte (end of stream, ...)
     */
    public byte getNextToken() throws IOException {
        read();
        if (WHITESPACE[last + 128]) {
            while (wasWhiteSpace()) {
                read();
            }
        }
        return last;
    }


    public long positionInStream(int offset) {
        return currentPosition + currentIndex - offset;
    }

    public int fillName() throws IOException {
        int hash = calcHash();
        if (read() != ':') {
            if (!wasWhiteSpace() || getNextToken() != ':') {
                throw newParseError("Expecting ':' after attribute name");
            }
        }
        return hash;
    }


    public int calcHash() throws IOException {
        if (last != '"') throw newParseError("Expecting '\"' for attribute name start");
        tokenStart = currentIndex;
        int ci = currentIndex;
        long hash = 0x811c9dc5;
        if (stream != null) {
            while (ci < readLimit) {
                byte b = buffer[ci];
                if (b == '\\') {
                    if (ci == readLimit - 1) {
                        return calcHashAndCopyName(hash, ci);
                    }
                    b = buffer[++ci];
                } else if (b == '"') {
                    break;
                }
                ci++;
                hash ^= b;
                hash *= 0x1000193;
            }
            if (ci >= readLimit) {
                return calcHashAndCopyName(hash, ci);
            }
            nameEnd = currentIndex = ci + 1;
        } else {
            //TODO: use length instead!? this will read data after used buffer size
            while (ci < buffer.length) {
                byte b = buffer[ci++];
                if (b == '\\') {
                    if (ci == buffer.length) throw newParseError("Expecting '\"' for attribute name end");
                    b = buffer[ci++];
                } else if (b == '"') {
                    break;
                }
                hash ^= b;
                hash *= 0x1000193;
            }
            nameEnd = currentIndex = ci;
        }
        return (int) hash;
    }



    private int lastNameLen;

    private int calcHashAndCopyName(long hash, int ci) throws IOException {
        int soFar = ci - tokenStart;
        long startPosition = currentPosition - soFar;
        while (chars.length < soFar) {
            chars = Arrays.copyOf(chars, chars.length * 2);
        }
        int i = 0;
        for (; i < soFar; i++) {
            chars[i] = (char) buffer[i + tokenStart];
        }
        currentIndex = ci;
        do {
            byte b = read();
            if (b == '\\') {
                b = read();
            } else if (b == '"') {
                nameEnd = -1;
                lastNameLen = i;
                return (int) hash;
            }
            if (i == chars.length) {
                chars = Arrays.copyOf(chars, chars.length * 2);
            }
            chars[i++] = (char) b;
            hash ^= b;
            hash *= 0x1000193;
        } while (!isEndOfStream());
        //TODO: check offset
        throw newParseErrorAt("JSON string was not closed with a double quote", (int) startPosition);
    }



    private byte skipString() throws IOException {
        byte c = read();
        boolean inEscape = false;
        while (c != '"' || inEscape) {
            inEscape = !inEscape && c == '\\';
            c = read();
        }
        return getNextToken();
    }

    /**
     * Skip to next non-whitespace token (byte)
     * Will not allocate memory while skipping over JSON input.
     *
     * @return next non-whitespace byte
     * @throws IOException unable to read next byte (end of stream, invalid JSON, ...)
     */
    public byte skip() throws IOException {
        if (last == '"') return skipString();
        if (last == '{') {
            byte nextToken = getNextToken();
            if (nextToken == '}') return getNextToken();
            if (nextToken == '"') {
                nextToken = skipString();
            } else {
                throw newParseError("Expecting '\"' for attribute name");
            }
            if (nextToken != ':') throw newParseError("Expecting ':' after attribute name");
            getNextToken();
            nextToken = skip();
            while (nextToken == ',') {
                nextToken = getNextToken();
                if (nextToken == '"') {
                    nextToken = skipString();
                } else {
                    throw newParseError("Expecting '\"' for attribute name");
                }
                if (nextToken != ':') throw newParseError("Expecting ':' after attribute name");
                getNextToken();
                nextToken = skip();
            }
            if (nextToken != '}') throw newParseError("Expecting '}' for object end");
            return getNextToken();
        }
        if (last == '[') {
            getNextToken();
            byte nextToken = skip();
            while (nextToken == ',') {
                getNextToken();
                nextToken = skip();
            }
            if (nextToken != ']') throw newParseError("Expecting ']' for array end");
            return getNextToken();
        }
        if (last == 'n') {
            if (!wasNull()) throw newParseErrorAt("Expecting 'null' for null constant", 0);
            return getNextToken();
        }
        if (last == 't') {
            if (!wasTrue()) throw newParseErrorAt("Expecting 'true' for true constant", 0);
            return getNextToken();
        }
        if (last == 'f') {
            if (!wasFalse()) throw newParseErrorAt("Expecting 'false' for false constant", 0);
            return getNextToken();
        }
        while (last != ',' && last != '}' && last != ']') {
            read();
        }
        return last;
    }

    /**
     * will be removed
     *
     * @return not used anymore
     * @throws IOException throws if invalid JSON detected
     */
    @Deprecated
    public String readNext() throws IOException {
        int start = currentIndex - 1;
        skip();
        return new String(buffer, start, currentIndex - start - 1, StandardCharsets.UTF_8);
    }

    public byte[] readBase64() throws IOException {
        if (stream != null && Base64.findEnd(buffer, currentIndex) == buffer.length) {
            int len = parseString();
            byte[] input = new byte[len];
            for (int i = 0; i < input.length; i++) {
                input[i] = (byte) chars[i];
            }
            return Base64.decodeFast(input, 0, len);
        }
        if (last != '"') throw newParseError("Expecting '\"' for base64 start");
        int start = currentIndex;
        int end = Base64.findEnd(buffer, start);
        last = buffer[end];
        if (last != '"') throw newParseError("Expecting '\"' for base64 end");
        byte[] result = Base64.decodeFast(buffer, start, end);
        if (result.length == 0 && end > start) {
            throw newParseErrorAt("Invalid base64 detected", 0);
        }
        currentIndex = end + 1;
        return result;
    }

    /**
     * Read key value of JSON input.
     * If key cache is used, it will be looked up from there.
     *
     * @return parsed key value
     * @throws IOException unable to parse string input
     */
    public String readKey() throws IOException {
        int len = parseString();
        String key = keyCache != null ? keyCache.get(chars, len) : new String(chars, 0, len);
        if (getNextToken() != ':') throw newParseError("Expecting ':' after attribute name");
        getNextToken();
        return key;
    }

    /**
     * Custom objects can be deserialized based on the implementation specified through this interface.
     * Annotation processor creates custom deserializers at compile time and registers them into DslJson.
     *
     * @param <T> type
     */
    public interface ReadObject<T> {
        @Nullable
        T read(JsonReader reader) throws IOException;
    }



    public interface ReadJsonObject<T extends JsonObject> {
        @Nullable
        T deserialize(JsonReader reader) throws IOException;
    }

    /**
     * Checks if 'null' value is at current position.
     * This means last read byte was 'n' and 'ull' are next three bytes.
     * If last byte was n but next three are not 'ull' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'null' value is at current position
     * @throws ParsingException invalid 'null' value detected
     */
    public boolean wasNull() throws ParsingException {
        if (last == 'n') {
            if (currentIndex + 2 < length && buffer[currentIndex] == 'u'
                    && buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 'l') {
                currentIndex += 3;
                last = 'l';
                return true;
            }
            throw newParseErrorAt("Invalid null constant found", 0);
        }
        return false;
    }

    /**
     * Checks if 'true' value is at current position.
     * This means last read byte was 't' and 'rue' are next three bytes.
     * If last byte was t but next three are not 'rue' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'true' value is at current position
     * @throws ParsingException invalid 'true' value detected
     */
    public boolean wasTrue() throws ParsingException {
        if (last == 't') {
            if (currentIndex + 2 < length && buffer[currentIndex] == 'r'
                    && buffer[currentIndex + 1] == 'u' && buffer[currentIndex + 2] == 'e') {
                currentIndex += 3;
                last = 'e';
                return true;
            }
            throw newParseErrorAt("Invalid true constant found", 0);
        }
        return false;
    }

    /**
     * Checks if 'false' value is at current position.
     * This means last read byte was 'f' and 'alse' are next four bytes.
     * If last byte was f but next four are not 'alse' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'false' value is at current position
     * @throws ParsingException invalid 'false' value detected
     */
    public boolean wasFalse() throws ParsingException {
        if (last == 'f') {
            if (currentIndex + 3 < length && buffer[currentIndex] == 'a'
                    && buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 's'
                    && buffer[currentIndex + 3] == 'e') {
                currentIndex += 4;
                last = 'e';
                return true;
            }
            throw newParseErrorAt("Invalid false constant found", 0);
        }
        return false;
    }

    /**
     * Check if the last read token is an array end
     *
     * @throws IOException it's not array end
     */
    public void checkArrayEnd() throws IOException {
        if (last != ']') {
            if (currentIndex >= length) throw newParseErrorAt("Unexpected end of JSON in collection", 0, eof);
            throw newParseError("Expecting ']' as array end");
        }
    }


    @Nullable
    private Object readNull(Class<?> manifest) throws IOException {
        if (!wasNull()) throw newParseErrorAt("Expecting 'null' as null constant", 0);
        if (manifest.isPrimitive()) {
            if (manifest == int.class) return 0;
            else if (manifest == long.class) return 0L;
            else if (manifest == short.class) return (short) 0;
            else if (manifest == byte.class) return (byte) 0;
            else if (manifest == float.class) return 0f;
            else if (manifest == double.class) return 0d;
            else if (manifest == boolean.class) return false;
            else if (manifest == char.class) return '\0';
        }
        return null;
    }



    /**
     * Will advance to next token and read the JSON into specified type
     *
     * @param reader reader to use
     * @param <T>    type
     * @return new instance from input JSON
     * @throws IOException unable to process JSON
     */
    @Nullable
    public <T> T next(ReadObject<T> reader) throws IOException {
        if (this.getNextToken() == 'n') {
            if (!wasNull()) throw newParseErrorAt("Expecting 'null' as null constant", 0);
            return null;
        }
        return reader.read(this);
    }





    private static class WithReader<T> implements Iterator<T> {
        private final ReadObject<T> reader;
        private final JsonReader json;

        private boolean hasNext;

        WithReader(ReadObject<T> reader, JsonReader json) {
            this.reader = reader;
            this.json = json;
            hasNext = true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public void remove() {
        }

        @Nullable
        @Override
        public T next() {
            try {
                byte nextToken = json.last();
                T instance;
                if (nextToken == 'n') {
                    if (!json.wasNull()) throw json.newParseErrorAt("Expecting 'null' as null constant", 0);
                    instance = null;
                } else {
                    instance = reader.read(json);
                }
                hasNext = json.getNextToken() == ',';
                if (hasNext) {
                    json.getNextToken();
                } else {
                    if (json.last() != ']') throw json.newParseError("Expecting ']' for iteration end");
                    //TODO: ideally we should release stream bound to reader
                }
                return instance;
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }

    private static class WithObjectReader<T extends JsonObject> implements Iterator<T> {
        private final ReadJsonObject<T> reader;
        private final JsonReader json;

        private boolean hasNext;

        WithObjectReader(ReadJsonObject<T> reader, JsonReader json) {
            this.reader = reader;
            this.json = json;
            hasNext = true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public void remove() {
        }

        @Nullable
        @Override
        public T next() {
            try {
                byte nextToken = json.last();
                T instance;
                if (nextToken == 'n') {
                    if (!json.wasNull()) throw json.newParseErrorAt("Expecting 'null' as null constant", 0);
                    instance = null;
                } else if (nextToken == '{') {
                    json.getNextToken();
                    instance = reader.deserialize(json);
                } else {
                    throw json.newParseError("Expecting '{' for object start in iteration");
                }
                hasNext = json.getNextToken() == ',';
                if (hasNext) {
                    json.getNextToken();
                } else {
                    if (json.last() != ']') throw json.newParseError("Expecting ']' for iteration end");
                    //TODO: ideally we should release stream bound to reader
                }
                return instance;
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }
}
