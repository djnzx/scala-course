package __udemy.scala_beginners.lectures.part3fp._seq

import scala.util.Random

/**
  * Created by Daniel.
  */
object Sequences extends App {

  // Seq
  val aSequence = Seq(1,3,2,4)
/*
  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(0))
  println(aSequence ++ Seq(7,5,6))
  println(aSequence.sorted)
*/

  // Ranges
  val aRange: Seq[Int] = 1 until 10
  val aRange2: Seq[Int] = 1 to 10
  //aRange.foreach(println)

  //1.to(10).foreach(x => println("Hello "+x))
  //(1 to 10).foreach(x => println("Hello "+x))

  // lists
  val aList = List(1,2,3)
  //val prepended = 42 +: aList :+ 89
  val x = 11 +: aList :+ 66
  val y = (11 +: aList) :+ 66
  val z = 11 +: (aList :+ 66)
//  println(x)
//  println(y)
//  println(z)
  val prepended = 42 +: aList :+ 89
//  println(prepended)

  val apples5 = List.fill(5)("apple")
//  println(apples5)
//  println(aList.mkString("[","-","]"))

  // arrays
  val numbers = Array(1,2,3,4)
  val threeElements = Array.ofDim[String](3)
  threeElements(0)="a"
  threeElements(1)="b"
  threeElements(2)="c"
  //threeElements(3)="!" //won't work because of dimension
  //threeElements.foreach(println)

  // mutation
  numbers.update(2, 0)
  numbers(2) = 0  // syntax sugar for numbers.update(2, 0)
//  println(numbers.mkString(" "))

  // arrays and seq
  val numbersSeq: Seq[Int] = numbers  // implicit conversion to WrappedArray(1, 2, 0, 4)
  //println(numbersSeq)

  // vectors
  val vector: Vector[Int] = Vector(1,2,3)
  //println(vector)

  // vectors vs lists

  val maxRuns = 1000
  val maxCapacity = 1000000 // 1 million

  def getWriteTime(collection: Seq[Int]): Long = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val start = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - start
    }
    times.sum
    //println(x)
    //x * 1.0 / maxRuns
  }

  /**
    * Seq - Java.List
    * IndexedSeq - Vector, Range, String - fast index based, internally like array - Java.ArrayList
    * LinearSeq - fast prepend, append, add, remove, internally linked list - Java.LinkedList
    */

  val numbersList = (1 to maxCapacity).toList // List[Int] = List(...)
  val numbersVector = (1 to maxCapacity).toVector // Vector[Int] = Vector(...)

  // keeps reference to tail
  // updating an element in the middle takes long
  println(getWriteTime(numbersList)) // 10s
  // depth of the tree is small
  // needs to replace an entire 32-element chunk
  println(getWriteTime(numbersVector)) // 0.01s /1000 times faster

}

