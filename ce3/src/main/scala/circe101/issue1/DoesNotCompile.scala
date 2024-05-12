package circe101.issue1

import circe101.Base
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.scalatest.Succeeded

object DoesNotCompile {

  sealed trait Cond[A]

  object Cond {
    case class Cond1(x: Int) extends Cond[Int]
//    object Cond1 {
//      implicit val enc: Encoder[Cond1] = deriveEncoder
//    }

//    implicit def enc[A: Encoder]: Encoder[Cond[A]] = deriveEncoder
  }

}

class DoesNotCompile extends Base {
  test("1") {
    Succeeded
  }
}
