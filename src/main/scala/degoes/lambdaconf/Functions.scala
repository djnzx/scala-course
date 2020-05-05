package degoes.lambdaconf

/**
  * https://gist.github.com/jdegoes/c0d923893ddb7039dcf44e8fe6012e0f
  */

import scala.util.Try

object Functions extends App {

  object totality {
    // problem 1. null instead of value
    //def f[A]: A = null

    // problem 2. exceptions instead of value
    def g[A]: A = throw new Error

    // problem 3. non determinism: value or exceptions
    def parseInt_bad(v: String): Int = v.toInt
    def parseInt(v: String): Option[Int] = Try(v.toInt).toOption

    // problem 4.1 OWN types instead of primitive types
    type FileHandle = String
    type FileError = String
    def openFile(name: String): Either[FileError, FileHandle] = ???
    // 4.2 or value types
    case class FileError2(err: String) extends AnyVal
    case class FileHandle2(handle: Long) extends AnyVal
    def openFile2(name: String): Either[FileError2, FileHandle2] = ???

    def identity[A](a: A): A = a
  }

  object determinism {
    // these variables always different
    val f = Math.random()
    val g = Math.random()
  }

  object purity {
    // returns unit, but do side-effects (read from console, write to console, etc)
    def println(line: String): Unit = ()
    println("foo")
    def readLine: String = ???
  }

  object higher_order {
    // this type describes function, which takes string
    // and returns either error, either tuple with the same string and result
    type Parser[E, A] = String => Either[E, (String, A)]

    // this function takes two parsers and return another parser
    def alt[E, A](l: Parser[E, A], r: Parser[E, A]): Parser[E, A] =
      // it tries to apply left parser. if it returns E(left), we just apply right parser.
      (i: String) => l(i).fold(_ => r(i), r => Right(r))

    val p: Parser[String, Int] = alt(
      s => if (s.isEmpty) Left("is empty") else Right((s, s.length)),
      s => Right((s"++$s++", s.length))
    )
    val r1: Either[String, (String, Int)] = p("")
    val r2: Either[String, (String, Int)] = p("abc")
    println(r1)
    println(r2)
  }

  object polymorphic {
    // functions can be polymorphic in their return types based on given types
    def square(x: Int): Int = x * x

    object identity {
      // [A] => ((a: A) => A)
      def apply[A](a: A): A = a
    }

    def myMethod[A, B, C](a: A, b: B, c: C) = ???
//    def myMethod[A: *, B: *, C: *](a: A, b: B, c: C) = ???

    val id1: Int = identity(1)     // identity.apply[Int](1)
    val id2: String = identity("foo") // identity.apply[String]("foo")
    identity[String]("foo")

    // second((1, 2)) // 2
    // second((true, "false")) // "false"
    object second {
      def apply[A, B](t: (A, B)): B = t._2
    }
  }

  object types {
    // product type - means combination (and)
    object products {
      type Point = (Int, Int)
      case class Person(name: String, age: Int)
      case class ServerLocation(host: String, port: Int)
      type Currency = String

      case class Amount1(total: BigDecimal, unit: Unit)
      case class Amount2(total: BigDecimal)

      def amount1ToAmount2(a: Amount1): Amount2 = Amount2(a.total)
      def amount2ToAmount1(a: Amount2): Amount1 = Amount1(a.total, ())
    }
    // sum type - means disjoint (or)
    object sums {
      type Option[A] = Either[Unit, A]

      sealed trait Currency
      object USD extends Currency
      object EUR extends Currency
      object UAH extends Currency

      def m1(c: Currency): String = c match {
        case UAH => "UAH"
        case USD => "USD"
        case EUR => "EUR"
        case _ => "other:not supported"
      }

      sealed trait JobTitle
      case class Engineer(level: Int) extends JobTitle

      final abstract class Zero {
        def absurd[A]: A
      }

      def simplifyL[A](e: Either[A, Zero]): A = e.fold(identity, z => z.absurd[A])

      // IO[E, A] - Fail with an E, or compute an A
      // IO[Throwable, A]
      // IO[Nothing, A]

      // List[Void]
      // Future[Void]

      case class Person(name: String, zero: Zero)

      // Traffic light
    }
    // data / knowledge representation
    object models {
      type Email = String

      val myEmail: Email = "ksljdf82938j9jfwdsifj892"

      sealed trait Site
      object Dungeon extends Site
      object Forest extends Site
      object University extends Site

      val Topology : List[(Site, Site)] =
        (Dungeon, University) :: (University, Forest) :: Nil

      case class Vitals(hp: Int, strength: Int, health: Int)
      case class Character(category: Category, name: String, vitals: Vitals)

      sealed trait Category
      object Warrior extends Category
      object Healer extends Category
      object Engineer extends Category

      sealed trait Vertical
      case object Northsouth extends Vertical
      case object Southnorth extends Vertical

      sealed trait Lateral
      case object Westeast extends Lateral
      case object Eastwest extends Lateral

      sealed trait Direction
      case class Left(vertical: Vertical)
      case class Right(vertical: Vertical)
      case class Both(both: (Vertical, Lateral))

      sealed trait Action
      case class Move(site: Site)
    }
    object kinds {
//      sealed trait List[+A]
//      case class Cons[+A](head: A, tail: List[A]) extends List[A]
//      case object Nil extends List[Nothing]
      val myList : List[Int] = ???
      // function from Int to Int
      val f: Int => Int = (v: Int) => v + 1
      f(1)
      case class Person[A, B](name: A, age: B)
      // List : * => *
      // List[Int]
      // f    : Int => Int
      // f(1)
      // g      : (Int, Int) => Int
      // Either : [*, *] => *

      def myMethod0[A, B, C](a: A, b: B, c: C) = ???
      def myMethod1[F[_], A](fa: F[A]): F[A] = ???
      def myMethod2[F[_, _], A, B](fab: F[A, B]): F[A, B] = ???

      // String
      // def method[A] -- *
      // method[String]
      // List
      // def method[A[_]] -- * => *
      // method[List]
      // Option
      // def method[X[_]] -- * => *
      // method[Option]
      // Either
      // def method[A[_, _]] -- [*, *] => *
      // method[Either]
      // Tuple3
      // def method[T[_, _, _]] -- [*, *, *] => *
      // method[Tuple3]

      // very generic implementation of stack
      trait StackModule[F[_]] {
        def empty[A]: F[A]                      // F[A] (empty)
        def push[A](a: A, s: F[A]): F[A]        // F[A] + a
        def pop[A](s: F[A]): Option[(A, F[A])]  // Option(a, F[A])
      }
      // StackModule : (* => *) => *
      // type T = StackModule[Future]

      val stack1: StackModule[List] = new StackModule[List] {
        override def empty[A]: List[A] = List()
        override def push[A](a: A, s: List[A]): List[A] = a +: s
        override def pop[A](s: List[A]): Option[(A, List[A])] = if (s.isEmpty) None else Some((s.head, s.tail))
      }

      val stack2 = new StackModule[List] {
        override def empty[A]: List[A] = Nil
        override def push[A](a: A, as: List[A]): List[A] = a :: as
        override def pop[A](s: List[A]): Option[(A, List[A])] = s match {
          case Nil => None
          case a :: s => Some((a, s))
        }
      }

      trait Envelope[A] {
        def get:A
        def put1(a: A): Envelope[A]
        def put2(a: A): A
      }

      trait Box[A]

      trait Ex1[A]

      val ex1: Ex1[Int] = new Ex1[Int] {}

      trait Ex2[F[_]] {
        def m1[A](a: A): F[A]
      }

      val ex20: Ex2[Ex1] = new Ex2[Ex1] {
        override def m1[A](a: A): Ex1[A] = ???
      }

      val ex21: Ex2[List] = new Ex2[List] {
        override def m1[A](a: A): List[A] = ???
      }

      val ex22: Ex2[Box] = new Ex2[Box] {
        override def m1[A](a: A): Box[A] = ???
      }

      trait Example1    // *
      trait Example2[A] // * => *
      trait Example3[F[_, _]] // ([*, *] => *) => *
      trait Example4[F[_[_, _], _], G[_[_[_]]], H]
      // trait Example4[X, Y, Z]
      // [[[*, *] => *, *] => *, ((* => *) => *) => *, *] => *
      // trait NaturalTransformation[F[_], G[_]]
      // (* => *, * => *) => *
      // Map -- F[_, _] -- [*, *] => *
      // * => *
      // [*, *] => *
      // [* => *, *] => *
      // ((* => *) => *) => *
      trait FourthExercise[_[_[_]]]
    }
    object partial_application {
      val add : (Int, Int) => Int = (a: Int, b: Int) => a + b

      val increment3: Int => Int = (a: Int) => add(1, a)
      val increment2             = (a: Int) => add(1, a)
      val increment1: Int => Int =  a       => add(1, a)
      val increment:  Int => Int =             add(1, _)

      (1 :: 2 :: Nil).map(increment)

      trait Sized1[F[_]] {
        def size[A](fa: F[A]): Int
      }
      trait Sized2[F[_, _]] {
        def size0      (fa: F[_, _]): Int
        def size1[A]   (fa: F[A, _]): Int
        def size2[A, B](fa: F[A, B]): Int
      }
//       ({type TupleA[B] = (A, B)})#TupleA
      val sizedMap: Sized2[Map] = new Sized2[Map] {
        override def size0      (fa: Map[_, _]): Int = fa.size
        override def size1[A]   (fa: Map[A, _]): Int = fa.size
        override def size2[A, B](fa: Map[A, B]): Int = fa.size
      }
      sizedMap.size0(Map(1->2, 3->3)) // 2
//      def SizedTuple2[A]: Sized[Tuple2[A, ?]] = new Sized[(A, ?)] {
//        def size[B](fb: (A, B)): Int = 1
//      }
      val SizedList: Sized1[List] = new Sized1[List] {
        def size[A](fa: List[A]): Int = fa.length
      }
    }
  }

  object type_classes {
    def repeat(n: Int, s: String): String =
      if (n <= 0) "" else s + repeat(n - 1, s)

    def foo(n: Int, s: String): String = ???

    def bar1[A, B](t: (A, B)): B = ???
    def bar2[A, B](a: A, e: Either[A, B]): A =
      e.fold(identity, _ => a)
    def bar3[A](a: A, f: A => A): A = ???
    def bar4[A, B, C](l: List[A], r: List[B], f: A => C, g: B => C, h: (A, B) => C): List[C] = ???

    import algebra._
    def repeat2[A: Monoid](n: Int, r: A): A =
      if (n <= 0) empty[A] else r <> repeat2(n - 1, r)

    object algebra {
      trait Semigroup[A] {
        // Associative Law:
        // append(a, append(b, c)) == append(append(a, b), c)
        def append(l: A, r: A): A
      }
      trait Monoid[A] extends Semigroup[A] {
        // Identity laws:
        // append(a, zero) == a
        // append(zero, a) == a
        def empty: A
      }
      // instance picker
      object Monoid {
        def apply[A](implicit M: Monoid[A]): Monoid[A] = M
      }
      implicit val MonoidString = new Monoid[String] {
        def empty: String = ""
        def append(l: String, r: String): String = l + r
      }
      val ms: Monoid[String] = Monoid[String]

      implicit def MonoidList[A] = new Monoid[List[A]] {
        def empty: List[A] = Nil
        def append(l: List[A], r: List[A]): List[A] = l ++ r
      }
      implicit def OptionMonoid[A: Semigroup] = new Monoid[Option[A]] {
        def empty: Option[A] = None
        def append(l: Option[A], r: Option[A]): Option[A] =
          (for {
            l <- l
            r <- r
          } yield l <> r).orElse(l).orElse(r) // or none...
      }
      implicit def MapSemigroup[K, V: Semigroup] = new Semigroup[Map[K, V]] {
        def append(l: Map[K, V], r: Map[K, V]): Map[K, V] =
          (l.toList ++ r.toList).foldLeft[Map[K, V]](Map()) {
            case (map, (k, v)) =>
              (map.get(k) <> Option(v)).fold(map)(map.updated(k, _))
          }
      }
      def empty[A: Monoid]: A = Monoid[A].empty
      object Semigroup {
        def apply[A](implicit S: Semigroup[A]): Semigroup[A] = S
      }
      implicit class SemigroupSyntax[A](val l: A) extends AnyVal {
        def <> (r: A)(implicit S: Semigroup[A]): A = S.append(l, r)
      }
      // operator `<>` taken from SemigroupSyntax
      // for any type A for which we have implicit
      // Semigroup instance: implicit val MonoidString = new Monoid[String]
      val s: String = empty[String] <> empty[String]
    }

    object ct {
      trait Functor[F[_]] {
        // Identity Law:
        //   fmap(fa, identity) == fa
        // Composition Law:
        //   fmap(fmap(fa, g), f) == fmap(fa, f.compose(g))
        def fmap[A, B](fa: F[A], f: A => B): F[B]
      }
      object Functor {
        def apply[F[_]](implicit F: Functor[F]): Functor[F] = F
        implicit val ListFunctor = new Functor[List] {
          def fmap[A, B](fa: List[A], f: A => B): List[B] = fa.map(f)
        }
        implicit val OptionFunctor = new Functor[Option] {
          def fmap[A, B](fa: Option[A], f: A => B): Option[B] = fa.map(f)
        }
//        implicit def FunctionFunctor[K] = new Functor[Function[K, ?]] {
//                     def fmap[A, B](fa: K => A, f: A => B): K => B = f.compose(fa)
//          override def fmap[A, B](fa: K => A, f: A => B): K => B = fa.andThen(f)
//        }
      }

      trait Contravariant[F[_]] {
        def contramap[A, B](fa: F[A], f: B => A): F[B]
      }
//      implicit def ContravariantFunction[K] =
//        new Contravariant[Function[?, K]] {
//          def contramap[A, B](fa: A => K, f: B => A): B => K =
//            fa.compose(f)
//        }
      trait Invariant[F[_]] {
        def xmap[A, B](fa: F[A], ab: A => B, ba: B => A): F[B]
      }

      type Json = String
      trait JsonCodec[A] {
        def encode(a: A): Json
        def decode(j: Json): A
      }

      trait Apply[F[_]] extends Functor[F] {
        def ap[A, B](ff: F[A => B], fa: F[A]): F[B]
        def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ap(fmap(fa, (a: A) => (b: B) => (a, b)), fb)
      }
      object Apply {
        def apply[F[_]](implicit F: Apply[F]): Apply[F] = F
//        def apply[F : Apply]: Apply[F] = implicitly[Apply[F]]
      }

      implicit val OptionApply = new Apply[Option] {
        def fmap[A, B](fa: Option[A], f: A => B): Option[B] = fa.map(f)

        def ap[A, B](ff: Option[A => B], fa: Option[A]): Option[B] = (ff, fa) match {
          case (Some(f), Some(a)) => Some(f(a))
          case _ => None
        }
      }
      implicit val ListApply = new Apply[List] {
        def fmap[A, B](fa: List[A], f: A => B): List[B] =
          fa.map(f)

        def ap[A, B](ff: List[A => B], fa: List[A]): List[B] =
          for {
            f <- ff
            a <- fa
          } yield f(a)
      }

      // we apply all the functions to all element of the list (Cartesian product)
      val l2: Seq[Int] = ListApply.ap(
        List(
          (x: Int) => x + 1,
          (x: Int) => x + 2,
      ),
        List(1,2,3)
      )
//      println(l2)

      case class Parser[E, A](run: String => Either[E, (String, A)])
      object Parser {
        def success[E, A](a: A): Parser[E, A] =
          Parser[E, A](i => Right((i, a)))
      }

//      implicit def ParserApply[E]: Apply[Parser] = new Apply[Parser[E, ?]] {
//        def ap[A, B](ff: Parser[E, A => B], fa: Parser[E, A]): Parser[E, B] =
//          Parser[E, B](i => ff.run(i) match {
//            case Left(e) => Left(e)
//            case Right((i, f)) =>
//              fa.run(i) match {
//                case Left(e) => Left(e)
//                case Right((i, a)) =>
//                  Right((i, f(a)))
//              }
//          })
//        def fmap[A, B](fa: Parser[E, A], f: A => B): Parser[E, B] =
//          Parser[E, B](i => fa.run(i) match {
//            case Left(e) => Left(e)
//            case Right((i, a)) => Right((i, f(a)))
//          })
//      }

      trait Applicative[F[_]] extends Apply[F] {
        def point[A](a: A): F[A]
      }
      object Applicative {
        def apply[F[_]](implicit F: Applicative[F]): Applicative[F] = F
      }
      trait Monad[F[_]] extends Applicative[F] {
        def bind[A, B](fa: F[A], afb: A => F[B]): F[B]
      }
      object Monad {
        def apply[F[_]](implicit F: Monad[F]): Monad[F] = F

        implicit val MonadOption = new Monad[Option] {
          def point[A](a: A): Option[A] = Some(a)

          def bind[A, B](fa: Option[A], afb: A => Option[B]): Option[B] =
            fa.fold[Option[B]](None)(afb)

          def fmap[A, B](fa: Option[A], f: A => B): Option[B] =
            fa.map(f)

          def ap[A, B](ff: Option[A => B], fa: Option[A]): Option[B] =
            Apply[Option].ap(ff, fa)
        }
//        implicit def MonadParser[E] = new Monad[Parser[E, ?]] {
//          def point[A](a: A): Parser[E, A] = Parser.success(a)
//          def bind[A, B](fa: Parser[E, A], f: A => Parser[E, B]): Parser[E, B] =
//            Parser[E, B](i => fa.run(i) match {
//              case Left(e) => Left(e)
//              case Right((i, a)) => f(a).run(i)
//            })
//          def ap[A, B](ff: Parser[E, A => B], fa: Parser[E, A]): Parser[E, B] =
//            Apply[Parser[E, ?]].ap(ff, fa)
//
//          def fmap[A, B](fa: Parser[E, A], f: A => B): Parser[E, B] =
//            Apply[Parser[E, ?]].fmap(fa, f)
//        }
      }
      implicit class MonadSyntax[F[_], A](fa: F[A]) {
        def map[B](f: A => B)(implicit F: Monad[F]): F[B] =
          F.fmap(fa, f)

        def flatMap[B](f: A => F[B])(implicit F: Monad[F]): F[B] =
          F.bind(fa, f)

        def zip[B](fb: F[B])(implicit F: Monad[F]): F[(A, B)] =
          F.zip(fa, fb)
      }
    }
  }

//  object effects {
//    import type_classes.ct._
//    final abstract class Void {
//      def absurd[A]: A
//    }
//    case class IO[E, A](unsafePerformIO: () => Either[E, A]) { self =>
//      def map[B](f: A => B): IO[E, B] =
//        IO[E, B](() => self.unsafePerformIO().map(f))
//      def flatMap[B](f: A => IO[E, B]): IO[E, B] =
//        IO[E, B](() => self.unsafePerformIO() match {
//          case Left(e) => Left(e)
//          case Right(a) => f(a).unsafePerformIO()
//        })
//      def attempt: IO[Void, Either[E, A]] =
//        IO[Void, Either[E, A]](() => Right(self.unsafePerformIO()))
//    }
//    object IO {
//      def point[E, A](a: A): IO[E, A] = IO[E, A](() => Right(a))
//      def fail[E, A](e: E): IO[E, A] = IO[E, A](() => Left(e))
//
//      implicit def MonadIO[E]: Monad[IO[E, ?]] = new Monad[IO[E, ?]] {
//        def point[A](a: A): IO[E, A] = IO.point(a)
//        def fmap[A, B](fa: IO[E, A], f: A => B): IO[E, B] = fa.map(f)
//        def ap[A, B](ff: IO[E, A => B], fa: IO[E, A]): IO[E, B] =
//          for {
//            f <- ff
//            a <- fa
//          } yield f(a)
//        def bind[A, B](fa: IO[E, A], f: A => IO[E, B]): IO[E, B] =
//          fa.flatMap(f)
//      }
//    }
//
//    object console2 {
//      def putStrLn[F[_]](line: String)(implicit F: Console[F]): F[Unit] =
//        F.putStrLn(line)
//      def getStrLn[F[_]](implicit F: Console[F]): F[String] =
//        F.getStrLn
//    }
//    trait Console[F[_]] {
//      def putStrLn(line: String): F[Unit]
//      val getStrLn: F[String]
//    }
//    object Console {
//      implicit val ConsoleIO: Console[IO[Void, ?]] = new Console[IO[Void, ?]] {
//        def putStrLn(line: String): IO[Void, Unit] =
//          IO(() => Right(println(line)))
//        val getStrLn: IO[Void, String] =
//          IO(() => Right(scala.io.StdIn.readLine()))
//      }
//    }
//    trait Random[F[_]] {
//      def nextInt(bound: Int): F[Int]
//    }
//    object Random {
//      implicit val RandomIO: Random[IO[Void, ?]] = new Random[IO[Void, ?]] {
//        def nextInt(bound: Int): IO[Void, Int] =
//          IO(() => Right(scala.util.Random.nextInt(bound)))
//      }
//    }
//    object rand {
//      def nextInt[F[_]](bound: Int)(implicit F: Random[F]): F[Int] =
//        F.nextInt(bound)
//    }
//  }


  //object app {
  //  import type_classes.ct._
  //  import effects._
  //  import console2._
  //  import rand._
  //
  //  sealed trait Selection
  //  case object PlayGame extends Selection
  //  case object Exit     extends Selection
  //
  //  def parseSelection(input: String): Option[Selection] =
  //    input.trim.toLowerCase match {
  //      case "1" => Some(PlayGame)
  //      case "2" => Some(Exit)
  //      case _ => None
  //    }
  //
  //  def invalidChoice[F[_]: Console]: F[Unit] =
  //    putStrLn("Your choice was invalid.")
  //
  //  def chooseWord[F[_]: Functor: Random]: F[String] = {
  //    val list = ("monad" :: "functor" :: "israel" :: "jalapeno" :: Nil)
  //
  //    Functor[F].fmap(nextInt[F](list.length), list)
  //  }
  //
  //  def printGame[F[_]: Monad: Console](state: GameState): F[Unit] = {
  //    val word =
  //      state.word.toList.map(char =>
  //        if (state.guessed.contains(char)) char else '_').mkString(" ")
  //
  //    for {
  //      _ <- putStrLn("==================")
  //      _ <- putStrLn(word)
  //      _ <- putStrLn("You've guessed: " + state.guessed.mkString(", "))
  //    } yield ()
  //  }
  //
  //  case class GameState(guessed: Set[Char], word: String)
  //
  //  def parseLetter(l: String): Option[Char] = l.trim.toLowerCase.toList match {
  //    case x :: Nil => Some(x)
  //    case _ => None
  //  }
  //
  //  def updateState[F[_]: Monad: Console](state: GameState, c: Char): F[Option[GameState]] = {
  //    val state2 = state.copy(guessed = state.guessed + c)
  //
  //    for {
  //      _ <- printGame(state2)
  //      o <- if ((state2.word.toSet -- state2.guessed).isEmpty)
  //        putStrLn("Congratulations, you won!!!").map(_ => None)
  //      else if (state2.guessed.size > state2.word.length * 2)
  //        putStrLn("It's not your day. You were hanged.").map(_ => None)
  //      else putStrLn("You made a guess.").map(_ => Some(state2))
  //    } yield o
  //  }
  //
  //  def playGameLoop[F[_]: Monad: Console: Random](state: GameState): F[Unit] =
  //    for {
  //      _ <- printGame[F](state)
  //      l <- getStrLn[F]
  //      o <- parseLetter(l) match {
  //        case None    => putStrLn("Enter one letter.").map(_ => Some(state))
  //        case Some(c) => updateState[F](state, c)
  //      }
  //      _ <- o.fold(putStrLn("Good game!"))(playGameLoop[F])
  //    } yield ()
  //
  //  def playGame[F[_]: Monad: Console: Random]: F[Unit] =
  //    chooseWord[F].flatMap(word => playGameLoop[F](GameState(Set(), word)))
  //
  //  def printMenu[F[_]: Console]: F[Unit] =
  //    putStrLn("""
  //Please enter your selection:
  //  1. Play a new game
  //  2. Exit the game""")
  //
  //  def mainLoop[F[_]: Monad: Console: Random](name: String): F[Unit] =
  //    for {
  //      _       <- printMenu
  //      choice  <- getStrLn
  //      _       <- parseSelection(choice).fold(invalidChoice.flatMap(_ => mainLoop(name))) {
  //        case Exit     => putStrLn("Goodbye, " + name + "!")
  //        case PlayGame => playGame.flatMap(_ => mainLoop(name))
  //      }
  //    } yield ()
  //
  //  def run[F[_]: Monad: Console: Random]: F[Unit] =
  //    for {
  //      _     <- putStrLn("What is your name?")
  //      name  <- getStrLn
  //      _     <- putStrLn("Hello, " + name + ", welcome to the game!")
  //      _     <- mainLoop(name)
  //    } yield ()
  //
  //  case class TestState(
  //                        input:  List[String],
  //                        output: List[String],
  //                        randos: List[Int]) {
  //    def showResults: String = output.reverse.mkString("\n")
  //  }
  //
  //  case class TestIO[A](run: TestState => (TestState, A)) { self =>
  //    def map[B](f: A => B): TestIO[B] =
  //      TestIO[B](s => {
  //        val (s2, a) = self.run(s)
  //
  //        (s2, f(a))
  //      })
  //    def flatMap[B](f: A => TestIO[B]): TestIO[B] =
  //      TestIO[B](s => {
  //        val (s2, a) = self.run(s)
  //
  //        f(a).run(s2)
  //      })
  //  }
  //  object TestIO {
  //    def point[A](a: A): TestIO[A] =
  //      TestIO[A](s => (s, a))
  //    implicit val MonadTestIO = new Monad[TestIO] {
  //      def point[A](a: A): TestIO[A] = TestIO.point(a)
  //      def fmap[A, B](fa: TestIO[A], f: A => B): TestIO[B] = fa.map(f)
  //      def ap[A, B](ff: TestIO[A => B], fa: TestIO[A]): TestIO[B] =
  //        for {
  //          f <- ff
  //          a <- fa
  //        } yield f(a)
  //      def bind[A, B](fa: TestIO[A], f: A => TestIO[B]): TestIO[B] =
  //        fa.flatMap(f)
  //    }
  //    implicit val ConsoleTestIO = new Console[TestIO] {
  //      def putStrLn(line: String): TestIO[Unit] =
  //        TestIO(s => (s.copy(output = line :: s.output), ()))
  //      val getStrLn: TestIO[String] =
  //        TestIO(s => (s.copy(input = s.input.drop(1)), s.input.head))
  //    }
  //    implicit val RandomTestIO = new Random[TestIO] {
  //      def nextInt(bound: Int): TestIO[Int] =
  //        TestIO(s => (s.copy(randos = s.randos.drop(1)), s.randos.head))
  //    }
  //  }
  //
  //  val MockRun: TestState =
  //    TestState(
  //      output = Nil,
  //      input  = List("John", "1", "m", "o", "n", "a", "d", "2"),
  //      randos = List(0)
  //    )
  //
  //  def runIO: IO[Void, Unit] = run[IO[Void, ?]]
  //  def runTest: TestIO[Unit] = run[TestIO]
}
