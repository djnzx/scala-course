package catsx.reader

import cats.{Id, Monad}
import cats.implicits._

object BusinessLogicExplainApp {
  
  case class AppState(a: Long, b: String)
  
  trait Api[F[_]] {
    def obtainData(key: Long): F[AppState]
    def process(p: AppState): F[AppState]
  }
  
  trait BusinessLogic[F[_]] {
    def initial: F[AppState]
    def process(st: AppState): F[AppState]
    def act(st: AppState): F[AppState]
  }
  
  class BusinessLogicImpl[F[_]](api: Api[F])(implicit F: Monad[F]) extends BusinessLogic[F] {

    override def initial: F[AppState] = api.obtainData(1L)

    override def process(st: AppState): F[AppState] = for {
      st2 <- api.process(st)
    } yield st2.copy(a = st2.a + 1)

    override def act(st: AppState): F[AppState] = for {
      _ <- F.pure(println(st)) 
    } yield st.copy(b = st.b + "+") 
    
  }
  
  val apiMocked = new Api[Id] {
    override def obtainData(key: Long): Id[AppState] = AppState(key, "empty")
    override def process(p: AppState): Id[AppState] = p.copy(a = p.a + 100)
  }
  
  val mid: Monad[Id] = implicitly(Monad[Id])
  
  val business = new BusinessLogicImpl(apiMocked)
  val r: AppState = business.initial
}
