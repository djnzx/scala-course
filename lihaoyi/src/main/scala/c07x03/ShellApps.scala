package c07x03

import os.{CommandResult, SubProcess}

// p.140
object GitApp extends App {
  // status + Seq[Either[stdout, stderr]]
  // by default - no input, output to result, errors to console 
  val gitStatus   : CommandResult  = os.proc("git", "status").call()
  val exitCode    : Int            = gitStatus.exitCode
  val stdoutText  : String         = gitStatus.out.text()
  val stdoutLines : Vector[String] = gitStatus.out.lines()
  val stderrText  : String         = gitStatus.err.text()

  // spawn sub-process
  val p: SubProcess = os.proc("git", "status").spawn()
  
}

object RemoveNonCurrentGitBranches extends App {
  val gitBranchLines = os.proc("git", "branch").call().out.lines()
//  val otherBranches2 = gitBranchLines.filter(_.startsWith(" ")).map(_.drop(2))
// collect with case like flatmap with Option result  
  val otherBranches = gitBranchLines.collect { case s"  $bname" => bname }
  pprint.log(gitBranchLines)
  pprint.log(otherBranches)
  for (b <- otherBranches) os.proc("git", "branch", "-D", b).call()
}

// redirect output to a file
object CurlToALocalFile extends App {
  val url = "https://api.github.com/repos/lihaoyi/mill/releases"
  // download
  os.proc("curl", url).call(stdout = os.pwd / "github.json")
  // check
  os.proc("ls", "-lh", "github.json").call().out.text()
}

//streaming
object GZip extends App {
  os.proc("gzip")
    .call(stdin = os.pwd / "github.json", stdout = os.pwd / "github.json.gz")
  os.proc("ls", "-lh", "github.json.gz").call().out.text()
}

//pipe
object Interact extends App {
  val sub = os.proc("python", "-u", "-c", "while True: print(eval(raw_input()))").spawn()

  sub.stdin.writeLine("1 + 2 + 4")
  sub.stdin.flush()

  val s1 = sub.stdout.readLine()
  pprint.log(s1)
  
  sub.stdin.writeLine("'1' + '2' + '4'")
  sub.stdin.flush()
  
  val s2 = sub.stdout.readLine()
  pprint.log(s2)
  
  pprint.log(sub.isAlive())
  sub.destroy()
  sub.join()
  pprint.log(sub.isAlive())
  
  
}

object GitDistinctContributors extends App {
  var gitLog = os.proc("git", "log").spawn()
  val grepAuthor = os.proc("grep", "Author: ").spawn(stdin = gitLog.stdout)
  val output = grepAuthor.stdout.lines()
    .distinct
    .collect { case s"Author: $author" => author }
  pprint.log(output)
}

object Streaming1 extends App {
  val download = os
    .proc("curl", "https://api.github.com/repos/lihaoyi/mill/releases")
    .spawn()

  val base64 = os
    .proc("base64")
    .spawn(stdin = download.stdout)

  val gzip = os
    .proc("gzip")
    .spawn(stdin = base64.stdout)
  
  val upload = os
    .proc("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything")
    .spawn(stdin = gzip.stdout)

  val contentLength = upload.stdout.lines()
    .filter(_.contains("Content-Length"))
  
  pprint.log(contentLength)
}

