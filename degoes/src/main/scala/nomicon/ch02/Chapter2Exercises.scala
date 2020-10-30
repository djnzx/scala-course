package nomicon.ch02

import java.util.concurrent.TimeUnit

import zio._
import zio.clock.Clock

object Chapter2Exercises {

  def readFile(file: String): String = {
    val source = scala.io.Source.fromFile(file)
    try source.getLines().mkString finally source.close()
  }
  
  /** generic error Throwable */
  def readFileZio(file: String): Task[String] = ZIO.effect(readFile(file))

  def writeFile(file: String, text: String): Unit = {
    import java.io._
    val pw = new PrintWriter(new File(file))
    try pw.write(text) finally pw.close()
  }
  
  def writeFileZio(file: String, text: String): Task[Unit] = ZIO.effect(writeFile(file, text))

  def copyFile(source: String, dest: String): Unit = { 
    val contents = readFile(source)
    writeFile(dest, contents)
  }
  
  def copyFileZio1(src: String, dst: String) =
    readFileZio(src).flatMap(contents => writeFileZio(dst, contents))

  def copyFileZio2(src: String, dst: String) =
    for {
      contents <- readFileZio(src)
      _        <- writeFileZio(dst, contents)
    } yield()

  def myZip[R1, R2, E, A, B, C](za: ZIO[R1, E, A], zb: ZIO[R2, E, B])(f: (A, B) => C): ZIO[R2 with R1, E, C] =
    for {
      a <- za
      b <- zb
    } yield f(a, b)
  
  myZip(ZIO.succeed(3), ZIO.succeed(2))(_*_)
  
  // 7
  def myCollectAll[R, E, A](in: Iterable[ZIO[R, E, A]]): ZIO[R, E, List[A]] = 
    in.foldLeft(
      // just take the empty list wrapped with the ZIO[R, E, A]
      // TODO: how to create empty ZIO[R, E, *] ??
      in.head.map(_ => List.empty[A])
    ) { (acc, a) => 
      acc.flatMap(la => a.map(_::la))
    }
      .map(_.reverse)

  // 8
  def foreach[R, E, A, B](in: Iterable[A])(f: A => ZIO[R, E, B]): ZIO[R, E, List[B]] =
    myCollectAll(in.map(f))
  // 11
  def myEitherToZio[E, A](e: Either[E, A]): ZIO[Any, E, A] = e match {
    case Left(value) => ZIO.fail(value)
    case Right(value) => ZIO.succeed(value)
  }
  // 12
  def listToZIO1[A](list: List[A]): ZIO[Any, None.type, A] = list.headOption match {
    case Some(value) => ZIO.succeed(value) 
    case _           => ZIO.fail(None)
  }
  // 12
  def listToZIO2[A](list: List[A]): ZIO[Any, Option[Nothing], A] =
    ZIO.fromOption(list.headOption)
  // 13
  val currentLegacy:    ZIO[Any,   Nothing, Long] = ZIO.effectTotal(System.currentTimeMillis())
  val currentIdiomatic: ZIO[Clock, Nothing, Long] = clock.currentTime(TimeUnit.MILLISECONDS)
  // 14
  def getCacheValue(key: String, 
                    onSuccess: String => Unit,   // callback to run in happy case 
                    onFailure: Throwable => Unit // callback to run in case of failure
                   ): Unit = ???
  
  def getCacheValueZio(key: String): ZIO[Any, Throwable, String] =
    ZIO.effectAsync(cb => getCacheValue(key,
      s  => cb(ZIO.succeed(s)),
      th => cb(ZIO.fail(th))
    ))
    
  // 15
  trait User
  def saveUserRecord(user: User,
                     onSuccess: () => Unit,
                     onFailure: Throwable => Unit
                    ): Unit = ???

  def saveUserRecordZio(user: User): ZIO[Any, Throwable, Unit] =
    ZIO.effectAsync(cb => saveUserRecord(user,
      () => cb(ZIO.succeed(())),
      th => cb(ZIO.fail(th))
    ))
  
  // 16
  import scala.concurrent.{ExecutionContext, Future} 
  trait Query
  trait Result
  def doQuery(query: Query)(implicit ec: ExecutionContext): Future[Result] = ???
  def doQueryZio(query: Query): ZIO[Any, Throwable, Result] =
    ZIO.fromFuture(implicit ec => doQuery(query))
    
}
