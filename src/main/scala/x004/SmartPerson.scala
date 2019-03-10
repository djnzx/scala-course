package x004

class SmartPerson (private var name: String) {
  def name__(name: String) { this.name = name }
  def name_ = this.name
}
