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
          val obj = js.filterAllValues(p => p.value.isStr)
          obj.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isStr)
      }
      )
  }

  property("filterElems (removes every element but strings)")
  {
    check(
      forAll(jsGen.jsObjGen)
      {
        js =>
          val obj = js.filterValues(p => p.value.isStr)
          obj.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isStr)
      }
      )
  }


  property("filterElems_ (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsObjGen)
      {
        js =>
          val obj = js.filterAllValues(p => p.value.isIntegral)
          obj.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isIntegral)
      }
      )
  }


  property("filterElems (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsObjGen)
      {
        js =>
          val obj = js.filterAllValues(p => p.value.isIntegral)
          obj.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isIntegral)
      }
      )
  }

  property("filterElems_ (removes null)")
  {
    check(
      forAll(jsGen.jsObjGen)
      {
        js =>
          val obj = js.filterAllValues(p => p.value.isNotNull)
          obj.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isNotNull)
      }
      )
  }

  property("filterElems (removes null)")
  {
    check(
      forAll(jsGen.jsObjGen)
      {
        js =>
          val obj = js.filterValues(p => p.value.isNotNull)
          obj.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isNotNull)
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
          obj.streamAll().allMatch(p => p.path.head().asKey().name != "a")
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
          val obj = js.filterAllKeys(predicate)
          obj.streamAll().allMatch(predicate)
      }
      )
  }


}
