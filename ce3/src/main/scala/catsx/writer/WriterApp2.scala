package catsx.writer

import cats.Id
import cats.data.Writer
import cats.data.WriterT
import pprint.pprintln

object WriterApp2 extends App {

  val x1 = Writer.apply(List.empty[String], 0)

  val x2: WriterT[Id, List[String], Int] = x1.bimap(_.appended("line1"), _ + 1)

  val x3: WriterT[Id, List[String], Int] = x2.bimap(_.appended("line2"), _ * 2)

  pprintln(x3.run)

}
