package fp_red.red15

import scala.language.implicitConversions

object StatefulStreamTransducers {

  object Process {

    sealed trait Process[-A, S, +B] {

      def apply(as: Stream[A]): Stream[B] = this match {
        /** we are done */
        case Halt(state, handle) => handle(state)
        /** we are ready to emit */
        case Emit(ready, state, tail) =>
          ready #::: tail(state)(as)
        /** we wait for the next element */
        case Await(state, onReceive) =>
          as match {
            case a #:: t => onReceive(state)(Some(a))(t)
            case _       => onReceive(state)(None)(Stream.empty)
          }
      }

      def map[C](fbc: B => C): Process[A, S, C] = this match {
        case Halt(os, f)              => Halt(os, (os: Option[S]) => f(os).map(fbc))
        case Emit(ready, state, tail) => Emit(ready.map(fbc), state, (os: Option[S]) => tail(os).map(fbc))
        case Await(state, onReceive)  => Await(state, (os: Option[S]) => oa => onReceive(os)(oa).map(fbc))
      }

      def flatMap[C, A2 <: A](f: B => Process[A2, S, C]): Process[A2, S, C] = this match {
        case Halt(state, handle)      => ???
        case Emit(ready, state, tail) => ???
        case Await(state, onReceive)  => ???
      }

    }

    /** 1. ready to emit values */
    case class Emit[A, S, B](
        ready: Stream[B],
        state: Option[S] = None,
        tail: Option[S] => Process[A, S, B])
        extends Process[A, S, B]

    /** 2. needs to read more data to process them */
    case class Await[A, S, B](
        state: Option[S] = None,
        onReceive: Option[S] => Option[A] => Process[A, S, B])
        extends Process[A, S, B]

    /** termination with/without state */
    case class Halt[A, S, B](
        state: Option[S] = None,
        handle: Option[S] => Stream[B] = (_: Option[S]) => Stream.empty)
        extends Process[A, S, B]

  }
}
