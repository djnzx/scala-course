import cats.effect.Async

import cats.effect.ContextShift
import cats.effect.IO
import doobiex.Db.driver
import doobiex.Db.pass
import doobiex.Db.url
import doobiex.Db.user

import doobie.Transactor

package object doobiex {

  def putStrLn(line: String) = IO { println(line) }

  /** transactor, wrapper about connection */
  def xa[F[_]: ContextShift: Async] = Transactor.fromDriverManager[F](driver, url, user, pass)

}
