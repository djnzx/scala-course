package genrec

import org.apache.avro.generic.GenericRecord
import scala.annotation.tailrec

object syntax {

  implicit class GenericRecordOps(record: GenericRecord) {

    // TODO: since this is Avro an it has a schema
    //  we can think of detailed error support like:
    //  - field `name` doesn't exist in a given schema on the corresponding layer
    //  - field `name` exists but has a different type ...
    def go[A](path: String)(implicit ex: TypeExtractor[A], gx: TypeExtractor[GenericRecord]): Option[A] = {

      @tailrec
      def navigate(cursor: GenericRecord, path: List[String]): Option[A] = path match {
        case Nil      => None
        case x :: Nil => ex.extract(cursor.get(x))
        case x :: t   =>
          gx.extract(cursor.get(x)) match {
            case Some(r) => navigate(r, t)
            case None    => None
          }
      }

      navigate(record, path.split("\\.").toList)
    }

  }

}
