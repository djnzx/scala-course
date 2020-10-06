package luxsoft

object LuxApp extends App {
  
  /** 1. define behavior */
  trait Show[A] {
    def show(a: A): String
  }
  
  /** 2. create instances for types I want to attach my behavior */
  implicit val ss = new Show[String] {
    override def show(a: String): String = s"I'm a String: $a"
  }
  implicit val si = new Show[Int] {
    override def show(a: Int): String = s"I'm an Int: $a"
  }
  
  /** 2.1. use. not compelling at all) */ 
  ss.show("AAA")
  si.show(123)
  /** 3.0 define syntax */
  implicit class ShowSyntax[A: Show](a: A) {
    def show() = implicitly[Show[A]].show(a)
  }
  /** 4. use! */
  "AAA".show()
  123.show()
  
}
