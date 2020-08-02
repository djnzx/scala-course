package fp_red.red06

import scala.annotation.tailrec

trait RNG {
  /**
    * Should generate a random `Int`.
    * But returns not only the value, but also a new state
    */
  def nextInt: (Int, RNG) 
}

object RNG {

  /**
    * next long value based on previous one
    */
  def next(prev: Long): Long =
    (prev * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
  
  def toInt(long: Long): Int = (long >>> 16).toInt
  
  /**
    * the simple implementation
    */
  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = next(seed)
      (toInt(newSeed), Simple(newSeed))
    }
  }

  /** We need to be quite careful not to skew the generator.
    * Since `Int.Minvalue` is 1 smaller than `-(Int.MaxValue)`,
    * it suffices to increment the negative numbers by 1 and make them positive.
    * This maps Int.MinValue to Int.MaxValue and -1 to 0.
    */
  def rectify(x: Int): Int = if (x < 0) -(x + 1) else x

  /**
    * `nonNegativeInt` plain chaining
    */
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    val nonNeg = rectify(i)
    (nonNeg, r)
  }

  /**
    * one possible solution
    */
  def double(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    val d = i / (Int.MaxValue.toDouble + 1)
    (d, r)
  }

  def boolean(rng: RNG): (Boolean, RNG) =
    rng.nextInt match { case (i, rng2) => (i%2 == 0, rng2) }

  /**
    * via already defined `nextInt` and `double`
    */
  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r1) = rng.nextInt
    val (d, r2) = double(r1)
    ((i, d), r2)
  }

  /**
    * via already defined `intDouble`
    */
  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), r) = intDouble(rng)
    ((d, i), r)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)
  }

  /**
    * list of ints - recursive solution
    */
  def ints(count: Int)(rng: RNG): (List[Int], RNG) =
    if (count == 0)
      (List(), rng)
    else {
      val (x, r1)  = rng.nextInt
      val (xs, r2) = ints(count - 1)(r1)
      (x :: xs, r2)
    }

  /**
    * list of ints - tail recursive solution
    */
  def ints2(count: Int)(rng: RNG): (List[Int], RNG) = {
    
    @tailrec
    def go(count: Int, r: RNG, xs: List[Int]): (List[Int], RNG) =
      if (count == 0)
        (xs, r)
      else {
        val (x, r2) = r.nextInt
        go(count - 1, r2, x :: xs)
      }
      
    go(count, rng, List.empty)
  }
  
  def fiveInts: RNG => (List[Int], RNG) = ints2(5)
  
  /**
    * let's introduce more general representation:
    * Rand[+A] = RNG => (A, RNG).
    * RNG will be out `state`
    */
  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  /**
    * just lift the value `a` to RNG => (a, RNG).
    * Whatever RNG given - produce the result `a`
    */
  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    r0 => {
      val (a, rng2): (A, RNG) = s(r0)
      val b: B = f(a)
      (b, rng2)
    }

  /**
    * `double` via `nonNegativeInt` in terms of `map`
    */
  val _double: Rand[Double] =
    map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))

  /**
    * This implementation of map2 passes the initial RNG to the first argument
    * and the resulting RNG to the second argument. It's not necessarily wrong
    * to do this the other way around, since the results are random anyway.
    * We could even pass the initial RNG to both `f` and `g`, but that might
    * have unexpected results. E.g. if both arguments are `RNG.int` then we would
    * always get two of the same `Int` in the result. When implementing functions
    * like this, it's important to consider how we would test them for
    * correctness. 
    */
  def map2[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    r0 => {
      val (a, r1): (A, RNG) = ra(r0)
      val (b, r2): (B, RNG) = rb(r1)
      val c: C = f(a, b)
      (c, r2)
    }

  /**
    * both, actually a `product` via `map2`
    */
  def both[A,B](ra: Rand[A], rb: Rand[B]): Rand[(A,B)] =
    map2(ra, rb)((_, _))

  val randIntDouble: Rand[(Int, Double)] =
    both(int, double)

  val randDoubleInt: Rand[(Double, Int)] =
    both(double, int)

  /**
    * `sequence` - plain recursive solution
    */
  def sequenceR[A](fs: List[Rand[A]]): Rand[List[A]] = s0 => fs match {
    case Nil  => (Nil, s0)
    case h::t =>
      // apply function from head to initial state
      val (a: A, s2: RNG) = h(s0)
      // recursively get the tail
      val fx: Rand[List[A]] = sequenceR(t)
      val (la: List[A], s3: RNG) = fx(s2)
      // join
      (a::la, s3)
  }

  /**
    * `sequence` - tail recursive solution
    */
  def sequenceTR[A](fs: List[Rand[A]]): Rand[List[A]] = {

    @tailrec
    def go(tail: List[Rand[A]], acc: List[A], s: RNG): (List[A], RNG) = tail match {
      case Nil  => (acc.reverse, s)
      case h::t => h(s) match {
        case (a, s2) => go(t, a::acc, s2)
      }
    }
    
    s0 => go(fs, List.empty, s0)
  }
  
  /**
    * `sequence` in terms of `foldRight` and `map2`
    */
  def sequence_fr[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight(unit(List.empty[A])) {
      (f: Rand[A], acc: Rand[List[A]]) => map2(f, acc)(_ :: _)
    }

  /**
    * `sequence` in terms of `map2` (recursive)
    */
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = fs match {
    case Nil  => s => (Nil, s)
    case h::t => map2(h, sequence(t))(_ :: _)
  }
  
  /**
    * `ints` via `sequence`
    */
  def ints_seq(count: Int): Rand[List[Int]] =
    sequence(List.fill(count)(int))

  /**
    * flatMap, chaining
    */
  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = r0 =>
    f(r0) match {
      case (a, r2) => g(a)(r2)
    }

  /**
    * `map` via `flatMap`
    */
  def map_fm[A,B](s: Rand[A])(f: A => B): Rand[B] =
    flatMap(s)(a => unit(f(a)))

  /**
    * `map2` via `flatMap`
    */
  def map2_fm[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    flatMap(ra)(a => map(rb)(b => f(a, b)))

  /**
    * `nonNegativeInt` via `map`
    */
  def nonNegInt: Rand[Int] = map(int)(rectify)

  /**
    * `nonNegIntEven` via `nonNegInt`
    */
  def nonNegIntEven: Rand[Int] = map(nonNegInt)(n => n + n % 2)

  /**
    * `nonNegIntLessThan` via `nonNegInt` and `map`
    */
  def nonNegIntLessThan(n: Int): Rand[Int] = map(nonNegInt)(_ % n)

  /**
    * `nonNegIntLessThan` via `nonNegInt` and `flatMap`
    */
  def nonNegIntLessThan_fm(n: Int): Rand[Int] = flatMap(nonNegInt)(x => unit(x % n))

  def plus1(n: Int): Int = n + 1
  def plus(a: Int, b: Int): Int = a + b
  val zeroToFive: Rand[Int] = nonNegIntLessThan(6)
  /**
    * `rollDie` via `nonNegativeLessThan` in terms of `map`, `map2`
    */
  def rollDie : Rand[Int] = map(zeroToFive)(_ + 1)
  def rollDie1: Rand[Int] = map(zeroToFive)(plus1)
  def rollDie2: Rand[Int] = map2(zeroToFive, unit(1))(_ + _)
  def rollDie3: Rand[Int] = map2(zeroToFive, unit(1))(plus)
  def rollDie4: Rand[Int] = flatMap(zeroToFive)(x => unit(plus1(x)))

  /**
    * List of length N filled by rule - Scala solution based on ListBuffer
    */
  def timesNscala[A](n: Int, rule: Rand[A]): List[Rand[A]] =
    List.fill(n)(rule)

  /**
    * List of length N filled by rule - recursive solution
    */
  def timesNrec[A](n: Int, rule: Rand[A]): List[Rand[A]] = {
    def go(x: Int): List[Rand[A]] = x match {
      case 0 => Nil
      case _ => rule :: go(x - 1)
    }
    
    go(n)
  }
  
  /**
    * List of length N filled by rule - tail recursive solution
    */
  def timesNtr[A](n: Int, rule: Rand[A]): List[Rand[A]] = {
    @tailrec
    def go(x: Int, acc: List[Rand[A]]): List[Rand[A]] = x match {
      case 0 => acc.reverse
      case _ => go(x - 1, rule :: acc)
    }
    
    go(n, List.empty)
  }
  
  def listOfN[A](n: Int)(rule: Rand[A]): Rand[List[A]] =
    sequence(timesNtr(n, rule))
  
  def roll(n: Int): Rand[List[Int]] = listOfN(n)(rollDie)
  def roll(n: Int, r: RNG): List[Int] = roll(n)(r)._1
  
}
