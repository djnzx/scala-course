package topics.partial

object PartialExperimentsApp extends App {

  // data representation
  sealed trait Badness extends RuntimeException
  final case class Badness1(m: String) extends Badness
  final case class Badness2(m: String) extends Badness

  // type aliases
  type ResultOrError = Either[Badness, Int]
  type ResultEnsured = String
  type PartHandler = PartialFunction[ResultOrError, ResultEnsured]

  // business logic emulation
  def process(variance: Int): ResultOrError = variance match {
    case 0   => Left(Badness1("A"))
    case 1   => Left(Badness2("B"))
    case 2   => Left(new Badness {})
    case n@_ => Right(n)
  }

  // Ok handler
  val okHandler: PartHandler = {
    case Right(i) => s"Result!: $i"
  }

  // Badness1 handler
  val handle1: PartHandler = {
    case Left(Badness1(m)) => s"Badness1 handled here ($m)"
  }

  // Badness2 handler
  val handle2: PartHandler = {
    case Left(Badness2(m)) => s"Badness2 handled here ($m)"
  }

  // All handler
  val handleAll: PartHandler = _ => s"Something else CAUGHT"

  // Full handler - just combination of all handlers
  val fullHandler: PartHandler =
    okHandler orElse handle1 orElse handle2 orElse handleAll

  // demo !
  println(fullHandler(process(0)))
  println(fullHandler(process(1)))
  println(fullHandler(process(2)))
  println(fullHandler(process(3)))
}
