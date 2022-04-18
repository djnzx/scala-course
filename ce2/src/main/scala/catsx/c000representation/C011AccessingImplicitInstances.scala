package catsx.c000representation

object C011AccessingImplicitInstances {

  trait Box[A] {
    val a: A
  }

  val boxInt = new Box[Int] {
    override val a: Int = 33
  }

  object Method1Direct {
    val a = boxInt.a
  }

  object Method2ViaImplicitKeyword {
    implicit val boxIntImpl = boxInt
    val a = implicitly[Box[Int]].a
  }

  object Method3ViaImplicitParameter {
    implicit val boxIntImpl = boxInt
    def whatever(implicit ba: Box[Int]) = ba.a
    val a = whatever
  }

}
