package advanced

object TypeLambda3 extends App {

  // Functor has one `hole`
  trait Functor[F[_]]

  // MonadError has two `holes`
  trait MonadError[F[_], E]

  // Functor can be specified as a type bounds
  class Repository1[F[_]: Functor]

  // but MonadError can not

  // we can introduce type alias
  type MonadErrorThrowable[F[_]] = MonadError[F, Throwable]

  // and use it
  class Repository2[F[_]: MonadErrorThrowable]

  // or use type lambda and not declare alias
  class Repository3[F[_]: ({ type MET[f[_]] = MonadError[f, Throwable] })#MET]

  // or use type lambda and not declare alias
  class Repository4[F[_]: MonadError[*[_], Throwable]]

}
