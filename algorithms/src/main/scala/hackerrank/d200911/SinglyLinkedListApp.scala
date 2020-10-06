package hackerrank.d200911

import java.io.PrintWriter

/**
  * https://www.hackerrank.com/challenges/insert-a-node-at-a-specific-position-in-a-linked-list/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=linked-lists
  */
object SinglyLinkedListApp {
  class SinglyLinkedListNode(var data: Int, var next: SinglyLinkedListNode = null) {
  }

  class SinglyLinkedList(var head: SinglyLinkedListNode = null, var tail: SinglyLinkedListNode = null) {
    def insertNode(nodeData: Int) = {
      val node = new SinglyLinkedListNode(nodeData)

      if (this.head == null) {
        this.head = node
      } else {
        this.tail.next = node
      }

      this.tail = node
    }
  }

  def printSinglyLinkedList(head: SinglyLinkedListNode, sep: String, printWriter: PrintWriter) = {
    var node = head

    while (node != null) {
      printWriter.print(node.data)

      node = node.next

      if (node != null) {
        printWriter.print(sep)
      }
    }
  }

  def insertNodeAtPosition(llist: SinglyLinkedListNode, data: Int, position: Int): SinglyLinkedListNode = {
    type Node = SinglyLinkedListNode

    if (position == 0) {
      new Node(data, llist)
    } else {
      var pos = position
      var curr = llist
      while (pos > 1) {
        curr = curr.next
        pos -= 1
      }
      val node = new Node(data, curr.next)
      curr.next = node

      llist
    }
  }
    
}
