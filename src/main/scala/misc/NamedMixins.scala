package misc

trait Vehicle

trait Horn {
  def toot(): Unit
}

trait LoudHorn extends Horn {
  override def toot(): Unit = println("TOOT!")
}

trait SoftHorn extends Horn {
  override def toot(): Unit = println("toot")
}

class Car extends Vehicle { this: Horn =>
}

class CarSoft extends Car with SoftHorn
class CarLoud extends Car with LoudHorn

object NamedMixins extends App {
  new CarSoft().toot()
  new CarLoud().toot()
}
