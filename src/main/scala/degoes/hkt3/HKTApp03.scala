package degoes.hkt3

object HKTApp03 extends App {

  trait Random {
    def nextInt(upper: Int): Int
  }

  case class TestData(nums: List[Int]) {
    def nextInt(upper: Int): (TestData, Int) = (copy(nums = nums.drop(1)), nums.head)
  }

  object real {
    implicit val RandomReal: Random = new Random {
      override def nextInt(upper: Int): Int = scala.util.Random.nextInt(upper)
    }
  }

  object mock {
    val testDataSet = TestData(nums = List(1,3,5,7,9))
    var index = 0
    implicit val RandomMock: Random = new Random {
      override def nextInt(upper: Int): Int = {
        val idx = index
        index +=1
        testDataSet.nums(idx)
      }
    }
  }

  object RealApp {
    def run(): Unit = {
      import real._

      val randoms: Seq[Int] = for {
        _ <- 1 to 5
      } yield implicitly[Random].nextInt(20)

      println(randoms)
    }
  }

  object MockApp {
    def run(): Unit = {
      import mock._

      val randoms: Seq[Int] = for {
        _ <- 1 to 5
      } yield implicitly[Random].nextInt(20)

      println(randoms)
    }
  }

  RealApp.run()
  MockApp.run()
}
