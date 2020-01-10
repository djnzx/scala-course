package _twitter

object TWApp02 extends App {
  /**
    * math: f: A -> B
    *  all type information is removed at compile time.
    *  It is no longer needed. This is called erasure.
    */
  def toList[A](a: A) = List(a)
  // doesn't compile
//  def foo[A, B](f: A => List[A], b: B) = f(b)

  def id[T](x: T): T = x

  val v1: Boolean = id(true)
  val v2: Int = id(5)
  val v3: String = id("Hello")
  val v4: Array[Int] = id(Array(1,2,3,4))
  /**
    * invariant:     [T]  - C[T] and C[T’] are not related
    * covariant:     [+T] - C[T’] is a subclass of C[T]
    * contravariant: [-T] - C[T] is a subclass of C[T’]
    */
  class Covariant[+A]
  class Contravariant[-A]
  val cv1: Covariant[AnyRef] = new Covariant[String]
//  val cv2: Covariant[String] = new Covariant[AnyRef]
  val cv3: Contravariant[String] = new Contravariant[AnyRef]
//  val cv4: Contravariant[AnyRef] = new Contravariant[String]

  // Animal -> Bird -> Chicken
  class Animal { val sound = "rustle" }
  class Bird extends Animal { override val sound = "call" }
  class Chicken extends Bird { override val sound = "cluck" }


  /**
    * parameter - is contravariant: (we can supply  Bird, Animal)
    * result    - is covariant: (we can provide Bird, Chicken)
    */
  val getTweet: Bird => String = (a: Animal) => a.sound
  val hatch: () => Bird = () => new Chicken

  // won't compile
//  def cacophony[T](things: Seq[T]) = things map (_.sound)
  /**
    * Upper bounds:
    * T <: Animal
    * means any `subtype` of Animal
    */
  def biophony[T <: Animal](things: Seq[T]): Seq[String] = things map (_.sound)
  biophony(Seq(new Chicken, new Bird, new Animal))

  /**
    * lower bounds:
    * def :: [B >: A](elem: B): List[B]
    * B >: T. That specifies type B as a superclass of T
    */
  val birds = List(new Bird, new Bird)
  val lower1: Seq[Bird]   = new Chicken :: birds // Chicken + Bird's => Bird's
  val lower2: Seq[Animal] = new Animal :: birds  // Animal + Bird's => Animal

  // don't care
  def count1[A](l: List[A]) = l.size
  def count2(l: List[T forSome { type T }]) = l.size
  def count3(l: List[_]) = l.size
  def drop11(l: List[T forSome { type T }]) = l.tail
  def drop12(l: List[_]) = l.tail

  def hashcodes(l: Seq[_ <: AnyRef]) = l map (_.hashCode)
  // Error: the result type of an implicit conversion must be more specific than AnyRef
//  hashcodes(Seq(1,2,3))
}
