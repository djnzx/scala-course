package testable_ideas

object Testable06 extends App {

  trait Random {
    def nextInt: Int
  }

  implicit val real: Random = new Random {
    override def nextInt: Int = scala.util.Random.nextInt()
  }

  val mock = new Random {
    override def nextInt: Int = 42
  }

  def random1(instance: Random): Int = instance.nextInt
  def random2: Int = implicitly[Random].nextInt
  def random3(implicit instance: Random): Int = instance.nextInt

  random1(mock)
  random2
  random3
  random3(real)

}
