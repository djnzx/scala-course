package x009

object P7FuncRetFunc extends App {
  val concat = (a: String, b: String) => a + " " + b
  val r1 = concat("a", "b")
  val concat_f1: (String, String) => String = (a, b) => a + " " + b
//  val concat_f2 = (a: String, b: String)(c: String) => a + " " + b + " " + c

  // just function
  def concat_1(a: String, b: String) = { a + " " + b }
  // function returns function
  def concat_21(a: String, b: String) = (s: String) => { a + " " + b + " " + s}
  // curry
  def concat_22(a: String, b: String)(s: String) = { a + " " + b + " " + s}

  val x2 = concat_21("Q", "W")
  val x21 = concat_21("Q", "W")("X")
  val x22 = concat_22("Q", "W")("X")
  val x3 = x2("E")
  println(x2)
  println(x21)
  println(x22)
  println(x3)

  def greeting(lang: String) = (name: String) => {
    val eng = () => s"Hello, $name"
    val spa = () => s"Hola, $name"
    lang match {
      case "eng" => eng()
      case "spa" => spa()
      // be careful, without parentheses we will get only links on functions
//      case "eng" => eng
//      case "spa" => spa
    }
  }

  val e = greeting("eng")
  val s = greeting("spa")

  val e1 = e("Alex")
  val s1 = s("Olena")

  // func
  println(e)
  println(s)

  // values
  println(e1)
  println(s1)


}
