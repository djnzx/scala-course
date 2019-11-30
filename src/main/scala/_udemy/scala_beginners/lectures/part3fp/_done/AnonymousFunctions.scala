package _udemy.scala_beginners.lectures.part3fp._done

/**
  * Created by Daniel.
  */
object AnonymousFunctions extends App {

  // anonymous function (LAMBDA)
  val doubler: Int => Int = (x: Int) => x * 2
  val double2: Int => Int = x => x * 2
  val double3: Int => Int = _ * 2
  val conv1: String => Int = (x: String) => x.toInt
  val conv2: String => Int = x => x.toInt
  val conv3: String => Int = _.toInt
  val conv4 = { str: String => str.toInt }

  // multiple params in a lambda
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b

  // no params
  val justDoSomething: () => Int = () => 3

  // careful
  println(justDoSomething) // function itself
  println(justDoSomething()) // call

  // curly braces with lambdas
  val stringToInt = { str: String =>
    str.toInt
  }

  // MOAR syntactic sugar
  val niceIncrementer: Int => Int = _ + 1 // equivalent to x => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a,b) => a + b

  /*
    1.  MyList: replace all FunctionX calls with lambdas
    2.  Rewrite the "special" adder as an anonymous function
   */

  val superAdd = (x: Int) => (y: Int) => x + y
  println(superAdd(3)(4))

  def f1(x: => Int):Int = x + 1
  def f2(x: => Int) = x + 1

  println(f1(f1(1)))
}
