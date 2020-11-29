package nomicon.ch02layer.services

import cats.effect.{ContextShift, IO}
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux
import nomicon.ch02layer.services.Aliases.Configuration
import pureconfigx.AppConfig
import zio.{UIO, URIO, ZIO}

object DbConnection {
  
  trait Service {
    def xa: UIO[Aux[IO, Unit]]
  }
  
  def xa(implicit cs: ContextShift[IO]): URIO[Configuration, Aux[IO, Unit]] = ZIO.accessM { conf: Configuration =>
    val confS: Configuration.Service = conf.get
    val tx: ZIO[Any, Nothing, Aux[IO, Unit]] = for {
      ac <- confS.conf
      db = ac.db
      t = Transactor.fromDriverManager[IO](db.driver, db.url, db.user, db.password)
    } yield t
    tx
  } 

}
