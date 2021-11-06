package scalacheck

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

/**
  * we define properties
  * - they will be checked automatically
  * - data will be generated automatically (randomly with corner cases, or by our custom logic)
  * - tests will be run in parallel
  * - these are descriptions!
  */
object PropsStringOperation extends Properties("String some methods"){

  property("string: startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

  property("string: concatenate") = forAll { (a: String, b: String) =>
    val ab = a + b
    
    ab.length >= a.length && 
      ab.length >= b.length
  }

  property("string: substring") = forAll { (a: String, b: String, c: String) =>
    (a + b + c).substring(a.length, a.length + b.length) == b
  }

}

object PropertiesIntegerAddition extends Properties("calc.add.assoc.prop") {
  
  property("int add: zero element") = forAll { (a: Int) =>
    a + 0 == a &&
      0 + a == a
  }
  
  property("int add: commutativity") = forAll { (a: Int, b: Int) =>
    a + b == b + a
  }
  
  property("int add: associativity") = forAll { (a: Int, b: Int, c: Int) =>
    (a + b) + c == a + (b + c)
  }
  
}


