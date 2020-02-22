package topics.modad_laws

import scalaz.Monad

import scalaz.std._

object LawsApp1 extends App {
  // (Monad[F].point(x) flatMap {f}) assert_=== f(x)
//  (Monad[Option].point(3) >>= { x => (x + 100000).some }) assert_=== 3 |> { x => (x + 100000).some }
}
