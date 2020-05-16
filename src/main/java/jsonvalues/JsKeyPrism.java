package jsonvalues;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class JsKeyPrism {
  public final Function<Position, Optional<String>> getOptional;
  public final Function<String, Position> reverseGet;

  JsKeyPrism() {
    this.getOptional = pos -> pos.isKey() ? Optional.of(pos.asKey().name) : Optional.empty();
    this.reverseGet = Key::of;
  }

  public final Function<Position, Position> modify(Function<String, String> f) {
    return v ->
    {
      final Optional<String> opt = getOptional.apply(v);
      if (opt.isPresent()) return reverseGet.apply(f.apply(opt.get()));
      else return v;
    };
  }


  public final Function<Position, Optional<Position>> modifyOptional(Function<String, String> f) {
    return v ->
    {
      final Optional<String> opt = getOptional.apply(v);
      return opt.map(t -> reverseGet.apply(f.apply(t)));
    };
  }

  public final boolean isEmpty(Position value) {
    return !getOptional.apply(value)
      .isPresent();
  }


  public final boolean nonEmpty(Position value) {
    return getOptional.apply(value)
      .isPresent();
  }

  public final Function<Position, Optional<String>> find(Predicate<String> predicate) {
    return v -> getOptional.apply(v)
      .filter(predicate);
  }


  public final Predicate<Position> exists(Predicate<String> predicate) {
    return v -> getOptional.apply(v)
      .filter(predicate)
      .isPresent();
  }

  /**
   * check if there is no target or the target satisfies the predicate
   */

  public final Predicate<Position> all(Predicate<String> predicate) {
    return v ->
    {
      final Optional<String> value = getOptional.apply(v);
      return value.map(predicate::test)
        .orElse(true);
    };
  }


}
