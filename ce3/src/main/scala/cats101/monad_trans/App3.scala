package cats101.monad_trans

import cats.implicits.catsSyntaxEitherId

/**
  * eff ???
  * http://eed3si9n.com/herding-cats/
  */
object App3 {
  trait Result
  
  val r: Either[Exception, Result] = new Exception("Broken!").asLeft[Result]
  
}
