package genrec

import org.apache.avro.generic.GenericRecord
import scala.annotation.tailrec

object syntax {

  implicit class GenericRecordOps(record: GenericRecord) {

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
