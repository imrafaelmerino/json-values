package jsonvalues;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class JsIndexPrism {
  public final Function<Position, Optional<Integer>> getOptional;
  public final Function<Integer, Position> reverseGet;

  JsIndexPrism() {
    this.getOptional = pos -> pos.isIndex() ? Optional.of(pos.asIndex().n) : Optional.empty();
    this.reverseGet = Index::of;
  }

  public final Function<Position, Position> modify(Function<Integer, Integer> f) {
    return v ->
    {
      final Optional<Integer> opt = getOptional.apply(v);
      if (opt.isPresent()) return reverseGet.apply(f.apply(opt.get()));
      else return v;
    };
  }


  public final Function<Position, Optional<Position>> modifyOptional(Function<Integer, Integer> f) {
    return v ->
    {
      final Optional<Integer> opt = getOptional.apply(v);
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

  public final Function<Position, Optional<Integer>> find(Predicate<Integer> predicate) {
    return v -> getOptional.apply(v)
      .filter(predicate);
  }


  public final Predicate<Position> exists(Predicate<Integer> predicate) {
    return v -> getOptional.apply(v)
      .filter(predicate)
      .isPresent();
  }

  /**
   * check if there is no target or the target satisfies the predicate
   */

  public final Predicate<Position> all(Predicate<Integer> predicate) {
    return v ->
    {
      final Optional<Integer> value = getOptional.apply(v);
      return value.map(predicate::test)
        .orElse(true);
    };
  }


}
