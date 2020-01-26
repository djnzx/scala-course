package degoes._chunks

object Combination extends App {

  trait T1 {
    def m1: Int
  }

  trait T2 {
    def m2: Int
  }

  trait T3 {
    def m3: Int
  }

  type T12  = T1 with T2
  type T23  = T2 with T3
  type T123 = T1 with T2 with T3

  def m1(x: T123): Unit = {
    println(x.m1)
    println(x.m2)
    println(x.m3)
  }



}
