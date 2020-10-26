package whg

import tools.spec.ASpec

class ColorSpec extends ASpec {
  
  it("another color") {
    val data = Seq(
      Black -> White,
      White -> Black
    )
    for {
      (in, out) <- data
    } Color.another(in) shouldEqual out
  }

}
