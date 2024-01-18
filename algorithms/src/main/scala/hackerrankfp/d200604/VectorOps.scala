package hackerrankfp.d200604

import scala.collection.immutable.VectorBuilder
import scala.collection.mutable.ArrayBuffer

object VectorOps extends App{
  private def insertAt[A](v: Vector[A], pos: Int, x: A): Vector[A] = {
    val left = v.slice(0, pos)
    val right = v.slice(pos, v.size)
    // (left :+ x) ++ right
    val vb = new VectorBuilder[A]
    vb ++= left
    vb += x
    vb ++= right
    vb.result()
  }
  private def removeAt[A](v: Vector[A], pos: Int): Vector[A] = {
    val left = v.slice(0, pos)
    val right = v.slice(pos+1, v.size)
    left ++ right
  }

  val data = "0123456"
  
  println(insertAt(data.toVector, 5, "A").mkString)
  val x: ArrayBuffer[Char] = ArrayBuffer(data.toSeq: _*)
  x.insert(5, 'A')
  println(x.mkString)
  //  println(ArrayBuffer(data.toSeq: _*).insert(5, 'A'))


  val y: ArrayBuffer[Char] = ArrayBuffer(data.toSeq: _*)
  y.remove(5)
  println(y.mkString)

  println(removeAt(data.toVector, 5).mkString)
  
}
