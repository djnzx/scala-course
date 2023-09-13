package lessons.typeclasses

/**
  * https://gist.github.com/aaronlevin/d3911ba50d8f5253c85d2c726c63947b
  */
object Events {
  // Our events
  case class Click(user: String, page: String)
  case class Play(user: String, trackId: Long)
  case class Pause(user: String, trackId: Long, ts: Long)
  // A type alias for "end of the type-level list"
  type EndOfList = Unit
  // list of events
  type Events = (Click, (Play, (Pause, EndOfList)))

  // A typeclass to extract names from a list of events.
  trait Named[E] {
    val name: String
  }
  // instances of Named for our events
  implicit val namedClick = new Named[Click] { val name: String = "click" }
  implicit val namedPlay  = new Named[Play]  { val name: String = "play"  }
  implicit val namedPause = new Named[Pause] { val name: String = "pause" }
  // Named: base case
  implicit val base = new Named[EndOfList] { val name: String = "" }
  // Named induction step: (E, Tail)
  implicit def step[Head,Tail](implicit n: Named[Head], tailNames: Named[Tail]
    ): Named[(Head, Tail)] = new Named[(Head,Tail)] {
    val name: String = s"${n.name}, ${tailNames.name}"
  }
  // helper
  def getNamed[E](implicit names: Named[E]): String = names.name
  /****************************************************************************
    **************** Part 2: Parsing Events / Dynamic Dispatch *****************
    ***************************************************************************/
  // A Typeclass for dynamic-dispatch on events
  trait HandleEvents[Events] {
    type Out
    def handleEvent(eventName: String, payload: String): Either[String, Out]
  }




  // HandleEvents: base case
  implicit val baseCaseHandleEvents = new HandleEvents[EndOfList] {
    type Out = Nothing
    def handleEvent(eventName: String, payload: String) = Left(s"Did not find event $eventName")
  }




  // A typeclass for types that can be parsed from strings.
  trait FromString[E] {
    def fromString(s: String): Option[E]
  }




  // Parser instances for our types.
  implicit val clickFromstring = new FromString[Click] {
    def fromString(s: String) = s.split('\t').toList match {
      case user :: track :: Nil => Some(Click(user, track))
      case _ => None
    }}

  // A small helper
  def safeToLong(s: String): Option[Long] = try { Some(s.toLong) } catch { case _: java.lang.NumberFormatException => None }

  implicit val playFromString = new FromString[Play] {
    def fromString(s: String) = s.split('\t').toList match {
      case user :: track :: Nil => safeToLong(track).map(Play(user,_))
      case _ => None
    }}

  implicit val pauseFromString = new FromString[Pause] {
    def fromString(s: String) = s.split('\t').toList match {
      case user :: track :: ts :: Nil => safeToLong(track).flatMap { t => safeToLong(ts).map(Pause(user, t,_)) }
      case _ => None
    }}




  // HandleEvents: induction step (E, Tail)
  implicit def inductionStepHandleEvents[E, Tail](
                                                   implicit
                                                   namedEvent: Named[E],
                                                   fromString: FromString[E],
                                                   tailHandles: HandleEvents[Tail]
                                                 ): HandleEvents[(E, Tail)] {type Out = Either[tailHandles.Out, E]} = new HandleEvents[(E, Tail)] {

    type Out = Either[tailHandles.Out, E]

    def handleEvent(eventName: String, payload: String): Either[String, Out] = {
      if(eventName == namedEvent.name) {
        fromString.fromString(payload) match {
          case None => Left(s"""Could not decode event "$eventName" with payload "$payload"""")
          case Some(e) => Right(Right(e))
        }
      } else {
        tailHandles.handleEvent(eventName, payload) match {
          case Left(e) => Left(e)
          case Right(e) => Right(Left(e))
        }
      }
    }
  }




  // Helper.
  def handleEvent[Events](eventName: String, payload: String)(
    implicit
    names: HandleEvents[Events]
  ): Either[String, names.Out] = names.handleEvent(eventName, payload)


  /****************************************************************************
    **************** Part 3: Putting it all together ***************************
    ***************************************************************************/

  def main(args: Array[String]): EndOfList = {
    println(s"""\ntype Events = (Click, (Play, (Pause, EndOfList)))\n""")
    println(s"1. event names:\n\t getNamed[Events] = ${getNamed[Events]}\n")
    println(s"""2. dynamic dispatch on click:\n\thandleEvent[Events]("click", "lambdaworld\\tpage/rules") = ${handleEvent[Events]("click", "lambdaworld\tpage/rules")}\n""")
    println(s"""3. dynamic dispatch play:\n\thandleEvent[Events]("play", "lambdaworld\\t123") = ${handleEvent[Events]("play", "lambdaworld\t123")}\n""")
    println(s"""4. dynamic dispatch pause:\n\thandleEvent[Events]("pause", "lambdaworld\\t123\\t456") = ${handleEvent[Events]("pause", "lambdaworld\t123\t456")}\n""")
    println(s"""5. dynamic dispatch (wrong payload):\n\thandleEvents[Events]("play", "lambdaworld\\tnotanumber") = ${handleEvent[Events]("play", "lambdaworld\tnotanumber")}\n""")
    println(s"""6. dynamic dispatch (wrong event):\n\thandleEvents[Events]("lambda-world", "lambdaworld\\t123") = ${handleEvent[Events]("lambda-world", "lambdaworld\t123")}\n""")
  }
}
