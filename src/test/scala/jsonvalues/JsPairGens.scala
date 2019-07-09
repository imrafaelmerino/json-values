package jsonvalues

import org.scalacheck.Gen

case class JsPairGens(jsPathGens: JsPathGens = JsPathGens(),
                      jsElemGens: JsElemGens = JsElemGens()
                     )
{

  val pairOfStringGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsStrGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfBoolGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsBoolGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfNumberGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsNumberGen

    } yield JsPair.of(path,
                      elem
                      )


  val pairOfIntGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsIntGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfBigIntGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsBigIntGen

    } yield JsPair.of(path,
                      elem
                      )


  val pairOfLongGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsLongGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfDoubleGen =

    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsDoubleGen

    } yield JsPair.of(path,
                      elem
                      )


  val pairOfJsObjGen =
    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsObjGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfJsObjArrGen =
    for
      {
      path <- jsPathGens.pathGen
      elem <- jsElemGens.jsArrGen

    } yield JsPair.of(path,
                      elem
                      )

  val pairOfValueGen = for
    {
    path <- jsPathGens.pathGen
    elem <- jsElemGens.jsValueGen

  } yield JsPair.of(path,
                    elem
                    )

  val pairGen = Gen.frequency((1, pairOfValueGen),
                              (1, pairOfJsObjArrGen),
                              (1, pairOfJsObjGen)
                              )


}
