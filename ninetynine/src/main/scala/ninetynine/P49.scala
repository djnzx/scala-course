package ninetynine

import scala.annotation.tailrec

object P49 {
  val variants = List(0, 1)

  def gray(n: Int): List[List[Int]] = n match {
    case 0 => List(Nil)
    case n =>
      val prev = gray(n - 1)
      variants.flatMap { d => prev.map(d :: _) }
  }
  
  def gray2(n: Int): List[List[Int]] = n match {
    case 0 => List(Nil)
    case n => gray(n - 1).flatMap { l => variants.map(l :+ _) }
  }
  
}

object P49Strings {
  val variants = List("0", "1")
  
  def gray(n: Int): List[String] = n match {
    case 0 => List("")
    case n =>
      val prev = gray(n - 1)
      variants.flatMap { d => prev.map(d + _) }
  }
  
  def gray2(n: Int): List[String] = n match {
    case 0 => List("")
    case n => gray(n - 1).flatMap { d => variants.map(d + _) }
  }
  
}

object P49TR {
  val variants = List("0", "1")

  def gray(num: Int): List[String] = {
    
    @tailrec
    def step(n: Int, list: List[String]): List[String] = n match {
      case `num` => list
      case n     => step(n + 1, variants.flatMap(d => list.map(d + _)))
    }
    
    step(0, List(""))
  }
}

object P49ITR {
  val variants = List("0", "1")

  def gray(num: Int): List[String] = {
    var n = 0
    var list = List("")
    while (n < num) {
      list = variants.flatMap(d => list.map(d + _))
      n += 1
    }
    list
  }
}

object P49Plain {
  def gray(num: Int) = {
    var n = 0
    var a = Array("")
    while (n < num) {
      a = a.map(s => "0" + s).concat(a.map(s => "1" + s))
      n += 1
    }
    a
  }
}

class P49Spec extends NNSpec {
  val data = Seq(
    0 -> List(""),
    1 -> List("0", "1"),
    2 -> List("00", "01", "10", "11"),
    3 -> List("000", "001", "010", "011", "100", "101", "110", "111"),
  )

  describe("head recursive") {
    it("list implementation") {
      import P49._

      data.foreach { case (in, out) =>
        gray(in).map(_.mkString).shouldEqual(out)
        gray2(in).map(_.mkString).shouldEqual(out)
      }
    }

    it("string implementation") {
      import P49Strings._

      data.foreach { case (in, out) =>
        gray(in).shouldEqual(out)
        gray2(in).shouldEqual(out)
      }
    }
  }
  
  describe("tail recursive") {
    it("string implementation") {
      import P49TR._

      data.foreach { case (in, out) =>
        gray(in).shouldEqual(out)
      }
    }
  }

  it("iterative string implementation") {
    data.foreach { case (in, out) =>
      P49ITR.gray(in).shouldEqual(out)
    }
  }

  it("no flatmap string implementation") {
    data.foreach { case (in, out) =>
      P49Plain.gray(in).shouldEqual(out)
    }
  }

}
