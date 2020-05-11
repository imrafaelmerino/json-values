package jsonvalues.optics;
import jsonvalues.JsValue;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Prism<T>
{

  public final Function<JsValue, Optional<T>> getOptional;
  public final Function<T,JsValue> reverseGet;

  public Prism(final Function<JsValue, Optional<T>> getOptional,
               final Function<T, JsValue> reverseGet
              )
  {
    this.getOptional = getOptional;
    this.reverseGet = reverseGet;
  }

  public final Function<JsValue,JsValue> getOrModify(Function<T,T> f){
   return v ->
   {
     final Optional<T> opt = getOptional.apply(v);

     if(opt.isPresent())return reverseGet.apply(f.apply(opt.get()));
     else return v;
   };
  }

  public final boolean isEmpty(JsValue value) {
    return !getOptional.apply(value).isPresent();
  }


  public final boolean nonEmpty(JsValue value) {
    return getOptional.apply(value).isPresent();
  }

  public final Function<JsValue,Optional<T>> find(Predicate<T> predicate){
    return  v-> getOptional.apply(v).filter(predicate);
  }


  public final Predicate<JsValue> exists(Predicate<T> predicate){
    return  v-> getOptional.apply(v).filter(predicate).isPresent();
  }

  /** check if there is no target or the target satisfies the predicate */

  public final Predicate<JsValue> all(Predicate<T> predicate){
    return  v-> {

      final Optional<T> value = getOptional.apply(v);
      return value.map(predicate::test)
                  .orElse(true);
    };
  }

}
