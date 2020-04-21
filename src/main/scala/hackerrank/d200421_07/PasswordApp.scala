package hackerrank.d200421_07

import java.math.BigDecimal

object PasswordApp extends App {
  type BD = BigDecimal
  val bd0 = new BD(0)
  val bd1 = new BD(1)
  val M = new BD(1_000_000_000L + 7)
  val p = new BD(131)

  // get the ASCII code
  def f(c: Char): BigDecimal = new BD(c.toInt)

  // power without precision losing
  def pow(a: BD, b: Int): BD =
    (0 until b).foldLeft(bd1) { (b, _) => b multiply a }

  // hash
  def h(pass: String): Long =
    pass
      .zip(Range.inclusive(pass.length-1, 0, -1))
      .foldLeft(bd0) { case (r, (c, i)) =>
        r.add( pow(p, i).multiply(f(c)) )
      }
      .remainder(M)
      .longValueExact

  def isValid1(pass: String, hash: Long): Boolean = hash == h(pass)

  def isValid2(part: String, hash: Long): Boolean =
    ('0' to 'z').map { c => s"$part$c" }
      .foldLeft(false) { (r, s) => r || isValid1(s , hash) }

  case class State(hash: Long, passwd: String, outcome: List[Int])

  def authEvents(events: List[List[String]]): List[Int] = events match { case (_::p0::Nil)::tl =>
    tl.foldLeft(State(h(p0), p0, List.empty[Int])) { (st, ev) =>
      ev match {
        case "setPassword"::pwd::Nil => st.copy(hash = h(pwd), passwd = pwd)
        case _::hash::Nil            => st.copy(outcome = {
          val hl = hash.toLong
          val valid = if (hl == st.hash || isValid2(st.passwd, hl)) 1 else 0
          valid::st.outcome
        })
      }
    }
      .outcome reverse
  }

  val a = authEvents(List(
    List("setPassword", "000A"),
    List("authorize", "108738450"),
    List("authorize", "108738449"),
    List("authorize", "244736787")
  ))

  assert( a == List(0,1,1))
}
