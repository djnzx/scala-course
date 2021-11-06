package ipv4

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class IPv4Spec extends AnyFunSpec with Matchers {
  import IPv4._
  
  it("1") {
    val source = Seq(
      "1.2.3.4",
      "1.2.3.4",
      "7.7.7.7",
      "7.8.8.8",
      "7.8.8.8",
      "7.8.8.8",
      "7.8.8.9",
      "1.2.3.5",
      "1.2.3.5"
    )
    val expected = Map(
      1 -> Map(2 -> Map(3 -> Set(4,5))),
      7 -> Map(
        7 -> Map(7 -> Set(7)),
        8 -> Map(8 -> Set(8, 9))       
      ),
    )
    val uniq = UniqueIPs.process(source)

    uniq.map shouldEqual expected
    uniq.contains(IP(1,2,3,4)) shouldEqual true
    uniq.contains(IP(11,2,3,4)) shouldEqual false
    uniq.contains("1.2.3.4") shouldEqual true
    uniq.contains("11.2.3.4") shouldEqual false
  }
  
}
