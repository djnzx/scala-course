package winitzki

import cats.implicits._

class EitherVariations extends Base {

  test("1") {

    trait Animal
    trait Dog             extends Animal
    case class Shepherd() extends Dog

    val e1: Either[String, Animal] = Shepherd().asRight[String].widen[Animal]
    val e2: Either[String, Dog] = Shepherd().asRight[String].widen[Dog]
    val e3: Either[String, Shepherd] = Shepherd().asRight[String]

    def m1(e: Either[String, Animal]) = pprint.log("m1" -> e)
    m1(e1)
    m1(e2)
    m1(e3)

    def m2(e: Either[String, Dog]) = pprint.log("m2" -> e)
//    m2(e1)
    m2(e2)
    m2(e3)

    def m3(e: Either[String, Shepherd]) = pprint.log("m3" -> e)
//    m3(e1)
//    m3(e2)
    m3(e3)

  }

}
