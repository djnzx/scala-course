package fp_red.red15

import fp_red.red13.{IO, Monad}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec
import scala.language.implicitConversions
import scala.util.chaining.scalaUtilChainingOps

object SimpleStreamTransducers {

  /**
    * We now introduce a type, `Process`, representing pure, single-input
    * stream transducers. It can be in of three states, it can be: 
    * - emitting a value to the output (`Emit`),
    * - reading a value from its input (`Await`),
    * - signaling termination via `Halt`.
    */
  sealed trait Process[A, B] {
    import Process._

    /**
      * this is a driver (interpreter) of our stream
      * A `Process[I,O]` can be used to transform
      * `Stream[I]` to a `Stream[O]`
      */
    def apply(s: Stream[A]): Stream[B] = this match {
      case Halt()     => Stream.empty
      case Await(f)   => s match {
        /** Non-empty stream, apply function to the head */
        case h #:: t =>
          val p: Process[A, B] = f(Some(h)) 
          p(t) 
        /** empty */
        case _       => 
          val p: Process[A, B] = f(None)
          p(Stream.empty)
      }
      /** we are ready to emit value */
      case Emit(h, t) => h #:: t(s)
    }

    /**
      * See `Process.lift` for a typical repeating `Process`
      * definition expressed with explicit recursion 
      */

    /**
     * `Process`  definitions can often be expressed without explicit
     * recursion, by repeating some simpler `Process` forever */
    def repeat: Process[A, B] = {
      
      def go(p: Process[A, B]): Process[A,B] = p match {
        case Halt() => go(this)
        case Await(recv) => Await {
          case None => recv(None)
          case i => go(recv(i))
        }
        case Emit(h, t) => Emit(h, go(t))
      }
      
      go(this)
    }

    def repeatN(n: Int): Process[A, B] = {
      
      def go(n: Int, p: Process[A, B]): Process[A, B] = p match {
        case Halt() if n > 0 => go(n - 1, this)
        case Halt() => Halt()
        case Await(recv) => Await {
          case None => recv(None)
          case oi   => go(n, recv(oi))
        }
        case Emit(h, t) => Emit(h, go(n,t))
      }
      
      go(n, this)
    }

    /**
     * `Process` can be thought of as a sequence of values of type `O`
     * and many of the operations that would be defined for `List[O]`
     * can be defined for `Process[I,O]`, for instance `map`, `++` and
     * `flatMap`. The definitions are analogous.
     */
    def map[O2](f: B => O2): Process[A, O2] = this match {
      case Halt()      => Halt()
      case Emit(h, t)  => Emit(f(h), t map f)
      case Await(recv) => Await(recv andThen (_ map f))
    }
    
    def map_pipe[O2](f: B => O2): Process[A,O2] = this |> lift(f)

    def ++(p: => Process[A, B]): Process[A, B] = this match {
      case Halt() => p
      case Emit(h, t) => Emit(h, t ++ p)
      case Await(recv) => Await(recv andThen (_ ++ p))
    }
    def flatMap[O2](f: B => Process[A, O2]): Process[A, O2] = this match {
      case Halt() => Halt()
      case Emit(h, t) => f(h) ++ t.flatMap(f)
      case Await(recv) => Await(recv andThen (_ flatMap f))
    }

    /**
     * Exercise 5: Implement `|>`. Let the types guide your implementation.
     */
    def |>[O2](p2: Process[B, O2]): Process[A, O2] =
      p2 match {
        case Halt()     => Halt()
        case Emit(h, t) => Emit(h, this |> t)
        case Await(f)   => this match {
          case Emit(h, t) => t |> f(Some(h))
          case x @ Halt() => x |> f(None)
          case Await(g)   => Await { i: Option[A] => g(i) |> p2 }
        }
      }

    /**
     * Feed `in` to this `Process`. Uses a tail recursive loop as long
     * as `this` is in the `Await` state.
     */
    def feed(in: Seq[A]): Process[A, B] = {
      
      @tailrec
      def go(in: Seq[A], cur: Process[A, B]): Process[A, B] =
        cur match {
          case Halt() => Halt()
          case Await(recv) =>
            if (in.nonEmpty) go(in.tail, recv(Some(in.head)))
            else cur
          case Emit(h, t) => Emit(h, t.feed(in))
        }
      
      go(in, this)
    }

    /**
     * As an example of `repeat`, see `Process.filter`. We define
     * a convenience function here for composing this `Process`
     * with a `Process` that filters the output type `O`.
     */
    def filter(f: B => Boolean): Process[A,B] =
      this |> Process.filter(f)

    /** Exercise 7: see definition below. */
    def zip[O2](p: Process[A,O2]): Process[A,(B,O2)] =
      Process.zip(this, p)

    /** Exercise 6: Implement `zipWithIndex` */
    def zipWithIndex: Process[A, (B, Int)] = 
      this zip[Int] (count[A].map(_ - 1))

    /** Add `p` to the fallback branch of this process */
    def orElse(p: Process[A, B]): Process[A, B] = this match {
      case Halt() => p
      case Await(f) => Await {
        case None => p
        case x => f(x)
      }
      case _ => this
    }
  }

  object Process {

    /** REPRESENTATION:
      * 
      * emitting a value to the output */
    case class Emit[A, B](item: B, tail: Process[A, B] = Halt[A, B]()) extends Process[A, B]
    
    /** reading a value from its input */
    case class Await[A, B](onReceive: Option[A] => Process[A, B]) extends Process[A, B]
    
    /** termination */
    case class Halt[A, B]() extends Process[A, B]

    /** emitting ONE value, without carrying to input value and its type */
    def emitOne[A, B](item: B, tail: Process[A, B] = Halt[A, B]()): Process[A, B] =
      Emit(item, tail)

    def emitSeq[A, B](items: Seq[B], tail: Process[A, B] = Halt[A, B]()): Process[A, B] = items match {
      case b +: bs => Emit(b, emitSeq(bs, tail))
      case _       => tail
    }

    def emitStream[A, B](items: Stream[B], tail: Process[A, B] = Halt[A, B]()): Process[A, B] = items match {
      case b #:: bs => Emit(b, emitSeq(bs, tail))
      case _ => tail
    }

    /**
      * We can convert any function `f: I => O` to a `Process[I,O]`. We
      * simply `Await`, then `Emit` the value received, transformed by `f`.
      * We terminate stream after 1st element 
      */
    def liftOne[I, O](f: I => O): Process[I,O] =
      Await {
        case Some(i) => emitOne(f(i))
        case None    => Halt()
      }
      
    /** lifting whole stream just repeating [[liftOne]] */
    def lift[I,O](f: I => O): Process[I,O] =
      liftOne(f).repeat

    /**
      * transducer can do way more than map:
      * insert, delete, filter, ...
      */
    def filter[I](f: I => Boolean): Process[I, I] =
      Await[I, I] {
        case Some(i) if f(i) => emitOne(i)
        case _ => Halt()
      }.repeat

    /**
      * A helper function to await an element 
      * or fall back to another process
      * if there is no input.
      */
    def await[I,O](f: I => Process[I, O], 
                   fallback: Process[I, O] = Halt[I,O]()
                  ): Process[I,O] =
      Await[I,O] {
        case Some(i) => f(i)
        case None    => fallback
      }
      
    /**
     * Here's a typical `Process` definition that requires tracking some
     * piece of state (in this case, the running total):
     */
    def sum: Process[Double, Double] = {
      
      def go(acc: Double): Process[Double, Double] =
        await { x => 
          emitOne(x + acc, go(x + acc))
        }
        
      go(0.0)
    }
    
    /** count recursive */
    def count1[I]: Process[I, Int] = {
      def go(count: Int): Process[I, Int] =
        await { _ =>
          emitOne(count + 1, go(count + 1))
        }
      
      go(0)
    }
    /**
      * Here's one implementation, with three stages - we map all inputs
      * to 1.0, compute a running sum, then finally convert the output
      * back to `Int`. The three stages will be interleaved - as soon
      * as the first element is examined, it will be converted to 1.0,
      * then added to the running total, and then this running total
      * will be converted back to `Int`, then the `Process` will examine
      * the next element, and so on.
      */
    def count[I]: Process[I, Int] =
      lift { _: I => 1.0 } |> sum |> lift(_.toInt)

    /**
      * Implement `mean`.
      * This is an explicit recursive definition. We'll factor out a
      * generic combinator shortly.
      */
    def mean: Process[Double, Double] = {
      
      def go(sum: Double, count: Double): Process[Double, Double] =
        await { d: Double =>
          emitOne((sum + d) / (count + 1), go(sum + d, count + 1))
        }
      
      go(0.0, 0.0)
    }
    /**
      * discovering the new primitive !
      */
    def loop[S, I, O](z: S)(f: (I, S) => (O, S)): Process[I, O] =
      await { i: I => f(i, z) match {
        case (o, s2) => emitOne(o, loop(s2)(f))
      }}

    /** stateful iteration */
    def loops[S, I, O](z: S)(f: (I, S) => (Option[O], S))(ft: S => Option[O]): Process[I, O] = 
      await(
        { // handle the next item
          i: I => f(i, z) match {
            case (None, s2)    => loops(s2)(f)(ft)
            case (Some(o), s2) => emitOne[I, O](o, loops(s2)(f)(ft))
        }},
        { // carefully handle state on finished stream
          ft(z) match {
            case None    => Halt[I, O]()
            case Some(x) => emitOne[I, O](x)
        }}
      )

    /** stateful iteration, V2 */
    def loops2[S, A, B](s: S)(f: (S, Option[A]) => (Option[B], S)): Process[A, B] = 
      await(
        { i: A =>
          f(s, Some(i)) match {
            case (Some(o), s2) => emitOne(o, loops2(s2)(f))
            case (None,    s2) => loops2(s2)(f)
          }
        },
        f(s, None) match {
          case (Some(x), _) => emitOne(x)
          case (None,    _) => Halt()
        }
      )
      
    /** 
      * every new item ot type A potentially can produce 0 or more elements of type B
      * to handle that, probably we need emit items recursively 
      */
    def loopssq[S, A, B](s: S)(f: (S, A) => (Seq[B], S))(residual: S => Seq[B]): Process[A, B] =
      await(
        { a: A =>
          f(s, a) match {
            case (Seq(), s2) => loopssq(s2)(f)(residual)
            case (bs,    s2) => emitSeq(bs, loopssq(s2)(f)(residual))
          }
        },
        residual(s) match {
          case Seq() => Halt()
          case bs => emitSeq(bs)
        }
      )
    
    /** stream version */
    def loopsst[S, A, B](s: S)(f: (S, Option[A]) => (Stream[B], S)): Process[A, B] =
      await(
        { i: A =>
          f(s, Some(i)) match {
            case (Seq(), s2) => loopsst(s2)(f)
            case (bs,    s2) => emitStream(bs, loopsst(s2)(f))
          }
        },
        f(s, None) match {
          case (Seq(), _) => Halt()
          case (bs,    _) => emitStream(bs)
        }
      )

    /** Process forms a monad, and we provide monad syntax for it  */
    def monad[I]: Monad[({ type f[x] = Process[I, x]})#f] =
      new Monad[({ type f[x] = Process[I,x]})#f] {
        def unit[O](o: => O): Process[I,O] = emitOne(o)
        def flatMap[O, O2](p: Process[I,O])(f: O => Process[I, O2]): Process[I,O2] =
          p flatMap f
      }

    /** enable monadic syntax */
    implicit def toMonadic[I,O](a: Process[I,O]) = monad[I].toMonadic(a)

    def take[I](n: Int): Process[I, I] =
      if (n <= 0) Halt()
      else await { i => emitOne(i, take[I](n - 1)) }

    def drop[I](n: Int): Process[I, I] =
      if (n <= 0) id
      else await { _ => drop[I](n - 1) }

    def takeWhile[I](f: I => Boolean): Process[I,I] =
      await { i =>
        if (f(i)) emitOne(i, takeWhile(f))
        else      Halt()
      }

    def dropWhile[I](f: I => Boolean): Process[I,I] =
      await { i =>
        if (f(i)) dropWhile(f)
        else      emitOne(i,id)
      }

    /* The identity `Process`, just repeatedly echos its input. */
    def id[I]: Process[I,I] = lift(identity)

    /* Exercise 4: Implement `sum` and `count` in terms of `loop` */

    def sum2: Process[Double,Double] =
      loop(0.0)((d:Double, acc) => (acc+d,acc+d))

    def count3[I]: Process[I,Int] =
      loop(0)((_:I,n) => (n+1,n+1))

    /**
     * Exercise 7: Can you think of a generic combinator that would
     * allow for the definition of `mean` in terms of `sum` and
     * `count`?
     *
     * Yes, it is `zip`, which feeds the same input to two processes.
     * The implementation is a bit tricky, as we have to make sure
     * that input gets fed to both `p1` and `p2`.
     */
    def zip[A, B, C](p1: Process[A, B], p2: Process[A, C]): Process[A, (B, C)] =
      (p1, p2) match {
        case (Halt(), _)                => Halt()
        case (_, Halt())                => Halt()
        case (Emit(b, t1), Emit(c, t2)) => Emit((b,c), zip(t1, t2))
        case (Await(recv1), _)          => Await { oa: Option[A] => zip(recv1(oa),    feed(oa)(p2)) }
        case (_, Await(recv2))          => Await { oa: Option[A] => zip(feed(oa)(p1), recv2(oa))    }
      }

    def feed[A, B](oa: Option[A])(p: Process[A, B]): Process[A, B] =
      p match {
        case Halt()      => p
        case Emit(h,t)   => Emit(h, feed(oa)(t))
        case Await(recv) => recv(oa)
      }

    /**
     * Using zip, we can then define `mean`. Again, this definition
     * operates in a single pass.
     */
    val mean2 = (sum zip count) |> lift { case (s, n) => s / n }

    /**
     * Exercise 6: Implement `zipWithIndex`.
     *
     * See definition on `Process` above.
     */

    /**
     * Exercise 8: Implement `exists`
     *
     * We choose to emit all intermediate values, and not halt.
     * See `existsResult` below for a trimmed version.
     */
    def exists[I](f: I => Boolean): Process[I,Boolean] =
      lift(f) |> any

    /* Emits whether a `true` input has ever been received. */
    def any: Process[Boolean,Boolean] =
      loop(false)((b:Boolean,s) => (s || b, s || b))

    /* A trimmed `exists`, containing just the final result. */
    def existsResult[I](f: I => Boolean) =
      exists(f) |> takeThrough(!_) |> dropWhile(!_) |> echo.orElse(emitOne(false))

    /**
     * Like `takeWhile`, but includes the first element that tests
     * false.
     */
    def takeThrough[I](f: I => Boolean): Process[I,I] =
      takeWhile(f) ++ echo

    /* Awaits then emits a single value, then halts. */
    def echo[I]: Process[I,I] = await(i => emitOne(i))

    def skip[I,O]: Process[I,O] = await(_ => Halt())
    def ignore[I,O]: Process[I,O] = skip.repeat

    def terminated[I]: Process[I,Option[I]] =
      await((i: I) => emitOne(Some(i), terminated[I]), emitOne(None))

    def processFile[A,B](f: java.io.File,
                         p: Process[String, A],
                         z: B)(g: (B, A) => B): IO[B] = IO {
      @annotation.tailrec
      def go(ss: Iterator[String], cur: Process[String, A], acc: B): B =
        cur match {
          case Halt() => acc
          case Await(recv) =>
            val next = if (ss.hasNext) recv(Some(ss.next()))
                       else recv(None)
            go(ss, next, acc)
          case Emit(h, t) => go(ss, t, g(acc, h))
        }
      val s = scala.io.Source.fromFile(f)
      try go(s.getLines(), p, z)
      finally s.close
    }

    /**
     * Exercise 9: Write a program that reads degrees fahrenheit as `Double` values from a file,
     * converts each temperature to celsius, and writes results to another file.
     */

    // This process defines the here is core logic, a transducer that converts input lines
    // (assumed to be temperatures in degrees fahrenheit) to output lines (temperatures in
    // degrees celsius). Left as an exercise to supply another wrapper like `processFile`
    // to actually do the IO and drive the process.
    def convertFahrenheit: Process[String,String] =
      filter((line: String) => !line.startsWith("#")) |>
      filter(line => line.trim.nonEmpty) |>
      lift(line => toCelsius(line.toDouble).toString)

    def toCelsius(fahrenheit: Double): Double =
      (5.0 / 9.0) * (fahrenheit - 32.0)
  }
}

object SimpleStreamTransducerPlayground extends App {
  import SimpleStreamTransducers.Process

  //                                initial state
  val source: Stream[Int] = Stream.unfold(0) {
    case n if n <= 5 =>
      print(".")
      //  element, next state
      Some((n, n + 1))
    case _ => None
  }
  println("source constructed (lazily)") // not completely lazy. we created the first element

  val process: Process[Int, String] = Process.lift((x: Int) => s"<<${x * 2}>>")
  println("transformer constructed (lazily)")
  
  val processed: Stream[String] = process.apply(source)
  println("transformer applied (lazily)")  // not completely lazy. we touched the first element
  
  println("running .toList on the result")
  pprint.pprintln(processed.toList) // one less because 1st already evaluated

  print("zipping:")
  val s2 = source zip Stream.from(100)
  println("zipped (lazily)")
  
  pprint.pprintln(s2.toList)
}

object SimpleStreamTransducerPlayground2 extends App {
  import SimpleStreamTransducers._
  import SimpleStreamTransducers.Process._
  
  val src = Stream(1,2,3,4,5).map(_.toDouble)
  val p: Process[Double, Double] = sum
  val dst = p(src)
  pprint.pprintln(dst.toList)
}

object SimpleStreamTransducerPlayground3 extends App {
  
  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._
  
  val s = (1 to 10).to(Stream)
  
  /** pack numbers in list of N */
  def repackBy(n: Int): Process[Int, List[Int]] =
    loops(List.empty[Int]) { (x: Int, buf) =>
      if (buf.length == n) (Some(buf), List(x))
      else (None, buf :+ x)
    } { buf => Some(buf) }

  /** pack numbers in list of N */
  def repackByV2(n: Int): Process[Int, List[Int]] =
    loops2(List.empty[Int]) {
      case (buf, Some(x)) => 
        if (buf.length == n) (Some(buf), List(x))
        else (None, buf :+ x)
      case (buf, None) => (Some(buf), buf)
    }
  
  repackBy  (4)(s).toList.pipe(println)
  repackByV2(4)(s).toList.pipe(println)
  
}

object SimpleStreamTransducerPlayground4 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._
  
  val p = emitSeq(List(1,2,3))
  val r = p(Stream.empty).toList
  pprint.pprintln(r)

  val src: Stream[Int] = (1 to 10).to(Stream)
  val p2: Process[Nothing, Int] = emitStream(src).filter(_ < 5)
  val r2 = p2(Stream.empty).toList
  pprint.pprintln(r2)

}

object SimpleStreamTransducerPlayground5 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._

  val s = (1 to 10).to(Stream)

  /** pack List[Int] to List of len 3 */
  val src: Stream[List[Int]] = List(
    List(1),            // emit: -,               state: (1)
    List(2),            // emit: -,               state: (1,2)
    List(),             // emit: -,               state: (1,2)
    List(3,4,5),        // emit: (1,2,3),         state: (4,5)
    List(6,7,8,9,11,12) // emit: (4,5,6), (7,8,9) state: (11,12)
                        // emit: (11, 12)
  ).to(Stream)

  /** implementation */
  def joinBy[A](n: Int, buf0: List[A], data0: List[A]): (List[List[A]], List[A]) = {
    
    @tailrec
    def go(bufLen: Int, buf: List[A], data: List[A], acc: List[List[A]] = Nil): (List[List[A]], List[A]) =
      data match {
        /** collected enough, add to the ac */
        case _ if bufLen == n => go(0, Nil, data, acc :+ buf)
        /** data exhausted, return what we have */
        case Nil              => (acc, buf)
        /** size of buffer is less, keep collecting */
        case a :: as          => go(bufLen + 1, buf :+ a, as, acc)
      }
    
    go(buf0.length, buf0, data0)
  }  

  /** attaching to the Streams */  
  def repackInto(n: Int): Process[List[Int], List[Int]] =
    loopssq(List.empty[Int]) { (buf, item: List[Int]) => 
      /** function to handle normal join */
      joinBy(n, buf, item)
    } {
      /** function to handle tail state */
      buf => List(buf)
    }
  
  val r = repackInto(3)(src).toList
  pprint.pprintln(r)
}

class TestJoinSpec extends AnyFunSpec with Matchers {

  import SimpleStreamTransducerPlayground5._
  
  describe("join") {
    it("1") {
      joinBy(3, List(), List(1,2)) shouldEqual (Nil, List(1,2))
      joinBy(3, List(), List(1,2,3)) shouldEqual (List(List(1,2,3)), Nil)
      joinBy(3, List(1), List(10,20,30)) shouldEqual (List(List(1,10,20)), List(30))
      joinBy(3, List(1), List(3,4,5,6,7,8)) shouldEqual (List(List(1,3,4),List(5,6,7)), List(8))
    }
  }

}

