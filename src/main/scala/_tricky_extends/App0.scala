package _tricky_extends

object App0 extends App {
  val i0 = new Impl0
  println(i0.smart)

  val t0 = new Trait0 {
    override val smart: Int = 43
  }
  println(t0.smart)

  val t2 = new Trait2 with Trait0 {
    override val smart: Int = 43
  }
  println(t2)
}
