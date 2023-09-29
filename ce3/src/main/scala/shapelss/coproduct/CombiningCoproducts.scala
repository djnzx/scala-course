package shapelss.coproduct

import cats.implicits.catsSyntaxEitherId
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import shapeless.:+:
import shapeless.CNil
import shapeless.Coproduct
import shapeless.Inl
import shapeless.Inr
import shapeless.Poly1
import shapeless.ops.coproduct.ExtendRight.Aux
import shapeless.ops.coproduct._

object CoproductNested {

  case object A
  case object B
  case object C

  type T1 = A.type :+: B.type :+: CNil
  type T2 = T1 :+: C.type :+: CNil

  val t1a: T1 = Coproduct[T1](A)
  val t1b: T1 = Coproduct[T1](B)
  val t2a: T2 = Coproduct[T2](Coproduct[T1](A))
  val t2b: T2 = Coproduct[T2](Coproduct[T1](B))
  val t2c: T2 = Coproduct[T2](C)

  object T {
    // A -> T1 one step
    implicit def lift1[A](a: A)(implicit inj: Inject[T1, A]): T1 = inj(a)
    // A -> T2 one step
    implicit def lift2[A](a: A)(implicit inj: Inject[T2, A]): T2 = inj(a)
    // A -> T2 two steps
    implicit def lift12[A](a: A)(implicit inj: Inject[T1, A], inj2: Inject[T2, T1]): T2 = inj2(inj(a))
  }

  import T._

  val tt1a: T1 = A
  val tt1b: T1 = B
  val tt2a: T2 = A
  val tt2b: T2 = B
  val tt2c: T2 = C

  object foldT1toString extends Poly1 {
    implicit val a = at[A.type] { a: A.type => a.toString }
    implicit val b = at[B.type] { b: B.type => b.toString }
  }

  object foldT2toString extends Poly1 {
    implicit val t1 = at[T1] { t1: T1 => t1.fold(foldT1toString) }
    implicit val c  = at[C.type] { x: C.type => x.toString }
  }

  val s1 = t2a.fold(foldT2toString)
  val s2 = t2b.fold(foldT2toString)
  val s3 = t2c.fold(foldT2toString)

}

object CoproductRemap {

  case object A
  case object B
  case object C

  type T1 = A.type :+: B.type :+: CNil
  type T2 = B.type :+: C.type :+: CNil
  type T3 = A.type :+: B.type :+: C.type :+: CNil

  object FoldT1toT3 extends Poly1 {
    val to3             = Coproduct[T3]
    implicit val case_a = at[A.type] { a: A.type => to3(a) }
    implicit val case_b = at[B.type] { b: B.type => to3(b) }
  }

  object FoldT2toT3 extends Poly1 {
    val to              = Coproduct[T3]
    implicit val case_b = at[B.type](to(_))
    implicit val case_c = at[C.type](to(_))
  }

  def foldT1toT3(t1: T1): T3 = t1.fold(FoldT1toT3)
  def foldT2toT3(t2: T2): T3 = t2.fold(FoldT2toT3)

  val b1: T1 = Coproduct[T1](B)
  val b2: T2 = Coproduct[T2](B)

  val b3a: T3 = foldT1toT3(b1)
  val b3b: T3 = foldT2toT3(b2)

}

object CombiningCoproducts2 {

  case object A
  case object B
  case object C
  case object D
  case object E
  case object F

  sealed trait G
  case object G1 extends G
  case object G2 extends G

  type T1 = G :+: A.type :+: B.type :+: CNil
  type T2 = C.type :+: D.type :+: CNil
  type T3 = T1 :+: T2 :+: E.type :+: F.type :+: CNil

  val t1t2 = Prepend[T1, T2]

  val a: T1                              = Coproduct[T1](A)
  val xx: G :+: A.type :+: B.type :+: T2 = t1t2.apply(a.asLeft)

  val a1: T1 = Coproduct[T1](G1)
  val a2: T1 = Coproduct[T1](G2)
  //    val aa: T3 = Coproduct[T3](A)

  val b: T1                              = Coproduct[T1](B)
  val g1: T1                             = Coproduct[T1](G1)
  val g2: T1                             = Coproduct[T1](G2)
  val extend                             = ExtendBy[T1, T2]
  val c: T2                              = Coproduct[T2](C)
  val e2: G :+: A.type :+: B.type :+: T2 = extend.left(c)
  val yy: G :+: A.type :+: B.type :+: T2 = t1t2.apply(c.asRight)
  val d: T2                              = Coproduct[T2](D)
  val e: T3                              = Coproduct[T3](E)
  val f: T3                              = Coproduct[T3](F)

  val tx: G :+: A.type :+: B.type :+: T2 = ExtendLeftBy[T1, T2].apply(c)

  Remove[T1, B.type].apply(a1)

  val z: Aux[T1, Int, G :+: CombiningCoproducts2.A.type :+: CombiningCoproducts2.B.type :+: Int :+: CNil] = ExtendRight[T1, Int]
  val z1: G :+: A.type :+: B.type :+: Int :+: CNil                                                        = z.apply(a)

}

class CoproductNestedSpec extends AnyFunSuite with Matchers {

  import CoproductNested._

  test("plain unified - OK") {
    pprint.pprintln(t1a.unify) // A
    pprint.pprintln(t1b.unify) // B
    pprint.pprintln(t2c.unify) // C
  }

  test("plain non-unified - OK") {
    pprint.pprintln(t1a) // Inl(head = A)
    pprint.pprintln(t1b) // Inr(tail = Inl(head = B))
    pprint.pprintln(t2c) // Inr(tail = Inl(head = C))
  }

  test("nested non-unified") {
    pprint.pprintln(t2a) // Inl(head = Inl(head = A))
    pprint.pprintln(t2b) // Inl(head = Inr(tail = Inl(head = B)))
  }

  implicit class ExtractNestedCoproduct(x: Any) {
    def doUnify(x0: Any): Any = x0 match {
      case Inl(x) => doUnify(x)
      case Inr(x) => x
      case x      => x
    }
    def unify0: Any           = doUnify(x)
  }

  test("nested unified in a custom way") {
    pprint.pprintln(t2a)              // Inl(head = Inl(head = A))
    pprint.pprintln(t2a.unify)        // Inl(head = A)
    pprint.pprintln(t2a.unify.unify0) // A
    pprint.pprintln(t2a.unify0)       // A
  }

  test("unified (shapeless) pattern match - type is preserved during unification") {
    t1a.unify match {
      case _: A.type => println("A!")
      case _         => println("other...")
    }
  }

  test("unified (recursion) pattern match - type is preserved during unification") {
    t1a.unify0 match {
      case _: A.type => println("A!")
      case _         => println("other...")
    }
  }

  test("nested unified in a custom way 1") {
    val x: Option[C.type]  = t2a.select[C.type]
    val ao: Option[A.type] = t2a.select[T1].flatMap(_.select[A.type])

    pprint.pprintln(x)
    pprint.pprintln(ao)
  }

  test("toString") {
    pprint.pprintln(s1)
    pprint.pprintln(s2)
    pprint.pprintln(s3)
    1 shouldEqual 1
  }
}
