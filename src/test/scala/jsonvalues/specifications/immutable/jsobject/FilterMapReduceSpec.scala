package jsonvalues.specifications.immutable.jsobject


import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll


class FilterMapReduceSpec extends BasePropSpec
{


  property("filterElems_ (removes every element but strings)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems_(p => p.elem.isStr)
               obj.stream_().filter(p => p.elem.isNotJson).allMatch(p => p.elem.isStr)
           }
           )
  }

  property("filterElems (removes every element but strings)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems(p => p.elem.isStr)
               obj.stream_().filter(p => p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p => p.elem.isStr)
           }
           )
  }


  property("filterElems_ (removes every element but integral numbers)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems_(p => p.elem.isIntegral)
               obj.stream_().filter(p => p.elem.isNotJson).allMatch(p => p.elem.isIntegral)
           }
           )
  }


  property("filterElems (removes every element but integral numbers)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems_(p => p.elem.isIntegral)
               obj.stream_().filter(p => p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p => p.elem.isIntegral)
           }
           )
  }

  property("filterElems_ (removes null)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems_(p => p.elem.isNotNull)
               obj.stream_().filter(p => p.elem.isNotJson).allMatch(p => p.elem.isNotNull)
           }
           )
  }

  property("filterElems (removes null)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val obj = js.filterElems(p => p.elem.isNotNull)
               obj.stream_().filter(p => p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p => p.elem.isNotNull)
           }
           )
  }

  property("filterKeys (removes all keys)")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val predicate: Predicate[JsPair] = _ => false
               val obj = js.filterKeys(predicate)
               obj == JsObj.empty()
           }
           )
  }

  property("filterKeys (removes keys named 'a')")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val predicate: Predicate[JsPair] = p => p.path.head().asKey().name != "a"
               val obj = js.filterKeys(predicate)
               obj.stream_().allMatch(p => p.path.head().asKey().name != "a")
           }
           )
  }

  property("filterKeys_ (removes keys named 'a')")
  {
    check(
           forAll(jsGen.jsObjGen)
           {
             js =>
               val predicate: Predicate[JsPair] = p => p.path.stream().filter(it => it.isKey).allMatch(it => it.asKey().name != "a")
               val obj = js.filterKeys_(predicate)
               obj.stream_().allMatch(predicate)
           }
           )
  }


}
