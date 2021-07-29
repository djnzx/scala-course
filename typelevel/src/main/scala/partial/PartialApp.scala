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
    case 3 => "three"
    case 4 => "four"
    case 5 => "five"
  }

  /** postprocess */
  def postprocess(s: String) = s.toUpperCase

  /** elseCase */
  def another: PartialFunction[Int, String] = {
    case _ => "else"
  }

  /** whole composition */
  def composition = validatePartial andThen represent andThen (postprocess _) orElse another

  val data = 1 to 15

  val outcome = data.collect(composition)
  println(outcome)

}
