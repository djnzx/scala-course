package degoes.tips12

case class Sender[A](origin: A) extends AnyVal
case class Receiver[A](origin: A) extends AnyVal

class Tip12OneTypePerScope {
  /**
    * instead of:
    * transfer(true, false)
    * use:
    * transfer(Set(EnableRetry, UseSSL)) - case objects
    *
    *
    * transfer(a: Account, b: Account)
    *
    * transfer(Sender(x), Receiver(y))
    */


}
