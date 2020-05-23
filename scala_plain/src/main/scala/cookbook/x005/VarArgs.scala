package cookbook.x005

object VarArgs extends App {

  class VArgs {
    def printAll(names: String*) = {
      names.foreach(println)
    }
  }

  val va = new VArgs
  va.printAll()
  va.printAll("a")
  va.printAll("qw", "er", "ty")
  val list = List("Jeep", "Dodge", "Chrysler")
  // any collection to varargs
  va.printAll(list: _*)
}
