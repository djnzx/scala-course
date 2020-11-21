package nomicon.ch04test

import zio.{Exit, ZIO}

object SimpleDiv {

  /**
    * [[ZIO.effect]] constructor guarantees that it will catch any non-fatal
    */
  //                           no res, fail, result
  def safeDiv(x: Int, y: Int): ZIO[Any, Unit, Int] = ZIO.effect(x / y)
    .catchAll(_ => ZIO.fail(()))

}

import zio.test._
import zio.test.Assertion._

object ExampleSpec1 extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")(
    test("addition works") {
      assert(1 + 1)(equalTo(2))
    }
  )
}

/**
  * only one change:
  *   test   => testM
  *   assert => assertM
  */
object ExampleSpec2 extends DefaultRunnableSpec {
  def sum(a: Int, b: Int) = ZIO.succeed(a + b)
  def spec = suite("ExampleSpec")(
    testM("ZIO.succeed succeeds with specified value") {
      assertM(sum(1, 1))(equalTo(2))
    }
  )
}

object ExampleSpec3 extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")(

    testM("testing an effect using map operator") {
      ZIO.succeed(1 + 1)
        .map(n => assert(n)(equalTo(2)))
    },

    testM("testing an effect using a for comprehension") {
      for {
        n <- ZIO.succeed(1 + 1)
      } yield assert(n)(equalTo(2))
    }
  )
}

object ExampleSpec4 extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")( testM("and") {
    for {
      x <- ZIO.succeed(1)
      y <- ZIO.succeed(2)
    } yield assert(x)(equalTo(1)) &&
      assert(y)(equalTo(2))
  })
}

object ExampleSpec5 extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")( test("hasSameElement") {
    assert(List(1, 1, 2, 3))(hasSameElements(List(3, 2, 1, 1)))
  })
}

object ExampleSpec6 extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")( testM("fails") {
    for {
      exit <- ZIO.effect(1 / 0)
        .catchAll(_ => ZIO.fail(()))
        .run
    } yield assert(exit)(fails(isUnit)) }
  )
}

object Example7 extends DefaultRunnableSpec {

  val assertion1: Assertion[Iterable[Int]] = isNonEmpty && forall(nonNegative)
  val assertion2: Assertion[Iterable[Any]] = isEmpty || hasSize(equalTo(3))
  val atLeastOneDup = not(isDistinct)
  
  override def spec = suite("custom suite")(
    test("custom test") {
      assert(List(1,1,2))(atLeastOneDup)
    },
    test("notEmpty") {
      assert(List(1,2,3))(assertion1)
    }
  )
}