package jsonvalues.specs;


import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;

import java.util.Set;


class Fun {

    private Fun() {
    }

    public static void assertErrorIs(final Set<SpecError> error,
                                     final ERROR_CODE code,
                                     final JsValue value,
                                     final JsPath path
    ) {
        final SpecError pair = error.stream()
                                    .findFirst()
                                    .get();

        Assertions.assertEquals(pair.errorCode,
                                code
        );

        Assertions.assertEquals(pair.value,
                                value
        );

        Assertions.assertEquals(pair.path,
                                path
        );
    }


}
