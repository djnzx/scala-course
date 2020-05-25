package lihaoyi

object S0540Parser extends App {

  trait StrParser[A] {
    def parse(s: String): A
  }

  object StrParser {
    // basic types
    implicit val p1: StrParser[Int] = _.toInt
    implicit val p2: StrParser[Long] = _.toLong
    implicit val p3: StrParser[Double] = _.toDouble
    implicit val p4: StrParser[Boolean] = _.toBoolean
    // sequence of any declared type
    implicit def p5[A](implicit p: StrParser[A]): StrParser[Seq[A]] =
      _.split(",").toSeq.map(_.trim).map(p.parse)
    // tuples
    implicit def p6[A,B](implicit p1: StrParser[A], p2: StrParser[B]) = new StrParser[(A,B)] {
      override def parse(s: String): (A, B) = s match {
        case s"($a=$b)" => (p1.parse(a), p2.parse(b))
      }
    }
  }

  def parse[A: StrParser](s: String): A = implicitly[StrParser[A]].parse(s)

  val x: Double = parse[Double]("3.14")
  val y1: Boolean = parse[Boolean]("true")
  val y2: Seq[Boolean] = parse[Seq[Boolean]]("true, false")
  println(parse[Seq[Double]]("1.6,2.3,3.7"))
  println(parse[(Int, Boolean)]("(5=true)"))
  println(parse[Seq[(Int, Boolean)]]("(5=true),(6=false)"))
  println(parse[(Seq[Int], Seq[Boolean])]("(1,2,3=false,false,true,true)"))
}
