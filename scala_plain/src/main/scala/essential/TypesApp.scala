package essential

object TypesApp extends App {
  val r1: Set[(Boolean, Boolean)] = for {
    a <- Set(true, false)
    b <- Set(true, false)
  } yield (a,b)

  val bools = Set(true, false)
  val r11 = bools.flatMap(v1 => bools.map(v2 => (v1, v2)))

  val r2: Set[Unit] = for {
    a <- Set( () )
  } yield a

  val r3: Set[Either[Unit, Boolean]] = for {
    a <- Set( () ).map(Left(_))
    b <- Set(true, false).map(Right(_))
    c <- Seq(a, b)
  } yield c
  println(r3)
}
