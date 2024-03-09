package stmx

import cats.effect.IO
import cats.effect.IOApp
import io.github.timwspence.cats.stm._

// https://timwspence.github.io/cats-stm/start.html
object Intro extends IOApp.Simple {

  def wibble(stm: STM[IO])(tvar: stm.TVar[Int]): stm.Txn[Int] =
    for {
      current <- tvar.get
      updated = current + 1
      _       <- tvar.set(updated)
    } yield updated

  def runStm(stm: STM[IO]): IO[Int] = {
    import stm._

    for {
      tvar: stm.TVar[Int] <- stm.commit(TVar.of[Int](0))
      x: stm.Txn[Int] = wibble(stm)(tvar)
      res                 <- stm.commit(x)
    } yield res
  }

  val stmRuntime = STM.runtime[IO]

  val app = stmRuntime
    .flatMap(stm => runStm(stm))
    .flatMap(x => IO.println(x))

  override def run: IO[Unit] = app
}
