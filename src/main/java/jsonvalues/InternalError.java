package jsonvalues;

import java.io.IOError;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 Exception that models an internal error made by a developer. An issue in GitHub should be open reporting
 the exception message.
 */
public final class InternalError extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private static final String GENERAL_MESSAGE = "Create an issue in https://github.com/imrafaelmerino/values: %s.";

    private InternalError(final String message)
    {
        super(String.format(GENERAL_MESSAGE,
                            message
                           ));
    }

    private InternalError(final Exception e)
    {
        super(e);
    }

    static InternalError unexpectedErrorSerializingAJsonIntoString(IOException e){
        return new InternalError(e);
    }

    static InternalError arrayOptionNotImplemented(final String option)
    {
        return new InternalError(String.format("New option %s in enum JsArray.TYPE not implemented.",
                                               option
                                              ));
    }

    static InternalError encodingNotSupported(final UnsupportedEncodingException e)
    {
        return new InternalError(e);
    }

    static InternalError notEmptyMapWithoutAKey()
    {
        return new InternalError("A calculation on a non empty map was expected to return a result.");
    }

    static InternalError opNotSupportedForArrays()
    {
        return new InternalError("A JsArray doesn't have keys. Don't call this method.");
    }

    static InternalError keyNotFound(final String key)
    {
        return new InternalError(String.format("key %s not found in map. Use contains(key) before or the method getOptional(key).",
                                               key
                                              ));
    }

    static InternalError patchOperationNotSupported(final String op)
    {
        return new InternalError(String.format("Patch operation '%s' not supported.",
                                               op
                                              ));
    }

    static InternalError tokenNotExpected(String token)
    {
        return new InternalError(String.format("token %s not expected during parsing",
                                               token
                                              ));
    }

    static InternalError tryComputationWithNoResult(String message)
    {
        return new InternalError(message);
    }

    static InternalError tryPatchComputationWithNoResult()
    {
        return new InternalError("TryPatch.orElseThrow() with nor result nor error");
    }

    static InternalError tryPatchSuccesfullWithNoResult()
    {
        return new InternalError("TryPatch: isSuccess is true but result is null");
    }
}
