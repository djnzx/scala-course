package x060essential

object ADT extends App {

  object ProductTypePattern_HasAnd {
    // product - has - AND - v1
    case class B(id: Int)
    case class C(name: String)
    case class A1(b: B, c: C)
    // product - has - AND - v2
    trait A2 {
      def b: B
      def c: C
    }
  }

  object ProductTypePattern_IsAnd {
    // is - AND
    trait D
    trait E
    trait F
    trait A3 extends D with E with F
  }

  object SumTypePattern_IsOr {
    // sum - is - OR
    sealed trait A
    final case class B() extends A
    final case class C() extends A
  }

  object HasOr1 {
    trait A {
      def d: D
    }
    sealed trait D
    final case class B() extends D
    final case class C() extends D
  }

  object HasOr2 {
    sealed trait A
    case class B()
    case class C()
    final case class D(b: B) extends A
    final case class E(c: C) extends A
  }

  sealed trait TrafficLight
  final case class Red() extends TrafficLight
  final case class Yellow() extends TrafficLight
  final case class Green() extends TrafficLight

  sealed trait CalcResult
  final case class OK(value: Int) extends CalcResult
  final case class ERR(msg: String) extends CalcResult

  sealed trait WaterSource
  final case object Well extends WaterSource
  final case object Spring extends WaterSource
  final case object Tap extends WaterSource
  case class Bottle(source: WaterSource, size: Int)

  Bottle(Well, 1)
  Bottle(Spring, 2)
}
