package aa_fp

import java.io.{BufferedReader, FileReader}

import scala.io.{BufferedSource, Source}
import scala.util.Using

/**
  * https://docs.scala-lang.org/overviews/collections-2.13/overview.html
  */
object Fps005Collection extends App {
  val im_list1: List[Int] = List(1,2,3)
  val im_list2: Seq[Int] = Seq(1,2,3) // but actually it will be a list

  val it: Iterator[String] = Source.fromFile("1.txt").getLines()

  val filename = "fileopen.scala"
  for (line <- Source.fromFile(filename).getLines) {
    println(line)
  }
//  val lines = Source.fromFile("/Users/Al/.bash_profile").getLines.toList

  val bufferedSource = Source.fromFile("example.txt")
  for (line <- bufferedSource.getLines) {
    println(line.toUpperCase)
  }

  bufferedSource.close

  Using

  val source = scala.io.Source.fromFile("file.txt")
  val lines = try source.mkString finally source.close()

  import scala.io.{BufferedSource, Codec, Source}
  import scala.util.Try

  def readFileUtf8(path: String): Try[String] = Try {
    val source: BufferedSource = Source.fromFile(path)(Codec.UTF8)
    val content = source.mkString
    source.close()
    content
  }

  /**
    * https://github.com/pathikrit/better-files#streams-and-codecs
    */
  val lines0: Try[Seq[String]] =
    Using(new BufferedReader(new FileReader("file.txt"))) { reader =>
      Iterator.unfold(())(_ => Option(reader.readLine()).map(_ -> ())).toList
    }

  val lines2: Try[Seq[String]] = Using.Manager { use =>
    val r1 = use(new BufferedReader(new FileReader("file1.txt")))
    val r2 = use(new BufferedReader(new FileReader("file2.txt")))
    val r3 = use(new BufferedReader(new FileReader("file3.txt")))
    val r4: BufferedReader = use(new BufferedReader(new FileReader("file4.txt")))
    // use your resources here
    def lines(reader: BufferedReader): Iterator[String] =
      Iterator.unfold(())(_ => Option(reader.readLine()).map(_ -> ()))

    (lines(r1) ++ lines(r2) ++ lines(r3) ++ lines(r4)).toList
  }

  val lines3: Seq[String] =
    Using.resource(new BufferedReader(new FileReader("file.txt"))) { reader =>
      Iterator.unfold(())(_ => Option(reader.readLine()).map(_ -> ())).toList
    }


}
