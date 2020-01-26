package aa_cookbook.x010

object C10_20 extends App {
  val a = List.range(1,3)
  val r1 = a.reduce((a,b) => a + b+1) // = reduceLeft
  val r2 = a.reduceLeft((a,b) => a+b+2)
  val r3 = a.reduceLeftOption((a,b) => a+b+3)
  val r4 = a.reduceRight((a,b) => a+b+4)
  val r5 = a.reduceRightOption((a,b) => a+b+5)
  val f1 = a.fold(0)((a,b) => a + b + 11)
  val f2 = a.foldLeft(0)((a,b) => a + b + 12)
  val f3 = a.foldRight(0)((a,b) => a + b + 13)
//  a.scan()
//  a.scanLeft()
//  a.scanRight()
  println(r1)
  println(r2)
  println(r3)
  println(r4)
  println(r5)
  println(f1)
  println(f2)
  println(f3)
}
