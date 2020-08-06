package typelambda

import scala.concurrent.Future

/**
  * https://underscore.io/blog/posts/2016/12/05/type-lambdas.html
  * https://blog.adilakhter.com/2015/02/18/applying-scalas-type-lambda/
  */
object TypeLambda extends App {
  /** aliases */ 
  type L = List[Option[(Int, Double)]]
  type M[A] = Option[Map[Int, A]]
  val t: M[String] = Some(Map(1 -> "abc", 2 -> "xyz"))

  trait Functor[F[_]] {
    def as[A](a: A): F[A] = ???
  }
  
  type F1 = Functor[Option] // OK
  type F2 = Functor[List]   // OK
  // type F3 = Functor[Map]    // !!
  // Map takes two type parameters (Map[K,V]) while the type parameter to Functor expects one
  type IntKeyMap[A] = Map[Int, A]
  type F4 = Functor[IntKeyMap] // OK
  // type F5 = Functor[Map[Int, _]]
  // Map[Int, _] takes no type parameters, expected: one
  val cube: Double => Double = Math.pow(_: Double, 3)

  /**
    * when something expects F[_] (one type parameter),
    * for example:
    */
  def doSomething[F[_], A](data: F[A]): Vector[F[A]] = Vector(data)
  /**
    * I can do:
    */
  val x3: Vector[Some[Boolean]]    = doSomething(Some(true))
  val x4: Vector[List[Double]]     = doSomething(List(1.5))
  // ??? ??? ???
  val x1: Vector[Map[Int, String]] = doSomething(Map(1 -> "a"))
  val x2: Vector[Map[Int, Double]] = doSomething(Map(1 -> 1D))
  /** 
    * but we have G[_, _] with one fixed type, for example to Int => G[Int, _]
    * we have two ways to solve the problem:  
    * 
    * 1. type alias
    * trait G[_, _]
    * type G1[A] = G[Int, A]
    * and use it
    * 
    * 2 type lambda
    * 
    * 
    */
  // approach 1.
  // declare an alias
  type MI[A] = Map[Int, A]
  // use it
  type F61 = Functor[MI]
  // the only drawback is - MI[A] leaks outside

  // approach 2.
  type F62 = Functor[
    (
      { type f[A] = Map[Int, A] } // structural type definition
    )
      #f                          // accessing (projection)
  ]

  /** I can't */
  //  def foo[A[_, _], B](functor: Functor[ A[B, ?]                       ] ) = ???
  /** I can 1: */
  type A1[A] = Map[Int, A]    
  def foo1[A[_, _], B](functor: Functor[A1] ) = ???
  /** I can 2: it's used if I don't know the type #1 (Int) */
  def foo2[A[_, _], B](functor: Functor[ ({ type ab[C] = A[B, C] }) #ab] ) = ???
  
  val mf: Functor[({
    type f[x] = Map[Int, x]
  })#f] = new Functor[({type f[x] = Map[Int, x]})#f] {}
  
  /** I can't write */
//  def makeFunctor[A]: Functor[Map[Int, A]] = new Functor[Map[Int, A]]] {} 
  /** I need to */
  def makeFunctor[A] = new Functor[ ({ type f[x] = Map[A, x] })#f] {}
  
  /**
    * we set 1st type parameter here
    * and Functor has no information about it
    * Functor will operate with,
    * Map[Int, _]
    * as
    * F[_]
    */
  val fun1: Functor[({
    type f[x] = Map[Int, x]
  })#f] = makeFunctor[Int]
  /**
    * we set 2-nd type parameter here 
    */
  val z1: Map[Int, Int] = fun1.as(1)
  val z2: Map[Int, Boolean] = fun1.as(true)
  val z3: Map[Int, String] = fun1.as[String]("Hell")

  // I can erase type intentionally
  type MapK[K] = Map[K, _]
  val mapk1: MapK[Int] = Map(1->1)
  val val1: Option[_] = mapk1.get(1)
}
