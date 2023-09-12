package fps

import scala.annotation.unused

object Fps004Lazy2 extends App {

  var counter: Long = 0;

  val block_of_code: () => Long = () => {
    println("evaluating")
    counter+=1
    counter
  }

  def ex1a(code: => Long): Unit = {
    println("ex1a:entrance")
    println(code)                  // code will be evaluated (called) HERE
    println("ex1a:quit")
  }

  def ex1b(@unused code: => Long): Unit = { // code won't be evaluated (called)
    println("ex1b:entrance")
    println("ex1b:quit")
  }

  def ex2a(code: Long): Unit = {
    println("ex2a:entrance")
    println(code)
    println("ex2a:quit")
  }

  def ex2b(@unused code: Long): Unit = {
    println("ex2b:entrance")
    println("ex2b:quit")
  }

  ex1a(block_of_code()) // block_of_code MAY BE evaluated (called) INSIDE function, because of: `code: => Long`
  ex1b(block_of_code()) // block_of_code MAY BE evaluated (called) INSIDE function, because of: `code: => Long`
  ex2a(block_of_code()) // block_of_code WILL BE evaluated (called) HERE, because of: `code: Long`
  ex2b(block_of_code()) // block_of_code WILL BE evaluated (called) HERE, because of: `code: Long`

  println(counter) // 3 will be printed

}
