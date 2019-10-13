package __udemy.scala_beginners.lectures.part2oop._wip

object ClassAccessTypes extends App {

  // private, val
  class T1(name: String) {
    // public, val
    val f1: Int = 77
    // public, var
    var f2: Int = 88
    // private, final
    private val f3 = 33
    private var f4: Int = 0
    // getter to field from constructor
    def get(): String = name
    // getter corresponding to f3
    def get_f3(): Int = f3
    def get_f4(): Int = f4
    // setter
    def set_f4(f4: Int): Unit = this.f4 = f4
  }

  val t1 = new T1("Alex")
  println(t1.get())
  t1.f2 = 77
  t1.get_f3()
  t1.set_f4(7)
  println(t1.get_f4())

  // val, private, unvisible
  class T31(name: String)
  // val, private, visible
  class T32(private val name: String)
  // val, public
  class T2(val name: String)
  // var, private
  class T4(private var name: String)
  // var, public
  class T5(var name: String)

  val t2 = new T2("Dima")
  t2.name
  val t31 = new T31("Serg")
  val t32 = new T32("Serg")
  val t4 = new T4("Masha")
  val t5 = new T5("Lena")
  t5.name = "Lena#1"



}
