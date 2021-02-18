package fp_red.red15

import fp_red.red13.{IO, Monad}

import scala.annotation.tailrec
import scala.language.{implicitConversions, postfixOps}

object SimpleStreamTransducers {

  /**
    * We now introduce a type, `Process`, representing pure, single-input
    * stream transducers. It can be in of three states, it can be: 
    * - emitting a value to the output (`Emit`),
    * - reading a value from its input (`Await`),
    * - signaling termination via `Halt`.
    */
  sealed trait Process[I,O] {
    import Process._

    /**
      * this is a driver (interpreter) of our stream
      * A `Process[I,O]` can be used to transform
      * `Stream[I]` to a `Stream[O]`
      */
    def apply(s: Stream[I]): Stream[O] = this match {
      case Halt()      => Stream.empty
      case Await(recv) => s match {
        case h #:: t => recv(Some(h))(t)  // Non-empty stream
        case xs      => recv(None   )(xs) // Empty stream
      }
      case Emit(h, t)  => h #:: t(s)
    }

    /**
     * See `Process.lift` for a typical repeating `Process`
     * definition expressed with explicit recursion */

    /**
     * `Process`  definitions can often be expressed without explicit
     * recursion, by repeating some simpler `Process` forever */
    def repeat: Process[I, O] = {
      
      def go(p: Process[I, O]): Process[I,O] = p match {
        case Halt() => go(this)
        case Await(recv) => Await {
          case None => recv(None)
          case i => go(recv(i))
        }
        case Emit(h, t) => Emit(h, go(t))
      }
      
      go(this)
    }

    def repeatN(n: Int): Process[I, O] = {
      
      def go(n: Int, p: Process[I, O]): Process[I, O] = p match {
        case Halt() if n > 0 => go(n - 1, this)
        case Halt() => Halt()
        case Await(recv) => Await {
          case None => recv(None)
          case i => go(n,recv(i))
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
    def map[O2](f: O => O2): Process[I,O2] = this match {
      case Halt() => Halt()
      case Emit(h, t) => Emit(f(h), t map f)
      case Await(recv) => Await(recv andThen (_ map f))
    }
    def map_pipe[O2](f: O => O2): Process[I,O2] = this |> lift(f)

    def ++(p: => Process[I,O]): Process[I,O] = this match {
      case Halt() => p
      case Emit(h, t) => Emit(h, t ++ p)
      case Await(recv) => Await(recv andThen (_ ++ p))
    }
    def flatMap[O2](f: O => Process[I,O2]): Process[I,O2] = this match {
      case Halt() => Halt()
      case Emit(h, t) => f(h) ++ t.flatMap(f)
      case Await(recv) => Await(recv andThen (_ flatMap f))
    }

    /**
     * Exercise 5: Implement `|>`. Let the types guide your implementation.
     */
    def |>[O2](p2: Process[O, O2]): Process[I, O2] =
      p2 match {
        case Halt()    => Halt()
        case Emit(h,t) => Emit(h, this |> t)
        case Await(f)  => this match {
          case Emit(h,t)  => t |> f(Some(h))
          case x @ Halt() => x |> f(None)
          case Await(g)   => Await { i: Option[I] => g(i) |> p2 }
        }
      }

    /**
     * Feed `in` to this `Process`. Uses a tail recursive loop as long
     * as `this` is in the `Await` state.
     */
    def feed(in: Seq[I]): Process[I,O] = {
      @tailrec
      def go(in: Seq[I], cur: Process[I,O]): Process[I,O] =
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
    def filter(f: O => Boolean): Process[I,O] =
      this |> Process.filter(f)

    /** Exercise 7: see definition below. */
    def zip[O2](p: Process[I,O2]): Process[I,(O,O2)] =
      Process.zip(this, p)

    /** Exercise 6: Implement `zipWithIndex` */
    def zipWithIndex: Process[I,(O, Int)] = 
      this zip (count map (_ - 1))

    /** Add `p` to the fallback branch of this process */
    def orElse(p: Process[I,O]): Process[I,O] = this match {
      case Halt() => p
      case Await(recv) => Await {
        case None => p
        case x => recv(x)
      }
      case _ => this
    }
  }

  object Process {

    /** REPRESENTATION:
      * 
      * emitting a value to the output */
    case class Emit[I,O](
      head: O,
      tail: Process[I, O] = Halt[I, O]()
    ) extends Process[I, O]
    
    /** reading a value from its input */
    case class Await[I, O](
      recv: Option[I] => Process[I, O]
    ) extends Process[I, O]
    
    /** termination */
    case class Halt[I, O]() extends Process[I, O]

    /** emitting ONE value, without carrying to input type */
    def emit[I, O](head: O, tail: Process[I, O] = Halt[I, O]()): Process[I, O] =
      Emit(head, tail)

    /**
      * We can convert any function `f: I => O` to a `Process[I,O]`. We
      * simply `Await`, then `Emit` the value received, transformed by `f`.
      * We terminate stream after 1st element 
      */
    def liftOne[I, O](f: I => O): Process[I,O] =
      Await {
        case Some(i) => emit(f(i))
        case None    => Halt()
      }
      
    /** lifting whole stream just repeating [[liftOne]] */
    def lift[I,O](f: I => O): Process[I,O] =
      liftOne(f).repeat

    /**
      * transducer can do way more than map:
      * insert, delete, filter, ...
      */
    def filter[I](f: I => Boolean): Process[I,I] =
      Await[I,I] {
        case Some(i) if f(i) => emit(i)
        case _ => Halt()
      }.repeat

    /**
      * A helper function to await an element 
      * or fall back to another process
      * if there is no input.
      */
    def await[I,O](f: I => Process[I,O],
                   fallback: Process[I,O] = Halt[I,O]()): Process[I,O] =
      Await[I,O] {
        case Some(i) => f(i)
        case None => fallback
      }
      
    /**
     * Here's a typical `Process` definition that requires tracking some
     * piece of state (in this case, the running total):
     */
    def sum: Process[Double,Double] = {
      def go(acc: Double): Process[Double,Double] =
        await(d => emit(d+acc, go(d+acc)))
        
      go(0.0)
    }
    
    /** count recursive */
    def count1[I]: Process[I, Int] = {
      def go(count: Int): Process[I, Int] =
        await(_ => emit(count + 1, go(count + 1)))
      
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
    def count[I]: Process[I,Int] =
      lift((i: I) => 1.0) |> sum |> lift(_.toInt)

    /**
      * Implement `mean`.
      * This is an explicit recursive definition. We'll factor out a
      * generic combinator shortly.
      */
    def mean: Process[Double,Double] = {
      def go(sum: Double, count: Double): Process[Double,Double] =
        await((d: Double) => emit((sum+d) / (count+1), go(sum+d, count+1)))
      go(0.0, 0.0)
    }
    /**
      * discovering the new primitive !
      */
    def loop[S,I,O](z: S)(f: (I,S) => (O,S)): Process[I,O] =
      await((i: I) => f(i,z) match {
        case (o,s2) => emit(o, loop(s2)(f))
      })

    /** Process forms a monad, and we provide monad syntax for it  */
    def monad[I]: Monad[({ type f[x] = Process[I,x]})#f] =
      new Monad[({ type f[x] = Process[I,x]})#f] {
        def unit[O](o: => O): Process[I,O] = emit(o)
        def flatMap[O,O2](p: Process[I,O])(f: O => Process[I,O2]): Process[I,O2] =
          p flatMap f
      }

    /** enable monadic syntax */
    implicit def toMonadic[I,O](a: Process[I,O]) = monad[I].toMonadic(a)

    /*
     * Exercise 1: Implement `take`, `drop`, `takeWhile`, and `dropWhile`.
     */
    def take[I](n: Int): Process[I,I] =
      if (n <= 0) Halt()
      else await(i => emit(i, take[I](n-1)))

    def drop[I](n: Int): Process[I,I] =
      if (n <= 0) id
      else await(i => drop[I](n-1))

    def takeWhile[I](f: I => Boolean): Process[I,I] =
      await(i =>
        if (f(i)) emit(i, takeWhile(f))
        else      Halt())

    def dropWhile[I](f: I => Boolean): Process[I,I] =
      await(i =>
        if (f(i)) dropWhile(f)
        else      emit(i,id))

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
    def zip[A,B,C](p1: Process[A,B], p2: Process[A,C]): Process[A,(B,C)] =
      (p1, p2) match {
        case (Halt(), _) => Halt()
        case (_, Halt()) => Halt()
        case (Emit(b, t1), Emit(c, t2)) => Emit((b,c), zip(t1, t2))
        case (Await(recv1), _) =>
          Await((oa: Option[A]) => zip(recv1(oa), feed(oa)(p2)))
        case (_, Await(recv2)) =>
          Await((oa: Option[A]) => zip(feed(oa)(p1), recv2(oa)))
      }

    def feed[A,B](oa: Option[A])(p: Process[A,B]): Process[A,B] =
      p match {
        case Halt() => p
        case Emit(h,t) => Emit(h, feed(oa)(t))
        case Await(recv) => recv(oa)
      }

    /**
     * Using zip, we can then define `mean`. Again, this definition
     * operates in a single pass.
     */
    val mean2 = (sum zip count) |> lift { case (s,n) => s / n }

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
      exists(f) |> takeThrough(!_) |> dropWhile(!_) |> echo.orElse(emit(false))

    /**
     * Like `takeWhile`, but includes the first element that tests
     * false.
     */
    def takeThrough[I](f: I => Boolean): Process[I,I] =
      takeWhile(f) ++ echo

    /* Awaits then emits a single value, then halts. */
    def echo[I]: Process[I,I] = await(i => emit(i))

    def skip[I,O]: Process[I,O] = await(i => Halt())
    def ignore[I,O]: Process[I,O] = skip.repeat

    def terminated[I]: Process[I,Option[I]] =
      await((i: I) => emit(Some(i), terminated[I]), emit(None))

    def processFile[A,B](f: java.io.File,
                         p: Process[String, A],
                         z: B)(g: (B, A) => B): IO[B] = IO {
      @annotation.tailrec
      def go(ss: Iterator[String], cur: Process[String, A], acc: B): B =
        cur match {
          case Halt() => acc
          case Await(recv) =>
            val next = if (ss.hasNext) recv(Some(ss.next))
                       else recv(None)
            go(ss, next, acc)
          case Emit(h, t) => go(ss, t, g(acc, h))
        }
      val s = scala.io.Source.fromFile(f)
      try go(s.getLines, p, z)
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
}

