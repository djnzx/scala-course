package diwo.test

import tools.spec.ASpec

class DomainSpec extends ASpec {
  
  import diwo.Domain._
  import diwo.ExtendedSyntax._
  
  it("Normal Ticket") {
    val nt = NormalTicket(Set(1,2,3,4,5), Set(10,11))
    nt shouldEqual Right(nt.getOrDie)

      NormalTicket(Set(1,2,3,4,5,6), Set(10,11)) shouldEqual
        Left("Normal ticket count of numbers size must equal 5, 6 given")

      NormalTicket(Set(1,2,3,4,5), Set(7,10,11)) shouldEqual
        Left("Normal ticket count of star numbers size must equal 2, 3 given")
  }

  it("System Ticket") {
    val st = SystemTicket(Set(1,2,3,4,5,7,8,9), Set(10,11,22,33))
    st shouldEqual Right(st.getOrDie)

    SystemTicket(Set(1,2,3), Set(10,11)) shouldEqual
      Left("System ticket count of numbers size must be in range[5, 10], 3 given")
    
    SystemTicket(Set(1,2,3,4,5,6,7,8,9,10,11), Set(10,11)) shouldEqual
      Left("System ticket count of numbers size must be in range[5, 10], 11 given")
    
    SystemTicket(Set(1,2,3,4,5,6,7,8,9,10), Set(10,11,12,13,14,15)) shouldEqual
      Left("System ticket count of star numbers size must be in range[2, 5], 6 given")
    
  }

  it("Draw") {
    /**
      * actually draw has absolutely the same implementation,
      * only the difference is the message
      * 
      * we put it in the separate class
      * only because if semantics
      */
  }

  it("Parse Tickets / Draw") {
    val t = Seq(
      "1,2,3,4/11,12" -> None,
      "1,2,3,4,5/11,12" -> Some(NormalTicket.buildOrDie(Set(1,2,3,4,5), Set(11,12))),
      "1,2,3,4,5,6,7,8,9,10/11,12,13,14,15" -> Some(SystemTicket.buildOrDie(Set(1,2,3,4,5,6,7,8,9,10), Set(11,12,13,14,15))),
    ).map { case (a, b) => Ticket(a) -> b }
    val d = Seq(
      "1,2,3,4,5/11,12" -> Right(Draw.buildOrDie(Set(1,2,3,4,5), Set(11,12))),
    ).map { case (a, b) => Draw.parse(a) -> b }
    
    for {
      (in, out) <- t ++ d
    } in shouldEqual out  
  }
}
