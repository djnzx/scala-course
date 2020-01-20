package _degoes._chunks

object Hkt01 extends App {

  // general behavior
  trait Random[F[_]] {
    def nextInt(upper: Int): F[Int]
  }

  // implementation picker
  object Random {
    def apply[F[_]: Random]: Random[F] = implicitly[Random[F]]
  }

  case class Real[A](run: () => A)
  implicit val RandomReal: Random[Real] = new Random[Real] {
    override def nextInt(upper: Int): Real[Int] = Real(() => scala.util.Random.nextInt(upper))
  }

  case class Mock[A](run: () => A)
  implicit val RandomMock: Random[Mock] = new Random[Mock] {
    val testData = List(11,22,33,44,55);
    private var idx = -1;

    override def nextInt(upper: Int): Mock[Int] = {
      idx = (idx + 1) % testData.length;
      Mock( () => testData(idx) )
    }
  }

  val instances: Seq[_ >: Mock[_] with Real[_]] = Seq(Random[Real], Random[Mock])
  val instance =
    Random[Real]
//    Random[Mock]
  for {
    _ <- 1 to 5
  } println(instance.nextInt(100).run())

}
