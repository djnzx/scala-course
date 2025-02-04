package gitt

import java.io.File
import org.eclipse.jgit.api.{Git, Status}
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.util

class GitApiPlayground extends AnyFunSuite with Matchers {

  val git = Git.open(new File("."))
  val repo: Repository = git.getRepository

  test("tag") {
    val x: Ref = git.tag().call()
    pprint.log(x)
  }

  /**
   * GitApiPlayground.scala:24 x: Ref[refs/heads/maste2=0ec5f80d445b5f916e4fdd4e5f5287577cfdcf74(-1)]
   * GitApiPlayground.scala:24 x: Ref[refs/heads/master=0ec5f80d445b5f916e4fdd4e5f5287577cfdcf74(-1)]
   */
  test("list of branches with their HEAD commit hashes") {
    val xs = git.branchList().call()
    xs.forEach { x: Ref =>
      pprint.log(x)
    }
  }

  // by default `git.describe` returns null
  // but if we have
  //  git tag -a v13 -m "v13-description"
  // we will get "v13" as a value
  //  if we have a tag somewhere in the git tree,
  // we will have message like "v12-4-gdc7c17b3"
  // where:
  // `v12`       - is the most recent annotated tag
  // `4`         - how many commits AFTER this tag
  // `gdc7c17b3` - current hash
  // so we can generate good version number
  test("git.describe") {
    val x = git.describe.call
    pprint.log(x)
  }

  test("git.status") {
    // set of files with uncommited changes
    // GitApiPlayground.scala:49 x.getUncommittedChanges: [sandbox/src/main/scala/gitt/GitApiPlayground.scala]
    val x: util.Set[String] = git.status.call.getUncommittedChanges
    pprint.log(x.isEmpty)
  }

}
