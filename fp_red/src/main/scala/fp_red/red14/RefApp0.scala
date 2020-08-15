package fp_red.red14

object RefApp0 extends App {
  /**
    * creating STRef without the context leads to 
    * necessity of specifying `Nothing` and creating `STRef[Nothing, A]`
    * therefore:
    * - no way to construct Nothing,
    * - no way to wrap into RunnableST
    * - no way to run
    * - we CAN control the scope of using mutable state on compiler level!!!
    */ 
  val st0: ST[Nothing, (Int, Int)] = for {
    r1 <- STRef[Nothing, Int](10) // STRef[Nothing, Int]
    r2 <- STRef[Nothing, Int](1)
    x <- r1.read
    y <- r2.read
    _ <- r1.write(y+1)
    _ <- r2.write(x+1)
    a <- r1.read
    b <- r2.read
  } yield (a, b) // (2, 11)

  /**
    * proper wrapping gives ability to run
    */
  val rst: RunnableST[(Int, Int)] = new RunnableST[(Int, Int)] {
    override def apply[S]: ST[S, (Int, Int)] = for {
      r1 <- STRef(10) // STRef[S, Int]
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
  pprint.log(r)

}
