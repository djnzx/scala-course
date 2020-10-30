package nomicon.ch02

import zio._

import scala.concurrent.{ExecutionContext, Future}

object ZApp4 {
  
  // side-effect with ANY error, and NO resource
  val z1: Task[Unit] = ZIO.effect(println("Hello"))
  // side-effect can't fail, NO resources, shows the honest intention
  val z1a: UIO[Int] = ZIO.effectTotal(15)
  // effect with NO error, and NO resource
  val z2: UIO[Int] = ZIO.succeed(123)
  // effect with SPECIFIC error, and NO resource
  val z3: IO[NumberFormatException, Nothing] = ZIO.fail(new NumberFormatException)
  
  val z4: IO[String, Int] = ZIO.fromEither(Right(42).withLeft[String])
  val z5: IO[Option[Nothing], Int] = ZIO.fromOption(Some(3))
  // result Int, can fail with any Throwable, doesn't require resources
  val z6: Task[Int] = ZIO.fromFuture(Future(15)(_))

  def getUserByIdAsync(id: Int)(cb: Option[String] => Unit): Unit = ???
  def getUserByIdAsyncV2(id: Int)(fail: None.type => Unit, success: String => Unit): Unit = ???
  
  getUserByIdAsync(5) {
    case Some(name) => println(s"found: $name")
    case None       => println("User not found!")
  }
  
  getUserByIdAsyncV2(5)(
    _ => println("User not found!"),
    s => println(s"found: $s")
  )

  def getUserByIdZIO(id: Int): ZIO[Any, None.type, String] = ZIO.effectAsync { cb =>
    getUserByIdAsync(id) {
      case Some(name) => cb(ZIO.succeed(name))
      case None       => cb(ZIO.fail(None))
    }
  }

  def getUserByIdZIO2(id: Int): ZIO[Any, None.type, String] = ZIO.effectAsync { cb =>
    getUserByIdAsyncV2(id)(
      _ => cb(ZIO.fail(None)),
      s => cb(ZIO.succeed(s))
    )
  }

  def goShoppingFuture(implicit ec: ExecutionContext): Future[Unit] =
    Future(println("Going to the grocery store"))

  val goShopping: Task[Unit] = Task.fromFuture(implicit ec => goShoppingFuture)

}
