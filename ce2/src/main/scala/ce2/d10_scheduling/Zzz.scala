package ce2.d10_scheduling

import cats.effect.IO

trait Zzz {
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}

object Zzz {

  /** awaking instance */
  def apply: IO[Zzz] = IO {
    new Zzz {
      // TODO: implement
      override def sleep: IO[Unit] = ???
      override def wakeUp: IO[Unit] = IO.unit
    }
  }

  /** sleeping instance */
  def asleep: IO[Zzz] = IO {
    new Zzz {
      override def sleep: IO[Unit] = IO.unit
      // TODO: implement
      override def wakeUp: IO[Unit] = ???
    }
  }
}
