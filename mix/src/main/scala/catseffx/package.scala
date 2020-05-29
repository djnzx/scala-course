import cats.effect.IO

package object catseffx {
  // current time
  val t:     () => Long = ()         => System.currentTimeMillis()
  // delta to given time
  val dt:  Long => Long = (t0: Long) => t() - t0
  // print time
  val pt:    () => Unit = ()         => println(t())
  // print delta time
  val pdt: Long => Unit = (t0: Long) => println(dt(t0))

  // print time IO
  val ptIO:          IO[Unit] =               IO { pt() }
  // print delta time IO
  val pdtIO: Long => IO[Unit] = (t0: Long) => IO { pdt(t0) }

}
