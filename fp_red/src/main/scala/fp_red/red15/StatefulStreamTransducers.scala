package fp_red.red15

import scala.language.implicitConversions

object StatefulStreamTransducers {

  sealed trait Process[-A, +B, +S] {
    import Process._

    /** syntax to write handle function */
    def handle[S2 >: S]: Handle[A, B, S2] = {
      /** process next element + state from previous step */
      case (Some(a), Some(s)) => ???
      /** process next element + NO state from previous step */
      case (Some(a), None)    => ???
      /** end of the stream + state from previous step */
      case (None,    Some(s)) => ???
      /** end of the stream + NO state from previous step */
      case (None,    None)    => ???
    }
    
    def apply(as: Stream[A]): Stream[B] = ??? 
//      this match {
//      /** terminate */
//      case Halt()     => Stream.empty
//      /** emit value and keep processing */
//      case Emit(h, t) => h #:: t(as)
//      case Await(f)   => as match {
//        case h #:: t => f(Some(h))(t)
//        case _       => f(None   )(Stream.empty)
//      }
//      case AwaitS(s, f) => as match {
//        case h #:: t => f(Some(h), s)(t)
//        case _       => f(None,    s)(Stream.empty)
//      }
//    }

//    def repeat: Process[A, B, S] = {
//
//      def go(p: Process[A, B, S]): Process[A, B, S] = p match {
//        case Halt() => go(this)
//        case Await(onReceive) => Await[A, B, S] {
//          case None => onReceive(None)
//          case a    => go(onReceive(a))
//        }
//        // TODO: handle this !!!
//        case AwaitS(s, onReceive) => ??? 
//        case Emit(h, t) => Emit(h, go(t))
//      }
//
//      go(this)
//    }

//    def repeatN(n: Int): Process[A, B] = {
//
//      def go(n: Int, p: Process[A, B]): Process[A, B] = p match {
//        case Halt() if n > 0 => go(n - 1, this)
//        case Halt() => Halt()
//        case Await(recv) => Await {
//          case None => recv(None)
//          case oi   => go(n, recv(oi))
//        }
//        case Emit(h, t) => Emit(h, go(n,t))
//      }
//
//      go(n, this)
//    }
//
//    /**
//     * `Process` can be thought of as a sequence of values of type `O`
//     * and many of the operations that would be defined for `List[O]`
//     * can be defined for `Process[I,O]`, for instance `map`, `++` and
//     * `flatMap`. The definitions are analogous.
//     */
//    def map[O2](f: B => O2): Process[A, O2] = this match {
//      case Halt()      => Halt()
//      case Emit(h, t)  => Emit(f(h), t map f)
//      case Await(recv) => Await(recv andThen (_ map f))
//    }
//
//    def map_pipe[O2](f: B => O2): Process[A,O2] = this |> lift(f)
//
//    def ++(p: => Process[A, B]): Process[A, B] = this match {
//      case Halt() => p
//      case Emit(h, t) => Emit(h, t ++ p)
//      case Await(recv) => Await(recv andThen (_ ++ p))
//    }
//    def flatMap[O2](f: B => Process[A, O2]): Process[A, O2] = this match {
//      case Halt() => Halt()
//      case Emit(h, t) => f(h) ++ t.flatMap(f)
//      case Await(recv) => Await(recv andThen (_ flatMap f))
//    }
//
//    /**
//     * Exercise 5: Implement `|>`. Let the types guide your implementation.
//     */
//    def |>[O2](p2: Process[B, O2]): Process[A, O2] =
//      p2 match {
//        case Halt()     => Halt()
//        case Emit(h, t) => Emit(h, this |> t)
//        case Await(f)   => this match {
//          case Emit(h, t) => t |> f(Some(h))
//          case x @ Halt() => x |> f(None)
//          case Await(g)   => Await { i: Option[A] => g(i) |> p2 }
//        }
//      }
//
//    /**
//     * Feed `in` to this `Process`. Uses a tail recursive loop as long
//     * as `this` is in the `Await` state.
//     */
//    def feed(in: Seq[A]): Process[A, B] = {
//
//      @tailrec
//      def go(in: Seq[A], cur: Process[A, B]): Process[A, B] =
//        cur match {
//          case Halt() => Halt()
//          case Await(onReceive) =>
//            if (in.nonEmpty) go(in.tail, onReceive(Some(in.head)))
//            else cur
//          case Emit(h, t) => Emit(h, t.feed(in))
//        }
//
//      go(in, this)
//    }
//
//    /**
//     * As an example of `repeat`, see `Process.filter`. We define
//     * a convenience function here for composing this `Process`
//     * with a `Process` that filters the output type `O`.
//     */
//    def filter(f: B => Boolean): Process[A,B] =
//      this |> Process.filter(f)
//
//    /** Exercise 7: see definition below. */
//    def zip[O2](p: Process[A,O2]): Process[A,(B,O2)] =
//      Process.zip(this, p)
//
//    /** Exercise 6: Implement `zipWithIndex` */
//    def zipWithIndex: Process[A, (B, Int)] =
//      this zip[Int] (count[A].map(_ - 1))
//
//    /** Add `p` to the fallback branch of this process */
//    def orElse(p: Process[A, B]): Process[A, B] = this match {
//      case Halt() => p
//      case Await(f) => Await {
//        case None => p
//        case x => f(x)
//      }
//      case _ => this
//    }
  }

  object Process {

    /** option to handle end of the stream */
    type Handle      [-A, +B, S] = (Option[A], Option[S]) => Process[A, B, S]
    type HandleSimple[-A, +B, S] = Option[A]              => Process[A, B, S]
    type HandleState [-A, +B, S] = (Option[A], S)         => Process[A, B, S]

    /** emitting a value to the output */
    case class Emit[A, B, S](item: B, tail: Process[A, B, S] = Halt[A, B, S]()) extends Process[A, B, S]

    /** reading a value from its input, NO state */
    case class Await[A, B, S](onReceive: HandleSimple[A, B, S]) extends Process[A, B, S]
    /** reading a value from its input, HAVE state */
    case class AwaitS[A, B, S](s: S, onReceive: HandleState[A, B, S]) extends Process[A, B, S]

    /** termination with/without state*/
    case class Halt[A, B, S](s: Option[S] = None) extends Process[A, B, S]

//    /** emitting ONE value, without carrying to input value and its type */
//    def emitOne[A, B, S](item: B, tail: Process[A, B, S] = Halt[A, B, S]()): Process[A, B, S] =
//      Emit(item, tail)
//
//    /** emitting SEQ of values, without carrying to input value and its type */
//    def emitSeq[A, B, S](items: Seq[B], tail: Process[A, B, S] = Halt[A, B, S]()): Process[A, B, S] = items match {
//      case b +: bs => Emit(b, emitSeq(bs, tail))
//      case _       => tail
//    }
//
//    /** emitting STREAM of values, without carrying to input value and its type */
//    def emitStream[A, B, S](items: Stream[B], tail: Process[A, B, S] = Halt[A, B, S]()): Process[A, B, S] = items match {
//      case b #:: bs => Emit(b, emitStream(bs, tail))
//      case _        => tail
//    }
//
//    /** lift function A => B to Process[A, B, S] */
//    def liftOne[A, B, S](f: A => B): Process[A, B, S] =
//      Await[A, B, S] {
//        case Some(i) => emitOne(f(i))
//        case None    => Halt()
//      }
//
//    /** lifting whole stream just repeating [[liftOne]] */
//    def lift[A, B, S](f: A => B): Process[A, B, S] =
//      liftOne(f).repeat

//    /**
//      * transducer can do way more than map:
//      * insert, delete, filter, ...
//      */
//    def filter[I](f: I => Boolean): Process[I, I] =
//      Await[I, I] {
//        case Some(i) if f(i) => emitOne(i)
//        case _ => Halt()
//      }.repeat
//
//    /**
//      * A helper function to await an element
//      * or fall back to another process
//      * if there is no input.
//      */
//    def await[I,O](f: I => Process[I, O],
//                   fallback: Process[I, O] = Halt[I,O]()
//                  ): Process[I,O] =
//      Await[I,O] {
//        case Some(i) => f(i)
//        case None    => fallback
//      }
//
//    /**
//     * Here's a typical `Process` definition that requires tracking some
//     * piece of state (in this case, the running total):
//     */
//    def sum: Process[Double, Double] = {
//
//      def go(acc: Double): Process[Double, Double] =
//        await { x =>
//          emitOne(x + acc, go(x + acc))
//        }
//
//      go(0.0)
//    }
//
//    /** count recursive */
//    def count1[I]: Process[I, Int] = {
//      def go(count: Int): Process[I, Int] =
//        await { _ =>
//          emitOne(count + 1, go(count + 1))
//        }
//
//      go(0)
//    }
//    /**
//      * Here's one implementation, with three stages - we map all inputs
//      * to 1.0, compute a running sum, then finally convert the output
//      * back to `Int`. The three stages will be interleaved - as soon
//      * as the first element is examined, it will be converted to 1.0,
//      * then added to the running total, and then this running total
//      * will be converted back to `Int`, then the `Process` will examine
//      * the next element, and so on.
//      */
//    def count[I]: Process[I, Int] =
//      lift { _: I => 1.0 } |> sum |> lift(_.toInt)
//
//    /**
//      * Implement `mean`.
//      * This is an explicit recursive definition. We'll factor out a
//      * generic combinator shortly.
//      */
//    def mean: Process[Double, Double] = {
//
//      def go(sum: Double, count: Double): Process[Double, Double] =
//        await { d: Double =>
//          emitOne((sum + d) / (count + 1), go(sum + d, count + 1))
//        }
//
//      go(0.0, 0.0)
//    }
//    /**
//      * discovering the new primitive !
//      */
//    def loop[S, I, O](z: S)(f: (I, S) => (O, S)): Process[I, O] =
//      await { i: I => f(i, z) match {
//        case (o, s2) => emitOne(o, loop(s2)(f))
//      }}
//
//    /** stateful iteration */
//    def loops[S, I, O](z: S)(f: (I, S) => (Option[O], S))(ft: S => Option[O]): Process[I, O] =
//      await(
//        { // handle the next item
//          i: I => f(i, z) match {
//            case (None, s2)    => loops(s2)(f)(ft)
//            case (Some(o), s2) => emitOne[I, O](o, loops(s2)(f)(ft))
//        }},
//        { // carefully handle state on finished stream
//          ft(z) match {
//            case None    => Halt[I, O]()
//            case Some(x) => emitOne[I, O](x)
//        }}
//      )
//
//    /** stateful iteration, V2 */
//    def loops2[S, A, B](s: S)(f: (S, Option[A]) => (Option[B], S)): Process[A, B] =
//      await(
//        { i: A =>
//          f(s, Some(i)) match {
//            case (Some(o), s2) => emitOne(o, loops2(s2)(f))
//            case (None,    s2) => loops2(s2)(f)
//          }
//        },
//        f(s, None) match {
//          case (Some(x), _) => emitOne(x)
//          case (None,    _) => Halt()
//        }
//      )
//
//    /**
//      * every new item ot type A potentially can produce 0 or more elements of type B
//      * to handle that, probably we need emit items recursively
//      */
//    def loopssq[S, A, B](s: S)(f: (S, A) => (Seq[B], S))(residual: S => Seq[B]): Process[A, B] =
//      await(
//        { a: A =>
//          f(s, a) match {
//            case (Seq(), s2) => loopssq(s2)(f)(residual)
//            case (bs,    s2) => emitSeq(bs, loopssq(s2)(f)(residual))
//          }
//        },
//        residual(s) match {
//          case Seq() => Halt()
//          case bs => emitSeq(bs)
//        }
//      )
//
//    /** stream version */
//    def loopsst[S, A, B](s: S)(f: (S, Option[A]) => (Stream[B], S)): Process[A, B] =
//      await(
//        { i: A =>
//          f(s, Some(i)) match {
//            case (Seq(), s2) => loopsst(s2)(f)
//            case (bs,    s2) => emitStream(bs, loopsst(s2)(f))
//          }
//        },
//        f(s, None) match {
//          case (Seq(), _) => Halt()
//          case (bs,    _) => emitStream(bs)
//        }
//      )
//
//    /**
//      * to make it lazy even in term of one chunk,
//      * we emit sequence of elements with type (B)
//      * and in the end we emit the state (S)
//      *
//      * @param s - initial state
//      * @param f - function to fold the state with source element of type A to produce the Stream of type B and state in the end
//      * @param rf - function to process the state after chunk processing
//      * @tparam S - state
//      * @tparam A - source stream type
//      * @tparam B - target stream type
//      */
//    def loopsst2[S, A, B](s: S)(f: (S, Option[A]) => Stream[Either[S, B]])(rf: S => Stream[B] = (_: S) => Stream.empty): Process[A, B] =
//      await(
//        { i: A =>
//          f(s, Some(i)) match {
//            case h #:: t => h match {
//              case b: B  => ??? // lazily emit one by one until we have type b
//              case s2: S => ??? // use state S to further process of elements of type A
//            }
//            case _       => sys.error("wrong state")
//          }
////          match {
////            case (Seq(), s2) => loopsst(s2)(f)
////            case (bs,    s2) => emitStream(bs, loopsst(s2)(f))
////          }
//          ???
//        },
//        emitStream(rf(s))
//      )
//
//    /** Process forms a monad, and we provide monad syntax for it  */
//    def monad[A]: Monad[({ type f[x] = Process[A, x]})#f] =
//      new Monad[({ type f[x] = Process[A,x]})#f] {
//        def unit[B](o: => B): Process[A,B] = emitOne(o)
//        def flatMap[B, B2](p: Process[A, B])(f: B => Process[A, B2]): Process[A,B2] =
//          p flatMap f
//      }
//
//    /** enable monadic syntax */
//    implicit def toMonadic[I, O](a: Process[I, O]) = monad[I].toMonadic(a)
//
//    def take[I](n: Int): Process[I, I] =
//      if (n <= 0) Halt()
//      else await { i => emitOne(i, take[I](n - 1)) }
//
//    def drop[I](n: Int): Process[I, I] =
//      if (n <= 0) id
//      else await { _ => drop[I](n - 1) }
//
//    def takeWhile[I](f: I => Boolean): Process[I,I] =
//      await { i =>
//        if (f(i)) emitOne(i, takeWhile(f))
//        else      Halt()
//      }
//
//    def dropWhile[I](f: I => Boolean): Process[I,I] =
//      await { i =>
//        if (f(i)) dropWhile(f)
//        else      emitOne(i,id)
//      }
//
//    /* The identity `Process`, just repeatedly echos its input. */
//    def id[I]: Process[I,I] = lift(identity)
//
//    /* Exercise 4: Implement `sum` and `count` in terms of `loop` */
//
//    def sum2: Process[Double,Double] =
//      loop(0.0)((d:Double, acc) => (acc+d,acc+d))
//
//    def count3[I]: Process[I,Int] =
//      loop(0)((_:I,n) => (n+1,n+1))
//
//    /**
//     * Exercise 7: Can you think of a generic combinator that would
//     * allow for the definition of `mean` in terms of `sum` and
//     * `count`?
//     *
//     * Yes, it is `zip`, which feeds the same input to two processes.
//     * The implementation is a bit tricky, as we have to make sure
//     * that input gets fed to both `p1` and `p2`.
//     */
//    def zip[A, B, C](p1: Process[A, B], p2: Process[A, C]): Process[A, (B, C)] =
//      (p1, p2) match {
//        case (Halt(), _)                => Halt()
//        case (_, Halt())                => Halt()
//        case (Emit(b, t1), Emit(c, t2)) => Emit((b,c), zip(t1, t2))
//        case (Await(recv1), _)          => Await { oa: Option[A] => zip(recv1(oa),    feed(oa)(p2)) }
//        case (_, Await(recv2))          => Await { oa: Option[A] => zip(feed(oa)(p1), recv2(oa))    }
//      }
//
//    def feed[A, B](oa: Option[A])(p: Process[A, B]): Process[A, B] =
//      p match {
//        case Halt()      => p
//        case Emit(h,t)   => Emit(h, feed(oa)(t))
//        case Await(recv) => recv(oa)
//      }
//
//    /**
//     * Using zip, we can then define `mean`. Again, this definition
//     * operates in a single pass.
//     */
//    val mean2 = (sum zip count) |> lift { case (s, n) => s / n }
//
//    /**
//     * Exercise 6: Implement `zipWithIndex`.
//     *
//     * See definition on `Process` above.
//     */
//
//    /**
//     * Exercise 8: Implement `exists`
//     *
//     * We choose to emit all intermediate values, and not halt.
//     * See `existsResult` below for a trimmed version.
//     */
//    def exists[I](f: I => Boolean): Process[I,Boolean] =
//      lift(f) |> any
//
//    /* Emits whether a `true` input has ever been received. */
//    def any: Process[Boolean,Boolean] =
//      loop(false)((b:Boolean,s) => (s || b, s || b))
//
//    /* A trimmed `exists`, containing just the final result. */
//    def existsResult[I](f: I => Boolean) =
//      exists(f) |> takeThrough(!_) |> dropWhile(!_) |> echo.orElse(emitOne(false))
//
//    /**
//     * Like `takeWhile`, but includes the first element that tests
//     * false.
//     */
//    def takeThrough[I](f: I => Boolean): Process[I,I] =
//      takeWhile(f) ++ echo
//
//    /* Awaits then emits a single value, then halts. */
//    def echo[I]: Process[I,I] = await(i => emitOne(i))
//
//    def skip[I,O]: Process[I,O] = await(_ => Halt())
//    def ignore[I,O]: Process[I,O] = skip.repeat
//
//    def terminated[I]: Process[I,Option[I]] =
//      await((i: I) => emitOne(Some(i), terminated[I]), emitOne(None))

  }
}

