package com.dslplatform.json;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 Modified class from dsl-json library.
 deserializeInt, deserializeLong and so long, convert string numbers into numbers:
 "123" -> 123
 which I consider it's a mistake. The change consists of throwing an error
 */

@SuppressWarnings("rawtypes")
 abstract class MyNumberConverter {


    private final static int[] DIGITS = new int[1000];
    private final static int[] DIFF = {111, 222, 444, 888, 1776};
    private final static int[] ERROR = {50, 100, 200, 400, 800};
    private final static int[] SCALE_10 = {10000, 1000, 100, 10, 1};

    @SuppressWarnings("FloatingPointLiteralPrecision")
    private final static double[] POW_10 = {
            1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9,
            1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19,
            1e20, 1e21, 1e22, 1e23, 1e24, 1e25, 1e26, 1e27, 1e28, 1e29,
            1e30, 1e31, 1e32, 1e33, 1e34, 1e35, 1e36, 1e37, 1e38, 1e39,
            1e40, 1e41, 1e42, 1e43, 1e44, 1e45, 1e46, 1e47, 1e48, 1e49,
            1e50, 1e51, 1e52, 1e53, 1e54, 1e55, 1e56, 1e57, 1e58, 1e59,
            1e60, 1e61, 1e62, 1e63, 1e64, 1e65
    };

    static {
        for (int i = 0; i < DIGITS.length; i++) {
            DIGITS[i] = (i < 10 ? (2 << 24) : i < 100 ? (1 << 24) : 0)
                    + (((i / 100) + '0') << 16)
                    + ((((i / 10) % 10) + '0') << 8)
                    + i % 10 + '0';
        }
    }


    static void numberException(final JsonReader reader,
                                final int start,
                                final int end,
                                String message) throws ParsingException {
        final int len = end - start;
        if (len > reader.maxNumberDigits) {
            throw reader.newParseErrorWith("Too many digits detected in number",
                                           len,
                                           "",
                                           "Too many digits detected in number",
                                           end,
                                           ""
                                          );
        }
        throw reader.newParseErrorWith("Error parsing number",
                                       len,
                                       "",
                                       message,
                                       null,
                                       ". Error parsing number"
                                      );
    }

    static void numberException(final JsonReader reader,
                                final int start,
                                final int end,
                                String message,
                                Object messageArgument) throws ParsingException {
        final int len = end - start;
        if (len > reader.maxNumberDigits) {
            throw reader.newParseErrorWith("Too many digits detected in number",
                                           len,
                                           "",
                                           "Too many digits detected in number",
                                           end,
                                           ""
                                          );
        }
        throw reader.newParseErrorWith("Error parsing number",
                                       len,
                                       "",
                                       message,
                                       messageArgument,
                                       ". Error parsing number"
                                      );
    }

    private static BigDecimal parseNumberGeneric(final char[] buf,
                                                 final int len,
                                                 final JsonReader reader,
                                                 final boolean withQuotes) throws ParsingException {
        int end = len;
        while (end > 0 && Character.isWhitespace(buf[end - 1])) {
            end--;
        }
        if (end > reader.maxNumberDigits) {
            throw reader.newParseErrorWith("Too many digits detected in number",
                                           len,
                                           "",
                                           "Too many digits detected in number",
                                           end,
                                           ""
                                          );
        }
        final int offset = buf[0] == '-' ? 1 : 0;
        if (buf[offset] == '0' && end > offset + 1 && buf[offset + 1] >= '0' && buf[offset + 1] <= '9') {
            throw reader.newParseErrorAt("Leading zero is not allowed. Error parsing number",
                                         len + (withQuotes ? 2 : 0)
                                        );
        }
        try {
            return new BigDecimal(buf,
                                  0,
                                  end
            );
        } catch (NumberFormatException nfe) {
            throw reader.newParseErrorAt("Error parsing number",
                                         len + (withQuotes ? 2 : 0),
                                         nfe
                                        );
        }
    }

    private static class NumberInfo {
        final char[] buffer;
        final int length;

        NumberInfo(final char[] buffer,
                   final int length) {
            this.buffer = buffer;
            this.length = length;
        }
    }

    private static MyNumberConverter.NumberInfo readLongNumber(final JsonReader reader,
                                                               final int start) throws IOException {
        int len = reader.length() - start;
        char[] result = reader.prepareBuffer(start,
                                             len
                                            );
        while (reader.length() == reader.getCurrentIndex()) {
            if (reader.isEndOfStream()) break;
            reader.scanNumber(); // peek, do not read
            int end    = reader.getCurrentIndex();
            int oldLen = len;
            len += end;
            if (len > reader.maxNumberDigits) {
                throw reader.newParseErrorFormat("Too many digits detected in number",
                                                 len,
                                                 "Number of digits larger than %d. Unable to read number",
                                                 reader.maxNumberDigits
                                                );
            }
            char[] tmp = result;
            result = new char[len];
            System.arraycopy(tmp,
                             0,
                             result,
                             0,
                             oldLen
                            );
            System.arraycopy(reader.prepareBuffer(0,
                                                  end
                                                 ),
                             0,
                             result,
                             oldLen,
                             end
                            );
        }
        return new MyNumberConverter.NumberInfo(result,
                                                len
        );
    }

    public static double deserializeDouble(final JsonReader reader) throws IOException {
        final int    start = reader.scanNumber();
        final int    end   = reader.getCurrentIndex();
        final byte[] buf   = reader.buffer;
        final byte   ch    = buf[start];
        if (ch == '-') {
            return -parseDouble(buf,
                                reader,
                                start,
                                end,
                                1
                               );
        }
        return parseDouble(buf,
                           reader,
                           start,
                           end,
                           0
                          );
    }

    private static double parseDouble(final byte[] buf,
                                      final JsonReader reader,
                                      final int start,
                                      final int end,
                                      final int offset) throws IOException {
        if (end - start - offset > reader.doubleLengthLimit) {
            if (end == reader.length()) {
                final MyNumberConverter.NumberInfo tmp = readLongNumber(reader,
                                                                        start + offset
                                                                       );
                return parseDoubleGeneric(tmp.buffer,
                                          tmp.length,
                                          reader,
                                          false
                                         );
            }
            return parseDoubleGeneric(reader.prepareBuffer(start + offset,
                                                           end - start - offset
                                                          ),
                                      end - start - offset,
                                      reader,
                                      false
                                     );
        }
        long          value       = 0;
        byte          ch          = ' ';
        int           i           = start + offset;
        final boolean leadingZero = buf[start + offset] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + offset + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (i > start + offset && reader.allWhitespace(i,
                                                               end
                                                              )) return value;
                numberException(reader,
                                start,
                                end,
                                "Unknown digit",
                                (char) ch
                               );
            }
            value = (value << 3) + (value << 1) + ind;
        }
        if (i == start + offset) numberException(reader,
                                                 start,
                                                 end,
                                                 "Digit not found"
                                                );
        else if (leadingZero && ch != '.' && i > start + offset + 1) numberException(reader,
                                                                                     start,
                                                                                     end,
                                                                                     "Leading zero is not allowed"
                                                                                    );
        else if (i == end) return value;
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Number ends with a dot"
                                         );
            final int    maxLen;
            final double preciseDividor;
            final int    expDiff;
            final int    decPos = i;
            final int    decOffset;
            if (value == 0) {
                maxLen = i + 15;
                ch = buf[i];
                if (ch == '0' && end > maxLen) {
                    return parseDoubleGeneric(reader.prepareBuffer(start + offset,
                                                                   end - start - offset
                                                                  ),
                                              end - start - offset,
                                              reader,
                                              false
                                             );
                }
                else if (ch < '8') {
                    preciseDividor = 1e14;
                    expDiff = -1;
                    decOffset = 1;
                }
                else {
                    preciseDividor = 1e15;
                    expDiff = 0;
                    decOffset = 0;
                }
            }
            else {
                maxLen = start + offset + 16;
                if (buf[start + offset] < '8') {
                    preciseDividor = 1e14;
                    expDiff = i - maxLen + 14;
                    decOffset = 1;
                }
                else {
                    preciseDividor = 1e15;
                    expDiff = i - maxLen + 15;
                    decOffset = 0;
                }
            }
            final int numLimit = maxLen < end ? maxLen : end;
            //TODO zeros
            for (; i < numLimit; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) return value / POW_10[i - decPos - 1];
                    numberException(reader,
                                    start,
                                    end,
                                    "Unknown digit",
                                    (char) buf[i]
                                   );
                }
                value = (value << 3) + (value << 1) + ind;
            }
            if (i == end) return value / POW_10[i - decPos - 1];
            else if (ch == 'e' || ch == 'E') {
                return doubleExponent(reader,
                                      value,
                                      i - decPos,
                                      0,
                                      buf,
                                      start,
                                      end,
                                      offset,
                                      i
                                     );
            }
            if (reader.doublePrecision == JsonReader.DoublePrecision.HIGH) {
                return parseDoubleGeneric(reader.prepareBuffer(start + offset,
                                                               end - start - offset
                                                              ),
                                          end - start - offset,
                                          reader,
                                          false
                                         );
            }
            int       decimals = 0;
            final int decLimit = start + offset + 18 < end ? start + offset + 18 : end;
            final int remPos   = i;
            for (; i < decLimit; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) {
                        return approximateDouble(decimals,
                                                 value / preciseDividor,
                                                 i - remPos - decOffset
                                                );
                    }
                    numberException(reader,
                                    start,
                                    end,
                                    "Unknown digit",
                                    (char) buf[i]
                                   );
                }
                decimals = (decimals << 3) + (decimals << 1) + ind;
            }
            final double number = approximateDouble(decimals,
                                                    value / preciseDividor,
                                                    i - remPos - decOffset
                                                   );
            while (i < end && ch >= '0' && ch <= '9') {
                ch = buf[i++];
            }
            if (ch == 'e' || ch == 'E') {
                return doubleExponent(reader,
                                      0,
                                      expDiff,
                                      number,
                                      buf,
                                      start,
                                      end,
                                      offset,
                                      i
                                     );
            }
            else if (expDiff > 0) {
                return number * POW_10[expDiff - 1];
            }
            else if (expDiff < 0) {
                return number / POW_10[-expDiff - 1];
            }
            else {
                return number;
            }
        }
        else if (ch == 'e' || ch == 'E') {
            return doubleExponent(reader,
                                  value,
                                  0,
                                  0,
                                  buf,
                                  start,
                                  end,
                                  offset,
                                  i
                                 );
        }
        return value;
    }

    private static double approximateDouble(final int decimals,
                                            final double precise,
                                            final int digits) {
        final long bits    = Double.doubleToRawLongBits(precise);
        final int  exp     = (int) (bits >> 52) - 1022;
        final int  missing = (decimals * SCALE_10[digits + 1] + ERROR[exp]) / DIFF[exp];
        return Double.longBitsToDouble(bits + missing);
    }

    private static double doubleExponent(JsonReader reader,
                                         final long whole,
                                         final int decimals,
                                         double fraction,
                                         byte[] buf,
                                         int start,
                                         int end,
                                         int offset,
                                         int i) throws IOException {
        if (reader.doublePrecision == JsonReader.DoublePrecision.EXACT) {
            return parseDoubleGeneric(reader.prepareBuffer(start + offset,
                                                           end - start - offset
                                                          ),
                                      end - start - offset,
                                      reader,
                                      false
                                     );
        }
        byte ch;
        ch = buf[++i];
        final int exp;
        if (ch == '-') {
            exp = parseNegativeInt(buf,
                                   reader,
                                   i,
                                   end
                                  ) - decimals;
        }
        else if (ch == '+') {
            exp = parsePositiveInt(buf,
                                   reader,
                                   i,
                                   end,
                                   1
                                  ) - decimals;
        }
        else {
            exp = parsePositiveInt(buf,
                                   reader,
                                   i,
                                   end,
                                   0
                                  ) - decimals;
        }
        if (fraction == 0) {
            if (exp == 0 || whole == 0) return whole;
            else if (exp > 0 && exp < POW_10.length) return whole * POW_10[exp - 1];
            else if (exp < 0 && -exp < POW_10.length) return whole / POW_10[-exp - 1];
            else if (reader.doublePrecision != JsonReader.DoublePrecision.HIGH) {
                if (exp > 0 && exp < 300) return whole * Math.pow(10,
                                                                  exp
                                                                 );
                else if (exp > -300 && exp < 0) return whole / Math.pow(10,
                                                                        exp
                                                                       );
            }
        }
        else {
            if (exp == 0) return whole + fraction;
            else if (exp > 0 && exp < POW_10.length) return fraction * POW_10[exp - 1] + whole * POW_10[exp - 1];
            else if (exp < 0 && -exp < POW_10.length) return fraction / POW_10[-exp - 1] + whole / POW_10[-exp - 1];
            else if (reader.doublePrecision != JsonReader.DoublePrecision.HIGH) {
                if (exp > 0 && exp < 300) return whole * Math.pow(10,
                                                                  exp
                                                                 );
                else if (exp > -300 && exp < 0) return whole / Math.pow(10,
                                                                        exp
                                                                       );
            }
        }
        return parseDoubleGeneric(reader.prepareBuffer(start + offset,
                                                       end - start - offset
                                                      ),
                                  end - start - offset,
                                  reader,
                                  false
                                 );
    }

    private static double parseDoubleGeneric(final char[] buf,
                                             final int len,
                                             final JsonReader reader,
                                             final boolean withQuotes) throws IOException {
        int end = len;
        while (end > 0 && Character.isWhitespace(buf[end - 1])) {
            end--;
        }
        if (end > reader.maxNumberDigits) {
            throw reader.newParseErrorWith("Too many digits detected in number",
                                           len,
                                           "",
                                           "Too many digits detected in number",
                                           end,
                                           ""
                                          );
        }
        final int offset = buf[0] == '-' ? 1 : 0;
        if (buf[offset] == '0' && end > offset + 1 && buf[offset + 1] >= '0' && buf[offset + 1] <= '9') {
            throw reader.newParseErrorAt("Leading zero is not allowed. Error parsing number",
                                         len + (withQuotes ? 2 : 0)
                                        );
        }
        try {
            return Double.parseDouble(new String(buf,
                                                 0,
                                                 end
            ));
        } catch (NumberFormatException nfe) {
            throw reader.newParseErrorAt("Error parsing number",
                                         len + (withQuotes ? 2 : 0),
                                         nfe
                                        );
        }
    }

    private static final byte MINUS = '-';
    private static final byte[] MIN_INT = "-2147483648".getBytes(StandardCharsets.UTF_8);

    public static void serialize(final int value,
                                 final JsonWriter sw) {
        final byte[] buf      = sw.ensureCapacity(11);
        final int    position = sw.size();
        int current = serialize(buf,
                                position,
                                value
                               );
        sw.advance(current - position);
    }

    private static int serialize(final byte[] buf,
                                 int pos,
                                 final int value) {
        int i;
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                for (int x = 0; x < MIN_INT.length; x++) {
                    buf[pos + x] = MIN_INT[x];
                }
                return pos + MIN_INT.length;
            }
            i = -value;
            buf[pos++] = MINUS;
        }
        else {
            i = value;
        }
        final int q1 = i / 1000;
        if (q1 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[i],
                                 pos
                                );
            return pos;
        }
        final int r1 = i - q1 * 1000;
        final int q2 = q1 / 1000;
        if (q2 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[q1];
            int off = writeFirstBuf(buf,
                                    v2,
                                    pos
                                   );
            writeBuf(buf,
                     v1,
                     pos + off
                    );
            return pos + 3 + off;
        }
        final int r2 = q1 - q2 * 1000;
        final int q3 = q2 / 1000;
        final int v1 = DIGITS[r1];
        final int v2 = DIGITS[r2];
        if (q3 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[q2],
                                 pos
                                );
        }
        else {
            final int r3 = q2 - q3 * 1000;
            buf[pos++] = (byte) (q3 + '0');
            writeBuf(buf,
                     DIGITS[r3],
                     pos
                    );
            pos += 3;
        }
        writeBuf(buf,
                 v2,
                 pos
                );
        writeBuf(buf,
                 v1,
                 pos + 3
                );
        return pos + 6;
    }


    public static int deserializeInt(final JsonReader reader) throws IOException {
        final int    start = reader.scanNumber();
        final int    end   = reader.getCurrentIndex();
        final byte[] buf   = reader.buffer;
        final byte   ch    = buf[start];
        if (ch == '-') {
            if (end > start + 2 && buf[start + 1] == '0' && buf[start + 2] >= '0' && buf[start + 2] <= '9') {
                numberException(reader,
                                start,
                                end,
                                "Leading zero is not allowed"
                               );
            }
            return parseNegativeInt(buf,
                                    reader,
                                    start,
                                    end
                                   );
        }
        else {
            if (ch == '0' && end > start + 1 && buf[start + 1] >= '0' && buf[start + 1] <= '9') {
                numberException(reader,
                                start,
                                end,
                                "Leading zero is not allowed"
                               );
            }
            return parsePositiveInt(buf,
                                    reader,
                                    start,
                                    end,
                                    0
                                   );
        }
    }

    private static int parsePositiveInt(final byte[] buf,
                                        final JsonReader reader,
                                        final int start,
                                        final int end,
                                        final int offset) throws IOException {
        int value = 0;
        int i     = start + offset;
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      "Digit not found"
                                     );
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (i > start + offset && reader.allWhitespace(i,
                                                               end
                                                              )) return value;
                else if (i == end - 1 && buf[i] == '.') numberException(reader,
                                                                        start,
                                                                        end,
                                                                        "Number ends with a dot"
                                                                       );
                final BigDecimal v = parseNumberGeneric(reader.prepareBuffer(start,
                                                                             end - start
                                                                            ),
                                                        end - start,
                                                        reader,
                                                        false
                                                       );
                if (v.scale() > 0) numberException(reader,
                                                   start,
                                                   end,
                                                   "Expecting int but found decimal value",
                                                   v
                                                  );
                return v.intValue();

            }
            value = (value << 3) + (value << 1) + ind;
            if (value < 0) {
                numberException(reader,
                                start,
                                end,
                                "Integer overflow detected"
                               );
            }
        }
        return value;
    }

    private static int parseNegativeInt(final byte[] buf,
                                        final JsonReader reader,
                                        final int start,
                                        final int end) throws IOException {
        int value = 0;
        int i     = start + 1;
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      "Digit not found"
                                     );
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                                                         )) return value;
                else if (i == end - 1 && buf[i] == '.') numberException(reader,
                                                                        start,
                                                                        end,
                                                                        "Number ends with a dot"
                                                                       );
                final BigDecimal v = parseNumberGeneric(reader.prepareBuffer(start,
                                                                             end - start
                                                                            ),
                                                        end - start,
                                                        reader,
                                                        false
                                                       );
                if (v.scale() > 0) numberException(reader,
                                                   start,
                                                   end,
                                                   "Expecting int but found decimal value",
                                                   v
                                                  );
                return v.intValue();
            }
            value = (value << 3) + (value << 1) - ind;
            if (value > 0) {
                numberException(reader,
                                start,
                                end,
                                "Integer overflow detected"
                               );
            }
        }
        return value;
    }


    private static int writeFirstBuf(final byte[] buf,
                                     final int v,
                                     int pos) {
        final int start = v >> 24;
        if (start == 0) {
            buf[pos++] = (byte) (v >> 16);
            buf[pos++] = (byte) (v >> 8);
        }
        else if (start == 1) {
            buf[pos++] = (byte) (v >> 8);
        }
        buf[pos] = (byte) v;
        return 3 - start;
    }

    private static void writeBuf(final byte[] buf,
                                 final int v,
                                 int pos) {
        buf[pos] = (byte) (v >> 16);
        buf[pos + 1] = (byte) (v >> 8);
        buf[pos + 2] = (byte) v;
    }

    private static final byte[] MIN_LONG = "-9223372036854775808".getBytes(StandardCharsets.UTF_8);

    public static void serialize(final long value,
                                 final JsonWriter sw) {
        final byte[] buf      = sw.ensureCapacity(21);
        final int    position = sw.size();
        int current = serialize(buf,
                                position,
                                value
                               );
        sw.advance(current - position);
    }

    private static int serialize(final byte[] buf,
                                 int pos,
                                 final long value) {
        long i;
        if (value < 0) {
            if (value == Long.MIN_VALUE) {
                for (int x = 0; x < MIN_LONG.length; x++) {
                    buf[pos + x] = MIN_LONG[x];
                }
                return pos + MIN_LONG.length;
            }
            i = -value;
            buf[pos++] = MINUS;
        }
        else {
            i = value;
        }
        final long q1 = i / 1000;
        if (q1 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[(int) i],
                                 pos
                                );
            return pos;
        }
        final int  r1 = (int) (i - q1 * 1000);
        final long q2 = q1 / 1000;
        if (q2 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[(int) q1];
            int off = writeFirstBuf(buf,
                                    v2,
                                    pos
                                   );
            writeBuf(buf,
                     v1,
                     pos + off
                    );
            return pos + 3 + off;
        }
        final int  r2 = (int) (q1 - q2 * 1000);
        final long q3 = q2 / 1000;
        if (q3 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[(int) q2];
            pos += writeFirstBuf(buf,
                                 v3,
                                 pos
                                );
            writeBuf(buf,
                     v2,
                     pos
                    );
            writeBuf(buf,
                     v1,
                     pos + 3
                    );
            return pos + 6;
        }
        final int r3 = (int) (q2 - q3 * 1000);
        final int q4 = (int) (q3 / 1000);
        if (q4 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[r3];
            final int v4 = DIGITS[(int) q3];
            pos += writeFirstBuf(buf,
                                 v4,
                                 pos
                                );
            writeBuf(buf,
                     v3,
                     pos
                    );
            writeBuf(buf,
                     v2,
                     pos + 3
                    );
            writeBuf(buf,
                     v1,
                     pos + 6
                    );
            return pos + 9;
        }
        final int r4 = (int) (q3 - q4 * 1000L);
        final int q5 = q4 / 1000;
        if (q5 == 0) {
            final int v1 = DIGITS[r1];
            final int v2 = DIGITS[r2];
            final int v3 = DIGITS[r3];
            final int v4 = DIGITS[r4];
            final int v5 = DIGITS[q4];
            pos += writeFirstBuf(buf,
                                 v5,
                                 pos
                                );
            writeBuf(buf,
                     v4,
                     pos
                    );
            writeBuf(buf,
                     v3,
                     pos + 3
                    );
            writeBuf(buf,
                     v2,
                     pos + 6
                    );
            writeBuf(buf,
                     v1,
                     pos + 9
                    );
            return pos + 12;
        }
        final int r5 = q4 - q5 * 1000;
        final int q6 = q5 / 1000;
        final int v1 = DIGITS[r1];
        final int v2 = DIGITS[r2];
        final int v3 = DIGITS[r3];
        final int v4 = DIGITS[r4];
        final int v5 = DIGITS[r5];
        if (q6 == 0) {
            pos += writeFirstBuf(buf,
                                 DIGITS[q5],
                                 pos
                                );
        }
        else {
            final int r6 = q5 - q6 * 1000;
            buf[pos++] = (byte) (q6 + '0');
            writeBuf(buf,
                     DIGITS[r6],
                     pos
                    );
            pos += 3;
        }
        writeBuf(buf,
                 v5,
                 pos
                );
        writeBuf(buf,
                 v4,
                 pos + 3
                );
        writeBuf(buf,
                 v3,
                 pos + 6
                );
        writeBuf(buf,
                 v2,
                 pos + 9
                );
        writeBuf(buf,
                 v1,
                 pos + 12
                );
        return pos + 15;
    }


    public static long deserializeLong(final JsonReader reader) throws IOException {
        final int    start = reader.scanNumber();
        final int    end   = reader.getCurrentIndex();
        final byte[] buf   = reader.buffer;
        final byte   ch    = buf[start];
        int          i     = start;
        long         value = 0;
        if (ch == '-') {
            i = start + 1;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Digit not found"
                                         );
            final boolean leadingZero = buf[i] == 48;
            for (; i < end; i++) {
                final int ind = buf[i] - 48;
                if (ind < 0 || ind > 9) {
                    if (leadingZero && i > start + 2) {
                        numberException(reader,
                                        start,
                                        end,
                                        "Leading zero is not allowed"
                                       );
                    }
                    if (i > start + 1 && reader.allWhitespace(i,
                                                              end
                                                             )) return value;
                    return parseLongGeneric(reader,
                                            start,
                                            end
                                           );
                }
                value = (value << 3) + (value << 1) - ind;
                if (value > 0) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Long overflow detected"
                                   );
                }
            }
            if (leadingZero && i > start + 2) {
                numberException(reader,
                                start,
                                end,
                                "Leading zero is not allowed"
                               );
            }
            return value;
        }
        if (i == end) numberException(reader,
                                      start,
                                      end,
                                      "Digit not found"
                                     );
        final boolean leadingZero = buf[i] == 48;
        for (; i < end; i++) {
            final int ind = buf[i] - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (ch == '+' && i > start + 1 && reader.allWhitespace(i,
                                                                       end
                                                                      )) return value;
                else if (ch != '+' && i > start && reader.allWhitespace(i,
                                                                        end
                                                                       )) return value;
                return parseLongGeneric(reader,
                                        start,
                                        end
                                       );
            }
            value = (value << 3) + (value << 1) + ind;
            if (value < 0) {
                numberException(reader,
                                start,
                                end,
                                "Long overflow detected"
                               );
            }
        }
        if (leadingZero && i > start + 1) {
            numberException(reader,
                            start,
                            end,
                            "Leading zero is not allowed"
                           );
        }
        return value;
    }

    private static long parseLongGeneric(final JsonReader reader,
                                         final int start,
                                         final int end) throws IOException {
        final int len = end - start;
        final char[] buf = reader.prepareBuffer(start,
                                                len
                                               );
        if (len > 0 && buf[len - 1] == '.') numberException(reader,
                                                            start,
                                                            end,
                                                            "Number ends with a dot"
                                                           );
        final BigDecimal v = parseNumberGeneric(buf,
                                                len,
                                                reader,
                                                false
                                               );
        if (v.scale() > 0) numberException(reader,
                                           start,
                                           end,
                                           "Expecting long, but found decimal value ",
                                           v
                                          );
        return v.longValue();
    }


    public static void serialize(final BigDecimal value,
                                 final JsonWriter sw) {
        sw.writeAscii(value.toString());
    }

    public static BigDecimal deserializeDecimal(final JsonReader reader) throws IOException {
        final int start = reader.scanNumber();
        int       end   = reader.getCurrentIndex();
        if (end == reader.length()) {
            MyNumberConverter.NumberInfo info = readLongNumber(reader,
                                                               start
                                                              );
            return parseNumberGeneric(info.buffer,
                                      info.length,
                                      reader,
                                      false
                                     );
        }
        int len = end - start;
        if (len > 18) {
            return parseNumberGeneric(reader.prepareBuffer(start,
                                                           len
                                                          ),
                                      len,
                                      reader,
                                      false
                                     );
        }
        final byte[] buf = reader.buffer;
        final byte   ch  = buf[start];
        if (ch == '-') {
            return parseNegativeDecimal(buf,
                                        reader,
                                        start,
                                        end
                                       );
        }
        return parsePositiveDecimal(buf,
                                    reader,
                                    start,
                                    end
                                   );
    }

    private static BigDecimal parsePositiveDecimal(final byte[] buf,
                                                   final JsonReader reader,
                                                   final int start,
                                                   final int end) throws IOException {
        long          value       = 0;
        byte          ch          = ' ';
        int           i           = start;
        final boolean leadingZero = buf[start] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (i > start && reader.allWhitespace(i,
                                                      end
                                                     )) return BigDecimal.valueOf(value);
                numberException(reader,
                                start,
                                end,
                                "Unknown digit",
                                (char) ch
                               );
            }
            value = (value << 3) + (value << 1) + ind;
        }
        if (i == start) numberException(reader,
                                        start,
                                        end,
                                        "Digit not found"
                                       );
        else if (leadingZero && ch != '.' && i > start + 1) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            "Leading zero is not allowed"
                                                                           );
        else if (i == end) return BigDecimal.valueOf(value);
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Number ends with a dot"
                                         );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) return BigDecimal.valueOf(value,
                                                                         i - dp
                                                                        );
                    numberException(reader,
                                    start,
                                    end,
                                    "Unknown digit",
                                    (char) ch
                                   );
                }
                value = (value << 3) + (value << 1) + ind;
            }
            if (i == end) return BigDecimal.valueOf(value,
                                                    end - dp
                                                   );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                                          );
                }
                else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                                          );
                }
                else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                                          );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp
                                         );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
                                     );
        }
        else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                                      );
            }
            else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                                      );
            }
            else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                                      );
            }
            return BigDecimal.valueOf(value,
                                      -exp
                                     );
        }
        return BigDecimal.valueOf(value);
    }

    private static BigDecimal parseNegativeDecimal(final byte[] buf,
                                                   final JsonReader reader,
                                                   final int start,
                                                   final int end) throws IOException {
        long          value       = 0;
        byte          ch          = ' ';
        int           i           = start + 1;
        final boolean leadingZero = buf[start + 1] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 2) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                                                         )) return BigDecimal.valueOf(value);
                numberException(reader,
                                start,
                                end,
                                "Unknown digit",
                                (char) ch
                               );
            }
            value = (value << 3) + (value << 1) - ind;
        }
        if (i == start + 1) numberException(reader,
                                            start,
                                            end,
                                            "Digit not found"
                                           );
        else if (leadingZero && ch != '.' && i > start + 2) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            "Leading zero is not allowed"
                                                                           );
        else if (i == end) return BigDecimal.valueOf(value);
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Number ends with a dot"
                                         );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) return BigDecimal.valueOf(value,
                                                                         i - dp
                                                                        );
                    numberException(reader,
                                    start,
                                    end,
                                    "Unknown digit",
                                    (char) ch
                                   );
                }
                value = (value << 3) + (value << 1) - ind;
            }
            if (i == end) return BigDecimal.valueOf(value,
                                                    end - dp
                                                   );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                                          );
                }
                else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                                          );
                }
                else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                                          );
                }
                return BigDecimal.valueOf(value,
                                          ep - dp - exp
                                         );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
                                     );
        }
        else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                                      );
            }
            else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                                      );
            }
            else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                                      );
            }
            return BigDecimal.valueOf(value,
                                      -exp
                                     );
        }
        return BigDecimal.valueOf(value);
    }

    private static final BigDecimal BD_MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal BD_MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);

    private static Number bigDecimalOrDouble(BigDecimal num,
                                             JsonReader.UnknownNumberParsing unknownNumbers) {
        return unknownNumbers == JsonReader.UnknownNumberParsing.LONG_AND_BIGDECIMAL
               ? num
               : num.doubleValue();
    }

    private static Number tryLongFromBigDecimal(final char[] buf,
                                                final int len,
                                                JsonReader reader) throws IOException {
        final BigDecimal num = parseNumberGeneric(buf,
                                                  len,
                                                  reader,
                                                  false
                                                 );
        if (num.scale() == 0 && num.precision() <= 19) {
            if (num.signum() == 1) {
                if (num.compareTo(BD_MAX_LONG) <= 0) {
                    return num.longValue();
                }
            }
            else if (num.compareTo(BD_MIN_LONG) >= 0) {
                return num.longValue();
            }
        }
        return bigDecimalOrDouble(num,
                                  reader.unknownNumbers
                                 );
    }

    public static Number deserializeNumber(final JsonReader reader) throws IOException {
        if (reader.unknownNumbers == JsonReader.UnknownNumberParsing.BIGDECIMAL) return deserializeDecimal(reader);
        else if (reader.unknownNumbers == JsonReader.UnknownNumberParsing.DOUBLE) return deserializeDouble(reader);
        final int start = reader.scanNumber();
        int       end   = reader.getCurrentIndex();
        if (end == reader.length()) {
            MyNumberConverter.NumberInfo info = readLongNumber(reader,
                                                               start
                                                              );
            return tryLongFromBigDecimal(info.buffer,
                                         info.length,
                                         reader
                                        );
        }
        int len = end - start;
        if (len > 18) {
            return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                              len
                                                             ),
                                         len,
                                         reader
                                        );
        }
        final byte[] buf = reader.buffer;
        final byte   ch  = buf[start];
        if (ch == '-') {
            return parseNegativeNumber(buf,
                                       reader,
                                       start,
                                       end
                                      );
        }
        return parsePositiveNumber(buf,
                                   reader,
                                   start,
                                   end
                                  );
    }

    private static Number parsePositiveNumber(final byte[] buf,
                                              final JsonReader reader,
                                              final int start,
                                              final int end) throws IOException {
        long          value       = 0;
        byte          ch          = ' ';
        int           i           = start;
        final boolean leadingZero = buf[start] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 1) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (i > start && reader.allWhitespace(i,
                                                      end
                                                     )) return value;
                return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                  end - start
                                                                 ),
                                             end - start,
                                             reader
                                            );
            }
            value = (value << 3) + (value << 1) + ind;
        }
        if (i == start) numberException(reader,
                                        start,
                                        end,
                                        "Digit not found"
                                       );
        else if (leadingZero && ch != '.' && i > start + 1) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            "Leading zero is not allowed"
                                                                           );
        else if (i == end) return value;
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Number ends with a dot"
                                         );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) return BigDecimal.valueOf(value,
                                                                         i - dp
                                                                        );
                    return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                      end - start
                                                                     ),
                                                 end - start,
                                                 reader
                                                );
                }
                value = (value << 3) + (value << 1) + ind;
            }
            if (i == end) return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                                       end - dp
                                                                      ),
                                                    reader.unknownNumbers
                                                   );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                                          );
                }
                else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                                          );
                }
                else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                                          );
                }
                return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                             ep - dp - exp
                                                            ),
                                          reader.unknownNumbers
                                         );
            }
            return BigDecimal.valueOf(value,
                                      end - dp
                                     );
        }
        else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                                      );
            }
            else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                                      );
            }
            else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                                      );
            }
            return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                         -exp
                                                        ),
                                      reader.unknownNumbers
                                     );
        }
        return bigDecimalOrDouble(BigDecimal.valueOf(value),
                                  reader.unknownNumbers
                                 );
    }

    private static Number parseNegativeNumber(final byte[] buf,
                                              final JsonReader reader,
                                              final int start,
                                              final int end) throws IOException {
        long          value       = 0;
        byte          ch          = ' ';
        int           i           = start + 1;
        final boolean leadingZero = buf[start + 1] == 48;
        for (; i < end; i++) {
            ch = buf[i];
            if (ch == '.' || ch == 'e' || ch == 'E') break;
            final int ind = ch - 48;
            if (ind < 0 || ind > 9) {
                if (leadingZero && i > start + 2) {
                    numberException(reader,
                                    start,
                                    end,
                                    "Leading zero is not allowed"
                                   );
                }
                if (i > start + 1 && reader.allWhitespace(i,
                                                          end
                                                         )) return value;
                return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                  end - start
                                                                 ),
                                             end - start,
                                             reader
                                            );
            }
            value = (value << 3) + (value << 1) - ind;
        }
        if (i == start + 1) numberException(reader,
                                            start,
                                            end,
                                            "Digit not found"
                                           );
        else if (leadingZero && ch != '.' && i > start + 2) numberException(reader,
                                                                            start,
                                                                            end,
                                                                            "Leading zero is not allowed"
                                                                           );
        else if (i == end) return value;
        else if (ch == '.') {
            i++;
            if (i == end) numberException(reader,
                                          start,
                                          end,
                                          "Number ends with a dot"
                                         );
            int dp = i;
            for (; i < end; i++) {
                ch = buf[i];
                if (ch == 'e' || ch == 'E') break;
                final int ind = ch - 48;
                if (ind < 0 || ind > 9) {
                    if (reader.allWhitespace(i,
                                             end
                                            )) return BigDecimal.valueOf(value,
                                                                         i - dp
                                                                        );
                    return tryLongFromBigDecimal(reader.prepareBuffer(start,
                                                                      end - start
                                                                     ),
                                                 end - start,
                                                 reader
                                                );
                }
                value = (value << 3) + (value << 1) - ind;
            }
            if (i == end) return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                                       end - dp
                                                                      ),
                                                    reader.unknownNumbers
                                                   );
            else if (ch == 'e' || ch == 'E') {
                final int ep = i;
                i++;
                ch = buf[i];
                final int exp;
                if (ch == '-') {
                    exp = parseNegativeInt(buf,
                                           reader,
                                           i,
                                           end
                                          );
                }
                else if (ch == '+') {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           1
                                          );
                }
                else {
                    exp = parsePositiveInt(buf,
                                           reader,
                                           i,
                                           end,
                                           0
                                          );
                }
                return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                             ep - dp - exp
                                                            ),
                                          reader.unknownNumbers
                                         );
            }
            return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                         end - dp
                                                        ),
                                      reader.unknownNumbers
                                     );
        }
        else if (ch == 'e' || ch == 'E') {
            i++;
            ch = buf[i];
            final int exp;
            if (ch == '-') {
                exp = parseNegativeInt(buf,
                                       reader,
                                       i,
                                       end
                                      );
            }
            else if (ch == '+') {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       1
                                      );
            }
            else {
                exp = parsePositiveInt(buf,
                                       reader,
                                       i,
                                       end,
                                       0
                                      );
            }
            return bigDecimalOrDouble(BigDecimal.valueOf(value,
                                                         -exp
                                                        ),
                                      reader.unknownNumbers
                                     );
        }
        return bigDecimalOrDouble(BigDecimal.valueOf(value),
                                  reader.unknownNumbers
                                 );
    }


}

