package fp_red.red08

import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

object ScalaCheckExperiments extends App {
  // this thing ONLY KNOWS how to generate list of int with size<=100
  val intList: Gen[List[Int]] = Gen.listOf(Gen.choose(0, 100))

  // this is PROPERTY (DESCRIPTION) which will be tested later
  val prop: Prop =
    forAll(intList) { ns: List[Int] => ns.reverse.reverse == ns } &&
      forAll(intList) { ns: List[Int] => ns.headOption == ns.reverse.lastOption }

  def filter[A](la: List[A])(p: A => Boolean): List[A] = la.filter(p)
  def filterGt10(x: Int): Boolean = x > 10

  // this is PROPERTY (DESCRIPTION) which will be tested later
  val prop2: Prop =
    forAll(intList) { ns => filter(ns) { filterGt10 }.length <= ns.length }

  // this is PROPERTY (DESCRIPTION) which will be tested later
  val failingProp: Prop = forAll(intList)(ns => ns.reverse == ns)

  // the goal is not to write test data, but describe the behavior
  // and split and postpone the test generation

  prop.check
  prop2.check
  failingProp.check

}
