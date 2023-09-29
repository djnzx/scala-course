package git_api

import cats.data.EitherT

import java.io.File
import java.time.{LocalDateTime, OffsetDateTime}
import sys.process.Process
import sys.process.ProcessBuilder
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Git(baseDir: File) {
  import Git._

  type P = ProcessBuilder
  val cmdName = "git"

  def exeName(command: String): String = {
    val maybeOsName    = sys.props.get("os.name").map(_.toLowerCase)
    val maybeIsWindows = maybeOsName.filter(_.contains("windows"))
    maybeIsWindows.map(_ => command + ".exe").getOrElse(command)
  }

  private lazy val exec = exeName(cmdName)

  def cmd(args: Any*): P = Process(exec +: args.map(_.toString), baseDir)

  def add(files: Seq[String]): P = cmd(("add" +: files): _*)

  def add(file: String, files: String*): P = add(file +: files)

  def revParse(name: String): String = cmd("rev-parse", name).!!.trim

  def currentBranch: String = cmd("symbolic-ref", "HEAD").!!.trim.stripPrefix("refs/heads/")

  def currentHash: String = revParse("HEAD")

  def currentHash(len: Int): String = currentHash.take(len)


  def commitList(): LazyList[Commit] =
    cmd("log", "--pretty=format:%h : %an : %ae :  %cI : %s", "--date=iso8601").lazyLines
      .map(_.split(" : ").map(_.trim))
      .collect { case Array(a, b, c, d, e) => Commit(a, b, c, OffsetDateTime.parse(d), e) }

  def commitList(n: Int): LazyList[Commit] = commitList.take(n)

  def tags: Iterable[String] = ???

  def commit(message: String) = ???
}

object Git {

  case class Commit(hash: String, user: String, email: String, date: OffsetDateTime, comment: String)

  def isRepository(dir: File): Option[File] = Option.when(new File(dir, ".git").exists)(dir)
  def detect(dir: File): Option[Git]        = isRepository(dir).map(apply)
  def detectUnsafe(dir: File): Git          = detect(dir).getOrElse(sys.error("Git Repository expected"))

  def apply(baseDir: File): Git   = new Git(baseDir)
  def apply(baseDir: String): Git = apply(new File(baseDir))
  def apply(): Git                = apply(".")
}

object GitApi extends App {
  val git = Git()
  println(git.currentBranch)
  println(git.currentHash(8))
  git.commitList(5)
    .foreach(pprint.pprintln(_))

}
