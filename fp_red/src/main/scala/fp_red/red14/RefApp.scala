package fp_red.red14

object RefApp extends App {
  /** no way to construct Nothing => no way to run */
  val s: ST[Nothing, (Int, Int)] =
    for {
      r1 <- STRef[Nothing,Int](10)
      r2 <- STRef[Nothing,Int](1)
      x <- r1.read
      y <- r2.read
      _ <- r1.write(y+1)
      _ <- r2.write(x+1)
      a <- r1.read
      b <- r2.read
    } yield (a,b) // (2, 11)

  val x: ST[Nothing, STRef[Nothing, Int]] = STRef(10)
  
  val rst: RunnableST[(Int, Int)] = new RunnableST[(Int, Int)] {
    override def apply[S]: ST[S, (Int, Int)] = for {
      r1 <- STRef(10)
      r2 <- STRef(1)
      x <- r1.read
      y <- r2.read
      _ <- r1.write(y+1)
      _ <- r2.write(x+1)
      a <- r1.read
      b <- r2.read
    } yield (a, b)
  }
  
  val r: (Int, Int) = ST.runST(rst)
  println(r)

  val ref: STRef[_, Int] = ST.runST(new RunnableST[STRef[_, Int]] {
    override def apply[S]: ST[S, STRef[_, Int]] =  for {
      r1 <- STRef(1)
    } yield r1
  })
  
//  new RunnableST[Int] {
//    def apply[R] = for { x <- ref.read } yield x
//  }
}
