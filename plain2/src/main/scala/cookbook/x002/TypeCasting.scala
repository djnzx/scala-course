package cookbook.x002

object TypeCasting extends App {
  val a = 42
  val b = a.toLong

  val c = 18.5.toInt
  println(c)
  a.isValidLong

  val d = 45.6d
  d.isValidInt

  val e = 1f
  val e2 = 1L
  val e3 = 1: Long
  val e4: Long = 1
  var e5 = 0x20

  var z1 = 1
  z1+=1
  z1-=1
  z1*=2
  z1/=3


}
