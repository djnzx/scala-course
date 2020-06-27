package fp_red.red03

import scala.annotation.tailrec

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  // sum recursive
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x,xs) => x + sum(xs)
  }
  // sum tail-recursive
  def sumTR(ints: List[Int], acc: Int = 0): Int = ints match {
    case Nil => acc
    case Cons(x, xs) => sumTR(xs, acc + x)
  }

  def productR(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => x * productR(xs)
  }
  
  def product(ds: List[Double]): Double = ds match {
    case Nil => throw new RuntimeException
    case _ => productR(ds)
  }

  def productTR(ds: List[Double], acc: Double): Double = ds match {
    case Nil => acc
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => productTR(xs, acc*x)
  }
  
  def productTR(ds: List[Double]): Double = ds match {
    case Nil => throw new RuntimeException
    case _ => productTR(ds, 1)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] = a1 match {
    case Nil => a2
    case Cons(h,t) => Cons(h, append(t, a2))
  }

  /**
    * right to left
    * by building function
    * f(a, f(a, f(a, ...f(a, z)))
    */
  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B = as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

  /**
    * sum via fold
    */
  def sum_fold(ns: List[Int]) = foldRight(ns, 0)(_ + _)

  /**
    * product via fold
    */
  def product_fold(ns: List[Double]) = foldRight(ns, 1.0)(_ * _)

  def tail[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(_, t) => t
  }
  
  def setHead[A](l: List[A], h: A): List[A] = l match {
    case Nil => Cons(h, Nil)
    case Cons(_, t) => Cons(h, t)
  }

  def drop[A](l: List[A], n: Int): List[A] = (l, n) match {
    case (_         , 0) => l
    case (Nil,        _) => Nil
    case (Cons(_, t), _) => drop(t, n-1)
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(h, t) if f(h) => dropWhile(t, f)
    case _ => l
  }

  // dropLast
  def init[A](l: List[A]): List[A] = l match {
    case Nil => sys.error("drop last on empty list")
    case Cons(_, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
  }

  def length[A](l: List[A], acc: Int = 0): Int = l match {
    case Nil => acc
    case Cons(_, t) => length(t, acc + 1)
  }

  @tailrec
  def foldLeft[A,B](l: List[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, f(z, h))(f)
  } 

  def reverse[A](l: List[A], acc: List[A] = Nil): List[A] = l match {
    case Nil => acc
    case Cons(head, tail) => reverse(tail, Cons(head, acc))
  } 

  def map[A,B](l: List[A])(f: A => B): List[B] = {
    
    def go(ta: List[A], acc: List[B]): List[B] = ta match {
      case Nil => List.reverse(acc)
      case Cons(h, t) => go(t, Cons(f(h), acc))
    }
    
    go(l, Nil)
  }
  
  def length_foldRight[A](l: List[A]): Int = foldRight(l, 0) { (_, acc) => acc + 1 }
  def length_foldLeft [A](l: List[A]): Int = foldLeft (l, 0) { (acc, _) => acc + 1 }
  def sum_foldLeft(l: List[Int]) = foldLeft(l, 0)(_ + _)
  def product_foldLeft(l: List[Double]) = foldLeft(l, 1.0)(_ * _)
  def reverse_foldLeft[A](l: List[A]): List[A] = foldLeft(l, List[A]()) { (as, a) => Cons(a, as)}
  def foldRightViFoldLeft[A, B](l: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(reverse(l), z) { (b, a) => f(a, b)}
  def foldRightViFoldLeft2[A, B](l: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(l, (b: B) => b) { (bb: B => B, a: A) => 
      b: B => bb(f(a, b))
    } (z)
  def foldLeftViFoldRight[A, B](l: List[A], z: B)(f: (B, A) => B): B =
    foldRight(l, (b: B) => b) { (a: A, bb: B => B) => 
      b: B => bb(f(b, a))
    } (z)
  def appendViaFoldRight[A](l: List[A], r: List[A]): List[A] =
    foldRight(l, r)(Cons(_,_))

  /*
Since `append` takes time proportional to its first argument, and this first argument never grows because of the
right-associativity of `foldRight`, this function is linear in the total length of all lists. You may want to try
tracing the execution of the implementation on paper to convince yourself that this works.

Note that we're simply referencing the `append` function, without writing something like `(x,y) => append(x,y)`
or `append(_,_)`. In Scala there is a rather arbitrary distinction between functions defined as _methods_, which are
introduced with the `def` keyword, and function values, which are the first-class objects we can pass to other
functions, put in collections, and so on. This is a case where Scala lets us pretend the distinction doesn't exist.
In other cases, you'll be forced to write `append _` (to convert a `def` to a function value)
or even `(x: List[A], y: List[A]) => append(x,y)` if the function is polymorphic and the type arguments aren't known.
*/
  def concat[A](l: List[List[A]]): List[A] =
    foldRight(l, Nil:List[A])(append)

  def add1(l: List[Int]): List[Int] =
    foldRight(l, Nil:List[Int])((h,t) => Cons(h+1,t))

  def doubleToString(l: List[Double]): List[String] =
    foldRight(l, Nil:List[String])((h,t) => Cons(h.toString,t))

  /*
  A natural solution is using `foldRight`, but our implementation of `foldRight` is not stack-safe. We can
  use `foldRightViaFoldLeft` to avoid the stack overflow (variation 1), but more commonly, with our current
  implementation of `List`, `map` will just be implemented using local mutation (variation 2). Again, note that the
  mutation isn't observable outside the function, since we're only mutating a buffer that we've allocated.
  */
  def map_2[A,B](l: List[A])(f: A => B): List[B] =
    foldRight(l, Nil:List[B])((h,t) => Cons(f(h),t))

  def map_3[A,B](l: List[A])(f: A => B): List[B] =
    foldRightViFoldLeft(l, Nil:List[B])((h, t) => Cons(f(h),t))

  def map_4[A,B](l: List[A])(f: A => B): List[B] = {
    val buf = new collection.mutable.ListBuffer[B]
    def go(l: List[A]): Unit = l match {
      case Nil => ()
      case Cons(h,t) => buf += f(h); go(t)
    }
    go(l)
    List(buf.toList: _*) // converting from the standard Scala list to the list we've defined here
  }

  /*
  The discussion about `map` also applies here.
  */
  def filter[A](l: List[A])(f: A => Boolean): List[A] =
    foldRight(l, Nil:List[A])((h,t) => if (f(h)) Cons(h,t) else t)

  def filter_1[A](l: List[A])(f: A => Boolean): List[A] =
    foldRightViFoldLeft(l, Nil:List[A])((h,t) => if (f(h)) Cons(h,t) else t)

  def filter_2[A](l: List[A])(f: A => Boolean): List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    def go(l: List[A]): Unit = l match {
      case Nil => ()
      case Cons(h,t) => if (f(h)) buf += h; go(t)
    }
    go(l)
    List(buf.toList: _*) // converting from the standard Scala list to the list we've defined here
  }

  /*
  This could also be implemented directly using `foldRight`.
  */
  def flatMap[A,B](l: List[A])(f: A => List[B]): List[B] =
    concat(map(l)(f))

  def filterViaFlatMap[A](l: List[A])(f: A => Boolean): List[A] =
    flatMap(l)(a => if (f(a)) List(a) else Nil)

  /*
  To match on multiple values, we can put the values into a pair and match on the pair, as shown next, and the same
  syntax extends to matching on N values (see sidebar "Pairs and tuples in Scala" for more about pair and tuple
  objects). You can also (somewhat less conveniently, but a bit more efficiently) nest pattern matches: on the
  right hand side of the `=>`, simply begin another `match` expression. The inner `match` will have access to all the
  variables introduced in the outer `match`.

  The discussion about stack usage from the explanation of `map` also applies here.
  */
  def addPairwise(a: List[Int], b: List[Int]): List[Int] = (a,b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(h1,t1), Cons(h2,t2)) => Cons(h1+h2, addPairwise(t1,t2))
  }

  /*
  This function is usually called `zipWith`. The discussion about stack usage from the explanation of `map` also
  applies here. By putting the `f` in the second argument list, Scala can infer its type from the previous argument list.
  */
  def zipWith[A,B,C](a: List[A], b: List[B])(f: (A,B) => C): List[C] = (a,b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(h1,t1), Cons(h2,t2)) => Cons(f(h1,h2), zipWith(t1,t2)(f))
  }

  /*
  There's nothing particularly bad about this implementation,
  except that it's somewhat monolithic and easy to get wrong.
  Where possible, we prefer to assemble functions like this using
  combinations of other functions. It makes the code more obviously
  correct and easier to read and understand. Notice that in this
  implementation we need special purpose logic to break out of our
  loops early. In Chapter 5 we'll discuss ways of composing functions
  like this from simpler components, without giving up the efficiency
  of having the resulting functions work in one pass over the data.
  
  It's good to specify some properties about these functions.
  For example, do you expect these expressions to be true?
  
  (xs append ys) startsWith xs
  xs startsWith Nil
  (xs append ys append zs) hasSubsequence ys
  xs hasSubsequence Nil

  */
  @annotation.tailrec
  def startsWith[A](l: List[A], prefix: List[A]): Boolean = (l,prefix) match {
    case (_,Nil) => true
    case (Cons(h,t),Cons(h2,t2)) if h == h2 => startsWith(t, t2)
    case _ => false
  }
  @annotation.tailrec
  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = sup match {
    case Nil => sub == Nil
    case _ if startsWith(sup, sub) => true
    case Cons(h,t) => hasSubsequence(t, sub)
  }



}
