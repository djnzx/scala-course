package fp_red.red06

import scala.annotation.tailrec

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
    val a2 = to + 1
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
  val seed = RNG.Simple(100)

  // running representation and extracting value
  val (v, r) = dice2.run(seed)
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
    case _ => dice1.map2(roll2(num - 1))((x: Int, xs: List[Int]) => x :: xs)
  }

  // roll tail recursive implementation
  def roll3(num: Int): State[RNG, List[Int]] = State(s0 => {
    @tailrec
    def go(n: Int, acc: List[Int], s: RNG): List[Int] = n match {
      case 0 => acc
      case _ => dice1.run(s) match {
        case (a, s2) => go(n - 1, a :: acc, s2)
      }
    }

    (go(num, Nil, s0) reverse, s0)
  })

  // roll implementation by using scala's foldLeft
  def roll4(num: Int): State[RNG, List[Int]] = State(s0 => {
    (1 to num)
      .foldLeft(
        (List[Int](), s0)
      )(
        (acc, _) => dice1.run(acc._2) match {
          case (a, s) => (a :: acc._1, s)
        }
      ) match {
      case (l, s) => (l reverse, s)
    }
  })


  val l1 = roll1(10).run(seed)._1
  val l2 = roll2(10).run(seed)._1
  val l3 = roll3(10).run(seed)._1
  val l4 = roll4(10).run(seed)._1
  println(l1)
  println(l2)
  println(l3)
  println(l4)

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()
}