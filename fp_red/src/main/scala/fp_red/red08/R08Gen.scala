package fp_red.red08

import java.util.concurrent.{ExecutorService, Executors}

import fp_red.c_answers.c05laziness.Stream
import fp_red.c_answers.c06state.{RNG, State}
import fp_red.c_answers.c07parallelism.Par
import Prop.{CountToRun, Falsified, MaxSize, Passed, Proved, Result}
import fp_red.c_answers.c07parallelism.Par.Par

// property description
case class Prop(run: (MaxSize, CountToRun, RNG) => Result) {
  def &&(that: Prop): Prop = Prop { (max, n, rng) =>
    // run ME
    run(max, n, rng) match {
      // if OK -               run given that
      case Passed           => that.run(max, n, rng)
      case Proved           => that.run(max, n, rng)
      // if ERR -              don't touch that given
      case f@ _             => f
    }
  }
  def ||(that: Prop): Prop = Prop { (max, n, rng) =>
    // run ME
    run(max, n, rng) match {
      // if ERR -
      case Falsified(msg, _) => that.tag(msg).run(max, n, rng)
      // if OK -               don't even touch given
      case p@ _             => p
    }
  }
  def tag(msg: String): Prop = Prop { (max, n, rng) =>
    run(max, n, rng) match {
      case Falsified(err, cnt) => Falsified(s"$msg\n$err", cnt)
      case p@ _               => p
    }
  }
}

object Prop {
  // type aliases
  type FailedCase = String
  type SuccessCount = Int
  type CountToRun = Int
  type MaxSize = Int

  // result representation
  sealed trait Result {
    def isFalsified: Boolean
  }
  final case object Passed extends Result {
    override def isFalsified: Boolean = false
  }
  final case object Proved extends Result {
    override def isFalsified: Boolean = true
  }
  final case class Falsified(failure: FailedCase, successes: SuccessCount) extends Result {
    override def isFalsified: Boolean = true
  }

  def buildMessage[A](s: A, e: Exception): String =
    s"test case: $s\n"+
      s"generated an exception: ${e.getMessage}\n"+
      s"stack trace:\n ${e.getStackTrace.mkString("\n")}"

  // this guy knows how generate random Stream[A]
  def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] =
    Stream.unfold(rng) { rng => Some(g.sample.run(rng)) }

  /**
    * (Gen[A], A => Boolean) => Prop
    */
  def forAll[A](g: Gen[A])(p: A => Boolean): Prop = Prop { (max, n, rng) =>
    val s1: Stream[A] = randomStream(g)(rng)
    val s2: Stream[Int] = Stream.from(0)
    val s3: Stream[(A, Int)] = s1 zip s2
    val s4: Stream[Result] = s3.take(n).map { case (a, idx) =>
      try {
        if (p(a)) Passed else Falsified(a.toString, idx)
      } catch {
        case e: Exception => Falsified(buildMessage(a, e), idx)
      }
    }
    val s5: Option[Result] = s4.find(_.isFalsified)
    s5.getOrElse(Passed)
  }

  /**
    * (SGen[A], A => Boolean) => Prop
    */
  def forAll[A](g: SGen[A])(p: A => Boolean): Prop = forAll(i => g.size(i))(p)

  /**
    * (Int => Gen[A], A => Boolean) => Prop
    */
  def forAll[A](g: Int => Gen[A])(p: A => Boolean): Prop = Prop { (max, n, rng) =>
    // testcase size
    val casesPerSize = (n + (max - 1)) / max
    // generate the Stream of Props to run
    val props: Stream[Prop] = Stream.from(0).take((n min max) + 1).map { i => forAll(g(i))(p) }
    val prop: Prop = props.map(p => Prop { (max, _, rng) =>
      p.run(max, casesPerSize, rng)
    }).toList.reduce { _ && _ }
    prop.run(max,n,rng)
  }

  /**
    * check predicate
    */
  def check(p: => Boolean): Prop = Prop { (_,_,_) =>
    if (p) Passed else Falsified("()", 0)
  }

  // helper function to avoid call run on Prop instances
  def run(p: Prop,
          maxSize: Int = 100,
          testCases: Int = 100,
          rng: RNG = RNG.Simple(System.currentTimeMillis)): Unit =
    p.run(maxSize, testCases, rng) match {
      case Falsified(msg, n) => println(s"! Falsified after $n passed tests:\n $msg")
      case Passed           => println(s"+ OK, passed $testCases tests.")
      case Proved           => println(s"+ OK, proved property.")
    }
}

// how to generate the one piece of data
case class Gen[+A](sample: State[RNG, A]) {
  def map[B](f: A => B): Gen[B] =
    Gen(sample.map(f))

  def map2[B,C](g: Gen[B])(f: (A,B) => C): Gen[C] =
    Gen(sample.map2(g.sample)(f))

  def flatMap[B](f: A => Gen[B]): Gen[B] =
    Gen(sample.flatMap(a => f(a).sample))

  // alias for Gen.listOfN[A](Int, Gen[A])
  def listOfN(size: Int): Gen[List[A]] = Gen.listOfN(size, this)

  def **[B](g: Gen[B]): Gen[(A,B)] =
    (this map2 g)((_,_))
}

object Gen {
  def unit[A](a: => A): Gen[A] = Gen(State.unit(a))

  val boolean: Gen[Boolean] = Gen(State(RNG.boolean))

  def choose(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive-start)))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] =
    Gen(State.sequence(List.fill(n)(g.sample)))

  def listOf[A](g: Gen[A]): SGen[List[A]] =
    SGen(n => g.listOfN(n))

  def weighted[A](g1: (Gen[A],Double), g2: (Gen[A],Double)): Gen[A] = {
    /* The probability we should pull from `g1`. */
    val g1Threshold = g1._2.abs / (g1._2.abs + g2._2.abs)

    Gen(State(RNG.double).flatMap(d => if (d < g1Threshold) g1._1.sample else g2._1.sample))
  }
}

// sized generation!
case class SGen[+A](size: Int => Gen[A])

object Playground extends App {

  val es: ExecutorService = Executors.newCachedThreadPool
  val p1 = Prop.forAll(Gen.unit(Par.unit(1)))(i =>
    Par.map(i)(_ + 1)(es).get == Par.unit(2)(es).get)

  val smallInt = Gen.choose(-10,10)
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
    Gen.choose(1,4).map(Executors.newFixedThreadPool) -> .75,
    Gen.unit(Executors.newCachedThreadPool) -> .25)

  def forAllPar_V1[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S.map2(g)((_,_))) { case (s,a) => f(a)(s).get }

  def forAllPar_V2[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S ** g) { case (s, a) => f(a)(s).get }

  // We can even introduce ** as a pattern using custom extractors
  def forAllPar[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    Prop.forAll(S ** g) { case s ** a => f(a)(s).get }

  object ** {
    def unapply[A,B](p: (A,B)) = Some(p)
  }

  def listOf1[A](g: Gen[A]): SGen[List[A]] =
    SGen(n => g.listOfN(n max 1))
  
  def checkPar(p: Par[Boolean]): Prop =
    forAllPar(Gen.unit(()))(_ => p)

  val p2a = checkPar {
    equal (
      Par.map(Par.unit(1))(_ + 1),
      Par.unit(2)
    )
  }
}
