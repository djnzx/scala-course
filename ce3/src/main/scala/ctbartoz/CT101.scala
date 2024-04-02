package ctbartoz

import cats.implicits._

object CT101 extends App {

  /** https://www.youtube.com/playlist?list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_
    * 1.1  https://www.youtube.com/watch?v=I8LbkfSSR58&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=1
    * 1.2  https://www.youtube.com/watch?v=p54Hd7AmVFU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=2
    * 2.1  https://www.youtube.com/watch?v=O2lZkr-aAqk&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=3
    * 2.2  https://www.youtube.com/watch?v=NcT7CGPICzo&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=4
    * 3.1  https://www.youtube.com/watch?v=aZjhqkD6k6w&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=5
    * 3.2  https://www.youtube.com/watch?v=i9CU4CuHADQ&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=6
    * 4.1  https://www.youtube.com/watch?v=zer1aFgj4aU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=7
    * 4.2  https://www.youtube.com/watch?v=Bsdl_NKbNnU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=8
    * 5.1  https://www.youtube.com/watch?v=LkIRsNj9T-8&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=10
    * 5.2  https://www.youtube.com/watch?v=w1WMykh7AxA&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=10
    * 6.1  https://www.youtube.com/watch?v=FyoQjkwsy7o&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=11
    * 6.2  https://www.youtube.com/watch?v=EO86S2EZssc&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=12
    * 7.1  https://www.youtube.com/watch?v=pUQ0mmbIdxs&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=13
    * 7.2  https://www.youtube.com/watch?v=wtIKd8AhJOc&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=14
    * 8.1  https://www.youtube.com/watch?v=REqRzMI26Nw&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=15
    * 8.2  https://www.youtube.com/watch?v=iXZR1v3YN-8&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=16
    * - 9.1  https://www.youtube.com/watch?v=2LJC-XD5Ffo&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=17
    * - 9.2  https://www.youtube.com/watch?v=wrpxBXXgLCI&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=18
    * - 10.1 https://www.youtube.com/watch?v=gHiyzctYqZ0&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=19
    * - 10.2 https://www.youtube.com/watch?v=GmgoPd7VQ9Q&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=20
    */

  class Associativity[A, B, C, D](
      f: A => B,
      g: B => C,
      h: C => D) {

    val composition1: A => D = (f andThen g) andThen h
    val composition2: A => D = f andThen (g andThen h)
  }

  object FlattenVsUnite {
    val xs: List[Vector[Int]] = List(Vector(1, 2), Vector(3, 4))
    // Alternative = Applicative + SemigroupK
    val r2: List[Int]         = xs.unite   // implicit FlatMap[F], Alternative[F], Foldable[G] for any F[G[A]]
    val r1: List[Int]         = xs.flatten // implicit iterableOnce: A => IterableOnce[B]
  }

  object RepackFutures {
    import scala.concurrent.{ExecutionContext, Future}

    def repack[A](fs: List[Future[A]])(implicit ec: ExecutionContext): Future[(List[Throwable], List[A])] = {
      def refine(fa: Future[A]): Future[Either[Throwable, A]] = fa.map(_.asRight).recover(_.asLeft)

      fs.map(refine)
        .sequence
        .map(_.separate)
    }

  }

}
