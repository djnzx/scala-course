package catsx.c099monaderror

import cats.MonadError

import scala.util.Try

object C099MonadErrorUsage2 extends App {

  def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable]): F[Int] =
    if (age >= 18) me.pure(age)
    else          me.raiseError(sys.error(s"age must me >= 18, given: $age"))

  type R1[A] = Either[Throwable, A]
  val r1: R1[Int] = validateAdult[R1](18)
  println(r1)

  type R2[A] = Try[A]
  val r2: R2[Int] = validateAdult[R2](19)
  println(r2)

}
