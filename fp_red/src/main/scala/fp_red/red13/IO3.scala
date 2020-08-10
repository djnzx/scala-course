package fp_red.red13

import fp_red.red07.Nonblocking.Par

import scala.annotation.tailrec
import scala.io.StdIn
import scala.language.postfixOps

/**
  * We can generalize `TailRec` and `Async` to the type `Free`, which is
  * a `Monad` for any choice of `F`.
  */
object IO3 {

  /**
    * F - doesn't need to be a Monad!
    * F - can be anything!
    */
  sealed trait Free[F[_], A] {
    def flatMap[B](f: A => Free[F, B]): Free[F, B] = FlatMap(this, f)
    def map[B]   (f: A => B         ): Free[F, B] = flatMap(f andThen (Return(_)))
  }
  case class Return[F[_], A](a: A) extends Free[F, A]
  case class Suspend[F[_], A](s: F[A]) extends Free[F, A]
  case class FlatMap[F[_], A, B](s: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

  type TailRec[A] = Free[Function0, A]
  type Async[A] = Free[Par, A]

  // Exercise 1: Implement the free monad
  def freeMonad[F[_]]: Monad[({type f[a] = Free[F,a]})#f] =
    new Monad[({type f[a] = Free[F,a]})#f] {
      override def unit[A](a: => A): Free[F, A] = Return(a)
      override def flatMap[A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] = fa flatMap f
    }
    
  // Exercise 2: Implement a specialized `Function0` interpreter.
  @tailrec
  def runTrampoline[A](a: Free[Function0, A]): A = a match {
    case Return(a) => a
    case Suspend(s) => s()
    case FlatMap(s, f) =>
      val ff = f.asInstanceOf[A => Free[Function0, A]]
      val step: Free[Function0, A] = s match {
        case Return(a) => ff(a.asInstanceOf[A])
        case Suspend(s) => ff(s().asInstanceOf[A])
        case FlatMap(y, g) =>
          val gg = g.asInstanceOf[A => Free[Function0, A]]
          y.asInstanceOf[Free[Function0, A]].flatMap { a => gg(a).flatMap(ff) }
      }
      runTrampoline(step)
  }

  // Exercise 3: Implement a `Free` interpreter which works for any `Monad`
  def run[F[_],A](fa: Free[F,A])(implicit F: Monad[F]): F[A] = step(fa) match {
    case Return(a) => F.unit(a)
    case Suspend(s) => s
    case FlatMap(Suspend(s), f) => F.flatMap(s)(a => run(f(a)))
    case _ => sys.error("Impossible, since `step` eliminates these cases")
  }

  // return either a `Suspend`, a `Return`, or a right-associated `FlatMap`
  @tailrec
  def step[F[_],A](fa: Free[F,A]): Free[F,A] = fa match {
    case FlatMap(FlatMap(x, f), g) => step(x.flatMap(a => f(a).flatMap(g)))
    case FlatMap(Return(a), f) => step(f(a))
    case _ => fa
  }

  /**
    * The type constructor `F` lets us control the set of external requests our
    * program is allowed to make. For instance, here is a type that allows for
    * only console I/O effects.
    */
  import fp_red.red07.Nonblocking.Par

  sealed trait Console[A] {
    def toThunk: () => A
    def toPar: Par[A]
    def toState: ConsoleState[A]
    def toReader: ConsoleReader[A]
  }
  
  /** one implementation, read */ 
  case object ReadLine extends Console[Option[String]] {
    /** naive, plain implementation, core */
    def run: Option[String] =
      try Option(StdIn.readLine())
      catch { case _: Exception => None }

    def toThunk = () => run
    def toPar = Par.lazyUnit(run)
    def toState = ConsoleState { bufs: Buffers =>
      bufs.in match {
        case Nil    => (None,    bufs)
        case h :: t => (Some(h), bufs.copy(in = t))
      }
    }
    def toReader = ConsoleReader { in: String => Some(in) }
  }

  /** another implementation. read */
  case class PrintLine(line: String) extends Console[Unit] {
    def toThunk = () => println(line)
    def toPar = Par.lazyUnit(println(line))
    def toState = ConsoleState { bufs => ((), bufs.copy(out = bufs.out :+ line)) }
    def toReader = ConsoleReader { _ => () } // noop
  }

  object Console {
    type ConsoleIO[A] = Free[Console, A]

    def readLn:                ConsoleIO[Option[String]] = Suspend(ReadLine)
    def printLn(line: String): ConsoleIO[Unit]           = Suspend(PrintLine(line))
  }

  /**
    * How to run `ConsoleIO` program? We don't have a `Monad[Console]`
    * to run, we need `F` to be a Monad: def run(...)(implicit F: Monad[F])
    */

  /** Translate between ANY `F[A]` to `G[A]`. */
  trait Translate[F[_], G[_]] { 
    def apply[A](f: F[A]): G[A]
  }
  type ~>[F[_], G[_]] = Translate[F,G] // infix syntax `F ~> G` for `Translate[F,G]`

  implicit val function0Monad: Monad[Function0] =
    new Monad[Function0] {
    def unit[A](a: => A) = () => a
    def flatMap[A, B](a: () => A)(f: A => (() => B)) = () => f(a())()
  }

  implicit val parMonad: Monad[Par] =
    new Monad[Par] {
    def unit[A](a: => A) = Par.unit(a)
    def flatMap[A,B](a: Par[A])(f: A => Par[B]) = Par.fork { Par.flatMap(a)(f) }
  }

  def runFree[F[_], G[_], A](free: Free[F, A])(t: F ~> G)(implicit G: Monad[G]): G[A] =
    step(free) match {
      case Return(a) => G.unit(a)
      case Suspend(r) => t(r)
      case FlatMap(Suspend(r), f) => G.flatMap(t(r))(a => runFree(f(a))(t))
      case _ => sys.error("Impossible, since `step` eliminates these cases")
    }

  val consoleToFunction0: Console ~> Function0 =
    new (Console ~> Function0) {
      override def apply[A](a: Console[A]) = a.toThunk
    }

  val consoleToPar: Console ~> Par =
    new (Console ~> Par) {
      override def apply[A](a: Console[A]) = a.toPar
    }

  /**
    * we can run it because we have implementations:
    * Console ~> Function0
    * Console ~> Par
    */
  def runConsoleFunction0[A](a: Free[Console, A]): () => A =
    runFree[Console,Function0,A](a)(consoleToFunction0)
    
  def runConsolePar[A](a: Free[Console, A]): Par[A] =
    runFree[Console,Par,A](a)(consoleToPar)
  /* Can interpet these as before to convert our `ConsoleIO` to a pure value that does no I/O! */

  /**
    * The `runConsoleFunction0` implementation is unfortunately not stack safe,
    * because it relies of the stack safety of the underlying monad, and the
    * `Function0` monad we gave is not stack safe. To see the problem, try
    * running: `freeMonad.forever(Console.printLn("Hello"))`. 
    */
  
  // Exercise 4 (optional, hard): Implement `runConsole` using `runFree`,
  /** translate via runFree & Suspend */
  def translate[F[_], G[_], A](f: Free[F,A])(fg: F ~> G): Free[G,A] = {
    type FreeG[A] = Free[G, A]
    val t: F ~> FreeG = new (F ~> FreeG) {
      override def apply[A](fa: F[A]): FreeG[A] = Suspend { fg(fa) }
    }
    runFree(f)(t)(freeMonad[G])
  }

  /** run Console via runTrampoline and translate */
  def runConsole[A](a: Free[Console, A]): A = {
    val t: Console ~> Function0 = new (Console ~> Function0) {
      override def apply[A](f: Console[A]): () => A = f.toThunk
    }
    val f0: Free[Function0, A] = translate(a)(t)
    runTrampoline(f0)
  }

  /**
    * There is nothing about `Free[Console,A]` that requires we interpret
    * `Console` using side effects. Here are two pure ways of interpreting
    * a `Free[Console,A]`.
    */
  import Console._

  case class Buffers(in: List[String], out: Vector[String])

  // A specialized state monad
  case class ConsoleState[A](run: Buffers => (A, Buffers)) {
    def map[B](f: A => B): ConsoleState[B] =
      ConsoleState { s =>
        val (a, s1) = run(s)
        (f(a), s1)
      }
    def flatMap[B](f: A => ConsoleState[B]): ConsoleState[B] =
      ConsoleState { s =>
        val (a, s1) = run(s)
        f(a).run(s1)
      }
  }
  object ConsoleState {
    implicit val monad = new Monad[ConsoleState] {
      def unit[A](a: => A) = 
        ConsoleState(bufs => (a,bufs))
      def flatMap[A,B](ra: ConsoleState[A])(f: A => ConsoleState[B]) = 
        ra flatMap f
    }
  }

  // A specialized reader monad
  case class ConsoleReader[A](run: String => A) {
    def map[B](f: A => B): ConsoleReader[B] =
      ConsoleReader(r => f(run(r)))
    def flatMap[B](f: A => ConsoleReader[B]): ConsoleReader[B] =
      ConsoleReader(r => f(run(r)).run(r))
  }
  object ConsoleReader {
    implicit val monad = new Monad[ConsoleReader] {
      def unit[A](a: => A) = ConsoleReader(_ => a)
      def flatMap[A,B](ra: ConsoleReader[A])(f: A => ConsoleReader[B]) = ra flatMap f
    }
  }

  val consoleToReader: Console ~> ConsoleReader =
    new (Console ~> ConsoleReader) {
      override def apply[A](f: Console[A]) = f.toReader
    }

  val consoleToState: Console ~> ConsoleState =
    new (Console ~> ConsoleState) {
      def apply[A](a: Console[A]) = a.toState
    }
    
  def runConsoleReader[A](io: ConsoleIO[A]): ConsoleReader[A] =
    runFree[Console,ConsoleReader,A](io)(consoleToReader)

  def runConsoleState[A](io: ConsoleIO[A]): ConsoleState[A] =
    runFree[Console,ConsoleState,A](io)(consoleToState)

  def runConsoleState2[A](io: Free[Console, A]): ConsoleState[A] =
    runFree[Console,ConsoleState,A](io)(consoleToState)

  /**
    * So `Free[F,A]` is not really an I/O type. The interpreter `runFree` gets
    * to choose how to interpret these `F` requests, and whether to do "real" I/O
    * or simply convert to some pure value!
    */

  /**
    * These interpretations are not stack safe for the same reason,
    * can instead work with `case class ConsoleReader[A](run: String => Trampoline[A])`,
    * which gives us a stack safe monad
    */

  // We conclude that a good representation of an `IO` monad is this:
  type IO[A] = Free[Par, A]

  /*
   * Exercise 5: Implement a non-blocking read from an asynchronous file channel.
   * We'll just give the basic idea - here, we construct a `Future`
   * by reading from an `AsynchronousFileChannel`, a `java.nio` class
   * which supports asynchronous reads.
   */
  import java.nio.channels._
  import java.nio.ByteBuffer

  def read(file: AsynchronousFileChannel,
           fromPosition: Long,
           numBytes: Int): Par[Either[Throwable, Array[Byte]]] =
    Par.async { (cb: Either[Throwable, Array[Byte]] => Unit) =>
      
      val buf = ByteBuffer.allocate(numBytes)
      
      val ch = new CompletionHandler[Integer, Unit] {
        override def completed(bytesRead: Integer, attachment: Unit): Unit = {
          val outcome = new Array[Byte](bytesRead)
          buf.slice.get(outcome, 0, bytesRead)
          cb(Right(outcome))
        }
        override def failed(ex: Throwable, attachment: Unit): Unit =
          cb(Left(ex))
      }
      
      file.read(buf, fromPosition, (), ch)
    }

  // Provides the syntax `Async { k => ... }` for asynchronous IO blocks.
  def Async[A](cb: (A => Unit) => Unit): IO[A] = Suspend(Par.async(cb))

  // Provides the `IO { ... }` syntax for synchronous IO blocks.
  def IO[A](a: => A): IO[A] = Suspend { Par.delay(a) }
}
