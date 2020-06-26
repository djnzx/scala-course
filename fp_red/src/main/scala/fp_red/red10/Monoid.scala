package fp_red.red10

trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

object MonoidLaws {
  
  import fp_red.red08.Prop._
  import fp_red.red08.{Gen, Prop}

  /**
    * Prop is a function which needs to be run
    * and Result will be provided
    */
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
  /**
    * is sequence ordered in terms of foldMapV
    */
  def isOrdered(ints: IndexedSeq[Int]): Boolean = {
    type IIB = Option[(Int, Int, Boolean)]

    // we need to define a function from Int => B
    //                             min, max of our range
    val f: Int => IIB = i => Some(( i,   i, true))
    
    // and define monoid for IIB
    val miib: Monoid[IIB] = new Monoid[IIB] {
      override def op(a1: IIB, a2: IIB): IIB = (a1, a2) match {
        case (Some((lmin, lmax, l)), Some((rmin, rmax, r))) => Some(
          (lmin min rmin, lmax max rmax, l && r && lmax <= rmin)
        )
        // strictly saying we never get this result because of foldMapV implementation,
        case (None, None) => None
        case _ => a1 orElse a2
      }
      override def zero: IIB = None
    }

    val folded: IIB = foldMapV(ints, miib) { f }
//    folded.map(_._3).getOrElse(true)
    folded.forall(_._3)
  }
  
  import fp_red.c_answers.c07parallelism.Nonblocking.Par.toParOps
  import fp_red.c_answers.c07parallelism.Nonblocking._

  /**
    * having Monoid[A]
    * and Par[A]
    * we can implement Monoid[Par[A]]
    */
  def par[A](m: Monoid[A]): Monoid[Par[A]] = new Monoid[Par[A]] {
    override def op(a1: Par[A], a2: Par[A]): Par[A] = Par.map2(a1, a2)(m.op)
    override def zero: Par[A] = Par.unit(m.zero)
  }

  /** trick to conform signature only (lifter) */
  def parFoldMap0[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] =
    Par.unit(v.map(f).foldLeft(m.zero)(m.op))

  def parFoldMap1[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] = {
    val mparb: Monoid[Par[B]] = par(m)
    
    // mapping sequentially, but reducing parallel
    foldMapV(v, mparb) { a: A =>
      val b: B = f(a)  
      val pb: Par[B] = Par.lazyUnit(b)
      pb
    }
  }

  // we perform the mapping and the reducing both in parallel
  def parFoldMapExplained[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] = {
    
    // mapping done in parallel
    val parbs: Par[IndexedSeq[B]] = Par.parMap(as)(f)
    
    // obtaining monoid for Par[B]
    val mparb: Monoid[Par[B]] = par(m)
    
    // function to lift X => Parx[X] for further parallel folding
    def lazyPar[X](x: X): Par[X] = Par.lazyUnit(x)
    
    // reducing in parallel by monoid
    val pb: Par[B] = parbs.flatMap { bs: IndexedSeq[B] =>
      foldMapV(bs, mparb) { b: B => lazyPar(b) }
    }

    pb
  }
  
  // we perform the mapping and the reducing both in parallel
  // type Par[A] = ExecutorService => Future[A]
  def parFoldMap[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] =
      Par.parMap(as)(f)
        .flatMap { foldMapV(_, par(m)) { Par.lazyUnit(_) } }

  sealed trait WC
  case class Stub(chars: String) extends WC
  case class Part(lStub: String, words: Int, rStub: String) extends WC

  val wcMonoid: Monoid[WC] = new Monoid[WC] {
    val zero: WC = Stub("")

    override def op(a1: WC, a2: WC): WC = (a1, a2) match {
      case (Stub(s1), Stub(s2)) => Stub(s1 + s2)
      case (Stub(s1), Part(l, n, r)) => Part(s1 + l, n, r)
      case (Part(l, n, r), Stub(s2)) => Part(l, n, r + s2)
      case (Part(l1, n1, r1), Part(l2, n2, r2)) => Part(l1, n1 + n2 + (if ((r1+l2).isEmpty) 0 else 1), r2)
    }
  }

  // function to build WC based on the value
  def wc(c: Char): WC =
    if (c.isWhitespace) Part("", 0, "")
    else                Stub(c.toString)

  def unstub(s: String): Int = s.length min 1

  // count via monoid balanced, can be run in parallel
  def count(s: String): Int = {

    val r: WC = foldMapV(s.toIndexedSeq, wcMonoid)(wc)
    
    r match {
      case Stub(s) => unstub(s)
      case Part(l, w, r) => unstub(l) + w + unstub(r)
    }
  }

  /**
    * homomorphism - preserve structure
    * M.op(f(x), f(y)) == f(N.op(x, y))
    */

  def productMonoid[A,B](A: Monoid[A], B: Monoid[B]): Monoid[(A, B)] = new Monoid[(A, B)] {
    override def op(a1: (A, B), a2: (A, B)): (A, B) = (a1, a2) match {
      case ((a1, b1), (a2, b2)) => (A.op(a1, a2), B.op(b1, b2))
    }  
    val zero: (A, B) = (A.zero, B.zero)
  }

  def functionMonoid[A, B](B: Monoid[B]): Monoid[A => B] = new Monoid[A => B] {
    override def op(f1: A => B, f2: A => B) = a => B.op(f1(a), f2(a))
    val zero: A => B = _ => B.zero
  }

  def mapMergeMonoid[K, V](vm: Monoid[V]): Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    override def op(m1: Map[K, V], m2: Map[K, V]): Map[K, V] = 
      (m1.keySet ++ m2.keySet).foldLeft(zero) { (map, k) => 
        val v1 = m1.getOrElse(k, vm.zero)
        val v2 = m2.getOrElse(k, vm.zero)
        map.updated(k, vm.op(v1, v2))
      }
    val zero: Map[K, V] = Map.empty
  }

  /**
    * balanced collect:
    * via foldMapV by using mapMergeMonoid
    */
  def bag[A](as: IndexedSeq[A]): Map[A, Int] =
    foldMapV(
      as,                                  // given collection
      mapMergeMonoid[A, Int](intAddition)  // map merger
    ) { a: A =>                            // for each element 
      Map(a -> 1)                          // we create element Map(k->1)
    }

  /**
    * balanced parallel collect:
    * via parFoldMap via foldMapV by using mapMergeMonoid
    */
  def parBag[A](as: IndexedSeq[A]): Par[Map[A, Int]] =
    parFoldMap(as, mapMergeMonoid[A, Int](intAddition)) { a => Map(a -> 1)}
}

trait Foldable[F[_]] {
  import Monoid.{dual, endoMonoid}

  type ABB[A, B] = (A, B) => B
  type BAB[A, B] = (B, A) => B
  type ABBC[A, B] = A => B => B
  
  def ABBtoABBc[A, B](f_ab_b: ABB[A, B]): ABBC[A, B] = a => b => f_ab_b(a, b) 
  def BABtoABBc[A, B](f_ba_b: BAB[A, B]): ABBC[A, B] = a => b => f_ba_b(b, a) 
  
  /**
    * foldRight via foldMap
    */
  def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B =
    foldMap(as)(ABBtoABBc(f))(endoMonoid[B])(z)

  /**
    * foldLeft via foldMap
    */
  def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B =
    foldMap(as)(BABtoABBc(f))(dual(endoMonoid[B]))(z)

  /**
    * foldMap via foldRight
    */
  def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B =
    foldRight(as)(mb.zero)((a, b) => mb.op(f(a), b))

  /**
    * concatenate via foldLeft
    */
  def concatenate[A](as: F[A])(m: Monoid[A]): A =
    foldLeft(as)(m.zero)(m.op)

  /**
    * toList via foldRight 
    */
  def toList[A](as: F[A]): List[A] =
    foldRight(as)(List.empty[A])(_ :: _)

}

object ListFoldable extends Foldable[List] {
  override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B) =
    as.foldRight(z)(f)
  override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B) =
    as.foldLeft(z)(f)
  override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B =
    foldLeft(as)(mb.zero) { (b, a) => mb.op(b, f(a)) }
  override def toList[A](as: List[A]): List[A] = as
}

object IndexedSeqFoldable extends Foldable[IndexedSeq] {
  import Monoid._
  
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

object OptionFoldable extends Foldable[Option] {
  override def foldMap[A, B](as: Option[A])(f: A => B)(mb: Monoid[B]) = as match {
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

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

/**
  * we haven't used `zero` from the `Monoid`
  * This is because there is no empty tree.
  * 
  * actually, we needed semigroup, not a monoid
  */
object TreeFoldable extends Foldable[Tree] {
  override def foldMap[A, B](as: Tree[A])(f: A => B)(mb: Monoid[B]) = as match {
    case Leaf(a) => f(a)
    case Branch(l, r) =>
      mb.op(
        foldMap(l)(f)(mb),
        foldMap(r)(f)(mb)
      )
  }
  override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B) = as match {
    case Leaf(a) => f(z, a)
    case Branch(l, r) =>
      val lb = foldLeft(l)(z)(f)
      foldLeft(r)(lb)(f)
  }
  override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B) = as match {
    case Leaf(a) => f(a, z)
    case Branch(l, r) =>
      val rb = foldRight(r)(z)(f)
      foldRight(l)(rb)(f)
  }
}

