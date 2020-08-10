package fp_red.red13

import scala.annotation.tailrec

/**
  * fixing stack overflow problem
  */
object IO2a {

  sealed trait IO[A] {
    /**
      * - here we build new description of operation
      * - we do not interpret the `flatMap` here, just return it as a value
      * - we will do this later
      */
    def flatMap[B](f: A => IO[B]): IO[B] = FlatMap(this, f)
    def map[B](f: A => B): IO[B] = flatMap(f andThen (Return(_)))
  }
  /** will be used for non-recursive calls */
  case class Return[A](a: A) extends IO[A]
  /** will be used for lazy initialization */
  case class Suspend[A](resume: () => A) extends IO[A]
  /** will be used for recursive calls */
  case class FlatMap[A,B](sub: IO[A], k: A => IO[B]) extends IO[B]

  object IO extends Monad[IO] { // Notice that none of these operations DO anything
    def unit   [A](a: => A)                 : IO[A] = Return(a)
    def suspend[A](a: => IO[A])             : IO[A] = Suspend(() => ()).flatMap { _ => a }
    def flatMap[A,B](a: IO[A])(f: A => IO[B]): IO[B] = a flatMap f
  }

  def printLine(s: String): IO[Unit] = Suspend(() => Return(println(s)))

  val p = IO.forever(printLine("Still going..."))

  val actions: Stream[IO[Unit]] = Stream.fill(100000)(printLine("Still going..."))
  val composite: IO[Unit] = actions.foldLeft(IO.unit(())) { (acc, a) => acc flatMap { _ => a } }

  // There is only one sensible way to implement this as a
  // tail-recursive function, the one tricky case is left-nested
  // flatMaps, as in `((a flatMap f) flatMap g)`, which we
  // reassociate to the right as `a flatMap (ar => f(a) flatMap g)`
  @tailrec
  def run[A](io: IO[A]): A = io match {
    case Return(a) => a     // A
    case Suspend(r) => r()  // A
    case FlatMap(x, f) =>
      val step: IO[A] = x match {
        case Return(a) => f(a)
        case Suspend(r) => f(r())
        case FlatMap(y, g) => y.flatMap(a => g(a).flatMap(f))
      }
      run(step)  // A
  }
}

object IO2aTests {
  import IO2a.IO
  import IO2a.run

  type IO[A] = IO2a.IO[A]
  val f: Int => IO[Int] = (i: Int) => IO2a.Return(i)

  val list: List[Int => IO[Int]] = List.fill(10000)(f)
  /**
    * we fold list of functions to one nested function
    * for further usage
    */
  val g: Int => IO[Int] =
    list.foldLeft(f) {
      //   accumulator  ,   list element
      (acc: Int => IO[Int], fn: Int => IO[Int]) =>
        (x: Int) => IO.suspend(acc(x).flatMap(fn))
    }

  def main(args: Array[String]): Unit = {
    // we pass the value 42 through the combination of 10k functions
    val g42: IO[Int] = g(42)
    val r: Int = run(g42)
    println(s"g(42): Function = $g42")
    println(s"run(g(42)): Value = $r")
  }
}

/**
  * As it turns out, there's nothing about this data type that is specific
  * to I/O, it's just a general purpose data type for optimizing tail calls.
  * Here it is, renamed to `TailRec`. This type is also sometimes called
  * `Trampoline`, because of the way interpreting it bounces back and forth
  * between the main `run` loop and the functions contained in the `TailRec`.
  */
object IO2b {

  sealed trait TailRec[A] {
    def flatMap[B](f: A => TailRec[B]): TailRec[B] = FlatMap(this, f)
    def map[B](f: A => B): TailRec[B] = flatMap(f andThen { x: B => Return(x) })
  }
  case class Return[A](a: A) extends TailRec[A]
  case class Suspend[A](resume: () => A) extends TailRec[A]
  case class FlatMap[A,B](sub: TailRec[A], k: A => TailRec[B]) extends TailRec[B]

  object TailRec extends Monad[TailRec] {
    def unit[A](a: => A): TailRec[A] = Return(a)
    def flatMap[A,B](a: TailRec[A])(f: A => TailRec[B]): TailRec[B] = a flatMap f
    def suspend[A](a: => TailRec[A]): TailRec[A] = Suspend(() => ()).flatMap { _ => a }
  }

  @tailrec
  def run[A](t: TailRec[A]): A = t match {
    case Return(a) => a
    case Suspend(r) => r()
    case FlatMap(x, f) =>
      val step: TailRec[A] = x match {
        case Return(a) => f(a)
        case Suspend(r) => f(r())
        case FlatMap(y, g) => y.flatMap(a => g(a).flatMap(f))
      }
      run(step)
  }
}

object IO2bTests {
  import IO2b._

  val f: Int => TailRec[Int] = (i: Int) => Return(i)

  val list: List[Int => TailRec[Int]] = List.fill(10000)(f)
  /**
    * we fold list of functions to one nested function
    * for further usage
    */
  val g: Int => TailRec[Int] =
    list.foldLeft(f) {
      //   accumulator        ,      list element
      (acc: Int => TailRec[Int], fn: Int => TailRec[Int]) =>
        (x: Int) => TailRec.suspend(acc(x).flatMap(fn))
    }

  def main(args: Array[String]): Unit = {
    // we pass the value 42 through the combination of 10k functions
    val g42: TailRec[Int] = g(42)
    val r: Int = run(g42)
    println(s"g(42): Function = $g42")
    println(s"run(g(42)): Value = $r")
  }
}

/**
  * 13.4.
  *
  * We've solved our first problem of ensuring stack safety, but we're still
  * being very inexplicit about what sort of effects can occur, and we also
  * haven't found a way of describing asynchronous computations. Our `Suspend
  * thunks will just block the current thread when run by the interpreter.
  * We could fix that by changing the signature of `Suspend` to take a `Par`.
  * We'll call this new type `Async`.
  */
object IO2c {

  import fp_red.red07.Nonblocking._

  sealed trait Async[A] { // will rename this type to `Async`
    def flatMap[B](f: A => Async[B]): Async[B] = FlatMap(this, f)
    def map[B](f: A => B): Async[B] = flatMap(f andThen ((b: B) => Return(b)))
  }
  case class Return[A](a: A) extends Async[A]
  /** we changed `() => A` to `(es) => Future[A]` */
  case class Suspend[A](resume: Par[A]) extends Async[A] // notice this is a `Par`
  case class FlatMap[A,B](sub: Async[A], k: A => Async[B]) extends Async[B]

  object Async extends Monad[Async] {
    def unit[A](a: => A): Async[A] = Return(a)
    def flatMap[A,B](a: Async[A])(f: A => Async[B]): Async[B] = a flatMap f
  }

  // return either a `Suspend`, a `Return`, or a right-associated `FlatMap`
  @tailrec
  def step[A](async: Async[A]): Async[A] = async match {
    case FlatMap(FlatMap(x, f), g) => step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => async
  }

  def run[A](async: Async[A]): Par[A] = step(async) match {
    case Return(a) => Par.unit(a)
    case Suspend(r) => r
    case FlatMap(x, f) => x match {
      case Suspend(r) => Par.flatMap(r)(a => run(f(a)))
      case _ => sys.error("Impossible, since `step` eliminates these cases")
    }
  }

  /**
    * The fact that `run` only uses the `unit` and `flatMap` functions of
    * `Par` is a clue that choosing `Par` was too specific of a choice,
    * this interpreter could be generalized to work with any monad.
    */
}
