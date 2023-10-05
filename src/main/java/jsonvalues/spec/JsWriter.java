package jsonvalues.spec;

import jsonvalues.JsSerializerException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * DslJson writes JSON into JsonWriter which has two primary modes of operation:
 * <p>
 * * targeting specific output stream
 * * buffering the entire response in memory
 * <p>
 * In both cases JsonWriter writes into an byte[] buffer.
 * If stream is used as target, it will copy buffer into the stream whenever there is no more room in buffer for new data.
 * If stream is not used as target, it will grow the buffer to hold the encoded result.
 * To use stream as target reset(OutputStream) must be called before processing.
 * This class provides low level methods for JSON serialization.
 * <p>
 * After the processing is done,
 * in case then stream was used as target, flush() must be called to copy the remaining of the buffer into stream.
 * When entire response was buffered in memory, buffer can be copied to stream or resulting byte[] can be used directly.
 * <p>
 * For maximum performance JsonWriter instances should be reused (to avoid allocation of new byte[] buffer instances).
 * They should not be shared across threads (concurrently) so for Thread reuse it's best to use patterns such as ThreadLocal.
 */
class JsWriter {


    JsWriter(byte[] buffer) {
        this.buffer = buffer;
    }

    byte[] ensureCapacity(int free) {
        if (position + free >= buffer.length) {
            enlargeOrFlush(position, free);
        }
        return buffer;
    }

    void advance(int size) {
        position += size;
    }

    private int position;
    private OutputStream target;
    private byte[] buffer;


    JsWriter(int size) {
        this.buffer = new byte[size];
    }


    /**
     * Helper for writing JSON object start: {
     */
    static byte OBJECT_START = '{';
    /**
     * Helper for writing JSON object end: }
     */
    static byte OBJECT_END = '}';
    /**
     * Helper for writing JSON array start: [
     */
    static byte ARRAY_START = '[';
    /**
     * Helper for writing JSON array end: ]
     */
    static byte ARRAY_END = ']';
    /**
     * Helper for writing comma separator: ,
     */
    static byte COMMA = ',';
    /**
     * Helper for writing semicolon: :
     */
    static byte SEMI = ':';
    /**
     * Helper for writing JSON quote: "
     */
    static byte QUOTE = '"';
    /**
     * Helper for writing JSON escape: \\
     */
    static byte ESCAPE = '\\';

    private void enlargeOrFlush(int size, int padding) {
        if (target != null) {
            try {
                target.write(buffer, 0, size);
            } catch (IOException ex) {
                throw new JsSerializerException("Unable to write to target stream.", ex);
            }
            position = 0;
            if (padding > buffer.length) {
                buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + padding);
            }
        } else {
            buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + padding);
        }
    }

    /**
     * Optimized method for writing 'null' into the JSON.
     */
    void writeNull() {
        if ((position + 4) >= buffer.length) {
            enlargeOrFlush(position, 0);
        }
        int s = position;
        byte[] _result = buffer;
        _result[s] = 'n';
        _result[s + 1] = 'u';
        _result[s + 2] = 'l';
        _result[s + 3] = 'l';
        position += 4;
    }

    /**
     * Write a single byte into the JSON.
     *
     * @param value byte to write into the JSON
     */
    void writeByte(byte value) {
        if (position == buffer.length) {
            enlargeOrFlush(position, 0);
        }
        buffer[position++] = value;
    }

    /**
     * Write a quoted string into the JSON.
     * String will be appropriately escaped according to JSON escaping rules.
     *
     * @param value string to write
     */
    void writeString(String value) {
        int len = value.length();
        if (position + (len << 2) + (len << 1) + 2 >= buffer.length) {
            enlargeOrFlush(position, (len << 2) + (len << 1) + 2);
        }
        byte[] _result = buffer;
        _result[position] = QUOTE;
        int cur = position + 1;
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c > 31 && c != '"' && c != '\\' && c < 126) {
                _result[cur++] = (byte) c;
            } else {
                writeQuotedString(value, i, cur, len);
                return;
            }
        }
        _result[cur] = QUOTE;
        position = cur + 1;
    }


    private void writeQuotedString(CharSequence str, int i, int cur, int len) {
        byte[] _result = this.buffer;
        for (; i < len; i++) {
            char c = str.charAt(i);
            if (c == '"') {
                _result[cur++] = ESCAPE;
                _result[cur++] = QUOTE;
            } else if (c == '\\') {
                _result[cur++] = ESCAPE;
                _result[cur++] = ESCAPE;
            } else if (c < 32) {
                if (c == 8) {
                    _result[cur++] = ESCAPE;
                    _result[cur++] = 'b';
                } else if (c == 9) {
                    _result[cur++] = ESCAPE;
                    _result[cur++] = 't';
                } else if (c == 10) {
                    _result[cur++] = ESCAPE;
                    _result[cur++] = 'n';
                } else if (c == 12) {
                    _result[cur++] = ESCAPE;
                    _result[cur++] = 'f';
                } else if (c == 13) {
                    _result[cur++] = ESCAPE;
                    _result[cur++] = 'r';
                } else {
                    _result[cur] = ESCAPE;
                    _result[cur + 1] = 'u';
                    _result[cur + 2] = '0';
                    _result[cur + 3] = '0';
                    switch (c) {
                        case 0 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '0';
                        }
                        case 1 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '1';
                        }
                        case 2 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '2';
                        }
                        case 3 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '3';
                        }
                        case 4 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '4';
                        }
                        case 5 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '5';
                        }
                        case 6 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '6';
                        }
                        case 7 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = '7';
                        }
                        case 11 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = 'B';
                        }
                        case 14 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = 'E';
                        }
                        case 15 -> {
                            _result[cur + 4] = '0';
                            _result[cur + 5] = 'F';
                        }
                        case 16 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '0';
                        }
                        case 17 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '1';
                        }
                        case 18 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '2';
                        }
                        case 19 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '3';
                        }
                        case 20 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '4';
                        }
                        case 21 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '5';
                        }
                        case 22 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '6';
                        }
                        case 23 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '7';
                        }
                        case 24 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '8';
                        }
                        case 25 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = '9';
                        }
                        case 26 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'A';
                        }
                        case 27 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'B';
                        }
                        case 28 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'C';
                        }
                        case 29 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'D';
                        }
                        case 30 -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'E';
                        }
                        default -> {
                            _result[cur + 4] = '1';
                            _result[cur + 5] = 'F';
                        }
                    }
                    cur += 6;
                }
            } else if (c < 0x007F) {
                _result[cur++] = (byte) c;
            } else {
                int cp = Character.codePointAt(str, i);
                if (Character.isSupplementaryCodePoint(cp)) {
                    i++;
                }
                if (cp == 0x007F) {
                    _result[cur++] = (byte) cp;
                } else if (cp <= 0x7FF) {
                    _result[cur++] = (byte) (0xC0 | ((cp >> 6) & 0x1F));
                    _result[cur++] = (byte) (0x80 | (cp & 0x3F));
                } else if ((cp < 0xD800) || (cp > 0xDFFF && cp <= 0xFFFF)) {
                    _result[cur++] = (byte) (0xE0 | ((cp >> 12) & 0x0F));
                    _result[cur++] = (byte) (0x80 | ((cp >> 6) & 0x3F));
                    _result[cur++] = (byte) (0x80 | (cp & 0x3F));
                } else if (cp >= 0x10000 && cp <= 0x10FFFF) {
                    _result[cur++] = (byte) (0xF0 | ((cp >> 18) & 0x07));
                    _result[cur++] = (byte) (0x80 | ((cp >> 12) & 0x3F));
                    _result[cur++] = (byte) (0x80 | ((cp >> 6) & 0x3F));
                    _result[cur++] = (byte) (0x80 | (cp & 0x3F));
                } else {
                    throw new JsSerializerException("Unknown unicode codepoint in string! " + Integer.toHexString(cp));
                }
            }
        }
        _result[cur] = QUOTE;
        position = cur + 1;
    }

    /**
     * Write string consisting of only ascii characters.
     * String will not be escaped according to JSON escaping rules.
     *
     * @param value ascii string
     */
    @SuppressWarnings("deprecation")
    void writeAscii(String value) {
        int len = value.length();
        if (position + len >= buffer.length) {
            enlargeOrFlush(position, len);
        }
        value.getBytes(0, len, buffer, position);
        position += len;
    }


    /**
     * Encode bytes as Base 64.
     * Provided value can't be null.
     *
     * @param value bytes to encode
     */
    void writeBinary(byte[] value) {
        if (position + (value.length << 1) + 2 >= buffer.length) {
            enlargeOrFlush(position, (value.length << 1) + 2);
        }
        buffer[position++] = '"';
        position += Base64.encodeToBytes(value, buffer, position);
        buffer[position++] = '"';
    }

    @SuppressWarnings("IdentityBinaryExpression")
    @Override
    public String toString() {
        return new String(buffer, 0, position, UTF_8);
    }


    /**
     * Current buffer.
     * If buffer grows, a new instance will be created and old one will not be used anymore.
     *
     * @return current buffer
     */
    byte[] getByteBuffer() {
        return buffer;
    }

    /**
     * Current position in the buffer. When stream is not used, this is also equivalent
     * to the size of the resulting JSON in bytes
     *
     * @return position in the populated buffer
     */
    int size() {
        return position;
    }

    /**
     * Resets the writer - same as calling reset(OutputStream = null)
     */
    void reset() {
        reset(null);
    }

    /**
     * Resets the writer - specifies the target stream and sets the position in buffer to 0.
     * If stream is set to null, JsonWriter will work in growing byte[] buffer mode (entire response will be buffered in memory).
     *
     * @param stream sets/clears the target stream
     */
    void reset(OutputStream stream) {
        position = 0;
        target = stream;
    }

    /**
     * If stream was used, copies the buffer to stream and resets the position in buffer to 0.
     * It will not reset the stream as target,
     * meaning new usages of the JsonWriter will try to use the already provided stream.
     * It will not do anything if stream was not used
     * <p>
     * To reset the stream to null use reset() or reset(OutputStream) methods.
     */
    void flush() {
        if (target != null && position != 0) {
            try {
                target.write(buffer, 0, position);
            } catch (IOException ex) {
                throw new JsSerializerException("Unable to write to target stream.", ex);
            }
            position = 0;
        }
    }



    /**
     * Custom objects can be serialized based on the implementation specified through this interface.
     * Annotation processor creates custom deserializers at compile time and registers them into DslJson.
     *
     * @param <T> type
     */
    interface WriteObject<T> {
        void write(JsWriter writer, T value);
    }


}
