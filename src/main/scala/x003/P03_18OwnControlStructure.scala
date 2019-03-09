package x003

import scala.annotation.tailrec

object P03_18OwnControlStructure extends App {

  def whilst(condition: => Boolean)(codeblock: => Unit): Unit = {
    while (condition) {
      codeblock
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

  whilst2 (i < 5) {
    println(i)
    i += 1
  }
}
