package catsx.c114reader

import cats.implicits._
import cats.{Id, Monad}

object Reader6MockingApiAndTestingBusinessLogic extends App {
  
  /** state definition */
  case class MyState(a: Long, b: String)

  /** API definition, can be Future[_], can be IO[_], whatever*/
  trait API[F[_]] {
    def obtainData(key: Long): F[MyState]
    def process(p: MyState): F[MyState]
  }

  /** business logic definition */
  trait BusinessLogic[F[_]] {
    def initial: F[MyState]
    def process(st: MyState): F[MyState]
    def act(st: MyState): F[MyState]
  }

  /** one of the many possible business logic implementation */
  class BusinessLogicImpl[F[_]](api: API[F])(implicit F: Monad[F]) extends BusinessLogic[F] {

    override def initial: F[MyState] = api.obtainData(1L)

    override def process(st: MyState): F[MyState] = for {
      st2 <- api.process(st)
    } yield st2.copy(a = st2.a + 1)

    override def act(st: MyState): F[MyState] = for {
      _ <- F.pure(println(st))
    } yield st.copy(b = st.b + "+")

  }

  /** we can mock our API */
  val apiMocked = new API[Id] {
    override def obtainData(key: Long): Id[MyState] = MyState(key, "empty")
    override def process(p: MyState): Id[MyState] = p.copy(a = p.a + 100)
  }

  /** feeding our business login with mocked API */
  val business: BusinessLogic[Id] = new BusinessLogicImpl(apiMocked)

  /** and write the tests */
  val s0 = business.initial
  assert(s0 == MyState(1L, "empty"))

  val s1 = business.process(s0)
  assert(s1 == MyState(102L, "empty")) // +100 by API and +1 by BusinessLogin

  val s2 = business.act(s1) // it does println also
  assert(s2 == MyState(102L, "empty+"))

}
