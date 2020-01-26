package degoes.hkt3

object HKTApp032 extends App {

  trait Logging {
    def log(message: String): Unit
  }

  def m1(message: String)(implicit logger: Logging): Unit = {
    logger.log(message);
  }

//  implicit
  val log2: Logging = new Logging {
    override def log(message: String): Unit = println(message)
  }

  implicit
  val log: Logging = new Logging {
    override def log(message: String): Unit = {}
  }

  m1("Hello")

}
