package _tricky_extends

object App0 extends App {
  val i0 = new Impl0
  println(i0.smart)

  val t0 = new Trait0 {
    override val smart: Int = 43
  }
  println(t0.smart)

  // there is impossible to instantiate Trait2 without Trait0, see 'this: Trait0 =>' in Trait2.scala
  val t2 = new Trait2 with Trait0 {
    override val smart: Int = 43
  }
  println(t2)
}
