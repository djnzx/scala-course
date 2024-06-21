package djnzx.features3

/** this is not related to scala3, but we need to know about that */
object ForwardReferenceIssue extends App {
  val x = y
  val y = 5

  pprint.log(x)
  pprint.log(y)
}
