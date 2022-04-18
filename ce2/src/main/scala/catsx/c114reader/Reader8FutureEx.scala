package catsx.c114reader

import cats.data.Reader
import cats.implicits.catsSyntaxTuple2Semigroupal
import utils.Timed.printTimed
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.Await
import scala.concurrent.Future

object Reader8FutureEx extends App {

  /** readers */
  val r1: Reader[Any, Future[Int]] = Reader { _: Any => Future { Thread.sleep(2000); 1 } }
  val r2: Reader[Any, Future[Int]] = Reader { _: Any => Future { Thread.sleep(4000); 2 } }

  /** composition 1a */
  val rc1: Reader[Any, Future[Int]] =
    for {
      f1 <- r1
      f2 <- r2
    } yield f1.flatMap { a => f2.map(b => a + b) }

  printTimed(
    Await.result(rc1(()), 10.seconds),
  )

  /** ~4000 */

  /** composition 1b */
  val rc1b: Reader[Any, Future[Int]] = r1 andThen r2

  printTimed(
    Await.result(rc1b(()), 10.seconds),
  )

  /** ~4000 */

  val rc2: Reader[Any, Future[Int]] =
    (r1, r2)
      .mapN { (f1, f2) => f1.flatMap { a => f2.map(b => a + b) } }

  printTimed(
    Await.result(rc2(()), 10.seconds),
  )

  /** ~4000 */

}
