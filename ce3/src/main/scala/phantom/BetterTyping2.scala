package phantom

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** phantom types help */
class BetterTyping2 extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  /** 1. let's say we have legacy library
    * but requiring certain order of calling to do the job
    *
    * - it's error-prone.
    * - it's really easy to mix the ordering due to the signature
    */
  case class State0()
  class OriginallyDesigned {
    def step1(x: Int): State0 = State0()
    def step2(x: State0): State0 = State0()
    def step3(x: State0): State0 = State0()
    def step4(x: State0): Unit = ()
  }

  /** 2. one possible solution
    * is to create corresponding case classes
    * and write a facade:
    */
  case class State1()
  case class State2()
  case class State3()
  class BetterDesigned {
    def step1(x: Int): State1 = State1()
    def step2(x: State1): State2 = State2()
    def step3(x: State2): State3 = State3()
    def step4(x: State3): Unit = ()
  }

  /** 3. but with phantom types we can do better
    * we can express the stage in a clear way!
    */
  sealed trait St
  trait Created   extends St
  trait Paid      extends St
  trait Delivered extends St

  case class State[A](state: State0)

  // format: off
  class BetterWithSemantic {
    private val underline = new OriginallyDesigned
    def step1(x: Int)             : State[Created]   = State(underline.step1(x))
    def step2(x: State[Created])  : State[Paid]      = State(underline.step2(x.state))
    def step3(x: State[Paid])     : State[Delivered] = State(underline.step3(x.state))
    def step4(x: State[Delivered]): Unit             =       underline.step4(x.state)
  }
  // format: on

  test("1") {

    val impl = new BetterWithSemantic
    val created: State[Created] = State[Created](State0())
    val paid: State[Paid] = impl.step2(created)

    /** will not compile */
//    impl.step3(created)
//    impl.step4(created)
  }

}
