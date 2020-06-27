package fp_red.red01

object NewTypes {
  // we can declare our type
  type ItoI = Int => Int
  // function declaration
  val inc: ItoI = (i: Int) => i + 1
  // composition
  val twice: ItoI => ItoI = (f: ItoI) => f compose f
  // ignoring input
  val unit7: ItoI = _ => 7
}

object DropWhile {
  def dropWhile[A](xs: List[A], f: A => Boolean): List[A] = xs match {
    case Nil => Nil
    case h::t => if (f(h)) dropWhile(t, f) else xs
  }
}

object TreeIdea {
  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
}

object OptionIdea {
  sealed trait Maybe[+A] {
    def map[B](f: A => B): Maybe[B]
    def flatMap[B](f: A => Maybe[B]): Maybe[B]
    def getOrElse[B >: A](default: => B): B
    def orElse[B >: A](value: Maybe[B]): Maybe[B]
    def filter(f: A => Boolean): Maybe[A]
  }

  // need to be implemented :)
  abstract case class Some[A](value: A) extends Maybe[A]
  trait None extends Maybe[Nothing]
}

object LiftApproach {
  /**
    * given f: A => B
    * how to lift it to Option[A] => Option[B]
    * high-order function
    */
  def lift[A, B](f: A => B): Option[A] => Option[B] =
    (oa: Option[A]) => oa map f
}

object OrIdea {
  sealed trait Or[+E, +A]
  case class Left[+E, Nothing](value: E) extends Or[E, Nothing]
  case class Right[Nothing, +A](value: A) extends Or[Nothing, A]
}

object Validation {
  case class Person(name: Name, age: Age)
  sealed case class Name(name: String)
  sealed case class Age(age: Int)

  def mkName(name: String): Either[String, Name] =
    if (name == null || name == "") Left("Name is empty")
    else Right(Name(name))

  def mkAge(age: Int): Either[String, Age] =
    if (age < 0) Left("Age < 0")
    else Right(Age(age))

  def mkPerson(name: String, age: Int): Either[String, Person] = for {
    name <- mkName(name)
    age  <- mkAge(age)
  } yield Person(name, age)
}

object Laziness {
  // eager
  List(1,2,3,4)
    .map(_ + 10)       // List(11,12,13,14)
    .filter(_ % 2 == 0) // List(12, 14)
    .map(_ * 3)        // List(36, 42)

  /**
    * if implementation lazy because of function
    */
  def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A =
    if (cond) onTrue() else onFalse()

  /**
    * if lazy because of by name syntax
    */
  def if3[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if (cond) onTrue else onFalse

  if2(1 > 2,
    () => println("a"),
    () => println("b")
  )
  
  if3(1 > 2,
    println("a"),
    println("b")
  )
  
  def lazyTwice(b: Boolean, i: => Int) =
    if (b) i + i // each call - new call !!!
    else 0
  
  def lazyCached(b: Boolean, i: => Int) = {
    lazy val cached = i
    if (b) cached + cached else 0
  }
}

object StreamsIdea {
  
  sealed trait Stream[+A] {
    def headOption: Option[A] = this match {
      case Empty      => None
      case Cons(h, _) => Some(h())
    }
  }
  
  case object Empty extends Stream[Nothing]
  // it requires functions because case classes should have final values
  case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

  // smart constructors in the companion object
  object Stream {
    def empty[A]: Stream[A] = Empty

    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
      lazy val head = hd
      lazy val tail = tl
      Cons(() => head, () => tail)
    }
    
    // convenient syntax to construct from Sequence
    def apply[A](as: A*): Stream[A] =
      if (as.isEmpty) empty
      else            cons(as.head, apply(as.tail: _*))
  }
}