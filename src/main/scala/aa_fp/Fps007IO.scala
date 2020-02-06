package aa_fp

import java.io.{BufferedReader, BufferedWriter, File, FileReader, FileWriter}

import scala.io.Source
import scala.util.Using

object Fps007IO extends App {
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
    Using(new BufferedReader(new FileReader("file.txt"))) { br =>
      Iterator.unfold(())(_ => Option(br.readLine()).map(_ -> ())).toList
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

  val file = () => new BufferedReader(new FileReader("file.txt"))

  val lines3: Seq[String] =
    Using.resource(file()) { br =>
      Iterator.unfold(())(_ => Option(br.readLine()).map(_ -> ())).toList
    }

  val contents: Try[List[String]] =
    Using(file()) { br =>
      Iterator.unfold(())(_ => Option(br.readLine()).map(_ -> ())).toList
    }

  def writeFile(filename: String, lines: Seq[String]): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    for (line <- lines) {
      bw.write(line)
    }
    bw.close()
  }

  def writeFile2(filename: String, lines: Seq[String]): Try[Seq[Unit]] =
    Using(new BufferedWriter(new FileWriter(new File(filename)))) { bw =>
      lines.map(l => bw.write(l))
    }

}
