package jsonvalues;

/**
 Exception that models a programming error made by the user. The user has a bug in their code and something
 has to be fixed. Part of the exception message is a suggestion to fix the bug.
 */
public final class UserError extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String GENERAL_MESSAGE = "%s. Suggestion: %s.";

    private UserError(final String message) {
        super(message);
    }


    static UserError indexWithLeadingZeros(final String token) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("index %s with leading zeros",
                                                         token
                                                        ),
                                           "removes the leading zeros"
                                          ));
    }

    static UserError isNotAJsBool(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBool expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBool() before invoking asJsBool()"
                                          ));
    }

    static UserError isNotAJsInt(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isInt() before invoking asJsInt()"
                                          ));
    }

    static UserError isNotAJsInstant(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsInstant expected or a JsString representing an instant formatted in ISO-8601, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isInstant() before invoking asJsInstant()"
                                          ));
    }

    static UserError isNotAJsBinary(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBinary expected or a JsString representing an array of bytes encoded in base64, " +
                                                                 "but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBinary() before invoking toJsBinary()"
                                          ));
    }

    static UserError isNotAJsLong(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsLong expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isLong() or isInt() before invoking toJsLong()"
                                          ));
    }

    static UserError isNotAJsDouble(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsDouble expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isDouble() or isDecimal() before invoking toJsDouble()"
                                          ));
    }

    static UserError isNotAJsBigInt(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigInt expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigInt() or isIntegral() before invoking toJsBigInt()"
                                          ));
    }

    static UserError isNotAJsBigDec(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsBigDec expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isBigDec() or isDecimal() before invoking toJsBigDec()"
                                          ));
    }

    static UserError isNotAJsArray(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsArray expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isArray() before invoking toJsArray()"
                                          ));
    }

    static UserError isNotAJsPrimitive(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsPrimitive expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isJsPrimitive() before invoking toJsPrimitive()"
                                          ));
    }

    static UserError isNotAJsNumber(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsNumber expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isNumber() before invoking toJsArray()"
                                          ));
    }

    static UserError isNotAJsObj(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsObj expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isObj() before invoking toJsObj()"
                                          ));
    }

    static UserError isNotAJson(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("Json expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isJson() or isArray() or isObj() before invoking toJson()"
                                          ));
    }

    static UserError isNotAJsString(final JsValue elem) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("JsStr expected, but %s was found",
                                                         elem.getClass()
                                                        ),
                                           "call the guard condition isStr() before invoking toJsStr()"
                                          ));
    }

    static UserError lastOfEmptyPath() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "last() of empty path",
                                           "call the guard condition isEmpty() before invoking last()"
                                          ));
    }

    static UserError headOfEmptyPath() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "head() of empty path",
                                           "call the guard condition isEmpty() before invoking head()"
                                          ));
    }


    static UserError pathMalformed(final String path) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           String.format("malformed path: %s",
                                                         path
                                                        ),
                                           "Go to https://imrafaelmerino.github.io/json-values/#jspath"
                                          ));
    }

    static UserError tailOfEmptyPath() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "tail() of empty path",
                                           "call the guard condition isEmpty() before invoking tail()"
                                          ));
    }

    static UserError initOfEmptyPath() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "init() of empty path",
                                           "call the guard condition isEmpty() before invoking init()"
                                          ));
    }


    static UserError asKeyOfIndex() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "asKey() of index",
                                           "use the guard condition position.isKey() before"
                                          ));
    }

    static UserError asIndexOfKey() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "asIndex() of key",
                                           "use the guard condition position.isIndex() before"
                                          ));
    }

    static UserError incOfKey() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "inc() of key",
                                           "use the guard condition last().isIndex() before invoking inc()"
                                          ));
    }

    static UserError decOfKey() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "dec() of key",
                                           "use the guard condition last().isIndex() before invoking dec()"
                                          ));
    }

    static UserError trampolineNotCompleted() {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "trampoline not completed",
                                           "Before calling the method get() on a trampoline, make sure a Trampoline.done() status is returned"
                                          ));
    }


    public static UserError pathHeadIsNotAnIndex(JsPath path) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "Path head is not an index: " + path,
                                           "Correct the path"
                                          ));
    }

    public static UserError pathHeadIsNotAKey(JsPath path) {
        return new UserError(String.format(GENERAL_MESSAGE,
                                           "Path head is not a key: " + path,
                                           "Correct the path"
                                          ));
    }
}
