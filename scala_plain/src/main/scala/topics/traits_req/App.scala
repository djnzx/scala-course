package topics.traits_req

object App extends App {
  val impl_a = new ImplA
  println(impl_a.a) // 42

  val trait_a = new TraitA {
    override val a: Int = 11
  }
  println(trait_a.a) // 11

  // there is impossible to instantiate Trait2 without Trait0, see 'this: Trait0 =>' in Trait2.scala
  val trait_b = new TraitA with TraitB {
    override val a: Int = 43
  }
  println(trait_b.a) // 43
  println(trait_b.b) // 1  variable is 0 on initialization phase !
  println(trait_b.c) // 44 method call !
}
