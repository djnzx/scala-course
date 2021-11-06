cats = 
- type classes:
- instances:    cats.instances.*
- syntax:       cats.syntax.*   + lifters

type classes:
 - Show[A]          .show
 - Eq[A]            .eq       === =!=
 - Semigroup[A]     .combine  |+|
 - Monoid[A]        .empty + .combine
 - Functor[A]       .map
 - Monad[A]         .flatMap
 - Contravariant[A]
 - Or[A, B]
 - Id[A]            identity wrapper
 - MonadError[F[_], E] raiseError handleError ensure
 - ApplicativeError
 - Eval: eager, lazy, memoized
 - Writer[W, A]      / .tell / .writer
 - Reader[A, B] = f: A => B
   - chaining -> andThan
   - parallel -> mapN, parMapN, flatMap
 - State[S, A]
 - Semigroupal[F[_]]  .mapN imapN
 - Validated[A]      .valid, .invalid, .mapN
 - Apply[A]          .ap
 - Applicative[A]    .pure
 - Foldable[A]
 - Traverse[A]
 - Kleisli[F, A, B]: f: A => F[B]
   - chaining -> andThan
   - parallel -> no way
 - monad transformers

Reader[-A, B] = ReaderT[Id, A, B] 
ReaderT[F[_], -A, B] = Kleisli[F, A, B] 
Kleisli[F, -A, B]  


