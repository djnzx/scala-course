package diwo.test

import tools.spec.ASpec

class PrizeSpec extends ASpec {
  
  val t = Set
  import diwo.Domain._
  import diwo.Prize
  
  it("1") {
    type SISI = (Set[Int], Set[Int])
    
    def mapperSome(t: ((SISI, SISI), Int)) = t match {
      case (((t1, t2), (d1, d2)), r) => (NormalTicket.buildOrDie(t1, t2), Draw.buildOrDie(d1, d2)) -> Some(r)
    }
    def mapperNone(t: (SISI, SISI)) = t match {
      case ((t1, t2), (d1, d2)) => (NormalTicket.buildOrDie(t1, t2), Draw.buildOrDie(d1, d2)) -> None
    }
    
    val some = Seq(
      ((t(1,2,3,4,5),     t(1,2)), (t(1,2,3,4,5), t(1,2))) -> 1,
      ((t(1,2,3,4,5),     t(1,2)), (t(1,2,3,4,5), t(1,21))) -> 2,
      ((t(1,2,3,4,5),     t(1,2)), (t(1,2,3,4,5), t(11,21))) -> 3,
      ((t(11,2,3,4,5),    t(1,2)), (t(1,2,3,4,5), t(1,2))) -> 4,
      ((t(11,2,3,4,5),    t(1,2)), (t(1,2,3,4,5), t(1,20))) -> 5,
      ((t(11,2,3,4,5),    t(1,2)), (t(1,2,3,4,5), t(10,20))) -> 6,
      ((t(1,2,3,40,50),   t(1,2)), (t(1,2,3,4,5), t(1,2))) -> 7,
      ((t(1,2,9,40,50),   t(1,2)), (t(1,2,3,4,5), t(1,2))) -> 8,
      ((t(1,2,3,40,50),   t(1,2)), (t(1,2,3,4,5), t(1,20))) -> 9,
      ((t(1,2,3,40,50),   t(1,2)), (t(1,2,3,4,5), t(10,20))) -> 10,
      ((t(1,20,30,40,50), t(1,2)), (t(1,2,3,4,5), t(1,2))) -> 11,
      ((t(1,2,30,40,50),  t(1,2)), (t(1,2,3,4,5), t(10,2))) -> 12,
      ((t(10,20,30,4,5),  t(1,2)), (t(1,2,3,4,5), t(10,20))) -> 13,
    ).map(mapperSome)
    
    val none = Seq(
      ((t(1,2,3,4,5), t(1,2)), (t(11,12,13,14,15), t(1,2))),
      ((t(1,2,3,4,5), t(1,2)), (t( 1,12,13,14,15), t(1,21))),
    ).map(mapperNone)
    
    for {
      ((t, d), r) <- some ++ none
    } Prize.calculate(d, t) shouldEqual r
    
  }
  
}
