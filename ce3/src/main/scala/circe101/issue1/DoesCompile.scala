package circe101.issue1

import circe101.Base
import io.circe.Encoder
import org.scalatest.Succeeded

object DoesCompile {

  sealed trait Cond[A]

  object Cond {

    case class Cond2[A](x: A) extends Cond[A]

    implicit def enc[A: Encoder]: Encoder[Cond[A]] =
      io.circe.generic.semiauto.deriveEncoder
  }

}

class DoesCompile extends Base {
  test("1") {
    Succeeded
  }
}
