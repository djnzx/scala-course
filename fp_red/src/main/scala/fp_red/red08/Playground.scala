package fp_red.red08

import java.util.concurrent.{ExecutorService, Executors}
import fp_red.c_answers.c07parallelism.Par
import fp_red.c_answers.c07parallelism.Par.Par
import Prop._
import Gen._

object Playground extends App {

  val es: ExecutorService = Executors.newCachedThreadPool
  val p1 = Prop.forAll(Gen.unit(Par.unit(1)))(i =>
    Par.map(i)(_ + 1)(es).get == Par.unit(2)(es).get)

  val smallInt = Gen.choose(-10, 10)
  val maxProp = Prop.forAll(Gen.listOf(smallInt)) { l =>
    val max = l.max
    !l.exists(_ > max) // No value greater than `max` should exist in `l`
  }

  val p2 = Prop.check {
    val pa = Par.map(Par.unit(1))(_ + 1)
    val pb = Par.unit(2)
    pa(es).get == pb(es).get // noise
  }

  // lift two pars to
  def equal[A](p1: Par[A], p2: Par[A]): Par[Boolean] =
    Par.map2(p1, p2)(_ == _)

  val p3 = Prop.check {
    equal(
      Par.map(Par.unit(1))(_ + 1),
      Par.unit(2)
    )(es).get
  }

  val S: Gen[ExecutorService] = Gen.weighted(
    Gen.choose(1, 4).map(Executors.newFixedThreadPool) -> .75,
    Gen.unit(Executors.newCachedThreadPool) -> .25)

  def forAllPar_V1[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S.map2(g)((_, _))) { case (s, a) => f(a)(s).get }

  def forAllPar_V2[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S ** g) { case (s, a) => f(a)(s).get }

  // We can even introduce ** as a pattern using custom extractors
  def forAllPar[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S ** g) { case s ** a => f(a)(s).get }

  object ** {
    def unapply[A, B](p: (A, B)) = Some(p)
  }

  def listOf1[A](g: Gen[A]): SGen[List[A]] =
    SGen(n => g.listOfN(n max 1))

  def checkPar(p: Par[Boolean]): Prop =
    forAllPar(Gen.unit(()))(_ => p)

  val p2a = checkPar {
    equal(
      Par.map(Par.unit(1))(_ + 1),
      Par.unit(2)
    )
  }

  def forAllPar2[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    forAll(S ** g) { case (s,a) => f(a)(s).get }

  def forAllPar3[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    forAll(S ** g) { case s ** a => f(a)(s).get }

  val pint = Gen.choose(0,10) map (Par.unit(_))
  val p4 =
    forAllPar(pint)(n => equal(Par.map(n)(y => y), n))

  val forkProp = forAllPar(pint2)(i => equal(Par.fork(i), i)) tag "fork"

}
