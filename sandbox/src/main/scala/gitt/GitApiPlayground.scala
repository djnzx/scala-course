package gitt

import java.io.File
import java.util
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GitApiPlayground extends AnyFunSuite with Matchers {

  val git = Git.open(new File("."))
  val repo: Repository = git.getRepository

  test("tag") {
    val x: Ref = git.tag().call()
    pprint.log(x)
  }

  /** GitApiPlayground.scala:24 x: Ref[refs/heads/maste2=0ec5f80d445b5f916e4fdd4e5f5287577cfdcf74(-1)]
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
    // true  when we have uncommited things
    // false when everything is commited
    pprint.log(x.isEmpty)
  }

  test("isDirty") {
    val is_dirty = !git.status.call.getUncommittedChanges.isEmpty
    pprint.log(is_dirty)
  }


  test("HEAD hash") {
    // full hash of the HEAD of the current branch
    // like this: a98211925086b3ff0b2f7433add6047267b0bce5
    val oId: ObjectId = repo.resolve("HEAD")
    pprint.log(oId.name()) // full 160-bit (20-byte)

    val hash7 = repo.newObjectReader.abbreviate(oId).name
    pprint.log(hash7)
  }

}
