package nomicon.playground

import java.util.concurrent.CompletionStage

import zio._
import pprint.{pprintln => println}
import zio.blocking.Blocking

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object ZIOPlayground extends App {
  case class Symptom(name: String)
  case class SymptomX(name: String) extends Exception(name)
  type R
  trait E
  trait E2 extends E
  type A
  type A2
  type B
  case class SymptomE(name: String) extends E

  object effectCreationManual {
    /**
      * [[ZIO.succeed]] == [[ZIO.effectTotal]] produces ZIO[*, Nothing, A]
      * can't fail at all, when we foldCauseM, we are at success branch */
    val zs1: IO[Nothing, Int] = ZIO.succeed(10)

    /**
      * since we called ZIO.succeed -> we created ZIO[*, Nothing, A]
      * but we lied, because we have thrown Exception, SO => cause.defects */
    val zd1: IO[Nothing, Int] = ZIO.succeed({
      throw SymptomX("ha-ha"); 11
    })
    /**
      * [[ZIO.effectTotal]] produces ZIO[*, [[Nothing]], *] 
      * shouldn't have been failed, but we lied in signature, SO => cause.defects */
    val zd2: IO[Nothing, Int] = ZIO.effectTotal({
      throw SymptomX("ah-ah"); 12
    })

    /**
      * [[ZIO.fail]] produces ZIO[*, E, Nothing]
      * we failed intentionally, didn't lie, SO => cause.failures */
    val zf1: IO[Symptom, Nothing] = ZIO.fail(Symptom("AA"))
    /**
      * [[ZIO.effect]] produces ZIO[*, [[Throwable]], *]
      * SymptomX is a subtype of Throwable, SO => cause.failures */
    val zf2: IO[Throwable, Nothing] = ZIO.effect(throw SymptomX("XX"))

    val z = zd2

    val app = z.foldCauseM(
      { cause =>
        Predef.print("defects: ");
        println(cause.defects) // List[Throwable]
        Predef.print("failures:");
        println(cause.failures) // List[E]
        console.putStrLn("foldM: at the failure branch")
      },
      { a: Int =>
        Predef.print("value: ");
        println(a)
        console.putStrLn("foldM: at the success branch")
      }
    )
  }
  object effectCreationManualAsync {
    trait User
    /** this is sync function */
    def getUser(id: Int): Option[User] = ???
    /** this is ASYNC function,
      * we need to provide a callback,
      * because we don't know when it will finish,
      * callback result type is Unit, because we can't use it
      * function result type is Unit, because we need to return immediately,
      * but it can be something more useful
      * 
      * version 1 without error handling */
    def getUserAsync(id: Int, callback: Option[User] => Unit): Unit = ???
    /** wrapping to ZIO */
    def wrapped1(id: Int): ZIO[Any, Option[Throwable], User] =
      ZIO.effectAsync((cb: ZIO[Any, Throwable, Option[User]] => Unit) =>
        getUserAsync(
          id,
          (ou: Option[User]) => cb(ZIO.succeed(ou)),
        )
      ).some

    /** version 2 with error handling */
    def getUserAsync2(id: Int, onSuccess: User => Unit, onFailure: Throwable => Unit): Unit = ???
    /** wrapping to ZIO */
    def wrapped2(id: Int): ZIO[Any, Throwable, User] =
      ZIO.effectAsync((cb: ZIO[Any, Throwable, User] => Unit) =>
        getUserAsync2(
          id,
          (u: User)      => cb(ZIO.succeed(u)),
          (t: Throwable) => cb(ZIO.fail(t))
        )
      )

    /** version 3 with error handling and */
    def getUserAsync3(id: Int, callback: Either[Throwable, Option[User]] => Unit): Unit = ???
    /** TODO: wrapping to ZIO */
    def wrapped3(id: Int) =
      ZIO.effectAsync(???)
      
  }
  object effectCreationManualExtra {
    val t: Throwable = ???
    val z1: IO[Throwable, Nothing] = ZIO.die(t)
    
  }
  object effectCreationFrom {
    val a: A = ???
    val e: E = ???
    val ea: Either[E, A] = ???
    val ra: R => A = ???
    val rfa: R => Future[A] = ???
    val rzea: R => IO[E, A] = ???
    val ecfa: ExecutionContext => Future[A] = ???
    val pa: scala.concurrent.Promise[A] = ???
    val csa: CompletionStage[A] = ???
    val jfa: java.util.concurrent.Future[A] = ???
    
    // option-related
    val z1a: IO[Option[Nothing], A] = ZIO.fromOption(Some(a))
    val z1b: IO[Nothing, Option[A]] = ZIO.some(a)

    // either-related
    val z2a: IO[E,       A                 ] = ZIO.fromEither(ea)
    val z2b: IO[Nothing, Either[A, Nothing]] = ZIO.left(a)
    val z2c: IO[Nothing, Either[Nothing, A]] = ZIO.right(a) 

    // standard libraries throwable-based 
    val z3: IO[Throwable, A] = ZIO.fromFuture(implicit ec => Future(a))
    val z4: IO[Throwable, A] = ZIO.fromTry(Try(a))
    val z5: IO[Throwable, A] = ZIO.fromFutureInterrupt(ecfa)
    val z6: IO[Throwable, A] = ZIO.fromPromiseScala(pa)
    val z7: IO[Throwable, A] = ZIO.fromCompletionStage(csa)

    // different stuff requires some resources
    val z8: ZIO[R, Throwable, A] = ZIO.fromFunctionFuture(rfa)
    val z9: ZIO[R, E,         A] = ZIO.fromFunctionM(rzea)
    val zA: ZIO[R, Nothing,   A] = ZIO.fromFunction(ra)
    
    // Java's Future is blocking
    val zB: ZIO[Blocking, Throwable, A] = ZIO.fromFutureJava(jfa)
  }
  /** when result is A */
  object channelsManipulationBasic {
    lazy val z3: ZIO[R, E, A] = ???
    
    // replace result with given
    lazy val b: B = ???
    val     z3c: ZIO[R, E, B] = z3.as(b)

    // move E to Left(E), move A to Right(A) on the result
    val     z3a: ZIO[R, Nothing, Either[E, A]] = z3.either
    
    // wrap result to Option
    val     z3b: ZIO[R, E, Option[A]] = z3.asSome
    
    // wrap error to Option
    val     z3d: ZIO[R, Option[E], A] = z3.asSomeError
  }
  /** when result is Option[A] */
  object channelsManipulationResultOption {
    lazy val z2: ZIO[R, E, Option[A]] = ???

    // opens Option and wraps Error to Option
    val     z2a: ZIO[R, Option[E], A] = z2.some
  }
  /** when result is Either[A1, A2] or Either[E, A] */
  object channelsManipulationResultEither {
    lazy val z4: ZIO[R, E, Either[A, A2]] = ???
    
    // extract left and wrap Error to Optional
    val     z4a: ZIO[R, Option[E], A] = z4.left
    
    // extract left or fail with given value E
    val     z4b: ZIO[R, E, A] = z4.leftOrFail(SymptomE("bad"))
    
    // extract left or fail with A2 => E
    val     z4c: ZIO[R, E, A] = z4.leftOrFailWith((_: A2) => SymptomE("bad"))
    
    // extract right and wrap Error to Optional + fail + failWith
    val     z4w: ZIO[R, Option[E], A2] = z4.right

    lazy val z5: ZIO[R, E2, Either[E, A]] = ???
    // shift E from Either.Left to E
    val     z5a: ZIO[R, E, A] = z5.absolve
  }
  /** error manipulation */
  object channelsManipulationError {
    lazy val z1: ZIO[R, Throwable,A] = ???

    // tries to convert defects into a failure
    val     z1a: ZIO[R, Throwable, A] = z1.absorb
  } 
  
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = effectCreationManual.app.exitCode
}
