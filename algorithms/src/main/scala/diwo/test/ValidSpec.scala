package diwo.test

import tools.spec.ASpec

class ValidSpec extends ASpec {
  
  import diwo.valid._
  
  it("validate") {

    val s4 = Set(1, 2, 3, 4)
    
    validate(s4)(_.size == 3) match {
      case vf =>
        vf shouldEqual None
    }
    
    validate(s4)(_.size == 4) match {
      case vt =>
        vt shouldEqual Some(s4)
    }
      
  }
  
  it("sizeEq") {

    val s4 = Set(1, 2, 3, 4)
    
    sizeEq(s4, 3) shouldEqual Left("size must equal 3, 4 given")
    sizeEq(s4, 4) shouldEqual Right(s4)
  }
  
  it("sizeBtw") {

    val s4 = Set(1, 2, 3, 4)

    sizeBtw(s4, 5,10) shouldEqual Left("size must be in range[5, 10], 4 given")
    sizeBtw(s4, 2, 5) shouldEqual Right(s4)
  }
  
  it("toInt") {
    toInt("5") shouldEqual Some(5)
    toInt("5a") shouldEqual None
  }
  
  it("parseTwoArrays") {
    val d = Seq(
      "1,2,3,4,5,6/10,11,12" -> Some((Set(1,2,3,4,5,6), Set(10,11,12))),
      "1,2,3,4a,5,6/10,11,12" -> Some((Set(1,2,3,5,6), Set(10,11,12))),
      "1,2,3,4a,5,6/11" -> Some((Set(1,2,3,5,6), Set(11))),
      "1,2,3,4a,5,6/g" -> Some((Set(1,2,3,5,6), Set())),
      "a/g" -> Some((Set(), Set())),
      "/5" -> Some((Set(),Set(5))), // here Java gives 2-elements array
      "1/" -> None,                 // here Java gives 1-element array
      "/" -> None,
      "" -> None,
    )
    
    for {
      (a, o) <- d
    } parseTwoArrays(a) shouldEqual o
    
    pprint.pprintln("/5".split("/"))
    pprint.pprintln("5/".split("/"))
  }
  
}
