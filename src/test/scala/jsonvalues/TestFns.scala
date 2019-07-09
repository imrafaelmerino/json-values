package jsonvalues

import scala.annotation.tailrec


object TestFns
{


//  def logDecorator[T <: Json[T]](logger: Logger)
//                                (
//                                  supplier: () => T,
//                                  name: String
//                                ): T
//  =
//  {
//    Utils.fnDecorator[T](logger)(ScalaToJava.supplier(supplier),
//                               name
//                              )
//  }


  def allKeyMatches_[T <: Json[T]](regex: String,
                      json : T
                    ): Boolean =
  {
    allMatch((p: JsPair) => p.path.last().isKey,
             (p: JsPair) => regex.r.pattern.matcher(p.path.last().asKey().name).matches(),
             json
             )
  }

  def allKeyMatches[T <: Json[T]](regex: String,
                    json : T
                   ): Boolean =
  {
    allMatch((p: JsPair) => p.path.last().isKey && p.path.size() == 1,
             (p: JsPair) => regex.r.pattern.matcher(p.path.last().asKey().name).matches(),
             json
             )
  }

  def allStrEqualTo_[T <: Json[T]](str : String,
                     json: T
                    ): Boolean =
  {
    allMatch(p => p.elem.isStr,
             p => p.elem.asInstanceOf[JsStr].x == str,
             json
             )
  }

  def allStrEqualTo[T <: Json[T]](str : String,
                    json: T
                   ): Boolean =
  {
    allMatch(p => p.elem.isStr && p.path.size() == 1,
             p => p.elem.asInstanceOf[JsStr].x == str,
             json
             )
  }

  def allStrMatches_[T <: Json[T]](regex: String,
                     json : T
                    ): Boolean =
  {
    allMatch((p: JsPair) => p.elem.isStr,
             (p: JsPair) => p.elem.isStr(s => regex.r.pattern.matcher(s).matches()),
             json
             )
  }

  def allStrMatches[T <: Json[T]](regex: String,
                    json : T
                   ): Boolean =
  {
    allMatch((p: JsPair) => p.elem.isStr && p.path.size() == 1,
             (p: JsPair) => p.elem.isStr(s => regex.r.pattern.matcher(s).matches()),
             json
             )
  }

  def allNotNull_[T <: Json[T]](json: T
                 ): Boolean =
  {
    allMatch((p: JsPair) => true,
              p => p.elem.isNotNull,
             json
             )
  }

  def allNotNull[T <: Json[T]](json: T
                ): Boolean =
  {
    allMatch((p: JsPair) => p.path.size() == 1,
             p => p.elem.isNotNull,
             json
             )
  }


  def allMatch[T <: Json[T]](filter   : JsPair => Boolean,
               predicate: JsPair => Boolean,
               json     : T
              ): Boolean =
  {
    json.stream_()
      .filter(ScalaToJava.predicate(filter))
      .allMatch(ScalaToJava.predicate(predicate))
  }


  def sameSize[T <: Json[T]] = (a: T,
                  b: T
                 ) => a.size() == b.size()


  //  def assertion(logger: String,
  //                              format: String = "[%1$tF %1$tT] %5$s %x"
  //                             )
  //                             (jsonGen: Gen[T],
  //                              pathGen: Gen[JsPath]
  //                             )
  //                             (
  //                               fn: (T, JsPath) => T,
  //                               checkFn: (T, T, JsPath) => Boolean,
  //                               name: String
  //                             ): Assertion =
  //  {
  //
  //    System.setProperty("java.util.logging.SimpleFormatter.format",
  //                       format
  //                      )
  //    check(
  //           forAll(jsonGen,
  //                  pathGen
  //                 )
  //           {
  //             (json: T,
  //              parse: JsPath
  //             ) =>
  //               checkFn(json,
  //                       logDecorator[T](Logger.getLogger(logger))(
  //                                                                  fn,
  //                                                                  json,
  //                                                                  name
  //                                                                ),
  //                       parse
  //                      )
  //           }
  //         )
  //  }

  def camel2Underscore(s: String): String =
  {
    @tailrec def camel2Underscore(s            : String,
                                  output       : String,
                                  lastUppercase: Boolean
                                 ): String =
      if (s.isEmpty) output
      else
      {
        val c = if (s.head.isUpper && !lastUppercase) "_" + s.head.toLower else s.head.toLower
        camel2Underscore(s.tail,
                         output + c,
                         s.head.isUpper && !lastUppercase
                        )
      }

    camel2Underscore(s,
                     "",
                     true
                    )
  }

}
