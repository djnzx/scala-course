package applicative

object ApplicativeIdeas {

  /** has functionality to map */
  trait Functor[F[_]] {
    def map[A, B](fa: F[A], f: A => B): F[B]
  }
  
  /** has functionality to lift */
  trait Apply[F[_]] {
    def unit[A](a: A): F[A]
  }
  
  /** has functionality to sequence, regardless the result */
  trait Applicative[F[_]] extends Functor[F] with Apply[F] {
    /** or */
    def map2[A, B, C](fa: F[A], fb: F[B], f: (A, B) => C): F[C]
    /** or */
    def ap[A, B](fa: F[A], f: F[A => B]): F[B]
    
    /** task1: ap via map2 */
    def ap_via_map2[A, B](fa: F[A], fab: F[A => B]): F[B] =
      map2(fa, fab, (a: A, ab: A => B) => ab(a))
    
    /** task2: map2 via ap*/
    def map2_via_ap[A, B, C](fa: F[A], fb: F[B], f: (A, B) => C): F[C] = {
      val fbc: F[B => C] = map(fa, f.curried)
      ap(fb, fbc)
    }
  } 
  
}
