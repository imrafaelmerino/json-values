package jsonvalues;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Lens is an optic that can be seen as a pair of functions:
 {@code
  - get: S      => O i.e. from an S, we can extract an O
  - set: (O, S) => S i.e. from an S and a O, we obtain a S. Unless a prism, to go back to S we need another S.
 }
 * Typically a Lens can be defined between a Product (e.g. record, tuple) and one of its component.
 * Given a lens there are essentially three things you might want to do:
 * -view the subpart
 * -modify the whole by changing the subpart
 * -combine this lens with another lens to look even deeper
 *
 * @param <S> the source of a lens
 * @param <O> the target of a lens
 */
public class Lens<S, O> {

  /**
   * function to view the part
   */
  public final Function<S, O> get;
  /**
   * function to modify the whole by setting the subpart
   */
  public final Function<O, Function<S, S>> set;
  /**
   * function to modify the whole by modifying the subpart with a function
   */
  public final Function<Function<O, O>, Function<S, S>> modify;

  Lens(final Function<S, O> get,
       final Function<O, Function<S, S>> set) {

    this.modify = f -> json -> set.apply(f.apply(get.apply(json))).apply(json);
    this.set = set;
    this.get = get;

  }

  /**
   * find if the target satisfies the predicate
   *
   * @param predicate the predicate
   * @return a function from the whole to an optional subpart
   */
  public Function<S, Optional<O>> find(final Predicate<O> predicate) {
    return s -> predicate.test(get.apply(s)) ?
      Optional.of((get.apply(s))) :
      Optional.empty();
  }

  public <A> Function<S,A> compose(Lens<O,A> other){
      //TODO
      return null;
  }

  /**
   * check if there is a target and it satisfies the predicate
   *
   * @param predicate the predicate
   * @return a predicate on the whole
   */
  public Predicate<S> exists(final Predicate<O> predicate) {
      Objects.requireNonNull(predicate);
    return s -> predicate.test(get.apply(s));
  }


}
