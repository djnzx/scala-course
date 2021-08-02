package catsx.c099monaderror

import cats.MonadError

import scala.util.Try

object C099MonadErrorUsage2 extends App {

  def validate(age: Int) = age >= 18

  def validateAndRepresent[F[_]](age: Int)(implicit me: MonadError[F, Throwable]): F[Int] =
    if (validate(age)) me.pure(age)
    else               me.raiseError(sys.error(s"age must be >= 18, given: $age"))

  type R1[A] = Either[Throwable, A]
  val r1: R1[Int] = validateAndRepresent[R1](18)
  println(r1)

  type R2[A] = Try[A]
  val r2: R2[Int] = validateAndRepresent[R2](19)
  println(r2)

}
