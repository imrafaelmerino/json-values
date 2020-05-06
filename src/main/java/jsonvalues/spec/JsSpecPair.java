package jsonvalues.spec;

import static java.util.Objects.requireNonNull;


  public final class JsSpecPair
  {
    public final String key;
    public final JsSpec spec;

    public static JsSpecPair of(final String key,
                                final JsSpec spec
                               )
    {
      return new JsSpecPair(requireNonNull(key),
                            requireNonNull(spec)
      );
    }

    private JsSpecPair(final String key,
                       final JsSpec spec
                      )
    {
      this.key = key;
      this.spec = spec;
    }

  }
