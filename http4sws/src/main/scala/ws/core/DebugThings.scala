package ws.core

import cats.effect.kernel.Sync
import fs2.Stream

trait DebugThings[F[_]: Sync] {

  def logF[A](a: A)(line: sourcecode.Line, fileName: sourcecode.FileName): F[Unit] =
    Sync[F].delay(pprint.log(a)(line, fileName))

  def logS[A](a: A)(line: sourcecode.Line, fileName: sourcecode.FileName): Stream[F, Nothing] =
    Stream.eval(logF(a)(line, fileName)).drain

}
