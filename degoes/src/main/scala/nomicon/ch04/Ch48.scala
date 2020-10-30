package nomicon.ch04

import zio._

object Ch48 {

  final case class ApiError(message: String) extends Exception(message) 
  final case class DbError(message: String) extends Exception(message)

  trait Result
  lazy val callApi: ZIO[Any, ApiError, String] = ???
  lazy val queryDb: ZIO[Any, DbError, Int] = ???
 
  /** Scala found the most common type Exception... */
  val combined: ZIO[Any, Exception, (String, Int)] = callApi.zip(queryDb)
  
  final case class InsufficientPermission(user: String, operation: String)
  final case class FileIsLocked(file: String)
  def shareDocument(doc: String): ZIO[Any, InsufficientPermission, Unit] = ???
  def moveDocument(doc: String, folder: String): ZIO[Any, FileIsLocked, Unit] = ???
  
  /** not good */
  val shared1: ZIO[Any, Product, (Unit, Unit)] = shareDocument("347823") &&& moveDocument("347823", "123")
  /** simple example, but good for easy examples */
  val shared2: ZIO[Any, Either[InsufficientPermission, FileIsLocked], (Unit, Unit)] = shareDocument("347823").mapError(Left(_)) &&& moveDocument("347823", "123").mapError(Right(_))
  /** DESIGN YOUR ERROR MODEL!!! */
  
  shared2.tapCause(e => console.putStrLn(e.prettyPrint)) // logging idea

  trait DatabaseError
  trait UserProfile
  def lookupProfile(userId: String): ZIO[Any, DatabaseError, Option[UserProfile]] = ???
  /**
    * smart remapping
    * no error, user exists => [...,           Profile]
    * no error, no user     => [None,          ...   ]
    * error                 => [Some(DbError), ...   ]
    */
  def lookupProfile2(userId: String): ZIO[Any, Option[DatabaseError], UserProfile] =
    lookupProfile(userId).foldM(
      err => ZIO.fail(Some(err)),
      {
        case None         => ZIO.fail(None)
        case Some(profile) => ZIO.succeed(profile)
      } 
    )
  /** or just this way */
  def lookupProfile3(userId: String): ZIO[Any, Option[DatabaseError], UserProfile] = lookupProfile(userId).some

  /**
    * Error behavior does n’t to be something you discover—it can be something you design.
    */
}
