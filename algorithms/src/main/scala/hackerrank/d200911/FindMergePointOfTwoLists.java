package hackerrank.d200911;

import java.util.Optional;

public class FindMergePointOfTwoLists {

  static class SinglyLinkedListNode {
    int data;
    SinglyLinkedListNode next;
  }

  static Optional<SinglyLinkedListNode> find(SinglyLinkedListNode head, SinglyLinkedListNode lookingFor) {
    if (head == null) return Optional.empty();
    
    SinglyLinkedListNode curr = head;
    while (curr.next != null) {
      if (curr == lookingFor) return Optional.of(curr);
      curr = curr.next;
    }
    return Optional.empty();
  }
  
  static int findMergeNode(SinglyLinkedListNode head1, SinglyLinkedListNode head2) {
    SinglyLinkedListNode curr = head1;
    
    while (curr.next != null) {
      Optional<SinglyLinkedListNode> found = find(head2, curr);
      if (found.isPresent()) return found.get().data;
      curr = curr.next;
    }
    
    throw new IllegalArgumentException("there was a guarantee");
  }

}
