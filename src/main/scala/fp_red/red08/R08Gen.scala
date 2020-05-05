package fp_red.red08

import java.util.concurrent.{ExecutorService, Executors}

import fp_red.answers.c05laziness.Stream
import fp_red.answers.c06state.{RNG, State}
import fp_red.answers.c07parallelism.Par
import Prop.{CountToRun, Falsified, MaxSize, Passed, Result}

// property description
case class Prop(run: (MaxSize, CountToRun, RNG) => Result) {
  def &&(that: Prop): Prop = Prop { (max, n, rng) =>
    // run ME
    run(max, n, rng) match {
      // if OK -               run given that
      case Passed           => that.run(max, n, rng)
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

  def run(p: Prop,
          maxSize: Int = 100,
          testCases: Int = 100,
          rng: RNG = RNG.Simple(System.currentTimeMillis)): Unit =
    p.run(maxSize, testCases, rng) match {
      case Falsified(msg, n) => println(s"! Falsified after $n passed tests:\n $msg")
      case Passed           => println(s"+ OK, passed $testCases tests.")
    }
}

// how to generate the one piece of data
case class Gen[+A](sample: State[RNG, A]) {
  // alias for Gen.listOfN[A](Int, Gen[A])
  def listOfN(size: Int): Gen[List[A]] = Gen.listOfN(size, this)
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
}

// sized generation!
case class SGen[+A](size: Int => Gen[A])

object Playground extends App {

  /**
    * check predicate
    */
  def check(p: => Boolean): Prop = Prop { (_,_,_) =>
    if (p) Passed else Falsified("()", 0)
  }

  val es: ExecutorService = Executors.newCachedThreadPool
  val p1 = Prop.forAll(Gen.unit(Par.unit(1)))(i =>
    Par.map(i)(_ + 1)(es).get == Par.unit(2)(es).get)

  val smallInt = Gen.choose(-10,10)
  val maxProp = Prop.forAll(Gen.listOf(smallInt)) { l =>
    val max = l.max
    !l.exists(_ > max) // No value greater than `max` should exist in `l`
  }

}
/**
  * ideas:
  * - shrinking
  * - incremental size generation
  */
