package fp_red.red11

// parser
import fp_red.red09.p0trait.Parsers
// testing
import fp_red.red08._
// concurrency
import fp_red.red07._
import fp_red.red07.Par._
// state
import fp_red.red06._
// streams
import fp_red.red05._

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  // unzip
  def distribute[A,B](fab: F[(A, B)]): (F[A], F[B]) =
    (map(fab)(_._1), map(fab)(_._2)) 
  
  def codistribute[A, B](e: Either[F[A], F[B]]): F[Either[A, B]] = e match {
    case Left(fa) => map(fa)(Left(_))
    case Right(fb) => map(fb)(Right(_))
  }
}

// TODO: !
object FunctorLaws {

  def identityLaw[F[_]: Functor, A](fa: F[A])(in: Gen[A]): Prop =
    Prop.forAll(in) { a: A =>
//      fa.map(fa)(identity) == fa
      a == a
    }
  
}

object Functor {
  
  val listFunctor = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B) = fa.map(f)
  }
  
}

/**
  * F can be Option, Either, Par, Parser, Gen, ...
  */
trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def map[A, B](fa: F[A])(f: A => B): F[B] = 
    flatMap(fa) { a => unit(f(a)) }
  
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    flatMap(fa) { a => map(fb) { b => f(a, b) } }
  
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    map2(fa, fb) { (_, _) }
  
  def product_via_flatMap[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    flatMap(fa) { a => map(fb) { b => (a, b) } }

  def sequence[A](la: List[F[A]]): F[List[A]] =
    la.foldRight(unit(List.empty[A])) { (fa: F[A], fla: F[List[A]]) => map2(fa, fla) { _ :: _ } }
  
  def traverse[A,B](la: List[A])(f: A => F[B]): F[List[B]] =
    la.foldRight(unit(List.empty[B])) { (a: A, flb: F[List[B]]) => map2(f(a), flb) { _ :: _ } }

  // The general meaning of `replicateM` is described very well by the
  // implementation `sequence(List.fill(n)(ma))`. It repeats the `ma` monadic value
  // `n` times and gathers the results in a single value, where the monad `M`
  // determines how values are actually combined.
  def replicateM[A](n: Int, ma: F[A]): F[List[A]] =
    sequence(List.fill(n)(ma))

  // Recursive version:
  def _replicateM[A](n: Int, ma: F[A]): F[List[A]] =
    if (n <= 0) unit(List[A]()) else map2(ma, _replicateM(n - 1, ma))(_ :: _)

  def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
    ms match {
      case Nil => unit(Nil)
      case h::t => flatMap(f(h)) { b: Boolean =>
        if (!b)  filterM(t)(f) // F[List[A]]
        else map(filterM(t)(f)) { la: List[A] => h :: la }
      } 
    }

  def filterM2[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
    ms.foldRight(unit(List.empty[A])) { (x, y) =>
      compose(
        f,
        (b: Boolean) => if (b) map2(unit(x), y)(_ :: _) else y
      )(x)
    }

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(g)

  /**
    * flatMat via compose
    * 
    * If I need to pass something, 
    * but I have nothing to pass,
    * I can pass Unit
    */
  def flatMap_compose[A,B](ma: F[A])(f: A => F[B]): F[B] = {
    val nothing: Unit = ()
    val composed = compose(
      (_: Unit) => ma,
      f
    )
    composed(nothing)
  }

  // flatten
  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(identity)
  def flatten[A](mma: F[F[A]]): F[A] = join(mma)

  /**
    * flatMat via flatten
    */
  def flatMap_join[A,B] (a: F[A])(f: A => F[B]) =
    join(map(a)(f))

  /**
    * compose via flatten
    */
  def compose_via_join[A,B,C] (f: A => F[B], g: B => F[C]): A => F[C] =
    a => join(map(f(a))(g))
  
}

case class Reader[R, A](run: R => A)

case class Id[A](value: A)

object Monad {
  /**
    * we implemented unit and flatMap
    * and we have 
    */
  val genMonad = new Monad[Gen] {
    override def unit[A](a: => A) = Gen.unit(a)
    override def flatMap[A, B](fa: Gen[A])(f: A => Gen[B]) = fa flatMap f
  }

  val parMonad = new Monad[Par] {
    def unit[A](a: => A) = Par. unit(a)
    def flatMap[A,B](ma: Par[A])(f: A => Par[B]) = Par.flatMap(ma)(f)
  }
  
  def parserMonad[P[+_]](p: Parsers[P]) = new Monad[P] {
    def unit[A](a: => A) = p.succeed(a)
    def flatMap[A,B](ma: P[A])(f: A => P[B]) = p.flatMap(ma)(f)
  }
  
  val optionMonad = new Monad[Option] {
    def unit[A](a: => A) = Some(a)
    def flatMap[A,B](ma: Option[A])(f: A => Option[B]) = ma flatMap f
  }
  
  val streamMonad = new Monad[Stream] {
    def unit[A](a: => A) = Stream(a)
    def flatMap[A,B](ma: Stream[A])(f: A => Stream[B]) = ma flatMap f
  }
  
  val listMonad = new Monad[List] {
    def unit[A](a: => A) = List(a)
    def flatMap[A,B](ma: List[A])(f: A => List[B]) = ma flatMap f
  }

  /**
    * Identity monad
    */
  val idMonad: Monad[Id] = new Monad[Id] {
    override def unit[A](a: => A): Id[A] = Id(a)
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa.value)
  }

  implicit class MonadSyntax[A](ia: Id[A]) {
    def map[B](f: A => B): Id[B] = idMonad.map(ia)(f)
    def flatMap[B](f: A => Id[B]): Id[B] = idMonad.flatMap(ia)(f)
  }

  val idx1: Id[String] =
    Id("Hello, ") flatMap (a =>
      Id("monad!") flatMap (b =>
        Id(a + b)))

  val idx2: Id[String] = for {
    a <- Id("Hello, ")
    b <- Id("monad!")
  } yield a + b

  type IntState[A] = State[Int, A]

  object IntStateMonad extends Monad[IntState] {
    override def unit[A](a: => A): IntState[A] = State.unit(a)
    override def flatMap[A, B](fa: IntState[A])(f: A => IntState[B]): IntState[B] = fa flatMap f
  }

  /**
    * https://scala-lang.org/files/archive/spec/2.13/03-types.html
    */
  object IntStateMonad2 extends Monad[
    (
      { type IS[A] = State[Int, A] }
      )#IS // anonymous type
  ] {
    override def unit[A](a: => A): State[Int, A] = State.unit(a)
    override def flatMap[A, B](fa: State[Int, A])(f: A => State[Int, B]): State[Int, B] = fa flatMap f
  }
  
  /**                                                               type lambda                 */
  def stateMonad[S] = new Monad[({ type t[x] = State[S, x]})#t] {
    override def unit[A](a: => A): State[S, A] = State.unit(a)
    override def flatMap[A, B](fa: State[S, A])(f: A => State[S, B]): State[S, B] = fa flatMap f
  }

  val F = stateMonad[Int]

  def zipWithIndex[A](as: List[A]): List[(Int, A)] = {
    val empty: State[Int, List[(Int, A)]] = F.unit(List.empty[(Int, A)])
    as.foldLeft(empty) { (acc, a) =>
      for {
        xs <- acc
        n  <- State.get        // last number
        _  <- State.set(n + 1) // new number
      } yield (n, a) :: xs
    }
      .run(0)
      ._1
      .reverse
  }

  // The action of Reader's `flatMap` is to pass the `r` argument along to both the
  // outer Reader and also to the result of `f`, the inner Reader. Similar to how
  // `State` passes along a state, except that in `Reader` the "state" is read-only.

  // The meaning of `sequence` here is that if you have a list of functions, you can
  // turn it into a function that takes one argument and passes it to all the functions
  // in the list, returning a list of the results.

  // The meaning of `join` is simply to pass the same value as both arguments to a
  // binary function.

  // The meaning of `replicateM` is to apply the same function a number of times to
  // the same argument, returning a list of the results. Note that if this function
  // is _pure_, (which it should be), this can be exploited by only applying the
  // function once and replicating the result instead of calling the function many times.
  // This means the Reader monad can override replicateM to provide a very efficient
  // implementation.
  def readerMonad[R] = new Monad[({type f[x] = Reader[R, x]})#f] {
    override def unit[A](a: => A): Reader[R, A] = Reader { _ => a }
    override def flatMap[A, B](fa: Reader[R, A])(f: A => Reader[R, B]): Reader[R, B] = Reader { r: R =>
      val a: A = fa.run(r)
      f(a).run(r)
    }
  }

}

object Reader {
  def ask[R]: Reader[R, R] = Reader(r => r)
}


object MonadExperiments {
  case class Order(item: Item, quantity: Int)
  case class Item(name: String, price: Double)

  val genOrder: Gen[Order] = for {
    name     <- Gen.stringN(3)
    price    <- Gen.uniform.map(_ * 10)
    quantity <- Gen.choose(1,100)
  } yield Order(Item(name, price), quantity)

  //or

  val genItem: Gen[Item] = for {
    name  <- Gen.stringN(3)
    price <- Gen.uniform.map(_ * 10)
  } yield Item(name, price)

  val genOrder2: Gen[Order] = for {
    item     <- genItem
    quantity <- Gen.choose(1,100)
  } yield Order(item, quantity)
  
}

/**
  * Monad Laws:
  * associativity:
  * x.flatMap(f).flatMap(g) == x.flatMap(a => f(a).flatMap(g))
  * compose(compose(f, g), h) == compose(f, compose(g, h))
  *
  * left and right identity:
  * compose(f, unit) == f
  * compose(unit, f) == f
  * or:
  * flatMap(x)(unit) == x
  * flatMap(unit(y))(f) == f(y)
  *
  * so, three minimal sets to implement:
  * unit + flatMap
  * unit + compose
  * unit + map + join
  *
  * So - monad - is a creature which is defined by its operations and laws
  * 
  * TODO: !
  */
object MonadLaws {
  
}