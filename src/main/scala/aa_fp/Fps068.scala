package aa_fp

object Fps068 extends App {

  case class Debuggable(value: Int, msg: String) {
    def map(f: Int => Int): Debuggable = {
      println("\n>>>> entered map  >>>>")
      println(s"map: value: ${value}")
      println(s"map: msg: (${msg})")
      val nextValue = f(value)   //Int
      // there is no `nextValue.msg`
      println(s"map: nextValue: ${nextValue}")
      println("<<<< leaving map  <<<<\n")
      Debuggable(nextValue, msg)
    }

    def flatMap(f: Int => Debuggable): Debuggable = {
      println("\n>>>> entered fmap >>>>")
      println(s"fmap: value: ${value}")
      println(s"fmap: msg: (${msg})")
      val nextValue = f(value)
      println(s"fmap: msg: (${msg})")
      println(s"fmap: next val: ${nextValue.value}")
      println(s"fmap: next msg: \n(${nextValue.msg})")
      println("<<<< leaving fmap <<<<\n")
      Debuggable(nextValue.value, msg + "\n" + nextValue.msg)
    }
  }

  def f(a: Int): Debuggable = {
    println(s"\n[f: a = $a]")
    val result = a * 2
    Debuggable(result, s"f: input: $a, result: $result")
  }

  def g(a: Int): Debuggable = {
    println(s"\n[g: a = $a]")
    val result = a * 3
    Debuggable(result, s"g: input: $a, result: $result")
  }

  def h(a: Int): Debuggable = {
    println(s"\n[h: a = $a]")
    val result = a * 4
    Debuggable(result, s"h: input: $a, result: $result")
  }

  val r: Debuggable = for {
    fr <- f(100) // flatMap
    gr <- g(fr)  // flatMap
    hr <- h(gr)  // map
  } yield hr

  println(s"r.value = ${r.value}")
  println(s"r.msg = ${r.msg}")

}
