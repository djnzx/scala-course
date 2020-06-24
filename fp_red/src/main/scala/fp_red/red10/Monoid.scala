package fp_red.red10

trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

object Monoid {
  
  /**
    * Monoid - category with one object
    * 
    * 1. type
    * 2. binary operation
    * 3. identity element
    * 4. associativity law
    * 
    * Semigroup - Monoid without zero
    * 
    */

  val stringConcat: Monoid[String] = new Monoid[String] {
    override def op(a1: String, a2: String): String = s"$a1$a2"
    override def zero: String = ""
  }
  
  val intAddition: Monoid[Int] = new Monoid[Int] {
    def op(x: Int, y: Int): Int = x + y
    val zero = 0
  }

  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    def op(x: Int, y: Int): Int = x * y
    val zero = 1
  }

  val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    def op(x: Boolean, y: Boolean): Boolean = x || y
    val zero = false
  }

  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    def op(x: Boolean, y: Boolean): Boolean = x && y
    val zero = true
  }  
  
  def listConcat[A]: Monoid[List[A]] = new Monoid[List[A]] {
    override def op(a1: List[A], a2: List[A]): List[A] = a1 ::: a2
    override def zero: List[A] = Nil
  }

  // left or right
  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    override def op(a1: Option[A], a2: Option[A]): Option[A] = a1 orElse a2
    override def zero: Option[A] = None
  }
  
  // right or left
  def optionMonoidR[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    override def op(a1: Option[A], a2: Option[A]): Option[A] = a2 orElse a1
    override def zero: Option[A] = None
  }
  
  // different implementation supposed A is also monoid
  def optionMonoidBoth[A: Monoid]: Monoid[Option[A]] = new Monoid[Option[A]] {
    override def op(a1: Option[A], a2: Option[A]): Option[A] = (a1, a2) match {
      case (None, None) => None
      case (Some(_), None) => a1
      case (None, Some(_)) => a2
      case (Some(a1v), Some(a2v)) => Some(implicitly[Monoid[A]].op(a1v, a2v))
    }
    override def zero: Option[A] = None
  }

  /**
    * We can get the dual of any monoid just by flipping the `op`.
    * swap the order 
    */
  def dual[A](m: Monoid[A]): Monoid[A] = new Monoid[A] {
    def op(x: A, y: A): A = m.op(y, x)
    val zero: A = m.zero
  }

  def endoMonoid[A]: Monoid[A => A] = new Monoid[A => A] {
    def op(f: A => A, g: A => A): A => A = f compose g
    val zero: A => A = identity
  }

  def endoMonoidAndThen[A]: Monoid[A => A] = new Monoid[A => A] {
    def op(f: A => A, g: A => A): A => A = f andThen g
    val zero: A => A = identity
  }

  import fp_red.red08.{Gen, Prop}
  import fp_red.red08.Prop._
  def monoidLaws[A](m: Monoid[A], gen: Gen[A]): Prop = {
    case class T3(a: A, b: A ,c: A)

    val data: Gen[T3] = for {
      a <- gen
      b <- gen
      c <- gen
    } yield T3(a,b,c)

    forAll(data) { p: T3 =>
      import p._
      m.op(a, m.op(b, c)) == m.op(m.op(a, b), c)
    } &&
      forAll(gen) { a: A =>
        m.op(a, m.zero) == a && m.op(m.zero, a) == a
      }
  }

  // Now we can have both monoids on hand:
  def firstOptionMonoid[A]: Monoid[Option[A]] = optionMonoid[A]
  def lastOptionMonoid[A]: Monoid[Option[A]] = dual(firstOptionMonoid)

  def trimMonoid(s: String): Monoid[String] = ???

  def concatenate[A](as: List[A], m: Monoid[A]): A =
    as.foldLeft(m.zero) { (acc, a) => m.op(acc, a) }

  // it will produce extra list, so 2 passes
  def foldMap1[A, B](as: List[A], m:Monoid[B])(f: A => B): B =
    as.map(f(_))
      .foldLeft(m.zero)(m.op)

  // it will fold in one pass
  def foldMap2[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.foldLeft(m.zero)((b, a) => m.op(b, f(a)))

  // fold with map in one pass
  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.foldLeft(m.zero) { (acc, a) => m.op(acc, f(a)) }

  /** foldRight - native,
    * 1 pass
    */
  def foldRight2[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    as.foldRight(z)(f)
  
  /** foldRight - via mapping List[A] and f(A,B)=>B to List[B => B],
    * 2 passes
    */
  def foldRight3[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
    type BB = B => B
    val fc: A => BB = f.curried
    // 1-st pass
    val lbb: List[BB] = as.map(fc)
    // 2-nd pass
    val folded: BB = lbb.foldLeft(identity[B] _) { (f1: BB, f2: BB) => f1 compose f2 }
    // final result
    folded(z)
  }

  /** foldRight - via mapping List[A] and f(A, B) => B to List[B => B],
    * 1 pass
    */
  def foldRight4[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
    val fc: A => B => B = f.curried
    val ebb: Monoid[B => B] = endoMonoid[B]
    // 1-st pass
    val folded: B => B = as.foldLeft(ebb.zero) { (fbb, a) => ebb.op(fbb, fc(a)) }
    // final result
    folded(z)
  }

  /** foldRight - via foldMap (mapping List[A] and f(A, B) => B to List[B=>B]),
    * 1 pass
    */
  def foldRight5[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
    val fc: A => B => B = f.curried
    val ebb: Monoid[B => B] = endoMonoid[B]
    // 1-st pass
    val folded: B => B = foldMap(as, ebb) { a: A => fc(a) }
    // final result
    folded(z)
  }

  /**
    * foldRight via foldMap 
    * endoMonoid:
    * f: (A, B) => B
    * curried, will be 
    * A => (B => B)
    *
    * essentially, by having function (A, B) => B
    * and List[A]
    * we partially apply our function f(A, B) => B (curried) to each element
    * and get List[B => B] 
    *
    * essentially, we build a function f(f(f(...f(z)...)))
    */
  def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    foldMap(as, endoMonoid[B])(f.curried) (z)

  /**
    * foldLeft via foldMap
    * the core idea is to flip the function
    * f: (B, A) => B
    * to
    * f: (A, B) => B
    * by applying approach: (a: A) => (b: B) => f(b, a)
    * and converting endoMonoid to dual(endoMonoid)
    * because we need andThan instead of compose
    */
  def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B =
    foldMap(as, dual(endoMonoid[B])) { a: A => b: B => f(b, a) } (z)

  /**
    * balanced fold allows parallelism,
    * but we need an IndexedSeq
    */
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
    type IIB = (Int, Int, Boolean)
    
    val mon = new Monoid[Option[IIB]] {
      def op(o1: Option[IIB], o2: Option[IIB]) =
        (o1, o2) match {
          case (Some((lower1, upper1, p)), Some((lower2, upper2, q))) =>
            Some((lower1 min lower2, upper1 max upper2, p && q && upper1 <= lower2))
          case (x, None) => x
          case (None, x) => x
        }
      val zero: Option[IIB] = None
    }
    
    val f: Int => Option[IIB] = (i: Int) => Some((i, i, true))
    
    val r: Option[IIB] = foldMapV(ints, mon)(f)
    r.map(_._3).getOrElse(true)
  }

  import fp_red.c_answers.c07parallelism.Nonblocking._
  import fp_red.c_answers.c07parallelism.Nonblocking.Par.toParOps

  def par[A](m: Monoid[A]): Monoid[Par[A]] = new Monoid[Par[A]] {
    def zero = Par.unit(m.zero)
    def op(a: Par[A], b: Par[A]) = a.map2(b)(m.op)
  }

  // we perform the mapping and the reducing both in parallel
  def parFoldMap[A,B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] =
    Par.parMap(v)(f).flatMap { bs =>
      foldMapV(bs, par(m))(b => Par.lazyUnit(b))
    }

  // EX. 10
  sealed trait WC
  case class Stub(chars: String) extends WC
  case class Part(lStub: String, words: Int, rStub: String) extends WC

  val wcMonoid: Monoid[WC] = new Monoid[WC] {
    val zero = Stub("")

    def op(a: WC, b: WC) = (a, b) match {
      case (Stub(c), Stub(d)) => Stub(c + d)
      case (Stub(c), Part(l, w, r)) => Part(c + l, w, r)
      case (Part(l, w, r), Stub(c)) => Part(l, w, r + c)
      case (Part(l1, w1, r1), Part(l2, w2, r2)) =>
        Part(l1, w1 + (if ((r1 + l2).isEmpty) 0 else 1) + w2, r2)
    }
  }

  // EX. 11
  def count(s: String): Int = {
    def wc(c: Char): WC =
      if (c.isWhitespace)
        Part("", 0, "")
      else
        Stub(c.toString)
    def unstub(s: String) = s.length min 1
    foldMapV(s.toIndexedSeq, wcMonoid)(wc) match {
      case Stub(s) => unstub(s)
      case Part(l, w, r) => unstub(l) + w + unstub(r)
    }
  }
  // homomorphism - preserve structure
  //    M.op(f(x), f(y)) == f(N.op(x, y))


  // we don't care about F[_]
  trait Foldable[F[_]] {
    def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B = 
      foldMap(as)(f.curried)(endoMonoid[B])(z)
    def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B = 
      foldMap(as)(a => (b: B) => f(b, a))(dual(endoMonoid[B]))(z)
    def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B = 
      foldRight(as)(mb.zero)((a, b) => mb.op(f(a), b))
    def concatenate[A](as: F[A])(m: Monoid[A]): A = 
      foldLeft(as)(m.zero)(m.op)
  }
  
  // EX.12
  object ListFoldable extends Foldable[List] {
    override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B) =
      as.foldRight(z)(f)
    override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B) =
      as.foldLeft(z)(f)
    override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B =
      foldLeft(as)(mb.zero)((b, a) => mb.op(b, f(a)))
  }

  object IndexedSeqFoldable extends Foldable[IndexedSeq] {
    override def foldRight[A, B](as: IndexedSeq[A])(z: B)(f: (A, B) => B) =
      as.foldRight(z)(f)
    override def foldLeft[A, B](as: IndexedSeq[A])(z: B)(f: (B, A) => B) =
      as.foldLeft(z)(f)
    override def foldMap[A, B](as: IndexedSeq[A])(f: A => B)(mb: Monoid[B]): B =
      foldMapV(as, mb)(f)
  }

  object StreamFoldable extends Foldable[Stream] {
    override def foldRight[A, B](as: Stream[A])(z: B)(f: (A, B) => B) =
      as.foldRight(z)(f)
    override def foldLeft[A, B](as: Stream[A])(z: B)(f: (B, A) => B) =
      as.foldLeft(z)(f)
  }
  
  // EX. 13
  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  object TreeFoldable extends Foldable[Tree] {
    override def foldMap[A, B](as: Tree[A])(f: A => B)(mb: Monoid[B]): B = as match {
      case Leaf(a) => f(a)
      case Branch(l, r) => mb.op(foldMap(l)(f)(mb), foldMap(r)(f)(mb))
    }
    override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B) = as match {
      case Leaf(a) => f(z, a)
      case Branch(l, r) => foldLeft(r)(foldLeft(l)(z)(f))(f)
    }
    override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B) = as match {
      case Leaf(a) => f(a, z)
      case Branch(l, r) => foldRight(l)(foldRight(r)(z)(f))(f)
    }
  }

  // EX.14
  object OptionFoldable extends Foldable[Option] {
    override def foldMap[A, B](as: Option[A])(f: A => B)(mb: Monoid[B]): B =
      as match {
        case None => mb.zero
        case Some(a) => f(a)
      }
    override def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B) = as match {
      case None => z
      case Some(a) => f(z, a)
    }
    override def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B) = as match {
      case None => z
      case Some(a) => f(a, z)
    }
  }
  
  // EX. 15
  //  def toList[F[_]: List, A](as: F[A]): List[A] = foldRight(as)(List[A]())(_ :: _)

  // EX. 16
  def productMonoid[A,B](A: Monoid[A], B: Monoid[B]): Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def op(x: (A, B), y: (A, B)) =
        (A.op(x._1, y._1), B.op(x._2, y._2))
      val zero = (A.zero, B.zero)
    }

  // EX 17
  def functionMonoid[A, B](B: Monoid[B]): Monoid[A => B] =
    new Monoid[A => B] {
      def op(f: A => B, g: A => B) = a => B.op(f(a), g(a))
      val zero: A => B = _ => B.zero
    }
  
  // EX. 18
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

  def bag[A](as: IndexedSeq[A]): Map[A, Int] =
    foldMapV(as, mapMergeMonoid[A, Int](intAddition))((a: A) => Map(a -> 1))


  val M: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))
  val m1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2))
  val m2 = Map("o1" -> Map("i2" -> 3))
  val m3 = M.op(m1, m2)

}
