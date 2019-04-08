package x010

import scala.collection.LinearSeq

object C10_01 extends App {
  val v = Vector(1,2,3,4,5)
  println(v(3)) // 4
  v.sum
  v.map(_ * 2)
  v.filter( _ % 2 == 0)
  v.hasDefiniteSize
  v.isTraversableAgain
  val fl = v.foldLeft("_")((x:String, y:Int)=> x.concat("-").concat(y.toString))
  val fr = v.foldRight("_")((y:Int, x:String)=> x.concat("-").concat(y.toString))
  println(fl) // _-1-2-3-4-5
  println(fr) // _-5-4-3-2-1

  val x = IndexedSeq(1,2,3) // Vector
  val z = LinearSeq(5,6,7) // List

  val im = Map(1->"a", 2->"b") // immutable map
  val mm = scala.collection.mutable.Map(1->"a", 2->"b") // mutable map

  val s1 = Set(1,2,3)
  val s2 = List(1,1,2,2,3,3).distinct
  val s3 = List(1,1,2,2,3,3).toSet

  println(s1)
  println(s2)
  println(s3)

  val seq1 = Seq(1, true, "Alex") // Seq[Any] = List(1, true, Alex)
  println(seq1)


}
