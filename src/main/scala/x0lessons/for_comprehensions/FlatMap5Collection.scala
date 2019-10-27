package x0lessons.for_comprehensions

import java.net.InetSocketAddress

import scala.util.Try

// https://www.brunton-spall.co.uk/post/2011/12/02/map-map-and-flatmap-in-scala/
object FlatMap5Collection extends App {
  val fruits = Seq("apple", "banana", "orange")
  fruits.map(_.toUpperCase)    // List(APPLE, BANANA, ORANGE)
  fruits.flatMap(_.toUpperCase) // List(A, P, P, L, E, B, A, N, A, N, A, O, R, A, N, G, E)

  def toInt(s: String): Option[Int] = Try(s.toInt).toOption
  val strings = Seq("1", "2", "foo", "3", "bar")
  val smapped = strings.map(toInt)          // List(Some(1), Some(2), None, Some(3), None)
  val sfmapped = strings.flatMap(toInt)      // List(1, 2, 3)
  val sfmapped2 = strings.map(toInt).flatten // List(1, 2, 3)

  val list = List(100,200,300,400,500)
  def to3(n: Int) = List(n-1, n, n+1)
  val lmapped = list.map(to3)     // List(List(99, 100, 101), List(199, 200, 201), List(299, 300, 301), List(399, 400, 401), List(499, 500, 501))
  val lfmapped = list.flatMap(to3) // List(99, 100, 101, 199, 200, 201, 299, 300, 301, 399, 400, 401, 499, 500, 501)

  val map = Map(1 -> "one", 2 -> "two", 3 -> "three")
  val m1: Seq[String] = 1 to map.size flatMap(map.get)
  val m2: Seq[Option[String]] = 1 to map.size map(map.get)
  val m3: Seq[String] = 1 to map.size flatMap(k => map.get(k))

  val chars = 'a' to 'd'
  val perms: Seq[String] = chars flatMap { a =>
    chars flatMap { b =>
      if (a != b) Seq("%c%c".format(a, b))
      else Seq()
    }
  } // Vector(ab, ac, ad, ba, bc, bd, ca, cb, cd, da, db, dc)

  val x3: Seq[Int] = Seq(1,2,3,4) flatMap { x => Seq(x, -x) } // List(1, -1, 2, -2, 3, -3, 4, -4)

  val evens = Seq(1,2,3,4) flatMap { x => if (x%2 == 0) Seq(x) else Seq() } // List(2, 4)

  val host: Option[String] = Some("google.com")
  val port: Option[Int] = Some(9000)  // None // Some(443)
  val addr: Option[InetSocketAddress] =
    host flatMap { h =>
      port map { p =>
        new InetSocketAddress(h, p)
      }
    } // if smth is null result is null without any if !
  println(addr)

  val o1 = Option(1)       // Option[Int]
  val oo1 = Option(o1)     // Option[Option[Int]]
  val ooo1 = Option(oo1)   // Option[Option[Option[Int]]]

  val opened1: Option[Int] = ooo1 flatMap(oo => oo.flatMap(o => o))
  val opened2: Option[Int] = ooo1.flatten.flatten


}

