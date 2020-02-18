package jsonvalues
import jsonvalues.ScalaToJava.toVavrVector
import org.scalacheck.Gen

case class FreqField(freqIndex: Int = 1,
                     freqKey  : Int = 1
                    )


case class JsPathGens(lengthGen: Gen[Int] = Gen.choose(1,
                                                       10
                                                      ),
                      keyGen      : Gen[String] = Gen.oneOf(Characters.ALPHABET),
                      indexGen    : Gen[Int] = Gen.choose(0,
                                                          10
                                                         ),
                      freqFieldGen: FreqField = FreqField()
                     )
{

  val indexFieldGen = indexGen.map(it => Index.of(it))

  val keyFieldGen = keyGen.map(it => Key.of(it))

  val fieldGen = Gen.frequency((freqFieldGen.freqIndex, indexFieldGen),
                               (freqFieldGen.freqKey, keyFieldGen)
                              )

  val pathGen = for
    {
    length <- lengthGen
    arr <- Gen.containerOfN[Vector, Position](length,
                                              fieldGen
                                             )
  } yield new JsPath(toVavrVector(arr))


  val   objectPathGen = for
    {
    length <- lengthGen
    key <- keyFieldGen
    arr <- Gen.containerOfN[Vector, Position](length,
                                              fieldGen
                                             )
  } yield new JsPath(toVavrVector(arr)).prepend(new JsPath(toVavrVector[Position](Vector(key))))

  val arrayPathGen = for
    {
    length <- lengthGen
    key <- indexFieldGen
    arr <- Gen.containerOfN[Vector, Position](length,
                                              fieldGen
                                             )
  } yield new JsPath(toVavrVector(arr)).prepend(new JsPath(toVavrVector[Position](Vector(key))))


}
