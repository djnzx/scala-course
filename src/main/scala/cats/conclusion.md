cats = 
- type classes:
- instances:    cats.instances.*
- syntax:       cats.syntax.*   + lifters

type classes:
 - Show[A]          .show
 - Eq[A]            .eq       === =!=
 - Semigroup[A]     .combine  |+|
 - Monoid[A]        .empty 
 - Functor[A]       .map
 - Contravariant[A]
 - Or[A, B]
 - Monad[A]         .flatMap
 - Id[A]
 - MonadError[F[_], E] raiseError handleError ensure
 - ApplicativeError
 - Eval: eager, lazy, memoized
 - Writer[W, A]      / .tell / .writer
 - Reader[A, B] = f: A=>B
 - State[S, A]
 - Semigroupal[F[_]]  .mapN imapN
 - Validated[A]
 - Apply[A]          .ap
 - Applicative[A]    .pure
 - Foldable[A]
 - Traverse[A]
 - Kleisli[F, A, B]: f: A=>F[B]
 - monad transformers
 
 


