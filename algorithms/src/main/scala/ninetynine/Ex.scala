package ninetynine

trait Ex {

  def neverByDesign: Nothing = sys.error("should never happen by design")
  def unexpected: Nothing = sys.error("unexpected data provided")

}

object Ex extends Ex
