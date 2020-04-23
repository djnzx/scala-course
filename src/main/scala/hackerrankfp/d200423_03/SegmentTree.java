package hackerrankfp.d200423_03;

public class SegmentTree {

  private final Node[] nodes;
  private final int[] values;

  private static class Node {
    final int left, right, min;

    Node(int left, int right, int min) {
      this.left = left;
      this.right = right;
      this.min = min;
    }
  }

  public SegmentTree(int[] values) {
    nodes = new Node[4*values.length];
    this.values = values;
    build(1,0, values.length-1);
  }

  private Node build(int index, int left, int right) {
    if (left == right){
      Node node = new Node(left, right, values[left]);
      nodes[index] = node;
      return node;
    } else {
      int median = (left+right)/2;
      Node l = build(2*index, left, median);
      Node r = build(2*index+1, median+1, right);
      Node node = new Node(left, right, Math.min(l.min, r.min));
      nodes[index] = node;
      return node;
    }
  }

  public int findMin(int left, int right) {
    return findMin(1,left,right);
  }

  private int findMin(int index, int left, int right){
    Node current = nodes[index];
    if(contains(current, left, right)) {
      return current.min;
    }
    else if(intersects(current, left, right)) {
      return Math.min(
          findMin(2*index,left,right),
          findMin(2*index+1,left,right));
    }
    else {
      return Integer.MAX_VALUE;
    }
  }

  private boolean intersects(Node current, int left, int right) {
    return left <= current.left && current.left <= right ||
        current.left <= left && left <= current.right;
  }

  private boolean contains(Node node,int left, int right) {
    return left <= node.left && node.right <= right;
  }
}
