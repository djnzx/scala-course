package hackerrankfp.d200421_01

import java.io.File

import scala.annotation.tailrec
import scala.io.BufferedSource

object IntroApp extends App {
  /**
    * https://www.hackerrank.com/challenges/fp-filter-positions-in-a-list/problem
    */
  def f01(arr: List[Int]): List[Int] =
    arr.zipWithIndex
    .filter { _._2 % 2 != 0 }
    .map { _._1 }

  /**
    * https://www.hackerrank.com/challenges/fp-array-of-n-elements/problem
    */
  def f02a(num:Int): List[Int] =
    Array.fill[Int](num)(1).toList
  def f02b(num:Int): List[Int] =
    1 to num toList

//  def readInt = scala.io.Source.stdin.getLines().take(1).map(_.toInt).sum
//  println(f02a(readInt))

  /**
    * https://www.hackerrank.com/challenges/fp-reverse-a-list/problem?h_r=next-challenge&h_v=zen
    */
  def f03(arr:List[Int]):List[Int] = {
    def fr(xs: List[Int], acc: List[Int]): List[Int] = xs match {
      case h::t => fr(t, h::acc)
      case _ => acc
    }
    fr(arr, List.empty[Int])
  }

  /**
    * https://www.hackerrank.com/challenges/fp-sum-of-odd-elements/problem?h_r=next-challenge&h_v=zen
    */
  def f04(arr:List[Int]):Int = arr.foldLeft(0) { (acc, el) => acc + (if (el %2 != 0) el else 0)}

  /**
    * https://www.hackerrank.com/challenges/fp-list-length/problem
    */
  def f05(arr:List[Int]):Int = {
    def fr(xs: List[Int], acc: Int): Int = xs match {
      case h::t => fr(t, acc+1)
      case _ => acc
    }
    fr(arr, 0)
  }

  /**
    * https://www.hackerrank.com/challenges/fp-update-list/problem
    */
  def f06(arr:List[Int]):List[Int] = {
    def fr(xs: List[Int], acc: List[Int]): List[Int] = xs match {
      case h::t => fr(t, scala.math.abs(h)::acc)
      case _ => acc
    }
    fr(arr, List.empty[Int]).reverse
  }

  /**
    * https://www.hackerrank.com/challenges/eval-ex/problem
    */
  def fact(n: Int): Long =             (1 to n).foldLeft(1: Long)  ((acc, el) => acc * el)
  def pow(x: Double, n: Int): Double = (1 to n).foldLeft(1: Double)((acc, _) => acc * x)
  def ex(x: Double): Double =      1 + (1 to 9).foldLeft(0: Double)((acc, n) => acc + pow(x,n)/fact(n) )

  def main_ex(args: Array[String]) {
    val stdin = scala.io.StdIn
    val n = stdin.readLine.trim.toInt
    for (_ <- 1 to n) {
      val x = stdin.readLine.trim.toDouble
      val r = ex(x)
      println(r.formatted("%.4f"))
    }
  }

  def consume_data(srcx: BufferedSource): List[List[(Int, Int)]] = {
    val origin: List[String] = srcx.getLines().map(_.trim).toList

    @tailrec
    def consume(sr: List[String], buf: List[(Int, Int)], acc: List[List[(Int, Int)]]): List[List[(Int, Int)]] = {
//      print(s"SR: $sr   ")
//      print(s"BUF: $buf   ")
//      println(s"ACC: $acc")
      sr match {
        case line::lines => line.split(" ") match {
          case Array(x, y) => consume(lines, (x.toInt, y.toInt)::buf, acc)
          case Array(_)    => consume(lines, List.empty, if (buf.isEmpty) acc else buf.reverse::acc)
        }
        case Nil => buf.reverse::acc
      }
    }

    consume(origin.tail, List.empty, List.empty) reverse
  }

  def process(data: List[(Int, Int)]): String = {
//    println(s"${data.length}: $data")
    if(data.groupBy(_._1).forall(kv => kv._2.size==1)) "YES" else "NO"
  }

  def main_is_fn(args: Array[String]): Unit = {
    val src: BufferedSource =
      scala.io.Source.fromFile(new File("src/main/scala/hackerrankfp/d200421_01/test1"))
    consume_data(src) map { process } foreach println
  }

  main_is_fn(Array.empty)
}
