package object monads_basic {
  val o2: Option[Int] = Some(2)
  val e3: Either[String, Int] = Right[String, Int](3)

  val f: (Int, Int) => Int = (a, b) => a + b
}
