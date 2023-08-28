package scalacheck

import org.scalacheck.Gen

import scala.util.Random

object GeneratorsApp extends App {
  val g: Gen[Int] = Gen.choose(-10, 10)

  val r = g.sample
  pprint.pprintln(r)

  val data = LazyList.unfold(10) {
    case 0 => None
    case n => g.sample.map(_ -> (n - 1))
  }

//  pprint.pprintln(data)

  val data2 = LazyList.unfold(10) {
    case 0 => None
    case n => Gen.uuid.sample.map(_ -> (n - 1)) // uuid.sample.map(_ -> (n - 1))
  }
//  pprint.pprintln(data2)

  import Guide2Gen._
  (1 to 5).foreach(_ => pprint.pprintln(gList2.sample))
  val rx = Gen.listOfN(12, genOp).sample
//  pprint.pprintln(rx)

  val gSpecificString: Gen[String] = for {
    p1 <- Gen.stringOfN(4, Gen.alphaLowerChar)
    p2 <- Gen.stringOfN(4, Gen.alphaUpperChar)
  } yield "*" + p1 + p2 + "*"

  Gen.listOfN(10, Gen.stringOfN(10, Gen.alphaChar)).sample.foreach(println)
  Gen.listOfN(10, gSpecificString).sample.foreach(println)
  Gen.pick(4, Random.shuffle((1 to 36).toList)).sample.foreach(println)
  Gen.pick(3, 'A' to 'Z').sample.foreach(println)
}
