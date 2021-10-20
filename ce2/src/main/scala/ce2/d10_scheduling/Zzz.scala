package ce2.d10_scheduling

import cats.effect.IO

trait Zzz {
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}

object Zzz {
  def apply: IO[Zzz] = ???
  def asleep: IO[Zzz] = ???
}
