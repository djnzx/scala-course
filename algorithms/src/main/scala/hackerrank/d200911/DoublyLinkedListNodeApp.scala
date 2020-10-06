package hackerrank.d200911

/**
  * https://www.hackerrank.com/challenges/insert-a-node-into-a-sorted-doubly-linked-list/problem
  * Sorted
  * Double Linked List
  * Without duplicates
  */
object DoublyLinkedListNodeApp extends App {

  class DoublyLinkedListNode(var data: Int, var next: DoublyLinkedListNode = null, var prev: DoublyLinkedListNode = null) {
  }

  class DoublyLinkedList(var head: DoublyLinkedListNode = null, var tail: DoublyLinkedListNode = null) {
    def insertNode(nodeData: Int) = {
      val node = new DoublyLinkedListNode(nodeData)

      if (this.head == null) {
        this.head = node
      } else {
        this.tail.next = node
        node.prev = this.tail
      }

      this.tail = node
    }
  }

  def printDoublyLinkedList(head: DoublyLinkedListNode, sep: String) = {
    var node = head

    while (node != null) {
      print(node.data)

      node = node.next

      if (node != null) {
        print(sep)
      }
    }
  }

  def sortedInsert(head: DoublyLinkedListNode, data: Int): DoublyLinkedListNode = {
    type Node = DoublyLinkedListNode
    if (head == null) {
      // empty list, just add
      new Node(data)
    } else if (data < head.data) {
      // non-empty, definitely, the first element
      val first = new Node(data, head)
      head.prev = first

      first
    } else {
      // not first, move on
      var curr = head
      while (curr.next != null && curr.next.data < data) {
        curr = curr.next
      }
      if (curr.data == data) return head // check for duplicate
      if (curr.next == null) {
        // last element
        val node = new Node(data, null, curr)
        curr.next = node
      } else {
        // in the middle
        val node = new Node(data, curr.next, curr)
        curr.next = node
        curr.next.prev = node
      }

      head
    }
  }

  printDoublyLinkedList(
    sortedInsert(null, 5)
    ," "
  )

}
