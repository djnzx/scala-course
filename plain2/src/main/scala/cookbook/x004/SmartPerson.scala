package cookbook.x004

class SmartPerson (private var name: String) {
  def name_ = this.name                     // getter
  def name__(name: String): Unit = this.name = name // setter
}
