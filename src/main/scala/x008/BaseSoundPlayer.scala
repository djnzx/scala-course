package x008

trait BaseSoundPlayer {
  def play
  def stop
  def pause
  def resume
  def rewind { println("rewinding") }
  var size = 14
  val SIZE = 30
  var amount: Int

}
