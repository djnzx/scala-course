package essential

object X192Monad extends App {
  val xm: Option[Int] = Some(12)
  val ym: Option[Int] = Some(34)
  // it works because Option has
  val zm: Option[Int] = for {
    x <- xm    // flatMap
    y <- ym    // map
    z = x + y
  } yield z
  println(zm)

  val am: Seq[Int] = Seq(1,2,3,4)
  val bm: Option[Int] = Some(2)
  val cm: Seq[Int] = for {
    a <- am    // flatMap
    b <- bm    // map
    c = a + b
  } yield c
  val cm2 = am.flatMap(a => bm.map(b => a + b))
  println(cm)

  val dm: Either[String, Int] = Right(44)
//  val dm: Either[String, Int] = Left("Error 1")
//  val em: Either[String, Int] = Right(33)
  val em: Either[String, Int] = Left("Error 2")
  val fm: Either[String, Int] = for {
    d <- dm
    e <- em
    f = d - e
  } yield f
  println(fm)
}
