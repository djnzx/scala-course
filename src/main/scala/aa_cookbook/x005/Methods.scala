package aa_cookbook.x005

object Methods extends App {
  class One {
    def method1(x: Int): String = x.toString
    def method2(x: Int) = x.toString
    def method21(x: Int = 42) = x.toString
    def method3(x: Int): Unit = println(x)
    def method4(x: Int) = println(x)
    def method5() = {}
    def method6 = {}
    // package private
    private [x005]def method7(a: Int = 1, b: Int = 2) = {}
    private def method8(o: One) = {
      method9 // ok
      // o.method9 // invisible because of private [this]
    }
    private [this] def method9 = {}
    protected def method10 = {}
  }
  class Two extends One {
    def method33 = {
      method10 // because of protected
    }
    super.method10
  }

  val o = new One
  o.method1(1)
  o.method2(3)
  o.method21()
  o.method21(22)
  o.method3(74)
  o.method3(75)
  o.method5
  o.method5()
  o.method6

  o.method7(11, 22)
  o.method7(11)
  o.method7()
  o.method7(a = 3)
  o.method7(b = 3)
  o.method7(a = 3, b = 6)
  o.method7(b = 3, a = 6) // (6, 3)
//  o.method8 // because of private

}
