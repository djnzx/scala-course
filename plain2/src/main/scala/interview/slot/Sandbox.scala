package interview.slot

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.util.Random

object Sandbox {

  trait SlotMachine {
    def spin(): Vector[Int]
  }

  def isWin(xs: Vector[Int]): Boolean =
    xs.toSet.size == 1

  def genWin: Vector[Int] = {
    val x = Random.nextInt(10)
    Vector.fill(3)(x)
  }

  def genLoss: Vector[Int] =
    Vector.fill(3)(Random.nextInt(10))

  def genLossGuarantee: Vector[Int] =
    genLoss match {
      case x if isWin(x) => genLossGuarantee
      case x             => x
    }

  class LiveSlotMachine(k: Double) extends SlotMachine {
    override def spin(): Vector[Int] =
      if (Random.nextDouble() < k) genWin else genLoss
  }

  object LiveSlotMachine {
    val initial5: Double = 0.039
    val default5: SlotMachine = new LiveSlotMachine(initial5)
  }

  def analyze(impl: SlotMachine): Double = {
    val n = 1_000_000
    val (win, loss) = (1 to n).foldLeft(0 -> 0) { case ((s1, s2), _) =>
      impl.spin() match {
        case x if isWin(x) => (s1 + 1) -> s2
        case _             => s1       -> (s2 + 1)
      }
    }
    win.toDouble / loss
  }

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Sandbox._

  test("1") {
    val impl = LiveSlotMachine.default5
    val k = analyze(impl)
    pprint.log(k)
  }

}
