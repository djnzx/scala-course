package partial

object PartialApp extends App {

  /** our validation (for example token checking) */
  def validate(x: Int) = x < 10

  /** our validation lifted to the partial */
  def validatePartial: PartialFunction[Int, Int] = {
    case x if validate(x) => x
  }

  /** here is the our "http" mapping */
  def represent: PartialFunction[Int, String] = {
    case 1 => "one"
    case 2 => "two"
  }

  /** postprocess */
  def postprocess(s: String) = s.toUpperCase

  /** elseCase */
  def another: PartialFunction[Int, String] = {
    case _ => "something else"
  }

  /** whole composition */
  def composition = validatePartial andThen represent andThen (postprocess _) orElse another

  val data = List(1,2,3,4,5)

  val outcome = data.collect(composition)
  println(outcome)

}
