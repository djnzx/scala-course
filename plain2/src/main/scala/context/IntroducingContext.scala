package context

import scala.util.chaining.scalaUtilChainingOps

object IntroducingContext extends App {

  /** general behavior */
  trait Random[F[_]] {
    def nextInt(upper: Int): F[Int]
  }

  /** implementation accessor */
  object Random {
    def apply[F[_]: Random]: Random[F] = implicitly[Random[F]]
    def apply2[F[_]](implicit instance: Random[F]): Random[F] = instance
  }

  case class Real[A](value: A)
  case class Mock[A](value: A)

  /** usage, we accessed it, but we don't know anything how it is wrapped */
  def useRandom[F[_]: Random]: F[Int] =
    Random[F].nextInt(100)

  def generateFiveRandomNumbers[F[_]: Random] =
    (1 to 5)
      .map(_ => useRandom[F])

  /** real implementation */
  implicit val randomReal: Random[Real] = new Random[Real] {
    override def nextInt(upper: Int): Real[Int] = Real(scala.util.Random.nextInt(upper))
  }

  /** mock implementation */
  implicit val randomMock: Random[Mock] = new Random[Mock] {
    val testData = List(11, 22, 33, 44, 55);
    private var idx = -1

    override def nextInt(upper: Int): Mock[Int] = {
      idx = (idx + 1) % testData.length;
      Mock(testData(idx))
    }
  }

  /** real */
  generateFiveRandomNumbers[Real]
    .map(_.value)
    .mkString(" ")
    .pipe(println)

  /** mocked */
  generateFiveRandomNumbers[Mock]
    .map(_.value)
    .mkString(" ")
    .pipe(println)

}
