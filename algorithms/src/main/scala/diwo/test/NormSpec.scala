package diwo.test

import diwo.Domain.{NormalTicket, SystemTicket}
import tools.spec.ASpec

class NormSpec extends ASpec {
  
  import diwo.Norm._

  it("normalize") {
    val st = SystemTicket.buildOrDie(Set(1,2,3,4,5,6), Set(1,2,3))
    val normalized = Seq(
      (Set(5, 1, 6, 2, 3), Set(1, 2)),
      (Set(5, 1, 6, 2, 3), Set(1, 3)),
      (Set(5, 1, 6, 2, 3), Set(2, 3)),
      (Set(5, 1, 6, 2, 4), Set(1, 2)),
      (Set(5, 1, 6, 2, 4), Set(1, 3)),
      (Set(5, 1, 6, 2, 4), Set(2, 3)),
      (Set(5, 1, 6, 3, 4), Set(1, 2)),
      (Set(5, 1, 6, 3, 4), Set(1, 3)),
      (Set(5, 1, 6, 3, 4), Set(2, 3)),
      (Set(5, 1, 2, 3, 4), Set(1, 2)),
      (Set(5, 1, 2, 3, 4), Set(1, 3)),
      (Set(5, 1, 2, 3, 4), Set(2, 3)),
      (Set(5, 6, 2, 3, 4), Set(1, 2)),
      (Set(5, 6, 2, 3, 4), Set(1, 3)),
      (Set(5, 6, 2, 3, 4), Set(2, 3)),
      (Set(1, 6, 2, 3, 4), Set(1, 2)),
      (Set(1, 6, 2, 3, 4), Set(1, 3)),
      (Set(1, 6, 2, 3, 4), Set(2, 3))
    ).map { case (a, b) => NormalTicket.buildOrDie(a, b) }

    val nt = NormalTicket.buildOrDie(Set(5, 1, 6, 2, 3), Set(2, 3))
    
    normalize(st) should contain theSameElementsAs normalized
    normalize(nt) shouldEqual Seq(nt) 

  }
  
}
