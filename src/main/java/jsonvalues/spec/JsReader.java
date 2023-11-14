package jsonvalues.spec;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static jsonvalues.spec.ParserErrors.UNEXPECTED_END_OF_JSON;

/**
 * Object for processing JSON from byte[] and InputStream. The only public methods are {@link #readNextToken()} and
 * {@link #reset()}. Most of the time you don't need to parse a JSON token by token since there are a lot of high-level
 * and efficient alternatives implemented in json-values. Just in case, find below an example:
 *
 * <pre>
 *     {@code
 * String json = "[1,2,3]";
 * var reader = JsonIO.INSTANCE.createReader(json.getBytes(StandardCharsets.UTF_8));
 * byte token;
 * while ((token = reader.readNextToken()) != ']'){
 * if(Character.isDigit(token)) System.out.println(Character.toString(token));
 * }
 *
 *
 *     }
 *
 * </pre>
 *
 * <p>
 * You can also  read tokens as JsValue and validate them using the method
 * {@link jsonvalues.spec.JsSpec#parse(String)}
 */
 final class JsReader {

    private static final boolean[] WHITESPACE = new boolean[256];

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

    private final StringCache keyCache;
    private final StringCache valuesCache;
    private final byte[] originalBuffer;
    private final int originalBufferLenWithExtraSpace;
    private final int maxStringBuffer;
    byte[] buffer;
    char[] chars;

    DoublePrecision doublePrecision;
    int doubleLengthLimit;
    int maxNumberDigits;
    private final Deque<Integer> markPositions = new ArrayDeque<>();
    private int currentIndex = 0;
    private long currentPosition = 0;
    private byte last = ' ';
    private int length;

    private InputStream stream;
    private int readLimit;
    //always leave some room for reading special stuff, so that buffer contains enough padding for such optimizations
    private int bufferLenWithExtraSpace;

    private JsReader(char[] tmp,
                     byte[] buffer,
                     int length,
                     StringCache keyCache,
                     StringCache valuesCache,
                     DoublePrecision doublePrecision,
                     int maxNumberDigits,
                     int maxStringBuffer
                    ) {
        this.buffer = buffer;
        this.length = length;
        this.bufferLenWithExtraSpace = buffer.length - 38; //currently maximum padding is for uuid
        this.chars = tmp;
        this.keyCache = keyCache;
        this.valuesCache = valuesCache;
        this.doublePrecision = doublePrecision;
        this.maxNumberDigits = maxNumberDigits;
        this.maxStringBuffer = maxStringBuffer;
        this.doubleLengthLimit = 15 + doublePrecision.level;
        this.originalBuffer = buffer;
        this.originalBufferLenWithExtraSpace = bufferLenWithExtraSpace;
    }

    JsReader(byte[] buffer,
             int length,
             char[] tmp,
             StringCache keyCache,
             StringCache valuesCache,
             DoublePrecision doublePrecision,
             int maxNumberDigits,
             int maxStringBuffer
            ) {
        this(tmp, buffer, length, keyCache, valuesCache, doublePrecision, maxNumberDigits, maxStringBuffer);
        if (length > buffer.length) {
            throw new IllegalArgumentException("length can't be longer than buffer.length");
        } else if (length < buffer.length) {
            buffer[length] = '\0';
        }
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

    /**
     * Reset reader after processing input It will release reference to provided byte[] or InputStream input
     */
    public void reset() {
        this.buffer = this.originalBuffer;
        this.bufferLenWithExtraSpace = this.originalBufferLenWithExtraSpace;
        this.currentIndex = 0;
        this.length = 0;
        this.readLimit = 0;
        this.stream = null;
    }

    /**
     * Bind input stream for processing. Stream will be processed in byte[] chunks. If stream is null, reference to
     * stream will be released.
     *
     * @param stream set input stream
     * @return itself
     * @throws JsParserException unable to read from stream
     */
    JsReader process(InputStream stream) throws JsParserException {
        this.currentPosition = 0;
        this.currentIndex = 0;
        this.stream = stream;
        this.readLimit = Math.min(this.length, bufferLenWithExtraSpace);
        int available;
        try {
            available = readFully(buffer, stream, 0);
        } catch (IOException e) {
            throw newParseError(e.getMessage());
        }
        readLimit = Math.min(available, bufferLenWithExtraSpace);
        this.length = available;
        return this;
    }

    /**
     * Bind byte[] buffer for processing. If this method is used in combination with process(InputStream) this buffer
     * will be used for processing chunks of stream. If null is sent for byte[] buffer, new length for valid input will
     * be set for existing buffer.
     *
     * @param newBuffer new buffer to use for processing
     * @param newLength length of buffer which can be used
     * @return itself
     */
    JsReader process(byte[] newBuffer, int newLength) {
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
    int length() {
        return length;
    }

    /**
     * Read next byte from the JSON input. If buffer has been read in full IOException will be thrown
     *
     * @return next byte
     * @throws JsParserException when end of JSON input
     */
    byte read() throws JsParserException {
        if (stream != null && currentIndex > readLimit) {
            prepareNextBlock();
        }
        if (currentIndex >= length) {
            throw JsParserException.reasonAt(UNEXPECTED_END_OF_JSON, currentIndex);
        }
        return last = buffer[currentIndex++];
    }

    private int prepareNextBlock() throws JsParserException {
        int len = length - currentIndex;
        System.arraycopy(buffer, currentIndex, buffer, 0, len);
        int available;
        try {
            available = readFully(buffer, stream, len);
        } catch (IOException e) {
            throw newParseError(e.getMessage());
        }
        currentPosition += currentIndex;
        if (available == len) {
            readLimit = length - currentIndex;
            length = readLimit;
        } else {
            readLimit = Math.min(available, bufferLenWithExtraSpace);
            this.length = available;
        }
        currentIndex = 0;
        return available;
    }

    boolean isEndOfStream() throws JsParserException {
        if (stream == null) {
            return length == currentIndex;
        }
        if (length != currentIndex) {
            return false;
        }
        return prepareNextBlock() == 0;
    }

    /**
     * Which was last byte read from the JSON input. JsonReader doesn't allow to go back, but it remembers previously
     * read byte
     *
     * @return which was the last byte read
     */
    byte last() {
        return last;
    }

    private long getPositionInStream(int offset) {
        return currentPosition + currentIndex - offset;
    }

    long getPositionInStream() {
        return getPositionInStream(0);
    }

    JsParserException newParseError(String description) {
        return JsParserException.reasonAt(description, getPositionInStream(0));
    }

    JsParserException newParseError(String description, int offset) {
        return JsParserException.reasonAt(description, getPositionInStream(offset));
    }

    int getCurrentIndex() {
        return currentIndex;
    }

    int scanNumber() {
        int tokenStart = currentIndex - 1;
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

    char[] prepareBuffer(int start, int len) throws JsParserException {
        if (len > maxNumberDigits) {
            throw newParseError(ParserErrors.TOO_MANY_DIGITS.formatted(len), len);
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
     * Read string from JSON input. If values cache is used, string will be looked up from the cache.
     * <p>
     * String value must start and end with a double quote (").
     *
     * @return parsed string
     * @throws JsParserException code reading string input
     */
    String readString() throws JsParserException {
        int len = parseString();
        return valuesCache == null ? new String(chars, 0, len) : valuesCache.get(chars, len);
    }

    int parseString() throws JsParserException {
        int startIndex = currentIndex;
        if (last != '"') throw newParseError(ParserErrors.EXPECTING_STRING_START);
        else if (currentIndex == length) throw newParseError(ParserErrors.PREMATURE_END_OF_JSONSTRING);

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
            // or a high bit was set - indicating a UTF-8 encoded multibyte character,
            // there is no chance that we can decode the string without instantiating
            // a temporary buffer, so quit this loop
            if ((bb ^ '\\') < 1) break;
            _tmp[i++] = (char) bb;
        }
        if (i == _tmp.length) {
            int newSize = chars.length * 2;
            if (newSize > maxStringBuffer) {
                throw newParseError(ParserErrors.MAXIMUM_STRING_BUFFER_REACHED.formatted(maxStringBuffer));
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
                        throw newParseError(ParserErrors.MAXIMUM_STRING_BUFFER_REACHED.formatted(maxStringBuffer));
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
                        throw newParseError(ParserErrors.INVALID_ESCAPE_CHARACTER.formatted(bc));
                }
            } else if ((bc & 0x80) != 0) {
                if (soFar >= _tmpLen - 4) {
                    int newSize = chars.length * 2;
                    if (newSize > maxStringBuffer) {
                        throw newParseError(ParserErrors.MAXIMUM_STRING_BUFFER_REACHED.formatted(maxStringBuffer));
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
                            throw newParseError(ParserErrors.INVALID_UNICODE_CHARACTER);
                        }

                        if (bc >= 0x10000) {
                            // check if valid unicode
                            if (bc >= 0x110000) {
                                throw newParseError(ParserErrors.INVALID_UNICODE_CHARACTER);
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
                    throw newParseError(ParserErrors.MAXIMUM_STRING_BUFFER_REACHED.formatted(maxStringBuffer));
                }
                _tmp = chars = Arrays.copyOf(chars, newSize);
                _tmpLen = _tmp.length;
            }

            _tmp[soFar++] = (char) bc;
        }
        throw newParseError(ParserErrors.STRING_NOT_CLOSED);
    }

    private int hexToInt(byte value) throws JsParserException {
        if (value >= '0' && value <= '9') return value - 0x30;
        if (value >= 'A' && value <= 'F') return value - 0x37;
        if (value >= 'a' && value <= 'f') return value - 0x57;
        throw newParseError(ParserErrors.INVALID_HEX);
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
     * Read next token (byte) from input JSON. Whitespace will be skipped and next non-whitespace byte will be
     * returned.
     *
     * @return next non-whitespace byte in the JSON input
     * @throws JsParserException unable to get next byte (end of stream, ...)
     */
    public byte readNextToken() throws JsParserException {
        read();
        if (WHITESPACE[last + 128]) {
            while (wasWhiteSpace()) {
                read();
            }
        }
        return last;
    }

    /**
     * Read key value of JSON input. If key cache is used, it will be looked up from there.
     *
     * @return parsed key value
     */
    String readKey() {
        int len = parseString();
        String key = keyCache != null ? keyCache.get(chars, len) : new String(chars, 0, len);
        if (readNextToken() != ':') throw newParseError(ParserErrors.EXPECTING_COLON);
        readNextToken();
        return key;
    }

    /**
     * Checks if 'null' value is at current position. This means last read byte was 'n' and 'ull' are next three bytes.
     * If last byte was n but next three are not 'ull' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'null' value is at current position
     * @throws JsParserException invalid 'null' value detected
     */
    boolean wasNull() throws JsParserException {
        if (last == 'n') {
            if (currentIndex + 2 < length && buffer[currentIndex] == 'u'
                && buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 'l') {
                currentIndex += 3;
                last = 'l';
                return true;
            }
            throw newParseError(ParserErrors.INVALID_NULL);
        }
        return false;
    }

    /**
     * Checks if 'true' value is at current position. This means last read byte wasn't' and 'rue' are next three bytes.
     * If last byte was t but next three are not 'rue' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'true' value is at current position
     * @throws JsParserException invalid 'true' value detected
     */
    boolean wasTrue() throws JsParserException {
        if (last == 't') {
            if (currentIndex + 2 < length && buffer[currentIndex] == 'r'
                && buffer[currentIndex + 1] == 'u' && buffer[currentIndex + 2] == 'e') {
                currentIndex += 3;
                last = 'e';
                return true;
            }
            throw newParseError(ParserErrors.INVALID_TRUE_CONSTANT);
        }
        return false;
    }

    /**
     * Checks if 'false' value is at current position. This means last read byte was 'f' and 'alse' are next four bytes.
     * If last byte was f but next four are not 'alse' it will throw since that is not a valid JSON construct.
     *
     * @return true if 'false' value is at current position
     * @throws JsParserException invalid 'false' value detected
     */
    boolean wasFalse() throws JsParserException {
        if (last == 'f') {
            if (currentIndex + 3 < length && buffer[currentIndex] == 'a'
                && buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 's'
                && buffer[currentIndex + 3] == 'e') {
                currentIndex += 4;
                last = 'e';
                return true;
            }
            throw newParseError(ParserErrors.INVALID_FALSE_CONSTANT);
        }
        return false;
    }

    /**
     * Check if the last read token is an array end
     */
    void checkArrayEnd() {
        if (last != ']') {
            if (currentIndex >= length) throw newParseError(ParserErrors.UNEXPECTED_END_OF_ARRAY);
            throw newParseError(ParserErrors.EXPECTING_END_OF_ARRAY);
        }
    }

    /**
     * Sets a mark at the current parsing position. This allows you to later roll back to this marked position using
     * {@link #rollbackToMark()}.
     */
    void setMark() {
        markPositions.push(currentIndex);
    }

    /**
     * Rolls back the parsing position to the last marked position set using {@link #setMark()}. This is useful for
     * reverting to a previous parsing state within the JSON document.
     *
     * @throws IllegalArgumentException if the stack of marks is empty and no mark is available to roll back to.
     */
    void rollbackToMark() {
        if (!markPositions.isEmpty()) {
            currentIndex = markPositions.pop();
            last = (currentIndex > 0) ? buffer[currentIndex - 1] : (byte) ' ';

        } else {
            throw new IllegalArgumentException("Stack of marks is empty. No mark available to roll back to.");
        }
    }

    enum DoublePrecision {
        EXACT(0),
        HIGH(1),
        DEFAULT(3),
        LOW(4);

        final int level;

        DoublePrecision(int level) {
            this.level = level;
        }
    }


}
