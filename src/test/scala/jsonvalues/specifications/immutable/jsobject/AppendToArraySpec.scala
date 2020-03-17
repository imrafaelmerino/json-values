package jsonvalues.specifications.immutable.jsobject

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class AppendToArraySpec extends BasePropSpec
{


  property("appends elements to the back after creating a new array in an empty object")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsObj.empty().append(path,
                                 elem
                                 ).getArray(path).size() == 1
          }
          )
  }

  property("appendIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !JsObj.empty().appendIfPresent(path,
                                           ScalaToJava.supplier(() => elem)
                                           ).getArrayOpt(path).isPresent
          }
          )
  }
  //
  property("appends elements to the front after creating a new array in an empty object")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsObj.empty().prepend(path,
                                  elem,
                                  elem
                                  ).getArray(path).size() == 2
          }
          )
  }

  property("prependIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !JsObj.empty().prependIfPresent(path,
                                            ScalaToJava.supplier(() => elem)
                                            ).getArrayOpt(path).isPresent
          }
          )
  }

  property("appendsFrontIfPresent appends elements to the front when the array containsPath")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsObj.empty().prepend(path,
                                  elem
                                  ).prependIfPresent(path,
                                                     ScalaToJava.supplier(() => elem)
                                                     ).getArray(path).size() == 2


          }
          )
  }

  property("appendsBackIfPresent appends elements to the back when the array containsPath")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsObj.empty().append(path,
                                 elem
                                 ).appendIfPresent(path,
                                                   ScalaToJava.supplier(() => elem)
                                                   ).getArrayOpt(path).get().size() == 2


          }
          )
  }
}
