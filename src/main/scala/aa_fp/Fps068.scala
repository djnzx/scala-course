package aa_fp

/**
  * templates
  * https://habr.com/ru/post/414813/
  */
object Fps068 extends App {

  case class Debuggable(value: Int, msg: String) { self =>

    def flatMap(f: Int => Debuggable): Debuggable = {
      println("\nflatMap: >>>>")
      println(s"flatMap: val: ${self.value}")
      println(s"flatMap: msg: ${self.msg}")
      val next: Debuggable = f(self.value)
      println(s"\nflatMap: msg: ${self.msg}")
      println(s"flatMap: next val: ${next.value}")
      println(s"flatMap: next msg: ${next.msg}")
      val msg2 = s"${self.msg} >> ${next.msg}"
      println(s"flatMap: comb msg: $msg2")
      println("flatMap: <<<<")
      Debuggable(next.value, msg2)
    }
    def map(f: Int => Int): Debuggable = {
      println("\nmap: >>>>")
      println(s"map: val: ${self.value}")
      println(s"map: msg: ${self.msg}")
      val next: Int = f(value)
      println(s"\nmap: next val: $next")
      println("map: <<<<")
      Debuggable(next, self.msg)
    }

  }

  def f(a: Int): Debuggable = {
    println(s"\nf: Int => Debuggable(Int, String)")
    println(s"f: applying to `$a`...")
    val r = a * 2
    val line = s"f($a)=$r"
    println(s"f: applied: `Debuggable($r, $line)`")
    Debuggable(r, s"$line")
  }

  def g(a: Int): Debuggable = {
    println(s"\ng: Int => Debuggable(Int, String)")
    println(s"g: applying to `$a`...")
    val r = a * 3
    val line = s"g($a)=$r"
    println(s"g: applied: `Debuggable($r, $line)`")
    Debuggable(r, s"$line")
  }

  def h(a: Int): Debuggable = {
    println(s"\nh: Int => Debuggable(Int, String)")
    println(s"h: applying to `$a`...")
    val r = a * 4
    val line = s"h($a)=$r"
    println(s"h: applied: `Debuggable($r, $line)`")
    Debuggable(r, s"$line")
  }

  val r1: Debuggable = for {
    fr <- f(100) // 1. f: Int => Debuggable (x=x*2)
                 // 2. applying `f` to `100`
                 // 3. the result of `f(100)` is `Debuggable(200, f(100)=200)`
                 // 4. flatMap: (Int => Debuggable): Debuggable
                 // 5. `<-` is actually `flatMap` which is running on `(3)`
                 // 6. `fr` and `g(fr)` are parts of the syntax `fr -> g(fr)`
                 // 7. running Debuggable(200, f(100)=200).flatMap(fr -> g(fr))
    gr <- g(fr)  // 8. g: Int => Debuggable (x=x*3)
                 // 9. applying `g` to Debuggable.value // 200
                 // 10. the result of `g(value)` is `Debuggable(600, g(200)=600)`
                 // 11. running Debuggable(600, g(200)=600).flatMap(gr -> h(gr))
    hr <- h(gr)  // 12. h: Int => Debuggable (x=x*4)
                 // 13. applying `h` to `600`
                 // 14. the result of `h(600)` is `Debuggable(2400, h(600)=2400)`
                 // 15. runnig Debuggable(2400, h(600)=2400).map(hr -> hr)
                 // 16. the result is Debuggable(2400, h(600)=2400)
                 // 17. unwinding
  } yield hr

  println("====")
  println(s"r1.val: ${r1.value}")
  println(s"r1.msg: ${r1.msg}")
  println("====")

  lazy val r2: Debuggable =
    f(100) flatMap { fr =>
      g(fr) flatMap { gr =>
        h(gr) map { hr =>
          hr
        }
      }
    }
}
