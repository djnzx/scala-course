package nomicon.ch03test

import zio.ZIO
import zio.test.TestAspect._
import zio.test._
import zio.test.Assertion._
import zio.duration._
import zio.random.Random

object Aspects extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")(
    testM("this test will be repeated to ensure it is stable") {
      assertM(ZIO.succeed(1 + 1))(equalTo(2))
    } @@ nonFlaky @@ timeoutWarning(10.millis)
  ) @@ timeoutWarning(1.second) @@ repeats(10)
}

/**
  * property testing out of the box
  */
object Property extends DefaultRunnableSpec {
  
  val intGen: Gen[Random, Int] = Gen.anyInt
  
  def spec = suite("Property Testing")(
    testM("int add assoc") {
      check(intGen, intGen, intGen) { (x, y, z) => 
        val left = (x + y) + z
        val right = x + (y + z)
        assert(left)(equalTo(right))
      }
    }
  )
  
}

object UserGen {
  final case class User(name: String, age: Int)
  val genName: Gen[Random with Sized, String] = Gen.anyASCIIString
  val genAge: Gen[Random, Int] = Gen.int(18, 120)
  val genUser: Gen[Random with Sized, User] = for {
    name <- genName
    age <- genAge
  } yield User(name, age)
}