package hackerrank.d200911

/**
  * https://www.hackerrank.com/challenges/reverse-a-doubly-linked-list/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=linked-lists
  */
object ReverseDoublyLinkedListApp {

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

  def reverse(head: DoublyLinkedListNode): DoublyLinkedListNode = {
    type Node = DoublyLinkedListNode
    
    if (head == null || head.next == null) {
      head
    } else {
      
      var curr = head
      var prev: Node = null
      
      while (curr.next != null) {
        val savedNext = curr.next
        
        curr.next = prev
        curr.prev = savedNext
        
        prev = curr
        curr = savedNext
      }
      
      curr.next = prev
      curr.prev = null
      curr
    }
  }

}
