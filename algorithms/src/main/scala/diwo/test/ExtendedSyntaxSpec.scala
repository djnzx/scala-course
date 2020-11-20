package diwo.test

import tools.spec.ASpec

class ExtendedSyntaxSpec extends ASpec {
  
  import diwo.ExtendedSyntax._
  
  it("Either.or") {
    
    val data = Seq(
      (Right(1),       Right(2))       -> Right(1),
      (Right(1),       Left("Err"))    -> Right(1),
      (Left("Error"),  Right(3))       -> Right(3),
      (Left("Error1"), Left("Error2")) -> Left("Error2"),
    )
    
    for {
      ((e1, e2), r) <- data
    } e1 or e2 shouldEqual r
    
  }

  it("Either.mapLeft") {
    
    val data = Seq(
      Right(1)         -> Right(1),
      Right(123)       -> Right(123),
      Left("Error")    -> Left("ERROR"),
      Left("Error1")   -> Left("ERROR1"),
    )
    
    val mapper = (s: String) => s.toUpperCase
    
    for {
      (in, out) <- data
    } in.mapLeft(mapper) shouldEqual out
    
  }
  
  it("Either.getOrDie") {

    Right(456).getOrDie shouldEqual 456
    def aboutToDie = Left("Error 345").getOrDie
    
    an  [IllegalArgumentException] should be thrownBy aboutToDie
    (the [IllegalArgumentException] thrownBy aboutToDie).getMessage should include ("345")

  }
  
  it("Exception") {
    def ex = !"invalid data"
    
    an  [IllegalArgumentException] should be thrownBy ex
    the [IllegalArgumentException] thrownBy ex should have message "invalid data"
  }
}
