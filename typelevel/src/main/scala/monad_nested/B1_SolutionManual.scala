package monad_nested

import cats.effect.IO

object B1_SolutionManual {

  val r1: IO[IO[Option[Option[Int]]]] =
    ioo5.map { o5 =>     // "opening" IO from ioo5
      ioo6.map { o6 =>   // "opening" IO from ioo6
        o5.map { v5 =>   // "opening" Option from ioo5
          o6.map { v6 => // "opening" Option from ioo6
            v5 + v6
          }
        }
      }
    }

  val r2: IO[IO[Option[Int]]] =
    ioo5.map { o5 =>      // "opening" IO from ioo5
      ioo6.map { o6 =>    // "opening" IO from ioo6
        o5.flatMap { v5 => // "mapping + flattening"
          o6.map { v6 =>
            v5 + v6
          }
        }
      }
    }

  val r3: IO[Option[Int]] =
    ioo5.flatMap { o5 =>   // "mapping + flattening" IO from ioo5
      ioo6.map { o6 =>    // "opening" IO from ioo6
        o5.flatMap { v5 => // "mapping + flattening"
          o6.map { v6 =>
            v5 + v6
          }
        }
      }
    }

}
