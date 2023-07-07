package interview.linearization

object LinearPlayground extends App {

  trait A {
    println("Trait A mixing..")
  }
  trait B {
    println("Trait B mixing..")
  }
  trait C {
    println("Trait C mixing..")
  }
  trait AB extends A with B {
    println("Trait AB")
  }
  trait BC extends B with C {
    println("Trait BC")
  }
  trait CA extends C with A {
    println("Trait CA")
  }
  // 1:  A, B, AB
  // 2:     C, BC
  // 3:        CA
  // 4:        App
  trait App extends BC with CA with AB {
    println("Trait App")
  }

  new App {}


}
