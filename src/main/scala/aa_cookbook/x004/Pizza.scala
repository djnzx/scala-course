package aa_cookbook.x004

class Pizza(val name: String = Pizza.DEFAULT_NAME, val size: Int = Pizza.DEFAULT_SIZE) {
  def this(name: String) {
    this(name, 30)
  }
  def this(size: Int) {
    this("Medium", size)
  }
  def this(from: List[String]) {
    this(from.head)
  }
  def this() {
    this(Pizza.DEFAULT_NAME, Pizza.DEFAULT_SIZE)
  }
  println("constructor")
}

// static fields implementation
object Pizza {
  val DEFAULT_NAME="Big&Smart"
  val DEFAULT_SIZE=30
  def apply(name: String, size: Int) = new Pizza (name, size)
  def apply() = new Pizza ()
}

