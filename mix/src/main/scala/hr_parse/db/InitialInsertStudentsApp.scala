package hr_parse.db

import java.io.File
import hr_parse.db.tables.students
import scala.io.Source
import scala.util.Using

object InitialInsertStudentsApp extends App {

  case class Student(name: String, hacker: Option[String], groupp: Int)
  val fname = "students.txt"
  val file = new File(this.getClass.getClassLoader.getResource(fname).getFile)
  Using(Source.fromFile(file)) { src =>
    src.getLines().toList
  } map { ls =>

    val students: List[Student] = ls.map { s =>
      val items: Array[String] = s.split(";").map(_.trim)
      val hname = items(2)
      Student(items(1), if (hname(0) == '-') None else Some(hname), items(0).toInt)
    }

    val q = new QuillConn
    import q.ctx
    import q.ctx._
    val insert = quote {
      liftQuery(students).foreach { st => query[students].insert(
        _.name   -> st.name,
        _.hacker -> st.hacker,
        _.groupp -> st.groupp
      )}
    }
    ctx.run(insert)
  }
}
