package aa_cookbook.x003

import scala.annotation.tailrec

object P03_18OwnControlStructure extends App {

  def whilst(condition: => Boolean)(codeBlock: => Unit): Unit = {
    while (condition) {
      codeBlock
    }
  }

  @tailrec
  def whilst2(condition: => Boolean)(codeblock: => Unit): Unit = {
    if (condition) {
      codeblock
      whilst2(condition)(codeblock)
    }
  }

  var i = 0
  whilst (i < 5) {
    println(i)
    i += 1
  }
}
