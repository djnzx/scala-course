package fp_red.red10


object MonoidApp extends App {
  
  /**
    * Monoid - category with one object
    * 
    * 1. type
    * 2. binary operation
    * 3. identity element
    * 4. associativity law
    * 
    */

  trait Monoid[A] {
    def op(a1: A, a2: A): A
    def zero: A
  }

  val stringConcat: Monoid[String] = new Monoid[String] {
    override def op(a1: String, a2: String): String = s"$a1$a2"
    override def zero: String = ""
  }

  val intAdd: Monoid[Int] = new Monoid[Int] {
    override def op(a1: Int, a2: Int): Int = a1 + a2
    override def zero: Int = 0
  }

  val intMul: Monoid[Int] = new Monoid[Int] {
    override def op(a1: Int, a2: Int): Int = a1 * a2
    override def zero: Int = 1
  }

  def listConcat[A]: Monoid[List[A]] = new Monoid[List[A]] {
    override def op(a1: List[A], a2: List[A]): List[A] = a1 ::: a2
    override def zero: List[A] = Nil
  }

  val boolAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    override def zero: Boolean = true
  }

  val boolOr: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a1: Boolean, a2: Boolean): Boolean = a1 || a2
    override def zero: Boolean = false
  }

  // Monoid, but order matters
  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def op(x: Option[A], y: Option[A]): Option[A] = x orElse y
    val zero: Option[A] = None
  }

  def endoMonoidAndThen[A]: Monoid[A => A] = new Monoid[A => A] {
    def op(f: A => A, g: A => A): A => A = f andThen g
    val zero: A => A = identity
  }

  def endoMonoidCompose[A]: Monoid[A => A] = new Monoid[A => A] {
    def op(f: A => A, g: A => A): A => A = f compose g
    val zero: A => A = identity
  }

  def foldLeft[A, B](z: B)(f: (A, B) => B): B = ???
  def foldRight[A, B](z: B)(f: (A, B) => B): B = ???

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = ???

  import fp_red.red08.{Gen, Prop}
  import fp_red.red08.Prop._
  def monoidLaws[A](m: Monoid[A], gen: Gen[A]): Prop = {

    val data = for {
      x <- gen
      y <- gen
      z <- gen
    } yield (x, y, z)

    forAll(data) { p: (A, A, A) =>
      m.op(p._1, m.op(p._2, p._3)) == m.op(m.op(p._1, p._2), p._3)
    } &&
      forAll(gen) { a: A =>
        m.op(a, m.zero) == a && m.op(m.zero, a) == a
      }

  }

  // balanced fold allows parallelism

  // it will produce extra list, so 2 passes
  def foldMap1[A, B](as: List[A], m:Monoid[B])(f: A => B): B =
    as.map(f(_)).foldLeft(m.zero)(m.op)

  // it will fold in one pass
  def foldMap2[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.foldLeft(m.zero)((b, a) => m.op(b, f(a)))

  // TODO: repeat
  def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B) =
    foldMap(as, endoMonoidCompose[B])(f.curried)

  def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B) =
    foldMap(as, endoMonoidCompose[B])(a => b => f(b, a))

  sealed trait WC
  case class Stub(chars: String) extends WC
  case class Part(lStub: String, words: Int, rStub: String) extends WC
  // homomorphism - preserve structure
  //    M.op(f(x), f(y)) == f(N.op(x, y))

  // EX.7
  def foldMapV[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): B = as.length match {
    case 0 => m.zero
    case 1 => f(as(0))
    case _ =>
      val (l, r) = as.splitAt(as.length / 2)
      m.op(
        foldMapV(l, m)(f),
        foldMapV(r, m)(f)
      )
  }
  
  // EX.8
  def isOrdered(ints: IndexedSeq[Int]): Boolean = {
    val mon = new Monoid[Option[(Int, Int, Boolean)]] {
      def op(o1: Option[(Int, Int, Boolean)], o2: Option[(Int, Int, Boolean)]) =
        (o1, o2) match {
          case (Some((lower1, upper1, p)), Some((lower2, upper2, q))) =>
            Some((lower1 min lower2, upper1 max upper2, p && q && upper1 <= lower2))
          case (x, None) => x
          case (None, x) => x
        }
      val zero = None
    }
    
    foldMapV(ints, mon) { i=> 
      Some((i, i, true))
    }.map(_._3).getOrElse(true)
  }

  // we don't care about F[_]
  trait Foldable[F[_]] {
    def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B
    def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B
    def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B
    def concatenate[A](as: F[A])(m: Monoid[A]): A = foldLeft(as)(m.zero)(m.op)
  }

  def mapMergeMonoid[K, V](V: Monoid[V]): Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    def zero: Map[K, V] = Map[K, V]()
    def op(a: Map[K, V], b: Map[K, V]): Map[K, V] =
      (a.keySet ++ b.keySet).foldLeft(zero) { (acc, k) =>
        acc.updated(k, V.op(
          a.getOrElse(k, V.zero),
          b.getOrElse(k, V.zero)
        ))
      }
  }

  val M: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAdd))
  val m1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2))
  val m2 = Map("o1" -> Map("i2" -> 3))
  val m3 = M.op(m1, m2)

//  val m = intMul(intAdd, intAdd)
//  val p = listFoldable.foldMap(List(1,2,3,4))(a => (1, a))(m)
//  val mean = p._1 / p._2.toDouble
  
}
