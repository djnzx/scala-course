package shapelessx

import shapeless.{::, Generic, HList, HNil}

object Shape extends App {

  trait Ordering[A] {
    def compare(left: A, right: A): Int
  }

  object Ordering {
    def apply[A](implicit ev: Ordering[A]): Ordering[A] = ev
  }

  implicit val compareInt: Ordering[Int] = (left: Int, right: Int) => left - right

  implicit val compareString: Ordering[String] = (left: String, right: String) => {
    if (left.length == right.length) {
      left.zip(right).zipWithIndex.find { case ((l, r), _) => l != r }.map(_._2 + 1).getOrElse(0)
    } else Math.min(left.length, right.length) + 1
  }

  implicit val compareHNil: Ordering[HNil] = (_: HNil, _: HNil) => 0

  implicit def compareHList[Head, Tail <: HList](
    implicit
    headCompare: Ordering[Head],
    tailCompare: Ordering[Tail]
  ) =
    new Ordering[Head :: Tail] {
      override def compare(left: Head :: Tail, right: Head :: Tail) = {
        val leftC = headCompare.compare(left.head, right.head)
        lazy val rightC = tailCompare.compare(left.tail, right.tail)

        if (leftC == 0) rightC else leftC
      }
    }

  implicit def compareCaseClasses[CC, HL <: HList](implicit gen: Generic.Aux[CC, HL],
                                                   hListC: Ordering[HL]): Ordering[CC] = new Ordering[CC] {
    override def compare(left: CC, right: CC): Int = {
      val lHL = gen.to(left)
      val rHl = gen.to(right)

      hListC.compare(lHL, rHl)
    }
  }

  case class User(name: String, age: Int)

  val transformer = Generic[User]

  transformer.from(HList("name", 42))
  transformer.to(User("name", 42))

  //  Compare[Int].compare(1, 1)
  //  Compare[String].compare("a", "a")
  //  Compare[User].compare(User("name", 42), User("name", 42))

  trait Transform[A, B] {
    def from(v: A): B

    def to(v: B): A
  }

  implicit def transformCaseClasses[CC1, CC2, HL <: HList](implicit gen1: Generic.Aux[CC1, HL],
                                                           gen2: Generic.Aux[CC2, HL]) = new Transform[CC1, CC2] {
    override def from(v: CC1) = {
      gen2.from(gen1.to(v))
    }

    override def to(v: CC2) = {
      gen1.from(gen2.to(v))
    }
  }

  case class Person(name: String, age: Int)

  implicitly[Transform[User, Person]].from(User("A", 1))
  implicitly[Transform[User, Person]].to(Person("A", 1))
}
