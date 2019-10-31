package x00topics.traits_req

// there is no way to use TraitB without traitA
trait TraitB { this: TraitA =>
  val b = a + 1
  def c = a + 1
}
