package fp_red.red08

import fp_red.c_answers.c05laziness.Stream
import fp_red.c_answers.c06state.RNG
import fp_red.red08.Prop._

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

  // this guy knows how generate random Stream[A]
  def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] =
    Stream.unfold(rng) { rng => Some(g.sample.run(rng)) }

  def buildMessage[A](s: A, e: Exception): String =
    s"test case: $s\n"+
      s"generated an exception: ${e.getMessage}\n"+
      s"stack trace:\n ${e.getStackTrace.mkString("\n")}"

  def apply(f: (CountToRun, RNG) => Result): Prop =
    Prop { (_,n,rng) => f(n,rng) }

  /**
    * (SGen[A], A => Boolean) => Prop
    */
  def forAll[A](g: SGen[A])(p: A => Boolean): Prop = forAll(g(_))(p)

  /**
    * (Int => Gen[A], A => Boolean) => Prop
    */
  def forAll[A](g: Int => Gen[A])(p: A => Boolean): Prop = Prop { (max, n, rng) =>
    // testcase size
    val casesPerSize = (n + (max - 1)) / max    // (n - 1) / max + 1
    // generate the Stream of Props to run
    val props: Stream[Prop] = Stream.from(0).take((n min max) + 1).map { i => forAll(g(i))(p) }
    val prop: Prop = props.map(p => Prop { (max, _, rng) =>
      p.run(max, casesPerSize, rng)
    }).toList.reduce { _ && _ }
    prop.run(max,n,rng)
  }

  /**
    * (Gen[A], A => Boolean) => Prop
    */
  def forAll[A](g: Gen[A])(p: A => Boolean): Prop = Prop { (n, rng) =>
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

  /**
    * check predicate
    */
  def check(p: => Boolean): Prop = Prop { (_,_,_) =>
    if (p) Passed else Falsified("()", 0)
  }

}
