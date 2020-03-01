package book_red.exercises.c06state

import book_red.exercises.c06state.RNGApp.SimpleRand

import scala.annotation.tailrec

trait RNG {
  def nextInt: (Int, RNG) // minimal behavior: nextInt + return new seed
}

object RNGApp extends App {

  case class SimpleRand(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = SimpleRand(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }
  val seed = SimpleRand(7)                // initialize random with predictable seed

  val (r1, sr1):(Int, RNG) = seed.nextInt // chain them
  val (r2, sr2)            = sr1.nextInt  // and we will get always same values
  val (r3, sr3)            = sr2.nextInt  // predictability !
  println(r1)
  println(r2)
  println(r3)
  println("------")

  /**
    * all functions will be in term: RNG -> (A, RNG)
    */
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (a, r) =  rng.nextInt
    val b = math.abs(a)
    (b, r)
  }

  def intDouble(rng: RNG): ((Int,Double), RNG) = {
    val (a1, r1) = rng.nextInt
    val (a2, r2) = r1.nextInt
    ((a1, a2.toDouble), r2)
  }

  def doubleInt(rng: RNG): ((Double,Int), RNG) = {
    val (a, r) = rng.nextInt
    val (a2, r2) = r.nextInt
    val d2 = a2.toDouble
    ((d2, a), r2)
  }

  def double3(rng: RNG): ((Double,Double,Double), RNG) = {
    val (a1, r1) = rng.nextInt
    val (a2, r2) = r1.nextInt
    val (a3, r3) = r2.nextInt
    ((a1.toDouble, a2.toDouble, a3.toDouble), r3)
  }

  def ints(count: Int, rule: Rand[Int])(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def go(cnt: Int, acc: List[Int], r: RNG): (List[Int], RNG) = cnt match {
      case 0 => (acc, r)
      case _ => rule(r) match {
        case (rv, rg) => go(cnt - 1, rv :: acc, rg)
      }
    }
    val (list, rx) = go(count, Nil, rng)
    (list reverse, rx)
  }

  /**
    * let's introduce more general representation:
    * Rand[+A] = RNG => (A, RNG)
    */
  type Rand[+A] = RNG => (A, RNG)

  val nextInt: Rand[Int] = s => s.nextInt

  val nextDouble: Rand[Double] = (s: RNG) => {
    val (a, s2): (Int, RNG) = s.nextInt
    val d: Double = a.toDouble
    (d, s2)
  }

  val fiveInts: (List[Int], RNG) = ints(5, nextInt)(seed)
  println(fiveInts._1)

  // doesn't change the state, just provides value. (lift ?)
  def unit[A](a: A): Rand[A] = s => (a, s)

  def map[A,B](fa: Rand[A])(f: A => B): Rand[B] = s => {
    // apply state transition
    val (a, s2): (A, RNG) = fa(s)
    // apply function given
    val b: B = f(a)
    (b, s2)
  }

  def map2[A,B,C](fa: Rand[A], fb: Rand[B])(f: (A, B) => C): Rand[C] = s => {
    // apply state transition # 1
    val (a, s2): (A, RNG) = fa(s)
    // apply state transition # 2
    val (b, s3): (B, RNG) = fb(s2)
    // apply function given
    val c: C = f(a, b)
    (c, s3)
  }

  def both[A,B](fa: Rand[A], fb: Rand[B]): Rand[(A, B)] =
    map2(fa, fb)((a, b) => (a, b)) // tuple them

  def randIntDouble: Rand[(Int, Double)] = both(nextInt, nextDouble)
  def randDoubleInt: Rand[(Double, Int)] = both(nextDouble, nextInt)

  // nonNegativeInt: RNG => (Int, RNG)
  //                     Rand[Int]
  val nonNegativeInt3: Rand[Int] = nonNegativeInt
  def nonNegativeInt2(rng: RNG): Rand[Int] = map(_ => rng.nextInt)(i => math.abs(i))
  def nonNegativeEven: Rand[Int] = map(nonNegativeInt3)(i => i - i % 2)

  val lessThan = (n: Int, than: Int) => n % than
  val plus1 = (n: Int) => n + 1
  val plus = (a: Int, b: Int) => a + b
  def nonNegativeLessThan (than: Int): Rand[Int] = map(nonNegativeInt3)(n => n % than)
  def nonNegativeLessThan2(than: Int): Rand[Int] = map(nonNegativeInt3)(lessThan(_, than))

  def rollDie2: Rand[Int] = map(nonNegativeLessThan(6))(plus1)
  def rollDie: Rand[Int] = map2(nonNegativeLessThan(6), unit(1))(plus)

  def roll(count: Int): Rand[List[Int]] = ints(count, rollDie)

  val result = roll(20)(seed)._1
  println(result)

  // plain naive implementation
  def sequenceR[A](fs: List[Rand[A]]): Rand[List[A]] = s => fs match {
    case Nil => (Nil, s)
    case h::t => {
      val (a, s2) = h(s)
      val (la, s3) = sequenceR(t)(s2)
      (a::la, s3)
    }
  }

  // implementation expressed via map2
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = fs match {
    case Nil => sx => (Nil, sx)
    case h::t => map2(h, sequence(t))((a: A, as: List[A]) => a :: as)
  }

  // tail recursive implementation, without structural recursion
  def sequenceTR[A](fs: List[Rand[A]]): Rand[List[A]] = s0 => {

    @tailrec
    def go(tail: List[Rand[A]], acc: List[A], s: RNG): (List[A], RNG) = tail match {
      case Nil  => (acc, s)
      case h::t => {
        val (a, s2): (A, RNG) = h(s)
        go(t, a::acc, s2)
      }
    }

    val (list, st_last) = go(fs, Nil, s0)
    (list reverse, st_last)
  }

  // scala foldLeft based solution
  def sequenceScala[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight( unit(List[A]()) )(  (acc, elem) => map2(acc, elem)((a: A, b: List[A]) => a :: b)  )

  def sequenceScala2[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight( unit(List[A]()) )(  (acc, ra) => map2(acc, ra)((a, b) => a :: b)  )

  def sequenceScala3[A](fs: List[Rand[A]]): Rand[List[A]] =
    fs.foldRight( unit(List[A]()) )(  (acc, ra) => map2(acc, ra)( _ :: _ ) )

  println("==========")
  val tr1: Rand[Int] = s => s.nextInt
  val tr2: Rand[Int] = s => (2, s)
  val tr3: Rand[Int] = s => (3, s)
  val ll1 = sequenceTR(List(tr1, tr2, tr3))(seed)._1
  val ll2 = sequenceR(List(tr1, tr2, tr3))(seed)._1
  val ll3 = sequence(List(tr1, tr2, tr3))(seed)._1
  val ll4 = sequenceScala(List(tr1, tr2, tr3))(seed)._1
  println(ll1)
  println(ll2)
  println(ll3)
  println(ll4)
  println("==========")

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = (s: RNG) => {
    // apply f
    val (a, s2): (A, RNG) = f(s)
    val frb: Rand[B] = g(a)
    val (b, s3) = frb(s2)
    (b, s3)
  }

}

case class State[S,+A](run: S => (A, S)) { me =>
  def map[B](f: A => B): State[S, B] = State( s => {
    val (a, s2): (A, S) = me.run(s)
    val b: B = f(a)
    (b, s2)
  })
  def map2[B,C](sb: State[S, B])(f: (A, B) => C): State[S, C] = State( s => {
    val (a, s2): (A, S) = me.run(s)
    val (b, s3): (B, S) = sb.run(s2)
    val c: C = f(a, b)
    (c, s3)
  })
  def flatMap[B](f: A => State[S, B]): State[S, B] = State( s => {
    val (a, s2): (A, S) = me.run(s)
    val ssb: State[S, B] = f(a)
    val bs: (B, S) = ssb.run(s2)
    bs
  })
}

object RollDieStateApp extends App {
  val nonNeg: State[RNG, Int] = State((s: RNG) => {
    val (a, s2) = s.nextInt
    println(s"NN:before:$a")
    val a2 = math.abs(a)
    println(s"NN:after: $a2")
    (a2, s2)
  })

  val notGreater: (Int, Int) => State[RNG, Int] = (what: Int, than: Int) => State((s: RNG) => {
    println(s"NG:before:$what")
    val a2 = what % than
    println(s"NG:after: $a2")
    (a2, s)
  })

  val plus1: Int => State[RNG, Int] = (to: Int) => State((s: RNG) => {
    println(s"P1:before:$to")
    val a2 = to +1
    println(s"P1:after: $a2")
    (a2, s)
  })

  // creating representation, syntax #1
  val dice1: State[RNG, Int] =
    nonNeg.flatMap(nn =>
      notGreater(nn, 6).flatMap(ng =>
        plus1(ng).map(z => z)
      )
    )

  // creating representation, syntax #2
  val dice2: State[RNG, Int] = for {
    a <- nonNeg
    b <- notGreater(a, 6)
    c <- plus1(b)
  } yield c

  // creating initial random state
  val seed = SimpleRand(100)

  // running representation and extracting value
  val (v, r) =  dice2.run(seed)
  println(v)

  println("-- roll N times")

  // roll classic recursive implementation
  def roll1(num: Int): State[RNG, List[Int]] = num match {
    case 0 => State(s => (Nil, s)) // recursion exit
    case _ => State(s => {
      val (value, s2) = dice1.run(s)
      val (tail, s3) = roll1(num - 1).run(s2)
      val list = value :: tail
      (list, s3)
    })
  }

  // roll implementation based on map2
  def roll2(num: Int): State[RNG, List[Int]] = num match {
    case 0 => State(s0 => (Nil, s0)) // recursion exit
    case _ => dice1.map2(roll2(num-1))((x: Int, xs: List[Int]) => x :: xs)
  }

  // roll tail recursive implementation
  def roll3(num: Int): State[RNG, List[Int]] = State( s0 => {
    @tailrec
    def go(n: Int, acc: List[Int], s: RNG): List[Int] = n match {
      case 0 => acc
      case _ => dice1.run(s) match { case (a, s2) => go(n-1, a::acc, s2) }
    }
    (go(num, Nil, s0) reverse, s0)
  })

  // roll implementation by using scala's foldLeft
  def roll4(num: Int): State[RNG, List[Int]] = State( s0 => {
    (1 to num)
      .foldLeft(
        (List[Int](), s0)
      )(
        (acc, _) => dice1.run(acc._2) match { case (a, s) => (a::acc._1, s) }
      ) match { case (l, s) => (l reverse, s) }
  })


  val l1 = roll1(10).run(seed)._1
  val l2 = roll2(10).run(seed)._1
  val l3 = roll3(10).run(seed)._1
  val l4 = roll4(10).run(seed)._1
  println(l1)
  println(l2)
  println(l3)
  println(l4)

  def get[S]: State[S, S] = State( s => (s, s))
  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()
}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object State {
  type Rand[A] = State[RNG, A]
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???
}
