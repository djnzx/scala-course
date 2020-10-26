package whg

import tools.spec.ASpec

class ColorSpec extends ASpec {

  val data = Seq(
    Black -> White,
    White -> Black
  )

  it("another color. syntax #1") {
    for {
      (in, out) <- data
    } Color.another(in) shouldEqual out
  }
  
  it("another color. syntax #2") {
    for {
      (in, out) <- data
    } in.another shouldEqual out
  }

}
