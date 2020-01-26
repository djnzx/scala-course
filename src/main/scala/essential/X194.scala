package essential

object X194 extends App {
  import scala.util.Try
  val opt1 = Some(1)
  val opt2 = Some(2)
  val opt3 = Some(3)
  val seq1 = Seq(1)
  val seq2 = Seq(2)
  val seq3 = Seq(3)
  val try1 = Try(1)
  val try2 = Try(2)
  val try3 = Try(3)
  val abc_ : Option[Int] = for {
    a <- opt1
    b <- opt2
    c <- opt3
  } yield a+b+c
  val def_ : Seq[Int] = for {
    d <- seq1
    e <- seq2
    f <- seq3
  } yield d+e+f
  val ghi_ : Try[Int] = for {
    g <- try1
    h <- try2
    i <- try3
  } yield g+h+i
}
