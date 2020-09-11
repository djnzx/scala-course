package hackerrank.d200911;

import java.util.HashSet;

public class LinkedListsDetectCycle {
  
  static class Node {
    int data;
    Node next;
  }

  boolean hasCycle(Node head) {
    if (head == null) return false;
    
    HashSet<Node> visited = new HashSet<>();
    Node curr = head;
    
    while (curr.next != null) {
      if (visited.contains(curr)) return true;
      visited.add(curr);
      curr = curr.next;
    }
    return false;
  }
  
}
