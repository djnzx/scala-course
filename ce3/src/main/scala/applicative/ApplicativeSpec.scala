package applicative

import cats._
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

class ApplicativeSpec extends AnyFunSuite {

  val data1: List[Int] = List(2, 3, 4)
  val data2: List[Int] = List(10, 20, 30, 40)

  val addFn: Int => Int => Int     = x => y => x + y
  val mulFn: Int => Int => Int     = x => y => x * y
  val fns: List[Int => Int => Int] = List(addFn, mulFn) // len=2

  /** the only idea is the ability to cope with unfinished calculations */
  test("curried") {
    val a1: List[Int => Int] = fns.ap(data1) // len=6, 2 functions applied to 3 params.
    pprint.pprintln(a1.length)

    val a2: List[Int] = a1.ap(data2) // len=24, 6 functions applied to 4 params
    pprint.pprintln(a2.length)
  }

  /** and special case  */
  test("tupled") {
    val AL = Applicative[List]

    val partially1: List[Int] => List[Int => Int] = AL.ap(fns)

    val partially2: List[Int => Int] = AL.ap(fns)(data1)
    val appliedFully: List[Int]      = AL.ap(partially2)(data2)

    val addt                          = (x: Int, y: Int) => x + y
    val mult                          = (x: Int, y: Int) => x * y
    val opst: List[(Int, Int) => Int] = List(addt, mult)
    val r2: List[Int]                 = opst.ap2(data1, data2)

    pprint.pprintln(appliedFully)
  }

}
