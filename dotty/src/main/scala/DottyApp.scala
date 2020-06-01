object DottyApp extends App {

  trait Resettable {
    def reset(): Resettable
  }
  
  trait Growable[T] {
    def add(x: T): this.type
  }

  def f(x: Resettable & Growable[String]) = {
    x.reset()
    x.add("first")
  }
  
  class RG extends Resettable with Growable[String] {
    def reset(): RG = ???
    override def add(x: String): RG.this.type = ???
  }
  
  f(new RG)
  
}
