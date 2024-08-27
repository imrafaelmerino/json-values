package jsonvalues.spec;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

final class MyPrettifyOutputStream extends OutputStream {

  private static final int INDENT_CACHE_SIZE = 257;

  private static final boolean[] WHITESPACE = new boolean[256];

  static {
    WHITESPACE[9] = true;
    WHITESPACE[10] = true;
    WHITESPACE[13] = true;
    WHITESPACE[32] = true;
  }

  private final OutputStream out;
  private final IndentType indentType;
  private final int indentLength;

  private int currentIndent = 0;
  private boolean inString = false;
  private boolean inEscape = false;
  private boolean beginObjectOrList = false;

  MyPrettifyOutputStream(OutputStream out,
                         IndentType indentType,
                         int indentLength
                        ) {
    if (indentLength < 1) {
      throw new IllegalArgumentException("'indentLength' must be >= 1");
    }
    this.out = out;
    this.indentType = indentType;
    this.indentLength = indentLength;
  }

  @Override
  public void write(final int b) throws JsSerializerException {
    try {
      if (inString) {
        if (b == '"' && !inEscape) {
          inString = false;
        } else {
          inEscape = !inEscape && b == '\\';
        }
        out.write(b);
      } else if (b == '"') {
        inString = true;
        if (beginObjectOrList) {
          writeNewLineWithIndent();
          beginObjectOrList = false;
        }
        out.write(b);
      } else if (b == ',') {
        out.write(',');
        writeNewLineWithIndent();
      } else if (b == ':') {
        out.write(':');
        out.write(' ');
      } else if (b == '{' || b == '[') {
        if (beginObjectOrList) {
          writeNewLineWithIndent();
        }
        beginObjectOrList = true;
        currentIndent += indentLength;
        out.write(b);
      } else if (b == '}' || b == ']') {
        currentIndent -= indentLength;
        if (beginObjectOrList) {
          beginObjectOrList = false;
        } else {
          writeNewLineWithIndent();
        }
        out.write(b);
      } else if (!WHITESPACE[b]) {
        if (beginObjectOrList) {
          writeNewLineWithIndent();
          beginObjectOrList = false;
        }
        out.write(b);
      }
    } catch (IOException e) {
      throw new JsSerializerException("Exception prettifying a Json",
                                      e);
    }
  }

  @Override
  public void write(final byte[] bytes,
                    final int off,
                    final int len
                   ) throws JsParserException {
    try {
      int start = off;

      for (int i = off; i < off + len; i++) {
        int b = bytes[i];

        if (inString) {
          if (b == '"' && !inEscape) {
            inString = false;
          } else {
            inEscape = !inEscape && b == '\\';
          }
        } else if (b == '"') {
          inString = true;
          if (beginObjectOrList) {
            writeNewLineWithIndent();
            beginObjectOrList = false;
          }
        } else if (b == ',') {
          out.write(bytes,
                    start,
                    i - start + 1
                   );
          start = i + 1;
          writeNewLineWithIndent();
        } else if (b == ':') {
          out.write(bytes,
                    start,
                    i - start + 1
                   );
          start = i + 1;
          out.write(' ');
        } else if (b == '{' || b == '[') {
          if (beginObjectOrList) {
            writeNewLineWithIndent();
          }
          beginObjectOrList = true;
          currentIndent += indentLength;
          out.write(bytes,
                    start,
                    i - start + 1
                   );
          start = i + 1;
        } else if (b == '}' || b == ']') {
          currentIndent -= indentLength;
          out.write(bytes,
                    start,
                    i - start
                   );
          if (beginObjectOrList) {
            beginObjectOrList = false;
          } else {
            writeNewLineWithIndent();
          }
          out.write(b);
          start = i + 1;
        } else if (WHITESPACE[b]) {
          out.write(bytes,
                    start,
                    i - start
                   );
          start = i + 1;
        } else if (beginObjectOrList) {
          writeNewLineWithIndent();
          beginObjectOrList = false;
        }
      }

      int remaining = off + len - start;
      if (remaining > 0) {
        out.write(bytes,
                  start,
                  remaining
                 );
      }
    } catch (IOException e) {
      throw new JsSerializerException("Exception prettifying a Json",
                                      e);
    }
  }

  private void writeNewLineWithIndent() throws IOException {
    final int size = currentIndent + 1;
    if (size < INDENT_CACHE_SIZE) {
      out.write(indentType.cache,
                0,
                size
               );
    } else {
      final byte[] cache = indentType.cache;
      out.write(cache);
      int remaining = size - INDENT_CACHE_SIZE;
      while (true) {
        if (remaining < INDENT_CACHE_SIZE) {
          out.write(cache,
                    1,
                    remaining
                   );
          break;
        } else {
          out.write(cache,
                    1,
                    INDENT_CACHE_SIZE - 1
                   );
          remaining -= INDENT_CACHE_SIZE - 1;
        }
      }
    }
  }

  @SuppressWarnings("ImmutableEnumChecker")
  public enum IndentType {
    SPACES((byte) ' '),
    TABS((byte) '\t');

    private final byte[] cache;

    IndentType(byte b) {
      cache = new byte[INDENT_CACHE_SIZE];
      cache[0] = '\n';
      Arrays.fill(cache,
                  1,
                  cache.length,
                  b
                 );
    }
  }
}

