package fp_red.red06

import fp_red.red06.State._

import scala.annotation.tailrec

/**
  * State[S, A] - just a wrapper over the function: S => (A, S)
  * 
  * by running state
  * we get the value and a new state
  */
case class State[S, +A](run: S => (A, S)) {
  /**
    * map means:
    * 
    * 1. we run the state function;
    * 2. we apply the given function;
    * 3. we return the ORIGINAL state and a new mapped value;
    */
  def map_1[B](f: A => B): State[S, B] = State[S, B] { s: S =>
    val (a, _) = run(s)
    (f(a), s)
  }
  
  /** map via flatMap */
  def map[B](f: A => B): State[S, B] =
    flatMap(a => unit(f(a)))
    
  /** map2 via flatMap */
  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    flatMap(a => sb.map(b => f(a, b)))
    
  /**
    * flatMap means:
    *
    * 1. we run the state function. get the A and S2;
    * 2. we apply the given function to a value produced, get the new function !!!;
    * 3. run the function produced on a new state S2;
    * 3. we return the NEW state S3 and a new value B;
    */
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a: A, s2: S) = run(s)
    val sb: State[S, B] = f(a) // produces new function
    val bAndS3: (B, S) = sb.run(s2)
    bAndS3
  })
}

/**
  * complement object contains a lot of useful methods
  */
object State {
  /**
    * Doesn't touch the state, just produces a value.
    * 
    * Can be treated as a lifter for a value to a context
    */
  def unit[S, A](a: A): State[S, A] =
    State(s => (a, s))

  /** This implementation uses a loop internally and is the same recursion
    * pattern as a left fold.
    * We could also use a collection.mutable.ListBuffer internally. */
  def sequence[S, A](sas: List[State[S, A]]): State[S, List[A]] = {

    @tailrec
    def go(s: S, tail: List[State[S, A]], acc: List[A]): (List[A], S) =
      tail match {
        case Nil => (acc.reverse, s)
        case h :: t => h.run(s) match { case (a, s2) => go(s2, t, a :: acc) }
      }

    State((s: S) => go(s, sas, List.empty))
  }

  /** The idiomatic recursive solution is expressed via foldRight */
  def sequenceViaFoldRight[S, A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.foldRight(unit[S, List[A]](List.empty[A]))((f, acc) => f.map2(acc)(_ :: _))

  /** We can also write the loop using a left fold. This is tail recursive like the
  * previous solution, but it reverses the list _before_ folding it instead of after.
  * You might think that this is slower than the `foldRight` solution since it
  * walks over the list twice, but it's actually faster! The `foldRight` solution
  * technically has to also walk the list twice, since it has to unravel the call
  * stack, not being tail recursive. And the call stack will be as tall as the list
  * is long. */
  def sequenceViaFoldLeft[S,A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.reverse
      .foldLeft(unit[S, List[A]](List.empty[A])) {
        (acc: State[S, List[A]], f: State[S, A]) =>
          f.map2(acc)(_ :: _)
      }

  /**
    * gets current state
    * 
    * State.get.run(s) -> (s, s)
    */
  def get[S]: State[S, S] = State(s => (s, s))

  /**
    * sets (overrides) the current state
    * 
    * State.set(s2).run(state_whatever) => ((), s2)
    */
  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
  
  /**
    * applies given function to current state.
    * flatMap based implementation
    */
  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get[S]  // Gets the current state and assigns it to `s`.
    s2 = f(s)    // applies `f` to `s`.
    _ <- set(s2) // Sets the new state to `s2`.
  } yield ()

}

