package interview.soft123.invertlist;

public class InvertNode {

    public static Node invert(Node item) {
        Node prev = null;
        Node current = item;
        while (current!=null) {
            Node saveNext=current.next();
            current.next(prev);
            prev = current;
            current = saveNext;
        }
        return prev;
    }

    public static void main(String[] args) {
        Node list_original =
            new Node(1,
                new Node(2,
                    new Node(3,
                        new Node(4,
                            null
                        )
                    )
                )
            );

        System.out.printf("orig:%s\n",
            list_original.toStringWithTail()
        );
        Node list_inverted = invert(list_original);
        System.out.printf("invt:%s\n",
            list_inverted.toStringWithTail()
        );
    }
}
