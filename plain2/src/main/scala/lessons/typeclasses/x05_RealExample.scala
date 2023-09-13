package lessons.typeclasses

object x05_RealExample extends App {
  trait Named[E] { val name: String }
  implicit val n_int: Named[Int] = new Named[Int] {
    override val name: String = "int"
  }
  implicit val n_chr: Named[Char] = new Named[Char] {
    override val name: String = "char"
  }
  implicit val n_str: Named[String] = new Named[String] {
    override val name: String = "string"
  }
  // json parser
  trait Parser[A] {
    def parse(s: String): Option[A]
  }
  implicit val p_int: Parser[Int] = new Parser[Int] {
    override def parse(s: String): Option[Int] = Some(s.toInt)
  }
  implicit val p_str: Parser[String] = new Parser[String] {
    override def parse(s: String): Option[String] = Some(s)
  }
  implicit val p_chr: Parser[Char] = new Parser[Char] {
    override def parse(s: String): Option[Char] = Some(s(0))
  }

  type EOL = Unit
  trait DynDisp[E] {
    type Out
    def dispatch(name: String, msg: String): Option[Out]
  }

  // base step
  implicit val base = new DynDisp[EOL] {
    override type Out = Nothing
    override def dispatch(name: String, msg: String) = None
  }
  // induction
  implicit def step[Head, Tail](implicit head: Named[Head], parser: Parser[Head], tail: DynDisp[Tail]
                               ): DynDisp[(Head, Tail)] {type Out = Either[tail.Out, Head]} = new DynDisp[(Head, Tail)] {

    override type Out = Either[tail.Out, Head]

    override def dispatch(name: String, msg: String): Option[Out] = {
      if (name == head.name) {
        parser.parse(msg).map(Right(_))
      } else {
        tail.dispatch(name, msg).map(Left(_))
      }
    }
  }

  val inst = implicitly[DynDisp[(Int, (Char, (String, EOL)))]]
  println(inst.dispatch("int", "123"))
  println(inst.dispatch("char", "X"))
  println(inst.dispatch("string", "Hello"))
  println(inst.dispatch("string_", "Hello"))

}
