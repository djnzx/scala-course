package _implicits.x5params

object HOF_App extends App {
  val multiply3: (Int) => Int = (x: Int) => x * 10
  val multiply31: () => (Int) => Int = () => (x: Int) => x * 10
  val multiply4: (Int, Int) => Int = (x: Int, y: Int) => x * y

  println(multiply3(10))
  println(multiply31()(10))
}
