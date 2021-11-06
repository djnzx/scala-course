package interview.soft123.invertlist;

public class Node {
    private final int value;
    private Node next;

    Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }

    Node next() {
        return next;
    }

    void next(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    public String toStringWithTail() {
        StringBuilder sb = new StringBuilder();
        Node current = this;
        while (current != null) {
            sb.append(current.toString());
            current = current.next();
        }
        return sb.toString();
    }

}
