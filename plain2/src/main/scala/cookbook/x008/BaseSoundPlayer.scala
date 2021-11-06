package cookbook.x008

trait BaseSoundPlayer {
  def play: Unit
  def stop: String
  def pause: Unit
  def resume: String
  def rewind: Unit = { println("rewinding") }
  var size = 14
  val SIZE = 30
  var amount: Int

}
