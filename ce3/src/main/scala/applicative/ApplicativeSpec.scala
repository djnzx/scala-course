package applicative

import cats._
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

class ApplicativeSpec extends AnyFunSuite {

  /** the only idea is the ability to cope with unfinished calculations */
  test("curried partial application") {

    /** 1. having data */
    val data123: List[Int] = List(1, 2, 3)
    val data102030: List[Int] = List(10, 20, 30)

    /** 2. having curried function */
    val addFn: Int => Int => Int = x => y => x + y
    val mulFn: Int => Int => Int = x => y => x * y

    /** 3.1 applying partially, we have function wrapped into List */
    val add1: List[Int => Int] = data123.map(addFn) // (+1), (+2), (+3)
    /** 3.2 using applicative we can have many functions, and get a cartesian even here */
    val add1a: List[Int => Int] = List(addFn, mulFn).ap(data123)

    /** 4.1. now we need a monad to combine, and actually get a cartesian product !!! */
    val add2a: List[Int] = add1a.flatMap(f => data102030.map(x => f(x)))
    val add2b: List[Int] = data102030.flatMap(x => add1a.map(f => f(x))) // order is different

    /** 4.2. but monad is not required if we have apply, even syntax is simpler */
    val add2c: List[Int] = add1a.ap(data102030)

    pprint.log(add2a)
    pprint.log(add2b)
    pprint.log(add2c)
  }

  /** and special case */
  test("tupled") {
    val data1: List[Int] = List(2, 3, 4)
    val data2: List[Int] = List(10, 20, 30, 40)

    val addFn: Int => Int => Int = x => y => x + y
    val mulFn: Int => Int => Int = x => y => x * y
    val fns: List[Int => Int => Int] = List(addFn, mulFn) // len=2

    val AL = Applicative[List]

    val partially1: List[Int] => List[Int => Int] = AL.ap(fns)
    val partially2: List[Int => Int] = AL.ap(fns)(data1)
    val appliedFully: List[Int] = AL.ap(partially2)(data2)
    pprint.pprintln(appliedFully, width = 1000)

    val addt = (x: Int, y: Int) => x + y
    val mult = (x: Int, y: Int) => x * y
    val opst: List[(Int, Int) => Int] = List(addt, mult)

    // cartesian product in one step
    val r2: List[Int] = opst.ap2(data1, data2)
    pprint.pprintln(r2, width = 1000)
  }

}
