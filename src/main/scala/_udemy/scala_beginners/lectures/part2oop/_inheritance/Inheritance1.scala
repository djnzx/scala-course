package _udemy.scala_beginners.lectures.part2oop._inheritance

object Inheritance1 extends App {

  final class P1 {
    def make:Unit = println("Make It");
  }

  sealed class P0 {
    final def make:Unit = println("Make It");
  }

  class P3 extends P0 {// we are able to extend sealed class in the same file
    // override def make:Unit = print("Made")
    // impossible to override final method
  }
  
  val p3 = new P3
  p3.make

  //class P2 extends P1 // impossible to extend final class
}
