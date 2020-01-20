package _degoes._chunks

object RepresentNotRun extends App {

  println("1:")

  def hprintln(s: String) = () => println(s)
  def hreadln = () => scala.io.StdIn.readLine()
  println("2:")

  lazy val h1 = hprintln("Enter name")
  lazy val h2 = hprintln(s"Hello, ${hreadln()}")
  println("3:")

  h1()
  h2()

  val chain: Seq[() => Any] = List(h1, h2)
  chain.foreach(h => h())
}
