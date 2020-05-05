package fp_red.red01_05

object Red05a extends App {
  List(1,2,3,4).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)

  def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A =
    if (cond) onTrue() else onFalse()

  def if3[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if (cond) onTrue else onFalse

  if2(1 > 2,
    () => println("a"),
    () => println("b")
  )
  if3(1 > 2,
    println("a"),
    println("b")
  )
  def maybeTwice(b: Boolean, i: => Int) = if (b) i+i else 0
  val x = maybeTwice(true, { println("hi"); 1+41 })
  def maybeTwice2(b: Boolean, i: => Int) = {
    lazy val j = i
    if (b) j+j else 0
  }
  val x2 = maybeTwice2(true, { println("hi"); 1+41 })

}
