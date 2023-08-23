package macros

import scala.language.experimental.macros
import scala.reflect.ClassTag

object Instances {

  trait EventProcessor[Event] {
    def addHandler[E <: Event: ClassTag](
        handler: E => Unit
    ): EventProcessor[Event]

    def process(event: Event)
  }

  sealed trait UserEvent
  final case class UserCreated(name: String, email: String) extends UserEvent
  sealed trait UserChanged                                  extends UserEvent
  final case class NameChanged(name: String)                extends UserChanged
  final case class EmailChanged(email: String)              extends UserChanged
  case object UserDeleted                                   extends UserEvent

  type Handler[Event] = (_ <: Event) => Unit

  case class EventProcessorImpl[Event](
    handlers: Map[Class[_ <: Event], List[Handler[Event]]] =
    Map[Class[_ <: Event], List[Handler[Event]]]()
  ) extends EventProcessor[Event] {

    override def addHandler[E <: Event: ClassTag](
      handler: E => Unit
    ): EventProcessor[Event] = {
      val eventClass = implicitly[ClassTag[E]].runtimeClass.asInstanceOf[Class[_ <: Event]]
      val eventHandlers = handler
        .asInstanceOf[Handler[Event]] :: handlers.getOrElse(eventClass, List())
      copy(handlers + (eventClass -> eventHandlers))
    }

    override def process(event: Event): Unit = {
      handlers
        .get(event.getClass)
        .foreach(_.foreach(_.asInstanceOf[Event => Unit].apply(event)))
    }
  }

//  val handler = new EventProcessorImpl[UserEvent]
//  val processor = EventProcessor[UserEvent]
//    .addHandler[UserCreated](handler)
//    .addHandler[NameChanged](handler)
//    .addHandler[EmailChanged](handler)
//    .addHandler[UserDeleted.type](handler)
//
//
//
//
//  val handler = new EventHandlerImpl[UserEvent]
//  val processor = EventProcessor[UserEvent].addHandler[UserEvent](handler)






  //  def find[A] = macro find_impl[A]
//
//  def find_impl[A: c.WeakTypeTag](c: blackbox.Context): List[c.universe.Symbol] = {
//    import c.universe._
//
//    def subclasses(symbol: c.universe.Symbol): List[c.universe.Symbol] = {
//
//      val symbol   = weakTypeOf[A].typeSymbol
//      val children = symbol.asClass.knownDirectSubclasses.toList
//
//      symbol :: children.flatMap(x => subclasses(x))
//    }
//
//    val symbol: c.universe.Symbol         = weakTypeOf[A].typeSymbol
//    val children: List[c.universe.Symbol] = subclasses(symbol)
//    children
//  }

}
