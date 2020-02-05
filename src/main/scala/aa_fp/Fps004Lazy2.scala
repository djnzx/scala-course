package aa_fp

object Fps004Lazy2 extends App {

  var counter = 0;

  val codeblock: () => Long = () => {
    println("evaluating")
    counter+=1
    counter
  }

  def ex1(code: => Long): Unit = {
  }
  def ex2(code: => Long): Unit = {
    println(code)
  }

  ex1(codeblock())
  ex1(codeblock())
  ex2(codeblock())
  ex2(codeblock())
  println(counter)

}
