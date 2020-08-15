package fp_red.red14

object Mutable {
  def quicksort(xs: List[Int]): List[Int] = if (xs.isEmpty) xs else {
    
    val arr = xs.toArray
    
    def swap(x: Int, y: Int) = {
      val tmp = arr(x)
      arr(x) = arr(y)
      arr(y) = tmp
    }
    
    def partition(l: Int, r: Int, pivot: Int) = {
      val pivotVal = arr(pivot)
      swap(pivot, r)
      var j = l
      for (i <- l until r) if (arr(i) < pivotVal) {
        swap(i, j)
        j += 1
      }
      swap(j, r)
      j
    }
    
    def qs(l: Int, r: Int): Unit = if (l < r) {
      val pi = partition(l, r, l + (r - l) / 2)
      qs(l, pi - 1)
      qs(pi + 1, r)
    }
    
    qs(0, arr.length - 1)
    arr.toList
  }
}

/**
  * actually this is just a State[S, A]
  * the only difference `run` is protected 
  */
sealed trait ST[S, A] { self =>
  /**
    * because an S represents the ability to mutate state,
    * and we don’t want the mutation to escape
    */
  protected def run(s: S): (A, S)
  
  def map[B](f: A => B): ST[S, B] = new ST[S, B] {
    def run(s: S) = {
      /** run encapsulated `run` */
      val (a, s2) = self.run(s)
      /** apply given function */
      val b: B = f(a)
      (b, s2)
    }
  }

  def flatMap[B](f: A => ST[S,B]): ST[S,B] = new ST[S,B] {
    def run(s: S) = {
      /** run encapsulated `run` */
      val (a, s2) = self.run(s)
      /** apply given function */
      val b: ST[S, B] = f(a)
      b.run(s2)
    }
  }
  
}

object ST {
  /** unit, lift */ 
  def apply[S, A](a: => A): ST[S, A] = {
    lazy val memo = a
    
    new ST[S, A] {
      def run(s: S) = (memo, s)
    }
  }
  
  // to run this runnable -> type S from RunnableST.apply should be Nothing.
  def runST[A](rst: RunnableST[A]): A = {
    val st: ST[Null, A] = rst[Null]
    val as: (A, Null) = st.run(null)
    as._1
  }
    
}

sealed trait STRef[S, A] {
  
  protected var cell: A
  
  def read: ST[S, A] = ST(cell)
  
  def write(a: => A): ST[S,Unit] = new ST[S,Unit] {
    def run(s: S) = {
      cell = a
      ((), s)
    }
  }
  
}

object STRef {
  def apply[S, A](a: A): ST[S, STRef[S,A]] = {
    
    val ref = new STRef[S, A] {
      override var cell: A = a
    }
    
    ST(ref)
  }

}

trait RunnableST[A] {
  def apply[S]: ST[S,A]
}

/** Scala requires an implicit Manifest for constructing arrays. */
sealed abstract class STArray[S,A](implicit manifest: Manifest[A]) {
  
  protected def value: Array[A]
  
  def size: ST[S,Int] = ST(value.length)

  // Write a value at the give index of the array
  def write(i: Int, a: A): ST[S,Unit] = new ST[S,Unit] {
    def run(s: S) = {
      value(i) = a
      ((), s)
    }
  }

  // Read the value at the given index of the array
  def read(i: Int): ST[S,A] = ST(value(i))

  // Turn the array into an immutable list
  def freeze: ST[S,List[A]] = ST(value.toList)

  def fill(xs: Map[Int,A]): ST[S,Unit] = ???

  def swap(i: Int, j: Int): ST[S,Unit] = for {
    x <- read(i)
    y <- read(j)
    _ <- write(i, y)
    _ <- write(j, x)
  } yield ()
}

object STArray {
  // Construct an array of the given size filled with the value v
  def apply[S,A:Manifest](sz: Int, v: A): ST[S, STArray[S,A]] =
    ST(new STArray[S,A] {
      lazy val value = Array.fill(sz)(v)
    })

  def fromList[S,A:Manifest](xs: List[A]): ST[S, STArray[S,A]] =
    ST(new STArray[S,A] {
      lazy val value = xs.toArray
    })
}

object Immutable {
  def noop[S] = ST[S, Unit](())

  def partition[S](a: STArray[S, Int], l: Int, r: Int, pivot: Int): ST[S, Int] =
    for {
      pv <- a.read(pivot)
      _  <- a.swap(pivot, r)
      j  <- STRef(l)
      _  <- (l until r).foldLeft(noop[S])((s, i) => for {
        _  <- s
        vi <- a.read(i)
        _  <- if (vi < pv) for {
          vj <- j.read
          _  <- a.swap(i, vj)
          _  <- j.write(vj + 1)
        } yield () else noop[S]
      } yield ())
      x  <- j.read
      _  <- a.swap(x, r)
    } yield x

  def qs[S](a: STArray[S,Int], l: Int, r: Int): ST[S, Unit] = ???

  def quicksort(xs: List[Int]): List[Int] =
    if (xs.isEmpty) xs else ST.runST(new RunnableST[List[Int]] {
      def apply[S] = for {
        arr    <- STArray.fromList(xs)
        size   <- arr.size
        _      <- qs(arr, 0, size - 1)
        sorted <- arr.freeze
      } yield sorted
  })
}

