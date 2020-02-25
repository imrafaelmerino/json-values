package jsonvalues.specifications.immutable.array

import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll


class FilterMapReduceSpec extends BasePropSpec
{


  property("filterAllValues (removes every element but strings)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isStr)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isStr)
      }
      )
  }

  property("filterValues (removes every element but strings)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterValues(p => p.value.isStr)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isStr)
      }
      )
  }


  property("filterAllValues (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isIntegral)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isIntegral)
      }
      )
  }


  property("filterValues (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isIntegral)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isIntegral)
      }
      )
  }

  property("filterAllValues (removes null)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isNotNull)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isNotNull)
      }
      )
  }

  property("filterValues (removes null)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>

          val arr = js.filterValues(p => p.value.isNotNull)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isNotNull)
      }
      )
  }


  property("filterKeys returns the same array")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val predicate: Predicate[JsPair] = _ => false
          val arr = js.filterKeys(predicate)
          arr == js
      }
      )
  }


  property("filterAllKeys (removes keys named 'a')")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val predicate: Predicate[JsPair] = p => p.path.stream().filter(it => it.isKey).allMatch(it => it.asKey().name != "a")
          val arr = js.filterAllKeys(predicate)
          arr.streamAll().allMatch(predicate)
      }
      )
  }
}
