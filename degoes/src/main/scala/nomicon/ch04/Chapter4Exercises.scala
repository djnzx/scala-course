package nomicon.ch04

import zio._

object Chapter4Exercises extends App {

  object task1 {
    // will definitely fail 
    def failWithMessage(string: String) =
      ZIO.fail(throw new Error(string))
    // won't fail at all
    def allCaught =
      failWithMessage("Hello")
        /**
          * catchAll:       Nothing   => ZIO[R,E,A]
          * catchAllDefect: Throwable => ZIO[R,E,A]
          */
        .catchAllDefect(th => ZIO.succeed(th.getMessage))

    val app = for {
      m <- allCaught
      _ <- console.putStrLn(m)
    } yield ()
  }
  object task2 {
    /**
      * Using the ZIO#foldCauseM operator and the Cause#defects method,
      * implement the following function. This function should take the effect,
      * ??? inspect defects, and if a suitable defect is found, ???
      * it should recover from the error with the help of the specified function,
      * which generates a new success value for such a defect.
      */
    def recoverFromSomeDefects[R, E, A](zio: ZIO[R, E, A])(f: Throwable => Option[A]): ZIO[R, E, A] =
      zio.foldCauseM(
        cause =>
          cause.defects
            .headOption
            .flatMap(f(_))
            .map(ZIO.succeed(_))
            .getOrElse(zio),
        _ => zio
      )

    /**
      * in case of ZIO.effect      - all EX's will be collected in cause.failures: List[E]
      * in case of ZIO.effectTotal - all EX's will be collected in cause.defects:  List[Throwable]
      */
    // will fail with ArithmeticException ~ Throwable
    val effect    = ZIO.effectTotal(10/0)
    val recovered = recoverFromSomeDefects(effect) { t: Throwable => Some(t.getMessage.length) }
      
    def app = for {
      a <- recovered
      _ <- console.putStrLn(a.toString)
    } yield a

  }
  object task3 {
    // TODO: how to inject console in a right way ???
    def logFailures[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] =
      zio.foldCauseM(
        (clause: Cause[E]) => ZIO.effectTotal(println(clause.prettyPrint)) *> zio,
        _ => zio
      )
      
    val app = logFailures(ZIO.succeed(5/1)).flatMap(a => console.putStrLn(a.toString))
  }
  object task4 {
    case class Symptom(x: String)
    def print(a: Any) = ZIO.effectTotal(println(a))
    def onAnyFailure[R, E, A](zio: ZIO[R, E, A], handler: ZIO[R, E, Any]): ZIO[R, E, A] =
      zio.foldCauseM(
        _ => handler *> zio,
        a => ZIO.succeed(a)
      )
      
    val code = ZIO.fail(Symptom("S"))
    val h = console.putStrLn(">> my specific handler <<")
    val app = onAnyFailure(code, h)
    
  }
  object task5 {
    def ioException[R, A](zio: ZIO[R, Throwable, A]): ZIO[R, java.io.IOException, A] =
      zio.refineOrDieWith {
        case x: java.io.IOException => x
      } (identity)
  }
  object task6 {
    val parseNumber: ZIO[Any, Throwable, Int] = ZIO.effect("foo".toInt)
    val parseRefined: ZIO[Any, NumberFormatException, Int] = 
      parseNumber
        .refineToOrDie[NumberFormatException]
  }
  object task7 {
    def left[R, E, A, B](zio: ZIO[R, E, Either[A, B]]) = zio.foldM(
      e               => ZIO.fail(Left(e)),
      {
        case Right(b) => ZIO.fail(Right(b))
        case Left(a)  => ZIO.succeed(a)
      }
    )
    
    def unleft[R, E, A, B](zio: ZIO[R, Either[E, B], A]) = zio.foldM(
      { case Left(e)  => ZIO.fail(e)
        case Right(b) => ZIO.succeed(Right(b))
      },
      a               => ZIO.succeed(Left(a))
    ) 
  }
  object task8 {
    def right[R, E, A, B](zio: ZIO[R, E, Either[A, B]]) =
      zio.foldM(
        e               => ZIO.fail(Left(e)),
        {
          case Left(a)  => ZIO.fail(Right(a))
          case Right(b) => ZIO.succeed(b)
        }
      )
    def unright[R, E, A, B](zio: ZIO[R, Either[E, A], B]): ZIO[R, E, Either[A, B]] =
      zio.foldM(
        {
          case Left(e)  => ZIO.fail(e)
          case Right(a) => ZIO.succeed(Left(a))
        },
        b               => ZIO.succeed(Right(b))
      )
  }
  object task9 {
    def catchAllCause[R, E1, E2, A](zio: ZIO[R, E1, A], handler: Cause[E1] => ZIO[R, E2, A]) =
      zio.sandbox
        .foldM(
          ce => handler(ce), // handle the error
          a => ZIO.succeed(a)
        )
  }
  object task10 {
    def catchAllCause[R, E1, E2, A](zio: ZIO[R, E1, A], handler: Cause[E1] => ZIO[R, E2, A] ) =
      zio.foldCauseM(
        ce => handler(ce),   // handle the error
        a => ZIO.succeed(a)
      )
  }
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = task4.app.exitCode
}
